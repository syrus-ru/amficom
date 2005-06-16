/*
 * $Id: ObjectEntities.java,v 1.70 2005/06/16 10:29:05 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import gnu.trove.TObjectShortHashMap;
import gnu.trove.TShortObjectHashMap;

/**
 * @version $Revision: 1.70 $, $Date: 2005/06/16 10:29:05 $
 * @author $Author: bass $
 * @module general_v1
 */
public final class ObjectEntities {
	/**
	 * Never gets registered.
	 */
	public static final String UNKNOWN_ENTITY = null;

	/*	Object Types	*/
	public static final String PARAMETERTYPE_ENTITY = "ParameterType";
	public static final String CHARACTERISTICTYPE_ENTITY = "CharacteristicType";

	public static final String EVENTTYPE_ENTITY = "EventType";
	public static final String EVENTTYPPARTYPLINK_ENTITY = "EventTypParTypLink";
	public static final String EVENTTYPEUSERALERT_ENTITY = "EventTypeUserAlert";
//	public static final String ALARMTYPE_ENTITY = "AlarmType";

	public static final String EQUIPMENTTYPE_ENTITY = "EquipmentType";
	public static final String PORTTYPE_ENTITY = "PortType";
	public static final String MEASUREMENTPORTTYPE_ENTITY = "MeasurementPortType";
	public static final String LINKTYPE_ENTITY = "LinkType";
	public static final String CABLELINKTYPE_ENTITY = "CableLinkType";
	public static final String CABLETHREADTYPE_ENTITY = "CableThreadType";

	public static final String MEASUREMENTTYPE_ENTITY = "MeasurementType";
	public static final String MNTTYPPARTYPLINK_ENTITY = "MntTypParTypLink";
	public static final String MNTTYPEMEASPORTTYPELINK_ENTITY = "MntTypMeasPortTypLink";
	public static final String ANALYSISTYPE_ENTITY = "AnalysisType";
	public static final String ANATYPPARTYPLINK_ENTITY = "AnaTypParTypLink";
	public static final String EVALUATIONTYPE_ENTITY = "EvaluationType";
	public static final String EVATYPPARTYPLINK_ENTITY = "EvaTypParTypLink";
	public static final String MNTTYPANATYPEVATYP_ENTITY = "MntTypAnaTypEvaTyp";
	public static final String MODELINGTYPE_ENTITY = "ModelingType";
	public static final String MODTYPPARTYPLINK_ENTITY = "ModTypParTypLink";
	//public static final String KISTYPE_ENTITY = "KISType";
	public static final String TRANSPATHTYPE_ENTITY = "TransmissionPathType";

	public static final String SITE_NODE_TYPE_ENTITY = "SiteNodeType";
	public static final String PHYSICAL_LINK_TYPE_ENTITY = "PhysicalLinkType";

	/*	General */
	public static final String CHARACTERISTIC_ENTITY = "Characteristic";

	/*	Event	*/
	public static final String EVENTPARAMETER_ENTITY = "EventParameter";
	public static final String EVENTSOURCE_ENTITY = "EventSource";
	public static final String EVENTSOURCE_LINK_ENTITY = "EventSourceLink";
	public static final String EVENT_ENTITY = "Event";
//	public static final String ALARM_ENTITY = "Alarm";

	/*	Administration	*/
	public static final String SYSTEM_USER_ENTITY = "SystemUser";
	public static final String DOMAIN_ENTITY = "Domain";
	public static final String SERVER_ENTITY = "Server";
	public static final String MCM_ENTITY = "MCM";
	public static final String SERVERPROCESS_ENTITY = "ServerProcess";
	public static final String PERMATTR_ENTITY = "PermissionAttributes";

	/*	Configuration	*/
	public static final String EQUIPMENT_ENTITY = "Equipment";
	public static final String EQUIPMENTMELINK_ENTITY = "EquipmentMELink";
	public static final String PORT_ENTITY = "Port";
	public static final String TRANSPATH_ENTITY = "TransmissionPath";
	public static final String TRANSPATHMELINK_ENTITY = "TransmissionPathMELink";
	public static final String KIS_ENTITY = "KIS";
	public static final String MEASUREMENTPORT_ENTITY = "MeasurementPort";
	public static final String MONITOREDELEMENT_ENTITY = "MonitoredElement";
	public static final String LINK_ENTITY = "Link";
	public static final String CABLETHREAD_ENTITY = "CableThread";

