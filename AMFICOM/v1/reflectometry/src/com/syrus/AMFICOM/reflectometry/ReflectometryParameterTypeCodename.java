/*-
 * $Id: ReflectometryParameterTypeCodename.java,v 1.1.2.8 2006/04/18 16:22:05 saa Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.reflectometry;

import com.syrus.util.Codename;

/**
 * @version $Revision: 1.1.2.8 $, $Date: 2006/04/18 16:22:05 $
 * @author $Author: saa $
 * @author Tashoyan Arseniy Feliksovich
 * @module reflectometry
 */
public enum ReflectometryParameterTypeCodename implements Codename {

	// Параметры измерения
	WAVE_LENGTH("ref_wvlen"),			// int
	TRACE_LENGTH("ref_trclen"),			// double
	RESOLUTION("ref_res"),				// double
	PULSE_WIDTH_NS("ref_pulswd_ns"),	// int
	PULSE_WIDTH_M("ref_pulswd_m"),		// int
	INDEX_OF_REFRACTION("ref_ior"),		// double
	AVERAGE_COUNT("ref_scans"),			// integer
	FLAG_PULSE_WIDTH_LOW_RES("ref_flag_pulswd_low_res"),	// boolean
	FLAG_GAIN_SPLICE_ON("ref_flag_gain_splice_on"),			// boolean
	FLAG_LIFE_FIBER_DETECT("ref_flag_life_fiber_detect"),	// boolean
	FLAG_SMOOTH_FILTER("ref_flag_smooth_filter"),			// boolean

	// Результат измерения
	REFLECTOGRAMMA("reflectogramma"),	// BellcoreStructure

	// Параметры анализа/сравнения dadara
	DADARA_CRITERIA("dadara_criteria"),
	REFLECTOGRAMMA_ETALON("reflectogramma_etalon"),	// BellcoreStructure
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
		return this.name() + "(" + Integer.toString(this.ordinal()) + ")";
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
