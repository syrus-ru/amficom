/*
 * $Id: ParameterTypeCodenames.java,v 1.3 2005/02/08 10:47:09 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.3 $, $Date: 2005/02/08 10:47:09 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public class ParameterTypeCodenames {

	public static final String	REFLECTOGRAMMA				= "reflectogramma";

	public static final String 	TRACE_FLAGS 				= "ref_flags";
	public static final String	TRACE_WAVELENGTH			= "ref_wvlen";
	public static final String	TRACE_LENGTH				= "ref_trclen";
	public static final String	TRACE_RESOLUTION			= "ref_res";
	public static final String	TRACE_PULSE_WIDTH			= "ref_pulswd";
	public static final String	TRACE_INDEX_OF_REFRACTION	= "ref_ior";
	public static final String	TRACE_AVERAGE_COUNT			= "ref_scans";
	public static final String	TRACE_EVENTS				= "traceeventarray";

	public static final String	DADARA_EVENTS				= "dadara_event_array";
	public static final String	DADARA_ETALON_EVENTS		= "dadara_etalon_event_array";
	public static final String	DADARA_THRESHOLDS			= "dadara_thresholds";
	public static final String	DADARA_MIN_TRACE_LEVEL		= "dadara_min_trace_level";
	public static final String	DADARA						= "dadara";
	public static final String	DADARA_ALARMS				= "dadara_alarm_array";

	//prediction
	public static final String	PREDICTION_TIME				= "predictionTime";
	public static final String	PREDICTION_DATA_FROM		= "time_start";
	public static final String	PREDICTION_DATA_TO			= "time_end";

	// analysis params
	public static final String	WAVELET_TYPE				= "ref_uselinear";
	public static final String	STRATEGY					= "ref_strategy";
	public static final String	CONNECTOR_FORM_FACTOR		= "ref_conn_fall_params";
	public static final String	MIN_EVENT_LEVEL				= "ref_min_level";
	public static final String	MAX_NOISE_LEVEL				= "ref_max_level_noise";
	public static final String	MIN_END_LEVEL				= "ref_min_level_to_find_end";
	public static final String	MIN_SPLICE					= "ref_min_weld";
	public static final String	MIN_CONNECTOR				= "ref_min_connector";

	public static final String ALARM_STATUS = "alarm_status";

	public static final String HZ_CHO = "hz_cho";

	private ParameterTypeCodenames() {
		//singleton
	}
}
