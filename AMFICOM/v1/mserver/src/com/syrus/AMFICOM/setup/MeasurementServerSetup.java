/*
 * $Id: MeasurementServerSetup.java,v 1.8 2004/08/22 18:58:07 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.setup;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.IdentifierGenerator;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.*;
import com.syrus.AMFICOM.measurement.*;
import com.syrus.AMFICOM.measurement.corba.*;
import com.syrus.AMFICOM.mserver.DatabaseContextSetup;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.ByteArray;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.8 $, $Date: 2004/08/22 18:58:07 $
 * @author $Author: arseniy $
 * @module mserver_v1
 */

public class MeasurementServerSetup {
	public static final String DB_SID = "amficom";
	public static final int DB_CONNECTION_TIMEOUT = 120;
	public static final String DB_LOGIN_NAME = "amficom";

	public static final String CODENAME_REF_WVLEN = "ref_wvlen";
	public static final String CODENAME_REF_TRCLEN = "ref_trclen";
	public static final String CODENAME_REF_RES = "ref_res";
	public static final String CODENAME_REF_PULSWD = "ref_pulswd";
	public static final String CODENAME_REF_IOR = "ref_ior";
	public static final String CODENAME_REF_SCANS = "ref_scans";
	public static final String CODENAME_REFLECTOGRAMMA = "reflectogramma";
	public static final String CODENAME_DADARA_TACTIC = "ref_uselinear";
	public static final String CODENAME_DADARA_EVENT_SIZE = "ref_eventsize";
	public static final String CODENAME_DADARA_CONN_FALL_PARAMS = "ref_conn_fall_params";
	public static final String CODENAME_DADARA_MIN_LEVEL = "ref_min_level";
	public static final String CODENAME_DADARA_MAX_LEVEL_NOISE = "ref_max_level_noise";
	public static final String CODENAME_DADARA_MIN_LEVEL_TO_FIND_END = "ref_min_level_to_find_end";
	public static final String CODENAME_DADARA_MIN_WELD = "ref_min_weld";
	public static final String CODENAME_DADARA_MIN_CONNECTOR = "ref_min_connector";
	public static final String CODENAME_DADARA_STRATEGY = "ref_strategy";
	public static final String CODENAME_DADARA_EVENT_ARRAY = "dadara_event_array";
	public static final String CODENAME_DADARA_ETALON_EVENT_ARRAY = "dadara_etalon_event_array";
	public static final String CODENAME_DADARA_THRESHOLDS = "dadara_thresholds";
	public static final String CODENAME_DADARA_ALARM_ARRAY = "dadara_alarm_array";

	private static final String CODENAME_MEASUREMENT_TYPE_REFLECTOMETRY = "reflectometry";
	private static final String CODENAME_ANALYSIS_TYPE_DADARA = "dadara";
	private static final String CODENAME_EVALUATION_TYPE_DADARA = "dadara"; 

	private MeasurementServerSetup() {
	}

	public static void main(String[] args) {
		Application.init("mserver");
		establishDatabaseConnection();
		DatabaseContextSetup.initDatabaseContext();
		DatabaseContextSetup.initObjectPools();

		Identifier sysAdminId = createSystemAdministrator();

		EquipmentType equipmentType = createEquipmentType(sysAdminId);

		PortType portType = createPortType(sysAdminId);

		MeasurementPortType mPortType = createMeasurementPortType(sysAdminId);


		Identifier domainId = createDomain(sysAdminId);


		Identifier serverUserId = createUser(sysAdminId,
																				 "mserver",
																				 UserSort.USER_SORT_SERVER,
																				 "User Serverovich",
																				 "User for measurement server");
		Identifier serverId = createServer(sysAdminId,
																			 domainId,
																			 serverUserId);

		Identifier mcmUserId = createUser(sysAdminId,
																			"mcm",
																			UserSort.USER_SORT_MCM,
																			"User Mcmovich",
																			"User for measurement control module");
		Identifier mcmId = createMCM(sysAdminId, domainId, mcmUserId, serverId);

		Identifier equipmentId = createEquipment(sysAdminId,
																						 domainId,
																						 equipmentType);

		Identifier portId1 = createPort(sysAdminId, portType, equipmentId);
		Identifier portId2 = createPort(sysAdminId, portType, equipmentId);

		Identifier tpId = createTransmissionPath(sysAdminId, domainId, portId1, portId2);


		Identifier kisId = createKIS(sysAdminId, domainId, equipmentId, mcmId);

		Identifier mportId = createMeasurementPort(sysAdminId, mPortType, kisId, portId1);

		MonitoredElement monitoredElement = createMonitoredElement(sysAdminId, domainId, mportId, tpId);


		createParameterTypes(sysAdminId);

		createTests(sysAdminId, monitoredElement);


		DatabaseConnection.closeConnection();
	}

