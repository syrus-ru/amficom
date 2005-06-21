/*
 * $Id: ParameterTypeCodename.java,v 1.1 2005/06/21 14:55:20 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

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
	DADARA_ANALYSIS_RESULT("dadara_analysis_result"), // Raw com.syrus.AMFICOM.analysis.dadara.AnalysisResult
	DADARA_ALARMS("dadara_alarm_array"),

	PREDICTION_TIME("prediction_time"),
	PREDICTION_DATA_FROM("time_start"),
	PREDICTION_DATA_TO("time_end");


	private String codename;

	private ParameterTypeCodename(final String codename) {
		this.codename = codename;
	}

	@Override
	public String toString() {
		return this.codename;
	}
}
