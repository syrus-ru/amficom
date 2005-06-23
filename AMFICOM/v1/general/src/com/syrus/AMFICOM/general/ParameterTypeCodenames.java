/*
 * $Id: ParameterTypeCodenames.java,v 1.19 2005/06/23 11:01:46 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.19 $, $Date: 2005/06/23 11:01:46 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public final class ParameterTypeCodenames {

	public static final String REFLECTOGRAMMA = ParameterTypeCodename.REFLECTOGRAMMA.stringValue();
	public static final String REFLECTOGRAMMA_ETALON = ParameterTypeCodename.REFLECTOGRAMMA_ETALON.stringValue();

	public static final String TRACE_WAVELENGTH = ParameterTypeCodename.TRACE_WAVELENGTH.stringValue();
	public static final String TRACE_LENGTH = ParameterTypeCodename.TRACE_LENGTH.stringValue();
	public static final String TRACE_RESOLUTION = ParameterTypeCodename.TRACE_RESOLUTION.stringValue();
	public static final String TRACE_PULSE_WIDTH_LOW_RES = ParameterTypeCodename.TRACE_PULSE_WIDTH_LOW_RES.stringValue();
	public static final String TRACE_PULSE_WIDTH_HIGH_RES = ParameterTypeCodename.TRACE_PULSE_WIDTH_HIGH_RES.stringValue();
	public static final String TRACE_INDEX_OF_REFRACTION = ParameterTypeCodename.TRACE_INDEX_OF_REFRACTION.stringValue();
	public static final String TRACE_AVERAGE_COUNT = ParameterTypeCodename.TRACE_AVERAGE_COUNT.stringValue();
	public static final String TRACE_FLAG_GAIN_SPLICE_ON = ParameterTypeCodename.TRACE_FLAG_GAIN_SPLICE_ON.stringValue();
	public static final String TRACE_FLAG_LIVE_FIBER_DETECT = ParameterTypeCodename.TRACE_FLAG_LIVE_FIBER_DETECT.stringValue();
	@Deprecated
	public static final String TRACE_EVENTS = "traceeventarray";

	// prediction
	public static final String PREDICTION_TIME = ParameterTypeCodename.PREDICTION_TIME.stringValue();
	public static final String PREDICTION_DATA_FROM = ParameterTypeCodename.PREDICTION_DATA_FROM.stringValue();
	public static final String PREDICTION_DATA_TO = ParameterTypeCodename.PREDICTION_DATA_TO.stringValue();

	// dadara etalon and thresholds
	public static final String DADARA_ANALYSIS_RESULT = ParameterTypeCodename.DADARA_ANALYSIS_RESULT.stringValue(); // Raw com.syrus.AMFICOM.analysis.dadara.AnalysisResult
	public static final String DADARA_ETALON_MTM = ParameterTypeCodename.DADARA_ETALON_MTM.stringValue(); // raw,
																																			// ModeltraceMananger
																																			// for
																																			// etalon+thresholds
	public static final String DADARA_MIN_TRACE_LEVEL = ParameterTypeCodename.DADARA_MIN_TRACE_LEVEL.stringValue(); // double

	// dadara analysis params
	@Deprecated
	public static final String DADARA = "dadara";
	public static final String DADARA_ALARMS = ParameterTypeCodename.DADARA_ALARMS.stringValue();

	// outdated, should be delete:
//	public static final String DADARA_NOISE_FACTOR = "dadara_noise_factor"; // double
//	public static final String MIN_EVENT = "dadara_min_event"; // double
//	public static final String MIN_SPLICE = "dadara_min_splice"; // double
//	public static final String MIN_CONNECTOR = "dadara_min_connector"; // double

	// new, instead of DADARA_NOISE_FACTOR, MIN_EVENT, MIN_SPLICE, MIN_CONNECTOR
	public static final String DADARA_CRITERIA = ParameterTypeCodename.DADARA_CRITERIA.stringValue(); // raw

	@Deprecated
	public static final String ALARM_STATUS = "alarm_status";

	public static final String HZ_CHO = ParameterTypeCodename.ХУЙ_ЗНАЕТ_ЧТО.stringValue();

	private ParameterTypeCodenames() {
		// singleton
		assert false;
	}
}
