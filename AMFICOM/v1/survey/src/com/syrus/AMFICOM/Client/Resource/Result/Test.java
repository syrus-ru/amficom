package com.syrus.AMFICOM.Client.Resource.Result;

import com.syrus.AMFICOM.CORBA.General.TestReturnType;
import com.syrus.AMFICOM.CORBA.General.TestStatus;
import com.syrus.AMFICOM.CORBA.General.TestTemporalType;
import com.syrus.AMFICOM.CORBA.General.TestTimeStamps;
import com.syrus.AMFICOM.CORBA.General.TestTimeStampsPackage.PeriodicalTestParameters;
import com.syrus.AMFICOM.CORBA.Survey.ClientTest_Transferable;
import com.syrus.AMFICOM.CORBA.Survey.ElementaryTestAlarm;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.General.Filter.ObjectResourceFilter;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceSorter;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Survey.Result.UI.TestDisplayModel;

import java.io.IOException;
import java.io.Serializable;

import java.util.Enumeration;
import java.util.Vector;

public class Test extends ObjectResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	transient static final public String typ = "test";
	transient public ClientTest_Transferable transferable;

	public String id = "";
	public String test_type_id = "";
	transient public TestTemporalType temporal_type;
	transient public TestReturnType return_type;
	public long start_time = 0;
	transient public TestTimeStamps time_stamps;
	public String kis_id = "";
	public String name = "";
	public String analysis_id = "";
	public String evaluation_id = "";
	public long modified = 0;
	public String request_id = "";
	public String user_id = "";
	public long deleted = 0;
	public String description = "";
	public long duration = 3000;

	public String test_setup_id = "";

	public String monitored_element_id = "";
	transient public TestStatus status;

	public String[] result_ids = new String[0];

	transient public ElementaryTestAlarm[] elementary_test_alarms = new ElementaryTestAlarm[0];
//public Vector parameters;

	public String test_argument_set_id = "";
