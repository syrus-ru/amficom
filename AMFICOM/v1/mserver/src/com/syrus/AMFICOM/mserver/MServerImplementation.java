/*
 * $Id: MServerImplementation.java,v 1.10 2004/08/23 20:48:36 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mserver;

import java.util.List;
import java.util.Iterator;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.IdentifierGenerator;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;

import com.syrus.AMFICOM.configuration.CharacteristicType;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.configuration.Characteristic;
import com.syrus.AMFICOM.configuration.User;
import com.syrus.AMFICOM.configuration.Domain;
import com.syrus.AMFICOM.configuration.Server;
import com.syrus.AMFICOM.configuration.MCM;
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.Port;
import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.corba.CharacteristicType_Transferable;
import com.syrus.AMFICOM.configuration.corba.EquipmentType_Transferable;
import com.syrus.AMFICOM.configuration.corba.PortType_Transferable;
import com.syrus.AMFICOM.configuration.corba.MeasurementPortType_Transferable;
import com.syrus.AMFICOM.configuration.corba.Characteristic_Transferable;
import com.syrus.AMFICOM.configuration.corba.User_Transferable;
import com.syrus.AMFICOM.configuration.corba.Domain_Transferable;
import com.syrus.AMFICOM.configuration.corba.Server_Transferable;
import com.syrus.AMFICOM.configuration.corba.MCM_Transferable;
import com.syrus.AMFICOM.configuration.corba.Equipment_Transferable;
import com.syrus.AMFICOM.configuration.corba.Port_Transferable;
import com.syrus.AMFICOM.configuration.corba.TransmissionPath_Transferable;
import com.syrus.AMFICOM.configuration.corba.KIS_Transferable;
import com.syrus.AMFICOM.configuration.corba.MeasurementPort_Transferable;
import com.syrus.AMFICOM.configuration.corba.MonitoredElement_Transferable;

import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.ParameterType;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.EvaluationType;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.TemporalPattern;
import com.syrus.AMFICOM.measurement.corba.ParameterType_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementType_Transferable;
import com.syrus.AMFICOM.measurement.corba.AnalysisType_Transferable;
import com.syrus.AMFICOM.measurement.corba.EvaluationType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Set_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementSetup_Transferable;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.AMFICOM.measurement.corba.Result_Transferable;
import com.syrus.AMFICOM.measurement.corba.TemporalPattern_Transferable;
import com.syrus.AMFICOM.measurement.corba.TestStatus;
import com.syrus.AMFICOM.mserver.corba.MServerPOA;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.10 $, $Date: 2004/08/23 20:48:36 $
 * @author $Author: arseniy $
 * @module mserver_v1
 */

public class MServerImplementation extends MServerPOA {
	static final long serialVersionUID = 5984823427343263335L;

	public MServerImplementation() {
		
	}

/////////////////////////////////////////	Identifier Generator ////////////////////////////////////////////////
	public Identifier_Transferable getGeneratedIdentifier(short entityCode) throws AMFICOMRemoteException {
		try {
			Identifier identifier = IdentifierGenerator.generateIdentifier(entityCode);
			return (Identifier_Transferable)identifier.getTransferable();
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
																			 CompletionStatus.COMPLETED_NO,
																			 "Illegal object entity: '" + ObjectEntities.codeToString(entityCode) + "'");
		}
		catch (IdentifierGenerationException ige) {
			Log.errorException(ige);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE,
																			 CompletionStatus.COMPLETED_NO,
																			 "Cannot create major/minor entries of identifier for entity: '" + ObjectEntities.codeToString(entityCode) + "' -- " + ige.getMessage());
		}	
	}

	public Identifier_Transferable[] getGeneratedIdentifierRange(short entityCode, int size) throws AMFICOMRemoteException {
		try {
			Identifier[] identifiers = IdentifierGenerator.generateIdentifierRange(entityCode, size);
			Identifier_Transferable[] identifiersT = new Identifier_Transferable[identifiers.length];
			for (int i = 0; i < identifiersT.length; i++)
				identifiersT[i] = (Identifier_Transferable)identifiers[i].getTransferable();
			return identifiersT;
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
																			 CompletionStatus.COMPLETED_NO,
																			 "Illegal object entity: '" + ObjectEntities.codeToString(entityCode) + "'");
		}
		catch (IdentifierGenerationException ige) {
			Log.errorException(ige);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE,
																			 CompletionStatus.COMPLETED_NO,
																			 "Cannot create major/minor entries of identifier for entity: '" + ObjectEntities.codeToString(entityCode) + "' -- " + ige.getMessage());
		}
	}


////////////////////////////////////////////	Receive ///////////////////////////////////////////////////
	public void receiveResults(Result_Transferable[] resultsT, Identifier_Transferable mcmIdT) throws AMFICOMRemoteException {
		Identifier mcmId = new Identifier(mcmIdT);
		Log.debugMessage("Received " + resultsT.length + " results from MCM '" + mcmId + "'", Log.DEBUGLEVEL03);
		synchronized (MServerMeasurementObjectLoader.lock) {
			MServerMeasurementObjectLoader.mcmId = mcmId;
			Result result;
			for (int i = 0; i < resultsT.length; i++) {
				try {
					result = new Result(resultsT[i]);
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
					throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
				}
			}
		}
	}


