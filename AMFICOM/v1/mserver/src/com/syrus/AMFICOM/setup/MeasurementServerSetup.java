/*
 * $Id: MeasurementServerSetup.java,v 1.22 2004/12/10 12:44:46 bob Exp $
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
 * @version $Revision: 1.22 $, $Date: 2004/12/10 12:44:46 $
 * @author $Author: bob $
 * @module mserver_v1
 */

public final class MeasurementServerSetup {
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
		// singleton constructor
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
		        
        TransmissionPathType transmissionPathType = createTransmissionPathType(sysAdminId);
		
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
		Identifier mcmId = createMCM(sysAdminId, domainId, mcmUserId, serverId, (short)7500);

		Identifier equipmentId = createEquipment(sysAdminId,
																						 domainId,
																						 equipmentType);

		Identifier portId1 = createPort(sysAdminId, portType, equipmentId);
		Identifier portId2 = createPort(sysAdminId, portType, equipmentId);

		Identifier tpId = createTransmissionPath(sysAdminId, domainId, portId1, portId2, transmissionPathType);


		Identifier kisId = createKIS(sysAdminId, domainId, equipmentId, mcmId, "rtu-1", (short)7501);

		Identifier mportId = createMeasurementPort(sysAdminId, mPortType, kisId, portId1);

		MonitoredElement monitoredElement = createMonitoredElement(sysAdminId, domainId, mportId, tpId);


		createParameterTypes(sysAdminId);

		createTests(sysAdminId, monitoredElement);


