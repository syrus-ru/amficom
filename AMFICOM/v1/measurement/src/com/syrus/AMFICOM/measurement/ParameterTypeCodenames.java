/*
 * $Id: ParameterTypeCodenames.java,v 1.3 2004/10/07 13:54:10 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

/**
 * @version $Revision: 1.3 $, $Date: 2004/10/07 13:54:10 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public interface ParameterTypeCodenames {

	String	REFLECTOGRAMMA		= "reflectogramma";

	String	TRACE_WAVELENGTH		= "ref_wvlen";
	String	TRACE_LENGTH		= "ref_trclen";
	String	TRACE_RESOLUTION		= "ref_res";
	String	TRACE_PULSE_WIDTH		= "ref_pulswd";
	String	TRACE_INDEX_OF_REFRACTION	= "ref_ior";
	String	TRACE_AVERAGE_COUNT			= "ref_scans";
	
	String TRACE_EVENTS 						= "traceeventarray";
	String DADARA_EVENTS 					= "dadara_event_array";
	String DADARA_ETALON_EVENTS		= "dadara_etalon_event_array";
	String DADARA_THRESHOLDS 			= "dadara_thresholds";
	String DADARA_MIN_TRACE_LEVEL 	= "dadara_min_trace_level";
	String DADARA 									= "dadara";

	// analysis params
	String WAVELET_TYPE	 				= "ref_uselinear";
	String STRATEGY 							= "ref_strategy";
	String CONNECTOR_FORM_FACTOR	= "ref_conn_fall_params";
	String MIN_EVENT_LEVEL 			= "ref_min_level";
	String MAX_NOISE_LEVEL		 		= "ref_max_level_noise";
	String MIN_END_LEVEL			 		= "ref_min_level_to_find_end";
	String MIN_SPLICE 						= "ref_min_weld";
	String MIN_CONNECTOR 				= "ref_min_connector";

}
