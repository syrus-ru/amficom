package com.syrus.AMFICOM.Client.Resource.Result;

import java.awt.*;
import java.text.*;
import java.util.*;

import com.syrus.AMFICOM.CORBA.General.*;
import com.syrus.AMFICOM.CORBA.Survey.*;
import com.syrus.AMFICOM.Client.Resource.*;
//import com.syrus.AMFICOM.Client.Analysis.Reflectometry.Data.*;

import com.syrus.AMFICOM.Client.General.UI.*;

public class TestRequest extends ObjectResource
{
	static final public String typ = "testrequest";
	public ClientTestRequest_Transferable transferable;
	public String id = "";
	public String name = "";
	public long created = 0;
	public String user_id = "";
	public long deleted = 0;
	public TestRequestStatus status = TestRequestStatus.TEST_REQUEST_STATUS_SHEDULED;
	public long completion_time = 0;
	public String status_text = "";
	public Vector test_ids = new Vector();

	Hashtable tests = new Hashtable();

	public TestRequest(ClientTestRequest_Transferable transferable)	
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public TestRequest(String id)	
	{
		this.id = id;
		transferable = new ClientTestRequest_Transferable();
	}
/*
	public void addParameter(Parameter parameter)	{
		parameters.add(parameter);
	}

	public void addArgument(Parameter argument)	{
		arguments.add(argument);
	}
*/
	public void updateLocalFromTransferable()	
	{
		int l;
		int i;

		l = test_ids.size();
		tests = new Hashtable();
		for(i = 0; i < l; i++)
			tests.put(test_ids.get(i),
				Pool.get("test", (String)test_ids.get(i)));
	}

	public void setTransferableFromLocal()	{
/*
		public java.lang.String id;
		public java.lang.String name;
		public int status;
		public long created;
		public java.lang.String user_id;
		public long deleted;
		public long completion_time;
		public java.lang.String status_text;
		public java.lang.String[] test_ids;
	*/
		this.transferable.id = this.id;
		this.transferable.name = this.name;
		this.transferable.status = this.status;
		this.transferable.created = this.created;
		this.transferable.user_id = this.user_id;
		this.transferable.deleted = this.deleted;
		this.transferable.completion_time = this.completion_time;
		this.transferable.status_text = "";

		this.transferable.test_ids = new String[this.test_ids.size()];
		for(int i = 0; i < this.transferable.test_ids.length; i++)
			this.transferable.test_ids[i] = (String)this.test_ids.get(i);
	}

	public void setLocalFromTransferable()	
	{
		this.id = this.transferable.id;
		this.name = this.transferable.name;
		this.created = this.transferable.created;
		this.user_id = this.transferable.user_id;
		this.deleted = this.transferable.deleted;
		this.status = this.transferable.status;
		this.completion_time = this.transferable.completion_time;
		this.status_text = "";

		this.test_ids = new Vector();
		for(int i = 0; i < this.transferable.test_ids.length; i++) {
			this.test_ids.add(this.transferable.test_ids[i]);
		}
	}

	public String getId()	
	{
		return id;
	}

	public String getDomainId()
	{
		return "sysdomain";
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

	public Hashtable getColumns()
	{
		Hashtable cols = new Hashtable();
		cols.put("created", "Время запроса");
		cols.put("user_id", "Пользователь");
		cols.put("status", "Статус");
		return cols;
	}

	public int getColumnSize(String col_id)
	{
		if(col_id.equals("created"))
			return 100;
		if(col_id.equals("user_id"))
			return 100;
		if(col_id.equals("status"))
			return 100;
		return 100;
	}

	public String getColumnValue(String col_id)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
		String s = "";
		try
		{
			if(col_id.equals("status"))
			{
				switch(status.value())
				{
					case TestRequestStatus._TEST_REQUEST_STATUS_SHEDULED:
						s = "Готов к выполнению";
						break;
					case TestRequestStatus._TEST_REQUEST_STATUS_PROCESSING:
						s = "Выполняется";
						break;
					case TestRequestStatus._TEST_REQUEST_STATUS_COMPLETED:
						s = "Выполнен";
						break;
					case TestRequestStatus._TEST_REQUEST_STATUS_ABORTED:
						s = "Снят с выполнения";
						break;
				}
			}
			if(col_id.equals("user_id"))
				s = user_id;
			if(col_id.equals("created"))
				s = sdf.format(new Date(created));
		}
		catch(Exception e)
		{
//			System.out.println("error gettin field value - TestRequest");
			s = "";
		}
		return s;
	}

	public boolean isColumnEditable(String col_id)
	{
		if(col_id.equals("created"))
			return false;
		if(col_id.equals("user_id"))
			return true;
		if(col_id.equals("status"))
			return false;
		return false;
	}

	public Component getColumnRenderer(String col_id)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");

		if(col_id.equals("status"))
			return new TextFieldEditor(getColumnValue("status"));
		if(col_id.equals("created"))
			return new TextFieldEditor(sdf.format(new Date(created)));
		if(col_id.equals("user_id"))
			return new ObjectResourceComboBox("user", user_id);
		return null;
	}

	public String getColumnName (String col_id)
	{
		String s = "";
		if(col_id.equals("creatd"))
			s = "Время запроса";
		if(col_id.equals("user_id"))
			s = "Пользователь";
		if(col_id.equals("status"))
			s = "Статус";
		return s;
	}

	public Component getColumnEditor(String col_id)
	{
		return getColumnRenderer(col_id);
	}

	public Enumeration getChildren(String key)
	{
		if(key.equals("test"))
		{
			return tests.elements();
		}
		return new Vector().elements();
	}
}