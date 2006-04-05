/*-
 * $Id: ModelingTypeCodename.java,v 1.1.2.3 2006/04/05 09:44:49 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import com.syrus.util.Codename;

/**
 * @version $Revision: 1.1.2.3 $, $Date: 2006/04/05 09:44:49 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public enum ModelingTypeCodename implements Codename {
	REFLECTOMETRY("reflectometry");

	private String codename;

	private ModelingTypeCodename(final String codename) {
		this.codename = codename;
	}

	public String stringValue() {
		return this.codename;
	}

	@Override
	public String toString() {
		return this.name() + "(" + Integer.toString(this.ordinal()) + ", " + this.stringValue() + ")";
	}
}
