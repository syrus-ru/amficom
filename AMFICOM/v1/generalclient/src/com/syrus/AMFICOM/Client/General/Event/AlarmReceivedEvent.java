package com.syrus.AMFICOM.Client.General.Event;

public class AlarmReceivedEvent extends OperationEvent
{
	static final public String type = "alarmreceived";

	public AlarmReceivedEvent(Object source)
	{
		super(source, 0, type);
	}
}