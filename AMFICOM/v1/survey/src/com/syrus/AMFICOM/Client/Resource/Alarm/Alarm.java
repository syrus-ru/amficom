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

import java.io.IOException;
import java.io.Serializable;
import java.io.ObjectStreamClass;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

public class Alarm extends ObjectResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	Alarm_Transferable transferable;

	public String id;
	public long generated;
	public long modified;
	public String source_id;
	public long fixed_when;
	public long assigned;
	public String comments;
	public String fixed_by;
	public String type_id;
	public AlarmStatus status;
	public String assigned_to;
	public String event_id;

	static final public String typ = "alarm";

//	Message_Transferable message;

	static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

	public Alarm()
	{
		super("alarm");
	}

	public Alarm(Alarm_Transferable transferable)
	{
		super("alarm");
		this.transferable = transferable;
		setLocalFromTransferable();
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
	}

	public String getTyp()
	{
		return typ;
	}

	public ObjectResourceModel getModel()
	{
		return new AlarmModel(this);
	}

	public static PropertiesPanel getPropertyPane()
	{
		return new GeneralPanel();
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new com.syrus.AMFICOM.Client.Survey.Alarm.UI.AlarmDisplayModel();
	}
	
	public static ObjectResourceFilter getFilter()
	{
		return new com.syrus.AMFICOM.Client.Survey.Alarm.Filter.AlarmFilter();
	}
	
	public static ObjectResourceSorter getSorter()
	{
		return new com.syrus.AMFICOM.Client.Survey.Alarm.Sorter.AlarmSorter();
	}
	
	public Object getTransferable()
	{
		return transferable;
	}

	public String getName()
	{
		String s = Pool.getName(AlarmType.typ, type_id);
				
		return sdf.format(new Date(generated)) + " [" + s + "]";
	}

	public String getId()
	{
		return id;
	}

	public long getModified()
	{
		return modified;
	}

	public String getDomainId()
	{
		return "sysdomain";
	}

	public SystemEvent getEvent()
	{
		return (SystemEvent )Pool.get("event", event_id);
	}

	public ObjectResource getDescriptor()
	{
		try 
        {
			SystemEvent event = (SystemEvent )Pool.get("event", event_id);
			if(event.type_id.equals("evaluationalarmevent"))
			{
				Evaluation ev = (Evaluation )Pool.get("evaluation", event.descriptor);
				return ev;
			}
			else
			if(event.type_id.equals("testalarmevent") || event.type_id.equals("testwarningevent"))
			{
				Test t = (Test )Pool.get("test", event.descriptor);
				return t;
			}

			return null;
        } 
		catch (Exception ex) 
        {
            ex.printStackTrace();
			return null;
        }
	}

	public ObjectResource getTest()
	{
		try 
        {
			SystemEvent event = (SystemEvent )Pool.get("event", event_id);
			if(event.type_id.equals("evaluationalarmevent"))
			{
				Evaluation ev = (Evaluation )Pool.get("evaluation", event.descriptor);
/*
				Result r = (Result )Pool.get("result", ev.result_id);
				if(!r.result_type.equals("test"))
					return null;
				Test t = (Test )Pool.get("test", r.test_id);
				return t;
*/
				return null;
			}
			else
			if(event.type_id.equals("testalarmevent") || event.type_id.equals("testwarningevent"))
			{
				Test t = (Test )Pool.get("test", event.descriptor);
				return t;
			}

			return null;
        } 
		catch (Exception ex) 
        {
            ex.printStackTrace();
			return null;
        }
        
	}

	public ObjectResource getElementaryTest()
	{
		try 
        {
			SystemEvent event = (SystemEvent )Pool.get("event", event_id);
			if(event.type_id.equals("evaluationalarmevent"))
			{
/*
				Evaluation ev = (Evaluation )Pool.get("evaluation", event.descriptor);
				Result r = (Result )Pool.get("result", ev.result_id);
				if(!r.result_type.equals("test"))
					return null;
				Test t = (Test )Pool.get("test", r.test_id);
				return t;
*/
				return null;
			}
			else
			if(event.type_id.equals("testalarmevent") || event.type_id.equals("testwarningevent"))
			{
				Test t = (Test )Pool.get("test", event.descriptor);
				return t;
			}

			return null;
        } 
		catch (Exception ex) 
        {
            ex.printStackTrace();
			return null;
        }
        
	}

	public ObjectResource getResult()
	{
		try 
        {
			SystemEvent event = (SystemEvent )Pool.get("event", event_id);
			if(event.type_id.equals("evaluationalarmevent"))
			{
				for(Enumeration enum = Pool.getHash("result").elements(); 
					enum.hasMoreElements();
					)
				{
					Result r = (Result )enum.nextElement();
					if(r.result_type.equals("evaluation") && r.id.equals(event.descriptor))
						return r;
				}
				return null;
			}
			else
			if(event.type_id.equals("testalarmevent") || event.type_id.equals("testwarningevent"))
			{
				for(Enumeration enum = Pool.getHash("result").elements(); 
					enum.hasMoreElements();
					)
				{
					Result r = (Result )enum.nextElement();
					if(r.result_type.equals("evaluation") && r.id.equals(event.descriptor))
						return r;
				}
				return null;
			}

			return null;
        } 
		catch (Exception ex) 
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
		}
		catch(Exception ex)
		{
			return null;
		}
		return null;
	}
	
	public String getMonitoredElementId()
	{
		try 
        {
			SystemEvent event = (SystemEvent )Pool.get("event", event_id);
			if(event.type_id.equals("evaluationalarmevent"))
			{
				return "";
			}
			else
			if(event.type_id.equals("testalarmevent") || event.type_id.equals("testwarningevent"))
			{
				Result res = (Result )Pool.get(Result.typ, event.descriptor);
				if(res == null)
					return "";
				if(res.result_type.equals("test"))
				{
					Test test = (Test )Pool.get(Test.typ, res.action_id);
					if(test == null)
						return "";
					return test.monitored_element_id;
				}
				if(res.result_type.equals("evaluation"))
				{
					Evaluation evaluation = (Evaluation )Pool.get(Evaluation.typ, res.action_id);
					if(evaluation == null)
						return "";
					return evaluation.monitored_element_id;
				}
				return "";
			}

			return "";
        } 
		catch (Exception ex) 
        {
            ex.printStackTrace();
			return "";
        }
        
	}

	public static Alarm getAlarmByTestResult(String result_id)
	{
		Result res = (Result )Pool.get(Result.typ, result_id);
		if(res == null)
			return null;
		Test test = (Test )Pool.get(Test.typ, res.action_id);
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
			SystemEvent event = (SystemEvent )Pool.get("event", a.event_id);
			if(event == null)
				continue;
			Result rrr = (Result )Pool.get("result", event.descriptor);
			if(rrr == null)
				continue;
			Evaluation eval = (Evaluation )Pool.get("evaluation", rrr.action_id);
			if(eval == null)
				continue;
			if(test.evaluation_id.equals(rrr.action_id))
			{
				if(eval.result_ids.length > res_index)
					if(eval.result_ids[res_index].equals(event.descriptor))
						return a;
			}
		}
		return null;
	}

	public Evaluation getEvaluation()
	{
		SystemEvent event = (SystemEvent )Pool.get("event", event_id);
		Result rrr = (Result )Pool.get("result", event.descriptor);
		Evaluation eval = (Evaluation )Pool.get("evaluation", rrr.action_id);
		return eval;
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

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
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

	public static ObjectResourceSorter getDefaultSorter()
	{
		return new AlarmTimeSorter();
	}

}

class AlarmTimeSorter extends ObjectResourceSorter
{
	String[][] sorted_columns = new String[][]{
		{"time", "long"}
		};

	public String[][] getSortedColumns()
	{
		return sorted_columns;
	}
	
	public String getString(ObjectResource or, String column)
	{
		return "";
	}

	public long getLong(ObjectResource or, String column)
	{
		Alarm alarm = (Alarm )or;
		return alarm.generated;
	}
}
