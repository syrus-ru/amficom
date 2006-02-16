/*-
 * $Id: EquipmentTypeCodename.java,v 1.3.2.1 2006/02/16 13:04:08 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.configuration;

import com.syrus.AMFICOM.bugs.Crutch136;

/**
 * @version $Revision: 1.3.2.1 $, $Date: 2006/02/16 13:04:08 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module config
 */
public enum EquipmentTypeCodename {
	REFLECTOMETER("reflectometer"),
	OPTICAL_SWITCH("optical_switch"),
	MUFF("muff"),
	CABLE_PANEL("cable_panel"),
	TRANSMITTER("transmitter"),
	RECEIVER("receiver"),
	MULTIPLEXOR("multiplexor"),
	CROSS("cross"),
	FILTER("filter"),
	OTHER("other"),
	UNKNOWN("unknown"),
	RACK("rack"),

	@Crutch136(notes = "Stub for SchemeElement without Equipment")
	BUG_136("bug136");

	private String codename;

	private EquipmentTypeCodename(final String codename) {
		this.codename = codename;
	}

	public String stringValue() {
		return this.codename;
	}

	@Override
	public String toString() {
		return this.stringValue();
	}

}
