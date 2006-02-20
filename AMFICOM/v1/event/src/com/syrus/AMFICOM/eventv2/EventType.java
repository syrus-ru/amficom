/*-
 * $Id: EventType.java,v 1.5 2006/02/20 17:14:56 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.eventv2.corba.IdlEventPackage.IdlEventType;
import com.syrus.util.transport.idl.IdlTransferableObject;


/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.5 $, $Date: 2006/02/20 17:14:56 $
 * @module event
 */
public enum EventType implements IdlTransferableObject<IdlEventType> {
	REFLECTORGAM_MISMATCH("reflectogramMismatch"),
	LINE_MISMATCH("lineMismatch"),
	NOTIFICATION("notification"),
	MEASUREMENT_STATUS_CHANGED("measurement_status_changed");

	private String codename;

	private EventType(final String codename) {
		this.codename = codename;
	}

	public String getCodename() {
		return this.codename;
	}

	public IdlEventType getIdlTransferable(final ORB orb) {
		try {
			return IdlEventType.from_int(this.ordinal());
		} catch (final BAD_PARAM bp) {
			throw new IllegalArgumentException(bp);
		}
	}
}
