/*
 * $Id: DatabaseConfigurationObjectLoader.java,v 1.6 2004/09/14 15:49:09 max Exp $
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

/**
 * @version $Revision: 1.6 $, $Date: 2004/09/14 15:49:09 $
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
        return database.retrieveByIds(ids, null);
    }

    public List loadEquipmentTypes(List ids) throws DatabaseException {
        EquipmentTypeDatabase database = (EquipmentTypeDatabase)ConfigurationDatabaseContext.getEquipmentTypeDatabase(); 
        return database.retrieveByIds(ids, null);
    }

    public List loadPortTypes(List ids) throws DatabaseException {
        PortTypeDatabase database = (PortTypeDatabase)ConfigurationDatabaseContext.getPortTypeDatabase();
        return database.retrieveByIds(ids, null);
    }

    public List loadMeasurementPortTypes(List ids) throws DatabaseException {
        MeasurementPortTypeDatabase database = (MeasurementPortTypeDatabase)ConfigurationDatabaseContext.getMeasurementPortTypeDatabase();
        return database.retrieveByIds(ids, null);
    }

    public List loadCharacteristics(List ids) throws DatabaseException {
        CharacteristicDatabase database = (CharacteristicDatabase)ConfigurationDatabaseContext.getCharacteristicDatabase();
        return database.retrieveByIds(ids, null);
    }

//  public PermissionAttributes loadPermissionAttributes(Identifier id) throws DatabaseException {
//      return new PermissionAttributes(id);
//  }

    public List loadUsers(List ids) throws DatabaseException {
        UserDatabase database = (UserDatabase)ConfigurationDatabaseContext.getUserDatabase();
        return database.retrieveByIds(ids, null);
    }

    public List loadDomains(List ids) throws DatabaseException {
        DomainDatabase database = (DomainDatabase)ConfigurationDatabaseContext.getDomainDatabase();
        return database.retrieveByIds(ids, null);
    }

    public List loadServers(List ids) throws DatabaseException {
        ServerDatabase database = (ServerDatabase)ConfigurationDatabaseContext.getServerDatabase();
        return database.retrieveByIds(ids, null);
    }

    public List loadMCMs(List ids) throws DatabaseException {
        MCMDatabase database = (MCMDatabase)ConfigurationDatabaseContext.getMCMDatabase();
        return database.retrieveByIds(ids, null);
    }

    public List loadEquipments(List ids) throws DatabaseException {
        EquipmentDatabase database = (EquipmentDatabase)ConfigurationDatabaseContext.getEquipmentDatabase();
        return database.retrieveByIds(ids, null);
    }

    public List loadPorts(List ids) throws DatabaseException {
        PortDatabase database = (PortDatabase)ConfigurationDatabaseContext.getPortDatabase();
        return database.retrieveByIds(ids, null); 
    }

    public List loadTransmissionPaths(List ids) throws DatabaseException {
        TransmissionPathDatabase database = (TransmissionPathDatabase)ConfigurationDatabaseContext.getPortDatabase();
        return database.retrieveByIds(ids, null);
    }

    public List loadKISs(List ids) throws DatabaseException {
        KISDatabase database = (KISDatabase)ConfigurationDatabaseContext.getKISDatabase();
        return database.retrieveByIds(ids, null);
    }

    public List loadMeasurementPorts(List ids) throws DatabaseException {
        MeasurementPortDatabase database = (MeasurementPortDatabase)ConfigurationDatabaseContext.getMeasurementPortDatabase();
        return database.retrieveByIds(ids, null);
    }

    public List loadMonitoredElements(List ids) throws DatabaseException{
        MonitoredElementDatabase database = (MonitoredElementDatabase)ConfigurationDatabaseContext.getMonitoredElementDatabase();
        return database.retrieveByIds(ids, null);
    }
    
}
