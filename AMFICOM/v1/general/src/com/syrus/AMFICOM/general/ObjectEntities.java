/*
 * $Id: ObjectEntities.java,v 1.25 2004/11/19 08:49:31 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.25 $, $Date: 2004/11/19 08:49:31 $
 * @author $Author: bob $
 * @module general_v1
 */

public abstract class ObjectEntities {
	/*	Object Types	*/
	public static final String CHARACTERISTICTYPE_ENTITY = "CharacteristicType";
	public static final String EQUIPMENTTYPE_ENTITY = "EquipmentType";
	public static final String PORTTYPE_ENTITY = "PortType";
	public static final String MEASUREMENTPORTTYPE_ENTITY = "MeasurementPortType";
	public static final String LINKTYPE_ENTITY = "LinkType";
	public static final String CABLETHREADTYPE_ENTITY = "CableThreadType";

	public static final String PARAMETERTYPE_ENTITY = "ParameterType";
	public static final String MEASUREMENTTYPE_ENTITY = "MeasurementType";
	public static final String MNTTYPPARTYPLINK_ENTITY = "MntTypParTypLink";
	public static final String MNTTYMEASPORTTYPELINK_ENTITY = "MntTypMeasPortTypLink";	
	public static final String ANALYSISTYPE_ENTITY = "AnalysisType";
	public static final String ANATYPPARTYPLINK_ENTITY = "AnaTypParTypLink";
	public static final String EVALUATIONTYPE_ENTITY = "EvaluationType";
	public static final String EVATYPPARTYPLINK_ENTITY = "EvaTypParTypLink";
    public static final String KISTYPE_ENTITY = "KISType";
    public static final String TRANSPATHTYPE_ENTITY = "TransmissionPathType";

	/*	Administration	*/

	/*	Configuration	*/
	public static final String CHARACTERISTIC_ENTITY = "Characteristic";
	public static final String PERMATTR_ENTITY = "PermissionAttributes";
	public static final String USER_ENTITY = "Users";
	public static final String DOMAIN_ENTITY = "Domain";
	public static final String SERVER_ENTITY = "Server";
	public static final String MCM_ENTITY = "MCM";
	public static final String EQUIPMENT_ENTITY = "Equipment";
    public static final String EQUIPMENTMELINK_ENTITY = "EquipmentMELink";
	public static final String PORT_ENTITY = "Port";
	public static final String TRANSPATH_ENTITY = "TransmissionPath";
	public static final String TRANSPATHMELINK_ENTITY = "TransmissionPathMELink";	
	public static final String KIS_ENTITY = "KIS";
    public static final String MEASUREMENTPORT_ENTITY = "MeasurementPort";
	public static final String ME_ENTITY = "MonitoredElement";
	public static final String LINK_ENTITY = "Link";

	/*	Measurement	*/
	public static final String SET_ENTITY = "Sett";
	public static final String SETPARAMETER_ENTITY = "SetParameter";
	public static final String SETMELINK_ENTITY = "SetMELink";
	public static final String MS_ENTITY = "MeasurementSetup";
	public static final String MSMELINK_ENTITY = "MeasurementSetupMELink";
	public static final String MEASUREMENT_ENTITY = "Measurement";
	public static final String ANALYSIS_ENTITY = "Analysis";
	public static final String EVALUATION_ENTITY = "Evaluation";
	public static final String TEST_ENTITY = "Test";
	public static final String MSTESTLINK_ENTITY = "MeasurementSetupTestLink";
	public static final String RESULT_ENTITY = "Result";
	public static final String RESULTPARAMETER_ENTITY = "ResultParameter";
	public static final String TEMPORALPATTERN_ENTITY = "TemporalPattern";
	public static final String MODELING_ENTITY = "Modeling";


	public static final short UNKNOWN_ENTITY_CODE = 0x0000;

	/*	Object Types 1 -- 128	*/
	public static final short CHARACTERISTICTYPE_ENTITY_CODE = 0x0001;
	public static final short EQUIPMENTTYPE_ENTITY_CODE = 0x0002;
	public static final short PORTTYPE_ENTITY_CODE = 0x0003;
	public static final short MEASUREMENTPORTTYPE_ENTITY_CODE = 0x0004;

	public static final short PARAMETERTYPE_ENTITY_CODE = 0x0005;
	public static final short MEASUREMENTTYPE_ENTITY_CODE = 0x0006;
	public static final short ANALYSISTYPE_ENTITY_CODE = 0x0007;
	public static final short EVALUATIONTYPE_ENTITY_CODE = 0x0008;	
	public static final short LINKTYPE_ENTITY_CODE = 0x0009;
    public static final short KISTYPE_ENTITY_CODE = 0x000A;
    public static final short TRANSPATHTYPE_ENTITY_CODE = 0x000B;
    public static final short CABLETHREAD_ENTITY_CODE = 0x000C;

	/*	Administration 129 -- 256	*/

