/*
 * $Id: PortTypeCodename.java,v 1.1 2005/06/23 14:41:35 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

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
		return this.getClass().getName() + " " + this.stringValue();
	}

}