		DatabaseConnection.closeConnection();
	}

	private static Identifier createSystemAdministrator() {
		try {
			Identifier userId = IdentifierGenerator.generateIdentifier(ObjectEntities.USER_ENTITY_CODE);
			User user = User.createInstance(userId,
					"sys",
					UserSort.USER_SORT_SYSADMIN,
					"sys",
					"System Administrator");
																			
			user.insert();
			return user.getId();
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static EquipmentType createEquipmentType(Identifier creatorId) {
		try {
			EquipmentType eqType = EquipmentType.createInstance(creatorId,
					"EqTypeKIS",
					"",
					"");
			eqType.insert();
			return eqType;
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}
    
    private static TransmissionPathType createTransmissionPathType(Identifier creatorId) {
        try {
            TransmissionPathType transmissionPathType = TransmissionPathType.createInstance(creatorId,
					"Type of TransmissionPath",
                    "",
					"");            
            transmissionPathType.insert();
            return transmissionPathType;
        }
        catch (Exception e) {
            Log.errorException(e);
            return null;
        }
    }
    
    private static PortType createPortType(Identifier creatorId) {
		try {
			PortType portType = PortType.createInstance(creatorId,
					"PortTypeReflectometry",
					"",
					"",
					PortTypeSort.PORTTYPESORT_ELECTRICAL);
			portType.insert();
			return portType;
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static MeasurementPortType createMeasurementPortType(Identifier creatorId) {
		try {
			MeasurementPortType mportType = MeasurementPortType.createInstance(creatorId,
					"MeasurementPortTypeReflectometry",
					"",
					"");
			mportType.insert();
			return mportType;
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static Identifier createDomain(Identifier creatorId) {
		try {
			Domain domain = Domain.createInstance(creatorId,
					null,
					"domain 1",
					"System domain");
			domain.insert();
			return domain.getId();
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
			Server server = Server.createInstance(creatorId,
				domainId,
				"server 1",
				"Measurement server",
				serverUserId);
			server.insert();
			return server.getId();
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static Identifier createMCM(Identifier creatorId,
										Identifier domainId,
										Identifier mcmUserId,
										Identifier serverId,
										short tcpPort) {
		try {
			MCM mcm = MCM.createInstance(creatorId,
				domainId,
				"mcm 1",
				"Measurement control module",
				mcmUserId,
				serverId,
				tcpPort);
			mcm.insert();
			return mcm.getId();
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static Identifier createKIS(Identifier creatorId,
																			Identifier domainId,
																			Identifier equipmentId,
																			Identifier mcmId,
																			String hostName,
																			short tcpPort) {
		try {
			KIS kis = KIS.createInstance(creatorId,
					domainId,
					"KIS",
					"kis ",
					hostName,
					tcpPort,
					equipmentId,
					mcmId);
			kis.insert();
			return kis.getId();
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
			Equipment eq = Equipment.createInstance(creatorId,
					domainId,
					eqType,
					"Equipment",
					"equipment",
					new Identifier("Image_1"),
					"default supplier",
					0.0,
					0.0);
			eq.insert();			
			return eq.getId();
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
			Port port = Port.createInstance(creatorId,
					type,
					"Port",
					equipmentId,
					PortSort.PORT_SORT_PORT);
			port.insert(); 
			return port.getId();
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static Identifier createTransmissionPath(Identifier creatorId,
													 Identifier domainId,
													 Identifier startPortId,
													 Identifier finishPortId,
													 TransmissionPathType type) {
		try {
			TransmissionPath tp = TransmissionPath.createInstance(creatorId,
					domainId,
					"TransmissionPath",
					"TransmissionPath",
					type,
					startPortId,
					finishPortId);
			tp.insert();
			return tp.getId();
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
			MeasurementPort mport = MeasurementPort.createInstance(creatorId,
					type,
					"MeasurementPort",
					"MeasurementPortTest",
					kisId,
					portId);
			mport.insert(); 
			return mport.getId();
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
			MonitoredElement monitoredElement = MonitoredElement.createInstance(creatorId,
					domainId,
					"ME",
					mPortId,
					MonitoredElementSort._MONITOREDELEMENT_SORT_TRANSMISSION_PATH,
					"ME",
					mdmIds);
			monitoredElement.insert(); 
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
			User user = User.createInstance(creatorId,
					login,
					sort,
					name,
					description);
																			
			user.insert();
			return user.getId();
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
		AnalysisTypeDatabase analysisTypeDatabase = ((AnalysisTypeDatabase)MeasurementDatabaseContext.getAnalysisTypeDatabase());
		EvaluationTypeDatabase evaluationTypeDatabase = ((EvaluationTypeDatabase)MeasurementDatabaseContext.getEvaluationTypeDatabase());
		MeasurementTypeDatabase measurementTypeDatabase = ((MeasurementTypeDatabase)MeasurementDatabaseContext.getMeasurementTypeDatabase());

		try {
			List parameterTypes = ((ParameterTypeDatabase)(MeasurementDatabaseContext.getParameterTypeDatabase())).retrieveAll();
			ParameterType pt;
			for (Iterator i = parameterTypes.iterator(); i.hasNext();) {
				pt = (ParameterType)i.next();
				System.out.println("id: " + pt.getId() + ", codename: " + pt.getCodename() + ", name: " + pt.getName());
			}

			List measurementTypes = measurementTypeDatabase.retrieveAll();
			MeasurementType mt;
			for (Iterator i = measurementTypes.iterator(); i.hasNext();) {
				mt = (MeasurementType)i.next();
				System.out.println("id: " + mt.getId() + ", codename: " + mt.getCodename() + ", description: " + mt.getDescription());
				List inp = mt.getInParameterTypes();
				for (Iterator j = inp.iterator(); j.hasNext();) {
					System.out.println("	in par id: " + ((ParameterType)j.next()).getId());
				}
			}

			List analysisTypes = analysisTypeDatabase.retrieveAll();
			AnalysisType at;
			for (Iterator i = analysisTypes.iterator(); i.hasNext();) {
				at = (AnalysisType)i.next();
				System.out.println("id: " + at.getId() + ", codename: " + at.getCodename() + ", description: " + at.getDescription());
				List inp = at.getInParameterTypes();
				for (Iterator j = inp.iterator(); j.hasNext();) {
					System.out.println("	in par id: " + ((ParameterType)j.next()).getId());
				}
			}

			List evaluationTypes = evaluationTypeDatabase.retrieveAll();
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
			ParameterType parameterType = ParameterType.createInstance(creatorId,
						codename,
						description,
						name);
			parameterType.insert();
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
			MeasurementType measurementType = MeasurementType.createInstance(creatorId,
					codename,
					description,
					inParameterTypes,
					outParameterTypes,
					new ArrayList());
			measurementType.insert();
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
			AnalysisType analysisType = AnalysisType.createInstance(creatorId,
					codename,
					description,
					inParameterTypes,
					criParameterTypes,
					etaParameterTypes,
					outParameterTypes);
			analysisType.insert();
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
			EvaluationType evaluationType = EvaluationType.createInstance(creatorId,
					codename,
					description,
					inParameterTypes,
					thrParameterTypes,
					etaParameterTypes,
					outParameterTypes);
			evaluationType.insert();
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
		ParameterTypeDatabase parameterTypeDatabase = ((ParameterTypeDatabase)MeasurementDatabaseContext.getParameterTypeDatabase());
		
		try {
			ParameterType wvlenParam = parameterTypeDatabase.retrieveForCodename(CODENAME_REF_WVLEN);
			ParameterType trclenParam = parameterTypeDatabase.retrieveForCodename(CODENAME_REF_TRCLEN);
			ParameterType resParam = parameterTypeDatabase.retrieveForCodename(CODENAME_REF_RES);
			ParameterType pulswdParam = parameterTypeDatabase.retrieveForCodename(CODENAME_REF_PULSWD);
			ParameterType iorParam = parameterTypeDatabase.retrieveForCodename(CODENAME_REF_IOR);
			ParameterType scansParam = parameterTypeDatabase.retrieveForCodename(CODENAME_REF_SCANS);

			SetParameter[] params = new SetParameter[6];
			params[0] = SetParameter.createInstance(wvlenParam, new ByteArray((int) 1625).getBytes());

			params[1] = SetParameter.createInstance(trclenParam, new ByteArray((double) 131072).getBytes());

			params[2] = SetParameter.createInstance(resParam, new ByteArray((double) 8).getBytes());

			params[3] = SetParameter.createInstance(pulswdParam, new ByteArray((long) 5000).getBytes());

			params[4] = SetParameter.createInstance(iorParam, new ByteArray((double) 1.457).getBytes());

			params[5] = SetParameter.createInstance(scansParam, new ByteArray((double) 4000).getBytes());

			Set set = Set.createInstance(creatorId,
					SetSort.SET_SORT_MEASUREMENT_PARAMETERS,
					"Set of measurement parameters",
					params,
					monitoredElementIds);
			set.insert();
			return set;
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static Set createCriteriaSet(Identifier creatorId, List monitoredElementIds) {
		ParameterTypeDatabase parameterTypeDatabase = ((ParameterTypeDatabase)MeasurementDatabaseContext.getParameterTypeDatabase());
		try {
			ParameterType tacticParam = parameterTypeDatabase.retrieveForCodename(CODENAME_DADARA_TACTIC);
			ParameterType eventsizeParam = parameterTypeDatabase.retrieveForCodename(CODENAME_DADARA_EVENT_SIZE);
			ParameterType connfallParam = parameterTypeDatabase.retrieveForCodename(CODENAME_DADARA_CONN_FALL_PARAMS);
			ParameterType minlevelParam = parameterTypeDatabase.retrieveForCodename(CODENAME_DADARA_MIN_LEVEL);
			ParameterType maxlevelnoiseParam = parameterTypeDatabase.retrieveForCodename(CODENAME_DADARA_MAX_LEVEL_NOISE);
			ParameterType minlevelendParam = parameterTypeDatabase.retrieveForCodename(CODENAME_DADARA_MIN_LEVEL_TO_FIND_END);
			ParameterType minweldParam = parameterTypeDatabase.retrieveForCodename(CODENAME_DADARA_MIN_WELD);
			ParameterType minconnectorParam = parameterTypeDatabase.retrieveForCodename(CODENAME_DADARA_MIN_CONNECTOR);
			ParameterType strategyParam = parameterTypeDatabase.retrieveForCodename(CODENAME_DADARA_STRATEGY);

			SetParameter[] params = new SetParameter[9];
			params[0] = SetParameter.createInstance(tacticParam, new ByteArray((int) 1).getBytes());

			params[1] = SetParameter.createInstance(eventsizeParam, new ByteArray((int) 2).getBytes());

			params[2] = SetParameter.createInstance(connfallParam, new ByteArray((double) 5.0).getBytes());

			params[3] = SetParameter.createInstance(minlevelParam, new ByteArray((double) 0.5).getBytes());

			params[4] = SetParameter.createInstance(maxlevelnoiseParam, new ByteArray((double) 1.0).getBytes());

			params[5] = SetParameter.createInstance(minlevelendParam, new ByteArray((double) 3.0).getBytes());

			params[6] = SetParameter.createInstance(minweldParam, new ByteArray((double) 0.2).getBytes());

			params[7] = SetParameter.createInstance(minconnectorParam, new ByteArray((double) 0.2).getBytes());

			params[8] = SetParameter.createInstance(strategyParam, new ByteArray((int) 1).getBytes());

			Set set = Set.createInstance(creatorId,
					SetSort.SET_SORT_MEASUREMENT_PARAMETERS,
					"Set of criteria",
					params,
					monitoredElementIds);
			set.insert();
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
			MeasurementSetup mSetup = MeasurementSetup.createInstance(creatorId,
					parameterSet,
					criteriaSet,
					thresholdSet,
					etalon,
					"created by MeasurementSetupTestCase",
					1000 * 60 * 10,
					monitoredElementIds);
			mSetup.insert();
			return mSetup;
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static TemporalPattern createTemporalPattern(Identifier creatorId) {
		try {
			String[] strings = new String[1];
			strings[0] = "*/10 * * * *";
			TemporalPattern temporalPattern = TemporalPattern.createInstance(creatorId,
				"TemporalPattern",
				strings);
			TemporalPattern temporalPattern1 = new TemporalPattern((TemporalPattern_Transferable)temporalPattern.getTransferable());
			return temporalPattern1;
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static Test createOnetimeTest(Identifier creatorId,
																				MonitoredElement monitoredElement,
																				List measurementSetupIds) {
		AnalysisTypeDatabase analysisTypeDatabase = ((AnalysisTypeDatabase)MeasurementDatabaseContext.getAnalysisTypeDatabase());
		EvaluationTypeDatabase evaluationTypeDatabase = ((EvaluationTypeDatabase)MeasurementDatabaseContext.getEvaluationTypeDatabase());
		MeasurementTypeDatabase measurementTypeDatabase = ((MeasurementTypeDatabase)MeasurementDatabaseContext.getMeasurementTypeDatabase());
		try {
			MeasurementType measurementType = measurementTypeDatabase.retrieveForCodename(CODENAME_MEASUREMENT_TYPE_REFLECTOMETRY);
			AnalysisType analysisType = analysisTypeDatabase.retrieveForCodename(CODENAME_ANALYSIS_TYPE_DADARA);
			EvaluationType evaluationType = evaluationTypeDatabase.retrieveForCodename(CODENAME_EVALUATION_TYPE_DADARA);

			Test test = Test.createInstance(creatorId,
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
			test.insert();
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
		AnalysisTypeDatabase analysisTypeDatabase = ((AnalysisTypeDatabase)MeasurementDatabaseContext.getAnalysisTypeDatabase());
		EvaluationTypeDatabase evaluationTypeDatabase = ((EvaluationTypeDatabase)MeasurementDatabaseContext.getEvaluationTypeDatabase());
		MeasurementTypeDatabase measurementTypeDatabase = ((MeasurementTypeDatabase)MeasurementDatabaseContext.getMeasurementTypeDatabase());
		try {
			MeasurementType measurementType = measurementTypeDatabase.retrieveForCodename(CODENAME_MEASUREMENT_TYPE_REFLECTOMETRY);
			AnalysisType analysisType = analysisTypeDatabase.retrieveForCodename(CODENAME_ANALYSIS_TYPE_DADARA);
			EvaluationType evaluationType = evaluationTypeDatabase.retrieveForCodename(CODENAME_EVALUATION_TYPE_DADARA);

			Test test = Test.createInstance(creatorId,
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
			test.insert();
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
