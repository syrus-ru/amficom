package com.syrus.AMFICOM.Client.Resource.Alarm;

import com.syrus.AMFICOM.CORBA.Alarm.AlarmType_Transferable;
import com.syrus.AMFICOM.Client.Resource.StubResource;

public class AlarmType extends StubResource
{
	public static final String typ = "alarmtype";

	AlarmType_Transferable transferable;

	public String id;
	public String name;
	public String description;

	public AlarmType()
	{
	}

	public AlarmType(AlarmType_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public void setLocalFromTransferable()
	{
		this.id = transferable.id;
		this.name = transferable.name;
		this.description = transferable.description;
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.name = name;
		transferable.description = description;
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
		return name;
	}

	public String getTyp()
	{
		return typ;
	}

	public Object getTransferable()
	{
		return transferable;
	}

	public String getPropertyPaneClassName()
	{
		return "";
	}
}