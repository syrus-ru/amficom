/*-
 * $Id: MeasurementTypeEnum.java,v 1.1 2005/08/19 17:48:49 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import java.util.EnumSet;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.TransferableObject;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementTypeEnum;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.1 $, $Date: 2005/08/19 17:48:49 $
 * @author $Author: arseniy $
 * @module measurement
 */
public enum MeasurementTypeEnum implements TransferableObject {
	REFLECTOMETRY("reflectometry",
			EnumSet.of(ParameterType.REF_WAVE_LENGTH,
					ParameterType.REF_TRACE_LENGTH,
					ParameterType.REF_RESOLUTION,
					ParameterType.REF_PULSE_WIDTH_HIGH_RES,
					ParameterType.REF_PULSE_WIDTH_LOW_RES,
					ParameterType.REF_INDEX_OF_REFRACTION,
					ParameterType.REF_AVERAGE_COUNT,
					ParameterType.REF_FLAG_GAIN_SPLICE_ON,
					ParameterType.REF_FLAG_LIFE_FIBER_DETECT),
			EnumSet.of(ParameterType.REFLECTOGRAMMA)),
	UNKNOWN("unknown",
			EnumSet.noneOf(ParameterType.class),
			EnumSet.noneOf(ParameterType.class));

	private static final String KEY_ROOT = "MeasurementType.Description.";

	private String codename;
	private EnumSet<ParameterType> inParameterTypes;
	private EnumSet<ParameterType> outParameterTypes;
	private String description;


	private MeasurementTypeEnum(final String codename,
			final EnumSet<ParameterType> inParameterTypes,
			final EnumSet<ParameterType> outParameterTypes) {
		this.codename = codename;
		this.inParameterTypes = inParameterTypes;
		this.outParameterTypes = outParameterTypes;
		this.description = LangModelMeasurement.getString(KEY_ROOT + this.codename);
	}

	public static MeasurementTypeEnum fromInt(final int code) {
		switch (code) {
			case IdlMeasurementTypeEnum._REFLECTOMETRY:
				return REFLECTOMETRY;
			default:
				Log.errorMessage("Illegal IDL measurement type: " + code);
			return UNKNOWN;
		}
	}

	public static MeasurementTypeEnum fromTransferable(final IdlMeasurementTypeEnum idlMeasurementType) {
		return fromInt(idlMeasurementType.value());
	}

	public int getCode() {
		return this.ordinal();
	}

	public String getCodename() {
		return this.codename;
	}

	public EnumSet<ParameterType> getInParameterTypes() {
		return this.inParameterTypes.clone();
	}

	public EnumSet<ParameterType> getOutParameterTypes() {
		return this.outParameterTypes.clone();
	}

	public String getDescription() {
		return this.description;
	}

	@SuppressWarnings("unused")
	public IdlMeasurementTypeEnum getTransferable(final ORB orb) {
		return IdlMeasurementTypeEnum.from_int(this.getCode());
	}

}
