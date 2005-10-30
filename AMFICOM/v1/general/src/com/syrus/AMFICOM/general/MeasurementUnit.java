/*-
 * $Id: MeasurementUnit.java,v 1.9 2005/10/30 14:49:07 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.corba.IdlMeasurementUnit;
import com.syrus.util.Codeable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.9 $, $Date: 2005/10/30 14:49:07 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public enum MeasurementUnit implements Codeable {
	NONDIMENSIONAL("nondimensional"),

	KILOMETER("km"),
	METER("m"),
	NANOMETER("nm"),

	SECOND("sec"),
	NANOSECOND("nsec"),

	UNKNOWN("unknown");

	private static final String KEY_ROOT = "MeasurementUnit.Name.";

	private final String codename;
	private final String name;


	private MeasurementUnit(final String codename) {
		this.codename = codename;
		this.name = LangModelGeneral.getString(KEY_ROOT + this.codename);
	}

	public static MeasurementUnit valueOf(final int code) {
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

			case IdlMeasurementUnit._UNKNOWN_MEASUREMENTUNIT:
				return UNKNOWN;
			default:
				Log.errorMessage("Illegal IDL code: " + code + ", returning RAW");
				return UNKNOWN;
		}
	}

	public static MeasurementUnit fromTransferable(final IdlMeasurementUnit idlMeasurementUnit) {
		return valueOf(idlMeasurementUnit.value());
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
		try {
			return IdlMeasurementUnit.from_int(this.getCode());
		} catch (org.omg.CORBA.BAD_PARAM bp) {
			Log.errorMessage("Illegal code: " + this.getCode() + ", returning UNKNOWN");
			return IdlMeasurementUnit.UNKNOWN_MEASUREMENTUNIT;
		}
	}
}
