package com.syrus.AMFICOM.Client.Resource.Result;

import java.util.*;

import com.syrus.AMFICOM.CORBA.General.*;
import com.syrus.AMFICOM.CORBA.Survey.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class TestRequest extends ObjectResource
{
	public static final  String typ = "testrequest";
	public ClientTestRequest_Transferable transferable;
	public String id = "";
	public String name = "";
	public long created = 0;
	public String user_id = "";
	public long deleted = 0;
	public TestRequestStatus status = TestRequestStatus.TEST_REQUEST_STATUS_SHEDULED;
	public long completion_time = 0;
	public String status_text = "";
	public ArrayList test_ids = new ArrayList();

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

		this.test_ids = new ArrayList();
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
}