package com.syrus.AMFICOM.Client.General.Event;

import java.awt.event.ActionEvent;

public class OperationEvent extends ActionEvent
{
	Object source;
	int id;
	String command;

	public OperationEvent(Object source, int id, String command)
	{
		super(source, id, command);
		this.source = source;
		this.id = id;
		this.command = command;
	}

	public String getActionCommand()
	{
		return command;
	}

	public Object getSource()
	{
		return source;
	}
}
