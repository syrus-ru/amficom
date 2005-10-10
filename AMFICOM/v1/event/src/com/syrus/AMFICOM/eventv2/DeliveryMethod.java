/*-
 * $Id: DeliveryMethod.java,v 1.2 2005/10/10 14:28:36 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/10/10 14:28:36 $
 * @module event
 */
public enum DeliveryMethod {
	EMAIL("email"),
	SMS("sms"),
	POPUP("popup");

	private String codename;

	private DeliveryMethod(final String codename) {
		this.codename = codename;
	}

	public String getCodename() {
		return this.codename;
	}
}
