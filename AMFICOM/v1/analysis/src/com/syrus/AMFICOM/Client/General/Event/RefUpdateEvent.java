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
	public boolean colorsChanged()
		{ return (flags & COLORS_CHANGED_EVENT) != 0; }
	public boolean analysisPerformed()
		{ return (flags & ANALYSIS_PERFORMED_EVENT) != 0; }
	public boolean eventSelected()
	    { return (flags & EVENT_SELECTED_EVENT) != 0; }
	public boolean thresholdChanged()
	    {return (flags & THRESHOLD_CHANGED_EVENT) != 0; }
	public boolean thresholdsUpdated()
	    { return (flags & THRESHOLDS_UPDATED_EVENT) != 0; }
	public boolean minTraceLevelChanged()
	    { return (flags & MIN_TRACE_LEVEL_CHANGED_EVENT) != 0; }
	public boolean modelFunctionChanged()
    { return (flags & MODEL_CHANGED_EVENT) != 0; }

	public static final long MARKER_MOVED_EVENT 			= 0x00000001;
	public static final long COLORS_CHANGED_EVENT 			= 0x00000010;
	public static final long ANALYSIS_PERFORMED_EVENT 		= 0x00000100;
	public static final long EVENT_SELECTED_EVENT 			= 0x00001000;
	public static final long THRESHOLD_CHANGED_EVENT 		= 0x00100000;
	public static final long THRESHOLDS_UPDATED_EVENT 		= 0x00200000;
	public static final long MIN_TRACE_LEVEL_CHANGED_EVENT 	= 0x01000000;
	public static final long MODEL_CHANGED_EVENT 			= 0x00000002;

	public static final String typ = "refupdate";

	public RefUpdateEvent(Object source, long type)
	{
		super(source, 0, typ);
		flags = type;
	}
}
