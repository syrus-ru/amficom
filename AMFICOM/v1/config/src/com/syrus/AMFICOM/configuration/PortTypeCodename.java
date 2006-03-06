/*
 * $Id: PortTypeCodename.java,v 1.3.2.1 2006/03/06 12:18:15 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

/**
 * @version $Revision: 1.3.2.1 $, $Date: 2006/03/06 12:18:15 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module config
 */

public enum PortTypeCodename {
	FC_PC("fc_pc");

	private String codename;

	private PortTypeCodename(final String codename) {
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