	/*	Measurement	*/
	public static final String PARAMETER_SET_ENTITY = "ParameterSet";
	public static final String PARAMETER_ENTITY = "Parameter";
	public static final String SETMELINK_ENTITY = "SetMELink";
	public static final String MEASUREMENTSETUP_ENTITY = "MeasurementSetup";
	public static final String MSMELINK_ENTITY = "MeasurementSetupMELink";
	public static final String MSMTLINK_ENTITY = "MeasurementSetupMTLink";
	public static final String MEASUREMENT_ENTITY = "Measurement";
	public static final String ANALYSIS_ENTITY = "Analysis";
	public static final String EVALUATION_ENTITY = "Evaluation";
	public static final String TEST_ENTITY = "Test";
	public static final String MSTESTLINK_ENTITY = "MeasurementSetupTestLink";
	public static final String RESULT_ENTITY = "Result";
	public static final String RESULTPARAMETER_ENTITY = "ResultParameter";
	public static final String CRONTEMPORALPATTERN_ENTITY = "CronTemporalPattern";
	public static final String INTERVALS_TEMPORALPATTERN_ENTITY = "ITempPattern";
	public static final String PERIODICAL_TEMPORALPATTERN_ENTITY = "PeriodicalTemporalPattern";
	public static final String MODELING_ENTITY = "Modeling";

	/*        Scheme        */
	public static final String CABLE_CHANNELING_ITEM_ENTITY = "CableChannelingItem";
	public static final String PATH_ELEMENT_ENTITY = "PathElement";
	public static final String SCHEME_ENTITY = "Scheme";
	public static final String SCHEME_CABLE_LINK_ENTITY = "SchemeCableLink";
	public static final String SCHEME_CABLE_PORT_ENTITY = "SchemeCablePort";
	public static final String SCHEME_CABLE_THREAD_ENTITY = "SchemeCableThread";
	public static final String SCHEME_DEVICE_ENTITY = "SchemeDevice";
	public static final String SCHEME_ELEMENT_ENTITY = "SchemeElement";
	public static final String SCHEME_LINK_ENTITY = "SchemeLink";
	public static final String SCHEME_MONITORING_SOLUTION_ENTITY = "SchemeMonitoringSolution";
	public static final String SCHEME_OPTIMIZE_INFO_ENTITY = "SchemeOptimizeInfo";
	public static final String SCHEME_OPTIMIZE_INFO_SWITCH_ENTITY = "SchemeOptimizeInfoSwitch";
	public static final String SCHEME_OPTIMIZE_INFO_RTU_ENTITY = "SchemeOptimizeInfoRtu";
	public static final String SCHEME_PATH_ENTITY = "SchemePath";
	public static final String SCHEME_PORT_ENTITY = "SchemePort";
	public static final String SCHEME_PROTO_ELEMENT_ENTITY = "SchemeProtoElement";
	public static final String SCHEME_PROTO_GROUP_ENTITY = "SchemeProtoGroup";

	/*			Map			*/
	public static final String SITE_NODE_ENTITY = "SiteNode";
	public static final String TOPOLOGICAL_NODE_ENTITY = "TopologicalNode";
	public static final String NODE_LINK_ENTITY = "NodeLink";
	public static final String MARK_ENTITY = "Mark";
	public static final String PHYSICAL_LINK_ENTITY = "PhysicalLink";
	public static final String COLLECTOR_ENTITY = "Collector";
	public static final String MAP_ENTITY = "Map";

	/*       Resource       */
	public static final String IMAGE_RESOURCE_ENTITY = "ImageResource";
	
	/*		MapView			*/
	public static final String MAPVIEW_ENTITY = "MapView";

