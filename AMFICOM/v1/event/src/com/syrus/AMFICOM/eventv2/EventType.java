/*-
 * $Id: EventType.java,v 1.1 2005/10/06 14:34:29 bass Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;


/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/10/06 14:34:29 $
 * @module event
 */
public enum EventType {
	REFLECTORGAM_MISMATCH("reflectogramMismatch"),
	LINE_MISMATCH("lineMismatch"),
	NOTIFICATION("notification");

	private String codename;

	private EventType(final String codename) {
		this.codename = codename;
	}

	public String getCodename() {
		return this.codename;
	}
}