/*-
 * $Id: EmailAddressRegexp.java,v 1.3 2005/11/22 11:46:42 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util.mail;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/11/22 11:46:42 $
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
	ADVANCED("^[\\w\\.\\+\\-=]+@[\\w\\.\\-]+\\.[\\w\\-]+$"),
	/**
	 * <p>An alternative to Bugzilla's Advanced, courtesy of Vladimir ``Bob''
	 * Dolzhenko.</p>
	 *
	 * <p>The pattern has been slightly modified from its original value to
	 * allow validation of the following addresses:<ul>
	 * <li><a href = "mailto:bass@localhost">bass@localhost</a>;</li>
	 * <li><a href = "mailto:bass@localhost.localdomain">bass@localhost.localdomain</a>;</li>
	 * <li><a href = "mailto:bass@2ka">bass@2ka</a>;</li>
	 * <li><a href = "mailto:bass@2ka.mipt.ru">bass@2ka.mipt.ru</a>;</li>
	 * <li><a href = "mailto:bass@194.85.82.65">bass@194.85.82.65</a>;</li>
	 * <li><a href = "mailto:bass@bass.science.syrus.ru">bass@bass.science.syrus.ru</a>;</li>
	 * <li><a href = "mailto:bass@sfv240-01">bass@sfv240-01</a>.</li></ul></p>
	 */
	BOB_S_ADVANCED("^[-a-z0-9_.]+@[-a-z0-9]+(\\.[-a-z0-9]+)*$");

	private String value;

	private EmailAddressRegexp(final String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
