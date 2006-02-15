/*-
 * $Id: ParameterCodename.java,v 1.1.2.1 2006/02/15 19:50:11 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.reflectometry;

/**
 * @version $Revision: 1.1.2.1 $, $Date: 2006/02/15 19:50:11 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module reflectometry
 */
public enum ParameterCodename {
	WAVE_LENGTH("ref_wvlen"),
	TRACE_LENGTH("ref_trclen"),
	RESOLUTION("ref_res"),
	PULSE_WIDTH_NS("ref_pulswd_ns"),
	PULSE_WIDTH_M("ref_pulswd_m"),
	INDEX_OF_REFRACTION("ref_ior"),
	AVERAGE_COUNT("ref_scans"),
	FLAG_PULSE_WIDTH_LOW_RES("ref_flag_pulswd_low_res"),
	FLAG_GAIN_SPLICE_ON("ref_flag_gain_splice_on"),
	FLAG_LIFE_FIBER_DETECT("ref_flag_life_fiber_detect"),

	REFLECTOGRAMMA("reflectogramma"),

	DADARA_QUALITY_PER_EVENT("dadara_quality_per_event"),
	DADARA_QUALITY_OVERALL_D("dadara_quality_overall_d"),
	DADARA_QUALITY_OVERALL_Q("dadara_quality_overall_q"),

	PREDICTION_TIME("prediction_time"),
	PREDICTION_TIME_START("prediction_time_start"),
	PREDICTION_TIME_END("prediction_time_end");

	private String codename;

	private ParameterCodename(final String codename) {
		this.codename = codename;
	}

	public String stringValue() {
		return this.codename;
	}

	@Override
	public String toString() {
		return this.stringValue();
	}
}
