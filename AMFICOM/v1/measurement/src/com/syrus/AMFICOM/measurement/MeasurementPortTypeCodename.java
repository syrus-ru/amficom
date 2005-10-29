/*-
 * $Id: MeasurementPortTypeCodename.java,v 1.2 2005/10/29 20:38:48 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

/**
 * @version $Revision: 1.2 $, $Date: 2005/10/29 20:38:48 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public enum MeasurementPortTypeCodename {
	REFLECTOMETRY_QP1640A("reflectometry_qp1640a"),
	REFLECTOMETRY_PK7600("reflectometry_pk7600"),

	UNKNOWN("unknown");

	private String codename;

	private MeasurementPortTypeCodename(final String codename) {
		this.codename = codename;
	}

	public String stringValue() {
		return this.codename;
	}

	@Override
	public final String toString() {
		return this.stringValue();
	}
}