	/*        Updike        */
	/**
	 * Шняга: {@value}.
	 */
	public static final String UPDIKE_ENTITY = "8========================D";

	/**
	 * Never gets registered.
	 */
	public static final short UNKNOWN_ENTITY_CODE = 0x0000;

	/*
	 * Здесь могла бы быть ваша реклама: 1-64 (0x0001-0x0040)
	 * (Места для вашей рекламы всё меньше и меньше...)
	 */

	/*
	 * General:               65- 96 (0x0041-0x0060)
	 * General Types:         97-128 (0x0061-0x0080)
	 */
	public static final short GENERAL_MIN_ENTITY_CODE = 0x0041;

	public static final short CHARACTERISTIC_ENTITY_CODE = GENERAL_MIN_ENTITY_CODE;

	public static final short PARAMETERTYPE_ENTITY_CODE = 0x0061;
	public static final short CHARACTERISTICTYPE_ENTITY_CODE = 0x0062;

	public static final short GENERAL_MAX_ENTITY_CODE = 0x0080;

	/*
	 * Event:                129-192 (0x0081-0x00C0)
	 * Event Types:	         193-256 (0x00C1-0x0100)
	 */
	public static final short EVENT_MIN_ENTITY_CODE = 0x0081;

	public static final short EVENTPARAMETER_ENTITY_CODE = EVENT_MIN_ENTITY_CODE;
	public static final short EVENTSOURCE_ENTITY_CODE = 0x0082;
	public static final short EVENT_ENTITY_CODE = 0x0083;
//	public static final short ALARM_ENTITY_CODE = 0x0084;

	public static final short EVENTTYPE_ENTITY_CODE = 0x00C1;
//	public static final short ALARMTYPE_ENTITY_CODE = 0x00C2;

	public static final short EVENT_MAX_ENTITY_CODE = 0x0100;

	/*
	 * Administration:       257-320 (0x0101-0x0140)
	 * Administration Types: 321-384 (0x0141-0x0180)
	 */
	public static final short ADMINISTRATION_MIN_ENTITY_CODE = 0x0101;

	public static final short SYSTEM_USER_ENTITY_CODE = ADMINISTRATION_MIN_ENTITY_CODE;
	public static final short DOMAIN_ENTITY_CODE = 0x0102;
	public static final short SERVER_ENTITY_CODE = 0x0103;
	public static final short MCM_ENTITY_CODE = 0x0104;
	public static final short SERVERPROCESS_ENTITY_CODE = 0x0105;
	public static final short PERMATTR_ENTITY_CODE = 0x0106;

	public static final short ADMINISTRATION_MAX_ENTITY_CODE = 0x0180;

	/*
	 * Configuration:       385-448 (0x0181-0x01C0)
	 * Configuration Types: 449-512 (0x01C1-0x0200)
	 */
	public static final short CONFIGURATION_MIN_ENTITY_CODE = 0x0181;

	public static final short EQUIPMENT_ENTITY_CODE = CONFIGURATION_MIN_ENTITY_CODE;
	public static final short PORT_ENTITY_CODE = 0x0182;
	public static final short TRANSPATH_ENTITY_CODE = 0x0183;
	public static final short KIS_ENTITY_CODE = 0x0184;
	public static final short MEASUREMENTPORT_ENTITY_CODE = 0x0185;
	public static final short MONITOREDELEMENT_ENTITY_CODE = 0x0186;
	public static final short LINK_ENTITY_CODE = 0x0187;
	public static final short CABLETHREAD_ENTITY_CODE = 0x0188;

	public static final short EQUIPMENTTYPE_ENTITY_CODE = 0x01C1;
	public static final short PORTTYPE_ENTITY_CODE = 0x01C2;
	public static final short MEASUREMENTPORTTYPE_ENTITY_CODE = 0x01C3;
	public static final short LINKTYPE_ENTITY_CODE = 0x01C4;
  //	public static final short KISTYPE_ENTITY_CODE = 0x01C5;
	public static final short TRANSPATHTYPE_ENTITY_CODE = 0x01C6;
	public static final short CABLETHREADTYPE_ENTITY_CODE = 0x01C7;
	public static final short CABLELINKTYPE_ENTITY_CODE = 0x01C8;

