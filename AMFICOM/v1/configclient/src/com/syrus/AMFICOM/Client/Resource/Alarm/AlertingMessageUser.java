package com.syrus.AMFICOM.Client.Resource.Alarm;

import com.syrus.AMFICOM.CORBA.Alarm.AlertingMessageUser_Transferable;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.User;

public class AlertingMessageUser extends StubResource
{
	AlertingMessageUser_Transferable transferable;

	public String id = "";
	public String user_id = "";
	public String alerting_message_id = "";
	public String alerting_type_id = "";

	public static final String typ = "alertingmessageuser";

	public AlertingMessageUser()
	{
		transferable = new AlertingMessageUser_Transferable();
	}

	public AlertingMessageUser(AlertingMessageUser_Transferable tr)
	{
		transferable = tr;
		setLocalFromTransferable();
	}

	public AlertingMessageUser(
			String id,
			String user_id,
			String alerting_message_id,
			String alerting_type_id)
	{
		this.id = id;
		this.user_id = user_id;
			this.alerting_message_id = alerting_message_id;
			this.alerting_type_id = alerting_type_id;

		transferable = new AlertingMessageUser_Transferable();
	}
	public void setLocalFromTransferable()
	{
		this.id = transferable.id;
			this.user_id = transferable.user_id;
			this.alerting_message_id = transferable.alerting_message_id;
			this.alerting_type_id = transferable.alerting_type_id;
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
			transferable.user_id = user_id;
			transferable.alerting_message_id = alerting_message_id;
			transferable.alerting_type_id = alerting_type_id;
	}

	public void updateLocalFromTransferable()
	{
	}

	public String getId()
	{
		return id;
	}

	public String getName()
	{
		return Pool.getName(User.typ, user_id);
	}

	public String getTyp()
	{
		return typ;
	}

	public String getDomainId()
	{
		return "sysdomain";
	}

	public Object getTransferable()
	{
//		setTransferableFromLocal();
		return transferable;
	}

	public String getPropertyPaneClassName()
	{
		return "";
	}
}