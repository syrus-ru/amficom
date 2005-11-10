/*-
 * $Id: ObjectGroupEntities.java,v 1.41 2005/11/10 13:58:48 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.ObjectEntities.ANALYSIS_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.ATTACHEDTEXT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.CABLECHANNELINGITEM_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.CABLELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.CABLELINK_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.CABLETHREAD_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.CABLETHREAD_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.COLLECTOR_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.CRONTEMPORALPATTERN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.DOMAIN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.EQUIPMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.EVENTSOURCE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.EVENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.EVENT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.IMAGERESOURCE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.INTERVALSTEMPORALPATTERN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.KIS_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.LAYOUT_ITEM_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.LINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.LINK_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MAPLIBRARY_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MAPVIEW_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MAP_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MARK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MCM_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTPORT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTSETUP_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MODELING_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MONITOREDELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.NODELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PARAMETERSET_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PATHELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PERIODICALTEMPORALPATTERN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PERMATTR_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PHYSICALLINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PHYSICALLINK_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PIPEBLOCK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PORT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PROTOEQUIPMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.REPORTDATA_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.REPORTIMAGE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.REPORTTABLEDATA_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.REPORTTEMPLATE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.RESULT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.ROLE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLEPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLETHREAD_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEDEVICE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEMONITORINGSOLUTION_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEOPTIMIZEINFORTU_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEOPTIMIZEINFOSWITCH_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEOPTIMIZEINFO_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPATH_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPROTOELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPROTOGROUP_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEME_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SERVERPROCESS_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SERVER_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SITENODE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SITENODE_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SYSTEMUSER_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TEST_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TOPOLOGICALNODE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TRANSMISSIONPATH_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TRANSPATH_TYPE_CODE;
import static java.util.logging.Level.SEVERE;
import gnu.trove.TObjectShortHashMap;
import gnu.trove.TShortObjectHashMap;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.41 $, $Date: 2005/11/10 13:58:48 $
 * @author $Author: bass $
 * @author Selivanov Maksim Fedorovich
 * @module general
 */
public final class ObjectGroupEntities {
	//  Group Types
	public static final String GENERAL_GROUP = "GeneralGroup";
	public static final String EVENT_GROUP = "EventGroup";
	public static final String ADMINISTRATION_GROUP = "AdministrationGroup";
	public static final String CONFIGURATION_GROUP = "ConfigurationGroup";
	public static final String MEASUREMENT_GROUP = "MeasurementGroup";
	public static final String SCHEME_GROUP = "SchemeGroup";
	public static final String MAP_GROUP = "MapGroup";
	public static final String RESOURCE_GROUP = "ResourceGroup";
	public static final String MAPVIEW_GROUP = "MapViewGroup";
	public static final String REPORT_GROUP = "ReportGroup";

	//  Group Codes
	public static final short GENERAL_GROUP_CODE = 0x0001;
	public static final short EVENT_GROUP_CODE = 0x0002;
	public static final short ADMINISTRATION_GROUP_CODE = 0x0003;
	public static final short CONFIGURATION_GROUP_CODE = 0x0004;
	public static final short MEASUREMENT_GROUP_CODE = 0x0005;
	public static final short SCHEME_GROUP_CODE = 0x0006;
	public static final short MAP_GROUP_CODE = 0x0007;
	public static final short RESOURCE_GROUP_CODE = 0x0008;
	public static final short MAPVIEW_GROUP_CODE = 0x0009;
	public static final short REPORT_GROUP_CODE = 0x000A;

	private static final TObjectShortHashMap NAME_CODE_MAP = new TObjectShortHashMap();
	private static final TShortObjectHashMap CODE_NAME_MAP = new TShortObjectHashMap();
	private static final TShortObjectHashMap GROUP_CODES_MAP = new TShortObjectHashMap();

	private ObjectGroupEntities() {
		// singleton constructor
		assert false;
	}

	static {
		registerGroups();
	}

