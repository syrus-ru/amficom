package com.syrus.AMFICOM.Client.Resource.Alarm;

import com.syrus.AMFICOM.CORBA.Alarm.Alarm_Transferable;
import com.syrus.AMFICOM.CORBA.General.AlarmStatus;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.PropertiesPanel;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.General.Filter.ObjectResourceFilter;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceSorter;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Result.Evaluation;
import com.syrus.AMFICOM.Client.Resource.Result.Result;
import com.syrus.AMFICOM.Client.Resource.Result.Test;
import com.syrus.AMFICOM.Client.Survey.General.ConstStorage;

import java.io.*;

import java.util.*;

public class Alarm extends ObjectResource implements Serializable
{

	private static final long serialVersionUID = 01L;

	public static final String typ = "alarm";
	public long assigned;
	public String assigned_to;
	public String comments;
	public String event_id;
	public String fixed_by;
	public long fixed_when;
	public long generated;

	public String id;
	public long modified;
	public String source_id;
	public AlarmStatus status;
	Alarm_Transferable transferable;
	public String type_id;

	//	Message_Transferable message;

	public Alarm()
	{
		super(typ);
	}

	public Alarm(Alarm_Transferable transferable)
	{
		super(typ);
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public static Alarm getAlarmByTestResult(String result_id)
	{
		Result res = (Result )Pool.get(Result.typ, result_id);
		if(res == null)
			return null;
		Test test = (Test )Pool.get(Test.typ, res.getActionId());
		if(test == null)
			return null;
		int res_index;
		for(res_index = 0; res_index < test.result_ids.length; res_index++)
			if(test.result_ids[res_index].equals(result_id))
				break;
		if(res_index == test.result_ids.length)
			return null;

		Hashtable ht = Pool.getHash(Alarm.typ);
		if(ht == null)
			return null;

		for(Enumeration e = ht.elements(); e.hasMoreElements();)
		{
			Alarm a = (Alarm )e.nextElement();
			SystemEvent event = (SystemEvent )Pool.get(SystemEvent.typ, a.event_id);
			if(event == null)
				continue;
			Result rrr = (Result )Pool.get(Result.typ, event.descriptor);
			if(rrr == null)
				continue;
			Evaluation eval = (Evaluation )Pool.get(Evaluation.typ, rrr
					.getActionId());
			if(eval == null)
				continue;
			if(test.evaluation_id.equals(rrr.getActionId()))
			{
				if(eval.getResultIds().length > res_index)
					if(eval.getResultIds()[res_index].equals(event.descriptor))
						return a;
			}
		}
		return null;
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new com.syrus.AMFICOM.Client.Survey.Alarm.UI.AlarmDisplayModel();
	}

	public static ObjectResourceSorter getDefaultSorter()
	{
		return new AlarmTimeSorter();
	}

	public static ObjectResourceFilter getFilter()
	{
		return new com.syrus.AMFICOM.Client.Survey.Alarm.Filter.AlarmFilter();
	}

	public static PropertiesPanel getPropertyPane()
	{
		return new GeneralPanel();
	}

	public static ObjectResourceSorter getSorter()
	{
		return new com.syrus.AMFICOM.Client.Survey.Alarm.Sorter.AlarmSorter();
	}

	public ObjectResource getDescriptor()
	{
		try
		{
			SystemEvent event = (SystemEvent )Pool.get(SystemEvent.typ, this.event_id);
			if(event.type_id.equals("evaluationalarmevent"))
			{
				Evaluation ev = (Evaluation )Pool.get(Evaluation.typ,
						event.descriptor);
				return ev;
			} else
				if(event.type_id.equals("testalarmevent")
						|| event.type_id.equals("testwarningevent"))
				{
					Test t = (Test )Pool.get(Test.typ, event.descriptor);
					return t;
				}

			return null;
		} catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}

	public String getDomainId()
	{
		return ConstStorage.SYS_DOMAIN;
	}

	public ObjectResource getElementaryTest()
	{
		try
		{
			SystemEvent event = (SystemEvent )Pool.get(SystemEvent.typ, event_id);
			if(event.type_id.equals("evaluationalarmevent"))
			{
				/*
				 * Evaluation ev = (Evaluation )Pool.get("evaluation",
				 * event.descriptor); Result result = (Result
				 * )Pool.get("result", ev.result_id);
				 * if(!result.result_type.equals("test")) return null; Test t =
				 * (Test )Pool.get("test", result.test_id); return t;
				 */
				return null;
			} else
				if(event.type_id.equals("testalarmevent")
						|| event.type_id.equals("testwarningevent"))
				{
					Test t = (Test )Pool.get("test", event.descriptor);
					return t;
				}

			return null;
		} catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}

	}

	public Evaluation getEvaluation()
	{
		SystemEvent event = (SystemEvent )Pool.get(SystemEvent.typ, event_id);
		Result rrr = (Result )Pool.get(Result.typ, event.descriptor);
		Evaluation eval = (Evaluation )Pool
				.get(Evaluation.typ, rrr.getActionId());
		return eval;
	}

	public SystemEvent getEvent()
	{
		return (SystemEvent )Pool.get(SystemEvent.typ, event_id);
	}

	public String getId()
	{
		return id;
	}

	public ObjectResourceModel getModel()
	{
		return new AlarmModel(this);
	}

	public long getModified()
	{
		return modified;
	}

