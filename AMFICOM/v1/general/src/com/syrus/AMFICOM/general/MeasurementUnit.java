/*-
 * $Id: MeasurementUnit.java,v 1.1 2005/08/19 14:02:41 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.corba.IdlMeasurementUnit;

/**
 * @version $Revision: 1.1 $, $Date: 2005/08/19 14:02:41 $
 * @author $Author: arseniy $
 * @module general
 */
public enum MeasurementUnit {
	NONDIMENSIONAL("nondimensional"),

	KILOMETER("km"),
	METER("m"),
	NANOMETER("nm"),

	SECOND("sec"),
	NANOSECOND("nsec"),

	ïþëï("ÏÞËÏ");

	private static final String KEY_ROOT = "MeasurementUnit.Name.";

	private final String codename;
	private final String name;


	private MeasurementUnit(final String codename) {
		this.codename = codename;
		this.name = LangModelGeneral.getString(KEY_ROOT + this.codename);
	}

	public static MeasurementUnit fromInt(final int code) {
		switch (code) {
			case IdlMeasurementUnit._NONDIMENSIONAL:
				return NONDIMENSIONAL;

			case IdlMeasurementUnit._KILOMETER:
				return KILOMETER;
			case IdlMeasurementUnit._METER:
				return METER;
			case IdlMeasurementUnit._NANOMETER:
				return NANOMETER;

			case IdlMeasurementUnit._SECOND:
				return SECOND;
			case IdlMeasurementUnit._NANOSECOND:
				return NANOSECOND;

			default:
				return ïþëï;
		}
	}

	public static MeasurementUnit fromTransferable(final IdlMeasurementUnit idlMeasurementUnit) {
		return fromInt(idlMeasurementUnit.value());
	}

	public int getCode() {
		return this.ordinal();
	}

	public String getCodename() {
		return this.codename;
	}

	public String getName() {
		return this.name;
	}

	@SuppressWarnings("unused")
	public IdlMeasurementUnit getTransferable(final ORB orb) {
		return IdlMeasurementUnit.from_int(this.getCode());
	}
}