	private static void registerGroups() {
		registerGroup(GENERAL_GROUP_CODE, GENERAL_GROUP, new short[] { CHARACTERISTIC_TYPE_CODE, CHARACTERISTIC_CODE });
		registerGroup(EVENT_GROUP_CODE, EVENT_GROUP, new short[] { EVENT_TYPE_CODE, EVENTSOURCE_CODE, EVENT_CODE, DELIVERYATTRIBUTES_CODE });
		registerGroup(ADMINISTRATION_GROUP_CODE, ADMINISTRATION_GROUP, new short[] { SYSTEMUSER_CODE,
				DOMAIN_CODE,
				SERVER_CODE,
				MCM_CODE,
				SERVERPROCESS_CODE,
				PERMATTR_CODE,
				ROLE_CODE});
		registerGroup(CONFIGURATION_GROUP_CODE, CONFIGURATION_GROUP, new short[] { PORT_TYPE_CODE,
				TRANSPATH_TYPE_CODE,
				LINK_TYPE_CODE,
				CABLELINK_TYPE_CODE,
				CABLETHREAD_TYPE_CODE,
				PROTOEQUIPMENT_CODE,
				EQUIPMENT_CODE,
				PORT_CODE,
				TRANSMISSIONPATH_CODE,
				LINK_CODE,
				CABLELINK_CODE,
				CABLETHREAD_CODE });
		registerGroup(MEASUREMENT_GROUP_CODE, MEASUREMENT_GROUP, new short[] { MEASUREMENT_CODE,
				ANALYSIS_CODE,
				MODELING_CODE,
				MEASUREMENTSETUP_CODE,
				RESULT_CODE,
				PARAMETERSET_CODE,
				TEST_CODE,
				CRONTEMPORALPATTERN_CODE,
				INTERVALSTEMPORALPATTERN_CODE,
				PERIODICALTEMPORALPATTERN_CODE,
				MEASUREMENTPORT_TYPE_CODE,
				MEASUREMENTPORT_CODE,
				KIS_CODE,
				MONITOREDELEMENT_CODE });
		registerGroup(SCHEME_GROUP_CODE, SCHEME_GROUP, new short[] { CABLECHANNELINGITEM_CODE,
				PATHELEMENT_CODE,
				SCHEME_CODE,
				SCHEMECABLELINK_CODE,
				SCHEMECABLEPORT_CODE,
				SCHEMECABLETHREAD_CODE,
				SCHEMEDEVICE_CODE,
				SCHEMEELEMENT_CODE,
				SCHEMELINK_CODE,
				SCHEMEMONITORINGSOLUTION_CODE,
				SCHEMEOPTIMIZEINFO_CODE,
				SCHEMEOPTIMIZEINFOSWITCH_CODE,
				SCHEMEOPTIMIZEINFORTU_CODE,
				SCHEMEPATH_CODE,
				SCHEMEPORT_CODE,
				SCHEMEPROTOELEMENT_CODE,
				SCHEMEPROTOGROUP_CODE });
		registerGroup(MAP_GROUP_CODE, MAP_GROUP, new short[] { SITENODE_TYPE_CODE,
				PHYSICALLINK_TYPE_CODE,
				SITENODE_CODE,
				TOPOLOGICALNODE_CODE,
				NODELINK_CODE,
				MARK_CODE,
				PHYSICALLINK_CODE,
				COLLECTOR_CODE,
				MAP_CODE,
				MAPLIBRARY_CODE,
				PIPEBLOCK_CODE });
		registerGroup(RESOURCE_GROUP_CODE, RESOURCE_GROUP, new short[] { IMAGERESOURCE_CODE, LAYOUT_ITEM_CODE });
		registerGroup(MAPVIEW_GROUP_CODE, MAPVIEW_GROUP, new short[] { MAPVIEW_CODE });
		registerGroup(REPORT_GROUP_CODE, REPORT_GROUP, new short[] { ATTACHEDTEXT_CODE,
				REPORTIMAGE_CODE,
				REPORTDATA_CODE,
				REPORTTABLEDATA_CODE,
				REPORTTEMPLATE_CODE });
	}

	private static void registerGroup(final short groupCode, final String group, final short[] entityCodes) {
		assert CODE_NAME_MAP.get(groupCode) == null;
		CODE_NAME_MAP.put(groupCode, group);
		assert NAME_CODE_MAP.get(group) == 0;
		NAME_CODE_MAP.put(group, groupCode);
		assert GROUP_CODES_MAP.get(groupCode) == null;
		GROUP_CODES_MAP.put(groupCode, entityCodes);
	}

	public static short stringToCode(final String group) {
		final short returnValue = NAME_CODE_MAP.get(group);
		if (returnValue == 0) {
			throw new IllegalArgumentException(group);
		}
		return returnValue;
	}

	public static String codeToString(final short groupCode) {
		final String returnValue = (String) CODE_NAME_MAP.get(groupCode);
		if (returnValue == null) {
			throw new IllegalArgumentException(String.valueOf(groupCode));
		}
		return returnValue;
	}

	/**
	 * @param groupCode
	 */
	public static short[] getEntityCodes(final short groupCode) {
		short[] enityCodes = (short[]) GROUP_CODES_MAP.get(groupCode);
		assert enityCodes != null : ErrorMessages.ILLEGAL_GROUP_CODE + ": " + groupCode;
		return enityCodes;
	}

	/**
	 * Here and below, an assertion is made in order to ensure that entity code is
	 * not only within the valid range, but also refers to a valid entity name.
	 */

	public static boolean isInGeneralGroup(final short entityCode) {
		return entityCode >= ObjectEntities.GENERAL_MIN_CODE && entityCode <= ObjectEntities.GENERAL_MAX_CODE;
	}

	public static boolean isInGeneralGroup(final String entityName) {
		return isInGeneralGroup(ObjectEntities.stringToCode(entityName));
	}

	public static boolean isInEventGroup(final short entityCode) {
		return entityCode >= ObjectEntities.EVENT_MIN_CODE && entityCode <= ObjectEntities.EVENT_MAX_CODE;
	}

