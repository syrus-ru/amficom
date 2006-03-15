/*-
 * $Id: MeasurementUnit.java,v 1.14 2006/03/15 15:17:43 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.corba.IdlMeasurementUnit;
import com.syrus.util.Log;
import com.syrus.util.transport.idl.IdlTransferableObject;

/**
 * @version $Revision: 1.14 $, $Date: 2006/03/15 15:17:43 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public enum MeasurementUnit implements IdlTransferableObject<IdlMeasurementUnit> {
	NONDIMENSIONAL("nondimensional"),

	KILOMETER("km"),
	METER("m"),
	NANOMETER("nm"),

	SECOND("sec"),
	NANOSECOND("nsec"),

	UNKNOWN("unknown");

	private static final MeasurementUnit VALUES[] = values();

	private static final String KEY_ROOT = "MeasurementUnit.Name.";

	private final String codename;
	private final String name;


	private MeasurementUnit(final String codename) {
		this.codename = codename;
		this.name = LangModelGeneral.getString(KEY_ROOT + this.codename);
	}

	public static MeasurementUnit valueOf(final int code) {
		try {
			return VALUES[code];
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			Log.errorMessage("Illegal code: " + code + ", returning UNKNOWN");
			return UNKNOWN;
		}
	}

	public static MeasurementUnit valueOf(final IdlMeasurementUnit idlMeasurementUnit) {
		return valueOf(idlMeasurementUnit.value());
	}

	public String getCodename() {
		return this.codename;
	}

	public String getName() {
		return this.name;
	}

	public IdlMeasurementUnit getIdlTransferable(final ORB orb) {
		return this.getIdlTransferable();
	}

	public IdlMeasurementUnit getIdlTransferable() {
		try {
			return IdlMeasurementUnit.from_int(this.ordinal());
		} catch (final BAD_PARAM bp) {
			Log.errorMessage("Illegal code: " + this.ordinal() + ", returning UNKNOWN");
			return IdlMeasurementUnit.UNKNOWN_MEASUREMENTUNIT;
		}
	}

	public String stringValue() {
		return this.codename;
	}

	@Override
	public String toString() {
		return this.name() + "(" + Integer.toString(this.ordinal()) + ", " + this.stringValue() + ")";
	}
}
