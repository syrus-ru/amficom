/*-
 * $Id: InitialSetup.java,v 1.1 2005/05/26 14:32:22 bob Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.Collections;
import java.util.HashSet;

import junit.framework.Test;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.MCM;
import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.administration.corba.UserSort;
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
import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.AMFICOM.general.corba.ParameterType_Transferable;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.EvaluationType;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2005/05/26 14:32:22 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module test
 */
public class InitialSetup extends CommonTest {

	public InitialSetup(String name) {
		super(name);
	}

	public static Test suite() {
		return suiteWrapper(InitialSetup.class);
	}

	public void testCreateSystemAdministrator() throws ApplicationException {
		User user = User.createInstance(null, "sys", UserSort.USER_SORT_SYSADMIN, "sys", "System Administrator");
		StorableObjectPool.putStorableObject(user);
		StorableObjectPool.flush(user.getId(), true);
	}

	public void testCreateEquipmentType() throws ApplicationException {
		EquipmentType eqType = EquipmentType.createInstance(creatorUser.getId(), "EqTypeKIS", "", "", "", "");
		StorableObjectPool.putStorableObject(eqType);
		StorableObjectPool.flush(eqType.getId(), true);
	}

	private static EquipmentType createEquipmentType(Identifier creatorId) throws ApplicationException {
		EquipmentType eqType = EquipmentType.createInstance(creatorId, "EqTypeKIS", "", "", "", "");
		StorableObjectPool.putStorableObject(eqType);
		StorableObjectPool.flush(eqType.getId(), true);
		return eqType;

	}

	private static TransmissionPathType createTransmissionPathType(Identifier creatorId) throws ApplicationException {
		TransmissionPathType transmissionPathType = TransmissionPathType.createInstance(creatorId,
			"Type of TransmissionPath", "", "");
		StorableObjectPool.putStorableObject(transmissionPathType);
		StorableObjectPool.flush(transmissionPathType.getId(), true);
		return transmissionPathType;
	}

	private static PortType createPortType(Identifier creatorId) throws ApplicationException {
		PortType portType = PortType.createInstance(creatorId, "PortTypeReflectometry", "", "",
			PortTypeSort.PORTTYPESORT_ELECTRICAL);
		StorableObjectPool.putStorableObject(portType);
		StorableObjectPool.flush(portType.getId(), true);
		return portType;

	}

	private static MeasurementPortType createMeasurementPortType(Identifier creatorId) throws ApplicationException {
		MeasurementPortType mportType = MeasurementPortType.createInstance(creatorId,
			"MeasurementPortTypeReflectometry", "", "");
		StorableObjectPool.putStorableObject(mportType);
		StorableObjectPool.flush(mportType.getId(), true);
		return mportType;

	}

	private static Identifier createDomain(Identifier creatorId) throws ApplicationException {
		Domain domain = Domain.createInstance(creatorId, null, "domain 1", "System domain");
		StorableObjectPool.putStorableObject(domain);
		StorableObjectPool.flush(domain.getId(), true);
		return domain.getId();
	}

	private static Identifier createServer(	Identifier creatorId,
											Identifier domainId,
											String hostname) throws ApplicationException {
		Server server = Server.createInstance(creatorId, domainId, "server 1", "Measurement server", hostname);
		StorableObjectPool.putStorableObject(server);
		StorableObjectPool.flush(server.getId(), true);
		return server.getId();

	}

	private static Identifier createMCM(Identifier creatorId,
										Identifier domainId,
										String hostname,
										Identifier mcmUserId,
										Identifier serverId) throws ApplicationException {
		MCM mcm = MCM.createInstance(creatorId, domainId, "mcm 1", "Measurement control module", hostname, mcmUserId,
			serverId);
		StorableObjectPool.putStorableObject(mcm);
		StorableObjectPool.flush(mcm.getId(), true);
		return mcm.getId();
	}

	private static Identifier createKIS(Identifier creatorId,
										Identifier domainId,
										Identifier equipmentId,
										Identifier mcmId,
										String hostName,
										short tcpPort) throws ApplicationException {
		KIS kis = KIS.createInstance(creatorId, domainId, "KIS", "kis ", hostName, tcpPort, equipmentId, mcmId);
		StorableObjectPool.putStorableObject(kis);
		StorableObjectPool.flush(kis.getId(), true);
		return kis.getId();

	}

