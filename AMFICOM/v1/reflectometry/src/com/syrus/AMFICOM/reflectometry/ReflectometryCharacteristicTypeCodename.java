/*-
 * $Id: ReflectometryCharacteristicTypeCodename.java,v 1.1.2.1 2006/04/14 14:45:45 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.reflectometry;

import com.syrus.util.Codename;

/**
 * @version $Revision: 1.1.2.1 $, $Date: 2006/04/14 14:45:45 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module reflectometry
 */
public enum ReflectometryCharacteristicTypeCodename implements Codename {
	POINTS_MAX_NUMBER("points_max_number"),
	LOW_RES_MIN_PULSE_WIDTH("low_res_min_pulse_width"),
	HIGH_RES_MAX_PULSE_WIDTH("high_res_max_pulse_width");

	private String codename;

	private ReflectometryCharacteristicTypeCodename(final String codename) {
		this.codename = codename.intern();
	}

	public String stringValue() {
		return this.codename;
	}

	@Override
	public String toString() {
		return this.name() + "(" + Integer.toString(this.ordinal()) + ")";
	}
}
