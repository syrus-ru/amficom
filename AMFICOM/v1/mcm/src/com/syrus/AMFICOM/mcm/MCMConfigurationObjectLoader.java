/*
 * $Id: MCMConfigurationObjectLoader.java,v 1.2 2004/08/22 19:10:57 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.configuration.ConfigurationObjectLoader;
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
import com.syrus.util.Log;

/**
 * @version $Revision: 1.2 $, $Date: 2004/08/22 19:10:57 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

public final class MCMConfigurationObjectLoader implements ConfigurationObjectLoader {

	public MCMConfigurationObjectLoader() {
	}

	public CharacteristicType loadCharacteristicType(Identifier id) throws RetrieveObjectException, CommunicationException {
		CharacteristicType characteristicType = null;
		try {
			characteristicType = new CharacteristicType(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("Characteristic type '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
			try {
				characteristicType = new CharacteristicType(MeasurementControlModule.mServerRef.transmitCharacteristicType((Identifier_Transferable)id.getTransferable()));
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.activateMServerReference();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					Log.errorMessage("Characteristic type '" + id + "' not found on server database");
				else
					Log.errorMessage("Cannot retrieve characteristic type '" + id + "' from server database -- " + are.message);
			}
			catch (CreateObjectException coe) {
				Log.errorException(coe);
			}
		}
		return characteristicType;
	}

	public EquipmentType loadEquipmentType(Identifier id) throws RetrieveObjectException, CommunicationException {
		EquipmentType equipmentType = null;
		try {
			equipmentType = new EquipmentType(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("Equipment type '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
			try {
				equipmentType = new EquipmentType(MeasurementControlModule.mServerRef.transmitEquipmentType((Identifier_Transferable)id.getTransferable()));
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.activateMServerReference();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					Log.errorMessage("Equipment type '" + id + "' not found on server database");
				else
					Log.errorMessage("Cannot retrieve equipment type '" + id + "' from server database -- " + are.message);
			}
			catch (CreateObjectException coe) {
				Log.errorException(coe);
			}
		}
		return equipmentType;
	}

	public PortType loadPortType(Identifier id) throws RetrieveObjectException, CommunicationException {
		PortType portType = null;
		try {
			portType = new PortType(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("Port type '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
			try {
				portType = new PortType(MeasurementControlModule.mServerRef.transmitPortType((Identifier_Transferable)id.getTransferable()));
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.activateMServerReference();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					Log.errorMessage("Port type '" + id + "' not found on server database");
				else
					Log.errorMessage("Cannot retrieve port type '" + id + "' from server database -- " + are.message);
			}
			catch (CreateObjectException coe) {
				Log.errorException(coe);
			}
		}
		return portType;
	}

	public MeasurementPortType loadMeasurementPortType(Identifier id) throws RetrieveObjectException, CommunicationException {
		MeasurementPortType measurementPortType = null;
		try {
			measurementPortType = new MeasurementPortType(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("Measurement port type '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
			try {
				measurementPortType = new MeasurementPortType(MeasurementControlModule.mServerRef.transmitMeasurementPortType((Identifier_Transferable)id.getTransferable()));
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.activateMServerReference();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					Log.errorMessage("Measurement port type '" + id + "' not found on server database");
				else
					Log.errorMessage("Cannot retrieve measurement port type '" + id + "' from server database -- " + are.message);
			}
			catch (CreateObjectException coe) {
				Log.errorException(coe);
			}
		}
		return measurementPortType;
	}

	public Characteristic loadCharacteristic(Identifier id) throws RetrieveObjectException, CommunicationException {
		Characteristic characteristic = null;
		try {
			characteristic = new Characteristic(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("Characteristic '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
			try {
				characteristic = new Characteristic(MeasurementControlModule.mServerRef.transmitCharacteristic((Identifier_Transferable)id.getTransferable()));
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.activateMServerReference();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					Log.errorMessage("Characteristic '" + id + "' not found on server database");
				else
					Log.errorMessage("Cannot retrieve characteristic '" + id + "' from server database -- " + are.message);
			}
			catch (CreateObjectException coe) {
				Log.errorException(coe);
			}
		}
		return characteristic;
	}

//	public PermissionAttributes loadPermissionAttributes(Identifier id) throws DatabaseException {
//		return new PermissionAttributes(id);
//	}

	public User loadUser(Identifier id) throws RetrieveObjectException, CommunicationException {
		User user = null;
		try {
			user = new User(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("User '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
			try {
				user = new User(MeasurementControlModule.mServerRef.transmitUser((Identifier_Transferable)id.getTransferable()));
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.activateMServerReference();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					Log.errorMessage("User '" + id + "' not found on server database");
				else
					Log.errorMessage("Cannot retrieve user '" + id + "' from server database -- " + are.message);
			}
			catch (CreateObjectException coe) {
				Log.errorException(coe);
			}
		}
		return user;
	}

	public Domain loadDomain(Identifier id) throws RetrieveObjectException, CommunicationException {
		Domain domain = null;
		try {
			domain = new Domain(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("Domain '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
			try {
				domain = new Domain(MeasurementControlModule.mServerRef.transmitDomain((Identifier_Transferable)id.getTransferable()));
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.activateMServerReference();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					Log.errorMessage("Domain '" + id + "' not found on server database");
				else
					Log.errorMessage("Cannot retrieve domain '" + id + "' from server database -- " + are.message);
			}
			catch (CreateObjectException coe) {
				Log.errorException(coe);
			}
		}
		return domain;
	}

	public Server loadServer(Identifier id) throws RetrieveObjectException, CommunicationException {
		Server server = null;
		try {
			server = new Server(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("Server '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
			try {
				server = new Server(MeasurementControlModule.mServerRef.transmitServer((Identifier_Transferable)id.getTransferable()));
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.activateMServerReference();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					Log.errorMessage("Server '" + id + "' not found on server database");
				else
					Log.errorMessage("Cannot retrieve Server '" + id + "' from server database -- " + are.message);
			}
			catch (CreateObjectException coe) {
				Log.errorException(coe);
			}
		}
		return server;
	}

	public MCM loadMCM(Identifier id) throws RetrieveObjectException, CommunicationException {
		MCM mcm = null;
		try {
			mcm = new MCM(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("MCM '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
			try {
				mcm = new MCM(MeasurementControlModule.mServerRef.transmitMCM((Identifier_Transferable)id.getTransferable()));
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.activateMServerReference();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					Log.errorMessage("MCM '" + id + "' not found on server database");
				else
					Log.errorMessage("Cannot retrieve MCM '" + id + "' from server database -- " + are.message);
			}
			catch (CreateObjectException coe) {
				Log.errorException(coe);
			}
		}
		return mcm;
	}

	public Equipment loadEquipment(Identifier id) throws RetrieveObjectException, CommunicationException {
		Equipment equipment = null;
		try {
			equipment = new Equipment(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("Equipment '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
			try {
				equipment = new Equipment(MeasurementControlModule.mServerRef.transmitEquipment((Identifier_Transferable)id.getTransferable()));
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.activateMServerReference();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					Log.errorMessage("Equipment '" + id + "' not found on server database");
				else
					Log.errorMessage("Cannot retrieve equipment '" + id + "' from server database -- " + are.message);
			}
			catch (CreateObjectException coe) {
				Log.errorException(coe);
			}
		}
		return equipment;
	}

	public Port loadPort(Identifier id) throws RetrieveObjectException, CommunicationException {
		Port port = null;
		try {
			port = new Port(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("Port '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
			try {
				port = new Port(MeasurementControlModule.mServerRef.transmitPort((Identifier_Transferable)id.getTransferable()));
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.activateMServerReference();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					Log.errorMessage("Port '" + id + "' not found on server database");
				else
					Log.errorMessage("Cannot retrieve port '" + id + "' from server database -- " + are.message);
			}
			catch (CreateObjectException coe) {
				Log.errorException(coe);
			}
		}
		return port;
	}

	public TransmissionPath loadTransmissionPath(Identifier id) throws RetrieveObjectException, CommunicationException {
		TransmissionPath transmissionPath = null;
		try {
			transmissionPath = new TransmissionPath(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("Transmission path '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
			try {
				transmissionPath = new TransmissionPath(MeasurementControlModule.mServerRef.transmitTransmissionPath((Identifier_Transferable)id.getTransferable()));
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.activateMServerReference();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					Log.errorMessage("Transmission path '" + id + "' not found on server database");
				else
					Log.errorMessage("Cannot retrieve transmission path '" + id + "' from server database -- " + are.message);
			}
			catch (CreateObjectException coe) {
				Log.errorException(coe);
			}
		}
		return transmissionPath;
	}

	public KIS loadKIS(Identifier id) throws RetrieveObjectException, CommunicationException {
		KIS kis = null;
		try {
			kis = new KIS(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("KIS '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
			try {
				kis = new KIS(MeasurementControlModule.mServerRef.transmitKIS((Identifier_Transferable)id.getTransferable()));
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.activateMServerReference();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					Log.errorMessage("KIS '" + id + "' not found on server database");
				else
					Log.errorMessage("Cannot retrieve KIS '" + id + "' from server database -- " + are.message);
			}
			catch (CreateObjectException coe) {
				Log.errorException(coe);
			}
		}
		return kis;
	}

	public MeasurementPort loadMeasurementPort(Identifier id) throws RetrieveObjectException, CommunicationException {
		MeasurementPort measurementPort = null;
		try {
			measurementPort = new MeasurementPort(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("Measurement port '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
			try {
				measurementPort = new MeasurementPort(MeasurementControlModule.mServerRef.transmitMeasurementPort((Identifier_Transferable)id.getTransferable()));
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.activateMServerReference();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					Log.errorMessage("Measurement port '" + id + "' not found on server database");
				else
					Log.errorMessage("Cannot retrieve measurement port '" + id + "' from server database -- " + are.message);
			}
			catch (CreateObjectException coe) {
				Log.errorException(coe);
			}
		}
		return measurementPort;
	}

	public MonitoredElement loadMonitoredElement(Identifier id) throws RetrieveObjectException, CommunicationException {
		MonitoredElement monitoredElement = null;
		try {
			monitoredElement = new MonitoredElement(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("Monitored element '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
			try {
				monitoredElement = new MonitoredElement(MeasurementControlModule.mServerRef.transmitMonitoredElement((Identifier_Transferable)id.getTransferable()));
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.activateMServerReference();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					Log.errorMessage("Monitored element '" + id + "' not found on server database");
				else
					Log.errorMessage("Cannot retrieve monitored element '" + id + "' from server database -- " + are.message);
			}
			catch (CreateObjectException coe) {
				Log.errorException(coe);
			}
		}
		return monitoredElement;
	}
}
