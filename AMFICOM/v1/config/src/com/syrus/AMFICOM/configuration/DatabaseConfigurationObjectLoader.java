/*
 * $Id: DatabaseConfigurationObjectLoader.java,v 1.13 2004/10/22 10:23:41 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.List;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.13 $, $Date: 2004/10/22 10:23:41 $
 * @author $Author: max $
 * @module configuration_v1
 */

public class DatabaseConfigurationObjectLoader implements ConfigurationObjectLoader {

	public CharacteristicType loadCharacteristicType(Identifier id) throws DatabaseException {
		return new CharacteristicType(id);
	}

	public EquipmentType loadEquipmentType(Identifier id) throws DatabaseException {
		return new EquipmentType(id);
	}

	public PortType loadPortType(Identifier id) throws DatabaseException {
		return new PortType(id);
	}

	public MeasurementPortType loadMeasurementPortType(Identifier id) throws DatabaseException {
		return new MeasurementPortType(id);
	}

	public Characteristic loadCharacteristic(Identifier id) throws DatabaseException {
		return new Characteristic(id);
	}
    
    public KISType loadKISType(Identifier id) throws DatabaseException {
        return new KISType(id);
    }

//	public PermissionAttributes loadPermissionAttributes(Identifier id) throws DatabaseException {
//		return new PermissionAttributes(id);
//	}

	public User loadUser(Identifier id) throws DatabaseException {
		return new User(id);
	}

	public Domain loadDomain(Identifier id) throws DatabaseException {
		return new Domain(id);
	}

	public Server loadServer(Identifier id) throws DatabaseException {
		return new Server(id);
	}

	public MCM loadMCM(Identifier id) throws DatabaseException {
		return new MCM(id);
	}

	public Equipment loadEquipment(Identifier id) throws DatabaseException {
		return new Equipment(id);
	}

	public Port loadPort(Identifier id) throws DatabaseException {
		return new Port(id);
	}

	public TransmissionPath loadTransmissionPath(Identifier id) throws DatabaseException {
		return new TransmissionPath(id);
	}

	public KIS loadKIS(Identifier id) throws DatabaseException {
		return new KIS(id);
	}

	public MeasurementPort loadMeasurementPort(Identifier id) throws DatabaseException {
		return new MeasurementPort(id);
	}

	public MonitoredElement loadMonitoredElement(Identifier id) throws DatabaseException {
		return new MonitoredElement(id);
	}
    
