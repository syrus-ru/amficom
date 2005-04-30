package com.syrus.AMFICOM.Client.General.Event;

public class RefUpdateEvent extends OperationEvent
{
	private long flags = 0;
	
	/*
	 * EVENT_SELECTED_EVENT:  
	 *  source = Int(#события), может быть -1 (это неправильно, но может случаться)
	 */
	
	public boolean markerMoved()
	    { return (flags & MARKER_MOVED_EVENT) != 0; }
	public boolean thresholdChanged()
	    {return (flags & THRESHOLD_CHANGED_EVENT) != 0; }
	public boolean minTraceLevelChanged()
	    { return (flags & MIN_TRACE_LEVEL_CHANGED_EVENT) != 0; }

	public static final long MARKER_MOVED_EVENT 			= 0x00000001;
	public static final long THRESHOLD_CHANGED_EVENT 		= 0x00100000;
	public static final long MIN_TRACE_LEVEL_CHANGED_EVENT 	= 0x01000000;

	public static final String typ = "refupdate";

	public RefUpdateEvent(Object source, long type)
	{
		super(source, 0, typ);
		flags = type;
	}
}
