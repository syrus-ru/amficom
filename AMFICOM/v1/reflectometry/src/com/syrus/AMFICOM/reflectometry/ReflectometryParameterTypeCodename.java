/*-
 * $Id: ReflectometryParameterTypeCodename.java,v 1.1.2.6 2006/04/11 09:29:05 arseniy Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.reflectometry;

import com.syrus.util.Codename;

/**
 * @version $Revision: 1.1.2.6 $, $Date: 2006/04/11 09:29:05 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module reflectometry
 */
public enum ReflectometryParameterTypeCodename implements Codename {

	// Параметры измерения
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
	FLAG_SMOOTH_FILTER("ref_flag_smooth_filter"),

	// Результат измерения
	REFLECTOGRAMMA("reflectogramma"),

	// Параметры анализа/сравнения dadara
	DADARA_CRITERIA("dadara_criteria"),
	REFLECTOGRAMMA_ETALON("reflectogramma_etalon"),
	DADARA_ETALON("dadara_etalon"),

	// Результаты анализа/сравнения dadara
	DADARA_ANALYSIS_RESULT("dadara_analysis_result"),
	DADARA_ALARMS("dadara_alarm_array"),
	DADARA_QUALITY_PER_EVENT("dadara_quality_per_event"),
	DADARA_QUALITY_OVERALL_D("dadara_quality_overall_d"),
	DADARA_QUALITY_OVERALL_Q("dadara_quality_overall_q"),

	// Параметры прогнозирования
	PREDICTION_TIME("prediction_time"),
	PREDICTION_TIME_START("prediction_time_start"),
	PREDICTION_TIME_END("prediction_time_end");

	private String codename;

	private ReflectometryParameterTypeCodename(final String codename) {
		this.codename = codename.intern();
	}

	public String stringValue() {
		return this.codename;
	}

	@Override
	public String toString() {
		return this.stringValue();
	}

	/**
	 * FIXME: стоит ли ввести такой метод? 
	 * Проверяет совпадение this с codename, представленного строкой
	 * @param typeCodename codename, представленный строкой
	 */
	private boolean matches(final String typeCodename) {
		return this.codename.equals(typeCodename);
	}
}
