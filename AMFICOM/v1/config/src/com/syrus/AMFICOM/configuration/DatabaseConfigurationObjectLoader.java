/*
 * $Id: DatabaseConfigurationObjectLoader.java,v 1.8 2004/09/15 10:40:40 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.8 $, $Date: 2004/09/15 10:40:40 $
 * @author $Author: max $
 * @module configuration_v1
 */

public class DatabaseConfigurationObjectLoader implements ConfigurationObjectLoader {

	public DatabaseConfigurationObjectLoader() {
	}

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
    
}
