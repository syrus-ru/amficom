package com.syrus.AMFICOM.setup;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.IdentifierGenerator;
import com.syrus.AMFICOM.configuration.User;
import com.syrus.AMFICOM.configuration.Domain;
import com.syrus.AMFICOM.configuration.Server;
import com.syrus.AMFICOM.configuration.MCM;
import com.syrus.AMFICOM.configuration.corba.User_Transferable;
import com.syrus.AMFICOM.configuration.corba.UserSort;
import com.syrus.AMFICOM.configuration.corba.Domain_Transferable;
import com.syrus.AMFICOM.configuration.corba.Server_Transferable;
import com.syrus.AMFICOM.configuration.corba.MCM_Transferable;
import com.syrus.AMFICOM.measurement.ParameterType;
import com.syrus.AMFICOM.measurement.ParameterTypeDatabase;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.MeasurementTypeDatabase;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.AnalysisTypeDatabase;
import com.syrus.AMFICOM.measurement.EvaluationType;
import com.syrus.AMFICOM.measurement.EvaluationTypeDatabase;
import com.syrus.AMFICOM.measurement.corba.ParameterType_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementType_Transferable;
import com.syrus.AMFICOM.measurement.corba.AnalysisType_Transferable;
import com.syrus.AMFICOM.measurement.corba.EvaluationType_Transferable;
import com.syrus.AMFICOM.mserver.DatabaseContextSetup;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

public class MeasurementServerSetup {
	public static final String DB_SID = "amficom";
	public static final int DB_CONNECTION_TIMEOUT = 120;

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

		Identifier creatorId = new Identifier("Users_9");
//		createUser(creatorId,
//							 "mserver",
//							 UserSort.USER_SORT_SERVER,
//							 "User Serverovich",
//							 "User for measurement server");
//		createUser(creatorId,
//							 "mcm",
//							 UserSort.USER_SORT_MCM,
//							 "User Mcmovich",
//							 "User for measurement control module");

//		createDomain(creatorId);
//		checkDomain();

		Identifier serverUserId = new Identifier("Users_10");
		Identifier mcmUserId = new Identifier("Users_11");
		Identifier domainId = new Identifier("Domain_26");
//		createServer(creatorId,
//								 domainId,
//								 serverUserId);
//		checkServer();

		Identifier serverId = new Identifier("Server_3");
		createMCM(creatorId, domainId, mcmUserId, serverId);

//		createParameterTypes(creatorId);
//		checkParameterTypes();

