/*-
 * $Id: EmailAddressRegexp.java,v 1.1 2005/11/02 15:03:47 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util.mail;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/11/02 15:03:47 $
 * @module util
 */
public enum EmailAddressRegexp {
	/**
	 * Regexp suitable for validation of local addresses. Courtesy of
	 * Bugzilla.
	 */
	LOCAL("^[^@]+$"),
	/**
	 * Regexp suitable for validation of fully qualified addresses. Courtesy
	 * of Bugzilla.
	 */
	ADVANCED("^[^@]+@[^@]+\\.[^@]+$"),
	/**
	 * An alternative to Bugzilla's Advanced, courtesy of Vladimir ``Bob''
	 * Dolzhenko.
	 */
	BOB_S_ADVANCED("^[-a-z0-9_.]+@([-a-z0-9]+\\.)+[a-z]{2,4}$");

	private String value;

	private EmailAddressRegexp(final String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
