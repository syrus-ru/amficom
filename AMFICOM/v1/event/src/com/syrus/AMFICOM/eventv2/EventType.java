/*-
 * $Id: EventType.java,v 1.2 2005/10/07 14:58:57 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.eventv2.corba.IdlEventPackage.IdlEventType;
import com.syrus.util.TransferableObject;


/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/10/07 14:58:57 $
 * @module event
 */
public enum EventType implements TransferableObject<IdlEventType> {
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

	public IdlEventType getTransferable(final ORB orb) {
		try {
			return IdlEventType.from_int(this.ordinal());
		} catch (final BAD_PARAM bp) {
			throw new IllegalArgumentException(bp);
		}
	}
}