	private static Identifier createEquipment(	Identifier creatorId,
												Identifier domainId,
												EquipmentType eqType) {
		try {
			Equipment eq = Equipment.createInstance(creatorId, domainId, eqType, "Equipment", "equipment",
				new Identifier("Image_1"), "default supplier", "default supplierCode", 0.0f, 0.0f, "", "", "", "", "");
			StorableObjectPool.putStorableObject(eq);
			StorableObjectPool.flush(eq.getId(), true);
			return eq.getId();
		} catch (Exception e) {
			Log.errorException(e);
			return null;
		}
	}

	private static Identifier createPort(	Identifier creatorId,
											PortType type,
											Identifier equipmentId) throws ApplicationException {
		Port port = Port.createInstance(creatorId, type, "Port", equipmentId, PortSort.PORT_SORT_PORT);
		StorableObjectPool.putStorableObject(port);
		StorableObjectPool.flush(port.getId(), true);
		return port.getId();

	}

	private static Identifier createTransmissionPath(	Identifier creatorId,
														Identifier domainId,
														Identifier startPortId,
														Identifier finishPortId,
														TransmissionPathType type) throws ApplicationException {
		TransmissionPath tp = TransmissionPath.createInstance(creatorId, domainId, "TransmissionPath",
			"TransmissionPath", type, startPortId, finishPortId);
		StorableObjectPool.putStorableObject(tp);
		StorableObjectPool.flush(tp.getId(), true);
		return tp.getId();
	}

	private static Identifier createMeasurementPort(Identifier creatorId,
													MeasurementPortType type,
													Identifier kisId,
													Identifier portId) throws ApplicationException {
		MeasurementPort mport = MeasurementPort.createInstance(creatorId, type, "MeasurementPort",
			"MeasurementPortTest", kisId, portId);
		StorableObjectPool.putStorableObject(mport);
		StorableObjectPool.flush(mport.getId(), true);
		return mport.getId();

	}

	private static MonitoredElement createMonitoredElement(	Identifier creatorId,
															Identifier domainId,
															Identifier mPortId,
															Identifier transmissionPathId) throws ApplicationException {
		MonitoredElement monitoredElement = MonitoredElement.createInstance(creatorId, domainId, "ME", mPortId,
			MonitoredElementSort.MONITOREDELEMENT_SORT_TRANSMISSION_PATH, "ME", Collections
					.singleton(transmissionPathId));
		StorableObjectPool.putStorableObject(monitoredElement);
		StorableObjectPool.flush(monitoredElement.getId(), true);
		return monitoredElement;

	}

	private static Identifier createUser(	Identifier creatorId,
											String login,
											UserSort sort,
											String name,
											String description) throws ApplicationException {
		User user = User.createInstance(creatorId, login, sort, name, description);
		StorableObjectPool.putStorableObject(user);
		StorableObjectPool.flush(user.getId(), true);
		return user.getId();

	}

