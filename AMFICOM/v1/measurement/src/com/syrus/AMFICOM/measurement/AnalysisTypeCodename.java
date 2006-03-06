/*-
 * $Id: AnalysisTypeCodename.java,v 1.1.2.2 2006/03/06 12:21:33 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

/**
 * @version $Revision: 1.1.2.2 $, $Date: 2006/03/06 12:21:33 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public enum AnalysisTypeCodename {
	DADARA("dadara");

	private String codename;

	private AnalysisTypeCodename(final String codename) {
		this.codename = codename;
	}

	public String stringValue() {
		return this.codename;
	}

	@Override
	public String toString() {
		return this.name() + "(" + Integer.toString(this.ordinal()) + ")";
	}
}
