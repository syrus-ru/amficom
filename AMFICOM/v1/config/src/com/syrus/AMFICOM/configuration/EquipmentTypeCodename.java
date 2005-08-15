/*-
 * $Id: EquipmentTypeCodename.java,v 1.1 2005/08/15 10:38:33 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

/**
 * @version $Revision: 1.1 $, $Date: 2005/08/15 10:38:33 $
 * @author $Author: arseniy $
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
