package com.syrus.AMFICOM.Client.Resource.Alarm;

import com.syrus.AMFICOM.CORBA.Alarm.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class AlertingMessage extends ObjectResource
{
	AlertingMessage_Transferable transferable;

    public String id = "";
    public String text = "";
    public String message_type_id = "";
    public String event_type_id = "";

	public static final String typ = "alertingmessage";

	public AlertingMessage()
	{
		transferable = new AlertingMessage_Transferable();
	}

	public AlertingMessage(AlertingMessage_Transferable tr)
	{
		transferable = tr;
		setLocalFromTransferable();
	}

	public AlertingMessage(
			String id,
			String text,
			String message_type_id,
			String event_type_id)
	{
		this.id = id;
		this.text = text;
	    this.message_type_id = message_type_id;
	    this.event_type_id = event_type_id;

		transferable = new AlertingMessage_Transferable();
	}

	public void setLocalFromTransferable()
	{
		this.id = transferable.id;
	    this.text = transferable.text;
	    this.message_type_id = transferable.message_type_id;
	    this.event_type_id = transferable.event_type_id;
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
	    transferable.text = text;
	    transferable.message_type_id = message_type_id;
	    transferable.event_type_id = event_type_id;
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
		return text;
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
}