	public void testCreateParameterTypes(Identifier creatorId) throws ApplicationException {
		java.util.Set inParTyps;
		java.util.Set criParTyps;
		java.util.Set etaParTyps;
		java.util.Set thrParTyps;
		java.util.Set outParTyps;

		inParTyps = new HashSet(6);
		inParTyps.add(createParameterType(creatorId, ParameterTypeCodenames.TRACE_WAVELENGTH, "Длина волны",
			"Wavelength", DataType.DATA_TYPE_INTEGER));
		inParTyps.add(createParameterType(creatorId, ParameterTypeCodenames.TRACE_LENGTH, "Длина линии",
			"Trace length", DataType.DATA_TYPE_LONG));
		inParTyps.add(createParameterType(creatorId, ParameterTypeCodenames.TRACE_RESOLUTION, "Разрешение",
			"Resolution", DataType.DATA_TYPE_DOUBLE));
		inParTyps.add(createParameterType(creatorId, ParameterTypeCodenames.TRACE_PULSE_WIDTH_HIGH_RES, "Ширина импульса в режиме высокого разрешения",
			"Ширина импульса", DataType.DATA_TYPE_LONG));
		inParTyps.add(createParameterType(creatorId, ParameterTypeCodenames.TRACE_PULSE_WIDTH_LOW_RES, "Ширина импульса в режиме низкого разрешения",
			"Ширина импульса", DataType.DATA_TYPE_LONG));		
		inParTyps.add(createParameterType(creatorId, ParameterTypeCodenames.TRACE_INDEX_OF_REFRACTION,
			"Коэффициент преломления", "Index of refraction", DataType.DATA_TYPE_DOUBLE));
		inParTyps.add(createParameterType(creatorId, ParameterTypeCodenames.TRACE_AVERAGE_COUNT, "Количество усреднений",
			"Number of averages", DataType.DATA_TYPE_LONG));		
		inParTyps.add(createParameterType(creatorId, ParameterTypeCodenames.TRACE_FLAG_GAIN_SPLICE_ON, "Gain splice",
			"Gain splice flag", DataType.DATA_TYPE_BOOLEAN));		
		inParTyps.add(createParameterType(creatorId, ParameterTypeCodenames.TRACE_FLAG_LIVE_FIBER_DETECT, "Live fiber detect",
			"Live fiber detect flag", DataType.DATA_TYPE_BOOLEAN));

		
		createParameterType(creatorId, ParameterTypeCodenames.DADARA_MIN_TRACE_LEVEL,
			"Минимальный уровень рефлектограммы", "Minimal trace level", DataType.DATA_TYPE_DOUBLE);		

		outParTyps = Collections.singleton(createParameterType(creatorId, ParameterTypeCodenames.REFLECTOGRAMMA,
			"For reflectometry", "Reflectogramma", DataType.DATA_TYPE_RAW));
		Identifier measurementTypeId = createMeasurementType(creatorId, MeasurementType.CODENAME_REFLECTOMETRY,
			"Reflectometry", inParTyps, outParTyps);

		inParTyps = outParTyps;
		criParTyps = Collections.singleton(createParameterType(creatorId, ParameterTypeCodenames.DADARA_CRITERIA,
			"Dadara criteria parameter type", "Dadara criteria", DataType.DATA_TYPE_RAW));

		etaParTyps = new HashSet(1);
		outParTyps = new HashSet(1);
		createAnalysisType(creatorId, AnalysisType.CODENAME_DADARA, "DADARA", inParTyps, criParTyps, etaParTyps,
			outParTyps, Collections.singleton(measurementTypeId));

		thrParTyps = new HashSet(1);
		etaParTyps = new HashSet(1);
		outParTyps = new HashSet(1);
		outParTyps.add(createParameterType(creatorId, ParameterTypeCodenames.DADARA_ALARMS, "For DADARA analysis",
			"Alarms", DataType.DATA_TYPE_RAW));
		createEvaluationType(creatorId, EvaluationType.CODENAME_DADARA, "DADARA", inParTyps, thrParTyps, etaParTyps,
			outParTyps, Collections.singleton(measurementTypeId));
	}

