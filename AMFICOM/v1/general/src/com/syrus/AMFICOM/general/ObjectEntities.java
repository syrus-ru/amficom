/*
 * $Id: ObjectEntities.java,v 1.33 2004/12/06 17:51:31 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.33 $, $Date: 2004/12/06 17:51:31 $
 * @author $Author: bass $
 * @module general_v1
 */
public final class ObjectEntities {
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
	//public static final String KISTYPE_ENTITY = "KISType";
	public static final String TRANSPATHTYPE_ENTITY = "TransmissionPathType";

	public static final String SITE_NODE_TYPE_ENTITY = "SiteNodeType";
	public static final String PHYSICAL_LINK_TYPE_ENTITY = "PhysicalLinkType";

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

	/*        Updike        */
	/**
	 * Шняга: {@value}.
	 */
	public static final String UPDIKE_ENTITY = "8========================D";


	public static final short UNKNOWN_ENTITY_CODE = 0x0000;

	/*
	 * Здесь могла бы быть ваша реклама: 001-128 (0x0001-0x0080)
	 */

	/*
	 * Administration:       129-192 (0x0081-0x00C0)
	 * Administration Types: 193-256 (0x00C1-0x0100)
	 */
	public static final short ADMINISTRATION_MIN_ENTITY_CODE = 0x0081;

	public static final short ADMINISTRATION_MAX_ENTITY_CODE = 0x0100;

	/*
	 * Configuration:       257-320 (0x0101-0x0140)
	 * Configuration Types: 321-384 (0x0141-0x0180)
	 */
	public static final short CONFIGURATION_MIN_ENTITY_CODE = 0x0101;

	public static final short CHARACTERISTIC_ENTITY_CODE = CONFIGURATION_MIN_ENTITY_CODE;
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

	public static final short CHARACTERISTICTYPE_ENTITY_CODE = 0x0141;
	public static final short EQUIPMENTTYPE_ENTITY_CODE = 0x0142;
	public static final short PORTTYPE_ENTITY_CODE = 0x0143;
	public static final short MEASUREMENTPORTTYPE_ENTITY_CODE = 0x0144;

	public static final short CONFIGURATION_MAX_ENTITY_CODE = 0x0180;

	/*
	 * Measurement:       385-448 (0x0181-0x01C0)
	 * Measurement Types: 449-512 (0x01C1-0x0200)
	 */
	public static final short MEASUREMENT_MIN_ENTITY_CODE = 0x0181;

	public static final short SET_ENTITY_CODE = MEASUREMENT_MIN_ENTITY_CODE;
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

	public static final short PARAMETERTYPE_ENTITY_CODE = 0x01C1;
	public static final short MEASUREMENTTYPE_ENTITY_CODE = 0x01C2;
	public static final short ANALYSISTYPE_ENTITY_CODE = 0x01C3;
	public static final short EVALUATIONTYPE_ENTITY_CODE = 0x01C4;
	public static final short LINKTYPE_ENTITY_CODE = 0x01C5;
//	public static final short KISTYPE_ENTITY_CODE = 0x01C6;
	public static final short TRANSPATHTYPE_ENTITY_CODE = 0x01C7;
	public static final short CABLETHREADTYPE_ENTITY_CODE = 0x01C8;

	public static final short MEASUREMENT_MAX_ENTITY_CODE = 0x0200;

	/*
	 * Scheme:       513-576 (0x0201-0x0240)
	 * Scheme Types: 577-640 (0x0241-0x0280)
	 */
	public static final short SCHEME_MIN_ENTITY_CODE = 0x0201;

