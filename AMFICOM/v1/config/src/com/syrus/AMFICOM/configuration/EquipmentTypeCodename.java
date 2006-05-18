/*-
 * $Id: EquipmentTypeCodename.java,v 1.4 2006/04/19 13:22:15 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.configuration;

import com.syrus.AMFICOM.bugs.Crutch136;
import com.syrus.util.Codename;

/**
 * @version $Revision: 1.4 $, $Date: 2006/04/19 13:22:15 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module config
 */
public enum EquipmentTypeCodename implements Codename {
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
