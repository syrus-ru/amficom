/*-
 * $Id: UIStorage.java,v 1.16 2005/12/29 09:54:32 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.Client.Scheduler.General;

import java.text.SimpleDateFormat;

/**
 * Storage for scheduler gui keys
 * 
 * @version $Revision: 1.16 $, $Date: 2005/12/29 09:54:32 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler
 */
public final class UIStorage {

	public static final String				ICON_SCHEDULER_MINI		= "com.syrus.AMFICOM.icon.scheduler.mainmini";
	public static final String				ICON_DELETE				= "com.syrus.AMFICOM.icon.scheduler.delete";
	public static final String				ICON_RESUME				= "com.syrus.AMFICOM.icon.scheduler.resume";
	public static final String				ICON_PAUSE				= "com.syrus.AMFICOM.icon.scheduler.pause";

	public static final SimpleDateFormat	HOUR_MINUTE_DATE_FORMAT	= 
		new SimpleDateFormat("HH:mm");
	
	public static final String EDGE_COLOR =  "scheduler.color.edge";

	public static final String	COLOR_STOPPED						= "scheduler.color.stopped";

	public static final String	COLOR_STOPPED_SELECTED				= "scheduler.color.stoppedSelected";
	
	public static final String	COLOR_ABORTED						= "scheduler.color.aborted";

	public static final String	COLOR_ABORTED_SELECTED				= "scheduler.color.abortedSelected";

	public static final String	COLOR_ALARM							= "scheduler.color.alarm";

	public static final String	COLOR_ALARM_SELECTED				= "scheduler.color.alarmSelected";

	public static final String	COLOR_COMPLETED						= "scheduler.color.complete";

	public static final String	COLOR_COMPLETED_SELECTED			= "scheduler.color.completeSelected";

	public static final String	COLOR_PROCCESSING					= "scheduler.color.processing";

	public static final String	COLOR_PROCCESSING_SELECTED			= "scheduler.color.processingSelected";

	public static final String	COLOR_SCHEDULED						= "scheduler.color.scheduled";

	public static final String	COLOR_SCHEDULED_SELECTED			= "scheduler.color.scheduledSelected";

	public static final String	COLOR_UNRECOGNIZED					= "scheduler.color.unrecognized";

	public static final String	COLOR_WARNING						= "scheduler.color.warning";

	public static final String	COLOR_WARNING_SELECTED				= "scheduler.color.warningSelected";

	private UIStorage() {
		throw new AssertionError();
	}
}
