/*-
 * $Id: ParameterType.java,v 1.51 2005/08/19 15:50:00 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;

import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.corba.IdlParameterTypeEnum;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.51 $, $Date: 2005/08/19 15:50:00 $
 * @author $Author: arseniy $
 * @module general
 */
public enum ParameterType implements TransferableObject {
	REF_WAVE_LENGTH("ref_wvlen", DataType.INTEGER, MeasurementUnit.NANOMETER),
	REF_TRACE_LENGTH("ref_trclen", DataType.DOUBLE, MeasurementUnit.KILOMETER),
	REF_RESOLUTION("ref_res", DataType.DOUBLE, MeasurementUnit.METER),
	REF_PULSE_WIDTH_LOW_RES("ref_pulswd_low_res", DataType.INTEGER, MeasurementUnit.NANOSECOND),
	REF_PULSE_WIDTH_HIGH_RES("ref_pulswd_high_res", DataType.INTEGER, MeasurementUnit.NANOSECOND),
	REF_INDEX_OF_REFRACTION("ref_ior", DataType.DOUBLE, MeasurementUnit.NONDIMENSIONAL),
	REF_AVERAGE_COUNT("ref_scans", DataType.DOUBLE, MeasurementUnit.NONDIMENSIONAL),
	REF_FLAG_GAIN_SPLICE_ON("ref_flag_gain_splice_on", DataType.BOOLEAN, MeasurementUnit.NONDIMENSIONAL),
	REF_FLAG_LIFE_FIBER_DETECT("ref_flag_life_fiber_detect", DataType.BOOLEAN, MeasurementUnit.NONDIMENSIONAL),

	REFLECTOGRAMMA("reflectogramma", DataType.RAW, MeasurementUnit.NONDIMENSIONAL),
	REFLECTOGRAMMA_ETALON("reflectogramma_etalon", DataType.RAW, MeasurementUnit.NONDIMENSIONAL),
	DADARA_ETALON("dadara_etalon", DataType.RAW, MeasurementUnit.NONDIMENSIONAL), // Raw, com.syrus.AMFICOM.analysis.Etalon
	DADARA_CRITERIA("dadara_criteria", DataType.RAW, MeasurementUnit.NONDIMENSIONAL), // Raw
	DADARA_ANALYSIS_RESULT("dadara_analysis_result", DataType.RAW, MeasurementUnit.NONDIMENSIONAL), // Raw com.syrus.AMFICOM.analysis.dadara.AnalysisResult
	DADARA_ALARMS("dadara_alarm_array", DataType.RAW, MeasurementUnit.NONDIMENSIONAL),

	// Prediction
	PREDICTION_TIME("prediction_time", DataType.DATE, MeasurementUnit.SECOND),
	PREDICTION_TIME_START("prediction_time_start", DataType.DATE, MeasurementUnit.SECOND),
	PREDICTION_TIME_END("prediction_time_end", DataType.DATE, MeasurementUnit.SECOND),

	// Other useful things
	óéîõó_÷_÷ïåîîïå_÷òåíñ("ÓÉÎÕÓ_×_×ÏÅÎÎÏÅ_×ÒÅÍÑ", DataType.RAW, MeasurementUnit.ïþëï);

	private static final String KEY_ROOT = "ParameterType.Description.";

	private String codename;
	private DataType dataType;
	private MeasurementUnit measurementUnit;
	private String description;


	private ParameterType(final String codename, final DataType dataType, final MeasurementUnit measurementUnit) {
		this.codename = codename;
		this.dataType = dataType;
		this.measurementUnit = measurementUnit;
		this.description = LangModelGeneral.getString(KEY_ROOT + this.codename);
	}