	private static Identifier createSystemAdministrator() {
		try {
			Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.USER_ENTITY_CODE);
			User user = User.createInstance(id,
																			id,
																			"sys",
																			UserSort.USER_SORT_SYSADMIN,
																			"sys",
																			"System Administrator");
																			
			User user1 = new User((User_Transferable)user.getTransferable());
			return id;
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static EquipmentType createEquipmentType(Identifier creatorId) {
		try {
			Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE);
			EquipmentType eqType = EquipmentType.createInstance(id,
																													creatorId,
																													"EqTypeKIS",
																													"");
			EquipmentType eqType1 = new EquipmentType((EquipmentType_Transferable)eqType.getTransferable());
			return eqType;
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static PortType createPortType(Identifier creatorId) {
		try {
			Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.PORTTYPE_ENTITY_CODE);
			PortType portType = PortType.createInstance(id,
																									creatorId,
																									"PortTypeReflectometry",
																									"");
			PortType portType1 = new PortType((PortType_Transferable)portType.getTransferable());
			return portType;
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static MeasurementPortType createMeasurementPortType(Identifier creatorId) {
		try {
			Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE);
			MeasurementPortType mportType = MeasurementPortType.createInstance(id,
																																				 creatorId,
																																				 "MeasurementPortTypeReflectometry",
																																				 "");
			MeasurementPortType mportType1 = new MeasurementPortType((MeasurementPortType_Transferable)mportType.getTransferable());
			return mportType;
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static Identifier createDomain(Identifier creatorId) {
		try {
			Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.DOMAIN_ENTITY_CODE);
			Domain domain = Domain.createInstance(id,
																						creatorId,
																						null,
																						"domain 1",
																						"System domain");
			Domain domain1 = new Domain((Domain_Transferable)domain.getTransferable());
			return id;
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static Identifier createServer(Identifier creatorId,
																				 Identifier domainId,
																				 Identifier serverUserId) {
		try {
			Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.SERVER_ENTITY_CODE);
			Server server = Server.createInstance(id,
																						creatorId,
																						domainId,
																						"server 1",
																						"Measurement server",
																						serverUserId);
			new Server((Server_Transferable)server.getTransferable());
			return id;
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static Identifier createMCM(Identifier creatorId,
																			Identifier domainId,
																			Identifier mcmUserId,
																			Identifier serverId) {
		try {
			Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.MCM_ENTITY_CODE);
			MCM mcm = MCM.createInstance(id,
																	 creatorId,
																	 domainId,
																	 "mcm 1",
																	 "Measurement control module",
																	 mcmUserId,
																	 serverId);
			new MCM((MCM_Transferable)mcm.getTransferable());
			return id;
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static Identifier createKIS(Identifier creatorId,
																			Identifier domainId,
																			Identifier equipmentId,
																			Identifier mcmId) {
		try {
			Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.KIS_ENTITY_CODE);
			KIS kis = KIS.createInstance(id,
																	 creatorId,
																	 domainId,
																	 "KIS",
																	 "kis ",
																	 equipmentId,
																	 mcmId);
			KIS kis1 = new KIS((KIS_Transferable)kis.getTransferable());
			return id;
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static Identifier createEquipment(Identifier creatorId,
																						Identifier domainId,
																						EquipmentType eqType) {
		try {
			Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.EQUIPMENT_ENTITY_CODE);
			Equipment eq = Equipment.createInstance(id,
																							creatorId,
																							domainId,
																							eqType,
																							"Equipment",
																							"equipment",
																							new Identifier("Image_1"));
			Equipment eq1 = new Equipment((Equipment_Transferable)eq.getTransferable());
			return id;
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static Identifier createPort(Identifier creatorId,
																			 PortType type,
																			 Identifier equipmentId) {
		try {
			Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.PORT_ENTITY_CODE);
			Port port = Port.createInstance(id,
																			creatorId,
																			type,
																			"Port",
																			equipmentId,
																			PortSort._PORT_SORT_PORT);
			Port port1 = new Port((Port_Transferable)port.getTransferable()); 
			return id;
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static Identifier createTransmissionPath(Identifier creatorId,
																									 Identifier domainId,
																									 Identifier startPortId,
																									 Identifier finishPortId) {
		try {
			Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.TRANSPATH_ENTITY_CODE);
			TransmissionPath tp = TransmissionPath.createInstance(id,
																														creatorId,
																														domainId,
																														"TransmissionPath",
																														"TransmissionPath",
																														startPortId,
																														finishPortId);
			TransmissionPath tp1 = new TransmissionPath((TransmissionPath_Transferable)tp.getTransferable());
			return id;
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static Identifier createMeasurementPort(Identifier creatorId,
																									MeasurementPortType type,
																									Identifier kisId,
																									Identifier portId) {
		try {
			Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.MEASUREMENTPORT_ENTITY_CODE);
			MeasurementPort mport = MeasurementPort.createInstance(id,
																														 creatorId,
																														 type,
																														 "MeasurementPort",
																														 "MeasurementPortTest",
																														 kisId,
																														 portId);
			MeasurementPort mport1 = new MeasurementPort((MeasurementPort_Transferable)mport.getTransferable()); 
			return id;
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static MonitoredElement createMonitoredElement(Identifier creatorId,
																												 Identifier domainId,
																												 Identifier mPortId,
																												 Identifier transmissionPathId) {
		try {
			List mdmIds = new ArrayList(1);
			mdmIds.add(transmissionPathId);
			Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.ME_ENTITY_CODE);
			MonitoredElement monitoredElement = MonitoredElement.createInstance(id,
																																					creatorId,
																																					domainId,
																																					mPortId,
																																					MonitoredElementSort._MONITOREDELEMENT_SORT_TRANSMISSION_PATH,
																																					"ME",
																																					mdmIds);
			MonitoredElement monitoredElement1 = new MonitoredElement((MonitoredElement_Transferable)monitoredElement.getTransferable()); 
			return monitoredElement;
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static Identifier createUser(Identifier creatorId,
																			 String login,
																			 UserSort sort,
																			 String name,
																			 String description) {
		try {
			Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.USER_ENTITY_CODE);
			User user = User.createInstance(id,
																			creatorId,
																			login,
																			sort,
																			name,
																			description);
																			
			User user1 = new User((User_Transferable)user.getTransferable());
			return id;
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static void createParameterTypes(Identifier creatorId) {
		List inParTyps;
		List criParTyps;
		List etaParTyps;
		List thrParTyps;
		List outParTyps;

		inParTyps = new ArrayList(6);
		inParTyps.add(createParameterType(creatorId, CODENAME_REF_WVLEN, "For reflectometry", "Wavelength"));
		inParTyps.add(createParameterType(creatorId, CODENAME_REF_TRCLEN, "For reflectometry", "Trace length"));
		inParTyps.add(createParameterType(creatorId, CODENAME_REF_RES, "For reflectometry", "Resolution"));
		inParTyps.add(createParameterType(creatorId, CODENAME_REF_PULSWD, "For reflectometry", "Pulse width"));
		inParTyps.add(createParameterType(creatorId, CODENAME_REF_IOR, "For reflectometry", "Index of refraction"));
		inParTyps.add(createParameterType(creatorId, CODENAME_REF_SCANS, "For reflectometry", "Number of averages"));
		outParTyps = new ArrayList(1);
		ParameterType parTypRefl = createParameterType(creatorId, CODENAME_REFLECTOGRAMMA, "For reflectometry", "Reflectogramma");
		outParTyps.add(parTypRefl);
		createMeasurementType(creatorId, CODENAME_MEASUREMENT_TYPE_REFLECTOMETRY, "Reflectometry", inParTyps, outParTyps);

		inParTyps = new ArrayList(1);
		inParTyps.add(parTypRefl);
		criParTyps = new ArrayList(10);
		criParTyps.add(createParameterType(creatorId, CODENAME_DADARA_TACTIC, "For DADARA analysis", "Tactic"));
		criParTyps.add(createParameterType(creatorId, CODENAME_DADARA_EVENT_SIZE, "For DADARA analysis", "Event size"));
		criParTyps.add(createParameterType(creatorId, CODENAME_DADARA_CONN_FALL_PARAMS, "For DADARA analysis", "Connector fall"));
		criParTyps.add(createParameterType(creatorId, CODENAME_DADARA_MIN_LEVEL, "For DADARA analysis", "Min level"));
		criParTyps.add(createParameterType(creatorId, CODENAME_DADARA_MAX_LEVEL_NOISE, "For DADARA analysis", "Max level noise"));
		criParTyps.add(createParameterType(creatorId, CODENAME_DADARA_MIN_LEVEL_TO_FIND_END, "For DADARA analysis", "Min level to find end"));
		criParTyps.add(createParameterType(creatorId, CODENAME_DADARA_MIN_WELD, "For DADARA analysis", "Min weld"));
		criParTyps.add(createParameterType(creatorId, CODENAME_DADARA_MIN_CONNECTOR, "For DADARA analysis", "Min connector"));
		criParTyps.add(createParameterType(creatorId, CODENAME_DADARA_STRATEGY, "For DADARA analysis", "strategy"));
		etaParTyps = new ArrayList(1);
		ParameterType parTypEtalonEventArr = createParameterType(creatorId, CODENAME_DADARA_ETALON_EVENT_ARRAY, "For DADARA analysis", "Etalon event array");
		etaParTyps.add(parTypEtalonEventArr);
		outParTyps = new ArrayList(1);
		ParameterType parTypEventArr = createParameterType(creatorId, CODENAME_DADARA_EVENT_ARRAY, "For DADARA analysis", "Event array");
		outParTyps.add(parTypEventArr);
		createAnalysisType(creatorId, CODENAME_ANALYSIS_TYPE_DADARA, "DADARA", inParTyps, criParTyps, etaParTyps, outParTyps);

		inParTyps = new ArrayList(1);
		inParTyps.add(parTypRefl);
		thrParTyps = new ArrayList(1);
		thrParTyps.add(createParameterType(creatorId, CODENAME_DADARA_THRESHOLDS, "For DADARA analysis", "Thresholds"));
		etaParTyps = new ArrayList(1);
		etaParTyps.add(parTypEtalonEventArr);
		outParTyps = new ArrayList(1);
		outParTyps.add(createParameterType(creatorId, CODENAME_DADARA_ALARM_ARRAY, "For DADARA analysis", "Alarms"));
		createEvaluationType(creatorId, CODENAME_EVALUATION_TYPE_DADARA, "DADARA", inParTyps, thrParTyps, etaParTyps, outParTyps);
	}

	private static void checkParameterTypes() {
		try {
			List parameterTypes = ParameterTypeDatabase.retrieveAll();
			ParameterType pt;
			for (Iterator i = parameterTypes.iterator(); i.hasNext();) {
				pt = (ParameterType)i.next();
				System.out.println("id: " + pt.getId() + ", codename: " + pt.getCodename() + ", name: " + pt.getName());
			}

			List measurementTypes = MeasurementTypeDatabase.retrieveAll();
			MeasurementType mt;
			for (Iterator i = measurementTypes.iterator(); i.hasNext();) {
				mt = (MeasurementType)i.next();
				System.out.println("id: " + mt.getId() + ", codename: " + mt.getCodename() + ", description: " + mt.getDescription());
				List inp = mt.getInParameterTypes();
				for (Iterator j = inp.iterator(); j.hasNext();) {
					System.out.println("	in par id: " + ((ParameterType)j.next()).getId());
				}
			}

			List analysisTypes = AnalysisTypeDatabase.retrieveAll();
			AnalysisType at;
			for (Iterator i = analysisTypes.iterator(); i.hasNext();) {
				at = (AnalysisType)i.next();
				System.out.println("id: " + at.getId() + ", codename: " + at.getCodename() + ", description: " + at.getDescription());
				List inp = at.getInParameterTypes();
				for (Iterator j = inp.iterator(); j.hasNext();) {
					System.out.println("	in par id: " + ((ParameterType)j.next()).getId());
				}
			}

			List evaluationTypes = EvaluationTypeDatabase.retrieveAll();
			EvaluationType et;
			for (Iterator i = evaluationTypes.iterator(); i.hasNext();) {
				et = (EvaluationType)i.next();
				System.out.println("id: " + et.getId() + ", codename: " + et.getCodename() + ", description: " + et.getDescription());
				List inp = et.getInParameterTypes();
				for (Iterator j = inp.iterator(); j.hasNext();) {
					System.out.println("	in par id: " + ((ParameterType)j.next()).getId());
				}
			}
		}
		catch (Exception e) {
			Log.errorException(e);
		}
	}

	private static ParameterType createParameterType(Identifier creatorId,
																									 String codename,
																									 String description,
																									 String name) {
		try {
			Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.PARAMETERTYPE_ENTITY_CODE);
			ParameterType parameterType = ParameterType.createInstance(id,
																																 creatorId,
																																 codename,
																																 description,
																																 name);
			new ParameterType((ParameterType_Transferable)parameterType.getTransferable());
			return parameterType;
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static void createMeasurementType(Identifier creatorId,
																						String codename,
																						String description,
																						List inParameterTypes,
																						List outParameterTypes) {
		try {
			Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE);
			MeasurementType measurementType = MeasurementType.createInstance(id,
																																			 creatorId,
																																			 codename,
																																			 description,
																																			 inParameterTypes,
																																			 outParameterTypes);
			new MeasurementType((MeasurementType_Transferable)measurementType.getTransferable());
		}
		catch (Exception e) {
			Log.errorException(e);
		}
	}

	private static void createAnalysisType(Identifier creatorId,
																				 String codename,
																				 String description,
																				 List inParameterTypes,
																				 List criParameterTypes,
																				 List	etaParameterTypes,
																				 List outParameterTypes) {
		try {
			Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.ANALYSISTYPE_ENTITY_CODE);
			AnalysisType analysisType = AnalysisType.createInstance(id,
																															creatorId,
																															codename,
																															description,
																															inParameterTypes,
																															criParameterTypes,
																															etaParameterTypes,
																															outParameterTypes);
			new AnalysisType((AnalysisType_Transferable)analysisType.getTransferable());
		}
		catch (Exception e) {
			Log.errorException(e);
		}
	}

	private static void createEvaluationType(Identifier creatorId,
																					 String codename,
																					 String description,
																					 List inParameterTypes,
																					 List thrParameterTypes,
																					 List	etaParameterTypes,
																					 List outParameterTypes) {
		try {
			Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.EVALUATIONTYPE_ENTITY_CODE);
			EvaluationType evaluationType = EvaluationType.createInstance(id,
																																		creatorId,
																																		codename,
																																		description,
																																		inParameterTypes,
																																		thrParameterTypes,
																																		etaParameterTypes,
																																		outParameterTypes);
			new EvaluationType((EvaluationType_Transferable)evaluationType.getTransferable());
		}
		catch (Exception e) {
			Log.errorException(e);
		}
	}

	private static void createTests(Identifier creatorId, MonitoredElement monitoredElement) {
		List meIds = new ArrayList(1);
		meIds.add(monitoredElement.getId());
		Set parameterSet = createParameterSet(creatorId, meIds);
		Set criteriaSet = createCriteriaSet(creatorId, meIds);
		MeasurementSetup measurementSetup = createMeasurementSetup(creatorId, 
																															 parameterSet,
																															 null,
																															 null,
																															 null,
																															 meIds);

		List msIds = new ArrayList(1);
		msIds.add(measurementSetup.getId());
		createOnetimeTest(creatorId, monitoredElement, msIds);

		TemporalPattern temporalPattern = createTemporalPattern(creatorId);

		createPeriodicalTest(creatorId, temporalPattern, monitoredElement, msIds);
	}

	private static Set createParameterSet(Identifier creatorId, List monitoredElementIds) {
		try {
			ParameterType wvlenParam = ParameterTypeDatabase.retrieveForCodename(CODENAME_REF_WVLEN);
			ParameterType trclenParam = ParameterTypeDatabase.retrieveForCodename(CODENAME_REF_TRCLEN);
			ParameterType resParam = ParameterTypeDatabase.retrieveForCodename(CODENAME_REF_RES);
			ParameterType pulswdParam = ParameterTypeDatabase.retrieveForCodename(CODENAME_REF_PULSWD);
			ParameterType iorParam = ParameterTypeDatabase.retrieveForCodename(CODENAME_REF_IOR);
			ParameterType scansParam = ParameterTypeDatabase.retrieveForCodename(CODENAME_REF_SCANS);

			SetParameter[] params = new SetParameter[6];
			Identifier paramId;
			paramId = IdentifierGenerator.generateIdentifier(ObjectEntities.SETPARAMETER_ENTITY_CODE);
			params[0] = new SetParameter(paramId, wvlenParam, new ByteArray((int) 1625).getBytes());

			paramId = IdentifierGenerator.generateIdentifier(ObjectEntities.SETPARAMETER_ENTITY_CODE);
			params[1] = new SetParameter(paramId, trclenParam, new ByteArray((double) 131072).getBytes());

			paramId = IdentifierGenerator.generateIdentifier(ObjectEntities.SETPARAMETER_ENTITY_CODE);
			params[2] = new SetParameter(paramId, resParam, new ByteArray((double) 8).getBytes());

			paramId = IdentifierGenerator.generateIdentifier(ObjectEntities.SETPARAMETER_ENTITY_CODE);
			params[3] = new SetParameter(paramId, pulswdParam, new ByteArray((long) 5000).getBytes());

			paramId = IdentifierGenerator.generateIdentifier(ObjectEntities.SETPARAMETER_ENTITY_CODE);
			params[4] = new SetParameter(paramId, iorParam, new ByteArray((double) 1.457).getBytes());

			paramId = IdentifierGenerator.generateIdentifier(ObjectEntities.SETPARAMETER_ENTITY_CODE);
			params[5] = new SetParameter(paramId, scansParam, new ByteArray((double) 4000).getBytes());

			Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.SET_ENTITY_CODE);
			Set set = Set.createInstance(id,
																	 creatorId,
																	 SetSort.SET_SORT_MEASUREMENT_PARAMETERS,
																	 "Set of measurement parameters",
																	 params,
																	 monitoredElementIds);
			Set set1 = new Set((Set_Transferable) set.getTransferable());
			return set;
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static Set createCriteriaSet(Identifier creatorId, List monitoredElementIds) {
		try {
			ParameterType tacticParam = ParameterTypeDatabase.retrieveForCodename(CODENAME_DADARA_TACTIC);
			ParameterType eventsizeParam = ParameterTypeDatabase.retrieveForCodename(CODENAME_DADARA_EVENT_SIZE);
			ParameterType connfallParam = ParameterTypeDatabase.retrieveForCodename(CODENAME_DADARA_CONN_FALL_PARAMS);
			ParameterType minlevelParam = ParameterTypeDatabase.retrieveForCodename(CODENAME_DADARA_MIN_LEVEL);
			ParameterType maxlevelnoiseParam = ParameterTypeDatabase.retrieveForCodename(CODENAME_DADARA_MAX_LEVEL_NOISE);
			ParameterType minlevelendParam = ParameterTypeDatabase.retrieveForCodename(CODENAME_DADARA_MIN_LEVEL_TO_FIND_END);
			ParameterType minweldParam = ParameterTypeDatabase.retrieveForCodename(CODENAME_DADARA_MIN_WELD);
			ParameterType minconnectorParam = ParameterTypeDatabase.retrieveForCodename(CODENAME_DADARA_MIN_CONNECTOR);
			ParameterType strategyParam = ParameterTypeDatabase.retrieveForCodename(CODENAME_DADARA_STRATEGY);

			SetParameter[] params = new SetParameter[9];
			Identifier paramId;
			paramId = IdentifierGenerator.generateIdentifier(ObjectEntities.SETPARAMETER_ENTITY_CODE);
			params[0] = new SetParameter(paramId, tacticParam, new ByteArray((int) 1).getBytes());

			paramId = IdentifierGenerator.generateIdentifier(ObjectEntities.SETPARAMETER_ENTITY_CODE);
			params[1] = new SetParameter(paramId, eventsizeParam, new ByteArray((int) 2).getBytes());

			paramId = IdentifierGenerator.generateIdentifier(ObjectEntities.SETPARAMETER_ENTITY_CODE);
			params[2] = new SetParameter(paramId, connfallParam, new ByteArray((double) 5.0).getBytes());

			paramId = IdentifierGenerator.generateIdentifier(ObjectEntities.SETPARAMETER_ENTITY_CODE);
			params[3] = new SetParameter(paramId, minlevelParam, new ByteArray((double) 0.5).getBytes());

			paramId = IdentifierGenerator.generateIdentifier(ObjectEntities.SETPARAMETER_ENTITY_CODE);
			params[4] = new SetParameter(paramId, maxlevelnoiseParam, new ByteArray((double) 1.0).getBytes());

			paramId = IdentifierGenerator.generateIdentifier(ObjectEntities.SETPARAMETER_ENTITY_CODE);
			params[5] = new SetParameter(paramId, minlevelendParam, new ByteArray((double) 3.0).getBytes());

			paramId = IdentifierGenerator.generateIdentifier(ObjectEntities.SETPARAMETER_ENTITY_CODE);
			params[6] = new SetParameter(paramId, minweldParam, new ByteArray((double) 0.2).getBytes());

			paramId = IdentifierGenerator.generateIdentifier(ObjectEntities.SETPARAMETER_ENTITY_CODE);
			params[7] = new SetParameter(paramId, minconnectorParam, new ByteArray((double) 0.2).getBytes());

			paramId = IdentifierGenerator.generateIdentifier(ObjectEntities.SETPARAMETER_ENTITY_CODE);
			params[8] = new SetParameter(paramId, strategyParam, new ByteArray((int) 1).getBytes());

			Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.SET_ENTITY_CODE);
			Set set = Set.createInstance(id,
																	 creatorId,
																	 SetSort.SET_SORT_MEASUREMENT_PARAMETERS,
																	 "Set of criteria",
																	 params,
																	 monitoredElementIds);
			Set set1 = new Set((Set_Transferable) set.getTransferable());
			return set;
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static MeasurementSetup createMeasurementSetup(Identifier creatorId,
																												 Set parameterSet,
																												 Set criteriaSet,
																												 Set thresholdSet,
																												 Set etalon,
																												 List monitoredElementIds) {
		try {
			Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.MS_ENTITY_CODE);
			MeasurementSetup mSetup = MeasurementSetup.createInstance(id,
																																creatorId,
																																parameterSet,
																																criteriaSet,
																																thresholdSet,
																																etalon,
																																"created by MeasurementSetupTestCase",
																																1000 * 60 * 10,
																																monitoredElementIds);
			MeasurementSetup mSetup1 = new MeasurementSetup((MeasurementSetup_Transferable)mSetup.getTransferable());
			return mSetup;
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static TemporalPattern createTemporalPattern(Identifier creatorId) {
		try {
			Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.TEMPORALPATTERN_ENTITY_CODE);
			String[] strings = new String[1];
			strings[0] = "*/10 * * * *";
			TemporalPattern temporalPattern = TemporalPattern.createInstance(id,
																																			 creatorId,
																																			 "TemporalPattern",
																																			 strings);
			TemporalPattern temporalPattern1 = new TemporalPattern((TemporalPattern_Transferable)temporalPattern.getTransferable());
			return temporalPattern;
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static Test createOnetimeTest(Identifier creatorId,
																				MonitoredElement monitoredElement,
																				List measurementSetupIds) {
		try {
			Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.TEST_ENTITY_CODE);

			MeasurementType measurementType = MeasurementTypeDatabase.retrieveForCodename(CODENAME_MEASUREMENT_TYPE_REFLECTOMETRY);
			AnalysisType analysisType = AnalysisTypeDatabase.retrieveForCodename(CODENAME_ANALYSIS_TYPE_DADARA);
			EvaluationType evaluationType = EvaluationTypeDatabase.retrieveForCodename(CODENAME_EVALUATION_TYPE_DADARA);

			Test test = Test.createInstance(id,
																			creatorId,
																			new Date(System.currentTimeMillis()),
																			null,
																			null,
																			TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME,
																			measurementType,
																			analysisType,
																			evaluationType,
																			monitoredElement,
																			TestReturnType.TEST_RETURN_TYPE_WHOLE,
																			"Onetime test",
																			measurementSetupIds);
			Test test1 = new Test((Test_Transferable)test.getTransferable());
			return test;
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static Test createPeriodicalTest(Identifier creatorId,
																					 TemporalPattern temporalPattern,
																					 MonitoredElement monitoredElement,
																					 List measurementSetupIds) {
		try {
			Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.TEST_ENTITY_CODE);

			MeasurementType measurementType = MeasurementTypeDatabase.retrieveForCodename(CODENAME_MEASUREMENT_TYPE_REFLECTOMETRY);
			AnalysisType analysisType = AnalysisTypeDatabase.retrieveForCodename(CODENAME_ANALYSIS_TYPE_DADARA);
			EvaluationType evaluationType = EvaluationTypeDatabase.retrieveForCodename(CODENAME_EVALUATION_TYPE_DADARA);

			Test test = Test.createInstance(id,
																			creatorId,
																			new Date(System.currentTimeMillis()),
																			new Date(System.currentTimeMillis() + 1000 * 60 * 30),
																			temporalPattern,
																			TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL,
																			measurementType,
																			analysisType,
																			evaluationType,
																			monitoredElement,
																			TestReturnType.TEST_RETURN_TYPE_WHOLE,
																			"Onetime test",
																			measurementSetupIds);
			Test test1 = new Test((Test_Transferable)test.getTransferable());
			return test;
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static void establishDatabaseConnection() {
		String dbHostName = ApplicationProperties.getString("DBHostName", Application.getInternetAddress());
		String dbSid = ApplicationProperties.getString("DBSID", DB_SID);
		long dbConnTimeout = ApplicationProperties.getInt("DBConnectionTimeout", DB_CONNECTION_TIMEOUT)*1000;
		String dbLoginName = ApplicationProperties.getString("DBLoginName", DB_LOGIN_NAME);
		try {
			DatabaseConnection.establishConnection(dbHostName, dbSid, dbConnTimeout, dbLoginName);
		}
		catch (Exception e) {
			Log.errorException(e);
			System.exit(-1);
		}
	}
}
