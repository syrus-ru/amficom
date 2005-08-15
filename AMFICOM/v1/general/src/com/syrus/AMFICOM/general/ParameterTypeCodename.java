/*
 * $Id: ParameterTypeCodename.java,v 1.7 2005/08/15 10:38:44 arseniy Exp $
 * 
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.7 $, $Date: 2005/08/15 10:38:44 $
 * @author $Author: arseniy $
 * @module general
 */

public enum ParameterTypeCodename {
	TRACE_WAVELENGTH("ref_wvlen"),
	TRACE_LENGTH("ref_trclen"),
	TRACE_RESOLUTION("ref_res"),
	TRACE_PULSE_WIDTH_LOW_RES("ref_pulswd_low_res"),
	TRACE_PULSE_WIDTH_HIGH_RES("ref_pulswd_high_res"),
	TRACE_INDEX_OF_REFRACTION("ref_ior"),
	TRACE_AVERAGE_COUNT("ref_scans"),
	TRACE_FLAG_GAIN_SPLICE_ON("ref_flag_gain_splice_on"),
	TRACE_FLAG_LIVE_FIBER_DETECT("ref_flag_live_fiber_detect"),

	REFLECTOGRAMMA("reflectogramma"),
	REFLECTOGRAMMA_ETALON("reflectogramma_etalon"),
	DADARA_ETALON("dadara_etalon"), // Raw, com.syrus.AMFICOM.analysis.Etalon
	DADARA_CRITERIA("dadara_criteria"), // Raw
	DADARA_ANALYSIS_RESULT("dadara_analysis_result"), // Raw com.syrus.AMFICOM.analysis.dadara.AnalysisResult
	DADARA_ALARMS("dadara_alarm_array"),

	// prediction
	PREDICTION_TIME("prediction_time"),
	PREDICTION_DATA_FROM("time_start"),
	PREDICTION_DATA_TO("time_end"),

	// Other useful things
	���_�����_���("���_�����_���");


	private String codename;

	private ParameterTypeCodename(final String codename) {
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
