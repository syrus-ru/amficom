/*-
 * $Id: TransmissionPathTypeCodename.java,v 1.2 2006/04/19 13:22:15 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.configuration;

/**
 * @version $Revision: 1.2 $, $Date: 2006/04/19 13:22:15 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module config
 */
public enum TransmissionPathTypeCodename {
	OPTICAL("optical");

	private String codename;

	private TransmissionPathTypeCodename(final String codename) {
		this.codename = codename;
	}

	public String stringValue() {
		return this.codename;
	}

	@Override
	public String toString() {
		return this.getClass().getName() + " " + this.stringValue();
	}

}
