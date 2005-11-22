/*-
 * $Id: AbstractLineMismatchEvent.java,v 1.4 2005/11/22 19:33:13 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import static com.syrus.AMFICOM.eventv2.EventType.LINE_MISMATCH;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/11/22 19:33:13 $
 * @module event
 */
public abstract class AbstractLineMismatchEvent implements LineMismatchEvent {
	public final EventType getType() {
		return LINE_MISMATCH;
	}

	protected String paramString() {
		return "alarmType = " + this.getAlarmType()
				+ "; severity = " + this.getSeverity()
				+ "; resultId = " + this.getResultId();
	}

	@Override
	public final String toString() {
		return this.getClass().getName() + "[" + this.paramString() + "]";
	}
}
