/*
 * $Id: MCMConfigurationObjectLoader.java,v 1.6 2004/09/28 10:31:15 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.configuration.Characteristic;
import com.syrus.AMFICOM.configuration.CharacteristicDatabase;
import com.syrus.AMFICOM.configuration.CharacteristicType;
import com.syrus.AMFICOM.configuration.CharacteristicTypeDatabase;
import com.syrus.AMFICOM.configuration.ConfigurationDatabaseContext;
import com.syrus.AMFICOM.configuration.ConfigurationObjectLoader;
import com.syrus.AMFICOM.configuration.Domain;
import com.syrus.AMFICOM.configuration.DomainDatabase;
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.EquipmentDatabase;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.EquipmentTypeDatabase;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.configuration.KISDatabase;
import com.syrus.AMFICOM.configuration.MCM;
import com.syrus.AMFICOM.configuration.MCMDatabase;
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
import com.syrus.AMFICOM.configuration.Server;
import com.syrus.AMFICOM.configuration.ServerDatabase;
import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.configuration.TransmissionPathDatabase;
import com.syrus.AMFICOM.configuration.User;
import com.syrus.AMFICOM.configuration.UserDatabase;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.6 $, $Date: 2004/09/28 10:31:15 $
 * @author $Author: bob $
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
				characteristicType = CharacteristicType.getInstance(MeasurementControlModule.mServerRef.transmitCharacteristicType((Identifier_Transferable)id.getTransferable()));
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
				equipmentType = EquipmentType.getInstance(MeasurementControlModule.mServerRef.transmitEquipmentType((Identifier_Transferable)id.getTransferable()));
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
				portType = PortType.getInstance(MeasurementControlModule.mServerRef.transmitPortType((Identifier_Transferable)id.getTransferable()));
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
				measurementPortType = MeasurementPortType.getInstance(MeasurementControlModule.mServerRef.transmitMeasurementPortType((Identifier_Transferable)id.getTransferable()));
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
				characteristic = Characteristic.getInstance(MeasurementControlModule.mServerRef.transmitCharacteristic((Identifier_Transferable)id.getTransferable()));
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
				user = User.getInstance(MeasurementControlModule.mServerRef.transmitUser((Identifier_Transferable)id.getTransferable()));
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
				domain = Domain.getInstance(MeasurementControlModule.mServerRef.transmitDomain((Identifier_Transferable)id.getTransferable()));
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
				server = Server.getInstance(MeasurementControlModule.mServerRef.transmitServer((Identifier_Transferable)id.getTransferable()));
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
				mcm = MCM.getInstance(MeasurementControlModule.mServerRef.transmitMCM((Identifier_Transferable)id.getTransferable()));
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
				equipment = Equipment.getInstance(MeasurementControlModule.mServerRef.transmitEquipment((Identifier_Transferable)id.getTransferable()));
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
				port = Port.getInstance(MeasurementControlModule.mServerRef.transmitPort((Identifier_Transferable)id.getTransferable()));
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
				transmissionPath = TransmissionPath.getInstance(MeasurementControlModule.mServerRef.transmitTransmissionPath((Identifier_Transferable)id.getTransferable()));
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
				kis = KIS.getInstance(MeasurementControlModule.mServerRef.transmitKIS((Identifier_Transferable)id.getTransferable()));
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
				measurementPort = MeasurementPort.getInstance(MeasurementControlModule.mServerRef.transmitMeasurementPort((Identifier_Transferable)id.getTransferable()));
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
				monitoredElement = MonitoredElement.getInstance(MeasurementControlModule.mServerRef.transmitMonitoredElement((Identifier_Transferable)id.getTransferable()));
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
    
	public List loadCharacteristics(List ids) throws DatabaseException,
			CommunicationException {
		CharacteristicDatabase database = (CharacteristicDatabase)ConfigurationDatabaseContext.getCharacteristicDatabase();
        List list;
        List copyOfList;
        Characteristic characteristic;
        try {
            list = database.retrieveByIds(ids, null);
            copyOfList = new LinkedList(list);
            for (Iterator it = copyOfList.iterator(); it.hasNext();) {
                Identifier id = ((StorableObject) it.next()).getId();
                if(ids.contains(id)) it.remove();
            }
            for (Iterator it = copyOfList.iterator(); it.hasNext();) {
                Identifier id = ((StorableObject) it.next()).getId();
                Log.debugMessage("Characteristic '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
                try {
                    characteristic = Characteristic.getInstance(MeasurementControlModule.mServerRef.transmitCharacteristic((Identifier_Transferable)id.getTransferable()));
                    list.add(characteristic);
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
        } catch (IllegalDataException e) {
            Log.errorMessage("MCMConfigurationObjectLoader.loadMeasurementSetups | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("MCMConfigurationObjectLoader.loadMeasurementSetups | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }
    
	public List loadCharacteristicTypes(List ids) throws DatabaseException,
			CommunicationException {
		CharacteristicTypeDatabase database = (CharacteristicTypeDatabase)ConfigurationDatabaseContext.getCharacteristicTypeDatabase();
        List list;
        List copyOfList;
        CharacteristicType characteristicType;
        try {
            list = database.retrieveByIds(ids, null);
            copyOfList = new LinkedList(list);
            for (Iterator it = copyOfList.iterator(); it.hasNext();) {
                Identifier id = ((StorableObject) it.next()).getId();
                if(ids.contains(id)) it.remove();
            }
            for (Iterator it = copyOfList.iterator(); it.hasNext();) {
                Identifier id = ((StorableObject) it.next()).getId();
                Log.debugMessage("Characteristic type '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
                try {
                    characteristicType = CharacteristicType.getInstance(MeasurementControlModule.mServerRef.transmitCharacteristicType((Identifier_Transferable)id.getTransferable()));
                    list.add(characteristicType);
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
        } catch (IllegalDataException e) {
            Log.errorMessage("MCMConfigurationObjectLoader.loadCharacteristicTypes | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("MCMConfigurationObjectLoader.loadCharacteristicTypes | Illegal Storable Object: " + e.getMessage());
        }
        return list;
	}
    
	public List loadDomains(List ids) throws DatabaseException,
			CommunicationException {
		DomainDatabase database = (DomainDatabase)ConfigurationDatabaseContext.getDomainDatabase();
        List list;
        List copyOfList;
        Domain domain;
        try {
            list = database.retrieveByIds(ids, null);
            copyOfList = new LinkedList(list);
            for (Iterator it = copyOfList.iterator(); it.hasNext();) {
                Identifier id = ((StorableObject) it.next()).getId();
                if(ids.contains(id)) it.remove();
            }
            for (Iterator it = copyOfList.iterator(); it.hasNext();) {
                Identifier id = ((StorableObject) it.next()).getId();
                Log.debugMessage("Domain '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
                try {
                    domain = Domain.getInstance(MeasurementControlModule.mServerRef.transmitDomain((Identifier_Transferable)id.getTransferable()));
                    list.add(domain);
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
        } catch (IllegalDataException e) {
            Log.errorMessage("MCMConfigurationObjectLoader.loadDomains | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("MCMConfigurationObjectLoader.loadDomains | Illegal Storable Object: " + e.getMessage());
        }
        return list;
	}
    
	public List loadEquipments(List ids) throws DatabaseException,
			CommunicationException {
		EquipmentDatabase database = (EquipmentDatabase)ConfigurationDatabaseContext.getEquipmentDatabase();
        List list;
        List copyOfList;
        Equipment equipment;
        try {
            list = database.retrieveByIds(ids, null);
            copyOfList = new LinkedList(list);
            for (Iterator it = copyOfList.iterator(); it.hasNext();) {
                Identifier id = ((StorableObject) it.next()).getId();
                if(ids.contains(id)) it.remove();
            }
            for (Iterator it = copyOfList.iterator(); it.hasNext();) {
                Identifier id = ((StorableObject) it.next()).getId();
                Log.debugMessage("Equipment '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
                try {
                    equipment = Equipment.getInstance(MeasurementControlModule.mServerRef.transmitEquipment((Identifier_Transferable)id.getTransferable()));
                    list.add(equipment);
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
            }
        } catch (IllegalDataException e) {
            Log.errorMessage("MCMConfigurationObjectLoader.loadEquipments | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("MCMConfigurationObjectLoader.loadEquipments | Illegal Storable Object: " + e.getMessage());
        }
        return list;
	}
    
	public List loadEquipmentTypes(List ids) throws DatabaseException,
			CommunicationException {
		EquipmentTypeDatabase database = (EquipmentTypeDatabase)ConfigurationDatabaseContext.getEquipmentTypeDatabase();
        List list;
        List copyOfList;
        EquipmentType equipmentType;
        try {
            list = database.retrieveByIds(ids, null);
            copyOfList = new LinkedList(list);
            for (Iterator it = copyOfList.iterator(); it.hasNext();) {
                Identifier id = ((StorableObject) it.next()).getId();
                if(ids.contains(id)) it.remove();
            }
            for (Iterator it = copyOfList.iterator(); it.hasNext();) {
                Identifier id = ((StorableObject) it.next()).getId();
                Log.debugMessage("Equipment type '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
                try {
                    equipmentType = EquipmentType.getInstance(MeasurementControlModule.mServerRef.transmitEquipmentType((Identifier_Transferable)id.getTransferable()));
                    list.add(equipmentType);
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
            }
        } catch (IllegalDataException e) {
            Log.errorMessage("MCMConfigurationObjectLoader.loadEquipmentTypes | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("MCMConfigurationObjectLoader.loadEquipmentTypes | Illegal Storable Object: " + e.getMessage());
        }
        return list;
	}
    
	public List loadKISs(List ids) throws DatabaseException,
			CommunicationException {
		KISDatabase database = (KISDatabase)ConfigurationDatabaseContext.getKISDatabase();
        List list;
        List copyOfList;
        KIS kis;
        try {
            list = database.retrieveByIds(ids, null);
            copyOfList = new LinkedList(list);
            for (Iterator it = copyOfList.iterator(); it.hasNext();) {
                Identifier id = ((StorableObject) it.next()).getId();
                if(ids.contains(id)) it.remove();
            }
            for (Iterator it = copyOfList.iterator(); it.hasNext();) {
                Identifier id = ((StorableObject) it.next()).getId();
                Log.debugMessage("KIS '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
                try {
                    kis = KIS.getInstance(MeasurementControlModule.mServerRef.transmitKIS((Identifier_Transferable)id.getTransferable()));
                    list.add(kis);
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
            }
        } catch (IllegalDataException e) {
            Log.errorMessage("MCMConfigurationObjectLoader.loadKISs | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("MCMConfigurationObjectLoader.loadKISs | Illegal Storable Object: " + e.getMessage());
        }
        return list;
	}
    
	public List loadMCMs(List ids) throws DatabaseException,
			CommunicationException {
		MCMDatabase database = (MCMDatabase)ConfigurationDatabaseContext.getMCMDatabase();
        List list;
        List copyOfList;
        MCM mcm;
        try {
            list = database.retrieveByIds(ids, null);
            copyOfList = new LinkedList(list);
            for (Iterator it = copyOfList.iterator(); it.hasNext();) {
                Identifier id = ((StorableObject) it.next()).getId();
                if(ids.contains(id)) it.remove();
            }
            for (Iterator it = copyOfList.iterator(); it.hasNext();) {
                Identifier id = ((StorableObject) it.next()).getId();
                Log.debugMessage("MCM '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
                try {
                    mcm = MCM.getInstance(MeasurementControlModule.mServerRef.transmitMCM((Identifier_Transferable)id.getTransferable()));
                    list.add(mcm);
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
        } catch (IllegalDataException e) {
            Log.errorMessage("MCMConfigurationObjectLoader.loadKISs | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("MCMConfigurationObjectLoader.loadKISs | Illegal Storable Object: " + e.getMessage());
        }
        return list;
	}
    
	public List loadMeasurementPorts(List ids) throws DatabaseException,
			CommunicationException {
		MeasurementPortDatabase database = (MeasurementPortDatabase)ConfigurationDatabaseContext.getMeasurementPortDatabase();
        List list;
        List copyOfList;
        MeasurementPort measurementPort;
        try {
            list = database.retrieveByIds(ids, null);
            copyOfList = new LinkedList(list);
            for (Iterator it = copyOfList.iterator(); it.hasNext();) {
                Identifier id = ((StorableObject) it.next()).getId();
                if(ids.contains(id)) it.remove();
            }
            for (Iterator it = copyOfList.iterator(); it.hasNext();) {
                Identifier id = ((StorableObject) it.next()).getId();
                Log.debugMessage("Measurement port '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
                try {
                    measurementPort = MeasurementPort.getInstance(MeasurementControlModule.mServerRef.transmitMeasurementPort((Identifier_Transferable)id.getTransferable()));
                    list.add(measurementPort);
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
            }
        } catch (IllegalDataException e) {
            Log.errorMessage("MCMConfigurationObjectLoader.loadMeasurementPorts | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("MCMConfigurationObjectLoader.loadMeasurementPorts | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }
    
	public List loadMeasurementPortTypes(List ids) throws DatabaseException,
			CommunicationException {
		MeasurementPortTypeDatabase database = (MeasurementPortTypeDatabase)ConfigurationDatabaseContext.getMeasurementPortTypeDatabase();
        List list;
        List copyOfList;
        MeasurementPortType measurementPortType;
        try {
            list = database.retrieveByIds(ids, null);
            copyOfList = new LinkedList(list);
            for (Iterator it = copyOfList.iterator(); it.hasNext();) {
                Identifier id = ((StorableObject) it.next()).getId();
                if(ids.contains(id)) it.remove();
            }
            for (Iterator it = copyOfList.iterator(); it.hasNext();) {
                Identifier id = ((StorableObject) it.next()).getId();
                Log.debugMessage("Measurement port type '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
                try {
                    measurementPortType = MeasurementPortType.getInstance(MeasurementControlModule.mServerRef.transmitMeasurementPortType((Identifier_Transferable)id.getTransferable()));
                    list.add(measurementPortType);
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
        } catch (IllegalDataException e) {
            Log.errorMessage("MCMConfigurationObjectLoader.loadMeasurementPortTypes | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("MCMConfigurationObjectLoader.loadMeasurementPortTypes | Illegal Storable Object: " + e.getMessage());
        }
        return list;
	}
    
	public List loadMonitoredElements(List ids) throws DatabaseException,
			CommunicationException {
		MonitoredElementDatabase database = (MonitoredElementDatabase)ConfigurationDatabaseContext.getMonitoredElementDatabase();
        List list;
        List copyOfList;
        MonitoredElement monitoredElement;
        try {
            list = database.retrieveByIds(ids, null);
            copyOfList = new LinkedList(list);
            for (Iterator it = copyOfList.iterator(); it.hasNext();) {
                Identifier id = ((StorableObject) it.next()).getId();
                if(ids.contains(id)) it.remove();
            }
            for (Iterator it = copyOfList.iterator(); it.hasNext();) {
                Identifier id = ((StorableObject) it.next()).getId();
                Log.debugMessage("Monitored element '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
                try {
                    monitoredElement = MonitoredElement.getInstance(MeasurementControlModule.mServerRef.transmitMonitoredElement((Identifier_Transferable)id.getTransferable()));
                    list.add(monitoredElement);
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
        } catch (IllegalDataException e) {
            Log.errorMessage("MCMConfigurationObjectLoader.loadMonitoredElements | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("MCMConfigurationObjectLoader.loadMonitoredElements | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }
    
	public List loadPorts(List ids) throws DatabaseException,
			CommunicationException {
		PortDatabase database = (PortDatabase)ConfigurationDatabaseContext.getPortDatabase();
        List list;
        List copyOfList;
        Port port;
        try {
            list = database.retrieveByIds(ids, null);
            copyOfList = new LinkedList(list);
            for (Iterator it = copyOfList.iterator(); it.hasNext();) {
                Identifier id = ((StorableObject) it.next()).getId();
                if(ids.contains(id)) it.remove();
            }
            for (Iterator it = copyOfList.iterator(); it.hasNext();) {
                Identifier id = ((StorableObject) it.next()).getId();
                Log.debugMessage("Port '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
                try {
                    port = Port.getInstance(MeasurementControlModule.mServerRef.transmitPort((Identifier_Transferable)id.getTransferable()));
                    list.add(port);
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
        } catch (IllegalDataException e) {
            Log.errorMessage("MCMConfigurationObjectLoader.loadPorts | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("MCMConfigurationObjectLoader.loadPorts | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }
    
	public List loadPortTypes(List ids) throws DatabaseException,
			CommunicationException {
		PortTypeDatabase database = (PortTypeDatabase)ConfigurationDatabaseContext.getPortTypeDatabase();
        List list;
        List copyOfList;
        PortType portType;
        try {
            list = database.retrieveByIds(ids, null);
            copyOfList = new LinkedList(list);
            for (Iterator it = copyOfList.iterator(); it.hasNext();) {
                Identifier id = ((StorableObject) it.next()).getId();
                if(ids.contains(id)) it.remove();
            }
            for (Iterator it = copyOfList.iterator(); it.hasNext();) {
                Identifier id = ((StorableObject) it.next()).getId();
                Log.debugMessage("Port type '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
                try {
                    portType = PortType.getInstance(MeasurementControlModule.mServerRef.transmitPortType((Identifier_Transferable)id.getTransferable()));
                    list.add(portType);
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
        } catch (IllegalDataException e) {
            Log.errorMessage("MCMConfigurationObjectLoader.loadPortTypes | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("MCMConfigurationObjectLoader.loadPortTypes | Illegal Storable Object: " + e.getMessage());
        }
        return list;
	}
    
	public List loadServers(List ids) throws DatabaseException,
			CommunicationException {
		ServerDatabase database = (ServerDatabase)ConfigurationDatabaseContext.getServerDatabase();
        List list;
        List copyOfList;
        Server server;
        try {
            list = database.retrieveByIds(ids, null);
            copyOfList = new LinkedList(list);
            for (Iterator it = copyOfList.iterator(); it.hasNext();) {
                Identifier id = ((StorableObject) it.next()).getId();
                if(ids.contains(id)) it.remove();
            }
            for (Iterator it = copyOfList.iterator(); it.hasNext();) {
                Identifier id = ((StorableObject) it.next()).getId();
                Log.debugMessage("Server '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
                try {
                    server = Server.getInstance(MeasurementControlModule.mServerRef.transmitServer((Identifier_Transferable)id.getTransferable()));
                    list.add(server);
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
        } catch (IllegalDataException e) {
            Log.errorMessage("MCMConfigurationObjectLoader.loadServers | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("MCMConfigurationObjectLoader.loadServers | Illegal Storable Object: " + e.getMessage());
        }
        return list;
	}
    
	public List loadTransmissionPaths(List ids) throws DatabaseException,
			CommunicationException {
		TransmissionPathDatabase database = (TransmissionPathDatabase)ConfigurationDatabaseContext.getTransmissionPathDatabase();
        List list;
        List copyOfList;
        TransmissionPath transmissionPath;
        try {
            list = database.retrieveByIds(ids, null);
            copyOfList = new LinkedList(list);
            for (Iterator it = copyOfList.iterator(); it.hasNext();) {
                Identifier id = ((StorableObject) it.next()).getId();
                if(ids.contains(id)) it.remove();
            }
            for (Iterator it = copyOfList.iterator(); it.hasNext();) {
                Identifier id = ((StorableObject) it.next()).getId();
                Log.debugMessage("Transmission path '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
                try {
                    transmissionPath = TransmissionPath.getInstance(MeasurementControlModule.mServerRef.transmitTransmissionPath((Identifier_Transferable)id.getTransferable()));
                    list.add(transmissionPath);
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
            }
        } catch (IllegalDataException e) {
            Log.errorMessage("MCMConfigurationObjectLoader.loadTransmissionPaths | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("MCMConfigurationObjectLoader.loadTransmissionPaths | Illegal Storable Object: " + e.getMessage());
        }
        return list;
	}
    
	public List loadUsers(List ids) throws DatabaseException,
			CommunicationException {
		UserDatabase database = (UserDatabase)ConfigurationDatabaseContext.getUserDatabase();
        List list;
        List copyOfList;
        User user;
        try {
            list = database.retrieveByIds(ids, null);
            copyOfList = new LinkedList(list);
            for (Iterator it = copyOfList.iterator(); it.hasNext();) {
                Identifier id = ((StorableObject) it.next()).getId();
                if(ids.contains(id)) it.remove();
            }
            for (Iterator it = copyOfList.iterator(); it.hasNext();) {
                Identifier id = ((StorableObject) it.next()).getId();
                Log.debugMessage("User '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
                try {
                    user = User.getInstance(MeasurementControlModule.mServerRef.transmitUser((Identifier_Transferable)id.getTransferable()));
                    list.add(user);
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
        } catch (IllegalDataException e) {
            Log.errorMessage("MCMConfigurationObjectLoader.loadUsers | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("MCMConfigurationObjectLoader.loadUsers | Illegal Storable Object: " + e.getMessage());
        }
        return list;
	}
	
	public void saveCharacteristicType(CharacteristicType characteristicType, boolean force) throws DatabaseException, CommunicationException {
//		 TODO method isn't complete
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

		public void saveCharacteristic(Characteristic characteristic, boolean force) throws DatabaseException, CommunicationException {
//		 TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
		}

//			public void savePermissionAttributes(PermissionAttributes permissionAttributes, boolean force) throws DatabaseException, CommunicationException {
//		 TODO method isn't complete
//		throw new UnsupportedOperationException("method isn't complete");
//		}

		public void saveUser(User user, boolean force) throws DatabaseException, CommunicationException {
//		 TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
		}

		public void saveDomain(Domain domain, boolean force) throws DatabaseException, CommunicationException {
//		 TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
		}

		public void saveServer(Server server, boolean force) throws DatabaseException, CommunicationException {
//		 TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
		}

		public void saveMCM(MCM mcm, boolean force) throws DatabaseException, CommunicationException {
//		 TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
		}

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

		public void saveCharacteristicTypes(List list, boolean force) throws DatabaseException, CommunicationException {
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

		public void saveCharacteristics(List list, boolean force) throws DatabaseException, CommunicationException {
//		 TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
		}

//			public void savePermissionAttributes(PermissionAttributes permissionAttributes, boolean force) throws DatabaseException, CommunicationException {
//		 TODO method isn't complete
//		throw new UnsupportedOperationException("method isn't complete");
//		}

		public void saveUsers(List list, boolean force) throws DatabaseException, CommunicationException {
//		 TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
		}

		public void saveDomains(List list, boolean force) throws DatabaseException, CommunicationException {
//		 TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
		}

		public void saveServers(List list, boolean force) throws DatabaseException, CommunicationException {
//		 TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
		}

		public void saveMCMs(List list, boolean force) throws DatabaseException, CommunicationException {
//		 TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
		}

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

}
