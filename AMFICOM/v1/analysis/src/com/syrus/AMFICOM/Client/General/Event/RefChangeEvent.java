package com.syrus.AMFICOM.Client.General.Event;

public class RefChangeEvent extends OperationEvent
{
	public boolean OPEN = false;
	public boolean OPEN_ETALON = false;
	public boolean SAVE = false;
	public boolean CLOSE = false;
	public boolean CLOSE_ETALON = false;
	public boolean SELECT = false;

	public boolean THRESHOLDS_CALC = false;
	public boolean ANALYSE = false;

	public static final long OPEN_EVENT = 0x00000001;
	public static final long OPEN_ETALON_EVENT = 0x00000002;
	public static final long SAVE_EVENT = 0x00000010;
	public static final long CLOSE_EVENT = 0x00000100;
	public static final long CLOSE_ETALON_EVENT = 0x00000200;
	public static final long THRESHOLDS_CALC_EVENT = 0x00001000;
	public static final long SELECT_EVENT = 0x00010000;
	public static final long ANALYSE_EVENT = 0x00100000;

	//public long change_type;
	public static final String typ = "refchange";

	public RefChangeEvent(Object source, long type)
	{
		super(source, 0, typ);

		if((type & OPEN_EVENT) != 0)
			OPEN = true;
		if((type & OPEN_ETALON_EVENT) != 0)
			OPEN_ETALON = true;
		if((type & SAVE_EVENT) != 0)
			SAVE = true;
		if((type & CLOSE_EVENT) != 0)
			CLOSE = true;
		if((type & CLOSE_ETALON_EVENT) != 0)
			CLOSE_ETALON = true;
		if((type & THRESHOLDS_CALC_EVENT) != 0)
			THRESHOLDS_CALC = true;
		if((type & SELECT_EVENT) != 0)
			SELECT = true;
		if((type & ANALYSE_EVENT) != 0)
			ANALYSE = true;
	}
}