/*
 * $Id: DatabaseConfigurationObjectLoader.java,v 1.3 2004/08/16 08:17:26 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.DatabaseException;

/**
 * @version $Revision: 1.3 $, $Date: 2004/08/16 08:17:26 $
 * @author $Author: arseniy $
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

//	public MeasurementPort loadMeasurementPort(Identifier id) throws DatabaseException {
//		return new MeasurementPort(id);
//	}

	public MonitoredElement loadMonitoredElement(Identifier id) throws DatabaseException {
		return new MonitoredElement(id);
	}
}
