/*
 * $Id: MCMConfigurationObjectLoader.java,v 1.17 2005/02/15 15:12:21 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.configuration.ConfigurationDatabaseContext;
import com.syrus.AMFICOM.configuration.DatabaseConfigurationObjectLoader;
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.EquipmentDatabase;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.EquipmentTypeDatabase;
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
import com.syrus.AMFICOM.configuration.PortDatabase;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.PortTypeDatabase;
import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.configuration.TransmissionPathDatabase;
import com.syrus.AMFICOM.configuration.TransmissionPathType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.17 $, $Date: 2005/02/15 15:12:21 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

final class MCMConfigurationObjectLoader extends DatabaseConfigurationObjectLoader {

	public EquipmentType loadEquipmentType(Identifier id) throws RetrieveObjectException, CommunicationException {
		EquipmentType equipmentType = null;
		try {
			equipmentType = new EquipmentType(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("Equipment type '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
			try {
				equipmentType = new EquipmentType(MeasurementControlModule.mServerRef.transmitEquipmentType((Identifier_Transferable) id.getTransferable()));
				ConfigurationDatabaseContext.getEquipmentTypeDatabase().update(equipmentType, null, StorableObjectDatabase.UPDATE_FORCE);
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.activateMServerReference();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				String mesg = null;
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					mesg = "Equipment type '" + id + "' not found on server database";
				else
					mesg = "Cannot retrieve equipment type '" + id + "' from server database -- " + are.message;
				Log.errorMessage(mesg);
				throw new RetrieveObjectException(mesg);
			}
			catch (ApplicationException ae) {
				throw new RetrieveObjectException(ae);
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
				portType = new PortType(MeasurementControlModule.mServerRef.transmitPortType((Identifier_Transferable) id.getTransferable()));
				ConfigurationDatabaseContext.getPortTypeDatabase().update(portType, null, StorableObjectDatabase.UPDATE_FORCE);
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.activateMServerReference();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				String mesg = null;
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					mesg = "Port type '" + id + "' not found on server database";
				else
					mesg = "Cannot retrieve port type '" + id + "' from server database -- " + are.message;
				Log.errorMessage(mesg);
				throw new RetrieveObjectException(mesg);
			}
			catch (ApplicationException ae) {
				throw new RetrieveObjectException(ae);
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
				measurementPortType = new MeasurementPortType(MeasurementControlModule.mServerRef.transmitMeasurementPortType((Identifier_Transferable) id.getTransferable()));
				ConfigurationDatabaseContext.getMeasurementPortTypeDatabase().update(measurementPortType, null, StorableObjectDatabase.UPDATE_FORCE);
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.activateMServerReference();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				String mesg = null;
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					mesg = "Measurement port type '" + id + "' not found on server database";
				else
					mesg = "Cannot retrieve measurement port type '" + id + "' from server database -- " + are.message;
				Log.errorMessage(mesg);
				throw new RetrieveObjectException(mesg);
			}
			catch (ApplicationException ae) {
				throw new RetrieveObjectException(ae);
			}
		}
		return measurementPortType;
	}

	public Equipment loadEquipment(Identifier id) throws RetrieveObjectException, CommunicationException {
		Equipment equipment = null;
		try {
			equipment = new Equipment(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("Equipment '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
			try {
				equipment = new Equipment(MeasurementControlModule.mServerRef.transmitEquipment((Identifier_Transferable) id.getTransferable()));
				ConfigurationDatabaseContext.getEquipmentDatabase().update(equipment, null, StorableObjectDatabase.UPDATE_FORCE);
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.activateMServerReference();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				String mesg = null;
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					mesg = "Equipment '" + id + "' not found on server database";
				else
					mesg = "Cannot retrieve equipment '" + id + "' from server database -- " + are.message;
				Log.errorMessage(mesg);
				throw new RetrieveObjectException(mesg);
			}
			catch (ApplicationException ae) {
				throw new RetrieveObjectException(ae);
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
				port = new Port(MeasurementControlModule.mServerRef.transmitPort((Identifier_Transferable) id.getTransferable()));
				ConfigurationDatabaseContext.getPortDatabase().update(port, null, StorableObjectDatabase.UPDATE_FORCE);
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.activateMServerReference();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				String mesg = null;
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					mesg = "Port '" + id + "' not found on server database";
				else
					mesg = "Cannot retrieve port '" + id + "' from server database -- " + are.message;
				Log.errorMessage(mesg);
				throw new RetrieveObjectException(mesg);
			}
			catch (ApplicationException ae) {
				throw new RetrieveObjectException(ae);
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
				transmissionPath = new TransmissionPath(MeasurementControlModule.mServerRef.transmitTransmissionPath((Identifier_Transferable) id.getTransferable()));
				ConfigurationDatabaseContext.getTransmissionPathDatabase().update(transmissionPath, null, StorableObjectDatabase.UPDATE_FORCE);
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.activateMServerReference();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				String mesg = null;
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					mesg = "Transmission path '" + id + "' not found on server database";
				else
					mesg = "Cannot retrieve transmission path '" + id + "' from server database -- " + are.message;
				Log.errorMessage(mesg);
				throw new RetrieveObjectException(mesg);
			}
			catch (ApplicationException ae) {
				throw new RetrieveObjectException(ae);
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
				kis = new KIS(MeasurementControlModule.mServerRef.transmitKIS((Identifier_Transferable) id.getTransferable()));
				ConfigurationDatabaseContext.getKISDatabase().update(kis, null, StorableObjectDatabase.UPDATE_FORCE);
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.activateMServerReference();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				String mesg = null;
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					mesg = "KIS '" + id + "' not found on server database";
				else
					mesg = "Cannot retrieve KIS '" + id + "' from server database -- " + are.message;
				Log.errorMessage(mesg);
				throw new RetrieveObjectException(mesg);
			}
			catch (ApplicationException ae) {
				throw new RetrieveObjectException(ae);
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
				measurementPort = new MeasurementPort(MeasurementControlModule.mServerRef.transmitMeasurementPort((Identifier_Transferable) id.getTransferable()));
				ConfigurationDatabaseContext.getMeasurementPortDatabase().update(measurementPort, null, StorableObjectDatabase.UPDATE_FORCE);
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.activateMServerReference();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				String mesg = null;
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					mesg = "Measurement port '" + id + "' not found on server database";
				else
					mesg = "Cannot retrieve measurement port '" + id + "' from server database -- " + are.message;
				Log.errorMessage(mesg);
				throw new RetrieveObjectException(mesg);
			}
			catch (ApplicationException ae) {
				throw new RetrieveObjectException(ae);
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
				monitoredElement = new MonitoredElement(MeasurementControlModule.mServerRef.transmitMonitoredElement((Identifier_Transferable) id.getTransferable()));
				ConfigurationDatabaseContext.getMonitoredElementDatabase().update(monitoredElement, null, StorableObjectDatabase.UPDATE_FORCE);
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.activateMServerReference();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				String mesg = null;
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					mesg = "Monitored element '" + id + "' not found on server database";
				else
					mesg = "Cannot retrieve monitored element '" + id + "' from server database -- " + are.message;
				Log.errorMessage(mesg);
				throw new RetrieveObjectException(mesg);
			}
			catch (ApplicationException ae) {
				throw new RetrieveObjectException(ae);
			}
		}
		return monitoredElement;
	}	





	public Collection loadEquipments(Collection ids) throws DatabaseException, CommunicationException {
		EquipmentDatabase database = (EquipmentDatabase)ConfigurationDatabaseContext.getEquipmentDatabase();
		Collection collection;
		List copyOfList;
		List loadedObjects = new LinkedList();
		Equipment equipment;
		try {
			collection = database.retrieveByIds(ids, null);
			copyOfList = new LinkedList(collection);
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				if(ids.contains(id))
					it.remove();
			}
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				Log.debugMessage("Equipment '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
				try {
					equipment = new Equipment(MeasurementControlModule.mServerRef.transmitEquipment((Identifier_Transferable)id.getTransferable()));
					collection.add(equipment);
					loadedObjects.add(equipment);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.activateMServerReference();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					String mesg = null;
					if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
						mesg = "Equipment '" + id + "' not found on server database";
					else
						mesg = "Cannot retrieve equipment '" + id + "' from server database -- " + are.message;
					Log.errorMessage(mesg);
					throw new RetrieveObjectException(mesg);
				}
			}
			try {
				database.update(loadedObjects, null, StorableObjectDatabase.UPDATE_FORCE);
			}
			catch (VersionCollisionException vce) {
				//This never be caught
				Log.errorException(vce);
			}
		}
		catch (IllegalDataException e) {
			Log.errorMessage("MCMConfigurationObjectLoader.loadEquipments | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("MCMConfigurationObjectLoader.loadEquipments | Illegal Storable Object: " + e.getMessage());
		}
		return collection;
	}

	public Collection loadEquipmentTypes(Collection ids) throws DatabaseException, CommunicationException {
		EquipmentTypeDatabase database = (EquipmentTypeDatabase)ConfigurationDatabaseContext.getEquipmentTypeDatabase();
		Collection collection;
		List copyOfList;
		List loadedObjects = new LinkedList();
		EquipmentType equipmentType;
		try {
			collection = database.retrieveByIds(ids, null);
			copyOfList = new LinkedList(collection);
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				if(ids.contains(id))
					it.remove();
			}
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				Log.debugMessage("Equipment type '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
				try {
					equipmentType = new EquipmentType(MeasurementControlModule.mServerRef.transmitEquipmentType((Identifier_Transferable)id.getTransferable()));
					collection.add(equipmentType);
					loadedObjects.add(equipmentType);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.activateMServerReference();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					String mesg = null;
					if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
						mesg = "Equipment type '" + id + "' not found on server database";
					else
						mesg = "Cannot retrieve equipment type '" + id + "' from server database -- " + are.message;
					Log.errorMessage(mesg);
					throw new RetrieveObjectException(mesg);
				}
			}
			try {
				database.update(loadedObjects, null, StorableObjectDatabase.UPDATE_FORCE);
			}
			catch (VersionCollisionException vce) {
				//This never be caught
				Log.errorException(vce);
			}
		}
		catch (IllegalDataException e) {
			Log.errorMessage("MCMConfigurationObjectLoader.loadEquipmentTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("MCMConfigurationObjectLoader.loadEquipmentTypes | Illegal Storable Object: " + e.getMessage());
		}
		return collection;
	}

	public Collection loadKISs(Collection ids) throws DatabaseException, CommunicationException {
		KISDatabase database = (KISDatabase)ConfigurationDatabaseContext.getKISDatabase();
		Collection collection;
		List copyOfList;
		List loadedObjects = new LinkedList();
		KIS kis;
		try {
			collection = database.retrieveByIds(ids, null);
			copyOfList = new LinkedList(collection);
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				if(ids.contains(id))
					it.remove();
			}
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				Log.debugMessage("KIS '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
				try {
					kis = new KIS(MeasurementControlModule.mServerRef.transmitKIS((Identifier_Transferable)id.getTransferable()));
					collection.add(kis);
					loadedObjects.add(kis);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.activateMServerReference();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					String mesg = null;
					if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
						mesg = "KIS '" + id + "' not found on server database";
					else
						mesg = "Cannot retrieve KIS '" + id + "' from server database -- " + are.message;
					Log.errorMessage(mesg);
					throw new RetrieveObjectException(mesg);
				}
			}
			try {
				database.update(loadedObjects, null, StorableObjectDatabase.UPDATE_FORCE);
			}
			catch (VersionCollisionException vce) {
				//This never be caught
				Log.errorException(vce);
			}
		}
		catch (IllegalDataException e) {
			Log.errorMessage("MCMConfigurationObjectLoader.loadKISs | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("MCMConfigurationObjectLoader.loadKISs | Illegal Storable Object: " + e.getMessage());
		}
		return collection;
	}	

	public Collection loadMeasurementPorts(Collection ids) throws DatabaseException, CommunicationException {
		MeasurementPortDatabase database = (MeasurementPortDatabase)ConfigurationDatabaseContext.getMeasurementPortDatabase();
		Collection collection;
		List copyOfList;
		List loadedObjects = new LinkedList();
		MeasurementPort measurementPort;
		try {
			collection = database.retrieveByIds(ids, null);
			copyOfList = new LinkedList(collection);
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				if(ids.contains(id))
					it.remove();
			}
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				Log.debugMessage("Measurement port '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
				try {
					measurementPort = new MeasurementPort(MeasurementControlModule.mServerRef.transmitMeasurementPort((Identifier_Transferable)id.getTransferable()));
					collection.add(measurementPort);
					loadedObjects.add(measurementPort);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.activateMServerReference();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					String mesg = null;
					if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
						mesg = "Measurement port '" + id + "' not found on server database";
					else
						mesg = "Cannot retrieve measurement port '" + id + "' from server database -- " + are.message;
					Log.errorMessage(mesg);
					throw new RetrieveObjectException(mesg);
				}
			}
			try {
				database.update(loadedObjects, null, StorableObjectDatabase.UPDATE_FORCE);
			}
			catch (VersionCollisionException vce) {
				//This never be caught
				Log.errorException(vce);
			}
		}
		catch (IllegalDataException e) {
			Log.errorMessage("MCMConfigurationObjectLoader.loadMeasurementPorts | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("MCMConfigurationObjectLoader.loadMeasurementPorts | Illegal Storable Object: " + e.getMessage());
		}
		return collection;
	}

	public Collection loadMeasurementPortTypes(Collection ids) throws DatabaseException, CommunicationException {
		MeasurementPortTypeDatabase database = (MeasurementPortTypeDatabase)ConfigurationDatabaseContext.getMeasurementPortTypeDatabase();
		Collection collection;
		List copyOfList;
		List loadedObjects = new LinkedList();
		MeasurementPortType measurementPortType;
		try {
			collection = database.retrieveByIds(ids, null);
			copyOfList = new LinkedList(collection);
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				if(ids.contains(id))
					it.remove();
			}
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				Log.debugMessage("Measurement port type '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
				try {
					measurementPortType = new MeasurementPortType(MeasurementControlModule.mServerRef.transmitMeasurementPortType((Identifier_Transferable)id.getTransferable()));
					collection.add(measurementPortType);
					loadedObjects.add(measurementPortType);					
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.activateMServerReference();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					String mesg = null;
					if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
						mesg = "Measurement port type '" + id + "' not found on server database";
					else
						mesg = "Cannot retrieve measurement port type '" + id + "' from server database -- " + are.message;
					Log.errorMessage(mesg);
					throw new RetrieveObjectException(mesg);
				}
			}
			try {
				database.update(loadedObjects, null, StorableObjectDatabase.UPDATE_FORCE);
			}
			catch (VersionCollisionException vce) {
				//This never be caught
				Log.errorException(vce);
			}
		}
		catch (IllegalDataException e) {
			Log.errorMessage("MCMConfigurationObjectLoader.loadMeasurementPortTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("MCMConfigurationObjectLoader.loadMeasurementPortTypes | Illegal Storable Object: " + e.getMessage());
		}
		return collection;
	}

	public Collection loadMonitoredElements(Collection ids) throws DatabaseException, CommunicationException {
		MonitoredElementDatabase database = (MonitoredElementDatabase)ConfigurationDatabaseContext.getMonitoredElementDatabase();
		Collection list;
		List copyOfList;
		List loadedObjects = new LinkedList();
		MonitoredElement monitoredElement;
		try {
			list = database.retrieveByIds(ids, null);
			copyOfList = new LinkedList(list);
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				if(ids.contains(id))
					it.remove();
			}
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				Log.debugMessage("Monitored element '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
				try {
					monitoredElement = new MonitoredElement(MeasurementControlModule.mServerRef.transmitMonitoredElement((Identifier_Transferable)id.getTransferable()));
					list.add(monitoredElement);
					loadedObjects.add(monitoredElement);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.activateMServerReference();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					String mesg = null;
					if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
						mesg = "Monitored element '" + id + "' not found on server database";
					else
						mesg = "Cannot retrieve monitored element '" + id + "' from server database -- " + are.message;
					Log.errorMessage(mesg);
					throw new RetrieveObjectException(mesg);
				}
			}
			try {
				database.update(loadedObjects, null, StorableObjectDatabase.UPDATE_FORCE);
			}
			catch (VersionCollisionException vce) {
				Log.errorException(vce);
			}
		}
		catch (IllegalDataException e) {
			Log.errorMessage("MCMConfigurationObjectLoader.loadMonitoredElements | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("MCMConfigurationObjectLoader.loadMonitoredElements | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public Collection loadPorts(Collection ids) throws DatabaseException, CommunicationException {
		PortDatabase database = (PortDatabase)ConfigurationDatabaseContext.getPortDatabase();
		Collection collection;
		List copyOfList;
		List loadedObjects = new LinkedList();
		Port port;
		try {
			collection = database.retrieveByIds(ids, null);
			copyOfList = new LinkedList(collection);
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				if(ids.contains(id))
					it.remove();
			}
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				Log.debugMessage("Port '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
				try {
					port = new Port(MeasurementControlModule.mServerRef.transmitPort((Identifier_Transferable)id.getTransferable()));
					collection.add(port);
					loadedObjects.add(port);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.activateMServerReference();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					String mesg = null;
					if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
						mesg = "Port '" + id + "' not found on server database";
					else
						mesg = "Cannot retrieve port '" + id + "' from server database -- " + are.message;
					Log.errorMessage(mesg);
					throw new RetrieveObjectException(mesg);
				}
			}
			try {
				database.update(loadedObjects, null, StorableObjectDatabase.UPDATE_FORCE);
			}
			catch (VersionCollisionException vce) {
				Log.errorException(vce);
			}
		}
		catch (IllegalDataException e) {
			Log.errorMessage("MCMConfigurationObjectLoader.loadPorts | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("MCMConfigurationObjectLoader.loadPorts | Illegal Storable Object: " + e.getMessage());
		}
		return collection;
	}

	public Collection loadPortTypes(Collection ids) throws DatabaseException, CommunicationException {
		PortTypeDatabase database = (PortTypeDatabase)ConfigurationDatabaseContext.getPortTypeDatabase();
		Collection collection;
		List copyOfList;
		List loadedObjects = new LinkedList();
		PortType portType;
		try {
			collection = database.retrieveByIds(ids, null);
			copyOfList = new LinkedList(collection);
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				if(ids.contains(id))
					it.remove();
			}
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				Log.debugMessage("Port type '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
				try {
					portType = new PortType(MeasurementControlModule.mServerRef.transmitPortType((Identifier_Transferable)id.getTransferable()));
					collection.add(portType);
					loadedObjects.add(portType);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.activateMServerReference();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					String mesg = null;
					if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
						mesg = "Port type '" + id + "' not found on server database";
					else
						mesg = "Cannot retrieve port type '" + id + "' from server database -- " + are.message;
					Log.errorMessage(mesg);
					throw new RetrieveObjectException(mesg);
				}
			}
			try {
				database.update(loadedObjects, null, StorableObjectDatabase.UPDATE_FORCE);
			}
			catch (VersionCollisionException vce) {
				Log.errorException(vce);
			}
		}
		catch (IllegalDataException e) {
			Log.errorMessage("MCMConfigurationObjectLoader.loadPortTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("MCMConfigurationObjectLoader.loadPortTypes | Illegal Storable Object: " + e.getMessage());
		}
		return collection;
	}
	
	public Collection loadTransmissionPaths(Collection ids) throws DatabaseException, CommunicationException {
		TransmissionPathDatabase database = (TransmissionPathDatabase)ConfigurationDatabaseContext.getTransmissionPathDatabase();
		Collection collection;
		List copyOfList;
		List loadedObjects = new LinkedList();
		TransmissionPath transmissionPath;
		try {
			collection = database.retrieveByIds(ids, null);
			copyOfList = new LinkedList(collection);
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				if(ids.contains(id))
					it.remove();
			}
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				Log.debugMessage("Transmission path '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
				try {
					transmissionPath = new TransmissionPath(MeasurementControlModule.mServerRef.transmitTransmissionPath((Identifier_Transferable)id.getTransferable()));
					collection.add(transmissionPath);
					loadedObjects.add(transmissionPath);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.activateMServerReference();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					String mesg = null;
					if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
						mesg = "Transmission path '" + id + "' not found on server database";
					else
						mesg = "Cannot retrieve transmission path '" + id + "' from server database -- " + are.message;
					Log.errorMessage(mesg);
					throw new RetrieveObjectException(mesg);
				}
			}
			try {
				database.update(loadedObjects, null, StorableObjectDatabase.UPDATE_FORCE);
			}
			catch (VersionCollisionException vce) {
				Log.errorException(vce);
			}
		}
		catch (IllegalDataException e) {
			Log.errorMessage("MCMConfigurationObjectLoader.loadTransmissionPaths | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("MCMConfigurationObjectLoader.loadTransmissionPaths | Illegal Storable Object: " + e.getMessage());
		}
		return collection;
	}


	public java.util.Set refresh(java.util.Set s) {
//       TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}	

	public void saveEquipmentType(EquipmentType equipmentType, boolean force) throws DatabaseException, CommunicationException {
//		 TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	public void savePortType(PortType portType, boolean force) throws DatabaseException, CommunicationException {
//		 TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	public void saveMeasurementPortType(MeasurementPortType measurementPortType, boolean force) throws DatabaseException, CommunicationException {
//		 TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	public void saveTransmissionPathType(TransmissionPathType transmissionPathType, boolean force)
			throws VersionCollisionException, DatabaseException, CommunicationException {
			// TODO Auto-generated method stub

	}

//			public void savePermissionAttributes(PermissionAttributes permissionAttributes, boolean force) throws DatabaseException, CommunicationException {
//		 TODO method isn't complete
//		throw new UnsupportedOperationException("method isn't complete");
//		}


	public void saveEquipment(Equipment equipment, boolean force) throws DatabaseException, CommunicationException {
//		 TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	public void savePort(Port port, boolean force) throws DatabaseException, CommunicationException {
//		 TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	public void saveTransmissionPath(TransmissionPath transmissionPath, boolean force) throws DatabaseException, CommunicationException {
//		 TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	public void saveKIS(KIS kis, boolean force) throws DatabaseException, CommunicationException {
//		 TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	public void saveMeasurementPort(MeasurementPort measurementPort, boolean force) throws DatabaseException, CommunicationException {
//		 TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	public void saveMonitoredElement(MonitoredElement monitoredElement, boolean force) throws DatabaseException, CommunicationException {
//		 TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	public void saveEquipmentTypes(List list, boolean force) throws DatabaseException, CommunicationException {
//		 TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	public void savePortTypes(List list, boolean force) throws DatabaseException, CommunicationException {
//		 TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	public void saveMeasurementPortTypes(List list, boolean force) throws DatabaseException, CommunicationException {
//		 TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	public void saveTransmissionPathTypes(List list, boolean force)
			throws VersionCollisionException, DatabaseException, CommunicationException {
			// TODO Auto-generated method stub

	}

//			public void savePermissionAttributes(PermissionAttributes permissionAttributes, boolean force) throws DatabaseException, CommunicationException {
//		 TODO method isn't complete
//		throw new UnsupportedOperationException("method isn't complete");
//		}

	public void saveEquipments(List list, boolean force) throws DatabaseException, CommunicationException {
//		 TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	public void savePorts(List list, boolean force) throws DatabaseException, CommunicationException {
//		 TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	public void saveTransmissionPaths(List list, boolean force) throws DatabaseException, CommunicationException {
//		 TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	public void saveKISs(List list, boolean force) throws DatabaseException, CommunicationException {
//		 TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	public void saveMeasurementPorts(List list, boolean force) throws DatabaseException, CommunicationException {
//		 TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	public void saveMonitoredElements(List list, boolean force) throws DatabaseException, CommunicationException {
//		 TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	public List loadEquipmentTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
//			 TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}
		
		
	public List loadPortTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
//			 TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	public List loadMeasurementPortTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
//			 TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}		



//			  PermissionAttributes loadPermissionAttributesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
//			 TODO method isn't complete
//			throw new UnsupportedOperationException("method isn't complete");
//			}

	public List loadEquipmentsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
//			 TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	public List loadPortsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
//			 TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	public List loadTransmissionPathsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
//			 TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	public List loadKISsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
//			 TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	public List loadMeasurementPortsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
//			 TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	public List loadMonitoredElementsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
//			 TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	public List loadLinkTypes(List ids) throws DatabaseException, CommunicationException {
//           TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	public List loadLinks(List ids) throws DatabaseException, CommunicationException {
//           TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	public List loadLinkTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
//           TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	public List loadLinksButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
//           TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	public void saveLinkType(LinkType linkType, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException {
//           TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	public void saveLink(Link link, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException {
//           TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	public void saveLinkTypes(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException {
//           TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	public void saveLinks(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException {
//           TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	public List loadTransmissionPathTypes(List ids) throws DatabaseException, CommunicationException {
//           TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	public List loadTransmissionPathTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
//           TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	public LinkType loadLinkType(Identifier id) throws DatabaseException, CommunicationException {
//           TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	public TransmissionPathType loadTransmissionPathType(Identifier id) throws DatabaseException, CommunicationException {
//           TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	public Link loadLink(Identifier id) throws DatabaseException, CommunicationException {
//           TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

}