	public static boolean isInEventGroup(final String entityName) {
		return isInEventGroup(ObjectEntities.stringToCode(entityName));
	}

	public static boolean isInAdministrationGroup(final short entityCode) {
		return entityCode >= ObjectEntities.ADMINISTRATION_MIN_CODE && entityCode <= ObjectEntities.ADMINISTRATION_MAX_CODE;
	}

	public static boolean isInAdministrationGroup(final String entityName) {
		return isInAdministrationGroup(ObjectEntities.stringToCode(entityName));
	}

	public static boolean isInConfigurationGroup(final short entityCode) {
		return entityCode >= ObjectEntities.CONFIGURATION_MIN_CODE && entityCode <= ObjectEntities.CONFIGURATION_MAX_CODE;
	}

	public static boolean isInConfigurationGroup(final String entityName) {
		return isInConfigurationGroup(ObjectEntities.stringToCode(entityName));
	}

	public static boolean isInMeasurementGroup(final short entityCode) {
		return entityCode >= ObjectEntities.MEASUREMENT_MIN_CODE && entityCode <= ObjectEntities.MEASUREMENT_MAX_CODE;
	}

	public static boolean isInMeasurementGroup(final String entityName) {
		return isInMeasurementGroup(ObjectEntities.stringToCode(entityName));
	}

	public static boolean isInSchemeGroup(final short entityCode) {
		return entityCode >= ObjectEntities.SCHEME_MIN_CODE && entityCode <= ObjectEntities.SCHEME_MAX_CODE;
	}

	public static boolean isInSchemeGroup(final String entityName) {
		return isInSchemeGroup(ObjectEntities.stringToCode(entityName));
	}

	public static boolean isInMapGroup(final short entityCode) {
		return entityCode >= ObjectEntities.MAP_MIN_CODE && entityCode <= ObjectEntities.MAP_MAX_CODE;
	}

	public static boolean isInMapGroup(final String entityName) {
		return isInMapGroup(ObjectEntities.stringToCode(entityName));
	}

	public static boolean isInResourceGroup(final short entityCode) {
		return entityCode >= ObjectEntities.RESOURCE_MIN_CODE && entityCode <= ObjectEntities.RESOURCE_MAX_CODE;
	}

	public static boolean isInResourceGroup(final String entityName) {
		return isInResourceGroup(ObjectEntities.stringToCode(entityName));
	}

	public static boolean isInMapViewGroup(final short entityCode) {
		return entityCode >= ObjectEntities.MAPVIEW_MIN_CODE && entityCode <= ObjectEntities.MAPVIEW_MAX_CODE;
	}

	public static boolean isInMapViewGroup(final String entityName) {
		return isInMapViewGroup(ObjectEntities.stringToCode(entityName));
	}
	
	public static boolean isInReportGroup(final short entityCode) {
		return entityCode >= ObjectEntities.REPORT_MIN_CODE && entityCode <= ObjectEntities.REPORT_MAX_CODE;
	}
	
	public static boolean isInReportGroup(final String entityName) {
		return isInReportGroup(ObjectEntities.stringToCode(entityName));
	}

	public static short getGroupCode(final Short entityCode) {
		return getGroupCode(entityCode.shortValue());
	}

	public static short getGroupCode(final short entityCode) {
		if (isInGeneralGroup(entityCode)) {
			return GENERAL_GROUP_CODE;
		} else if (isInEventGroup(entityCode)) {
			return EVENT_GROUP_CODE;
		} else if (isInAdministrationGroup(entityCode)) {
			return ADMINISTRATION_GROUP_CODE;
		} else if(isInConfigurationGroup(entityCode)) {
			return CONFIGURATION_GROUP_CODE;
		} else if (isInMeasurementGroup(entityCode)) {
			return MEASUREMENT_GROUP_CODE;
		} else if (isInSchemeGroup(entityCode)) {
			return SCHEME_GROUP_CODE;
		} else if (isInMapGroup(entityCode)) {
			return MAP_GROUP_CODE;
		} else if (isInResourceGroup(entityCode)) {
			return RESOURCE_GROUP_CODE;
		} else if (isInMapViewGroup(entityCode)) {
			return MAPVIEW_GROUP_CODE;
		} else if (isInReportGroup(entityCode)) {
			return REPORT_GROUP_CODE;
		}
		throw new IllegalArgumentException(String.valueOf(entityCode));
	}

	public static String getGroupName(final short entityCode) {
		return codeToString(getGroupCode(entityCode));
	}
	
	public static String getPackageName(final short entityCode) {
		return "com.syrus.AMFICOM." + getGroupName(entityCode).toLowerCase().replaceAll("group$", "");
	}

	/**
	 * @param groupCode
	 * @deprecated error checking is made at every conversion back and forth. 
	 */
	@Deprecated
	public static boolean isGroupCodeValid(final short groupCode) {
		try {
			codeToString(groupCode);
			return true;
		} catch (final IllegalArgumentException iae) {
			Log.debugMessage(iae, SEVERE);
			return false;
		}
	}
}