	public static ParameterType fromInt(final int code) {
		switch (code) {
			case IdlParameterTypeEnum._REF_WAVE_LENGTH:
				return REF_WAVE_LENGTH;
			case IdlParameterTypeEnum._REF_TRACE_LENGTH:
				return REF_TRACE_LENGTH;
			case IdlParameterTypeEnum._REF_RESOLUTION:
				return REF_RESOLUTION;
			case IdlParameterTypeEnum._REF_PULSE_WIDTH_LOW_RES:
				return REF_PULSE_WIDTH_LOW_RES;
			case IdlParameterTypeEnum._REF_PULSE_WIDTH_HIGH_RES:
				return REF_PULSE_WIDTH_HIGH_RES;
			case IdlParameterTypeEnum._REF_INDEX_OF_REFRACTION:
				return REF_INDEX_OF_REFRACTION;
			case IdlParameterTypeEnum._REF_AVERAGE_COUNT:
				return REF_AVERAGE_COUNT;
			case IdlParameterTypeEnum._REF_FLAG_GAIN_SPLICE_ON:
				return REF_FLAG_GAIN_SPLICE_ON;
			case IdlParameterTypeEnum._REF_FLAG_LIFE_FIBER_DETECT:
				return REF_FLAG_LIFE_FIBER_DETECT;

			case IdlParameterTypeEnum._REFLECTOGRAMMA:
				return REFLECTOGRAMMA;
			case IdlParameterTypeEnum._REFLECTOGRAMMA_ETALON:
				return REFLECTOGRAMMA_ETALON;
			case IdlParameterTypeEnum._DADARA_ETALON:
				return DADARA_ETALON;
			case IdlParameterTypeEnum._DADARA_CRITERIA:
				return DADARA_CRITERIA;
			case IdlParameterTypeEnum._DADARA_ANALYSIS_RESULT:
				return DADARA_ANALYSIS_RESULT;
			case IdlParameterTypeEnum._DADARA_ALARMS:
				return DADARA_ALARMS;

			case IdlParameterTypeEnum._PREDICTION_TIME:
				return PREDICTION_TIME;
			case IdlParameterTypeEnum._PREDICTION_TIME_START:
				return PREDICTION_TIME_START;
			case IdlParameterTypeEnum._PREDICTION_TIME_END:
				return PREDICTION_TIME_END;

			default:
				Log.errorMessage("Illegal IDL parameter type: " + code);
				return óéîõó_÷_÷ïåîîïå_÷òåíñ;
		}
	}

	public static ParameterType fromTransferable(final IdlParameterTypeEnum idlParameterTypeEnum) {
		return fromInt(idlParameterTypeEnum.value());
	}

	public int getCode() {
		return this.ordinal();
	}

	public String getCodename() {
		return this.codename;
	}

	public DataType getDataType() {
		return this.dataType;
	}

	public MeasurementUnit getMeasurementUnit() {
		return this.measurementUnit;
	}

	public String getDescription() {
		return this.description;
	}

	@SuppressWarnings("unused")
	public IdlParameterTypeEnum getTransferable(final ORB orb) {
		return IdlParameterTypeEnum.from_int(this.getCode());
	}

	public static IdlParameterTypeEnum[] createTransferables(final Set<ParameterType> parameterTypes, final ORB orb) {
		assert parameterTypes != null: NON_NULL_EXPECTED;

		final IdlParameterTypeEnum[] idlParameterTypes = new IdlParameterTypeEnum[parameterTypes.size()];
		int i = 0;
		synchronized (parameterTypes) {
			for (final ParameterType parameterType : parameterTypes) {
				idlParameterTypes[i++] = parameterType.getTransferable(orb);
			}
		}
		return idlParameterTypes;
	}

	public static Set<ParameterType> fromTransferables(final IdlParameterTypeEnum[] idlParameterTypes) {
		final Set<ParameterType> parameterTypes = new HashSet<ParameterType>(idlParameterTypes.length);
		for (final IdlParameterTypeEnum idlParameterType : idlParameterTypes) {
			parameterTypes.add(ParameterType.fromTransferable(idlParameterType));
		}
		return parameterTypes;
	}
}
