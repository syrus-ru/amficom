/*-
 * $Id: CharacteristicTypeCodenames.java,v 1.12 2005/10/30 14:26:52 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.12 $, $Date: 2005/10/30 14:26:52 $
 * @author $Author: bass $
 * @author Vladimir Dolzhenko
 * @module general
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
	public static final String TRACE_PULSE_WIDTH_LOW_RES_SUFFIX = "_ref_pwd_low_res";
	public static final String TRACE_PULSE_WIDTH_HIGH_RES_SUFFIX = "_ref_pwd_high_res";

	
	public static final String UNITS_PREFIX = "units_";
	public static final String UNITS_RESOLUTION = UNITS_PREFIX + "res";
	public static final String UNITS_WAVELENGTH = UNITS_PREFIX + "wvlen";
	public static final String UNITS_AVERAGE_COUNT = UNITS_PREFIX + "scans";
	public static final String UNITS_TRACE_LENGTH = UNITS_PREFIX + "trclen";
	public static final String UNITS_PULSE_WIDTH = UNITS_PREFIX + "pulswd";

	
	public static final String USER_PREFIX = "user_";
	public static final String USER_NATURE = USER_PREFIX + "nature";
	public static final String USER_FULLNAME = USER_PREFIX + "fullname";
	public static final String USER_POSITION = USER_PREFIX + "position";
	public static final String USER_DEPARTEMENT = USER_PREFIX + "departement";
	public static final String USER_COMPANY = USER_PREFIX + "company";
	public static final String USER_ROOM_NO = USER_PREFIX + "room_no";
	public static final String USER_CITY = USER_PREFIX + "city";
	public static final String USER_STREET = USER_PREFIX + "street";
	public static final String USER_BUILDING = USER_PREFIX + "building";
	public static final String USER_EMAIL = USER_PREFIX + "email";
	public static final String USER_PHONE = USER_PREFIX + "phone";
	public static final String USER_CELLULAR = USER_PREFIX + "cellular";
	
	public static final String COMMON_COLOUR = "common_colour";
	
	public static final String LINK_ACTIVITY = "link_activity";
	
	private CharacteristicTypeCodenames() {
		//singleton
		assert false;
	}
}
