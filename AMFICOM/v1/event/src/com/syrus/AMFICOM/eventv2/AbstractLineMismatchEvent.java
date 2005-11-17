/*-
 * $Id: AbstractLineMismatchEvent.java,v 1.3 2005/11/17 16:21:38 bass Exp $
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
 * @version $Revision: 1.3 $, $Date: 2005/11/17 16:21:38 $
 * @module event
 */
public abstract class AbstractLineMismatchEvent implements LineMismatchEvent {
	private static final char NEWLINE = '\n';

	private static final char SPACE = ' ';

	private static final String COLON_TAB = ":\t";

	private static final String PHYSICAL_DISTANCE_TO = I18N.getString("NotificationEvent.PhysicalDistanceTo");

	private static final String METERS = I18N.getString("NotificationEvent.Meters");

	private static final String END_OF = I18N.getString("NotificationEvent.EndOf");

	private static final String START_OF = I18N.getString("NotificationEvent.StartOf");

	private static final String PATH_ELEMENT_GENITIVE = I18N.getString("NotificationEvent.PathElementGenitive");

	private static final String PATH_ELEMENT = I18N.getString("NotificationEvent.PathElement");

	private static final String AFFECTED = I18N.getString("NotificationEvent.Affected");

	private static final String MISMATCH_LEVEL = I18N.getString("NotificationEvent.MismatchLevel");

	private static final String MAXIMUM = I18N.getString("NotificationEvent.Maximum");

	private static final String MINIMUM = I18N.getString("NotificationEvent.Minimum");

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

	/**
	 * @see LineMismatchEvent#getMessage()
	 */
	public final String getMessage() {
		return this.getAlarmType().getLocalizedDescription() + NEWLINE
				+ this.getSeverity().getLocalizedDescription() + NEWLINE
				+ (this.hasMismatch()
						? MINIMUM + SPACE + MISMATCH_LEVEL + COLON_TAB + this.getMinMismatch() + NEWLINE
						+ MAXIMUM + SPACE + MISMATCH_LEVEL + COLON_TAB + this.getMaxMismatch() + NEWLINE
						: "")
				+ AFFECTED + SPACE + PATH_ELEMENT + COLON_TAB + this.getAffectedPathElementId() + NEWLINE
				+ (this.isAffectedPathElementSpacious()
						? PHYSICAL_DISTANCE_TO + SPACE + START_OF + SPACE + PATH_ELEMENT_GENITIVE + COLON_TAB + ((int) this.getPhysicalDistanceToStart()) + SPACE + METERS + NEWLINE
						+ PHYSICAL_DISTANCE_TO + SPACE + END_OF + SPACE + PATH_ELEMENT_GENITIVE + COLON_TAB + ((int) this.getPhysicalDistanceToEnd()) + SPACE + METERS + NEWLINE
						: "");
	}
}
