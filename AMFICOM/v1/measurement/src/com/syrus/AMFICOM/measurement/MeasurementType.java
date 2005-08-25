/*-
 * $Id: MeasurementType.java,v 1.97 2005/08/25 20:13:56 arseniy Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementType;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.97 $, $Date: 2005/08/25 20:13:56 $
 * @author $Author: arseniy $
 * @module measurement
 */
public enum MeasurementType implements ActionType {
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


	private MeasurementType(final String codename,
			final EnumSet<ParameterType> inParameterTypes,
			final EnumSet<ParameterType> outParameterTypes) {
		this.codename = codename;
		this.inParameterTypes = inParameterTypes;
		this.outParameterTypes = outParameterTypes;
		this.description = LangModelMeasurement.getString(KEY_ROOT + this.codename);
	}

	public static MeasurementType fromInt(final int code) {
		switch (code) {
			case IdlMeasurementType._REFLECTOMETRY:
				return REFLECTOMETRY;
			default:
				Log.errorMessage("Illegal IDL measurement type: " + code);
			return UNKNOWN;
		}
	}

	public static MeasurementType fromTransferable(final IdlMeasurementType idlMeasurementType) {
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
	public IdlMeasurementType getTransferable(final ORB orb) {
		return IdlMeasurementType.from_int(this.getCode());
	}

	public static IdlMeasurementType[] createTransferables(final EnumSet<MeasurementType> measurementTypes, final ORB orb) {
		assert measurementTypes != null: NON_NULL_EXPECTED;

		final IdlMeasurementType[] idlMeasurementTypes = new IdlMeasurementType[measurementTypes.size()];
		int i = 0;
		synchronized (measurementTypes) {
			for (final MeasurementType measurementType : measurementTypes) {
				idlMeasurementTypes[i++] = measurementType.getTransferable(orb);
			}
		}
		return idlMeasurementTypes;
	}

	public static EnumSet<MeasurementType> fromTransferables(final IdlMeasurementType[] idlMeasurementTypes) {
		assert idlMeasurementTypes != null: NON_NULL_EXPECTED;

		final Collection<MeasurementType> measurementTypes = new HashSet<MeasurementType>(idlMeasurementTypes.length);
		for (final IdlMeasurementType idlParameterType : idlMeasurementTypes) {
			measurementTypes.add(MeasurementType.fromTransferable(idlParameterType));
		}
		return EnumSet.copyOf(measurementTypes);
	}
}