	/*	Configuration	257 -- 384*/
	public static final short CHARACTERISTIC_ENTITY_CODE = 0x0101;
	public static final short PERMATTR_ENTITY_CODE = 0x0102;
	public static final short USER_ENTITY_CODE = 0x0103;
	public static final short DOMAIN_ENTITY_CODE = 0x0104;
	public static final short SERVER_ENTITY_CODE = 0x0105;
	public static final short MCM_ENTITY_CODE = 0x0106;
	public static final short EQUIPMENT_ENTITY_CODE = 0x0107;
	public static final short PORT_ENTITY_CODE = 0x0108;
	public static final short TRANSPATH_ENTITY_CODE = 0x0109;
	public static final short KIS_ENTITY_CODE = 0x010A;
	public static final short MEASUREMENTPORT_ENTITY_CODE = 0x010B;
	public static final short ME_ENTITY_CODE = 0x010C;
	public static final short LINK_ENTITY_CODE = 0x010D;

	/*	Measurement 385 -- 512	*/
	public static final short SET_ENTITY_CODE = 0x0181;
	public static final short SETPARAMETER_ENTITY_CODE = 0x0182;
	public static final short MS_ENTITY_CODE = 0x0183;
	public static final short MEASUREMENT_ENTITY_CODE = 0x0184;
	public static final short ANALYSIS_ENTITY_CODE = 0x0185;
	public static final short EVALUATION_ENTITY_CODE = 0x0186;
	public static final short TEST_ENTITY_CODE = 0x0187;
	public static final short RESULT_ENTITY_CODE = 0x0188;
	public static final short RESULTPARAMETER_ENTITY_CODE = 0x0189;
	public static final short TEMPORALPATTERN_ENTITY_CODE = 0x018A;
	public static final short MODELING_ENTITY_CODE = 0x018B;

	public static short stringToCode(String entity) {
		/**
		 * TODO recast using Trove Collections
		 */
		if (entity.equals(CHARACTERISTICTYPE_ENTITY)) return CHARACTERISTICTYPE_ENTITY_CODE;
		else if (entity.equals(EQUIPMENTTYPE_ENTITY)) return EQUIPMENTTYPE_ENTITY_CODE;
		else if (entity.equals(PORTTYPE_ENTITY)) return PORTTYPE_ENTITY_CODE;
		else if (entity.equals(MEASUREMENTPORTTYPE_ENTITY)) return MEASUREMENTPORTTYPE_ENTITY_CODE;
		else if (entity.equals(LINKTYPE_ENTITY)) return LINKTYPE_ENTITY_CODE;
		else if (entity.equals(CABLETHREADTYPE_ENTITY)) return CABLETHREAD_ENTITY_CODE;

		else if (entity.equals(PARAMETERTYPE_ENTITY)) return PARAMETERTYPE_ENTITY_CODE;
		else if (entity.equals(MEASUREMENTTYPE_ENTITY)) return MEASUREMENTTYPE_ENTITY_CODE;
		else if (entity.equals(ANALYSISTYPE_ENTITY)) return ANALYSISTYPE_ENTITY_CODE;
		else if (entity.equals(EVALUATIONTYPE_ENTITY)) return EVALUATIONTYPE_ENTITY_CODE;

		else if (entity.equals(CHARACTERISTIC_ENTITY)) return CHARACTERISTIC_ENTITY_CODE;
		else if (entity.equals(PERMATTR_ENTITY)) return PERMATTR_ENTITY_CODE;
		else if (entity.equals(USER_ENTITY)) return USER_ENTITY_CODE;
		else if (entity.equals(DOMAIN_ENTITY)) return DOMAIN_ENTITY_CODE;
		else if (entity.equals(SERVER_ENTITY)) return SERVER_ENTITY_CODE;
		else if (entity.equals(MCM_ENTITY)) return MCM_ENTITY_CODE;
		else if (entity.equals(EQUIPMENT_ENTITY)) return EQUIPMENT_ENTITY_CODE;
		else if (entity.equals(PORT_ENTITY)) return PORT_ENTITY_CODE;
		else if (entity.equals(TRANSPATH_ENTITY)) return TRANSPATH_ENTITY_CODE;
        else if (entity.equals(TRANSPATHTYPE_ENTITY)) return TRANSPATHTYPE_ENTITY_CODE;
		else if (entity.equals(KIS_ENTITY)) return KIS_ENTITY_CODE;
		else if (entity.equals(KISTYPE_ENTITY)) return KISTYPE_ENTITY_CODE;
        else if (entity.equals(MEASUREMENTPORT_ENTITY)) return MEASUREMENTPORT_ENTITY_CODE;
		else if (entity.equals(ME_ENTITY)) return ME_ENTITY_CODE;
		else if (entity.equals(LINK_ENTITY)) return LINK_ENTITY_CODE;

		else if (entity.equals(SET_ENTITY)) return SET_ENTITY_CODE;
		else if (entity.equals(SETPARAMETER_ENTITY)) return SETPARAMETER_ENTITY_CODE;
		else if (entity.equals(MS_ENTITY)) return MS_ENTITY_CODE;
		else if (entity.equals(MEASUREMENT_ENTITY)) return MEASUREMENT_ENTITY_CODE;
		else if (entity.equals(ANALYSIS_ENTITY)) return ANALYSIS_ENTITY_CODE;
		else if (entity.equals(EVALUATION_ENTITY)) return EVALUATION_ENTITY_CODE;
		else if (entity.equals(TEST_ENTITY)) return TEST_ENTITY_CODE;
		else if (entity.equals(RESULT_ENTITY)) return RESULT_ENTITY_CODE;
		else if (entity.equals(RESULTPARAMETER_ENTITY)) return RESULTPARAMETER_ENTITY_CODE;
		else if (entity.equals(TEMPORALPATTERN_ENTITY)) return TEMPORALPATTERN_ENTITY_CODE;
		else if (entity.equals(MODELING_ENTITY)) return MODELING_ENTITY_CODE;

		else return UNKNOWN_ENTITY_CODE;
	}

