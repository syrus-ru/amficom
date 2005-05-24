/*-
 * $Id: CharacteristicTypeCodenames.java,v 1.6 2005/05/24 16:04:20 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.6 $, $Date: 2005/05/24 16:04:20 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module general_v1
 */
public final class CharacteristicTypeCodenames {
	public static final String TRACE_FLAGS = "ref_flags";

	public static final String TRACE_MAXPOINTS = "ref_maxpoints";
	public static final String TRACE_RESOLUTION = "ref_res";
	public static final String TRACE_WAVELENGTH = "ref_wvlen";

	public static final String TRACE_WAVELENGTH_PREFIX = TRACE_WAVELENGTH + '_';

	public static final String TRACE_AVERAGE_COUNT_SUFFIX = "_ref_scans";
	public static final String TRACE_INDEX_OF_REFRACTION_SUFFIX = "_ref_ior";
	public static final String TRACE_LENGTH_SUFFIX = "_ref_trclen";
	public static final String TRACE_PULSE_WIDTH_LOW_RES_SUFFIX = "_ref_pulswd_low_res";
	public static final String TRACE_PULSE_WIDTH_HIGH_RES_SUFFIX = "_ref_pulswd_high_res";

	
	public static final String UNITS_PREFIX = "units_";
	public static final String UNITS_RESOLUTION = UNITS_PREFIX + "res";
	public static final String UNITS_WAVELENGTH = UNITS_PREFIX + "wvlen";
	public static final String UNITS_AVERAGE_COUNT = UNITS_PREFIX + "scans";
	public static final String UNITS_TRACE_LENGTH = UNITS_PREFIX + "trclen";
	public static final String UNITS_PULSE_WIDTH = UNITS_PREFIX + "pulswd";

	private CharacteristicTypeCodenames() {
		//singleton
		assert false;
	}
}
