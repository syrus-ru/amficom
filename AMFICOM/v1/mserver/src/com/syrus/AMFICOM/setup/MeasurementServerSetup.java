/*
 * $Id: MeasurementServerSetup.java,v 1.32 2005/04/01 21:47:42 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.setup;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import com.syrus.AMFICOM.administration.AdministrationDatabaseContext;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.MCM;
import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.administration.corba.UserSort;
import com.syrus.AMFICOM.configuration.ConfigurationDatabaseContext;
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.Port;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.configuration.TransmissionPathType;
import com.syrus.AMFICOM.configuration.corba.MonitoredElementSort;
import com.syrus.AMFICOM.configuration.corba.PortSort;
import com.syrus.AMFICOM.configuration.corba.PortTypeSort;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.GeneralDatabaseContext;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerator;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.ParameterTypeCodenames;
import com.syrus.AMFICOM.general.ParameterTypeDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.AnalysisTypeDatabase;
import com.syrus.AMFICOM.measurement.EvaluationType;
import com.syrus.AMFICOM.measurement.EvaluationTypeDatabase;
import com.syrus.AMFICOM.measurement.MeasurementDatabaseContext;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.MeasurementTypeDatabase;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.SetParameter;
import com.syrus.AMFICOM.measurement.TemporalPattern;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.corba.SetSort;
import com.syrus.AMFICOM.measurement.corba.TemporalPattern_Transferable;
import com.syrus.AMFICOM.measurement.corba.TestReturnType;
import com.syrus.AMFICOM.measurement.corba.TestTemporalType;
import com.syrus.AMFICOM.mserver.DatabaseContextSetup;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.ByteArray;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.32 $, $Date: 2005/04/01 21:47:42 $
 * @author $Author: arseniy $
 * @module mserver_v1
 */

public final class MeasurementServerSetup {
	public static final String DB_SID = "amficom";
	public static final int DB_CONNECTION_TIMEOUT = 120;
	public static final String DB_LOGIN_NAME = "amficom";