//	public Vector arguments;

	public Test(ClientTest_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public Test(String id)
	{
//	parameters = new Vector();
//		arguments = new Vector();
		this.id = id;
		time_stamps = new TestTimeStamps();
		time_stamps._default();
		elementary_test_alarms = new ElementaryTestAlarm[0];
		transferable = new ClientTest_Transferable();
	}

	public String getDomainId()
	{
		return "sysdomain";
	}

	public void addArgument(Parameter argument)
	{
//		arguments.add(argument);
	}

	public void updateLocalFromTransferable() 
	{
	}

	public Test myclone() 
	{
		Test new_test = new Test(this.id);
		new_test.test_type_id = this.test_type_id;
		new_test.start_time = this.start_time;
		new_test.deleted = this.deleted;
		new_test.kis_id = this.kis_id;
		new_test.analysis_id = this.analysis_id;
		new_test.evaluation_id = this.evaluation_id;
		new_test.name = this.name;
		new_test.modified = this.modified;
		new_test.request_id = this.request_id;
		new_test.monitored_element_id = this.monitored_element_id;
		new_test.temporal_type = this.temporal_type;
		new_test.return_type = this.return_type;
		new_test.status = this.status;
		new_test.time_stamps = this.time_stamps;
//		new_test.arguments = this.arguments;
		new_test.test_argument_set_id = test_argument_set_id;
		new_test.elementary_test_alarms = this.elementary_test_alarms;
		new_test.result_ids = this.result_ids;
		new_test.description = this.description;
		new_test.transferable = this.transferable;
		new_test.user_id = this.user_id;
		new_test.test_setup_id = this.test_setup_id;
		new_test.duration = this.duration;

		return new_test;
	}

	public void setTransferableFromLocal() 
	{
		transferable.id = this.id;
		transferable.analysis_id = this.analysis_id;
		transferable.evaluation_id = this.evaluation_id;
		transferable.test_type_id = this.test_type_id;
		transferable.start_time = this.start_time;
		transferable.deleted = 0;
		transferable.return_type = this.return_type;
		transferable.kis_id = this.kis_id;
		transferable.name = this.name;
		transferable.modified = this.modified;
		transferable.request_id = this.request_id;
		transferable.monitored_element_id = this.monitored_element_id;
		transferable.temporal_type = this.temporal_type;
		transferable.status = TestStatus.TEST_STATUS_SCHEDULED;
		transferable.time_stamps = this.time_stamps;
//		transferable.arguments = new ClientParameter_Transferable[this.arguments.size()];
		transferable.test_argument_set_id = test_argument_set_id;
		transferable.elementary_test_alarms = new ElementaryTestAlarm[0];
		transferable.result_ids = new String[0];
		transferable.test_setup_id = this.test_setup_id;
		transferable.duration = this.duration;
/*
		for (int i=0; i<transferable.arguments.length; i++)
		{
			Parameter argument = (Parameter)arguments.get(i);
			argument.setTransferableFromLocal();
			transferable.arguments[i] = (ClientParameter_Transferable)argument.getTransferable();
		}
*/
	}

	public void setLocalFromTransferable()	
	{
		this.id = transferable.id;
		this.analysis_id = transferable.analysis_id;
		this.evaluation_id = transferable.evaluation_id;
		this.test_type_id = transferable.test_type_id;
		this.return_type = transferable.return_type;
		this.start_time = transferable.start_time;
		this.deleted = transferable.deleted;
		this.kis_id = transferable.kis_id;
		this.name = transferable.name;
		this.modified = transferable.modified;
		this.request_id = transferable.request_id;
		this.monitored_element_id = transferable.monitored_element_id;
		this.temporal_type = transferable.temporal_type;
		this.time_stamps = transferable.time_stamps;
		this.status = transferable.status;

		result_ids = transferable.result_ids;
		this.elementary_test_alarms = transferable.elementary_test_alarms;

		this.test_argument_set_id = transferable.test_argument_set_id;
		this.test_setup_id = transferable.test_setup_id;
		this.duration = transferable.duration;
/*
		arguments = new Vector();
		for (int i=0; i<transferable.arguments.length; i++)
			arguments.add(new Parameter(transferable.arguments[i]));
*/
	}

	public String getId()	
	{
		return id;
	}

	public String getName()	
	{
		return name;
	}

	public Object getTransferable()	
	{
		return transferable;
	}

	public String getTyp ()	
	{
		return typ;
	}

	public long getModified ()	
	{
		return modified;
	}

	public ObjectResourceModel getModel()
	{
		return new TestModel(this);
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new TestDisplayModel();
	}

	public static ObjectResourceFilter getFilter()
	{
		return new com.syrus.AMFICOM.Client.Schedule.Filter.TestFilter();
	}

	public static ObjectResourceSorter getSorter()
	{
		return new com.syrus.AMFICOM.Client.Schedule.Sorter.TestSorter();
	}

	public Enumeration getChildren(String key)
	{
		Vector vec = new Vector();
		if(key.equals("elementaryresult"))
		{
			for(int i = 0; i < result_ids.length; i++)
				vec.add(Pool.get("result", result_ids[i]));
		}
		return vec.elements();
	}

	public Class getChildClass(String key)
	{
		if(key.equals("elementaryresult"))
		   return Result.class;
		return ObjectResource.class;
	}

	public Enumeration getChildTypes()
	{
		Vector vec = new Vector();
		vec.add("elementaryresult");
		return vec.elements();
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(test_type_id);

		out.writeInt(temporal_type.value());

		out.writeInt(return_type.value());
		
		out.writeLong(start_time);
//		out.writeObject(time_stamps);

		if(temporal_type.value() == (int )(TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL))
		{
			PeriodicalTestParameters ptp = time_stamps.ptpars();
			out.writeLong(ptp.dt);
			out.writeLong(ptp.end_time);
		}
		if(temporal_type.value() == (int )(TestTemporalType._TEST_TEMPORAL_TYPE_TIMETABLE))
			out.writeObject(time_stamps.ti());

		out.writeObject(kis_id);
		out.writeObject(name);
		out.writeObject(analysis_id);
		out.writeObject(evaluation_id);
		out.writeLong(modified);
		out.writeObject(request_id);
		out.writeObject(user_id);
		out.writeLong(deleted);
		out.writeObject(description);
		out.writeLong(duration);
		out.writeObject(test_setup_id);
		out.writeObject(monitored_element_id);
		out.writeInt(status.value());
		Object obj = result_ids;
		out.writeObject(obj);

//		Object obj2 = elementary_test_alarms;
//		out.writeObject(obj2);
		out.writeInt(elementary_test_alarms.length);
		for(int i = 0; i < elementary_test_alarms.length; i++)
		{
			out.writeLong(elementary_test_alarms[i].elementary_start_time);
			out.writeObject(elementary_test_alarms[i].alarm_id);
			out.writeObject(elementary_test_alarms[i].alarm_name);
		}

		out.writeObject(test_argument_set_id);
	}

	private void readObject(java.io.ObjectInputStream in)
		throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		test_type_id = (String )in.readObject();
		int ttt = in.readInt();
		temporal_type = TestTemporalType.from_int(ttt);
//		temporal_type = (TestTemporalType )in.readObject();
		int trt = in.readInt();
		return_type = TestReturnType.from_int(trt);
//		return_type = (TestReturnType )in.readObject();
		start_time = in.readLong();

//		time_stamps = (TestTimeStamps )in.readObject();
		time_stamps = new TestTimeStamps();
		if(temporal_type.value() == (int )(TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL))
		{
			PeriodicalTestParameters ptp = new PeriodicalTestParameters();
			ptp.dt = in.readLong();
			ptp.end_time = in.readLong();
			time_stamps.ptpars(ptp);
		}
		if(temporal_type.value() == (int )(TestTemporalType._TEST_TEMPORAL_TYPE_TIMETABLE))
		{
			long[] l = (long [])in.readObject();
			time_stamps.ti(l);
		}

		kis_id = (String )in.readObject();
		name = (String )in.readObject();
		analysis_id = (String )in.readObject();
		evaluation_id = (String )in.readObject();
		modified = in.readLong();
		request_id = (String )in.readObject();
		user_id = (String )in.readObject();
		deleted = in.readLong();
		description = (String )in.readObject();
		duration = in.readLong();
		test_setup_id = (String )in.readObject();
		monitored_element_id = (String )in.readObject();
		int ts = in.readInt();
		status = TestStatus.from_int(ts);
//		status = (TestStatus )in.readObject();
		Object obj = in.readObject();
		result_ids = (String[] )obj;
//		Object obj2 = in.readObject();

//		elementary_test_alarms = (ElementaryTestAlarm[] )obj2;
		int len = in.readInt();
		elementary_test_alarms = new ElementaryTestAlarm[len];
		for(int i = 0; i < len; i++)
		{
			elementary_test_alarms[i] = new ElementaryTestAlarm();
			elementary_test_alarms[i].elementary_start_time = in.readLong();
			elementary_test_alarms[i].alarm_id = (String )in.readObject();
			elementary_test_alarms[i].alarm_name = (String )in.readObject();
		}

		test_argument_set_id = (String )in.readObject();

		transferable = new ClientTest_Transferable();
		updateLocalFromTransferable();
	}

	public static ObjectResourceSorter getDefaultSorter()
	{
		return new TestTimeSorter();
	}
	
}

class TestTimeSorter extends ObjectResourceSorter
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
		Test test = (Test )or;
		return test.start_time;
	}
}
