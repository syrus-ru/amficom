/*-
 * $Id: MeasurementPortTypeCodename.java,v 1.1.2.1 2006/04/17 09:40:55 arseniy Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.reflectometry;

import com.syrus.util.Codename;

/**
 * @version $Revision: 1.1.2.1 $, $Date: 2006/04/17 09:40:55 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public enum MeasurementPortTypeCodename implements Codename {
	REFLECTOMETRY_QP1640A("reflectometry_qp1640a"),
	REFLECTOMETRY_QP1643A("reflectometry_qp1643a"),
	REFLECTOMETRY_PK7600("reflectometry_pk7600");

	private String codename;

	private MeasurementPortTypeCodename(final String codename) {
		this.codename = codename.intern();
	}

	public String stringValue() {
		return this.codename;
	}

	@Override
	public String toString() {
		return this.name() + "(" + Integer.toString(this.ordinal()) + ", " + this.stringValue() + ")";
	}
}