	public static String codeToString(short code) {
		/**
		 * TODO recast using Trove Collections
		 */
		switch (code) {
			case CHARACTERISTICTYPE_ENTITY_CODE:
				return CHARACTERISTICTYPE_ENTITY;
			case EQUIPMENTTYPE_ENTITY_CODE:
				return EQUIPMENTTYPE_ENTITY;
			case PORTTYPE_ENTITY_CODE:
				return PORTTYPE_ENTITY;
			case MEASUREMENTPORTTYPE_ENTITY_CODE:
				return MEASUREMENTPORTTYPE_ENTITY;
			case LINKTYPE_ENTITY_CODE:
				return LINKTYPE_ENTITY;
			case CABLETHREAD_ENTITY_CODE:
				return CABLETHREADTYPE_ENTITY;

			case PARAMETERTYPE_ENTITY_CODE:
				return PARAMETERTYPE_ENTITY;
			case MEASUREMENTTYPE_ENTITY_CODE:
				return MEASUREMENTTYPE_ENTITY;
			case ANALYSISTYPE_ENTITY_CODE:
				return ANALYSISTYPE_ENTITY;
			case EVALUATIONTYPE_ENTITY_CODE:
				return EVALUATIONTYPE_ENTITY;

			case CHARACTERISTIC_ENTITY_CODE:
				return CHARACTERISTIC_ENTITY;
			case PERMATTR_ENTITY_CODE:
				return PERMATTR_ENTITY;
			case USER_ENTITY_CODE:
				return USER_ENTITY;
			case DOMAIN_ENTITY_CODE:
				return DOMAIN_ENTITY;
			case SERVER_ENTITY_CODE:
				return SERVER_ENTITY;
			case MCM_ENTITY_CODE:
				return MCM_ENTITY;
			case EQUIPMENT_ENTITY_CODE:
				return EQUIPMENT_ENTITY;
			case PORT_ENTITY_CODE:
				return PORT_ENTITY;
			case TRANSPATH_ENTITY_CODE:
				return TRANSPATH_ENTITY;
            case TRANSPATHTYPE_ENTITY_CODE:
                return TRANSPATHTYPE_ENTITY;
			case KIS_ENTITY_CODE:
				return KIS_ENTITY;
            case KISTYPE_ENTITY_CODE:
                return KISTYPE_ENTITY;
			case MEASUREMENTPORT_ENTITY_CODE:
				return MEASUREMENTPORT_ENTITY;
			case ME_ENTITY_CODE:
				return ME_ENTITY;
			case LINK_ENTITY_CODE:
				return LINK_ENTITY;

			case SET_ENTITY_CODE:
				return SET_ENTITY;
			case SETPARAMETER_ENTITY_CODE:
				return SETPARAMETER_ENTITY;
			case MS_ENTITY_CODE:
				return MS_ENTITY;
			case MEASUREMENT_ENTITY_CODE:
				return MEASUREMENT_ENTITY;
			case ANALYSIS_ENTITY_CODE:
				return ANALYSIS_ENTITY;
			case EVALUATION_ENTITY_CODE:
				return EVALUATION_ENTITY;
			case TEST_ENTITY_CODE:
				return TEST_ENTITY;
			case RESULT_ENTITY_CODE:
				return RESULT_ENTITY;
			case RESULTPARAMETER_ENTITY_CODE:
				return RESULTPARAMETER_ENTITY;
			case TEMPORALPATTERN_ENTITY_CODE:
				return TEMPORALPATTERN_ENTITY;
			case MODELING_ENTITY_CODE:
				return MODELING_ENTITY;

			case UNKNOWN_ENTITY_CODE:
				return null;

			default:
				return null;
		}
	}
}
