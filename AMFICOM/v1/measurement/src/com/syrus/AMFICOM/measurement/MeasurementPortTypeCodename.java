/*-
 * $Id: MeasurementPortTypeCodename.java,v 1.1 2005/10/29 16:57:57 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

/**
 * @version $Revision: 1.1 $, $Date: 2005/10/29 16:57:57 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public enum MeasurementPortTypeCodename {
	REFLECTOMETRY_QP1640("reflectometry_qp1640"),
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