	public static final short CABLE_CHANNELING_ITEM_ENTITY_CODE = SCHEME_MIN_ENTITY_CODE;
	public static final short PATH_ELEMENT_ENTITY_CODE = 0x0202;
	public static final short SCHEME_ENTITY_CODE = 0x0203;
	public static final short SCHEME_CABLE_LINK_ENTITY_CODE = 0x0204;
	public static final short SCHEME_CABLE_PORT_ENTITY_CODE = 0x0205;
	public static final short SCHEME_CABLE_THREAD_ENTITY_CODE = 0x0206;
	public static final short SCHEME_DEVICE_ENTITY_CODE = 0x0207;
	public static final short SCHEME_ELEMENT_ENTITY_CODE = 0x0208;
	public static final short SCHEME_LINK_ENTITY_CODE = 0x0209;
	public static final short SCHEME_MONITORING_SOLUTION_ENTITY_CODE = 0x020A;
	public static final short SCHEME_OPTIMIZE_INFO_ENTITY_CODE = 0x020B;
	public static final short SCHEME_PATH_ENTITY_CODE = 0x020C;
	public static final short SCHEME_PORT_ENTITY_CODE = 0x020D;
	public static final short SCHEME_PROTO_ELEMENT_ENTITY_CODE = 0x020E;
	public static final short SCHEME_PROTO_GROUP_ENTITY_CODE = 0x020F;

	public static final short SCHEME_MAX_ENTITY_CODE = 0x0280;

	/*
	 * Map:       641-704 (0x0281-0x02C0)
	 * Map Types: 705-768 (0x02C1-0x0300)
	 */
	public static final short MAP_MIN_ENTITY_CODE = 0x0281;

	public static final short SITE_NODE_ENTITY_CODE = 0x0281;
	public static final short TOPOLOGICAL_NODE_ENTITY_CODE = 0x0282;
	public static final short NODE_LINK_ENTITY_CODE = 0x0283;
	public static final short MARK_ENTITY_CODE = 0x0284;
	public static final short PHYSICAL_LINK_ENTITY_CODE = 0x0285;
	public static final short COLLECTOR_ENTITY_CODE = 0x0286;
	public static final short MAP_ENTITY_CODE = 0x0287;

	public static final short SITE_NODE_TYPE_ENTITY_CODE = 0x02C1;
	public static final short PHYSICAL_LINK_TYPE_ENTITY_CODE = 0x02C2;

	public static final short MAP_MAX_ENTITY_CODE = 0x0300;

	/*
	 * Resource:       769-832 (0x0301-0x0340)
	 * Resource Types: 833-896 (0x0341-0x0380)
	 */
	public static final short RESOURCE_MIN_ENTITY_CODE = 0x0301;

	public static final short IMAGE_RESOURCE_ENTITY_CODE = RESOURCE_MIN_ENTITY_CODE;

	public static final short RESOURCE_MAX_ENTITY_CODE = 0x0380;

	/*
	 * Updike: 32767 (0x7FFF)
	 */
	public static final short UPDIKE_ENTITY_CODE = Short.MAX_VALUE;

	private ObjectEntities() {
		// empty singleton constructor
	}

