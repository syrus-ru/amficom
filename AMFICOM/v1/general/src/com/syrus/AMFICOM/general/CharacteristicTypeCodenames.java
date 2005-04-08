/*-
 * $Id: CharacteristicTypeCodenames.java,v 1.4 2005/04/08 14:35:37 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.4 $, $Date: 2005/04/08 14:35:37 $
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
	public static final String TRACE_PULSE_WIDTH_SUFFIX = "_ref_pulswd";
	
	public static final String UNITS_RESOLUTION = "units_res";
	public static final String UNITS_WAVELENGTH = "units_wvlen";
	public static final String UNITS_AVERAGE_COUNT = "units_scans";
	public static final String UNITS_TRACE_LENGTH = "units_trclen";
	public static final String UNITS_PULSE_WIDTH = "units_pulswd";

	private CharacteristicTypeCodenames() {
		//singleton
		assert false;
	}
}
