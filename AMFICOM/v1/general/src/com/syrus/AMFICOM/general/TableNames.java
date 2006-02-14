/*-
 * $Id: TableNames.java,v 1.10.2.3 2006/02/14 00:24:31 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.10.2.3 $, $Date: 2006/02/14 00:24:31 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public final class TableNames {
	public static final String DATA_TYPE = "DataType";
	public static final String MEASUREMENT_UNIT = "MeasurementUnit";

	public static final String SYSTEM_USER_ROLE_LINK = "SystemUserRoleLink";

	public static final String ACT_PAR_TMPL_LINK = "ActParTmplLink";
	public static final String ME_TMPL_LINK = "METmplLink";

	public static final String TEST_MEASTMPL_LINK = "TestMeasTmplLink";
	public static final String TEST_ANATMPL_LINK = "TestAnaTmplLink";
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
