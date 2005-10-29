/*-
 * $Id: CharacteristicTypeCodenames.java,v 1.11 2005/10/29 20:38:17 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.11 $, $Date: 2005/10/29 20:38:17 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module general
 */
public final class CharacteristicTypeCodenames {

	/*
	 * QP1640A
	 */
	public static final String REF_QP1640A_WVLEN = "ref_qp1640a_wvlen";
	public static final String REF_QP1640A_TRCLEN_1625 = "ref_qp1640a_trclen_1625";
	public static final String REF_QP1640A_RES_1625 = "ref_qp1640a_res_1625";
	public static final String REF_QP1640A_PULSWD_HIGHRES_1625 = "ref_qp1640a_pulswd_highres_1625";
	public static final String REF_QP1640A_PULSWD_LOWRES_1625 = "ref_qp1640a_pulswd_lowres_1625";
	public static final String REF_QP1640A_IOR = "ref_qp1640a_ior";
	public static final String REF_QP1640A_SCANS_1625 = "ref_qp1640a_scans_1625";
	public static final String REF_QP1640A_MAX_POINTS = "ref_qp1640a_max_points";

	/*
	 * PK7600
	 */
	public static final String REF_PK7600_WVLEN = "ref_pk7600_wvlen";
	public static final String REF_PK7600_TRCLEN = "ref_pk7600_trclen";
	public static final String REF_PK7600_RES_1625_4 = "ref_pk7600_res_1625_4";
	public static final String REF_PK7600_RES_1625_8 = "ref_pk7600_res_1625_8";
	public static final String REF_PK7600_RES_1625_16 = "ref_pk7600_res_1625_16";
	public static final String REF_PK7600_RES_1625_32 = "ref_pk7600_res_1625_32";
	public static final String REF_PK7600_RES_1625_65 = "ref_pk7600_res_1625_65";
	public static final String REF_PK7600_RES_1625_131 = "ref_pk7600_res_1625_131";
	public static final String REF_PK7600_RES_1625_262 = "ref_pk7600_res_1625_262";
	public static final String REF_PK7600_RES_1625_320 = "ref_pk7600_res_1625_320";
	public static final String REF_PK7600_PULSWD_1625_4 = "ref_pk7600_pulswd_1625_4";
	public static final String REF_PK7600_PULSWD_1625_8 = "ref_pk7600_pulswd_1625_8";
	public static final String REF_PK7600_PULSWD_1625_16 = "ref_pk7600_pulswd_1625_16";
	public static final String REF_PK7600_PULSWD_1625_32 = "ref_pk7600_pulswd_1625_32";
	public static final String REF_PK7600_PULSWD_1625_65 = "ref_pk7600_pulswd_1625_65";
	public static final String REF_PK7600_PULSWD_1625_131 = "ref_pk7600_pulswd_1625_131";
	public static final String REF_PK7600_PULSWD_1625_262 = "ref_pk7600_pulswd_1625_262";
	public static final String REF_PK7600_PULSWD_1625_320 = "ref_pk7600_pulswd_1625_320";
	public static final String REF_PK7600_IOR = "ref_pk7600_ior";

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