	public static final short CONFIGURATION_MAX_ENTITY_CODE = 0x0200;

	/*
	 * Measurement:       513-576 (0x0201-0x0240)
	 * Measurement Types: 577-640 (0x0241-0x0280)
	 */
	public static final short MEASUREMENT_MIN_ENTITY_CODE = 0x0201;

	public static final short PARAMETER_SET_ENTITY_CODE = MEASUREMENT_MIN_ENTITY_CODE;
	public static final short PARAMETER_ENTITY_CODE = 0x0202;
	public static final short MEASUREMENTSETUP_ENTITY_CODE = 0x0203;
	public static final short MEASUREMENT_ENTITY_CODE = 0x0204;
	public static final short ANALYSIS_ENTITY_CODE = 0x0205;
	public static final short EVALUATION_ENTITY_CODE = 0x0206;
	public static final short TEST_ENTITY_CODE = 0x0207;
	public static final short RESULT_ENTITY_CODE = 0x0208;
	public static final short RESULTPARAMETER_ENTITY_CODE = 0x0209;
	public static final short MODELING_ENTITY_CODE = 0x020A;
	public static final short CRONTEMPORALPATTERN_ENTITY_CODE = 0x020B;
	public static final short INTERVALS_TEMPORALPATTERN_ENTITY_CODE = 0x020C;
	public static final short PERIODICAL_TEMPORALPATTERN_ENTITY_CODE = 0x020D;

	public static final short MEASUREMENTTYPE_ENTITY_CODE = 0x0241;
	public static final short ANALYSISTYPE_ENTITY_CODE = 0x0242;
	public static final short EVALUATIONTYPE_ENTITY_CODE = 0x0243;
	public static final short MODELINGTYPE_ENTITY_CODE = 0x0244;

	public static final short MEASUREMENT_MAX_ENTITY_CODE = 0x0280;

	/*
	 * Scheme:       641-704 (0x0281-0x02C0)
	 * Scheme Types: 705-768 (0x02C1-0x0300)
	 */
	public static final short SCHEME_MIN_ENTITY_CODE = 0x0281;

	public static final short CABLE_CHANNELING_ITEM_ENTITY_CODE = SCHEME_MIN_ENTITY_CODE;
	public static final short PATH_ELEMENT_ENTITY_CODE = 0x0282;
	public static final short SCHEME_ENTITY_CODE = 0x0283;
	public static final short SCHEME_CABLE_LINK_ENTITY_CODE = 0x0284;
	public static final short SCHEME_CABLE_PORT_ENTITY_CODE = 0x0285;
	public static final short SCHEME_CABLE_THREAD_ENTITY_CODE = 0x0286;
	public static final short SCHEME_DEVICE_ENTITY_CODE = 0x0287;
	public static final short SCHEME_ELEMENT_ENTITY_CODE = 0x0288;
	public static final short SCHEME_LINK_ENTITY_CODE = 0x0289;
	public static final short SCHEME_MONITORING_SOLUTION_ENTITY_CODE = 0x028A;
	public static final short SCHEME_OPTIMIZE_INFO_ENTITY_CODE = 0x028B;
	public static final short SCHEME_OPTIMIZE_INFO_SWITCH_ENTITY_CODE = 0x028C;
	public static final short SCHEME_OPTIMIZE_INFO_RTU_ENTITY_CODE = 0x028D;
	public static final short SCHEME_PATH_ENTITY_CODE = 0x028E;
	public static final short SCHEME_PORT_ENTITY_CODE = 0x028F;
	public static final short SCHEME_PROTO_ELEMENT_ENTITY_CODE = 0x0290;
	public static final short SCHEME_PROTO_GROUP_ENTITY_CODE = 0x0291;

	public static final short SCHEME_MAX_ENTITY_CODE = 0x0300;

	/*
	 * Map:       769-832 (0x0301-0x0340)
	 * Map Types: 833-896 (0x0341-0x0380)
	 */
	public static final short MAP_MIN_ENTITY_CODE = 0x0301;

