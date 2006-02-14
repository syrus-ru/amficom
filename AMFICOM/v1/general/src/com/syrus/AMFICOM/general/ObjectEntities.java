/*-
 * $Id: ObjectEntities.java,v 1.96.2.3 2006/02/14 15:44:07 arseniy Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import static java.util.logging.Level.FINEST;
import static java.util.logging.Level.SEVERE;
import gnu.trove.TObjectShortHashMap;
import gnu.trove.TShortObjectHashMap;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.96.2.3 $, $Date: 2006/02/14 15:44:07 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public final class ObjectEntities {
	/*	##################################### General ##################################### */

	/*	Type */
	public static final String PARAMETER_TYPE = "ParameterType";
	public static final String CHARACTERISTIC_TYPE = "CharacteristicType";

	/*	Object */
	public static final String CHARACTERISTIC = "Characteristic";


	/*	##################################### Administration ##################################### */

	/*	Object */
	public static final String SYSTEMUSER = "SystemUser";
	public static final String DOMAIN = "Domain";
	public static final String SERVER = "Server";
	public static final String MCM = "MCM";
	public static final String SERVERPROCESS = "ServerProcess";
	public static final String PERMATTR = "PermissionAttributes";
	public static final String ROLE = "Role";


	/*	##################################### Configuration ##################################### */

	/*	Type */
	public static final String EQUIPMENT_TYPE = "EquipmentType";
	public static final String PORT_TYPE = "PortType";
	public static final String TRANSPATH_TYPE = "TransmissionPathType";
	public static final String LINK_TYPE = "LinkType";
	public static final String CABLELINK_TYPE = "CableLinkType";
	public static final String CABLETHREAD_TYPE = "CableThreadType";

	/*	Object */
	public static final String PROTOEQUIPMENT = "ProtoEquipment";
	public static final String EQUIPMENT = "Equipment";
	public static final String PORT = "Port";
	public static final String TRANSMISSIONPATH = "TransmissionPath";
	public static final String LINK = "Link";
	public static final String CABLETHREAD = "CableThread";
	public static final String CABLELINK = "CableLink";


	/*	##################################### Measurement ##################################### */

	/*	Type */
	public static final String MEASUREMENT_TYPE = "MeasurementType";
	public static final String ANALYSIS_TYPE = "AnalysisType";
	public static final String MODELING_TYPE = "ModelingType";
	public static final String MEASUREMENTPORT_TYPE = "MeasurementPortType";
	public static final String ACTIONPARAMETERTYPEBINDING = "ActionParameterTypeBinding";

	/*	Object */
	public static final String PARAMETERSET = "ParameterSet";
	public static final String ACTIONPARAMETER = "ActionParameter";
	public static final String ACTIONTEMPLATE = "ActionTemplate";
	public static final String MEASUREMENT = "Measurement";
	public static final String ANALYSIS = "Analysis";
	public static final String MODELING = "Modeling";
	public static final String TEST = "Test";
	public static final String RESULTPARAMETER = "ResultParameter";
	public static final String CRONTEMPORALPATTERN = "CronTemporalPattern";
	public static final String INTERVALSTEMPORALPATTERN = "ITempPattern";
	public static final String PERIODICALTEMPORALPATTERN = "PeriodicalTemporalPattern";
	public static final String MEASUREMENTPORT = "MeasurementPort";
	public static final String KIS = "KIS";
	public static final String MONITOREDELEMENT = "MonitoredElement";
	public static final String MEASUREMENTRESULTPARAMETER = "MeasurementResultParameter";
	public static final String ANALYSISRESULTPARAMETER = "AnalysisResultParameter";
	public static final String MODELINGRESULTPARAMETER = "ModelingResultParameter";


	/*	##################################### Event ##################################### */

	/*	Type */
	public static final String EVENT_TYPE = "EventType";
	/*	Object */
	public static final String EVENTPARAMETER = "EventParameter";
	public static final String EVENTSOURCE = "EventSource";
	public static final String EVENT = "Event";
	public static final String DELIVERYATTRIBUTES = "DeliveryAttributes";


	/*	##################################### Scheme ##################################### */

	/*	Object */
	public static final String CABLECHANNELINGITEM = "CableChannelingItem";
	public static final String PATHELEMENT = "PathElement";
	public static final String SCHEME = "Scheme";
	public static final String SCHEMECABLELINK = "SchemeCableLink";
	public static final String SCHEMECABLEPORT = "SchemeCablePort";
	public static final String SCHEMECABLETHREAD = "SchemeCableThread";
	public static final String SCHEMEDEVICE = "SchemeDevice";
	public static final String SCHEMEELEMENT = "SchemeElement";
	public static final String SCHEMELINK = "SchemeLink";
	public static final String SCHEMEMONITORINGSOLUTION = "SchemeMonitoringSolution";
	public static final String SCHEMEOPTIMIZEINFO = "SchemeOptimizeInfo";
	public static final String SCHEMEOPTIMIZEINFOSWITCH = "SchemeOptimizeInfoSwitch";
	public static final String SCHEMEOPTIMIZEINFORTU = "SchemeOptimizeInfoRtu";
	public static final String SCHEMEPATH = "SchemePath";
	public static final String SCHEMEPORT = "SchemePort";
	public static final String SCHEMEPROTOELEMENT = "SchemeProtoElement";
	public static final String SCHEMEPROTOGROUP = "SchemeProtoGroup";


	/*	##################################### Map ##################################### */

	/*	Type */
	public static final String SITENODE_TYPE = "SiteNodeType";
	public static final String PHYSICALLINK_TYPE = "PhysicalLinkType";

	/*	Object */
	public static final String SITENODE = "SiteNode";
	public static final String TOPOLOGICALNODE = "TopologicalNode";
	public static final String NODELINK = "NodeLink";
	public static final String MARK = "Mark";
	public static final String PHYSICALLINK = "PhysicalLink";
	public static final String COLLECTOR = "Collector";
	public static final String MAP = "Map";
	public static final String MAPLIBRARY = "MapLibrary";
	public static final String PIPEBLOCK = "PipeBlock";


	/*	##################################### Resource ##################################### */

	/*	Object */
	public static final String IMAGERESOURCE = "ImageResource";
	public static final String LAYOUT_ITEM = "LayoutItem";


	/*	##################################### MapView ##################################### */

	/*	Object */
	public static final String MAPVIEW = "MapView";
	
	/*	##################################### Report ##################################### */

	/*	Object */
	public static final String ATTACHEDTEXT = "AttachedText";
	public static final String REPORTIMAGE = "ReportImage";
	public static final String REPORTDATA = "ReportData";
	public static final String REPORTTABLEDATA = "ReportTableData";
	public static final String REPORTTEMPLATE = "ReportTemplate";


	/*	##################################### Updike ##################################### */

	/**
	 * Шняга: {@value}.
	 */
	public static final String UPDIKE = "8========================D";


	/*
	 * Здесь могла бы быть ваша реклама: 1-64 (0x0001-0x0040)
	 */

	/*
	 * General:               65- 96 (0x0041-0x0060)
	 * General Types:         97-128 (0x0061-0x0080)
	 */
	public static final short GENERAL_MIN_CODE = 0x0041;

	public static final short CHARACTERISTIC_CODE = GENERAL_MIN_CODE;

	public static final short PARAMETER_TYPE_CODE = 0x0061;
	public static final short CHARACTERISTIC_TYPE_CODE = 0x0062;

	public static final short GENERAL_MAX_CODE = 0x0080;

	/*
	 * Event:                129-192 (0x0081-0x00C0)
	 * Event Types:	         193-256 (0x00C1-0x0100)
	 */
	public static final short EVENT_MIN_CODE = 0x0081;

	public static final short EVENTPARAMETER_CODE = EVENT_MIN_CODE;
	public static final short EVENTSOURCE_CODE = 0x0082;
	public static final short EVENT_CODE = 0x0083;
	public static final short DELIVERYATTRIBUTES_CODE = 0x0084;

	public static final short EVENT_TYPE_CODE = 0x00C1;

	public static final short EVENT_MAX_CODE = 0x0100;

	/*
	 * Administration:       257-320 (0x0101-0x0140)
	 * Administration Types: 321-384 (0x0141-0x0180)
	 */
	public static final short ADMINISTRATION_MIN_CODE = 0x0101;

	public static final short SYSTEMUSER_CODE = ADMINISTRATION_MIN_CODE;
	public static final short DOMAIN_CODE = 0x0102;
	public static final short SERVER_CODE = 0x0103;
	public static final short MCM_CODE = 0x0104;
	public static final short SERVERPROCESS_CODE = 0x0105;
	public static final short PERMATTR_CODE = 0x0106;
	public static final short ROLE_CODE = 0x0107;

	public static final short ADMINISTRATION_MAX_CODE = 0x0180;

	/*
	 * Configuration:       385-448 (0x0181-0x01C0)
	 * Configuration Types: 449-512 (0x01C1-0x0200)
	 */
	public static final short CONFIGURATION_MIN_CODE = 0x0181;

	public static final short EQUIPMENT_CODE = CONFIGURATION_MIN_CODE;
	public static final short PORT_CODE = 0x0182;
	public static final short TRANSMISSIONPATH_CODE = 0x0183;
	public static final short LINK_CODE = 0x0184;
	public static final short CABLETHREAD_CODE = 0x0185;
	public static final short CABLELINK_CODE = 0x0186;
	public static final short PROTOEQUIPMENT_CODE = 0x0187;

	public static final short EQUIPMENT_TYPE_CODE = 0x01C1;
	public static final short PORT_TYPE_CODE = 0x01C2;
	public static final short LINK_TYPE_CODE = 0x01C3;
	public static final short TRANSPATH_TYPE_CODE = 0x01C4;
	public static final short CABLETHREAD_TYPE_CODE = 0x01C5;
	public static final short CABLELINK_TYPE_CODE = 0x01C6;

	public static final short CONFIGURATION_MAX_CODE = 0x0200;

	/*
	 * Measurement:       513-576 (0x0201-0x0240)
	 * Measurement Types: 577-640 (0x0241-0x0280)
	 */
	public static final short MEASUREMENT_MIN_CODE = 0x0201;

	public static final short PARAMETERSET_CODE = MEASUREMENT_MIN_CODE;
	public static final short ACTIONPARAMETER_CODE = 0x0202;
	public static final short ACTIONTEMPLATE_CODE = 0x0203;
	public static final short MEASUREMENT_CODE = 0x0204;
	public static final short ANALYSIS_CODE = 0x0205;
	public static final short TEST_CODE = 0x0207;
	public static final short RESULTPARAMETER_CODE = 0x0209;
	public static final short MODELING_CODE = 0x020A;
	public static final short CRONTEMPORALPATTERN_CODE = 0x020B;
	public static final short INTERVALSTEMPORALPATTERN_CODE = 0x020C;
	public static final short PERIODICALTEMPORALPATTERN_CODE = 0x020D;
	public static final short MEASUREMENTPORT_CODE = 0x020E;
	public static final short KIS_CODE = 0x020F;
	public static final short MONITOREDELEMENT_CODE = 0x0210;
	public static final short MEASUREMENTRESULTPARAMETER_CODE = 0x0211;
	public static final short ANALYSISRESULTPARAMETER_CODE = 0x0212;
	public static final short MODELINGRESULTPARAMETER_CODE = 0x0213;

	public static final short MEASUREMENT_TYPE_CODE = 0x0241;
	public static final short ANALYSIS_TYPE_CODE = 0x0242;
	public static final short MODELING_TYPE_CODE = 0x0243;
	public static final short MEASUREMENTPORT_TYPE_CODE = 0x0244;
	public static final short ACTIONPARAMETERTYPEBINDING_CODE = 0x0245;

	public static final short MEASUREMENT_MAX_CODE = 0x0280;

	/*
	 * Scheme:       641-704 (0x0281-0x02C0)
	 * Scheme Types: 705-768 (0x02C1-0x0300)
	 */
	public static final short SCHEME_MIN_CODE = 0x0281;

	public static final short CABLECHANNELINGITEM_CODE = SCHEME_MIN_CODE;
	public static final short PATHELEMENT_CODE = 0x0282;
	public static final short SCHEME_CODE = 0x0283;
	public static final short SCHEMECABLELINK_CODE = 0x0284;
	public static final short SCHEMECABLEPORT_CODE = 0x0285;
	public static final short SCHEMECABLETHREAD_CODE = 0x0286;
	public static final short SCHEMEDEVICE_CODE = 0x0287;
	public static final short SCHEMEELEMENT_CODE = 0x0288;
	public static final short SCHEMELINK_CODE = 0x0289;
	public static final short SCHEMEMONITORINGSOLUTION_CODE = 0x028A;
	public static final short SCHEMEOPTIMIZEINFO_CODE = 0x028B;
	public static final short SCHEMEOPTIMIZEINFOSWITCH_CODE = 0x028C;
	public static final short SCHEMEOPTIMIZEINFORTU_CODE = 0x028D;
	public static final short SCHEMEPATH_CODE = 0x028E;
	public static final short SCHEMEPORT_CODE = 0x028F;
	public static final short SCHEMEPROTOELEMENT_CODE = 0x0290;
	public static final short SCHEMEPROTOGROUP_CODE = 0x0291;

	public static final short SCHEME_MAX_CODE = 0x0300;

	/*
	 * Map:       769-832 (0x0301-0x0340)
	 * Map Types: 833-896 (0x0341-0x0380)
	 */
	public static final short MAP_MIN_CODE = 0x0301;

	public static final short SITENODE_CODE = MAP_MIN_CODE;
	public static final short TOPOLOGICALNODE_CODE = 0x0302;
	public static final short NODELINK_CODE = 0x0303;
	public static final short MARK_CODE = 0x0304;
	public static final short PHYSICALLINK_CODE = 0x0305;
	public static final short COLLECTOR_CODE = 0x0306;
	public static final short MAP_CODE = 0x0307;
	public static final short MAPLIBRARY_CODE = 0x0308;
	public static final short PIPEBLOCK_CODE = 0x0309;

	public static final short SITENODE_TYPE_CODE = 0x0341;
	public static final short PHYSICALLINK_TYPE_CODE = 0x0342;

	public static final short MAP_MAX_CODE = 0x0380;
	
	/*
	 * Resource:       897-960 (0x0381-0x03C0)
	 * Resource Types: 961-1024(0x03C1-0x0400)
	 */
	public static final short RESOURCE_MIN_CODE = 0x0381;

	public static final short IMAGERESOURCE_CODE = RESOURCE_MIN_CODE;
	public static final short LAYOUT_ITEM_CODE = 0x0382;

	public static final short RESOURCE_MAX_CODE = 0x0400;

	/*
	 * MapView:        1025-1088(0x0401-0x0440)
	 * MapView Types:  1089-1152(0x0441-0x0480)
	 */
	public static final short MAPVIEW_MIN_CODE = 0x0401;

	public static final short MAPVIEW_CODE = MAPVIEW_MIN_CODE;

	public static final short MAPVIEW_MAX_CODE = 0x0480;

	/*
	 * Report:        1152-1232(0x0481-0x04C0)
	 * Report Types:  1233-1280(0x04C1-0x0500)
	 */
	public static final short REPORT_MIN_CODE = 0x0481;

	public static final short ATTACHEDTEXT_CODE = REPORT_MIN_CODE;
	public static final short REPORTIMAGE_CODE = 0x0482;
	public static final short REPORTDATA_CODE = 0x0483;
	public static final short REPORTTABLEDATA_CODE = 0x0484;
	public static final short REPORTTEMPLATE_CODE = 0x0485;

	public static final short REPORT_MAX_CODE = 0x0500;

	/*
	 * Updike: 32767 (0x7FFF)
	 */
	public static final short UPDIKE_CODE = Short.MAX_VALUE;

	private static final TObjectShortHashMap NAME_CODE_MAP = new TObjectShortHashMap();

	private static final TShortObjectHashMap CODE_NAME_MAP = new TShortObjectHashMap();

	private static volatile boolean entitiesRegistered = false;

	private ObjectEntities() {
		// empty singleton constructor
		assert false;
	}

	static {
		Log.debugMessage(FINEST);
		try {
			Class.forName(ObjectGroupEntities.class.getName());
		} catch (final ClassNotFoundException cnfe) {
			Log.debugMessage(cnfe, SEVERE);
			/*
			 * Never.
			 */
			assert false;
		}
	}

	/**
	 * This method is <em>not</em> thread safe.
	 */
	static void registerEntities() {
		Log.debugMessage(FINEST);
		if (entitiesRegistered) {
			return;
		}

		registerEntity(PARAMETER_TYPE_CODE, PARAMETER_TYPE);
		registerEntity(CHARACTERISTIC_TYPE_CODE, CHARACTERISTIC_TYPE);

		registerEntity(EVENT_TYPE_CODE, EVENT_TYPE);

		registerEntity(EQUIPMENT_TYPE_CODE, EQUIPMENT_TYPE);
		registerEntity(PORT_TYPE_CODE, PORT_TYPE);
		registerEntity(LINK_TYPE_CODE, LINK_TYPE);
		registerEntity(CABLETHREAD_TYPE_CODE, CABLETHREAD_TYPE);
		registerEntity(CABLELINK_TYPE_CODE, CABLELINK_TYPE);

		registerEntity(MEASUREMENT_TYPE_CODE, MEASUREMENT_TYPE);
		registerEntity(ANALYSIS_TYPE_CODE, ANALYSIS_TYPE);
		registerEntity(MODELING_TYPE_CODE, MODELING_TYPE);
		registerEntity(MEASUREMENTPORT_TYPE_CODE, MEASUREMENTPORT_TYPE);
		registerEntity(ACTIONPARAMETERTYPEBINDING_CODE, ACTIONPARAMETERTYPEBINDING);

		registerEntity(SITENODE_TYPE_CODE, SITENODE_TYPE);
		registerEntity(PHYSICALLINK_TYPE_CODE, PHYSICALLINK_TYPE);

		registerEntity(CHARACTERISTIC_CODE, CHARACTERISTIC);

		registerEntity(EVENTPARAMETER_CODE, EVENTPARAMETER);
		registerEntity(EVENTSOURCE_CODE, EVENTSOURCE);
		registerEntity(EVENT_CODE, EVENT);
		registerEntity(DELIVERYATTRIBUTES_CODE, DELIVERYATTRIBUTES);

		registerEntity(SYSTEMUSER_CODE, SYSTEMUSER);
		registerEntity(DOMAIN_CODE, DOMAIN);
		registerEntity(SERVER_CODE, SERVER);
		registerEntity(MCM_CODE, MCM);
		registerEntity(SERVERPROCESS_CODE, SERVERPROCESS);
		registerEntity(PERMATTR_CODE, PERMATTR);
		registerEntity(ROLE_CODE, ROLE);

		registerEntity(PROTOEQUIPMENT_CODE, PROTOEQUIPMENT);
		registerEntity(EQUIPMENT_CODE, EQUIPMENT);
		registerEntity(PORT_CODE, PORT);
		registerEntity(TRANSMISSIONPATH_CODE, TRANSMISSIONPATH);
		registerEntity(TRANSPATH_TYPE_CODE, TRANSPATH_TYPE);
		registerEntity(LINK_CODE, LINK);
		registerEntity(CABLETHREAD_CODE, CABLETHREAD);
		registerEntity(CABLELINK_CODE, CABLELINK);

		registerEntity(PARAMETERSET_CODE, PARAMETERSET);
		registerEntity(ACTIONPARAMETER_CODE, ACTIONPARAMETER);
		registerEntity(ACTIONTEMPLATE_CODE, ACTIONTEMPLATE);
		registerEntity(MEASUREMENT_CODE, MEASUREMENT);
		registerEntity(ANALYSIS_CODE, ANALYSIS);
		registerEntity(TEST_CODE, TEST);
		registerEntity(RESULTPARAMETER_CODE, RESULTPARAMETER);
		registerEntity(CRONTEMPORALPATTERN_CODE, CRONTEMPORALPATTERN);
		registerEntity(INTERVALSTEMPORALPATTERN_CODE, INTERVALSTEMPORALPATTERN);
		registerEntity(PERIODICALTEMPORALPATTERN_CODE, PERIODICALTEMPORALPATTERN);
		registerEntity(MODELING_CODE, MODELING);
		registerEntity(KIS_CODE, KIS);
		registerEntity(MEASUREMENTPORT_CODE, MEASUREMENTPORT);
		registerEntity(MONITOREDELEMENT_CODE, MONITOREDELEMENT);
		registerEntity(MEASUREMENTRESULTPARAMETER_CODE, MEASUREMENTRESULTPARAMETER);
		registerEntity(ANALYSISRESULTPARAMETER_CODE, ANALYSISRESULTPARAMETER);
		registerEntity(MODELINGRESULTPARAMETER_CODE, MODELINGRESULTPARAMETER);

		registerEntity(CABLECHANNELINGITEM_CODE, CABLECHANNELINGITEM);
		registerEntity(PATHELEMENT_CODE, PATHELEMENT);
		registerEntity(SCHEME_CODE, SCHEME);
		registerEntity(SCHEMECABLELINK_CODE, SCHEMECABLELINK);
		registerEntity(SCHEMECABLEPORT_CODE, SCHEMECABLEPORT);
		registerEntity(SCHEMECABLETHREAD_CODE, SCHEMECABLETHREAD);
		registerEntity(SCHEMEDEVICE_CODE, SCHEMEDEVICE);
		registerEntity(SCHEMEELEMENT_CODE, SCHEMEELEMENT);
		registerEntity(SCHEMELINK_CODE, SCHEMELINK);
		registerEntity(SCHEMEMONITORINGSOLUTION_CODE, SCHEMEMONITORINGSOLUTION);
		registerEntity(SCHEMEOPTIMIZEINFO_CODE, SCHEMEOPTIMIZEINFO);
		registerEntity(SCHEMEOPTIMIZEINFOSWITCH_CODE, SCHEMEOPTIMIZEINFOSWITCH);
		registerEntity(SCHEMEOPTIMIZEINFORTU_CODE, SCHEMEOPTIMIZEINFORTU);
		registerEntity(SCHEMEPATH_CODE, SCHEMEPATH);
		registerEntity(SCHEMEPORT_CODE, SCHEMEPORT);
		registerEntity(SCHEMEPROTOELEMENT_CODE, SCHEMEPROTOELEMENT);
		registerEntity(SCHEMEPROTOGROUP_CODE, SCHEMEPROTOGROUP);

		registerEntity(SITENODE_CODE, SITENODE);
		registerEntity(TOPOLOGICALNODE_CODE, TOPOLOGICALNODE);
		registerEntity(NODELINK_CODE, NODELINK);
		registerEntity(MARK_CODE, MARK);
		registerEntity(PHYSICALLINK_CODE, PHYSICALLINK);
		registerEntity(COLLECTOR_CODE, COLLECTOR);
		registerEntity(MAP_CODE, MAP);
		registerEntity(MAPLIBRARY_CODE, MAPLIBRARY);
		registerEntity(PIPEBLOCK_CODE, PIPEBLOCK);

		registerEntity(IMAGERESOURCE_CODE, IMAGERESOURCE);
		registerEntity(LAYOUT_ITEM_CODE, LAYOUT_ITEM);

		registerEntity(MAPVIEW_CODE, MAPVIEW);

		registerEntity(ATTACHEDTEXT_CODE, ATTACHEDTEXT);
		registerEntity(REPORTIMAGE_CODE, REPORTIMAGE);
		registerEntity(REPORTDATA_CODE, REPORTDATA);
		registerEntity(REPORTTABLEDATA_CODE, REPORTTABLEDATA);
		registerEntity(REPORTTEMPLATE_CODE, REPORTTEMPLATE);

		registerEntity(UPDIKE_CODE, UPDIKE);

		entitiesRegistered = true;
	}

	/**
	 * <p>Should be invoked only <em>after</em> all entities get registered.</p>
	 *
	 * <p>It's ok for this method to return a (mutable) array, since a new
	 * instance is created on every invocation.</p>
	 */
	static short[] getCodes() {
		if (!entitiesRegistered) {
			throw new IllegalStateException();
		}

		return CODE_NAME_MAP.keys();
	}

	private static void registerEntity(final short entityCode, final String entity) {
		assert CODE_NAME_MAP.get(entityCode) == null;
		CODE_NAME_MAP.put(entityCode, entity);
		assert NAME_CODE_MAP.get(entity) == 0;
		NAME_CODE_MAP.put(entity, entityCode);
	}

	public static short stringToCode(final String entity) {
		final short returnValue = NAME_CODE_MAP.get(entity);
		if (returnValue == 0) {
			throw new IllegalArgumentException(entity);
		}
		return returnValue;
	}

	public static String codeToString(final short entityCode) {
		final String returnValue = (String) CODE_NAME_MAP.get(entityCode);
		if (returnValue == null) {
			throw new IllegalArgumentException(String.valueOf(entityCode));
		}
		return returnValue;
	}

	public static String codeToString(final Short code) {
		assert code != null;
		return codeToString(code.shortValue());
	}

	public static boolean isEntityCodeValid(final short entityCode) {
		try {
			codeToString(entityCode);
			return true;
		} catch (final IllegalArgumentException iae) {
			Log.debugMessage(iae, SEVERE);
			return false;
		}
	}
}