	public static short stringToCode(final String entity) {
		/**
		 * TODO recast using Trove Collections
		 */
		if (entity.equals(CHARACTERISTICTYPE_ENTITY)) return CHARACTERISTICTYPE_ENTITY_CODE;
		else if (entity.equals(EQUIPMENTTYPE_ENTITY)) return EQUIPMENTTYPE_ENTITY_CODE;
		else if (entity.equals(PORTTYPE_ENTITY)) return PORTTYPE_ENTITY_CODE;
		else if (entity.equals(MEASUREMENTPORTTYPE_ENTITY)) return MEASUREMENTPORTTYPE_ENTITY_CODE;
		else if (entity.equals(LINKTYPE_ENTITY)) return LINKTYPE_ENTITY_CODE;
		else if (entity.equals(CABLETHREADTYPE_ENTITY)) return CABLETHREADTYPE_ENTITY_CODE;

		else if (entity.equals(PARAMETERTYPE_ENTITY)) return PARAMETERTYPE_ENTITY_CODE;
		else if (entity.equals(MEASUREMENTTYPE_ENTITY)) return MEASUREMENTTYPE_ENTITY_CODE;
		else if (entity.equals(ANALYSISTYPE_ENTITY)) return ANALYSISTYPE_ENTITY_CODE;
		else if (entity.equals(EVALUATIONTYPE_ENTITY)) return EVALUATIONTYPE_ENTITY_CODE;

		else if (entity.equals(SITE_NODE_TYPE_ENTITY)) return SITE_NODE_TYPE_ENTITY_CODE;
		else if (entity.equals(PHYSICAL_LINK_TYPE_ENTITY)) return PHYSICAL_LINK_TYPE_ENTITY_CODE;

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
		//else if (entity.equals(KISTYPE_ENTITY)) return KISTYPE_ENTITY_CODE;
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

		else if (entity.equals(CABLE_CHANNELING_ITEM_ENTITY))
			return CABLE_CHANNELING_ITEM_ENTITY_CODE;
		else if (entity.equals(PATH_ELEMENT_ENTITY))
			return PATH_ELEMENT_ENTITY_CODE;
		else if (entity.equals(SCHEME_ENTITY))
			return SCHEME_ENTITY_CODE;
		else if (entity.equals(SCHEME_CABLE_LINK_ENTITY))
			return SCHEME_CABLE_LINK_ENTITY_CODE;
		else if (entity.equals(SCHEME_CABLE_PORT_ENTITY))
			return SCHEME_CABLE_PORT_ENTITY_CODE;
		else if (entity.equals(SCHEME_CABLE_THREAD_ENTITY))
			return SCHEME_CABLE_THREAD_ENTITY_CODE;
		else if (entity.equals(SCHEME_DEVICE_ENTITY))
			return SCHEME_DEVICE_ENTITY_CODE;
		else if (entity.equals(SCHEME_ELEMENT_ENTITY))
			return SCHEME_ELEMENT_ENTITY_CODE;
		else if (entity.equals(SCHEME_LINK_ENTITY))
			return SCHEME_LINK_ENTITY_CODE;
		else if (entity.equals(SCHEME_MONITORING_SOLUTION_ENTITY))
			return SCHEME_MONITORING_SOLUTION_ENTITY_CODE;
		else if (entity.equals(SCHEME_OPTIMIZE_INFO_ENTITY))
			return SCHEME_OPTIMIZE_INFO_ENTITY_CODE;
		else if (entity.equals(SCHEME_PATH_ENTITY))
			return SCHEME_PATH_ENTITY_CODE;
		else if (entity.equals(SCHEME_PORT_ENTITY))
			return SCHEME_PORT_ENTITY_CODE;
		else if (entity.equals(SCHEME_PROTO_ELEMENT_ENTITY))
			return SCHEME_PROTO_ELEMENT_ENTITY_CODE;
		else if (entity.equals(SCHEME_PROTO_GROUP_ENTITY))
			return SCHEME_PROTO_GROUP_ENTITY_CODE;

		else if (entity.equals(SITE_NODE_ENTITY)) return SITE_NODE_ENTITY_CODE;
		else if (entity.equals(TOPOLOGICAL_NODE_ENTITY)) return TOPOLOGICAL_NODE_ENTITY_CODE;
		else if (entity.equals(NODE_LINK_ENTITY)) return NODE_LINK_ENTITY_CODE;
		else if (entity.equals(MARK_ENTITY)) return MARK_ENTITY_CODE;
		else if (entity.equals(PHYSICAL_LINK_ENTITY)) return PHYSICAL_LINK_ENTITY_CODE;
		else if (entity.equals(COLLECTOR_ENTITY)) return COLLECTOR_ENTITY_CODE;
		else if (entity.equals(MAP_ENTITY)) return MAP_ENTITY_CODE;

		else if (entity.equals(IMAGE_RESOURCE_ENTITY))
			return IMAGE_RESOURCE_ENTITY_CODE;

		else if (entity.equals(UPDIKE_ENTITY))
			return UPDIKE_ENTITY_CODE;

		else return UNKNOWN_ENTITY_CODE;
	}

