package com.syrus.AMFICOM.Client.General.Event;

public class RefChangeEvent extends OperationEvent
{
//	public static final long OPEN_EVENT = 0x00000001;
	//public static final long OPEN_ETALON_EVENT = 0x00000002;
	//public static final long SAVE_EVENT = 0x00000010; -- was not listened
//	public static final long CLOSE_EVENT = 0x00000100;
	//public static final long CLOSE_ETALON_EVENT = 0x00000200; // never generated?
//	public static final long THRESHOLDS_CALC_EVENT = 0x00001000;
	public static final long SELECT_EVENT = 0x00010000;

	public static final String typ = "refchange";
	
	private long type;
	
	public boolean isEventSelect()
	{
		return (type & SELECT_EVENT) != 0;
	}

	public RefChangeEvent(Object source, long type)
	{
		super(source, 0, typ);
		this.type = type;
	}
}