package com.syrus.AMFICOM.Client.Resource.Alarm;

import com.syrus.AMFICOM.CORBA.Alarm.Event_Transferable;
import com.syrus.AMFICOM.CORBA.General.EventStatus;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;

import java.io.IOException;
import java.io.Serializable;

public class SystemEvent extends ObjectResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	Event_Transferable transferable;

	public String id = "";
	public String description = "";
	public String source_id = "";
	public String type_id = "";
	public String descriptor = "";
	public EventStatus status;
	public long created = 0;

	static final public String typ = "event";

	public SystemEvent()
	{
		transferable = new Event_Transferable();
	}

	public SystemEvent(Event_Transferable tr)
	{
		transferable = tr;
		setLocalFromTransferable();
	}

	SystemEvent(
			String id,
			String description,
			String source_id,
			String type_id,
			String descriptor,
			boolean processed,
			long created)
	{
		this.id = id;
		this.description = description;
		this.source_id = source_id;
		this.type_id = type_id;
		this.descriptor = descriptor;
		this.status = status;
		this.created = created;

		transferable = new Event_Transferable();
	}

	public void setLocalFromTransferable()
	{
		this.id = transferable.id;
		this.description = transferable.description;
		this.source_id = transferable.source_id;
		this.type_id = transferable.type_id;
		this.descriptor = transferable.descriptor;
		this.status = transferable.status;
		this.created = transferable.created;
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.description = description;
		transferable.source_id = source_id;
		transferable.type_id = type_id;
		transferable.descriptor = descriptor;
		transferable.status = status;
		transferable.created = created;
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
		return type_id;
	}
	
	public String getTyp()
	{
		return typ;
	}
	
	public Object getTransferable()
	{
		return transferable;
	}

	public EventSource getSource()
	{
		return (EventSource )Pool.get(EventSource.typ, source_id);
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(description);
		out.writeObject(source_id);
		out.writeObject(type_id);
		out.writeObject(descriptor);
		out.writeInt(status.value());
		out.writeLong(created);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		description = (String )in.readObject();
		source_id = (String )in.readObject();
		type_id = (String )in.readObject();
		descriptor = (String )in.readObject();
		status = EventStatus.from_int(in.readInt());
		created = in.readLong();

		transferable = new Event_Transferable();
		updateLocalFromTransferable();
	}
}