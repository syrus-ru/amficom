/*-
 * $Id: ObjectGroupEntities.java,v 1.44 2005/11/10 15:47:05 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.ObjectEntities.UPDIKE_CODE;
import static java.util.logging.Level.FINEST;
import static java.util.logging.Level.SEVERE;
import gnu.trove.TObjectShortHashMap;
import gnu.trove.TShortHashSet;
import gnu.trove.TShortObjectHashMap;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.44 $, $Date: 2005/11/10 15:47:05 $
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

	private static volatile boolean groupsRegistered = false;

	private ObjectGroupEntities() {
		// singleton constructor
		assert false;
	}

	static {
		Log.debugMessage(FINEST);
		ObjectEntities.registerEntities();
		registerGroups();
	}

	/**
	 * @see ObjectEntities#getCodes()
	 */
	static short[] getCodes() {
		if (!groupsRegistered) {
			throw new IllegalStateException();
		}

		return CODE_NAME_MAP.keys();
	}

	/**
	 * This method is <em>not</em> thread safe.
	 */
	private static void registerGroups() {
		Log.debugMessage(FINEST);
		if (groupsRegistered) {
			return;
		}

		registerGroup(GENERAL_GROUP_CODE, GENERAL_GROUP);
		registerGroup(EVENT_GROUP_CODE, EVENT_GROUP);
		registerGroup(ADMINISTRATION_GROUP_CODE, ADMINISTRATION_GROUP);
		registerGroup(CONFIGURATION_GROUP_CODE, CONFIGURATION_GROUP);
		registerGroup(MEASUREMENT_GROUP_CODE, MEASUREMENT_GROUP);
		registerGroup(SCHEME_GROUP_CODE, SCHEME_GROUP);
		registerGroup(MAP_GROUP_CODE, MAP_GROUP);
		registerGroup(RESOURCE_GROUP_CODE, RESOURCE_GROUP);
		registerGroup(MAPVIEW_GROUP_CODE, MAPVIEW_GROUP);
		registerGroup(REPORT_GROUP_CODE, REPORT_GROUP);

		for (final short entityCode : ObjectEntities.getCodes()) {
			if (entityCode == UPDIKE_CODE) {
				continue;
			}

			final short groupCode = getGroupCode(entityCode);
			TShortHashSet entityCodes = (TShortHashSet) GROUP_CODES_MAP.get(groupCode);
			if (entityCodes == null) {
				entityCodes = new TShortHashSet();
				GROUP_CODES_MAP.put(groupCode, entityCodes);
			}
			entityCodes.add(entityCode);
		}

		groupsRegistered = true;
	}

	private static void registerGroup(final short groupCode, final String group) {
		assert CODE_NAME_MAP.get(groupCode) == null;
		CODE_NAME_MAP.put(groupCode, group);
		assert NAME_CODE_MAP.get(group) == 0;
		NAME_CODE_MAP.put(group, groupCode);
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
	static TShortHashSet getEntityCodes(final short groupCode) {
		final TShortHashSet enityCodes = (TShortHashSet) GROUP_CODES_MAP.get(groupCode);
		if (enityCodes == null || enityCodes.isEmpty()) {
			throw new IllegalArgumentException(String.valueOf(groupCode));
		}
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
