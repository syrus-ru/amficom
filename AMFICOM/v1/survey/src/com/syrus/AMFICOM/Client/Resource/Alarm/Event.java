package com.syrus.AMFICOM.Client.Resource.Alarm;

import com.syrus.AMFICOM.CORBA.Alarm.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class Event extends StubResource
{
	Event_Transferable transferable;

	public String id = "";
	public String description = "";
	public String source_id = "";
	public String type_id = "";
	public String descriptor = "";
	public boolean processed = false;
	public long created = 0;

	public Event()
	{
		transferable = new Event_Transferable();
	}

	public Event(Event_Transferable tr)
	{
		transferable = tr;
		setLocalFromTransferable();
	}

	Event(
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
		this.processed = processed;
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
		this.processed = transferable.processed;
		this.created = transferable.created;
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.description = description;
		transferable.source_id = source_id;
		transferable.type_id = type_id;
		transferable.descriptor = descriptor;
		transferable.processed = processed;
		transferable.created = created;
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
		return type_id;
	}
	
	public Object getTransferable()
	{
		return transferable;
	}
}