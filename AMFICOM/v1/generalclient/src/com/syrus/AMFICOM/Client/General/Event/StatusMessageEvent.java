package com.syrus.AMFICOM.Client.General.Event;

public class StatusMessageEvent extends OperationEvent
{
	static final public String type = "statusmessage";

	public StatusMessageEvent(String text)
	{
		super(text, 0, type);
	}

	public String getText()
	{
		return (String )getSource();
	}
}