    // for multiple objects
    public List loadCharacteristicTypes(List ids) throws DatabaseException {
        CharacteristicTypeDatabase database = (CharacteristicTypeDatabase)ConfigurationDatabaseContext.getCharacteristicTypeDatabase();
        List list = null;
        try {
            list = database.retrieveByIds(ids, null);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadCharacteristicTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadCharacteristicTypes | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }

    public List loadEquipmentTypes(List ids) throws DatabaseException {
        EquipmentTypeDatabase database = (EquipmentTypeDatabase)ConfigurationDatabaseContext.getEquipmentTypeDatabase(); 
        List list = null;
        try {
            list = database.retrieveByIds(ids, null);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseConfigurationObjectLoader.loadEquipmentTypes | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.loadEquipmentTypes | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }

    public List loadPortTypes(List ids) throws DatabaseException {
        PortTypeDatabase database = (PortTypeDatabase)ConfigurationDatabaseContext.getPortTypeDatabase();
        List list = null;
        try {
            list = database.retrieveByIds(ids, null);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseConfigurationObjectLoader.loadPortTypes | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.loadPortTypes | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }

    public List loadMeasurementPortTypes(List ids) throws DatabaseException {
        MeasurementPortTypeDatabase database = (MeasurementPortTypeDatabase)ConfigurationDatabaseContext.getMeasurementPortTypeDatabase();
        List list = null;
        try {
            list = database.retrieveByIds(ids, null);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseConfigurationObjectLoader.loadMeasurementPortTypes | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.loadMeasurementPortTypes | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }

    public List loadCharacteristics(List ids) throws DatabaseException {
        CharacteristicDatabase database = (CharacteristicDatabase)ConfigurationDatabaseContext.getCharacteristicDatabase();
        List list = null;
        try {
            list = database.retrieveByIds(ids, null);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseConfigurationObjectLoader.loadCharacteristics | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.loadCharacteristics | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }
    
    public List loadKISTypes(List ids) throws DatabaseException {
        KISTypeDatabase database = (KISTypeDatabase)ConfigurationDatabaseContext.getKISTypeDatabase();
        List list = null;
        try {
            list = database.retrieveByIds(ids, null);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseConfigurationObjectLoader.loadKISTypeDatabases | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.loadCharacteristics | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }

//  public PermissionAttributes loadPermissionAttributes(Identifier id) throws DatabaseException {
//      return new PermissionAttributes(id);
//  }

    public List loadUsers(List ids) throws DatabaseException {
        UserDatabase database = (UserDatabase)ConfigurationDatabaseContext.getUserDatabase();
        List list = null;
        try {
            list = database.retrieveByIds(ids, null);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseConfigurationObjectLoader.loadUsers | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.loadUsers | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }

    public List loadDomains(List ids) throws DatabaseException {
        DomainDatabase database = (DomainDatabase)ConfigurationDatabaseContext.getDomainDatabase();
        List list = null;
        try {
            list = database.retrieveByIds(ids, null);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseConfigurationObjectLoader.loadDomains | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.loadDomains | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }

    public List loadServers(List ids) throws DatabaseException {
        ServerDatabase database = (ServerDatabase)ConfigurationDatabaseContext.getServerDatabase();
        List list = null;
        try {
            list = database.retrieveByIds(ids, null);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseConfigurationObjectLoader.loadDomains | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.loadDomains | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }

    public List loadMCMs(List ids) throws DatabaseException {
        MCMDatabase database = (MCMDatabase)ConfigurationDatabaseContext.getMCMDatabase();
        List list = null;
        try {
            list = database.retrieveByIds(ids, null);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseConfigurationObjectLoader.loadMCMs | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.loadMCMs | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }

    public List loadEquipments(List ids) throws DatabaseException {
        EquipmentDatabase database = (EquipmentDatabase)ConfigurationDatabaseContext.getEquipmentDatabase();
        List list = null;
        try {
            list = database.retrieveByIds(ids, null);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseConfigurationObjectLoader.loadEquipments | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.loadEquipments | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }

    public List loadPorts(List ids) throws DatabaseException {
        PortDatabase database = (PortDatabase)ConfigurationDatabaseContext.getPortDatabase();
        List list = null;
        try {
            list = database.retrieveByIds(ids, null);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseConfigurationObjectLoader.loadPorts | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.loadPorts | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }

    public List loadTransmissionPaths(List ids) throws DatabaseException {
        TransmissionPathDatabase database = (TransmissionPathDatabase)ConfigurationDatabaseContext.getPortDatabase();
        List list = null;
        try {
            list = database.retrieveByIds(ids, null);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseConfigurationObjectLoader.loadTransmissionPaths | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.loadTransmissionPaths | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }

    public List loadKISs(List ids) throws DatabaseException {
        KISDatabase database = (KISDatabase)ConfigurationDatabaseContext.getKISDatabase();
        List list = null;
        try {
            list = database.retrieveByIds(ids, null);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseConfigurationObjectLoader.loadKISs | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.loadKISs | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }

    public List loadMeasurementPorts(List ids) throws DatabaseException {
        MeasurementPortDatabase database = (MeasurementPortDatabase)ConfigurationDatabaseContext.getMeasurementPortDatabase();
        List list = null;
        try {
            list = database.retrieveByIds(ids, null);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseConfigurationObjectLoader.loadMeasurementPorts | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.loadMeasurementPorts | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }

    public List loadMonitoredElements(List ids) throws DatabaseException{
        MonitoredElementDatabase database = (MonitoredElementDatabase)ConfigurationDatabaseContext.getMonitoredElementDatabase();
        List list = null;
        try {
            list = database.retrieveByIds(ids, null);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseConfigurationObjectLoader.loadMonitoredElements | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.loadMonitoredElements | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }

    /* Load Configuration StorableObject but argument ids */
    
    public List loadCharacteristicTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException {
        CharacteristicTypeDatabase database = (CharacteristicTypeDatabase)ConfigurationDatabaseContext.getCharacteristicTypeDatabase();
        List list = null;
        try {
            list = database.retrieveByCondition(ids, condition);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadCharacteristicTypesButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadCharacteristicTypesButIds | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }

    public List loadEquipmentTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException {
        EquipmentTypeDatabase database = (EquipmentTypeDatabase)ConfigurationDatabaseContext.getEquipmentTypeDatabase(); 
        List list = null;
        try {
        	list = database.retrieveByCondition(ids, condition);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseConfigurationObjectLoader.loadEquipmentTypesButIds | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.loadEquipmentTypesButIds | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }

    public List loadPortTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException {
        PortTypeDatabase database = (PortTypeDatabase)ConfigurationDatabaseContext.getPortTypeDatabase();
        List list = null;
        try {
        	list = database.retrieveByCondition(ids, condition);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseConfigurationObjectLoader.loadPortTypesButIds | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.loadPortTypesButIds | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }

    public List loadMeasurementPortTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException {
        MeasurementPortTypeDatabase database = (MeasurementPortTypeDatabase)ConfigurationDatabaseContext.getMeasurementPortTypeDatabase();
        List list = null;
        try {
        	list = database.retrieveByCondition(ids, condition);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseConfigurationObjectLoader.loadMeasurementPortTypesButIds | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.loadMeasurementPortTypesButIds | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }

    public List loadCharacteristicsButIds(StorableObjectCondition condition, List ids) throws DatabaseException {
        CharacteristicDatabase database = (CharacteristicDatabase)ConfigurationDatabaseContext.getCharacteristicDatabase();
        List list = null;
        try {
            list = database.retrieveByCondition(ids, condition);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseConfigurationObjectLoader.loadCharacteristicsButIds | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.loadCharacteristicsButIds | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }
    
    public List loadKISTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException {
        KISTypeDatabase database = (KISTypeDatabase)ConfigurationDatabaseContext.getKISTypeDatabase();
        List list = null;
        try {
            list = database.retrieveByCondition(ids, condition);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseConfigurationObjectLoader.loadKISTypeButIds | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.loadKISTypeButIds | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }

//  public PermissionAttributes loadPermissionAttributesButIds(Identifier id) throws DatabaseException {
//      return new PermissionAttributes(id);
//  }

    public List loadUsersButIds(StorableObjectCondition condition, List ids) throws DatabaseException {
        UserDatabase database = (UserDatabase)ConfigurationDatabaseContext.getUserDatabase();
        List list = null;
        try {
            list = database.retrieveByCondition(ids, condition);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseConfigurationObjectLoader.loadUsersButIds | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.loadUsersButIds | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }

    public List loadDomainsButIds(StorableObjectCondition condition, List ids) throws DatabaseException {
        DomainDatabase database = (DomainDatabase)ConfigurationDatabaseContext.getDomainDatabase();
        List list = null;
        try {
        	list = database.retrieveByCondition(ids, condition);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseConfigurationObjectLoader.loadDomainsButIds | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.loadDomainsButIds | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }

    public List loadServersButIds(StorableObjectCondition condition, List ids) throws DatabaseException {
        ServerDatabase database = (ServerDatabase)ConfigurationDatabaseContext.getServerDatabase();
        List list = null;
        try {
        	list = database.retrieveByCondition(ids, condition);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseConfigurationObjectLoader.loadDomainsButIds | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.loadDomainsButIds | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }

    public List loadMCMsButIds(StorableObjectCondition condition, List ids) throws DatabaseException {
        MCMDatabase database = (MCMDatabase)ConfigurationDatabaseContext.getMCMDatabase();
        List list = null;
        try {
        	list = database.retrieveByCondition(ids, condition);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseConfigurationObjectLoader.loadMCMsButIds | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.loadMCMsButIds | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }

    public List loadEquipmentsButIds(StorableObjectCondition condition, List ids) throws DatabaseException {
        EquipmentDatabase database = (EquipmentDatabase)ConfigurationDatabaseContext.getEquipmentDatabase();
        List list = null;
        try {
        	list = database.retrieveByCondition(ids, condition);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseConfigurationObjectLoader.loadEquipmentsButIds | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.loadEquipmentsButIds | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }

    public List loadPortsButIds(StorableObjectCondition condition, List ids) throws DatabaseException {
        PortDatabase database = (PortDatabase)ConfigurationDatabaseContext.getPortDatabase();
        List list = null;
        try {
        	list = database.retrieveByCondition(ids, condition);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseConfigurationObjectLoader.loadPortsButIds | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.loadPortsButIds | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }

    public List loadTransmissionPathsButIds(StorableObjectCondition condition, List ids) throws DatabaseException {
        TransmissionPathDatabase database = (TransmissionPathDatabase)ConfigurationDatabaseContext.getPortDatabase();
        List list = null;
        try {
        	list = database.retrieveByCondition(ids, condition);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseConfigurationObjectLoader.loadTransmissionPathsButIds | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.loadTransmissionPathsButIds | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }

    public List loadKISsButIds(StorableObjectCondition condition, List ids) throws DatabaseException {
        KISDatabase database = (KISDatabase)ConfigurationDatabaseContext.getKISDatabase();
        List list = null;
        try {
        	list = database.retrieveByCondition(ids, condition);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseConfigurationObjectLoader.loadKISsButIds | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.loadKISsButIds | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }

    public List loadMeasurementPortsButIds(StorableObjectCondition condition, List ids) throws DatabaseException {
        MeasurementPortDatabase database = (MeasurementPortDatabase)ConfigurationDatabaseContext.getMeasurementPortDatabase();
        List list = null;
        try {
        	list = database.retrieveByCondition(ids, condition);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseConfigurationObjectLoader.loadMeasurementPortsButIds | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.loadMeasurementPortsButIds | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }

    public List loadMonitoredElementsButIds(StorableObjectCondition condition, List ids) throws DatabaseException{
        MonitoredElementDatabase database = (MonitoredElementDatabase)ConfigurationDatabaseContext.getMonitoredElementDatabase();
        List list = null;
        try {
        	list = database.retrieveByCondition(ids, condition);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseConfigurationObjectLoader.loadMonitoredElementsButIds | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.loadMonitoredElementsButIds | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }

    
	public void saveCharacteristicType(CharacteristicType characteristicType, boolean force) throws DatabaseException, CommunicationException{
		CharacteristicTypeDatabase database = (CharacteristicTypeDatabase)ConfigurationDatabaseContext.getCharacteristicTypeDatabase();
		try {
			database.update(characteristicType, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveCharacteristicType | UpdateObjectException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveCharacteristicType | UpdateObjectException: " + e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveCharacteristicType | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveCharacteristicType | Illegal Storable Object: " + e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveCharacteristicType | VersionCollisionException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveCharacteristicType | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveEquipmentType(EquipmentType equipmentType, boolean force) throws DatabaseException, CommunicationException{
		EquipmentTypeDatabase database = (EquipmentTypeDatabase)ConfigurationDatabaseContext.getEquipmentTypeDatabase();
		try {
			database.update(equipmentType, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveEquipmentType | UpdateObjectException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveEquipmentType | UpdateObjectException: " + e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveEquipmentType | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveEquipmentType | Illegal Storable Object: " + e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveEquipmentType | VersionCollisionException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveEquipmentType | VersionCollisionException: " + e.getMessage());
		}
	}

	public void savePortType(PortType portType, boolean force) throws DatabaseException, CommunicationException{
		PortTypeDatabase database = (PortTypeDatabase)ConfigurationDatabaseContext.getPortTypeDatabase();
		try {
			database.update(portType, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.savePortType | UpdateObjectException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.savePortType | UpdateObjectException: " + e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.savePortType | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.savePortType | Illegal Storable Object: " + e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.savePortType | VersionCollisionException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.savePortType | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveMeasurementPortType(MeasurementPortType measurementPortType, boolean force) throws DatabaseException, CommunicationException{
		MeasurementPortTypeDatabase database = (MeasurementPortTypeDatabase)ConfigurationDatabaseContext.getMeasurementPortTypeDatabase();
		try {
			database.update(measurementPortType, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveMeasurementPortType | UpdateObjectException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveMeasurementPortType | UpdateObjectException: " + e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveMeasurementPortType | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveMeasurementPortType | Illegal Storable Object: " + e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveMeasurementPortType | VersionCollisionException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveMeasurementPortType | VersionCollisionException: " + e.getMessage());
		}
	}
    
    public void saveKISType(KISType kisType, boolean force) throws DatabaseException, CommunicationException{
        KISTypeDatabase database = (KISTypeDatabase)ConfigurationDatabaseContext.getKISTypeDatabase();
        try {
            database.update(kisType, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
        } catch (UpdateObjectException e) {
            Log.errorMessage("DatabaseConfigurationObjectLoader.saveKISType | UpdateObjectException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveKISType | UpdateObjectException: " + e.getMessage());
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseConfigurationObjectLoader.saveKISType | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveKISType | Illegal Storable Object: " + e.getMessage());
        } catch (VersionCollisionException e) {
            Log.errorMessage("DatabaseConfigurationObjectLoader.saveKISType | VersionCollisionException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveKISType | VersionCollisionException: " + e.getMessage());
        }
    }

	public void saveCharacteristic(Characteristic characteristic, boolean force) throws DatabaseException, CommunicationException{
		CharacteristicDatabase database = (CharacteristicDatabase)ConfigurationDatabaseContext.getCharacteristicDatabase();
		try {
			database.update(characteristic, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveCharacteristic | UpdateObjectException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveCharacteristic | UpdateObjectException: " + e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveCharacteristic | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveCharacteristic | Illegal Storable Object: " + e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveCharacteristic | VersionCollisionException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveCharacteristic | VersionCollisionException: " + e.getMessage());
		}
	}

//	public void savePermissionAttributes(PermissionAttributes permissionAttributes, boolean force) throws DatabaseException, CommunicationException;

	public void saveUser(User user, boolean force) throws DatabaseException, CommunicationException{
		UserDatabase database = (UserDatabase)ConfigurationDatabaseContext.getUserDatabase();
		try {
			database.update(user, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveUser | UpdateObjectException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveUser | UpdateObjectException: " + e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveUser | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveUser | Illegal Storable Object: " + e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveUser | VersionCollisionException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveUser | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveDomain(Domain domain, boolean force) throws DatabaseException, CommunicationException{
		DomainDatabase database = (DomainDatabase)ConfigurationDatabaseContext.getDomainDatabase();
		try {
			database.update(domain, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveDomain | UpdateObjectException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveDomain | UpdateObjectException: " + e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveDomain | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveDomain | Illegal Storable Object: " + e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveDomain | VersionCollisionException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveDomain | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveServer(Server server, boolean force) throws DatabaseException, CommunicationException{
		ServerDatabase database = (ServerDatabase)ConfigurationDatabaseContext.getServerDatabase();
		try {
			database.update(server, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveServer | UpdateObjectException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveServer | UpdateObjectException: " + e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveServer | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveServer | Illegal Storable Object: " + e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveServer | VersionCollisionException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveServer | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveMCM(MCM mcm, boolean force) throws DatabaseException, CommunicationException{
		MCMDatabase database = (MCMDatabase)ConfigurationDatabaseContext.getMCMDatabase();
		try {
			database.update(mcm, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveMCM | UpdateObjectException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveMCM | UpdateObjectException: " + e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveMCM | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveMCM | Illegal Storable Object: " + e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveMCM | VersionCollisionException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveMCM | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveEquipment(Equipment equipment, boolean force) throws DatabaseException, CommunicationException{
		EquipmentDatabase database = (EquipmentDatabase)ConfigurationDatabaseContext.getEquipmentDatabase();
		try {
			database.update(equipment, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveEquipment | UpdateObjectException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveEquipment | UpdateObjectException: " + e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveEquipment | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveEquipment | Illegal Storable Object: " + e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveEquipment | VersionCollisionException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveEquipment | VersionCollisionException: " + e.getMessage());
		}
	}

	public void savePort(Port port, boolean force) throws DatabaseException, CommunicationException{
		PortDatabase database = (PortDatabase)ConfigurationDatabaseContext.getPortDatabase();
		try {
			database.update(port, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.savePort | UpdateObjectException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.savePort | UpdateObjectException: " + e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.savePort | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.savePort | Illegal Storable Object: " + e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.savePort | VersionCollisionException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.savePort | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveTransmissionPath(TransmissionPath transmissionPath, boolean force) throws DatabaseException, CommunicationException{
		TransmissionPathDatabase database = (TransmissionPathDatabase)ConfigurationDatabaseContext.getTransmissionPathDatabase();
		try {
			database.update(transmissionPath, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveTransmissionPath | UpdateObjectException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveTransmissionPath | UpdateObjectException: " + e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveTransmissionPath | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveTransmissionPath | Illegal Storable Object: " + e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveTransmissionPath | VersionCollisionException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveTransmissionPath | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveKIS(KIS kis, boolean force) throws DatabaseException, CommunicationException{
		KISDatabase database = (KISDatabase)ConfigurationDatabaseContext.getKISDatabase();
		try {
			database.update(kis, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveKIS | UpdateObjectException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveKIS | UpdateObjectException: " + e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveKIS | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveKIS | Illegal Storable Object: " + e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveKIS | VersionCollisionException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveKIS | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveMeasurementPort(MeasurementPort measurementPort, boolean force) throws DatabaseException, CommunicationException{
		MeasurementPortDatabase database = (MeasurementPortDatabase)ConfigurationDatabaseContext.getMeasurementPortDatabase();
		try {
			database.update(measurementPort, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveMeasurementPort | UpdateObjectException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveMeasurementPort | UpdateObjectException: " + e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveMeasurementPort | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveMeasurementPort | Illegal Storable Object: " + e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveMeasurementPort | VersionCollisionException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveMeasurementPort | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveMonitoredElement(MonitoredElement monitoredElement, boolean force) throws DatabaseException, CommunicationException{
		MonitoredElementDatabase database = (MonitoredElementDatabase)ConfigurationDatabaseContext.getMonitoredElementDatabase();
		try {
			database.update(monitoredElement, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveMonitoredElement | UpdateObjectException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveMonitoredElement | UpdateObjectException: " + e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveMonitoredElement | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveMonitoredElement | Illegal Storable Object: " + e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveMonitoredElement | VersionCollisionException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveMonitoredElement | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveCharacteristicTypes(List list, boolean force) throws DatabaseException, CommunicationException{
		CharacteristicTypeDatabase database = (CharacteristicTypeDatabase)ConfigurationDatabaseContext.getCharacteristicTypeDatabase();
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveCharacteristicTypes | UpdateObjectException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveCharacteristicTypes | UpdateObjectException: " + e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveCharacteristicTypes | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveCharacteristicTypes | Illegal Storable Object: " + e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveCharacteristicTypes | VersionCollisionException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveCharacteristicTypes | VersionCollisionException: " + e.getMessage());
		}
	}


	public void saveEquipmentTypes(List list, boolean force) throws DatabaseException, CommunicationException{
		EquipmentTypeDatabase database = (EquipmentTypeDatabase)ConfigurationDatabaseContext.getEquipmentTypeDatabase();
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveEquipmentTypes | UpdateObjectException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveEquipmentTypes | UpdateObjectException: " + e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveEquipmentTypes | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveEquipmentTypes | Illegal Storable Object: " + e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveEquipmentTypes | VersionCollisionException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveEquipmentTypes | VersionCollisionException: " + e.getMessage());
		}
	}


	public void savePortTypes(List list, boolean force) throws DatabaseException, CommunicationException{
		PortTypeDatabase database = (PortTypeDatabase)ConfigurationDatabaseContext.getPortTypeDatabase();
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.savePortTypes | UpdateObjectException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.savePortTypes | UpdateObjectException: " + e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.savePortTypes | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.savePortTypes | Illegal Storable Object: " + e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.savePortTypes | VersionCollisionException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.savePortTypes | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveMeasurementPortTypes(List list, boolean force) throws DatabaseException, CommunicationException{
		MeasurementPortTypeDatabase database = (MeasurementPortTypeDatabase)ConfigurationDatabaseContext.getMeasurementPortTypeDatabase();
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveMeasurementPortTypes | UpdateObjectException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveMeasurementPortTypes | UpdateObjectException: " + e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveMeasurementPortTypes | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveMeasurementPortTypes | Illegal Storable Object: " + e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveMeasurementPortTypes | VersionCollisionException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveMeasurementPortTypes | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveCharacteristics(List list, boolean force) throws DatabaseException, CommunicationException{
		CharacteristicDatabase database = (CharacteristicDatabase)ConfigurationDatabaseContext.getCharacteristicDatabase();
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveCharacteristics | UpdateObjectException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveCharacteristics | UpdateObjectException: " + e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveCharacteristics | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveCharacteristics | Illegal Storable Object: " + e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveCharacteristics | VersionCollisionException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveCharacteristics | VersionCollisionException: " + e.getMessage());
		}
	}
    
    public void saveKISTypes(List list, boolean force) throws DatabaseException, CommunicationException{
        KISTypeDatabase database = (KISTypeDatabase)ConfigurationDatabaseContext.getKISTypeDatabase();
        try {
            database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
        } catch (UpdateObjectException e) {
            Log.errorMessage("DatabaseConfigurationObjectLoader.saveKISTypes | UpdateObjectException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveKISTypes | UpdateObjectException: " + e.getMessage());
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseConfigurationObjectLoader.saveKISTypes | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveKISTypes | Illegal Storable Object: " + e.getMessage());
        } catch (VersionCollisionException e) {
            Log.errorMessage("DatabaseConfigurationObjectLoader.saveKISTypes | VersionCollisionException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveKISTypes | VersionCollisionException: " + e.getMessage());
        }
    }


//	public void savePermissionAttributes(PermissionAttributes permissionAttributes, boolean force) throws DatabaseException, CommunicationException;

	public void saveUsers(List list, boolean force) throws DatabaseException, CommunicationException{
		UserDatabase database = (UserDatabase)ConfigurationDatabaseContext.getUserDatabase();
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveUsers | UpdateObjectException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveUsers | UpdateObjectException: " + e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveUsers | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveUsers | Illegal Storable Object: " + e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveUsers | VersionCollisionException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveUsers | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveDomains(List list, boolean force) throws DatabaseException, CommunicationException{
		DomainDatabase database = (DomainDatabase)ConfigurationDatabaseContext.getDomainDatabase();
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveDomains | UpdateObjectException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveDomains | UpdateObjectException: " + e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveDomains | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveDomains | Illegal Storable Object: " + e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveDomains | VersionCollisionException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveDomains | VersionCollisionException: " + e.getMessage());
		}
	}


	public void saveServers(List list, boolean force) throws DatabaseException, CommunicationException{
		ServerDatabase database = (ServerDatabase)ConfigurationDatabaseContext.getServerDatabase();
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveServers | UpdateObjectException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveServers | UpdateObjectException: " + e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveServers | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveServers | Illegal Storable Object: " + e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveServers | VersionCollisionException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveServers | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveMCMs(List list, boolean force) throws DatabaseException, CommunicationException{
		MCMDatabase database = (MCMDatabase)ConfigurationDatabaseContext.getMCMDatabase();
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveMCMs | UpdateObjectException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveMCMs | UpdateObjectException: " + e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveMCMs | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveMCMs | Illegal Storable Object: " + e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveMCMs | VersionCollisionException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveMCMs | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveEquipments(List list, boolean force) throws DatabaseException, CommunicationException{
		EquipmentDatabase database = (EquipmentDatabase)ConfigurationDatabaseContext.getEquipmentDatabase();
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveEquipments | UpdateObjectException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveEquipments | UpdateObjectException: " + e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveEquipments | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveEquipments | Illegal Storable Object: " + e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveEquipments | VersionCollisionException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveEquipments | VersionCollisionException: " + e.getMessage());
		}
	}

	public void savePorts(List list, boolean force) throws DatabaseException, CommunicationException{
		PortDatabase database = (PortDatabase)ConfigurationDatabaseContext.getPortDatabase();
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.savePorts | UpdateObjectException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.savePorts | UpdateObjectException: " + e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.savePorts | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.savePorts | Illegal Storable Object: " + e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.savePorts | VersionCollisionException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.savePorts | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveTransmissionPaths(List list, boolean force) throws DatabaseException, CommunicationException{
		TransmissionPathDatabase database = (TransmissionPathDatabase)ConfigurationDatabaseContext.getTransmissionPathDatabase();
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveTransmissionPaths | UpdateObjectException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveTransmissionPaths | UpdateObjectException: " + e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveTransmissionPaths | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveTransmissionPaths | Illegal Storable Object: " + e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveTransmissionPaths | VersionCollisionException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveTransmissionPaths | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveKISs(List list, boolean force) throws DatabaseException, CommunicationException{
		KISDatabase database = (KISDatabase)ConfigurationDatabaseContext.getKISDatabase();
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveKISs | UpdateObjectException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveKISs | UpdateObjectException: " + e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveKISs | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveKISs | Illegal Storable Object: " + e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveKISs | VersionCollisionException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveKISs | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveMeasurementPorts(List list, boolean force) throws DatabaseException, CommunicationException{
		MeasurementPortDatabase database = (MeasurementPortDatabase)ConfigurationDatabaseContext.getMeasurementPortDatabase();
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveMeasurementPorts | UpdateObjectException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveMeasurementPorts | UpdateObjectException: " + e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveMeasurementPorts | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveMeasurementPorts | Illegal Storable Object: " + e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveMeasurementPorts | VersionCollisionException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveMeasurementPorts | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveMonitoredElements(List list, boolean force) throws DatabaseException, CommunicationException{
		MonitoredElementDatabase database = (MonitoredElementDatabase)ConfigurationDatabaseContext.getMonitoredElementDatabase();
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveMonitoredElements | UpdateObjectException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveMonitoredElements | UpdateObjectException: " + e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveMonitoredElements | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveMonitoredElements | Illegal Storable Object: " + e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveMonitoredElements | VersionCollisionException: " + e.getMessage());
            throw new DatabaseException("DatabaseConfigurationObjectLoader.saveMonitoredElements | VersionCollisionException: " + e.getMessage());
		}
	}

}