	// private static void checkParameterTypes() {
	// ParameterTypeDatabase parameterTypeDatabase =
	// GeneralDatabaseContext.getParameterTypeDatabase();
	// AnalysisTypeDatabase analysisTypeDatabase =
	// MeasurementDatabaseContext.getAnalysisTypeDatabase();
	// EvaluationTypeDatabase evaluationTypeDatabase =
	// MeasurementDatabaseContext.getEvaluationTypeDatabase();
	// MeasurementTypeDatabase measurementTypeDatabase =
	// MeasurementDatabaseContext.getMeasurementTypeDatabase();
	//
	// try {
	// java.util.Set parameterTypes = parameterTypeDatabase.retrieveAll();
	// ParameterType pt;
	// for (Iterator i = parameterTypes.iterator(); i.hasNext();) {
	// pt = (ParameterType) i.next();
	// System.out.println("id: " + pt.getId() + ", codename: " +
	// pt.getCodename() + ", name: " + pt.getName());
	// }
	//
	// java.util.Set measurementTypes = measurementTypeDatabase.retrieveAll();
	// MeasurementType mt;
	// for (Iterator i = measurementTypes.iterator(); i.hasNext();) {
	// mt = (MeasurementType) i.next();
	// System.out.println("id: " + mt.getId() + ", codename: " +
	// mt.getCodename() + ", description: "
	// + mt.getDescription());
	// java.util.Set inp = mt.getInParameterTypes();
	// for (Iterator j = inp.iterator(); j.hasNext();) {
	// System.out.println(" in par id: " + ((ParameterType) j.next()).getId());
	// }
	// }
	//
	// java.util.Set analysisTypes =
	// analysisTypeDatabase.retrieveButIdsByCondition(null,
	// new EquivalentCondition(ObjectEntities.ANALYSISTYPE_ENTITY_CODE));
	// AnalysisType at;
	// for (Iterator i = analysisTypes.iterator(); i.hasNext();) {
	// at = (AnalysisType) i.next();
	// System.out.println("id: " + at.getId() + ", codename: " +
	// at.getCodename() + ", description: "
	// + at.getDescription());
	// java.util.Set inp = at.getInParameterType();
	// for (Iterator j = inp.iterator(); j.hasNext();) {
	// System.out.println(" in par id: " + ((ParameterType) j.next()).getId());
	// }
	// }
	//
	// java.util.Set evaluationTypes = evaluationTypeDatabase.retrieveAll();
	// EvaluationType et;
	// for (Iterator i = evaluationTypes.iterator(); i.hasNext();) {
	// et = (EvaluationType) i.next();
	// System.out.println("id: " + et.getId() + ", codename: " +
	// et.getCodename() + ", description: "
	// + et.getDescription());
	// java.util.Set inp = et.getInParameterTypes();
	// for (Iterator j = inp.iterator(); j.hasNext();) {
	// System.out.println(" in par id: " + ((ParameterType) j.next()).getId());
	// }
	// }
	// } catch (Exception e) {
	// Log.errorException(e);
	// }
	// }

	private static Identifier createParameterType(	Identifier creatorId,
													String codename,
													String description,
													String name,
													DataType dataType) throws ApplicationException {
		ParameterType parameterType = ParameterType.createInstance(creatorId, codename, description, name, dataType);
		Identifier id = parameterType.getId();
		StorableObjectPool.putStorableObject(parameterType);
		StorableObjectPool.flush(id, true);
		return id;
	}

	private static Identifier createMeasurementType(Identifier creatorId,
													String codename,
													String description,
													java.util.Set inParameterTypes,
													java.util.Set outParameterTypes) throws ApplicationException {
		MeasurementType measurementType = MeasurementType.createInstance(creatorId, codename, description,
			inParameterTypes, outParameterTypes, new HashSet());
		StorableObjectPool.putStorableObject(measurementType);
		StorableObjectPool.flush(measurementType.getId(), true);
		return measurementType.getId();
	}

	public void createAnalysisType(	Identifier creatorId,
									String codename,
									String description,
									java.util.Set inParameterTypes,
									java.util.Set criParameterTypes,
									java.util.Set etaParameterTypes,
									java.util.Set outParameterTypes,
									java.util.Set measurementTypeIds) throws ApplicationException {
		AnalysisType analysisType = AnalysisType.createInstance(creatorId, codename, description, inParameterTypes,
			criParameterTypes, etaParameterTypes, outParameterTypes, measurementTypeIds);
		StorableObjectPool.putStorableObject(analysisType);
		StorableObjectPool.flush(analysisType.getId(), true);
	}

	private void createEvaluationType(	Identifier creatorId,
										String codename,
										String description,
										java.util.Set inParameterTypes,
										java.util.Set thrParameterTypes,
										java.util.Set etaParameterTypes,
										java.util.Set outParameterTypes,
										java.util.Set measurementTypeIds) throws ApplicationException {
		EvaluationType evaluationType = EvaluationType.createInstance(creatorId, codename, description,
			inParameterTypes, thrParameterTypes, etaParameterTypes, outParameterTypes, measurementTypeIds);
		StorableObjectPool.putStorableObject(evaluationType);
		StorableObjectPool.flush(evaluationType.getId(), true);
	}

}
