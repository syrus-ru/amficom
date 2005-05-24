/*
 * $Id: ParameterTypeCodenames.java,v 1.15 2005/05/24 12:24:23 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.15 $, $Date: 2005/05/24 12:24:23 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public final class ParameterTypeCodenames {

	public static final String REFLECTOGRAMMA = "reflectogramma";

	public static final String TRACE_WAVELENGTH = "ref_wvlen";
	public static final String TRACE_LENGTH = "ref_trclen";
	public static final String TRACE_RESOLUTION = "ref_res";
	public static final String TRACE_PULSE_WIDTH_LOW_RES = "ref_pulswd_low_res";
	public static final String TRACE_PULSE_WIDTH_HIGH_RES = "ref_pulswd_high_res";
	public static final String TRACE_INDEX_OF_REFRACTION = "ref_ior";
	public static final String TRACE_AVERAGE_COUNT = "ref_scans";
	public static final String TRACE_FLAG_GAIN_SPLICE_ON = "ref_flag_gain_splice_on";
	public static final String TRACE_FLAG_LIVE_FIBER_DETECT = "ref_flag_live_fiber_detect";
	public static final String TRACE_EVENTS = "traceeventarray";

	// prediction
	public static final String PREDICTION_TIME = "predictionTime";
	public static final String PREDICTION_DATA_FROM = "time_start";
	public static final String PREDICTION_DATA_TO = "time_end";

	// dadara etalon and thresholds
	public static final String DADARA_MTAE = "dadara_mtae"; // raw,
																													// ModelTraceAndEvents
																													// for analysis
																													// results
	public static final String DADARA_ETALON_MTM = "dadara_etalon_mtm"; // raw,
																																			// ModeltraceMananger
																																			// for
																																			// etalon+thresholds
	public static final String DADARA_MIN_TRACE_LEVEL = "dadara_min_trace_level"; // double

	// dadara analysis params
	public static final String DADARA = "dadara";
	public static final String DADARA_ALARMS = "dadara_alarm_array";

	// outdated, should be delete:
//	public static final String DADARA_NOISE_FACTOR = "dadara_noise_factor"; // double
//	public static final String MIN_EVENT = "dadara_min_event"; // double
//	public static final String MIN_SPLICE = "dadara_min_splice"; // double
//	public static final String MIN_CONNECTOR = "dadara_min_connector"; // double

	// new, instead of DADARA_NOISE_FACTOR, MIN_EVENT, MIN_SPLICE, MIN_CONNECTOR
	public static final String DADARA_CRITERIA = "dadara_criteria"; // raw

	public static final String ALARM_STATUS = "alarm_status";

	public static final String HZ_CHO = "hz_cho";

	private ParameterTypeCodenames() {
		// singleton
		assert false;
	}
}
