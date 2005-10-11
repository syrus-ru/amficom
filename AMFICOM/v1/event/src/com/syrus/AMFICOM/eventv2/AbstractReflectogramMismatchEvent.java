/*-
 * $Id: AbstractReflectogramMismatchEvent.java,v 1.2 2005/10/11 13:46:49 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import static com.syrus.AMFICOM.eventv2.EventType.REFLECTORGAM_MISMATCH;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/10/11 13:46:49 $
 * @module event
 */
public abstract class AbstractReflectogramMismatchEvent
		implements ReflectogramMismatchEvent {
	public final EventType getType() {
		return REFLECTORGAM_MISMATCH;
	}

	protected String paramString() {
		return "alarmType = " + this.getAlarmType()
				+ "; severity = " + this.getSeverity()
				+ "; monitoredElementId = " + this.getMonitoredElementId();
	}

	@Override
	public final String toString() {
		return this.getClass().getName() + "[" + this.paramString() + "]";
	}
}
