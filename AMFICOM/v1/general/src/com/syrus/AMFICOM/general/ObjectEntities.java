/*
 * $Id: ObjectEntities.java,v 1.13 2004/08/09 14:13:53 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.13 $, $Date: 2004/08/09 14:13:53 $
 * @author $Author: arseniy $
 * @module general_v1
 */

public abstract class ObjectEntities {
	/*	Object Types	*/
	public static final String CHARACTERISTICTYPE_ENTITY = "CharacteriscticType";
	public static final String EQUIPMENTTYPE_ENTITY = "EquipmentType";

	public static final String PARAMETERTYPE_ENTITY = "ParameterType";
	public static final String MEASUREMENTTYPE_ENTITY = "MeasurementType";
	public static final String MNTTYPPARTYPLINK_ENTITY = "MntTypParTypLink";
	public static final String ANALYSISTYPE_ENTITY = "AnalysisType";
	public static final String ANATYPPARTYPLINK_ENTITY = "AnaTypParTypLink";
	public static final String EVALUATIONTYPE_ENTITY = "EvaluationType";
	public static final String EVATYPPARTYPLINK_ENTITY = "EvaTypParTypLink";

	/*	Administration	*/

	/*	Configuration	*/
	public static final String CHARACTERISTIC_ENTITY = "Characterisctic";
	public static final String PERMATTR_ENTITY = "PermissionAttributes";
	public static final String USER_ENTITY = "User";
	public static final String DOMAIN_ENTITY = "Domain";
	public static final String SERVER_ENTITY = "Server";
	public static final String MCM_ENTITY = "MCM";
	public static final String EQUIPMENT_ENTITY = "Equipment";
	public static final String EQUIPMENTMELINK_ENTITY = "EquipmentMELink";
	public static final String TRANSPATH_ENTITY = "TransmissionPath";
	public static final String TRANSPATHMELINK_ENTITY = "TransmissionPathMELink";	
	public static final String KIS_ENTITY = "KIS";
	public static final String ME_ENTITY = "MonitoredElement";

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


	public static final short UNKNOWN_ENTITY_CODE = 0x0000;

	/*	Object Types 1 -- 128	*/
	public static final short CHARACTERISTICTYPE_ENTITY_CODE = 0x0001;
	public static final short EQUIPMENTTYPE_ENTITY_CODE = 0x0002;

	public static final short PARAMETERTYPE_ENTITY_CODE = 0x0003;
	public static final short MEASUREMENTTYPE_ENTITY_CODE = 0x0004;
	public static final short ANALYSISTYPE_ENTITY_CODE = 0x0005;
	public static final short EVALUATIONTYPE_ENTITY_CODE = 0x0006;

	/*	Administration 129 -- 256	*/

	/*	Configuration	257 -- 384*/
	public static final short CHARACTERISTIC_ENTITY_CODE = 0x0101;
	public static final short PERMATTR_ENTITY_CODE = 0x0102;
	public static final short USER_ENTITY_CODE = 0x0103;
	public static final short DOMAIN_ENTITY_CODE = 0x0104;
	public static final short SERVER_ENTITY_CODE = 0x0105;
	public static final short MCM_ENTITY_CODE = 0x0106;
	public static final short EQUIPMENT_ENTITY_CODE = 0x0107;
	public static final short TRANSPATH_ENTITY_CODE = 0x0108;
	public static final short KIS_ENTITY_CODE = 0x0109;
	public static final short ME_ENTITY_CODE = 0x010A;

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

	public static short getCodeForString(String entity) {
		if (entity.equals(CHARACTERISTICTYPE_ENTITY)) return CHARACTERISTICTYPE_ENTITY_CODE;
		else if (entity.equals(EQUIPMENTTYPE_ENTITY)) return EQUIPMENTTYPE_ENTITY_CODE;

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
		else if (entity.equals(TRANSPATH_ENTITY)) return TRANSPATH_ENTITY_CODE;
		else if (entity.equals(KIS_ENTITY)) return KIS_ENTITY_CODE;
		else if (entity.equals(ME_ENTITY)) return ME_ENTITY_CODE;

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

		else return UNKNOWN_ENTITY_CODE;
	}
}
