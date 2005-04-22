/*
 * $Id: MCMConfigurationObjectLoader.java,v 1.31 2005/04/22 13:55:49 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.HashSet;
import java.util.Set;

import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.configuration.CableThread;
import com.syrus.AMFICOM.configuration.CableThreadType;
import com.syrus.AMFICOM.configuration.ConfigurationDatabaseContext;
import com.syrus.AMFICOM.configuration.DatabaseConfigurationObjectLoader;
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.configuration.KISDatabase;
import com.syrus.AMFICOM.configuration.Link;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.configuration.MeasurementPortDatabase;
import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.configuration.MeasurementPortTypeDatabase;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.MonitoredElementDatabase;
import com.syrus.AMFICOM.configuration.Port;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.configuration.TransmissionPathType;
import com.syrus.AMFICOM.configuration.corba.EquipmentType_Transferable;
import com.syrus.AMFICOM.configuration.corba.Equipment_Transferable;
import com.syrus.AMFICOM.configuration.corba.KIS_Transferable;
import com.syrus.AMFICOM.configuration.corba.LinkType_Transferable;
import com.syrus.AMFICOM.configuration.corba.Link_Transferable;
import com.syrus.AMFICOM.configuration.corba.MeasurementPortType_Transferable;
import com.syrus.AMFICOM.configuration.corba.MeasurementPort_Transferable;
import com.syrus.AMFICOM.configuration.corba.MonitoredElement_Transferable;
import com.syrus.AMFICOM.configuration.corba.PortType_Transferable;
import com.syrus.AMFICOM.configuration.corba.Port_Transferable;
import com.syrus.AMFICOM.configuration.corba.TransmissionPathType_Transferable;
import com.syrus.AMFICOM.configuration.corba.TransmissionPath_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectConditionBuilder;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.mserver.corba.MServer;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.31 $, $Date: 2005/04/22 13:55:49 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

final class MCMConfigurationObjectLoader extends DatabaseConfigurationObjectLoader {

	/* Load single object*/

	public EquipmentType loadEquipmentType(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new EquipmentType(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("MCMConfigurationObjectLoader.loadEquipmentType | EquipmentType '" + id
					+ "' not found in database; trying to load from Measurement Server", Log.DEBUGLEVEL08);
			EquipmentType equipmentType = null;

			MServer mServerRef = MeasurementControlModule.mServerConnectionManager.getVerifiedMServerReference();
			try {
				EquipmentType_Transferable transferable = mServerRef.transmitEquipmentType((Identifier_Transferable) id.getTransferable());
				equipmentType = new EquipmentType(transferable);
				Log.debugMessage("MCMConfigurationObjectLoader.loadEquipmentType | EquipmentType '" + id
						+ "' loaded from MeasurementServer", Log.DEBUGLEVEL08);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
					throw new ObjectNotFoundException("EquipmentType '" + id + "' not found on Measurement Server -- " + are.message);
				throw new RetrieveObjectException("Cannot retrieve EquipmentType '" + id + "' from Measurement Server -- " + are.message);
			}
			catch (Throwable throwable) {
				Log.errorException(throwable);
			}

			if (equipmentType != null) {
				try {
					ConfigurationDatabaseContext.getEquipmentTypeDatabase().insert(equipmentType);
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
				return equipmentType;
			}
			throw new ObjectNotFoundException("EquipmentType '" + id + "' not found on Measurement Server");
		}	//catch (ObjectNotFoundException e)
	}

	public PortType loadPortType(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new PortType(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("MCMConfigurationObjectLoader.loadPortType | PortType '" + id
					+ "' not found in database; trying to load from Measurement Server", Log.DEBUGLEVEL08);
			PortType portType = null;
	
			MServer mServerRef = MeasurementControlModule.mServerConnectionManager.getVerifiedMServerReference();
			try {
				PortType_Transferable transferable = mServerRef.transmitPortType((Identifier_Transferable) id.getTransferable());
				portType = new PortType(transferable);
				Log.debugMessage("MCMConfigurationObjectLoader.loadPortType | PortType '" + id
						+ "' loaded from MeasurementServer", Log.DEBUGLEVEL08);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
					throw new ObjectNotFoundException("PortType '" + id + "' not found on Measurement Server -- " + are.message);
				throw new RetrieveObjectException("Cannot retrieve PortType '" + id
						+ "' from Measurement Server -- " + are.message);
			}
			catch (Throwable throwable) {
				Log.errorException(throwable);
			}
	
			if (portType != null) {
				try {
					ConfigurationDatabaseContext.getPortTypeDatabase().insert(portType);
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
				return portType;
			}
			throw new ObjectNotFoundException("PortType '" + id + "' not found on Measurement Server");
		}	//catch (ObjectNotFoundException e)
	}

	public MeasurementPortType loadMeasurementPortType(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new MeasurementPortType(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("MCMConfigurationObjectLoader.loadMeasurementPortType | MeasurementPortType '" + id
					+ "' not found in database; trying to load from Measurement Server", Log.DEBUGLEVEL08);
			MeasurementPortType measurementPortType = null;
	
			MServer mServerRef = MeasurementControlModule.mServerConnectionManager.getVerifiedMServerReference();
			try {
				MeasurementPortType_Transferable transferable = mServerRef.transmitMeasurementPortType((Identifier_Transferable) id.getTransferable());
				measurementPortType = new MeasurementPortType(transferable);
				Log.debugMessage("MCMConfigurationObjectLoader.loadMeasurementPortType | MeasurementPortType '" + id
						+ "' loaded from MeasurementServer", Log.DEBUGLEVEL08);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
					throw new ObjectNotFoundException("MeasurementPortType '" + id + "' not found on Measurement Server -- " + are.message);
				throw new RetrieveObjectException("Cannot retrieve MeasurementPortType '" + id
						+ "' from Measurement Server -- " + are.message);
			}
			catch (Throwable throwable) {
				Log.errorException(throwable);
			}
	
			if (measurementPortType != null) {
				try {
					ConfigurationDatabaseContext.getMeasurementPortTypeDatabase().insert(measurementPortType);
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
				return measurementPortType;
			}
			throw new ObjectNotFoundException("MeasurementPortType '" + id + "' not found on Measurement Server");
		}	//catch (ObjectNotFoundException e)
	}

	public TransmissionPathType loadTransmissionPathType(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new TransmissionPathType(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("MCMConfigurationObjectLoader.loadTransmissionPathType | TransmissionPathType '" + id
					+ "' not found in database; trying to load from Measurement Server", Log.DEBUGLEVEL08);
			TransmissionPathType transmissionPathType = null;

			MServer mServerRef = MeasurementControlModule.mServerConnectionManager.getVerifiedMServerReference();
			try {
				TransmissionPathType_Transferable transferable = mServerRef.transmitTransmissionPathType((Identifier_Transferable) id.getTransferable());
				transmissionPathType = new TransmissionPathType(transferable);
				Log.debugMessage("MCMConfigurationObjectLoader.loadTransmissionPathType | TransmissionPathType '" + id
						+ "' loaded from MeasurementServer", Log.DEBUGLEVEL08);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
					throw new ObjectNotFoundException("TransmissionPathType '" + id + "' not found on Measurement Server -- " + are.message);
				throw new RetrieveObjectException("Cannot retrieve TransmissionPathType '" + id
						+ "' from Measurement Server -- " + are.message);
			}
			catch (Throwable throwable) {
				Log.errorException(throwable);
			}

			if (transmissionPathType != null) {
				try {
					ConfigurationDatabaseContext.getTransmissionPathTypeDatabase().insert(transmissionPathType);
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
				return transmissionPathType;
			}
			throw new ObjectNotFoundException("TransmissionPathType '" + id + "' not found on Measurement Server");
		}	//catch (ObjectNotFoundException e)
	}

	public LinkType loadLinkType(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new LinkType(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("MCMConfigurationObjectLoader.loadLinkType | LinkType '" + id
					+ "' not found in database; trying to load from Measurement Server", Log.DEBUGLEVEL08);
			LinkType linkType = null;

			MServer mServerRef = MeasurementControlModule.mServerConnectionManager.getVerifiedMServerReference();
			try {
				LinkType_Transferable transferable = mServerRef.transmitLinkType((Identifier_Transferable) id.getTransferable());
				linkType = new LinkType(transferable);
				Log.debugMessage("MCMConfigurationObjectLoader.loadLinkType | LinkType '" + id
						+ "' loaded from MeasurementServer", Log.DEBUGLEVEL08);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
					throw new ObjectNotFoundException("LinkType '" + id + "' not found on Measurement Server -- " + are.message);
				throw new RetrieveObjectException("Cannot retrieve LinkType '" + id
						+ "' from Measurement Server -- " + are.message);
			}
			catch (Throwable throwable) {
				Log.errorException(throwable);
			}

			if (linkType != null) {
				try {
					ConfigurationDatabaseContext.getLinkTypeDatabase().insert(linkType);
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
				return linkType;
			}
			throw new ObjectNotFoundException("LinkType '" + id + "' not found on Measurement Server");
		}	//catch (ObjectNotFoundException e)
	}



	public Equipment loadEquipment(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new Equipment(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("MCMConfigurationObjectLoader.loadEquipment | Equipment '" + id
					+ "' not found in database; trying to load from Measurement Server", Log.DEBUGLEVEL08);
			Equipment equipment = null;

			MServer mServerRef = MeasurementControlModule.mServerConnectionManager.getVerifiedMServerReference();
			try {
				Equipment_Transferable transferable = mServerRef.transmitEquipment((Identifier_Transferable) id.getTransferable());
				equipment = new Equipment(transferable);
				Log.debugMessage("MCMConfigurationObjectLoader.loadEquipment | Equipment '" + id
						+ "' loaded from MeasurementServer", Log.DEBUGLEVEL08);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
					throw new ObjectNotFoundException("Equipment '" + id + "' not found on Measurement Server -- " + are.message);
				throw new RetrieveObjectException("Cannot retrieve Equipment '" + id
						+ "' from Measurement Server -- " + are.message);
			}
			catch (Throwable throwable) {
				Log.errorException(throwable);
			}

			if (equipment != null) {
				try {
					ConfigurationDatabaseContext.getEquipmentDatabase().insert(equipment);
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
				return equipment;
			}
			throw new ObjectNotFoundException("Equipment '" + id + "' not found on Measurement Server");
		}	//catch (ObjectNotFoundException e)
	}

	public Port loadPort(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new Port(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("MCMConfigurationObjectLoader.loadPort | Port '" + id
					+ "' not found in database; trying to load from Measurement Server", Log.DEBUGLEVEL08);
			Port port = null;

			MServer mServerRef = MeasurementControlModule.mServerConnectionManager.getVerifiedMServerReference();
			try {
				Port_Transferable transferable = mServerRef.transmitPort((Identifier_Transferable) id.getTransferable());
				port = new Port(transferable);
				Log.debugMessage("MCMConfigurationObjectLoader.loadPort | Port '" + id
						+ "' loaded from MeasurementServer", Log.DEBUGLEVEL08);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
					throw new ObjectNotFoundException("Port '" + id + "' not found on Measurement Server -- " + are.message);
				throw new RetrieveObjectException("Cannot retrieve Port '" + id
						+ "' from Measurement Server -- " + are.message);
			}
			catch (Throwable throwable) {
				Log.errorException(throwable);
			}

			if (port != null) {
				try {
					ConfigurationDatabaseContext.getPortDatabase().insert(port);
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
				return port;
			}
			throw new ObjectNotFoundException("Port '" + id + "' not found on Measurement Server");
		}	//catch (ObjectNotFoundException e)
	}

	public MeasurementPort loadMeasurementPort(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new MeasurementPort(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("MCMConfigurationObjectLoader.loadMeasurementPort | MeasurementPort '" + id
					+ "' not found in database; trying to load from Measurement Server", Log.DEBUGLEVEL08);
			MeasurementPort measurementPort = null;
	
			MServer mServerRef = MeasurementControlModule.mServerConnectionManager.getVerifiedMServerReference();
			try {
				MeasurementPort_Transferable transferable = mServerRef.transmitMeasurementPort((Identifier_Transferable) id.getTransferable());
				measurementPort = new MeasurementPort(transferable);
				Log.debugMessage("MCMConfigurationObjectLoader.loadMeasurementPort | MeasurementPort '" + id
						+ "' loaded from MeasurementServer", Log.DEBUGLEVEL08);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
					throw new ObjectNotFoundException("MeasurementPort '" + id + "' not found on Measurement Server -- " + are.message);
				throw new RetrieveObjectException("Cannot retrieve MeasurementPort '" + id
						+ "' from Measurement Server -- " + are.message);
			}
			catch (Throwable throwable) {
				Log.errorException(throwable);
			}
	
			if (measurementPort != null) {
				try {
					ConfigurationDatabaseContext.getMeasurementPortDatabase().insert(measurementPort);
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
				return measurementPort;
			}
			throw new ObjectNotFoundException("MeasurementPort '" + id + "' not found on Measurement Server");
		}	//catch (ObjectNotFoundException e)
	}

	public TransmissionPath loadTransmissionPath(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new TransmissionPath(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("MCMConfigurationObjectLoader.loadTransmissionPath | TransmissionPath '" + id
					+ "' not found in database; trying to load from Measurement Server", Log.DEBUGLEVEL08);
			TransmissionPath transmissionPath = null;
	
			MServer mServerRef = MeasurementControlModule.mServerConnectionManager.getVerifiedMServerReference();
			try {
				TransmissionPath_Transferable transferable = mServerRef.transmitTransmissionPath((Identifier_Transferable) id.getTransferable());
				transmissionPath = new TransmissionPath(transferable);
				Log.debugMessage("MCMConfigurationObjectLoader.loadTransmissionPath | TransmissionPath '" + id
						+ "' loaded from MeasurementServer", Log.DEBUGLEVEL08);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
					throw new ObjectNotFoundException("TransmissionPath '" + id + "' not found on Measurement Server -- " + are.message);
				throw new RetrieveObjectException("Cannot retrieve TransmissionPath '" + id
						+ "' from Measurement Server -- " + are.message);
			}
			catch (Throwable throwable) {
				Log.errorException(throwable);
			}
	
			if (transmissionPath != null) {
				try {
					ConfigurationDatabaseContext.getTransmissionPathDatabase().insert(transmissionPath);
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
				return transmissionPath;
			}
			throw new ObjectNotFoundException("TransmissionPath '" + id + "' not found on Measurement Server");
		}	//catch (ObjectNotFoundException e)
	}

	public KIS loadKIS(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new KIS(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("MCMConfigurationObjectLoader.loadKIS | KIS '" + id
					+ "' not found in database; trying to load from Measurement Server", Log.DEBUGLEVEL08);
			KIS kis = null;

			MServer mServerRef = MeasurementControlModule.mServerConnectionManager.getVerifiedMServerReference();
			try {
				KIS_Transferable transferable = mServerRef.transmitKIS((Identifier_Transferable) id.getTransferable());
				kis = new KIS(transferable);
				Log.debugMessage("MCMConfigurationObjectLoader.loadKIS | KIS '" + id
						+ "' loaded from MeasurementServer", Log.DEBUGLEVEL08);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
					throw new ObjectNotFoundException("KIS '" + id + "' not found on Measurement Server -- " + are.message);
				throw new RetrieveObjectException("Cannot retrieve KIS '" + id
						+ "' from Measurement Server -- " + are.message);
			}
			catch (Throwable throwable) {
				Log.errorException(throwable);
			}

			if (kis != null) {
				try {
					ConfigurationDatabaseContext.getKISDatabase().insert(kis);
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
				return kis;
			}
			throw new ObjectNotFoundException("KIS '" + id + "' not found on Measurement Server");
		}	//catch (ObjectNotFoundException e)
	}

	public MonitoredElement loadMonitoredElement(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new MonitoredElement(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("MCMConfigurationObjectLoader.loadMonitoredElement | MonitoredElement '" + id
					+ "' not found in database; trying to load from Measurement Server", Log.DEBUGLEVEL08);
			MonitoredElement monitoredElement = null;

			MServer mServerRef = MeasurementControlModule.mServerConnectionManager.getVerifiedMServerReference();
			try {
				MonitoredElement_Transferable transferable = mServerRef.transmitMonitoredElement((Identifier_Transferable) id.getTransferable());
				monitoredElement = new MonitoredElement(transferable);
				Log.debugMessage("MCMConfigurationObjectLoader.loadMonitoredElement | MonitoredElement '" + id
						+ "' loaded from MeasurementServer", Log.DEBUGLEVEL08);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
					throw new ObjectNotFoundException("MonitoredElement '" + id + "' not found on Measurement Server -- " + are.message);
				throw new RetrieveObjectException("Cannot retrieve MonitoredElement '" + id
						+ "' from Measurement Server -- " + are.message);
			}
			catch (Throwable throwable) {
				Log.errorException(throwable);
			}

			if (monitoredElement != null) {
				try {
					ConfigurationDatabaseContext.getMonitoredElementDatabase().insert(monitoredElement);
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
				return monitoredElement;
			}
			throw new ObjectNotFoundException("MonitoredElement '" + id + "' not found on Measurement Server");
		}	//catch (ObjectNotFoundException e)
	}

	public Link loadLink(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new Link(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("MCMConfigurationObjectLoader.loadLink | Link '" + id
					+ "' not found in database; trying to load from Measurement Server", Log.DEBUGLEVEL08);
			Link link = null;
	
			MServer mServerRef = MeasurementControlModule.mServerConnectionManager.getVerifiedMServerReference();
			try {
				Link_Transferable transferable = mServerRef.transmitLink((Identifier_Transferable) id.getTransferable());
				link = new Link(transferable);
				Log.debugMessage("MCMConfigurationObjectLoader.loadLink | Link '" + id
						+ "' loaded from MeasurementServer", Log.DEBUGLEVEL08);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
					throw new ObjectNotFoundException("Link '" + id + "' not found on Measurement Server -- " + are.message);
				throw new RetrieveObjectException("Cannot retrieve Link '" + id
						+ "' from Measurement Server -- " + are.message);
			}
			catch (Throwable throwable) {
				Log.errorException(throwable);
			}
	
			if (link != null) {
				try {
					ConfigurationDatabaseContext.getLinkDatabase().insert(link);
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
				return link;
			}
			throw new ObjectNotFoundException("Link '" + id + "' not found on Measurement Server");
		}	//catch (ObjectNotFoundException e)
	}



	/* Load multiple objects*/

	public Set loadMeasurementPortTypes(Set ids) throws RetrieveObjectException {
		MeasurementPortTypeDatabase database = ConfigurationDatabaseContext.getMeasurementPortTypeDatabase();
		Set objects = super.retrieveFromDatabase(database, ids);
		Identifier_Transferable[] loadIdsT = super.createLoadIdsTransferable(ids, objects);
		if (loadIdsT.length == 0)
			return objects;

		Set loadedObjects = new HashSet();

		try {
			MServer mServerRef = MeasurementControlModule.mServerConnectionManager.getVerifiedMServerReference();
			MeasurementPortType_Transferable[] transferables = mServerRef.transmitMeasurementPortTypes(loadIdsT);
			for (int i = 0; i < transferables.length; i++) {
				try {
					loadedObjects.add(new MeasurementPortType(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
		}
		catch (AMFICOMRemoteException are) {
			Log.errorMessage("MCMConfigurationObjectLoader.loadMeasurementPortTypes | Cannot load objects from MeasurementServer");
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
		}

		if (!loadedObjects.isEmpty()) {
			objects.addAll(loadedObjects);

			try {
				database.insert(loadedObjects);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}

		return objects;
	}



	public Set loadMeasurementPorts(Set ids) throws RetrieveObjectException {
		MeasurementPortDatabase database = ConfigurationDatabaseContext.getMeasurementPortDatabase();
		Set objects = super.retrieveFromDatabase(database, ids);
		Identifier_Transferable[] loadIdsT = super.createLoadIdsTransferable(ids, objects);
		if (loadIdsT.length == 0)
			return objects;

		Set loadedObjects = new HashSet();

		try {
			MServer mServerRef = MeasurementControlModule.mServerConnectionManager.getVerifiedMServerReference();
			MeasurementPort_Transferable[] transferables = mServerRef.transmitMeasurementPorts(loadIdsT);
			for (int i = 0; i < transferables.length; i++) {
				try {
					loadedObjects.add(new MeasurementPort(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
		}
		catch (AMFICOMRemoteException are) {
			Log.errorMessage("MCMConfigurationObjectLoader.loadMeasurementPorts | Cannot load objects from MeasurementServer");
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
		}

		if (!loadedObjects.isEmpty()) {
			objects.addAll(loadedObjects);

			try {
				database.insert(loadedObjects);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}

		return objects;
	}

	public Set loadKISs(Set ids) throws RetrieveObjectException {
		KISDatabase database = ConfigurationDatabaseContext.getKISDatabase();
		Set objects = super.retrieveFromDatabase(database, ids);
		Identifier_Transferable[] loadIdsT = super.createLoadIdsTransferable(ids, objects);
		if (loadIdsT.length == 0)
			return objects;
	
		Set loadedObjects = new HashSet();
	
		try {
			MServer mServerRef = MeasurementControlModule.mServerConnectionManager.getVerifiedMServerReference();
			KIS_Transferable[] transferables = mServerRef.transmitKISs(loadIdsT);
			for (int i = 0; i < transferables.length; i++) {
				try {
					loadedObjects.add(new KIS(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
		}
		catch (AMFICOMRemoteException are) {
			Log.errorMessage("MCMConfigurationObjectLoader.loadKISs | Cannot load objects from MeasurementServer");
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
		}
	
		if (!loadedObjects.isEmpty()) {
			objects.addAll(loadedObjects);
	
			try {
				database.insert(loadedObjects);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}
	
		return objects;
	}

	public Set loadMonitoredElements(Set ids) throws RetrieveObjectException {
		MonitoredElementDatabase database = ConfigurationDatabaseContext.getMonitoredElementDatabase();
		Set objects = super.retrieveFromDatabase(database, ids);
		Identifier_Transferable[] loadIdsT = super.createLoadIdsTransferable(ids, objects);
		if (loadIdsT.length == 0)
			return objects;

		Set loadedObjects = new HashSet();

		try {
			MServer mServerRef = MeasurementControlModule.mServerConnectionManager.getVerifiedMServerReference();
			MonitoredElement_Transferable[] transferables = mServerRef.transmitMonitoredElements(loadIdsT);
			for (int i = 0; i < transferables.length; i++) {
				try {
					loadedObjects.add(new MonitoredElement(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
		}
		catch (AMFICOMRemoteException are) {
			Log.errorMessage("MCMConfigurationObjectLoader.loadMonitoredElements | Cannot load objects from MeasurementServer");
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
		}

		if (!loadedObjects.isEmpty()) {
			objects.addAll(loadedObjects);

			try {
				database.insert(loadedObjects);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}

		return objects;
	}




	/* Load multiple objects but ids*/

	public Set loadKISsButIds(StorableObjectCondition condition, Set ids) throws RetrieveObjectException {
		KISDatabase database = ConfigurationDatabaseContext.getKISDatabase();
		Set objects = super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
		Identifier_Transferable[] loadButIdsT = super.createLoadButIdsTransferable(ids, objects);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);

		Set loadedObjects = new HashSet();

		try {
			MServer mServerRef = MeasurementControlModule.mServerConnectionManager.getVerifiedMServerReference();
			KIS_Transferable[] transferables = mServerRef.transmitKISsButIdsByCondition(loadButIdsT, conditionT);
			for (int i = 0; i < transferables.length; i++) {
				try {
					loadedObjects.add(new KIS(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
		}
		catch (AMFICOMRemoteException are) {
			Log.errorMessage("MCMConfigurationObjectLoader.loadKISsButIds | Cannot load objects from MeasurementServer");
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
		}

		if (!loadedObjects.isEmpty()) {
			objects.addAll(loadedObjects);

			try {
				database.insert(loadedObjects);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}

		return objects;
	}





	/*
	 * MCM do not need in all below methods
	 * */

	public CableLinkType loadCableLinkType(Identifier id) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, id: " + id);
	}

	public CableThreadType loadCableThreadType(Identifier id) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, id: " + id);
	}

	public CableThread loadCableThread(Identifier id) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, id: " + id);
	}





	public Set loadCableLinkTypes(Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Set loadCableLinkTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Set loadCableThreadTypes(Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Set loadCableThreadTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Set loadEquipmentTypes(Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Set loadEquipmentTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Set loadTransmissionPathTypes(Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Set loadTransmissionPathTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Set loadPortTypes(Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Set loadLinkTypes(Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Set loadLinkTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Set loadPortTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Set loadCableThreads(Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Set loadCableThreadsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Set loadEquipments(Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Set loadEquipmentsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Set loadLinks(Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Set loadLinksButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Set loadMeasurementPortsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Set loadMeasurementPortTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Set loadMonitoredElementsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Set loadPorts(Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Set loadPortsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Set loadTransmissionPaths(Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Set loadTransmissionPathsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}





	public void saveCableLinkType(CableLinkType cableLinkType, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, cableLinkType: " + cableLinkType.getId() + ", force: " + force);
	}

	public void saveCableThreadType(CableThreadType cableThreadType, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, cableThreadType: " + cableThreadType.getId() + ", force: " + force);
	}

	public void saveTransmissionPathType(TransmissionPathType transmissionPathType, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, transmissionPathType: " + transmissionPathType.getId() + ", force: " + force);
	}

	public void saveEquipmentType(EquipmentType equipmentType, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, equipmentType: " + equipmentType.getId() + ", force: " + force);
	}

	public void saveLinkType(LinkType linkType, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, linkType: " + linkType.getId() + ", force: " + force);
	}

	public void savePortType(PortType portType, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, portType: " + portType.getId() + ", force: " + force);
	}

	public void saveMeasurementPortType(MeasurementPortType measurementPortType, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, measurementPortType: " + measurementPortType.getId() + ", force: " + force);
	}

	public void saveCableThread(CableThread cableThread, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, cableThread: " + cableThread.getId() + ", force: " + force);
	}

	public void saveEquipment(Equipment equipment, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, equipment: " + equipment.getId() + ", force: " + force);
	}

	public void saveTransmissionPath(TransmissionPath transmissionPath, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, transmissionPath: " + transmissionPath.getId() + ", force: " + force);
	}

	public void saveKIS(KIS kis, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, kis: " + kis.getId() + ", force: " + force);
	}

	public void saveLink(Link link, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, link: " + link.getId() + ", force: " + force);
	}

	public void savePort(Port port, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, port: " + port.getId() + ", force: " + force);
	}

	public void saveMeasurementPort(MeasurementPort measurementPort, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, measurementPort: " + measurementPort.getId() + ", force: " + force);
	}

	public void saveMonitoredElement(MonitoredElement monitoredElement, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, monitoredElement: " + monitoredElement.getId() + ", force: " + force);
	}
	


	public void saveCableLinkTypes(Set objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}

	public void saveCableThreads(Set objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}

	public void saveCableThreadTypes(Set objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}

	public void saveEquipments(Set objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	
	public void saveEquipmentTypes(Set objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	
	public void saveKISs(Set objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	
	public void saveLinks(Set objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	
	public void saveLinkTypes(Set objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	
	public void saveMeasurementPorts(Set objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	
	public void saveMeasurementPortTypes(Set objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	
	public void saveMonitoredElements(Set objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	
	public void savePorts(Set objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	
	public void savePortTypes(Set objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	
	public void saveTransmissionPaths(Set objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	public void saveTransmissionPathTypes(Set objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}





	public Set refresh(Set storableObjects) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, objects: " + storableObjects);
	}





	public void delete(final Set identifiables) {
		throw new UnsupportedOperationException("Method not implemented, objects: " + identifiables);
	}

	public void delete(Identifier id) {
		throw new UnsupportedOperationException("Method not implemented, id: " + id);
	}

}
