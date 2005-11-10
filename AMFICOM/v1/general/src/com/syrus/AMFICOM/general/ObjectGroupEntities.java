/*-
 * $Id: ObjectGroupEntities.java,v 1.40 2005/11/10 13:25:58 bob Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.ObjectEntities.*;

import gnu.trove.TObjectShortHashMap;
import gnu.trove.TShortObjectHashMap;

/**
 * @version $Revision: 1.40 $, $Date: 2005/11/10 13:25:58 $
 * @author $Author: bob $
 * @author Selivanov Maksim Fedorovich
 * @module general
 */
public final class ObjectGroupEntities {
	//  Group Types
	/**
	 * Never gets registered.
	 */
	public static final String UNKNOWN_GROUP = null;
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
	/**
	 * Never gets registered.
	 */
	public static final short UNKNOWN_GROUP_CODE = 0x0000;
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
		return returnValue == 0 ? UNKNOWN_GROUP_CODE : returnValue;
	}

	public static String codeToString(final short groupCode) {
		final String returnValue = (String) CODE_NAME_MAP.get(groupCode);
		return returnValue == null ? UNKNOWN_GROUP : returnValue;
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
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE;
		return entityCode >= ObjectEntities.GENERAL_MIN_CODE && entityCode <= ObjectEntities.GENERAL_MAX_CODE;
	}

	public static boolean isInGeneralGroup(final String entityName) {
		return isInGeneralGroup(ObjectEntities.stringToCode(entityName));
	}

	public static boolean isInEventGroup(final short entityCode) {
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE;
		return entityCode >= ObjectEntities.EVENT_MIN_CODE && entityCode <= ObjectEntities.EVENT_MAX_CODE;
	}

	public static boolean isInEventGroup(final String entityName) {
		return isInEventGroup(ObjectEntities.stringToCode(entityName));
	}

	public static boolean isInAdministrationGroup(final short entityCode) {
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE;
		return entityCode >= ObjectEntities.ADMINISTRATION_MIN_CODE && entityCode <= ObjectEntities.ADMINISTRATION_MAX_CODE;
	}

	public static boolean isInAdministrationGroup(final String entityName) {
		return isInAdministrationGroup(ObjectEntities.stringToCode(entityName));
	}

	public static boolean isInConfigurationGroup(final short entityCode) {
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE;
		return entityCode >= ObjectEntities.CONFIGURATION_MIN_CODE && entityCode <= ObjectEntities.CONFIGURATION_MAX_CODE;
	}

	public static boolean isInConfigurationGroup(final String entityName) {
		return isInConfigurationGroup(ObjectEntities.stringToCode(entityName));
	}

	public static boolean isInMeasurementGroup(final short entityCode) {
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE;
		return entityCode >= ObjectEntities.MEASUREMENT_MIN_CODE && entityCode <= ObjectEntities.MEASUREMENT_MAX_CODE;
	}

	public static boolean isInMeasurementGroup(final String entityName) {
		return isInMeasurementGroup(ObjectEntities.stringToCode(entityName));
	}

	public static boolean isInSchemeGroup(final short entityCode) {
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE;
		return entityCode >= ObjectEntities.SCHEME_MIN_CODE && entityCode <= ObjectEntities.SCHEME_MAX_CODE;
	}

	public static boolean isInSchemeGroup(final String entityName) {
		return isInSchemeGroup(ObjectEntities.stringToCode(entityName));
	}

	public static boolean isInMapGroup(final short entityCode) {
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE;
		return entityCode >= ObjectEntities.MAP_MIN_CODE && entityCode <= ObjectEntities.MAP_MAX_CODE;
	}

	public static boolean isInMapGroup(final String entityName) {
		return isInMapGroup(ObjectEntities.stringToCode(entityName));
	}

	public static boolean isInResourceGroup(final short entityCode) {
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE;
		return entityCode >= ObjectEntities.RESOURCE_MIN_CODE && entityCode <= ObjectEntities.RESOURCE_MAX_CODE;
	}

	public static boolean isInResourceGroup(final String entityName) {
		return isInResourceGroup(ObjectEntities.stringToCode(entityName));
	}

	public static boolean isInMapViewGroup(final short entityCode) {
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE;
		return entityCode >= ObjectEntities.MAPVIEW_MIN_CODE && entityCode <= ObjectEntities.MAPVIEW_MAX_CODE;
	}

	public static boolean isInMapViewGroup(final String entityName) {
		return isInMapViewGroup(ObjectEntities.stringToCode(entityName));
	}
	
	public static boolean isInReportGroup(final short entityCode) {
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE;
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
		}
		else if (isInEventGroup(entityCode)) {
			return EVENT_GROUP_CODE;
		}
		else if (isInAdministrationGroup(entityCode)) {
			return ADMINISTRATION_GROUP_CODE;
		}
		else if(isInConfigurationGroup(entityCode)) {
			return CONFIGURATION_GROUP_CODE;
		}
		else if (isInMeasurementGroup(entityCode)) {
			return MEASUREMENT_GROUP_CODE;
		}
		else if (isInSchemeGroup(entityCode)) {
			return SCHEME_GROUP_CODE;
		}
		else if (isInMapGroup(entityCode)) {
			return MAP_GROUP_CODE;
		}
		else if (isInResourceGroup(entityCode)) {
			return RESOURCE_GROUP_CODE;
		}
		else if (isInMapViewGroup(entityCode)) {
			return MAPVIEW_GROUP_CODE;
		}
		else if (isInReportGroup(entityCode)) {
			return REPORT_GROUP_CODE;
		}
		return UNKNOWN_GROUP_CODE;
	}

	public static String getGroupName(final short entityCode) {
		return codeToString(getGroupCode(entityCode));
	}
	
	public static String getPackageName(final short entityCode) {
		return "com.syrus.AMFICOM." + getGroupName(entityCode).toLowerCase().replaceAll("group$", "");
	}

	/**
	 * @param groupCode
	 * @see ObjectEntities#isEntityCodeValid(short)
	 */
	public static boolean isGroupCodeValid(final short groupCode) {
		return codeToString(groupCode) != null;
	}
}