	public String getMonitoredElementId()
	{
		try
		{
			SystemEvent event = (SystemEvent )Pool.get(SystemEvent.typ, event_id);
			if(event.type_id.equals("evaluationalarmevent"))
			{
				return "";
			} else
				if(event.type_id.equals("testalarmevent")
						|| event.type_id.equals("testwarningevent"))
				{
					Result res = (Result )Pool
							.get(Result.typ, event.descriptor);
					if(res == null)
						return "";
					if(res.getResultType().equals(Test.typ))
					{
						Test test = (Test )Pool
								.get(Test.typ, res.getActionId());
						if(test == null)
							return "";
						return test.monitored_element_id;
					}
					if(res.getResultType().equals(Evaluation.typ))
					{
						Evaluation evaluation = (Evaluation )Pool.get(
								Evaluation.typ, res.getActionId());
						if(evaluation == null)
							return "";
						return evaluation.getMonitoredElementId();
					}
					return "";
				}

			return "";
		} catch(Exception ex)
		{
			ex.printStackTrace();
			return "";
		}

	}

	public String getName()
	{
		String s = Pool.getName(AlarmType.typ, type_id);

		return ConstStorage.SIMPLE_DATE_FORMAT.format(new Date(generated))
				+ " [" + s + "]";
	}

	public ObjectResource getResult()
	{
		try
		{
			SystemEvent event = (SystemEvent )Pool.get("event", event_id);
			if(event.type_id.equals("evaluationalarmevent"))
			{
				for(Enumeration enum = Pool.getHash(Result.typ).elements(); enum
						.hasMoreElements();)
				{
					Result r = (Result )enum.nextElement();
					if(r.getResultType().equals("evaluation")
							&& r.getId().equals(event.descriptor))
						return r;
				}
				return null;
			} else
				if(event.type_id.equals("testalarmevent")
						|| event.type_id.equals("testwarningevent"))
				{
					for(Enumeration enum = Pool.getHash("result").elements(); enum
							.hasMoreElements();)
					{
						Result r = (Result )enum.nextElement();
						if(r.getResultType().equals("evaluation")
								&& r.getId().equals(event.descriptor))
							return r;
					}
					return null;
				}

			return null;
		} catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}

	}

	public String getSourceId()
	{
		try
		{
			SystemEvent event = (SystemEvent )Pool.get("event", event_id);
			if(event != null)
				return event.source_id;
		} catch(Exception ex)
		{
			return null;
		}
		return null;
	}

	public ObjectResource getTest()
	{
		try
		{
			SystemEvent event = (SystemEvent )Pool.get("event", event_id);
			if(event.type_id.equals("evaluationalarmevent"))
			{
				//				Evaluation ev = (Evaluation) Pool.get("evaluation",
				//						event.descriptor);
				//				
				//				 Result result = (Result )Pool.get("result", ev.result_id);
				//				  if(!result.result_type.equals("test")) return null; Test t =
				// (Test
				//				  )Pool.get("test", result.test_id); return t;
				//				 
				return null;
			} else
				if(event.type_id.equals("testalarmevent")
						|| event.type_id.equals("testwarningevent"))
				{
					Test t = (Test )Pool.get("test", event.descriptor);
					return t;
				}

			return null;
		} catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}

	}

	public Object getTransferable()
	{
		return transferable;
	}

	public String getTyp()
	{
		return typ;
	}

	public void setLocalFromTransferable()
	{
		id = transferable.id;
		generated = transferable.generated;
		source_id = transferable.source_id;
		modified = transferable.modified;
		fixed_when = transferable.fixed_when;
		assigned = transferable.assigned;
		comments = transferable.comments;
		fixed_by = transferable.fixed_by;
		type_id = transferable.type_id;
		status = transferable.status;
		assigned_to = transferable.assigned_to;
		event_id = transferable.event_id;
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.generated = generated;
		transferable.source_id = source_id;
		transferable.modified = modified;
		transferable.fixed_when = fixed_when;
		transferable.assigned = assigned;
		transferable.comments = comments;
		transferable.type_id = type_id;
		transferable.fixed_by = fixed_by;
		transferable.status = status;
		transferable.assigned_to = assigned_to;
		transferable.event_id = event_id;
	}

	public void updateLocalFromTransferable()
	{
		// nothing to do
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException
	{
		id = (String )in.readObject();
		generated = in.readLong();
		modified = in.readLong();
		source_id = (String )in.readObject();
		fixed_when = in.readLong();
		assigned = in.readLong();
		comments = (String )in.readObject();
		fixed_by = (String )in.readObject();
		type_id = (String )in.readObject();
		status = AlarmStatus.from_int(in.readInt());
		assigned_to = (String )in.readObject();
		event_id = (String )in.readObject();

		transferable = new Alarm_Transferable();
		updateLocalFromTransferable();
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeLong(generated);
		out.writeLong(modified);
		out.writeObject(source_id);
		out.writeLong(fixed_when);
		out.writeLong(assigned);
		out.writeObject(comments);
		out.writeObject(fixed_by);
		out.writeObject(type_id);
		out.writeInt(status.value());
		out.writeObject(assigned_to);
		out.writeObject(event_id);
	}

}

class AlarmTimeSorter extends ObjectResourceSorter
{

	String[][] sorted_columns = new String[][]
	{
	{ "time", "long"}};

	public long getLong(ObjectResource or, String column)
	{
		Alarm alarm = (Alarm )or;
		return alarm.generated;
	}

	public String[][] getSortedColumns()
	{
		return sorted_columns;
	}

	public String getString(ObjectResource or, String column)
	{
		return "";
	}
}