package com.syrus.AMFICOM.Client.Resource.Alarm;

import com.syrus.AMFICOM.CORBA.Alarm.EventSource_Transferable;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import java.io.IOException;
import java.io.Serializable;

public class EventSource extends ObjectResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	EventSource_Transferable transferable;

    public String id = "";
    public String object_source_id = "";
    public String object_source_name = "";
    public String type_id = "";
    public String domain_id = "";

	static final public String typ = "eventsource";

	public EventSource()
	{
		transferable = new EventSource_Transferable();
	}

	public EventSource(EventSource_Transferable tr)
	{
		transferable = tr;
		setLocalFromTransferable();
	}

	EventSource(
			String id,
		    String object_source_id,
		    String object_source_name,
		    String type_id,
			String domain_id)
	{
		this.id = id;
	    this.object_source_id = object_source_id;
	    this.object_source_name = object_source_name;
	    this.type_id = type_id;
		this.domain_id = domain_id;

		transferable = new EventSource_Transferable();
	}

	public void setLocalFromTransferable()
	{
		this.id = transferable.id;
	    this.object_source_id = transferable.object_source_id;
	    this.object_source_name = transferable.object_source_name;
	    this.type_id = transferable.type_id;
		this.domain_id = transferable.domain_id;
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
	    transferable.object_source_id = object_source_id;
	    transferable.object_source_name = object_source_name;
	    transferable.type_id = type_id;
		transferable.domain_id = domain_id;
	}

	public void updateLocalFromTransferable()
	{
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
		return object_source_name;
	}
	
	public String getTyp()
	{
		return typ;
	}
	
	public Object getTransferable()
	{
		return transferable;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(object_source_id);
		out.writeObject(object_source_name);
		out.writeObject(type_id);
		out.writeObject(domain_id);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		object_source_id = (String )in.readObject();
		object_source_name = (String )in.readObject();
		type_id = (String )in.readObject();
		domain_id = (String )in.readObject();

		transferable = new EventSource_Transferable();
		updateLocalFromTransferable();
	}
}

