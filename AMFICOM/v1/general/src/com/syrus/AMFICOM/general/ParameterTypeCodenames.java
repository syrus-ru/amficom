/*
 * $Id: ParameterTypeCodenames.java,v 1.10 2005/04/12 13:07:25 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.10 $, $Date: 2005/04/12 13:07:25 $
 * @author $Author: bob $
 * @module general_v1
 */
public final class ParameterTypeCodenames {

	public static final String	REFLECTOGRAMMA				= "reflectogramma";

	public static final String 	TRACE_FLAGS 				= "ref_flags";
	public static final String	TRACE_WAVELENGTH			= "ref_wvlen";
	public static final String	TRACE_LENGTH				= "ref_trclen";
	public static final String	TRACE_RESOLUTION			= "ref_res";
	public static final String	TRACE_PULSE_WIDTH			= "ref_pulswd";
	public static final String	TRACE_INDEX_OF_REFRACTION	= "ref_ior";
	public static final String	TRACE_AVERAGE_COUNT			= "ref_scans";
	public static final String	TRACE_EVENTS				= "traceeventarray";

	//prediction
	public static final String	PREDICTION_TIME				= "predictionTime";
	public static final String	PREDICTION_DATA_FROM		= "time_start";
	public static final String	PREDICTION_DATA_TO			= "time_end";

	//	 dadara etalon and thresholds
	public static final String	DADARA_MTAE					= "dadara_mtae";			// raw, ModelTraceAndEvents for analysis results
	public static final String	DADARA_ETALON_MTM			= "dadara_etalon_mtm";		// raw, ModeltraceMananger for etalon+thresholds
	public static final String	DADARA_MIN_TRACE_LEVEL		= "dadara_min_trace_level"; // double

	// dadara analysis params
	public static final String	DADARA						= "dadara";
	public static final String	DADARA_ALARMS				= "dadara_alarm_array";
	public static final String	DADARA_NOISE_FACTOR			= "dadara_noise_factor";	// double
	public static final String	MIN_EVENT					= "dadara_min_event";		// double
	public static final String	MIN_SPLICE					= "dadara_min_splice";		// double
	public static final String	MIN_CONNECTOR				= "dadara_min_connector";	// double

	public static final String ALARM_STATUS = "alarm_status";

	public static final String HZ_CHO = "hz_cho";

	private ParameterTypeCodenames() {
		//singleton
		assert false;
	}
}
