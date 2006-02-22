/*-
 * $Id: AnalysisTypeCodename.java,v 1.1.2.1 2006/02/22 10:06:26 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

/**
 * @version $Revision: 1.1.2.1 $, $Date: 2006/02/22 10:06:26 $
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
		return this.getClass().getName() + " " + this.stringValue();
	}
}
