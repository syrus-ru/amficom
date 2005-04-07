/*-
 * $Id: CharacteristicTypeCodenames.java,v 1.1 2005/04/07 11:13:25 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.1 $, $Date: 2005/04/07 11:13:25 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module general_v1
 */
public interface CharacteristicTypeCodenames {
	String	TRACE_FLAGS							= "ref_flags";

	String	TRACE_MAXPOINTS						= "ref_maxpoints";
	String	TRACE_RESOLUTION					= "ref_res";
	String	TRACE_WAVELENGTH					= "ref_wvlen";
	
	String	TRACE_WAVELENGTH_PREFIX				= TRACE_WAVELENGTH + '_';
	
	String	TRACE_AVERAGE_COUNT_SUFFIX			= "_ref_scans";	
	String	TRACE_INDEX_OF_REFRACTION_SUFFIX	= "_ref_ior";
	String	TRACE_LENGTH_SUFFIX					= "_ref_trclen";
	String	TRACE_PULSE_WIDTH_SUFFIX			= "_ref_pulswd";

}
