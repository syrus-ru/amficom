/*
 * $Id: ObjectGroupEntities.java,v 1.16 2005/05/18 11:07:38 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import gnu.trove.TObjectShortHashMap;
import gnu.trove.TShortObjectHashMap;

/**
 * @version $Revision: 1.16 $, $Date: 2005/05/18 11:07:38 $
 * @author $Author: bass $
 * @module general_v1
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

	private static final TObjectShortHashMap NAME_CODE_MAP = new TObjectShortHashMap();

	private static final TShortObjectHashMap CODE_NAME_MAP = new TShortObjectHashMap();

	private ObjectGroupEntities() {
		// singleton constructor
		assert false;
	}

	static {
		registerGroups();
	}

	private static void registerGroups() {
		registerGroup(GENERAL_GROUP_CODE, GENERAL_GROUP);
		registerGroup(EVENT_GROUP_CODE, EVENT_GROUP);
		registerGroup(ADMINISTRATION_GROUP_CODE, ADMINISTRATION_GROUP);
		registerGroup(CONFIGURATION_GROUP_CODE, CONFIGURATION_GROUP);
		registerGroup(MEASUREMENT_GROUP_CODE, MEASUREMENT_GROUP);
		registerGroup(SCHEME_GROUP_CODE, SCHEME_GROUP);
		registerGroup(MAP_GROUP_CODE, MAP_GROUP);
		registerGroup(RESOURCE_GROUP_CODE, RESOURCE_GROUP);
		registerGroup(MAPVIEW_GROUP_CODE, MAPVIEW_GROUP);
	}

	private static void registerGroup(final short groupCode, final String group) {
		CODE_NAME_MAP.put(groupCode, group);
		NAME_CODE_MAP.put(group, groupCode);
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
	 * Here and below, an assertion is made in order to ensure that entity
	 * code is not only within the valid range, but also refers to a valid
	 * entity name.
	 */
	public static boolean isInGeneralGroup(final short entityCode) {
		assert ObjectEntities.codeToString(entityCode) != null;
		return entityCode >= ObjectEntities.GENERAL_MIN_ENTITY_CODE
			&& entityCode <= ObjectEntities.GENERAL_MAX_ENTITY_CODE;
	}

	public static boolean isInGeneralGroup(final String entityName) {
		return isInGeneralGroup(ObjectEntities.stringToCode(entityName));
	}

	public static boolean isInEventGroup(final short entityCode) {
		assert ObjectEntities.codeToString(entityCode) != null;
		return entityCode >= ObjectEntities.EVENT_MIN_ENTITY_CODE
			&& entityCode <= ObjectEntities.EVENT_MAX_ENTITY_CODE;
	}

	public static boolean isInEventGroup(final String entityName) {
		return isInEventGroup(ObjectEntities.stringToCode(entityName));
	}

	public static boolean isInAdministrationGroup(final short entityCode) {
		assert ObjectEntities.codeToString(entityCode) != null;
		return entityCode >= ObjectEntities.ADMINISTRATION_MIN_ENTITY_CODE
			&& entityCode <= ObjectEntities.ADMINISTRATION_MAX_ENTITY_CODE;
	}

	public static boolean isInAdministrationGroup(final String entityName) {
		return isInAdministrationGroup(ObjectEntities.stringToCode(entityName));
	}

	public static boolean isInConfigurationGroup(final short entityCode) {
		assert ObjectEntities.codeToString(entityCode) != null;
		return entityCode >= ObjectEntities.CONFIGURATION_MIN_ENTITY_CODE
			&& entityCode <= ObjectEntities.CONFIGURATION_MAX_ENTITY_CODE;
	}

	public static boolean isInConfigurationGroup(final String entityName) {
		return isInConfigurationGroup(ObjectEntities.stringToCode(entityName));
	}
	
	public static boolean isInMeasurementGroup(final short entityCode) {
		assert ObjectEntities.codeToString(entityCode) != null;
		return entityCode >= ObjectEntities.MEASUREMENT_MIN_ENTITY_CODE
			&& entityCode <= ObjectEntities.MEASUREMENT_MAX_ENTITY_CODE;
	}

	public static boolean isInMeasurementGroup(final String entityName) {
		return isInMeasurementGroup(ObjectEntities.stringToCode(entityName));
	}

	public static boolean isInSchemeGroup(final short entityCode) {
		assert ObjectEntities.codeToString(entityCode) != null;
		return entityCode >= ObjectEntities.SCHEME_MIN_ENTITY_CODE
			&& entityCode <= ObjectEntities.SCHEME_MAX_ENTITY_CODE;
	}

	public static boolean isInSchemeGroup(final String entityName) {
		return isInSchemeGroup(ObjectEntities.stringToCode(entityName));
	}

	public static boolean isInMapGroup(final short entityCode) {
		assert ObjectEntities.codeToString(entityCode) != null;
		return entityCode >= ObjectEntities.MAP_MIN_ENTITY_CODE
			&& entityCode <= ObjectEntities.MAP_MAX_ENTITY_CODE;
	}

	public static boolean isInMapGroup(final String entityName) {
		return isInMapGroup(ObjectEntities.stringToCode(entityName));
	}

	public static boolean isInResourceGroup(final short entityCode) {
		assert ObjectEntities.codeToString(entityCode) != null;
		return entityCode >= ObjectEntities.RESOURCE_MIN_ENTITY_CODE
			&& entityCode <= ObjectEntities.RESOURCE_MAX_ENTITY_CODE;
	}

	public static boolean isInResourceGroup(final String entityName) {
		return isInResourceGroup(ObjectEntities.stringToCode(entityName));
	}
	
	public static boolean isInMapViewGroup(final short entityCode) {
		assert ObjectEntities.codeToString(entityCode) != null;
		return entityCode >= ObjectEntities.MAPVIEW_MIN_ENTITY_CODE
			&& entityCode <= ObjectEntities.MAPVIEW_MAX_ENTITY_CODE;
	}

	public static boolean isInMapViewGroup(final String entityName) {
		return isInMapViewGroup(ObjectEntities.stringToCode(entityName));
	}

	public static short getGroupCode(final Short entityCode) {
		return getGroupCode(entityCode.shortValue());
	}

	public static short getGroupCode(final short entityCode) {
		if (isInGeneralGroup(entityCode))
			return GENERAL_GROUP_CODE;
		else if (isInEventGroup(entityCode))
			return EVENT_GROUP_CODE;
		else if (isInAdministrationGroup(entityCode))
			return ADMINISTRATION_GROUP_CODE;
		else if(isInConfigurationGroup(entityCode))
			return CONFIGURATION_GROUP_CODE;
		else if (isInMeasurementGroup(entityCode))
			return MEASUREMENT_GROUP_CODE;
		else if (isInSchemeGroup(entityCode))
			return SCHEME_GROUP_CODE;
		else if (isInMapGroup(entityCode))
			return MAP_GROUP_CODE;
		else if (isInResourceGroup(entityCode))
			return RESOURCE_GROUP_CODE;
		else if (isInMapViewGroup(entityCode))
			return MAPVIEW_GROUP_CODE;
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
