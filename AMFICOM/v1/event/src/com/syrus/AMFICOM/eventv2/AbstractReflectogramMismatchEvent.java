/*-
 * $Id: AbstractReflectogramMismatchEvent.java,v 1.5 2005/11/10 11:48:40 bass Exp $
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
 * @version $Revision: 1.5 $, $Date: 2005/11/10 11:48:40 $
 * @module event
 */
public abstract class AbstractReflectogramMismatchEvent
		implements ReflectogramMismatchEvent {
	public final EventType getType() {
		return REFLECTORGAM_MISMATCH;
	}

	protected String paramString() {
		final double deltaX = this.getDeltaX();
		final int coord = this.getCoord();
		return "alarmType = " + this.getAlarmType()
				+ "; severity = " + this.getSeverity()
				+ "; resultId = " + this.getResultId()
				+ "; deltaX = " + deltaX
				+ "; coord = " + coord
				+ "; distance = " + this.getDistance() + " = " + deltaX + " * " + coord;
	}

	@Override
	public final String toString() {
		return this.getClass().getName() + "[" + this.paramString() + "]";
	}
}
