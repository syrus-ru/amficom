package com.syrus.AMFICOM.general;

public class ObjectEntities {
	/*	Object Types	*/
	public static final String CHARACTERISTICTYPE_ENTITY = "CharacteristicType";
	public static final String PARAMETERTYPE_ENTITY = "ParameterType";
	public static final String MEASUREMENTTYPE_ENTITY = "MeasurementType";
	public static final String MNTTYPPARTYPLINK_ENTITY = "MntTypParTypLink";
	public static final String ANALYSISTYPE_ENTITY = "AnalysisType";
	public static final String ANATYPPARTYPLINK_ENTITY = "AnaTypParTypLink";
	public static final String EVALUATIONTYPE_ENTITY = "EvaluationType";
	public static final String EVATYPPARTYPLINK_ENTITY = "EvaTypParTypLink";

	/*	Administration	*/
	public static final String PERMATTR_ENTITY = "PermissionAttributes";
	public static final String DOMAIN_ENTITY = "domain";
	public static final String MCM_ENTITY = "mcm";
	public static final String SERVER_ENTITY = "Server";
	public static final String USER_ENTITY = "User";

	/*	Configuration	*/
	public static final String CHARACTERISTIC_ENTITY = "Characteristic";
	public static final String PORT_ENTITY = "Port";
	public static final String ME_ENTITY = "MonitoredElement";
	public static final String KIS_ENTITY = "kis";

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
	public static final String PTTEMPORALTEMPLATE_ENTITY = "PTTemporalTemplate";


	public static final short UNKNOWN_ENTITY_CODE = 0x0000;

	/*	Object Types 1 -- 128	*/
	public static final short CHARACTERISTICTYPE_ENTITY_CODE = 0x0001;
	public static final short PARAMETERTYPE_ENTITY_CODE = 0x0002;
	public static final short MEASUREMENTTYPE_ENTITY_CODE = 0x0003;
	public static final short ANALYSISTYPE_ENTITY_CODE = 0x0004;
	public static final short EVALUATIONTYPE_ENTITY_CODE = 0x0005;

	/*	Administration 129 -- 256	*/
	public static final short PERMATTR_ENTITY_CODE = 0x0081;
	public static final short DOMAIN_ENTITY_CODE = 0x0082;
	public static final short MCM_ENTITY_CODE = 0x0083;
	public static final short SERVER_ENTITY_CODE = 0x0084;
	public static final short USER_ENTITY_CODE = 0x0085;

	/*	Configuration	257 -- 384*/
	public static final short CHARACTERISTIC_CODE = 0x0101;
	public static final short PORT_ENTITY_CODE = 0x0102;
	public static final short ME_ENTITY_CODE = 0x0108;
	public static final short KIS_ENTITY_CODE = 0x0109;

	/*	Measurement 385 -- 512	*/
	public static final short SET_ENTITY_CODE = 0x0181;
	public static final short SETPARAMETER_ENTITY_CODE = 0x0182;
	public static final short SETMELINK_ENTITY_CODE = 0x0183;
	public static final short MS_ENTITY_CODE = 0x0184;
	public static final short MSMELINK_ENTITY_CODE = 0x0185;
	public static final short MEASUREMENT_ENTITY_CODE = 0x0186;
	public static final short ANALYSIS_ENTITY_CODE = 0x0187;
	public static final short EVALUATION_ENTITY_CODE = 0x0188;
	public static final short TEST_ENTITY_CODE = 0x0189;
	public static final short MSTESTLINK_ENTITY_CODE = 0x018A;
	public static final short RESULT_ENTITY_CODE = 0x018B;
	public static final short RESULTPARAMETER_ENTITY_CODE = 0x018C;
	public static final short PTTEMPORALTEMPLATE_ENTITY_CODE = 0x018D;

}