	public static String codeToString(final short code) {
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
			case CABLETHREADTYPE_ENTITY_CODE:
				return CABLETHREADTYPE_ENTITY;

			case PARAMETERTYPE_ENTITY_CODE:
				return PARAMETERTYPE_ENTITY;
			case MEASUREMENTTYPE_ENTITY_CODE:
				return MEASUREMENTTYPE_ENTITY;
			case ANALYSISTYPE_ENTITY_CODE:
				return ANALYSISTYPE_ENTITY;
			case EVALUATIONTYPE_ENTITY_CODE:
				return EVALUATIONTYPE_ENTITY;

			case SITE_NODE_TYPE_ENTITY_CODE:
				return SITE_NODE_ENTITY;
			case PHYSICAL_LINK_TYPE_ENTITY_CODE:
				return PHYSICAL_LINK_TYPE_ENTITY;

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
//			case KISTYPE_ENTITY_CODE:
//				return KISTYPE_ENTITY;
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

			case CABLE_CHANNELING_ITEM_ENTITY_CODE:
				return CABLE_CHANNELING_ITEM_ENTITY;
			case PATH_ELEMENT_ENTITY_CODE:
				return PATH_ELEMENT_ENTITY;
			case SCHEME_ENTITY_CODE:
				return SCHEME_ENTITY;
			case SCHEME_CABLE_LINK_ENTITY_CODE:
				return SCHEME_CABLE_LINK_ENTITY;
			case SCHEME_CABLE_PORT_ENTITY_CODE:
				return SCHEME_CABLE_PORT_ENTITY;
			case SCHEME_CABLE_THREAD_ENTITY_CODE:
				return SCHEME_CABLE_THREAD_ENTITY;
			case SCHEME_DEVICE_ENTITY_CODE:
				return SCHEME_DEVICE_ENTITY;
			case SCHEME_ELEMENT_ENTITY_CODE:
				return SCHEME_ELEMENT_ENTITY;
			case SCHEME_LINK_ENTITY_CODE:
				return SCHEME_LINK_ENTITY;
			case SCHEME_MONITORING_SOLUTION_ENTITY_CODE:
				return SCHEME_MONITORING_SOLUTION_ENTITY;
			case SCHEME_OPTIMIZE_INFO_ENTITY_CODE:
				return SCHEME_OPTIMIZE_INFO_ENTITY;
			case SCHEME_PATH_ENTITY_CODE:
				return SCHEME_PATH_ENTITY;
			case SCHEME_PORT_ENTITY_CODE:
				return SCHEME_PORT_ENTITY;
			case SCHEME_PROTO_ELEMENT_ENTITY_CODE:
				return SCHEME_PROTO_ELEMENT_ENTITY;
			case SCHEME_PROTO_GROUP_ENTITY_CODE:
				return SCHEME_PROTO_GROUP_ENTITY;

			case SITE_NODE_ENTITY_CODE:
				return SITE_NODE_ENTITY;
			case TOPOLOGICAL_NODE_ENTITY_CODE:
				return TOPOLOGICAL_NODE_ENTITY;
			case NODE_LINK_ENTITY_CODE:
				return NODE_LINK_ENTITY;
			case MARK_ENTITY_CODE:
				return MARK_ENTITY;
			case PHYSICAL_LINK_ENTITY_CODE:
				return PHYSICAL_LINK_ENTITY;
			case COLLECTOR_ENTITY_CODE:
				return COLLECTOR_ENTITY;
			case MAP_ENTITY_CODE:
				return MAP_ENTITY;

			case IMAGE_RESOURCE_ENTITY_CODE:
				return IMAGE_RESOURCE_ENTITY;

			case UPDIKE_ENTITY_CODE:
				return UPDIKE_ENTITY;

			case UNKNOWN_ENTITY_CODE:
			default:
				return null;
		}
	}
}
