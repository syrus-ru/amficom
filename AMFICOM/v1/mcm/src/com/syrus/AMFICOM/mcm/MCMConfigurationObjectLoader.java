/*
 * $Id: MCMConfigurationObjectLoader.java,v 1.24 2005/03/31 16:01:56 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
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
import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.configuration.MonitoredElement;
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
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectConditionBuilder;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.24 $, $Date: 2005/03/31 16:01:56 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

final class MCMConfigurationObjectLoader extends DatabaseConfigurationObjectLoader {

	public EquipmentType loadEquipmentType(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new EquipmentType(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("EquipmentType '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL08);
			com.syrus.AMFICOM.mserver.corba.MServer mServerRef = MeasurementControlModule.mServerRef;
			if (mServerRef != null) {

				try {
					EquipmentType_Transferable transferable = mServerRef.transmitEquipmentType((Identifier_Transferable) id.getTransferable());
					EquipmentType equipmentType = new EquipmentType(transferable);

					try {
						ConfigurationDatabaseContext.getEquipmentTypeDatabase().insert(equipmentType);
					}
					catch (ApplicationException ae) {
						Log.errorException(ae);
					}

					return equipmentType;
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
						throw new ObjectNotFoundException("EquipmentType '" + id + "' not found in MServer database");
					throw new RetrieveObjectException("Cannot retrieve EquipmentType '" + id + "' from MServer database -- " + are.message);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.resetMServerConnection();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
			}
			String mesg = "Remote reference for server is null; will try to reactivate it";
			Log.errorMessage(mesg);
			MeasurementControlModule.resetMServerConnection();
			throw new CommunicationException(mesg);
		}
	}

	public TransmissionPathType loadTransmissionPathType(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new TransmissionPathType(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("TransmissionPathType '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL08);
			com.syrus.AMFICOM.mserver.corba.MServer mServerRef = MeasurementControlModule.mServerRef;
			if (mServerRef != null) {

				try {
					TransmissionPathType_Transferable transferable = mServerRef.transmitTransmissionPathType((Identifier_Transferable) id.getTransferable());
					TransmissionPathType transmissionPathType = new TransmissionPathType(transferable);

					try {
						ConfigurationDatabaseContext.getTransmissionPathTypeDatabase().insert(transmissionPathType);
					}
					catch (ApplicationException ae) {
						Log.errorException(ae);
					}

					return transmissionPathType;
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
						throw new ObjectNotFoundException("TransmissionPathType '" + id + "' not found in MServer database");
					throw new RetrieveObjectException("Cannot retrieve TransmissionPathType '" + id + "' from MServer database -- " + are.message);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.resetMServerConnection();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
			}
			String mesg = "Remote reference for server is null; will try to reactivate it";
			Log.errorMessage(mesg);
			MeasurementControlModule.resetMServerConnection();
			throw new CommunicationException(mesg);
		}
	}

	public LinkType loadLinkType(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new LinkType(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("LinkType '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL08);
			com.syrus.AMFICOM.mserver.corba.MServer mServerRef = MeasurementControlModule.mServerRef;
			if (mServerRef != null) {

				try {
					LinkType_Transferable transferable = mServerRef.transmitLinkType((Identifier_Transferable) id.getTransferable());
					LinkType linkType = new LinkType(transferable);

					try {
						ConfigurationDatabaseContext.getLinkTypeDatabase().insert(linkType);
					}
					catch (ApplicationException ae) {
						Log.errorException(ae);
					}

					return linkType;
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
						throw new ObjectNotFoundException("LinkType '" + id + "' not found in MServer database");
					throw new RetrieveObjectException("Cannot retrieve LinkType '" + id + "' from MServer database -- " + are.message);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.resetMServerConnection();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
			}
			String mesg = "Remote reference for server is null; will try to reactivate it";
			Log.errorMessage(mesg);
			MeasurementControlModule.resetMServerConnection();
			throw new CommunicationException(mesg);
		}
	}

	public PortType loadPortType(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new PortType(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("PortType '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL08);
			com.syrus.AMFICOM.mserver.corba.MServer mServerRef = MeasurementControlModule.mServerRef;
			if (mServerRef != null) {

				try {
					PortType_Transferable transferable = mServerRef.transmitPortType((Identifier_Transferable) id.getTransferable());
					PortType portType = new PortType(transferable);

					try {
						ConfigurationDatabaseContext.getPortTypeDatabase().insert(portType);
					}
					catch (ApplicationException ae) {
						Log.errorException(ae);
					}

					return portType;
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
						throw new ObjectNotFoundException("PortType '" + id + "' not found in MServer database");
					throw new RetrieveObjectException("Cannot retrieve PortType '" + id + "' from MServer database -- " + are.message);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.resetMServerConnection();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
			}
			String mesg = "Remote reference for server is null; will try to reactivate it";
			Log.errorMessage(mesg);
			MeasurementControlModule.resetMServerConnection();
			throw new CommunicationException(mesg);
		}
	}

	public MeasurementPortType loadMeasurementPortType(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new MeasurementPortType(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("MeasurementPortType '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL08);
			com.syrus.AMFICOM.mserver.corba.MServer mServerRef = MeasurementControlModule.mServerRef;
			if (mServerRef != null) {

				try {
					MeasurementPortType_Transferable transferable = mServerRef.transmitMeasurementPortType((Identifier_Transferable) id.getTransferable());
					MeasurementPortType measurementPortType = new MeasurementPortType(transferable);

					try {
						ConfigurationDatabaseContext.getMeasurementPortTypeDatabase().insert(measurementPortType);
					}
					catch (ApplicationException ae) {
						Log.errorException(ae);
					}

					return measurementPortType;
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
						throw new ObjectNotFoundException("MeasurementPortType '" + id + "' not found in MServer database");
					throw new RetrieveObjectException("Cannot retrieve MeasurementPortType '" + id + "' from MServer database -- " + are.message);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.resetMServerConnection();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
			}
			String mesg = "Remote reference for server is null; will try to reactivate it";
			Log.errorMessage(mesg);
			MeasurementControlModule.resetMServerConnection();
			throw new CommunicationException(mesg);
		}
	}

	public Equipment loadEquipment(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new Equipment(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("Equipment '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL08);
			com.syrus.AMFICOM.mserver.corba.MServer mServerRef = MeasurementControlModule.mServerRef;
			if (mServerRef != null) {

				try {
					Equipment_Transferable transferable = mServerRef.transmitEquipment((Identifier_Transferable) id.getTransferable());
					Equipment equipment = new Equipment(transferable);

					try {
						ConfigurationDatabaseContext.getEquipmentDatabase().insert(equipment);
					}
					catch (ApplicationException ae) {
						Log.errorException(ae);
					}

					return equipment;
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
						throw new ObjectNotFoundException("Equipment '" + id + "' not found in MServer database");
					throw new RetrieveObjectException("Cannot retrieve Equipment '" + id + "' from MServer database -- " + are.message);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.resetMServerConnection();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
			}
			String mesg = "Remote reference for server is null; will try to reactivate it";
			Log.errorMessage(mesg);
			MeasurementControlModule.resetMServerConnection();
			throw new CommunicationException(mesg);
		}
	}

	public Port loadPort(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new Port(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("Port '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL08);
			com.syrus.AMFICOM.mserver.corba.MServer mServerRef = MeasurementControlModule.mServerRef;
			if (mServerRef != null) {

				try {
					Port_Transferable transferable = mServerRef.transmitPort((Identifier_Transferable) id.getTransferable());
					Port port = new Port(transferable);

					try {
						ConfigurationDatabaseContext.getPortDatabase().insert(port);
					}
					catch (ApplicationException ae) {
						Log.errorException(ae);
					}

					return port;
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
						throw new ObjectNotFoundException("Port '" + id + "' not found in MServer database");
					throw new RetrieveObjectException("Cannot retrieve Port '" + id + "' from MServer database -- " + are.message);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.resetMServerConnection();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
			}
			String mesg = "Remote reference for server is null; will try to reactivate it";
			Log.errorMessage(mesg);
			MeasurementControlModule.resetMServerConnection();
			throw new CommunicationException(mesg);
		}
	}

	public Link loadLink(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new Link(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("Link '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL08);
			com.syrus.AMFICOM.mserver.corba.MServer mServerRef = MeasurementControlModule.mServerRef;
			if (mServerRef != null) {

				try {
					Link_Transferable transferable = mServerRef.transmitLink((Identifier_Transferable) id.getTransferable());
					Link link = new Link(transferable);

					try {
						ConfigurationDatabaseContext.getLinkDatabase().insert(link);
					}
					catch (ApplicationException ae) {
						Log.errorException(ae);
					}

					return link;
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
						throw new ObjectNotFoundException("Link '" + id + "' not found in MServer database");
					throw new RetrieveObjectException("Cannot retrieve Link '" + id + "' from MServer database -- " + are.message);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.resetMServerConnection();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
			}
			String mesg = "Remote reference for server is null; will try to reactivate it";
			Log.errorMessage(mesg);
			MeasurementControlModule.resetMServerConnection();
			throw new CommunicationException(mesg);
		}
	}

	public TransmissionPath loadTransmissionPath(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new TransmissionPath(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("TransmissionPath '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL08);
			com.syrus.AMFICOM.mserver.corba.MServer mServerRef = MeasurementControlModule.mServerRef;
			if (mServerRef != null) {

				try {
					TransmissionPath_Transferable transferable = mServerRef.transmitTransmissionPath((Identifier_Transferable) id.getTransferable());
					TransmissionPath transmissionPath = new TransmissionPath(transferable);

					try {
						ConfigurationDatabaseContext.getTransmissionPathDatabase().insert(transmissionPath);
					}
					catch (ApplicationException ae) {
						Log.errorException(ae);
					}

					return transmissionPath;
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
						throw new ObjectNotFoundException("TransmissionPath '" + id + "' not found in MServer database");
					throw new RetrieveObjectException("Cannot retrieve TransmissionPath '" + id + "' from MServer database -- " + are.message);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.resetMServerConnection();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
			}
			String mesg = "Remote reference for server is null; will try to reactivate it";
			Log.errorMessage(mesg);
			MeasurementControlModule.resetMServerConnection();
			throw new CommunicationException(mesg);
		}
	}

	public KIS loadKIS(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new KIS(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("KIS '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL08);
			com.syrus.AMFICOM.mserver.corba.MServer mServerRef = MeasurementControlModule.mServerRef;
			if (mServerRef != null) {

				try {
					KIS_Transferable transferable = mServerRef.transmitKIS((Identifier_Transferable) id.getTransferable());
					KIS kis = new KIS(transferable);

					try {
						ConfigurationDatabaseContext.getKISDatabase().insert(kis);
					}
					catch (ApplicationException ae) {
						Log.errorException(ae);
					}

					return kis;
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
						throw new ObjectNotFoundException("KIS '" + id + "' not found in MServer database");
					throw new RetrieveObjectException("Cannot retrieve KIS '" + id + "' from MServer database -- " + are.message);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.resetMServerConnection();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
			}
			String mesg = "Remote reference for server is null; will try to reactivate it";
			Log.errorMessage(mesg);
			MeasurementControlModule.resetMServerConnection();
			throw new CommunicationException(mesg);
		}
	}

	public MeasurementPort loadMeasurementPort(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new MeasurementPort(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("MeasurementPort '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL08);
			com.syrus.AMFICOM.mserver.corba.MServer mServerRef = MeasurementControlModule.mServerRef;
			if (mServerRef != null) {

				try {
					MeasurementPort_Transferable transferable = mServerRef.transmitMeasurementPort((Identifier_Transferable) id.getTransferable());
					MeasurementPort measurementPort = new MeasurementPort(transferable);

					try {
						ConfigurationDatabaseContext.getMeasurementPortDatabase().insert(measurementPort);
					}
					catch (ApplicationException ae) {
						Log.errorException(ae);
					}

					return measurementPort;
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
						throw new ObjectNotFoundException("MeasurementPort '" + id + "' not found in MServer database");
					throw new RetrieveObjectException("Cannot retrieve MeasurementPort '" + id + "' from MServer database -- " + are.message);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.resetMServerConnection();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
			}
			String mesg = "Remote reference for server is null; will try to reactivate it";
			Log.errorMessage(mesg);
			MeasurementControlModule.resetMServerConnection();
			throw new CommunicationException(mesg);
		}
	}

	public MonitoredElement loadMonitoredElement(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new MonitoredElement(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("MonitoredElement '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL08);
			com.syrus.AMFICOM.mserver.corba.MServer mServerRef = MeasurementControlModule.mServerRef;
			if (mServerRef != null) {

				try {
					MonitoredElement_Transferable transferable = mServerRef.transmitMonitoredElement((Identifier_Transferable) id.getTransferable());
					MonitoredElement monitoredElement = new MonitoredElement(transferable);

					try {
						ConfigurationDatabaseContext.getMonitoredElementDatabase().insert(monitoredElement);
					}
					catch (ApplicationException ae) {
						Log.errorException(ae);
					}

					return monitoredElement;
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
						throw new ObjectNotFoundException("MonitoredElement '" + id + "' not found in MServer database");
					throw new RetrieveObjectException("Cannot retrieve MonitoredElement '" + id + "' from MServer database -- " + are.message);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.resetMServerConnection();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
			}
			String mesg = "Remote reference for server is null; will try to reactivate it";
			Log.errorMessage(mesg);
			MeasurementControlModule.resetMServerConnection();
			throw new CommunicationException(mesg);
		}
	}





	public Collection loadKISsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		KISDatabase database = (KISDatabase) ConfigurationDatabaseContext.getKISDatabase();
		Collection objects;
		try {
			objects = database.retrieveButIdsByCondition(ids, condition);
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			String mesg = "MCMConfigurationObjectLoader.loadKISsButIds | Cannot load objects from database: " + ide.getMessage();
			throw new RetrieveObjectException(mesg, ide);
		}

		Identifier id;
		Collection loadButIds = new HashSet(ids);
		for (Iterator it = objects.iterator(); it.hasNext();) {
			id = ((StorableObject) it.next()).getId();
			loadButIds.add(id);
		}

		Identifier_Transferable[] loadButIdsT = Identifier.createTransferables(loadButIds);

		com.syrus.AMFICOM.mserver.corba.MServer mServerRef = MeasurementControlModule.mServerRef;
		if (mServerRef != null) {
			StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
			Collection loadedObjects = null;
			try {
				KIS_Transferable[] transferables = mServerRef.transmitKISsButIdsByCondition(loadButIdsT, conditionT);
				loadedObjects = new ArrayList(transferables.length);
				for (int i = 0; i < transferables.length; i++)
					loadedObjects.add(new KIS(transferables[i]));
			}
			catch (AMFICOMRemoteException are) {
				Log.errorMessage("Cannot retrieve measurements from MServer database -- " + are.message);
			}
			catch (CreateObjectException coe) {
				Log.errorException(coe);
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.resetMServerConnection();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}

			if (loadedObjects != null && !loadedObjects.isEmpty()) {
				objects.addAll(loadedObjects);

				try {
					database.insert(loadedObjects);
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
			}

		}
		else {
			Log.errorMessage("Remote reference for server is null; will try to reactivate it");
			MeasurementControlModule.resetMServerConnection();
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





	public Collection loadCableLinkTypes(Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Collection loadCableLinkTypesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Collection loadCableThreadTypes(Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Collection loadCableThreadTypesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Collection loadEquipmentTypes(Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Collection loadEquipmentTypesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Collection loadTransmissionPathTypes(Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Collection loadTransmissionPathTypesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Collection loadPortTypes(Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Collection loadLinkTypes(Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Collection loadLinkTypesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Collection loadPortTypesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Collection loadCableThreads(Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Collection loadCableThreadsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Collection loadEquipments(Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Collection loadEquipmentsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Collection loadKISs(Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Collection loadLinks(Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Collection loadLinksButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Collection loadMeasurementPorts(Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Collection loadMeasurementPortsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Collection loadMeasurementPortTypes(Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Collection loadMeasurementPortTypesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Collection loadMonitoredElements(Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Collection loadMonitoredElementsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Collection loadPorts(Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Collection loadPortsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Collection loadTransmissionPaths(Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Collection loadTransmissionPathsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
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
	


	public void saveCableLinkTypes(Collection objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}

	public void saveCableThreads(Collection objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}

	public void saveCableThreadTypes(Collection objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}

	public void saveEquipments(Collection objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	
	public void saveEquipmentTypes(Collection objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	
	public void saveKISs(Collection objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	
	public void saveLinks(Collection objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	
	public void saveLinkTypes(Collection objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	
	public void saveMeasurementPorts(Collection objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	
	public void saveMeasurementPortTypes(Collection objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	
	public void saveMonitoredElements(Collection objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	
	public void savePorts(Collection objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	
	public void savePortTypes(Collection objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	
	public void saveTransmissionPaths(Collection objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	public void saveTransmissionPathTypes(Collection objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}





	public Set refresh(Set storableObjects) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, objects: " + storableObjects);
	}





	public void delete(Collection objects) throws IllegalDataException {
		throw new UnsupportedOperationException("Method not implemented, objects: " + objects);
	}

	public void delete(Identifier id) throws IllegalDataException {
		throw new UnsupportedOperationException("Method not implemented, id: " + id);
	}

}
