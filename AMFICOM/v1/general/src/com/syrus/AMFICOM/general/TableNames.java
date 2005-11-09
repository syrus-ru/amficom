/*-
 * $Id: TableNames.java,v 1.9 2005/11/09 11:35:11 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.9 $, $Date: 2005/11/09 11:35:11 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public final class TableNames {
	public static final String DATA_TYPE = "DataType";
	public static final String MEASUREMENT_UNIT = "MeasurementUnit";
	public static final String PARAMETER_TYPE = "ParameterType";

	public static final String SYSTEM_USER_ROLE_LINK = "SystemUserRoleLink";
	
	public static final String EQUIPMENT_TYPE = "EquipmentType";

	public static final String MEASUREMENT_TYPE = "MeasurementType";
	public static final String ANALYSIS_TYPE = "AnalysisType";
	public static final String MODELING_TYPE = "ModelingType";

	public static final String MNT_TYP_PAR_TYP_LINK = "MntTypParTypLink";
	public static final String ANA_TYP_PAR_TYP_LINK = "AnaTypParTypLink";
	public static final String MOD_TYP_PAR_TYP_LINK = "ModTypParTypLink";

	public static final String MNT_TYP_ANA_TYP_LINK = "MntTypAnaTypLink";
	public static final String MNTPORTTYPMNTTYPLINK = "MntPortTypMntTypLink";
	public static final String PARAMETERSETMELINK = "ParameterSetMELink";
	public static final String MEASUREMENTSETUP_ME_LINK = "MeasurementSetupMELink";
	public static final String MEASUREMENTSETUP_MT_LINK = "MeasurementSetupMTLink";
	public static final String TEST_STOP_LINK = "TestStopLink";
	public static final String MEASUREMENTSETUP_TEST_LINK = "MeasurementSetupTestLink";
	public static final String EQUIPMENT_ME_LINK = "EquipmentMELink";
	public static final String TRANSMISSIONPATH_ME_LINK = "TransmissionPathMELink";

	public static final String EVENTTYPPARTYPLINK = "EventTypParTypLink";
	public static final String EVENTTYPEUSERALERT = "EventTypeUserAlert";
	public static final String EVENTSOURCELINK = "EventSourceLink";

	private TableNames() {
		assert false;
	}
}
