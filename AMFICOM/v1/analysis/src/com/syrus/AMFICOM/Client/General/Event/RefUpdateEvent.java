package com.syrus.AMFICOM.Client.General.Event;

import java.beans.PropertyChangeEvent;

public class RefUpdateEvent extends PropertyChangeEvent
{
	private long flags = 0;
	
	/*
	 * EVENT_SELECTED_EVENT:  
	 *  source = Int(#события), может быть -1 (это неправильно, но может случаться)
	 */
	
	public boolean markerMoved()
	    { return (flags & MARKER_MOVED_EVENT) != 0; }
	public boolean markerLocated()
  { return (flags & MARKER_LOCATED_EVENT) != 0; }
	public boolean thresholdChanged()
	    {return (flags & THRESHOLD_CHANGED_EVENT) != 0; }
	public boolean traceChanged()
  		{return (flags & TRACE_CHANGED_EVENT) != 0; }
	public boolean minTraceLevelChanged()
	    { return (flags & MIN_TRACE_LEVEL_CHANGED_EVENT) != 0; }

	public static final long MARKER_MOVED_EVENT 			= 0x00000001;
	public static final long MARKER_LOCATED_EVENT 			= 0x00000002;
	public static final long THRESHOLD_CHANGED_EVENT 		= 0x00100000;
	public static final long TRACE_CHANGED_EVENT 		= 0x00200000;
	public static final long MIN_TRACE_LEVEL_CHANGED_EVENT 	= 0x01000000;

	public static final String typ = "refupdate";

	public RefUpdateEvent(Object source, long type)
	{
		super(source, typ, null, null);
		flags = type;
	}
	
	public RefUpdateEvent(Object source, Object newValue, long type)
	{
		super(source, typ, null, newValue);
		flags = type;
	}
}
