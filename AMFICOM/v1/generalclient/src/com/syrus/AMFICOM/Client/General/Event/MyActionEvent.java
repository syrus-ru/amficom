
// Copyright (c) Syrus Systems 2000 Syrus Systems
package com.syrus.AMFICOM.Client.General.Event;


public class MyActionEvent extends OperationEvent
{
	public String command_type;
	public String command_name;

	public MyActionEvent(Object source, String ct, String cn)
	{
		super(source, 0, "execute");
		command_type = new String(ct);
		command_name = new String(cn);
	}

	public void setCommandType(String ct)
	{
		command_type = new String(ct);
	}

	public void setCommandName(String cn)
	{
		command_name = new String(cn);
	}

	public String getCommandType()
	{
		return command_type;
	}

	public String getCommandName()
	{
		return command_name;
	}
}

