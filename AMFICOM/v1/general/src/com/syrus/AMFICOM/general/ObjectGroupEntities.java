/*
 * $Id: ObjectGroupEntities.java,v 1.7 2004/12/21 16:32:06 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.7 $, $Date: 2004/12/21 16:32:06 $
 * @author $Author: bob $
 * @module general_v1
 */
public final class ObjectGroupEntities {
	//  Group Types
	public static final String ADMINISTRATION_GROUP = "AdministrationGroup";
	public static final String CONFIGURATION_GROUP = "ConfigurationGroup";
	public static final String MEASUREMENT_GROUP = "MeasurementGroup";
	public static final String SCHEME_GROUP = "SchemeGroup";
	public static final String MAP_GROUP = "MapGroup";
	public static final String RESOURCE_GROUP = "ResourceGroup";
	public static final String MAPVIEW_GROUP = "MapViewGroup";
    
	//  Group Codes
	public static final short UNKNOWN_GROUP_CODE = 0x0000;
	public static final short ADMINISTRATION_GROUP_CODE = 0x0001;
	public static final short CONFIGURATION_GROUP_CODE = 0x0002;
	public static final short MEASUREMENT_GROUP_CODE = 0x0003;
	public static final short SCHEME_GROUP_CODE = 0x0004;
	public static final short MAP_GROUP_CODE = 0x0005;
	public static final short RESOURCE_GROUP_CODE = 0x0006;
	public static final short MAPVIEW_GROUP_CODE = 0x0007;

	private ObjectGroupEntities() {
		// singleton constructor
	}

	public static short stringToCode(final String groupString) {
		// recast using Trove Collections
		if (groupString.equals(ADMINISTRATION_GROUP))
			return ADMINISTRATION_GROUP_CODE;
		else if (groupString.equals(CONFIGURATION_GROUP))
			return CONFIGURATION_GROUP_CODE;
		else if (groupString.equals(MEASUREMENT_GROUP))
			return MEASUREMENT_GROUP_CODE;            
		else if (groupString.equals(SCHEME_GROUP))
			return SCHEME_GROUP_CODE;
		else if (groupString.equals(MAP_GROUP))
			return MAP_GROUP_CODE;
		else if (groupString.equals(RESOURCE_GROUP))
			return RESOURCE_GROUP_CODE;
		else if (groupString.equals(MAPVIEW_GROUP))
			return MAPVIEW_GROUP_CODE;
		return UNKNOWN_GROUP_CODE;        
	}
    
	public static String codeToString(final short groupCode) {
		// recast using Trove Collections
		switch (groupCode) {
			case ADMINISTRATION_GROUP_CODE:
				return ADMINISTRATION_GROUP;
			case CONFIGURATION_GROUP_CODE:
				return CONFIGURATION_GROUP;
			case MEASUREMENT_GROUP_CODE:
				return MEASUREMENT_GROUP;
			case SCHEME_GROUP_CODE:
				return SCHEME_GROUP;
			case MAP_GROUP_CODE:
				return MAP_GROUP;
			case RESOURCE_GROUP_CODE:
				return RESOURCE_GROUP;
			case MAPVIEW_GROUP_CODE:
				return MAPVIEW_GROUP;
			case UNKNOWN_GROUP_CODE:
			default:
				return null;        
		}
	}

	/**
	 * Here and below, an assertion is made in order to ensure that entity
	 * code is not only within the valid range, but also refers to a valid
	 * entity name.  
	 */
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

	public static short getGroupCode(final short entityCode) {
		if (isInAdministrationGroup(entityCode))
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
}