		DatabaseConnection.closeConnection();
	}

	private static void createUser(Identifier creatorId,
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
		}
		catch (Exception e) {
			Log.errorException(e);
		}
	}

	private static void checkUser() {
		try {
			Identifier id = new Identifier("Users_9");
			User user = new User(id);
			System.out.println("login: " + user.getLogin() + ", name: " + user.getName() + ", description: " + user.getDescription());
		}
		catch (Exception e) {
			Log.errorException(e);
		}
	}

	private static void createDomain(Identifier creatorId) {
		try {
			Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.DOMAIN_ENTITY_CODE);
			Domain domain = Domain.createInstance(id,
																						creatorId,
																						null,
																						"domain 1",
																						"System domain");
			Domain domain1 = new Domain((Domain_Transferable)domain.getTransferable());
		}
		catch (Exception e) {
			Log.errorException(e);
		}
	}

	private static void checkDomain() {
		try {
			Identifier id = new Identifier("Domain_26");
			Domain domain = new Domain(id);
			System.out.println("creator: " + domain.getCreatorId() + ", domain_id: " + domain.getDomainId() + ", name: " + domain.getName() + ", description: " + domain.getDescription());
		}
		catch (Exception e) {
			Log.errorException(e);
		}
	}

	private static void createServer(Identifier creatorId,
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
		}
		catch (Exception e) {
			Log.errorException(e);
		}
	}

	private static void checkServer() {
		try {
			Identifier id = new Identifier("Server_3");
			Server server = new Server(id);
			System.out.println("creator: " + server.getCreatorId() + ", domain_id: " + server.getDomainId() + ", name: " + server.getName() + ", description: " + server.getDescription() + ", user: " + server.getUserId());
		}
		catch (Exception e) {
			Log.errorException(e);
		}
	}

	private static void createMCM(Identifier creatorId,
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
		}
		catch (Exception e) {
			Log.errorException(e);
		}
	}

	private static void createParameterTypes(Identifier creatorId) {
		List inParTypIds;
		List criParTypIds;
		List etaParTypIds;
		List thrParTypIds;
		List outParTypIds;

		inParTypIds = new ArrayList(6);
		inParTypIds.add(createParameterType(creatorId, CODENAME_REF_WVLEN, "For reflectometry", "Wavelength"));
		inParTypIds.add(createParameterType(creatorId, CODENAME_REF_TRCLEN, "For reflectometry", "Trace length"));
		inParTypIds.add(createParameterType(creatorId, CODENAME_REF_RES, "For reflectometry", "Resolution"));
		inParTypIds.add(createParameterType(creatorId, CODENAME_REF_PULSWD, "For reflectometry", "Pulse width"));
		inParTypIds.add(createParameterType(creatorId, CODENAME_REF_IOR, "For reflectometry", "Index of refraction"));
		inParTypIds.add(createParameterType(creatorId, CODENAME_REF_SCANS, "For reflectometry", "Number of averages"));
		outParTypIds = new ArrayList(1);
		Identifier parTypRefl = createParameterType(creatorId, CODENAME_REFLECTOGRAMMA, "For reflectometry", "Reflectogramma");
		outParTypIds.add(parTypRefl);
		createMeasurementType(creatorId, CODENAME_MEASUREMENT_TYPE_REFLECTOMETRY, "Reflectometry", inParTypIds, outParTypIds);

		inParTypIds = new ArrayList(1);
		inParTypIds.add(parTypRefl);
		criParTypIds = new ArrayList(10);
		criParTypIds.add(createParameterType(creatorId, CODENAME_DADARA_TACTIC, "For DADARA analysis", "Tactic"));
		criParTypIds.add(createParameterType(creatorId, CODENAME_DADARA_EVENT_SIZE, "For DADARA analysis", "Event size"));
		criParTypIds.add(createParameterType(creatorId, CODENAME_DADARA_CONN_FALL_PARAMS, "For DADARA analysis", "Connector fall"));
		criParTypIds.add(createParameterType(creatorId, CODENAME_DADARA_MIN_LEVEL, "For DADARA analysis", "Min level"));
		criParTypIds.add(createParameterType(creatorId, CODENAME_DADARA_MAX_LEVEL_NOISE, "For DADARA analysis", "Max level noise"));
		criParTypIds.add(createParameterType(creatorId, CODENAME_DADARA_MIN_LEVEL_TO_FIND_END, "For DADARA analysis", "Min level to find end"));
		criParTypIds.add(createParameterType(creatorId, CODENAME_DADARA_MIN_WELD, "For DADARA analysis", "Min weld"));
		criParTypIds.add(createParameterType(creatorId, CODENAME_DADARA_MIN_CONNECTOR, "For DADARA analysis", "Min connector"));
		criParTypIds.add(createParameterType(creatorId, CODENAME_DADARA_STRATEGY, "For DADARA analysis", "strategy"));
		etaParTypIds = new ArrayList(1);
		Identifier parTypEtalonEventArr = createParameterType(creatorId, CODENAME_DADARA_ETALON_EVENT_ARRAY, "For DADARA analysis", "Etalon event array");
		etaParTypIds.add(parTypEtalonEventArr);
		outParTypIds = new ArrayList(1);
		Identifier parTypEventArr = createParameterType(creatorId, CODENAME_DADARA_EVENT_ARRAY, "For DADARA analysis", "Event array");
		outParTypIds.add(parTypEventArr);
		createAnalysisType(creatorId, CODENAME_ANALYSIS_TYPE_DADARA, "DADARA", inParTypIds, criParTypIds, etaParTypIds, outParTypIds);

		inParTypIds = new ArrayList(1);
		inParTypIds.add(parTypRefl);
		thrParTypIds = new ArrayList(1);
		thrParTypIds.add(createParameterType(creatorId, CODENAME_DADARA_THRESHOLDS, "For DADARA analysis", "Thresholds"));
		etaParTypIds = new ArrayList(1);
		etaParTypIds.add(parTypEtalonEventArr);
		outParTypIds = new ArrayList(1);
		outParTypIds.add(createParameterType(creatorId, CODENAME_DADARA_ALARM_ARRAY, "For DADARA analysis", "Alarms"));
		createEvaluationType(creatorId, CODENAME_EVALUATION_TYPE_DADARA, "DADARA", inParTypIds, thrParTypIds, etaParTypIds, outParTypIds);
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
				List inp = mt.getInParameterTypeIds();
				for (Iterator j = inp.iterator(); j.hasNext();) {
					System.out.println("	in par id: " + (Identifier)j.next());
				}
			}

			List analysisTypes = AnalysisTypeDatabase.retrieveAll();
			AnalysisType at;
			for (Iterator i = analysisTypes.iterator(); i.hasNext();) {
				at = (AnalysisType)i.next();
				System.out.println("id: " + at.getId() + ", codename: " + at.getCodename() + ", description: " + at.getDescription());
				List inp = at.getInParameterTypeIds();
				for (Iterator j = inp.iterator(); j.hasNext();) {
					System.out.println("	in par id: " + (Identifier)j.next());
				}
			}

			List evaluationTypes = EvaluationTypeDatabase.retrieveAll();
			EvaluationType et;
			for (Iterator i = evaluationTypes.iterator(); i.hasNext();) {
				et = (EvaluationType)i.next();
				System.out.println("id: " + et.getId() + ", codename: " + et.getCodename() + ", description: " + et.getDescription());
				List inp = et.getInParameterTypeIds();
				for (Iterator j = inp.iterator(); j.hasNext();) {
					System.out.println("	in par id: " + (Identifier)j.next());
				}
			}
		}
		catch (Exception e) {
			Log.errorException(e);
		}
	}

	private static Identifier createParameterType(Identifier creatorId,
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
			return id;
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static void createMeasurementType(Identifier creatorId,
																						String codename,
																						String description,
																						List inParameterTypeIds,
																						List outParameterTypeIds) {
		try {
			Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE);
			MeasurementType measurementType = MeasurementType.createInstance(id,
																																			 creatorId,
																																			 codename,
																																			 description,
																																			 inParameterTypeIds,
																																			 outParameterTypeIds);
			new MeasurementType((MeasurementType_Transferable)measurementType.getTransferable());
		}
		catch (Exception e) {
			Log.errorException(e);
		}
	}

	private static void createAnalysisType(Identifier creatorId,
																				 String codename,
																				 String description,
																				 List inParameterTypeIds,
																				 List criParameterTypeIds,
																				 List	etaParameterTypeIds,
																				 List outParameterTypeIds) {
		try {
			Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.ANALYSISTYPE_ENTITY_CODE);
			AnalysisType analysisType = AnalysisType.createInstance(id,
																															creatorId,
																															codename,
																															description,
																															inParameterTypeIds,
																															criParameterTypeIds,
																															etaParameterTypeIds,
																															outParameterTypeIds);
			new AnalysisType((AnalysisType_Transferable)analysisType.getTransferable());
		}
		catch (Exception e) {
			Log.errorException(e);
		}
	}

	private static void createEvaluationType(Identifier creatorId,
																					 String codename,
																					 String description,
																					 List inParameterTypeIds,
																					 List thrParameterTypeIds,
																					 List	etaParameterTypeIds,
																					 List outParameterTypeIds) {
		try {
			Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.EVALUATIONTYPE_ENTITY_CODE);
			EvaluationType evaluationType = EvaluationType.createInstance(id,
																																		creatorId,
																																		codename,
																																		description,
																																		inParameterTypeIds,
																																		thrParameterTypeIds,
																																		etaParameterTypeIds,
																																		outParameterTypeIds);
			new EvaluationType((EvaluationType_Transferable)evaluationType.getTransferable());
		}
		catch (Exception e) {
			Log.errorException(e);
		}
	}

	private static void establishDatabaseConnection() {
		String dbHostName = ApplicationProperties.getString("DBHostName", Application.getInternetAddress());
		String dbSid = ApplicationProperties.getString("DBSID", DB_SID);
		long dbConnTimeout = ApplicationProperties.getInt("DBConnectionTimeout", DB_CONNECTION_TIMEOUT)*1000;
		try {
			DatabaseConnection.establishConnection(dbHostName, dbSid, dbConnTimeout);
		}
		catch (Exception e) {
			Log.errorException(e);
			System.exit(-1);
		}
	}
}
