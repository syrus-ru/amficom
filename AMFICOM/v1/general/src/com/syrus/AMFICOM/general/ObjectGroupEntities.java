/*
 * $Id: ObjectGroupEntities.java,v 1.4 2004/11/23 15:21:12 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.4 $, $Date: 2004/11/23 15:21:12 $
 * @author $Author: bob $
 * @module module_name
 */
public class ObjectGroupEntities {
	
    //  Group Types
    public static final String MEASUREMENT_GROUP = "MeasurementGroup";
    public static final String CONFIGURATION_GROUP = "ConfigurationGroup";
    
    //  Group Codes
    public static final short UNKNOWN_GROUP_CODE = 0x0000;
    public static final short MEASUREMENT_GROUP_CODE = 0x0001;
    public static final short CONFIGURATION_GROUP_CODE = 0x0002;

		private ObjectGroupEntities() {
		}

    public static short stringToCode(String groupString) {
    	// recast using Trove Collections
        if(groupString.equals(MEASUREMENT_GROUP))
            return MEASUREMENT_GROUP_CODE;            
        if(groupString.equals(CONFIGURATION_GROUP))
            return CONFIGURATION_GROUP_CODE;
        return UNKNOWN_GROUP_CODE;        
    }
    
    public static String codeToString(short groupCode) {
    	// recast using Trove Collections
    	switch (groupCode) {
        case MEASUREMENT_GROUP_CODE:
            return MEASUREMENT_GROUP;
        case CONFIGURATION_GROUP_CODE:
            return CONFIGURATION_GROUP;
        default:
            return null;        
        }
    }
    
    public static boolean isInMeasurementGroup(short entityCode) {
    	switch (entityCode) {
            case ObjectEntities.PARAMETERTYPE_ENTITY_CODE:
            case ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE:
            case ObjectEntities.ANALYSISTYPE_ENTITY_CODE:
            case ObjectEntities.EVALUATIONTYPE_ENTITY_CODE:
            case ObjectEntities.SET_ENTITY_CODE:
            case ObjectEntities.MODELING_ENTITY_CODE:
            case ObjectEntities.MS_ENTITY_CODE:
            case ObjectEntities.ANALYSIS_ENTITY_CODE:
            case ObjectEntities.EVALUATION_ENTITY_CODE:
            case ObjectEntities.MEASUREMENT_ENTITY_CODE:
            case ObjectEntities.TEST_ENTITY_CODE:
            case ObjectEntities.RESULT_ENTITY_CODE:
            case ObjectEntities.TEMPORALPATTERN_ENTITY_CODE:
            	return true;
            default: 
            	return false;
        }
    }
    
    public static boolean isInMeasurementGroup(String entityName) {
        short entityCode = ObjectEntities.stringToCode(entityName);
        return isInMeasurementGroup(entityCode);
    }
    
    public static boolean isInConfigurationGroup(short entityCode) {
        switch (entityCode) {
            case ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE:
            case ObjectEntities.CABLETHREADTYPE_ENTITY_CODE:
            case ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE:
            case ObjectEntities.LINKTYPE_ENTITY_CODE:
            case ObjectEntities.PORTTYPE_ENTITY_CODE:
            case ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE:
            case ObjectEntities.CHARACTERISTIC_ENTITY_CODE:
            //          case ObjectEntities.PERMATTR_ENTITY_CODE:
            //              storableObject =
            // cObjectLoader.loadPermissionAttributes(objectId);
            //              break;
            case ObjectEntities.USER_ENTITY_CODE:
            case ObjectEntities.DOMAIN_ENTITY_CODE:
            case ObjectEntities.SERVER_ENTITY_CODE:
            case ObjectEntities.MCM_ENTITY_CODE:
            case ObjectEntities.EQUIPMENT_ENTITY_CODE:
            case ObjectEntities.PORT_ENTITY_CODE:
            case ObjectEntities.TRANSPATH_ENTITY_CODE:
            case ObjectEntities.KIS_ENTITY_CODE:
            case ObjectEntities.LINK_ENTITY_CODE:
            case ObjectEntities.MEASUREMENTPORT_ENTITY_CODE:
            case ObjectEntities.ME_ENTITY_CODE:
                return true;
            default: 
            	return false;
        }
    }
    
    public static boolean isInConfigurationGroup(String entityName) {
        short entityCode = ObjectEntities.stringToCode(entityName);
        return isInConfigurationGroup(entityCode);
    }
    
    public static short getGroupCode(short entityCode) {
        if(isInMeasurementGroup(entityCode))
            return MEASUREMENT_GROUP_CODE;
        if(isInConfigurationGroup(entityCode))
            return CONFIGURATION_GROUP_CODE;
        return UNKNOWN_GROUP_CODE;        
    }
}
