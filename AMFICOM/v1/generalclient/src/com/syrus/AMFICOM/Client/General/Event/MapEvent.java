package com.syrus.AMFICOM.Client.General.Event;

public class MapEvent extends OperationEvent 
{
	static final public String type = "mapevent";

	public MapEvent(Object source, long typ)
	{
		super(source, 0, type);
	}
}
