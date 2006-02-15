/*-
 * $Id: TableNames.java,v 1.10.2.4 2006/02/15 19:32:17 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.10.2.4 $, $Date: 2006/02/15 19:32:17 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public final class TableNames {
	public static final String DATA_TYPE = "DataType";
	public static final String MEASUREMENT_UNIT = "MeasurementUnit";

	public static final String SYSTEM_USER_ROLE_LINK = "SystemUserRoleLink";

	public static final String ACTMPL_PAR_LINK = "AcTmplParLink";
	public static final String ACTMPL_ME_LINK = "AcTmplMELink";

	public static final String MS_ME_LINK = "MSMELink";

	public static final String TEST_MS_LINK = "TestMSLink";
	public static final String TEST_STOP_LINK = "TestStopLink";

	//---------------
	public static final String MNT_TYP_ANA_TYP_LINK = "MntTypAnaTypLink";
	public static final String MNTPORTTYPMNTTYPLINK = "MntPortTypMntTypLink";
	public static final String EQUIPMENT_ME_LINK = "EquipmentMELink";
	public static final String TRANSMISSIONPATH_ME_LINK = "TransmissionPathMELink";

	public static final String EVENTTYPPARTYPLINK = "EventTypParTypLink";
	public static final String EVENTTYPEUSERALERT = "EventTypeUserAlert";
	public static final String EVENTSOURCELINK = "EventSourceLink";

	public static final String DELIVERY_ATTRIBUTES_ROLE_LINK = "DeliveryAttributesRoleLink";
	public static final String DELIVERY_ATTRIBUTES_SYSTEM_USER_LINK = "DeliveryAttributesUserLink";

	private TableNames() {
		assert false;
	}
}
