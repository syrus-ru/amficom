/*
 * $Id: PortTypeCodename.java,v 1.2 2005/08/15 10:38:36 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

/**
 * @version $Revision: 1.2 $, $Date: 2005/08/15 10:38:36 $
 * @author $Author: arseniy $
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
		return this.getClass().getName() + " " + this.stringValue();
	}

}