/////////////////////////////////////////	Configuration ////////////////////////////////////////////////
	public CharacteristicType_Transferable transmitCharacteristicType(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			CharacteristicType characteristicType = new CharacteristicType(id);
			return (CharacteristicType_Transferable)characteristicType.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
	}

	public EquipmentType_Transferable transmitEquipmentType(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			EquipmentType equipmentType = new EquipmentType(id);
			return (EquipmentType_Transferable)equipmentType.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
	}

	public PortType_Transferable transmitPortType(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			PortType portType = new PortType(id);
			return (PortType_Transferable)portType.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
	}

	public MeasurementPortType_Transferable transmitMeasurementPortType(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			MeasurementPortType measurementPortType = new MeasurementPortType(id);
			return (MeasurementPortType_Transferable)measurementPortType.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (Exception e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException();
		}
	}

	public Characteristic_Transferable transmitCharacteristic(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			Characteristic characteristic = new Characteristic(id);
			return (Characteristic_Transferable)characteristic.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
	}

	public User_Transferable transmitUser(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			User user = new User(id);
			return (User_Transferable)user.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
	}

	public Domain_Transferable transmitDomain(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			Domain domain = new Domain(id);
			return (Domain_Transferable)domain.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
	}

	public Server_Transferable transmitServer(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			Server server = new Server(id);
			return (Server_Transferable)server.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
	}

	public MCM_Transferable transmitMCM(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			MCM mcm = new MCM(id);
			return (MCM_Transferable)mcm.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
	}

	public Equipment_Transferable transmitEquipment(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			Equipment equipment = new Equipment(id);
			return (Equipment_Transferable)equipment.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
	}

	public Port_Transferable transmitPort(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			Port port = new Port(id);
			return (Port_Transferable)port.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
	}

	public TransmissionPath_Transferable transmitTransmissionPath(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			TransmissionPath transmissionPath = new TransmissionPath(id);
			return (TransmissionPath_Transferable)transmissionPath.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
	}

	public KIS_Transferable transmitKIS(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			KIS kis = new KIS(id);
			return (KIS_Transferable)kis.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (Exception e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException();
		}
	}

	public MeasurementPort_Transferable transmitMeasurementPort(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			MeasurementPort measurementPort = new MeasurementPort(id);
			return (MeasurementPort_Transferable)measurementPort.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
	}

	public MonitoredElement_Transferable transmitMonitoredElement(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			MonitoredElement monitoredElement = new MonitoredElement(id);
			return (MonitoredElement_Transferable)monitoredElement.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
	}


	public MonitoredElement_Transferable[] transmitKISMonitoredElements(Identifier_Transferable kisId) throws AMFICOMRemoteException {
		Identifier id = new Identifier(kisId);
		try {
			KIS kis = new KIS(id);
			List monitoredElements = kis.retrieveMonitoredElements();
			MonitoredElement_Transferable[] mesT = new MonitoredElement_Transferable[monitoredElements.size()];
			int i = 0;
			for (Iterator it = monitoredElements.iterator(); it.hasNext();)
				mesT[i++] = (MonitoredElement_Transferable)((MonitoredElement)it.next()).getTransferable();
			return mesT;
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
	}



/////////////////////////////////////////	Measurement //////////////////////////////////////////////////
	public ParameterType_Transferable transmitParameterType(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			ParameterType parameterType = new ParameterType(id);
			return (ParameterType_Transferable)parameterType.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
	}

	public MeasurementType_Transferable transmitMeasurementType(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			MeasurementType measurementType = new MeasurementType(id);
			return (MeasurementType_Transferable)measurementType.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
	}

	public AnalysisType_Transferable transmitAnalysisType(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			AnalysisType analysisType = new AnalysisType(id);
			return (AnalysisType_Transferable)analysisType.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
	}

	public EvaluationType_Transferable transmitEvaluationType(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			EvaluationType evaluationType = new EvaluationType(id);
			return (EvaluationType_Transferable)evaluationType.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
	}

	public Set_Transferable transmitSet(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			Set set = new Set(id);
			return (Set_Transferable)set.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
	}

	public MeasurementSetup_Transferable transmitMeasurementSetup(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			MeasurementSetup measurementSetup = new MeasurementSetup(id);
			return (MeasurementSetup_Transferable)measurementSetup.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
	}

	public Test_Transferable transmitTest(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			Test test = new Test(id);
			return (Test_Transferable)test.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
	}

	public TemporalPattern_Transferable transmitTemporalPattern(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			TemporalPattern temporalPattern = new TemporalPattern(id);
			return (TemporalPattern_Transferable)temporalPattern.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
	}

	public void updateTestStatus(Identifier_Transferable idT, TestStatus status) {
		Identifier id = new Identifier(idT);
		Log.debugMessage("Updating status of test '" + id + "' to " + status.value(), Log.DEBUGLEVEL07);
		try {
			Test test = (Test)MeasurementStorableObjectPool.getStorableObject(id, true);
			test.updateStatus(status, MeasurementServer.iAm.getUserId());
		}
		catch (ApplicationException ae) {
			Log.errorMessage("updateTestStatus | Cannot update status of test '" + id + "' -- " + ae.getMessage());
		}
	}



	public void ping(int i) throws AMFICOMRemoteException {
		System.out.println("i == " + i);
	}
}
