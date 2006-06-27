package com.syrus.AMFICOM;

import java.util.EventObject;

public class OperationEvent extends EventObject {
	int id;
	String command;

	public OperationEvent(Object source, int id, String command) {
		super(source);
		this.id = id;
		this.command = command;
	}

	public String getActionCommand() {
		return command;
	}

	public Object getSource()	{
		return source;
	}
}
