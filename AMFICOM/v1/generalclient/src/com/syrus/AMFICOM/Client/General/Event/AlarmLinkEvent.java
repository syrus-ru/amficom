package com.syrus.AMFICOM.Client.General.Event;

public class AlarmLinkEvent extends OperationEvent
{
	static final public String type = "alarmedlink";

	public AlarmReceivedEvent(Object source)
	{
		super(source, 0, type);
	}
}