	public static final short SITE_NODE_ENTITY_CODE = MAP_MIN_ENTITY_CODE;
	public static final short TOPOLOGICAL_NODE_ENTITY_CODE = 0x0302;
	public static final short NODE_LINK_ENTITY_CODE = 0x0303;
	public static final short MARK_ENTITY_CODE = 0x0304;
	public static final short PHYSICAL_LINK_ENTITY_CODE = 0x0305;
	public static final short COLLECTOR_ENTITY_CODE = 0x0306;
	public static final short MAP_ENTITY_CODE = 0x0307;

	public static final short SITE_NODE_TYPE_ENTITY_CODE = 0x0341;
	public static final short PHYSICAL_LINK_TYPE_ENTITY_CODE = 0x0342;

	public static final short MAP_MAX_ENTITY_CODE = 0x0380;
	
	/*
	 * Resource:       897-960 (0x0381-0x03C0)
	 * Resource Types: 961-1024(0x03C1-0x0400)
	 */
	public static final short RESOURCE_MIN_ENTITY_CODE = 0x0381;

	public static final short IMAGE_RESOURCE_ENTITY_CODE = RESOURCE_MIN_ENTITY_CODE;

	public static final short RESOURCE_MAX_ENTITY_CODE = 0x0400;
	
	/*
	 * MapView:        1025-1088(0x0401-0x0440)
	 * MapView Types:  1089-1152(0x0441-0x0480)
	 */
	public static final short MAPVIEW_MIN_ENTITY_CODE = 0x0401;
	
	public static final short MAPVIEW_ENTITY_CODE = MAPVIEW_MIN_ENTITY_CODE;
	
	public static final short MAPVIEW_MAX_ENTITY_CODE = 0x0480;


	/*
	 * Updike: 32767 (0x7FFF)
	 */
	public static final short UPDIKE_ENTITY_CODE = Short.MAX_VALUE;

	private static final TObjectShortHashMap NAME_CODE_MAP = new TObjectShortHashMap();

	private static final TShortObjectHashMap CODE_NAME_MAP = new TShortObjectHashMap();

	private ObjectEntities() {
		// empty singleton constructor
		assert false;
	}

	static {
		registerEntities();
	}