	/**
	 * @todo Use ParameterTypeCodenames
	 */

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
				UserSort.USER_SORT_SERVERPROCESS,
				"User Serverovich",
				"User for measurement server");
		Identifier serverId = createServer(sysAdminId, domainId, "mongol", serverUserId);

		Identifier mcmUserId = createUser(sysAdminId,
				"mcm",
				UserSort.USER_SORT_MCM,
				"User Mcmovich",
				"User for measurement control module");
		Identifier mcmId = createMCM(sysAdminId, domainId, "aldan", mcmUserId, serverId);

		Identifier equipmentId = createEquipment(sysAdminId, domainId, equipmentType);

		Identifier portId1 = createPort(sysAdminId, portType, equipmentId);
		Identifier portId2 = createPort(sysAdminId, portType, equipmentId);

		Identifier tpId = createTransmissionPath(sysAdminId, domainId, portId1, portId2, transmissionPathType);

		Identifier kisId = createKIS(sysAdminId, domainId, equipmentId, mcmId, "rtu-1", (short) 7501);

		Identifier mportId = createMeasurementPort(sysAdminId, mPortType, kisId, portId1);

		MonitoredElement monitoredElement = createMonitoredElement(sysAdminId, domainId, mportId, tpId);

		createParameterTypes(sysAdminId);

		createTests(sysAdminId, monitoredElement);

		DatabaseConnection.closeConnection();
	}

	private static Identifier createSystemAdministrator() {
		try {
			Identifier userId = IdentifierGenerator.generateIdentifier(ObjectEntities.USER_ENTITY_CODE);
			User user = User.createInstance(userId, "sys", UserSort.USER_SORT_SYSADMIN, "sys", "System Administrator");
			AdministrationDatabaseContext.getUserDatabase().insert(user);
			return user.getId();
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static EquipmentType createEquipmentType(Identifier creatorId) {
		try {
			EquipmentType eqType = EquipmentType.createInstance(creatorId, "EqTypeKIS", "", "", "", "");
			ConfigurationDatabaseContext.getEquipmentTypeDatabase().insert(eqType);
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
			ConfigurationDatabaseContext.getTransmissionPathTypeDatabase().insert(transmissionPathType);
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
			ConfigurationDatabaseContext.getPortTypeDatabase().insert(portType);
			return portType;
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static MeasurementPortType createMeasurementPortType(Identifier creatorId) {
		try {
			MeasurementPortType mportType = MeasurementPortType.createInstance(creatorId, "MeasurementPortTypeReflectometry", "", "");
			ConfigurationDatabaseContext.getMeasurementPortTypeDatabase().insert(mportType);
			return mportType;
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static Identifier createDomain(Identifier creatorId) {
		try {
			Domain domain = Domain.createInstance(creatorId, null, "domain 1", "System domain");
			AdministrationDatabaseContext.getDomainDatabase().insert(domain);
			return domain.getId();
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static Identifier createServer(Identifier creatorId, Identifier domainId, String hostname, Identifier serverUserId) {
		try {
			Server server = Server.createInstance(creatorId, domainId, "server 1", "Measurement server", hostname, serverUserId);
			AdministrationDatabaseContext.getServerDatabase().insert(server);
			return server.getId();
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static Identifier createMCM(Identifier creatorId,
			Identifier domainId,
			String hostname,
			Identifier mcmUserId,
			Identifier serverId) {
		try {
			MCM mcm = MCM.createInstance(creatorId, domainId, "mcm 1", "Measurement control module", hostname, mcmUserId, serverId);
			AdministrationDatabaseContext.getMCMDatabase().insert(mcm);
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
			KIS kis = KIS.createInstance(creatorId, domainId, "KIS", "kis ", hostName, tcpPort, equipmentId, mcmId);
			ConfigurationDatabaseContext.getKISDatabase().insert(kis);
			return kis.getId();
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static Identifier createEquipment(Identifier creatorId, Identifier domainId, EquipmentType eqType) {
		try {
			Equipment eq = Equipment.createInstance(creatorId,
					domainId,
					eqType,
					"Equipment",
					"equipment",
					new Identifier("Image_1"),
					"default supplier",
					"default supplierCode",
					0.0f,
					0.0f,
					"",
					"",
					"",
					"",
					"");
			ConfigurationDatabaseContext.getEquipmentDatabase().insert(eq);
			return eq.getId();
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static Identifier createPort(Identifier creatorId, PortType type, Identifier equipmentId) {
		try {
			Port port = Port.createInstance(creatorId, type, "Port", equipmentId, PortSort.PORT_SORT_PORT);
			ConfigurationDatabaseContext.getPortDatabase().insert(port);
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
			ConfigurationDatabaseContext.getTransmissionPathDatabase().insert(tp);
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
			ConfigurationDatabaseContext.getMeasurementPortDatabase().insert(mport);
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
			java.util.Set mdmIds = new HashSet(1);
			mdmIds.add(transmissionPathId);
			MonitoredElement monitoredElement = MonitoredElement.createInstance(creatorId,
					domainId,
					"ME",
					mPortId,
					MonitoredElementSort.MONITOREDELEMENT_SORT_TRANSMISSION_PATH,
					"ME",
					mdmIds);
			ConfigurationDatabaseContext.getMonitoredElementDatabase().insert(monitoredElement);
			return monitoredElement;
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static Identifier createUser(Identifier creatorId, String login, UserSort sort, String name, String description) {
		try {
			User user = User.createInstance(creatorId, login, sort, name, description);
			AdministrationDatabaseContext.getUserDatabase().insert(user);
			return user.getId();
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static void createParameterTypes(Identifier creatorId) {
		java.util.Set inParTyps;
		java.util.Set criParTyps;
		java.util.Set etaParTyps;
		java.util.Set thrParTyps;
		java.util.Set outParTyps;

		inParTyps = new HashSet(6);
		inParTyps.add(createParameterType(creatorId,
				ParameterTypeCodenames.TRACE_WAVELENGTH,
				"For reflectometry",
				"Wavelength",
				DataType.DATA_TYPE_INTEGER));
		inParTyps.add(createParameterType(creatorId,
				ParameterTypeCodenames.TRACE_LENGTH,
				"For reflectometry",
				"Trace length",
				DataType.DATA_TYPE_LONG));
		inParTyps.add(createParameterType(creatorId, ParameterTypeCodenames.TRACE_RESOLUTION, "For reflectometry", "Resolution", DataType.DATA_TYPE_DOUBLE));
		inParTyps.add(createParameterType(creatorId, ParameterTypeCodenames.TRACE_PULSE_WIDTH, "For reflectometry", "Pulse width", DataType.DATA_TYPE_LONG));
		inParTyps.add(createParameterType(creatorId,
				ParameterTypeCodenames.TRACE_INDEX_OF_REFRACTION,
				"For reflectometry",
				"Index of refraction",
				DataType.DATA_TYPE_DOUBLE));
		inParTyps.add(createParameterType(creatorId,
				ParameterTypeCodenames.TRACE_AVERAGE_COUNT,
				"For reflectometry",
				"Number of averages",
				DataType.DATA_TYPE_LONG));
		outParTyps = new HashSet(1);
		ParameterType parTypRefl = createParameterType(creatorId,
				ParameterTypeCodenames.REFLECTOGRAMMA,
				"For reflectometry",
				"Reflectogramma",
				DataType.DATA_TYPE_RAW);
		outParTyps.add(parTypRefl);
		Identifier measurementTypeId = createMeasurementType(creatorId, MeasurementType.CODENAME_REFLECTOMETRY, "Reflectometry", inParTyps, outParTyps);

		inParTyps = new HashSet(1);
		inParTyps.add(parTypRefl);
		criParTyps = new HashSet(10);
		criParTyps.add(createParameterType(creatorId,
				ParameterTypeCodenames.WAVELET_TYPE,
				"For DADARA analysis",
				"Tactic",
				DataType.DATA_TYPE_INTEGER));
		criParTyps.add(createParameterType(creatorId,
				ParameterTypeCodenames.EVENT_SIZE,
				"For DADARA analysis",
				"Event size",
				DataType.DATA_TYPE_DOUBLE));
		criParTyps.add(createParameterType(creatorId,
				ParameterTypeCodenames.CONNECTOR_FORM_FACTOR,
				"For DADARA analysis",
				"Connector fall",
				DataType.DATA_TYPE_DOUBLE));
		criParTyps.add(createParameterType(creatorId,
				ParameterTypeCodenames.MIN_EVENT_LEVEL,
				"For DADARA analysis",
				"Min level",
				DataType.DATA_TYPE_DOUBLE));
		criParTyps.add(createParameterType(creatorId,
				ParameterTypeCodenames.MAX_NOISE_LEVEL,
				"For DADARA analysis",
				"Max level noise",
				DataType.DATA_TYPE_DOUBLE));
		criParTyps.add(createParameterType(creatorId,
				ParameterTypeCodenames.MIN_END_LEVEL,
				"For DADARA analysis",
				"Min level to find end",
				DataType.DATA_TYPE_DOUBLE));
		criParTyps.add(createParameterType(creatorId,
				ParameterTypeCodenames.MIN_SPLICE,
				"For DADARA analysis",
				"Min weld",
				DataType.DATA_TYPE_DOUBLE));
		criParTyps.add(createParameterType(creatorId,
				ParameterTypeCodenames.MIN_CONNECTOR,
				"For DADARA analysis",
				"Min connector",
				DataType.DATA_TYPE_DOUBLE));
		criParTyps.add(createParameterType(creatorId,
				ParameterTypeCodenames.STRATEGY,
				"For DADARA analysis",
				"strategy",
				DataType.DATA_TYPE_INTEGER));
		etaParTyps = new HashSet(1);
		ParameterType parTypEtalonEventArr = createParameterType(creatorId,
				ParameterTypeCodenames.DADARA_ETALON_EVENTS,
				"For DADARA analysis",
				"Etalon event array",
				DataType.DATA_TYPE_RAW);
		etaParTyps.add(parTypEtalonEventArr);
		outParTyps = new HashSet(1);
		ParameterType parTypEventArr = createParameterType(creatorId,
				ParameterTypeCodenames.DADARA_EVENTS,
				"For DADARA analysis",
				"Event array",
				DataType.DATA_TYPE_RAW);
		outParTyps.add(parTypEventArr);
		createAnalysisType(creatorId,
				AnalysisType.CODENAME_DADARA,
				"DADARA",
				inParTyps,
				criParTyps,
				etaParTyps,
				outParTyps,
				Collections.singleton(measurementTypeId));

		inParTyps = new HashSet(1);
		inParTyps.add(parTypRefl);
		thrParTyps = new HashSet(1);
		thrParTyps.add(createParameterType(creatorId,
				ParameterTypeCodenames.DADARA_THRESHOLDS,
				"For DADARA analysis",
				"Thresholds",
				DataType.DATA_TYPE_RAW));
		etaParTyps = new HashSet(1);
		etaParTyps.add(parTypEtalonEventArr);
		outParTyps = new HashSet(1);
		outParTyps.add(createParameterType(creatorId,
				ParameterTypeCodenames.DADARA_ALARMS,
				"For DADARA analysis",
				"Alarms",
				DataType.DATA_TYPE_RAW));
		createEvaluationType(creatorId,
				EvaluationType.CODENAME_DADARA,
				"DADARA",
				inParTyps,
				thrParTyps,
				etaParTyps,
				outParTyps,
				Collections.singleton(measurementTypeId));
	}

	private static void checkParameterTypes() {
		ParameterTypeDatabase parameterTypeDatabase = GeneralDatabaseContext.getParameterTypeDatabase();
		AnalysisTypeDatabase analysisTypeDatabase = MeasurementDatabaseContext.getAnalysisTypeDatabase();
		EvaluationTypeDatabase evaluationTypeDatabase = MeasurementDatabaseContext.getEvaluationTypeDatabase();
		MeasurementTypeDatabase measurementTypeDatabase = MeasurementDatabaseContext.getMeasurementTypeDatabase();

		try {
			java.util.Set parameterTypes = parameterTypeDatabase.retrieveAll();
			ParameterType pt;
			for (Iterator i = parameterTypes.iterator(); i.hasNext();) {
				pt = (ParameterType) i.next();
				System.out.println("id: " + pt.getId() + ", codename: " + pt.getCodename() + ", name: " + pt.getName());
			}

			java.util.Set measurementTypes = measurementTypeDatabase.retrieveAll();
			MeasurementType mt;
			for (Iterator i = measurementTypes.iterator(); i.hasNext();) {
				mt = (MeasurementType) i.next();
				System.out.println("id: " + mt.getId() + ", codename: " + mt.getCodename() + ", description: " + mt.getDescription());
				java.util.Set inp = mt.getInParameterTypes();
				for (Iterator j = inp.iterator(); j.hasNext();) {
					System.out.println("	in par id: " + ((ParameterType) j.next()).getId());
				}
			}

			java.util.Set analysisTypes = analysisTypeDatabase.retrieveButIdsByCondition(null, new EquivalentCondition(ObjectEntities.ANALYSISTYPE_ENTITY_CODE));
			AnalysisType at;
			for (Iterator i = analysisTypes.iterator(); i.hasNext();) {
				at = (AnalysisType) i.next();
				System.out.println("id: " + at.getId() + ", codename: " + at.getCodename() + ", description: " + at.getDescription());
				java.util.Set inp = at.getInParameterTypes();
				for (Iterator j = inp.iterator(); j.hasNext();) {
					System.out.println("	in par id: " + ((ParameterType) j.next()).getId());
				}
			}

			java.util.Set evaluationTypes = evaluationTypeDatabase.retrieveAll();
			EvaluationType et;
			for (Iterator i = evaluationTypes.iterator(); i.hasNext();) {
				et = (EvaluationType) i.next();
				System.out.println("id: " + et.getId() + ", codename: " + et.getCodename() + ", description: " + et.getDescription());
				java.util.Set inp = et.getInParameterTypes();
				for (Iterator j = inp.iterator(); j.hasNext();) {
					System.out.println("	in par id: " + ((ParameterType) j.next()).getId());
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
			String name,
			DataType dataType) {
		try {
			ParameterType parameterType = ParameterType.createInstance(creatorId, codename, description, name, dataType);
			GeneralDatabaseContext.getParameterTypeDatabase().insert(parameterType);
			return parameterType;
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static Identifier createMeasurementType(Identifier creatorId,
			String codename,
			String description,
			java.util.Set inParameterTypes,
			java.util.Set outParameterTypes) {
		try {
			MeasurementType measurementType = MeasurementType.createInstance(creatorId,
					codename,
					description,
					inParameterTypes,
					outParameterTypes,
					new HashSet());
			MeasurementDatabaseContext.getMeasurementTypeDatabase().insert(measurementType);
			return measurementType.getId();
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static void createAnalysisType(Identifier creatorId,
			String codename,
			String description,
			java.util.Set inParameterTypes,
			java.util.Set criParameterTypes,
			java.util.Set etaParameterTypes,
			java.util.Set outParameterTypes,
			java.util.Set measurementTypeIds) {
		try {
			AnalysisType analysisType = AnalysisType.createInstance(creatorId,
					codename,
					description,
					inParameterTypes,
					criParameterTypes,
					etaParameterTypes,
					outParameterTypes,
					measurementTypeIds);
			MeasurementDatabaseContext.getAnalysisTypeDatabase().insert(analysisType);
		}
		catch (Exception e) {
			Log.errorException(e);
		}
	}

	private static void createEvaluationType(Identifier creatorId,
			String codename,
			String description,
			java.util.Set inParameterTypes,
			java.util.Set thrParameterTypes,
			java.util.Set etaParameterTypes,
			java.util.Set outParameterTypes,
			java.util.Set measurementTypeIds) {
		try {
			EvaluationType evaluationType = EvaluationType.createInstance(creatorId,
					codename,
					description,
					inParameterTypes,
					thrParameterTypes,
					etaParameterTypes,
					outParameterTypes,
					measurementTypeIds);
			MeasurementDatabaseContext.getEvaluationTypeDatabase().insert(evaluationType);
		}
		catch (Exception e) {
			Log.errorException(e);
		}
	}

	private static void createTests(Identifier creatorId, MonitoredElement monitoredElement) {
		java.util.Set meIds = new HashSet(1);
		meIds.add(monitoredElement.getId());
		Set parameterSet = createParameterSet(creatorId, meIds);
		Set criteriaSet = createCriteriaSet(creatorId, meIds);
		MeasurementSetup measurementSetup = createMeasurementSetup(creatorId, parameterSet, null, null, null, meIds);
		java.util.Set msIds = new HashSet(1);
		msIds.add(measurementSetup.getId());
		createOnetimeTest(creatorId, monitoredElement, msIds);

		TemporalPattern temporalPattern = createTemporalPattern(creatorId);

		createPeriodicalTest(creatorId, temporalPattern, monitoredElement, msIds);
	}

	private static Set createParameterSet(Identifier creatorId, java.util.Set monitoredElementIds) {
		ParameterTypeDatabase parameterTypeDatabase = GeneralDatabaseContext.getParameterTypeDatabase();

		try {
			TypicalCondition tc = new TypicalCondition(ParameterTypeCodenames.TRACE_WAVELENGTH, OperationSort.OPERATION_EQUALS, ObjectEntities.PARAMETERTYPE_ENTITY_CODE, StorableObjectWrapper.COLUMN_CODENAME);
			ParameterType wvlenParam = (ParameterType) parameterTypeDatabase.retrieveButIdsByCondition(null, tc).iterator().next();

			tc = new TypicalCondition(ParameterTypeCodenames.TRACE_LENGTH, OperationSort.OPERATION_EQUALS, ObjectEntities.PARAMETERTYPE_ENTITY_CODE, StorableObjectWrapper.COLUMN_CODENAME);
			ParameterType trclenParam = (ParameterType) parameterTypeDatabase.retrieveButIdsByCondition(null, tc).iterator().next();
			
			tc = new TypicalCondition(ParameterTypeCodenames.TRACE_RESOLUTION, OperationSort.OPERATION_EQUALS, ObjectEntities.PARAMETERTYPE_ENTITY_CODE, StorableObjectWrapper.COLUMN_CODENAME);
			ParameterType resParam = (ParameterType) parameterTypeDatabase.retrieveButIdsByCondition(null, tc).iterator().next();
			
			tc = new TypicalCondition(ParameterTypeCodenames.TRACE_PULSE_WIDTH, OperationSort.OPERATION_EQUALS, ObjectEntities.PARAMETERTYPE_ENTITY_CODE, StorableObjectWrapper.COLUMN_CODENAME);
			ParameterType pulswdParam = (ParameterType) parameterTypeDatabase.retrieveButIdsByCondition(null, tc).iterator().next();
			
			tc = new TypicalCondition(ParameterTypeCodenames.TRACE_INDEX_OF_REFRACTION, OperationSort.OPERATION_EQUALS, ObjectEntities.PARAMETERTYPE_ENTITY_CODE, StorableObjectWrapper.COLUMN_CODENAME);
			ParameterType iorParam = (ParameterType) parameterTypeDatabase.retrieveButIdsByCondition(null, tc).iterator().next();
			
			tc = new TypicalCondition(ParameterTypeCodenames.TRACE_AVERAGE_COUNT, OperationSort.OPERATION_EQUALS, ObjectEntities.PARAMETERTYPE_ENTITY_CODE, StorableObjectWrapper.COLUMN_CODENAME);
			ParameterType scansParam = (ParameterType) parameterTypeDatabase.retrieveButIdsByCondition(null, tc).iterator().next();

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
			MeasurementDatabaseContext.getSetDatabase().insert(set);
			return set;
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static Set createCriteriaSet(Identifier creatorId, java.util.Set monitoredElementIds) {
		ParameterTypeDatabase parameterTypeDatabase = GeneralDatabaseContext.getParameterTypeDatabase();
		try {
			TypicalCondition tc = new TypicalCondition(ParameterTypeCodenames.WAVELET_TYPE,
					OperationSort.OPERATION_EQUALS,
					ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
					StorableObjectWrapper.COLUMN_CODENAME);
			ParameterType tacticParam = (ParameterType) parameterTypeDatabase.retrieveButIdsByCondition(null, tc).iterator().next();

			tc = new TypicalCondition(ParameterTypeCodenames.EVENT_SIZE,
					OperationSort.OPERATION_EQUALS,
					ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
					StorableObjectWrapper.COLUMN_CODENAME);
			ParameterType eventsizeParam = (ParameterType) parameterTypeDatabase.retrieveButIdsByCondition(null, tc).iterator().next();

			tc = new TypicalCondition(ParameterTypeCodenames.CONNECTOR_FORM_FACTOR,
					OperationSort.OPERATION_EQUALS,
					ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
					StorableObjectWrapper.COLUMN_CODENAME);
			ParameterType connfallParam = (ParameterType) parameterTypeDatabase.retrieveButIdsByCondition(null, tc).iterator().next();

			tc = new TypicalCondition(ParameterTypeCodenames.MIN_EVENT_LEVEL,
					OperationSort.OPERATION_EQUALS,
					ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
					StorableObjectWrapper.COLUMN_CODENAME);
			ParameterType minlevelParam = (ParameterType) parameterTypeDatabase.retrieveButIdsByCondition(null, tc).iterator().next();

			tc = new TypicalCondition(ParameterTypeCodenames.MAX_NOISE_LEVEL,
					OperationSort.OPERATION_EQUALS,
					ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
					StorableObjectWrapper.COLUMN_CODENAME);
			ParameterType maxlevelnoiseParam = (ParameterType) parameterTypeDatabase.retrieveButIdsByCondition(null, tc).iterator().next();

			tc = new TypicalCondition(ParameterTypeCodenames.MIN_END_LEVEL,
					OperationSort.OPERATION_EQUALS,
					ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
					StorableObjectWrapper.COLUMN_CODENAME);
			ParameterType minlevelendParam = (ParameterType) parameterTypeDatabase.retrieveButIdsByCondition(null, tc).iterator().next();

			tc = new TypicalCondition(ParameterTypeCodenames.MIN_SPLICE,
					OperationSort.OPERATION_EQUALS,
					ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
					StorableObjectWrapper.COLUMN_CODENAME);
			ParameterType minweldParam = (ParameterType) parameterTypeDatabase.retrieveButIdsByCondition(null, tc).iterator().next();

			tc = new TypicalCondition(ParameterTypeCodenames.MIN_CONNECTOR,
					OperationSort.OPERATION_EQUALS,
					ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
					StorableObjectWrapper.COLUMN_CODENAME);
			ParameterType minconnectorParam = (ParameterType) parameterTypeDatabase.retrieveButIdsByCondition(null, tc).iterator().next();

			tc = new TypicalCondition(ParameterTypeCodenames.STRATEGY,
					OperationSort.OPERATION_EQUALS,
					ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
					StorableObjectWrapper.COLUMN_CODENAME);
			ParameterType strategyParam = (ParameterType) parameterTypeDatabase.retrieveButIdsByCondition(null, tc).iterator().next();

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
			MeasurementDatabaseContext.getSetDatabase().insert(set);
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
			java.util.Set monitoredElementIds) {
		try {
			MeasurementSetup mSetup = MeasurementSetup.createInstance(creatorId,
					parameterSet,
					criteriaSet,
					thresholdSet,
					etalon,
					"created by MeasurementSetupTestCase",
					1000 * 60 * 10,
					monitoredElementIds);
			MeasurementDatabaseContext.getMeasurementSetupDatabase().insert(mSetup);
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
			TemporalPattern temporalPattern = TemporalPattern.createInstance(creatorId, "TemporalPattern", strings);
			TemporalPattern temporalPattern1 = new TemporalPattern((TemporalPattern_Transferable) temporalPattern.getTransferable());
			return temporalPattern1;
		}
		catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static Test createOnetimeTest(Identifier creatorId, MonitoredElement monitoredElement, java.util.Set measurementSetupIds) {
		AnalysisTypeDatabase analysisTypeDatabase = MeasurementDatabaseContext.getAnalysisTypeDatabase();
		EvaluationTypeDatabase evaluationTypeDatabase = MeasurementDatabaseContext.getEvaluationTypeDatabase();
		MeasurementTypeDatabase measurementTypeDatabase = MeasurementDatabaseContext.getMeasurementTypeDatabase();
		try {
			TypicalCondition tc = new TypicalCondition(MeasurementType.CODENAME_REFLECTOMETRY, OperationSort.OPERATION_EQUALS, ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE, StorableObjectWrapper.COLUMN_CODENAME);
			MeasurementType measurementType = (MeasurementType) measurementTypeDatabase.retrieveButIdsByCondition(null, tc).iterator().next();

			tc = new TypicalCondition(AnalysisType.CODENAME_DADARA, OperationSort.OPERATION_EQUALS, ObjectEntities.ANALYSISTYPE_ENTITY_CODE, StorableObjectWrapper.COLUMN_CODENAME);
			AnalysisType analysisType = (AnalysisType) analysisTypeDatabase.retrieveButIdsByCondition(null, tc).iterator().next();

			tc = new TypicalCondition(EvaluationType.CODENAME_DADARA, OperationSort.OPERATION_EQUALS, ObjectEntities.EVALUATIONTYPE_ENTITY_CODE, StorableObjectWrapper.COLUMN_CODENAME);
			EvaluationType evaluationType = (EvaluationType) evaluationTypeDatabase.retrieveButIdsByCondition(null, tc).iterator().next();

			Test test = Test.createInstance(creatorId,
					new Date(System.currentTimeMillis()),
					null,
					null,
					TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME,
					measurementType.getId(),
					analysisType.getId(),
					evaluationType.getId(),
					monitoredElement,
					TestReturnType.TEST_RETURN_TYPE_WHOLE,
					"Onetime test",
					measurementSetupIds);
			MeasurementDatabaseContext.getTestDatabase().insert(test);
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
			java.util.Set measurementSetupIds) {
		AnalysisTypeDatabase analysisTypeDatabase = MeasurementDatabaseContext.getAnalysisTypeDatabase();
		EvaluationTypeDatabase evaluationTypeDatabase = MeasurementDatabaseContext.getEvaluationTypeDatabase();
		MeasurementTypeDatabase measurementTypeDatabase = MeasurementDatabaseContext.getMeasurementTypeDatabase();
		try {
			TypicalCondition tc = new TypicalCondition(MeasurementType.CODENAME_REFLECTOMETRY, OperationSort.OPERATION_EQUALS, ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE, StorableObjectWrapper.COLUMN_CODENAME);
			MeasurementType measurementType = (MeasurementType) measurementTypeDatabase.retrieveButIdsByCondition(null, tc).iterator().next();

			tc = new TypicalCondition(AnalysisType.CODENAME_DADARA, OperationSort.OPERATION_EQUALS, ObjectEntities.ANALYSISTYPE_ENTITY_CODE, StorableObjectWrapper.COLUMN_CODENAME);
			AnalysisType analysisType = (AnalysisType) analysisTypeDatabase.retrieveButIdsByCondition(null, tc).iterator().next();

			tc = new TypicalCondition(EvaluationType.CODENAME_DADARA, OperationSort.OPERATION_EQUALS, ObjectEntities.EVALUATIONTYPE_ENTITY_CODE, StorableObjectWrapper.COLUMN_CODENAME);
			EvaluationType evaluationType = (EvaluationType) evaluationTypeDatabase.retrieveButIdsByCondition(null, tc).iterator().next();

			Test test = Test.createInstance(creatorId,
					new Date(System.currentTimeMillis()),
					new Date(System.currentTimeMillis() + 1000 * 60 * 30),
					temporalPattern.getId(),
					TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL,
					measurementType.getId(),
					analysisType.getId(),
					evaluationType.getId(),
					monitoredElement,
					TestReturnType.TEST_RETURN_TYPE_WHOLE,
					"Onetime test",
					measurementSetupIds);
			MeasurementDatabaseContext.getTestDatabase().insert(test);
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
		long dbConnTimeout = ApplicationProperties.getInt("DBConnectionTimeout", DB_CONNECTION_TIMEOUT) * 1000;
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
