package com.syrus.AMFICOM.Client.General.Event;

public class RefUpdateEvent extends OperationEvent
{
	public boolean MARKER_MOVED = false;
	public boolean ANALYSIS_PERFORMED = false;
	public boolean EVENT_SELECTED = false;
	public boolean COLORS_CHANGED = false;
	public boolean THRESHOLD_CHANGED = false;
	public boolean THRESHOLDS_UPDATED = false;
	public boolean MIN_TRACE_LEVEL_CHANGED = false;
	public boolean CONCAVITY_SELECTED = false;

	public static final long MARKER_MOVED_EVENT = 0x00000001;
	public static final long COLORS_CHANGED_EVENT = 0x00000010;
	public static final long ANALYSIS_PERFORMED_EVENT = 0x00000100;
	public static final long EVENT_SELECTED_EVENT = 0x00001000;
	public static final long CONCAVITY_SELECTED_EVENT = 0x00010000;
	public static final long THRESHOLD_CHANGED_EVENT = 0x00100000;
	public static final long THRESHOLDS_UPDATED_EVENT = 0x00200000;
	public static final long MIN_TRACE_LEVEL_CHANGED_EVENT = 0x01000000;

	public long change_type;
	public static final String typ = "refupdate";

	public RefUpdateEvent(Object source, long type)
	{
		super(source, 0, typ);
		change_type = type;

		if((type & MARKER_MOVED_EVENT) != 0)
			MARKER_MOVED = true;
		if((type & COLORS_CHANGED_EVENT) != 0)
			COLORS_CHANGED = true;
		if((type & ANALYSIS_PERFORMED_EVENT) != 0)
			ANALYSIS_PERFORMED = true;
		if((type & EVENT_SELECTED_EVENT) != 0)
			EVENT_SELECTED = true;
		if((type & CONCAVITY_SELECTED_EVENT) != 0)
			CONCAVITY_SELECTED = true;
		if((type & THRESHOLD_CHANGED_EVENT) != 0)
			THRESHOLD_CHANGED = true;
	if((type & THRESHOLDS_UPDATED_EVENT) != 0)
			THRESHOLDS_UPDATED = true;
		if((type & MIN_TRACE_LEVEL_CHANGED_EVENT) != 0)
			MIN_TRACE_LEVEL_CHANGED = true;
	}
}