	private static void registerEntities() {
		registerEntity(PARAMETERTYPE_ENTITY_CODE, PARAMETERTYPE_ENTITY);
		registerEntity(CHARACTERISTICTYPE_ENTITY_CODE, CHARACTERISTICTYPE_ENTITY);

		registerEntity(EVENTTYPE_ENTITY_CODE, EVENTTYPE_ENTITY);
//		registerEntity(ALARMTYPE_ENTITY_CODE, ALARMTYPE_ENTITY);

		registerEntity(EQUIPMENTTYPE_ENTITY_CODE, EQUIPMENTTYPE_ENTITY);
		registerEntity(PORTTYPE_ENTITY_CODE, PORTTYPE_ENTITY);
		registerEntity(MEASUREMENTPORTTYPE_ENTITY_CODE, MEASUREMENTPORTTYPE_ENTITY);
		registerEntity(LINKTYPE_ENTITY_CODE, LINKTYPE_ENTITY);
		registerEntity(CABLETHREADTYPE_ENTITY_CODE, CABLETHREADTYPE_ENTITY);
		registerEntity(CABLELINKTYPE_ENTITY_CODE, CABLELINKTYPE_ENTITY);

		registerEntity(MEASUREMENTTYPE_ENTITY_CODE, MEASUREMENTTYPE_ENTITY);
		registerEntity(ANALYSISTYPE_ENTITY_CODE, ANALYSISTYPE_ENTITY);
		registerEntity(EVALUATIONTYPE_ENTITY_CODE, EVALUATIONTYPE_ENTITY);
		registerEntity(MODELINGTYPE_ENTITY_CODE, MODELINGTYPE_ENTITY);

		registerEntity(SITE_NODE_TYPE_ENTITY_CODE, SITE_NODE_TYPE_ENTITY);
		registerEntity(PHYSICAL_LINK_TYPE_ENTITY_CODE, PHYSICAL_LINK_TYPE_ENTITY);

		registerEntity(CHARACTERISTIC_ENTITY_CODE, CHARACTERISTIC_ENTITY);

		registerEntity(EVENTPARAMETER_ENTITY_CODE, EVENTPARAMETER_ENTITY);
		registerEntity(EVENTSOURCE_ENTITY_CODE, EVENTSOURCE_ENTITY);
		registerEntity(EVENT_ENTITY_CODE, EVENT_ENTITY);
//		registerEntity(ALARM_ENTITY_CODE, ALARM_ENTITY);

		registerEntity(SYSTEM_USER_ENTITY_CODE, SYSTEM_USER_ENTITY);
		registerEntity(DOMAIN_ENTITY_CODE, DOMAIN_ENTITY);
		registerEntity(SERVER_ENTITY_CODE, SERVER_ENTITY);
		registerEntity(MCM_ENTITY_CODE, MCM_ENTITY);
		registerEntity(SERVERPROCESS_ENTITY_CODE, SERVERPROCESS_ENTITY);
		registerEntity(PERMATTR_ENTITY_CODE, PERMATTR_ENTITY);

		registerEntity(EQUIPMENT_ENTITY_CODE, EQUIPMENT_ENTITY);
		registerEntity(PORT_ENTITY_CODE, PORT_ENTITY);
		registerEntity(TRANSPATH_ENTITY_CODE, TRANSPATH_ENTITY);
		registerEntity(TRANSPATHTYPE_ENTITY_CODE, TRANSPATHTYPE_ENTITY);
		registerEntity(KIS_ENTITY_CODE, KIS_ENTITY);
//		registerEntity(KISTYPE_ENTITY_CODE, KISTYPE_ENTITY);
		registerEntity(MEASUREMENTPORT_ENTITY_CODE, MEASUREMENTPORT_ENTITY);
		registerEntity(MONITOREDELEMENT_ENTITY_CODE, MONITOREDELEMENT_ENTITY);
		registerEntity(LINK_ENTITY_CODE, LINK_ENTITY);
		registerEntity(CABLETHREAD_ENTITY_CODE, CABLETHREAD_ENTITY);

		registerEntity(PARAMETER_SET_ENTITY_CODE, PARAMETER_SET_ENTITY);
		registerEntity(PARAMETER_ENTITY_CODE, PARAMETER_ENTITY);
		registerEntity(MEASUREMENTSETUP_ENTITY_CODE, MEASUREMENTSETUP_ENTITY);
		registerEntity(MEASUREMENT_ENTITY_CODE, MEASUREMENT_ENTITY);
		registerEntity(ANALYSIS_ENTITY_CODE, ANALYSIS_ENTITY);
		registerEntity(EVALUATION_ENTITY_CODE, EVALUATION_ENTITY);
		registerEntity(TEST_ENTITY_CODE, TEST_ENTITY);
		registerEntity(RESULT_ENTITY_CODE, RESULT_ENTITY);
		registerEntity(RESULTPARAMETER_ENTITY_CODE, RESULTPARAMETER_ENTITY);
		registerEntity(CRONTEMPORALPATTERN_ENTITY_CODE, CRONTEMPORALPATTERN_ENTITY);
		registerEntity(INTERVALS_TEMPORALPATTERN_ENTITY_CODE, INTERVALS_TEMPORALPATTERN_ENTITY);
		registerEntity(PERIODICAL_TEMPORALPATTERN_ENTITY_CODE, PERIODICAL_TEMPORALPATTERN_ENTITY);
		registerEntity(MODELING_ENTITY_CODE, MODELING_ENTITY);

		registerEntity(CABLE_CHANNELING_ITEM_ENTITY_CODE, CABLE_CHANNELING_ITEM_ENTITY);
		registerEntity(PATH_ELEMENT_ENTITY_CODE, PATH_ELEMENT_ENTITY);
		registerEntity(SCHEME_ENTITY_CODE, SCHEME_ENTITY);
		registerEntity(SCHEME_CABLE_LINK_ENTITY_CODE, SCHEME_CABLE_LINK_ENTITY);
		registerEntity(SCHEME_CABLE_PORT_ENTITY_CODE, SCHEME_CABLE_PORT_ENTITY);
		registerEntity(SCHEME_CABLE_THREAD_ENTITY_CODE, SCHEME_CABLE_THREAD_ENTITY);
		registerEntity(SCHEME_DEVICE_ENTITY_CODE, SCHEME_DEVICE_ENTITY);
		registerEntity(SCHEME_ELEMENT_ENTITY_CODE, SCHEME_ELEMENT_ENTITY);
		registerEntity(SCHEME_LINK_ENTITY_CODE, SCHEME_LINK_ENTITY);
		registerEntity(SCHEME_MONITORING_SOLUTION_ENTITY_CODE, SCHEME_MONITORING_SOLUTION_ENTITY);
		registerEntity(SCHEME_OPTIMIZE_INFO_ENTITY_CODE, SCHEME_OPTIMIZE_INFO_ENTITY);
		registerEntity(SCHEME_OPTIMIZE_INFO_SWITCH_ENTITY_CODE, SCHEME_OPTIMIZE_INFO_SWITCH_ENTITY);
		registerEntity(SCHEME_OPTIMIZE_INFO_RTU_ENTITY_CODE, SCHEME_OPTIMIZE_INFO_RTU_ENTITY);
		registerEntity(SCHEME_PATH_ENTITY_CODE, SCHEME_PATH_ENTITY);
		registerEntity(SCHEME_PORT_ENTITY_CODE, SCHEME_PORT_ENTITY);
		registerEntity(SCHEME_PROTO_ELEMENT_ENTITY_CODE, SCHEME_PROTO_ELEMENT_ENTITY);
		registerEntity(SCHEME_PROTO_GROUP_ENTITY_CODE, SCHEME_PROTO_GROUP_ENTITY);

		registerEntity(SITE_NODE_ENTITY_CODE, SITE_NODE_ENTITY);
		registerEntity(TOPOLOGICAL_NODE_ENTITY_CODE, TOPOLOGICAL_NODE_ENTITY);
		registerEntity(NODE_LINK_ENTITY_CODE, NODE_LINK_ENTITY);
		registerEntity(MARK_ENTITY_CODE, MARK_ENTITY);
		registerEntity(PHYSICAL_LINK_ENTITY_CODE, PHYSICAL_LINK_ENTITY);
		registerEntity(COLLECTOR_ENTITY_CODE, COLLECTOR_ENTITY);
		registerEntity(MAP_ENTITY_CODE, MAP_ENTITY);

		registerEntity(IMAGE_RESOURCE_ENTITY_CODE, IMAGE_RESOURCE_ENTITY);

		registerEntity(MAPVIEW_ENTITY_CODE, MAPVIEW_ENTITY);

		registerEntity(UPDIKE_ENTITY_CODE, UPDIKE_ENTITY);
	}

	private static void registerEntity(final short entityCode, final String entity) {
		assert CODE_NAME_MAP.get(entityCode) == null;
		CODE_NAME_MAP.put(entityCode, entity);
		assert NAME_CODE_MAP.get(entity) == 0;
		NAME_CODE_MAP.put(entity, entityCode);
	}

	public static short stringToCode(final String entity) {
		final short returnValue = NAME_CODE_MAP.get(entity);
		return returnValue == 0 ? UNKNOWN_ENTITY_CODE : returnValue;
	}

	public static String codeToString(final short entityCode) {
		final String returnValue = (String) CODE_NAME_MAP.get(entityCode);
		return returnValue == null ? UNKNOWN_ENTITY : returnValue;
	}

	public static String codeToString(final Short code) {
		assert code != null;
		return codeToString(code.shortValue());
	}

	/**
	 * @param entityCode
	 * @see ObjectGroupEntities#isGroupCodeValid(short)
	 */
	public static boolean isEntityCodeValid(final short entityCode) {
		return codeToString(entityCode) != null;
	}
}
