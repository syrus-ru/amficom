/*-
 * $Id: EquipmentTypeCodename.java,v 1.2 2005/09/14 18:42:07 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

/**
 * @version $Revision: 1.2 $, $Date: 2005/09/14 18:42:07 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module config
 */

public enum EquipmentTypeCodename {
	REFLECTOMETER("reflectometer"),
	SWITCH("switch"),
	MUFF("muff"),
	CABLE_PANEL("cable_panel"),
	TRANSMITTER("transmitter"),
	RECEIVER("receiver"),
	MULTIPLEXOR("multiplexor"),
	CROSS("cross"),
	FILTER("filter"),
	OTHER("other");

	private String codename;

	private EquipmentTypeCodename(final String codename) {
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
