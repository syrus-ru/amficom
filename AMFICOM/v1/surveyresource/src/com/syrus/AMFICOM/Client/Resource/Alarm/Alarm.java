package com.syrus.AMFICOM.Client.Resource.Alarm;

import java.io.*;
import java.util.Date;

import com.syrus.AMFICOM.CORBA.Alarm.Alarm_Transferable;
import com.syrus.AMFICOM.CORBA.General.AlarmStatus;
import com.syrus.AMFICOM.Client.General.Filter.ObjectResourceFilter;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Survey.General.ConstStorage;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.measurement.*;
import com.syrus.AMFICOM.measurement.corba.ResultSort;

public class Alarm extends StubResource implements Serializable
{
	private static final long serialVersionUID = 01L;

	public static final String typ = "alarm";

	public static final String EVALUATION_ALARM_EVENT = "evaluationalarmevent";
	public static final String TEST_ALARM_EVENT = "testalarmevent";
	public static final String TEST_WARNING_EVENT = "testwarningevent";

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

	public Alarm()
	{
	}

	public Alarm(Alarm_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public static ObjectResourceSorter getDefaultSorter()
	{
		return new AlarmTimeSorter();
	}

	public static ObjectResourceFilter getFilter()
	{
		return new com.syrus.AMFICOM.Client.Survey.Alarm.Filter.AlarmFilter();
	}

	public static String getPropertyPaneClassName()
	{
		return "com.syrus.AMFICOM.General.UI.GeneralPanel";
	}

	public static ObjectResourceSorter getSorter()
	{
		return new com.syrus.AMFICOM.Client.Survey.Alarm.Sorter.AlarmSorter();
	}

	public String getDomainId()
	{
		return ConstStorage.SYS_DOMAIN;
	}

	public Evaluation getEvaluation()
	{
		try {
			Result res = (Result)MeasurementStorableObjectPool.getStorableObject(
					new Identifier(getEvent().descriptor), true);
			 if (res.getSort().equals(ResultSort.RESULT_SORT_EVALUATION))
				return (Evaluation)res.getAction();
		}
		catch (ApplicationException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public SystemEvent getEvent()
	{
		return (SystemEvent)Pool.get(SystemEvent.typ, event_id);
	}

	public String getId()
	{
		return id;
	}

	public long getModified()
	{
		return modified;
	}

	public Identifier getMonitoredElementId()
	{
		Result res = getResult();
		if (res != null)
			return res.getAction().getMonitoredElementId();
		return null;
	}

	public String getName()
	{
		String s = Pool.getName(AlarmType.typ, type_id);

		return ConstStorage.SIMPLE_DATE_FORMAT.format(new Date(generated))
				+ " [" + s + "]";
	}

	public Result getResult()
	{
		try {
			return (Result)MeasurementStorableObjectPool.getStorableObject(
					new Identifier(getEvent().descriptor), true);
		}
		catch (ApplicationException ex) {
			return null;
		}
	}

	public String getSourceId()
	{
		try {
			SystemEvent event = getEvent();
			if (event != null)
				return event.source_id;
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
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

	String[][] sorted_columns = new String[][] {
			{"time", "long"}
	};

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

