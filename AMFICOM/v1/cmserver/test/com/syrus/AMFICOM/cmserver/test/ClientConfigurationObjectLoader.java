/*
 * $Id: ClientConfigurationObjectLoader.java,v 1.1 2004/09/22 07:27:52 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver.test;

import java.util.List;

import com.syrus.AMFICOM.cmserver.corba.CMServer;
import com.syrus.AMFICOM.configuration.Characteristic;
import com.syrus.AMFICOM.configuration.CharacteristicType;
import com.syrus.AMFICOM.configuration.ConfigurationObjectLoader;
import com.syrus.AMFICOM.configuration.Domain;
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.configuration.MCM;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.Port;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.Server;
import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.configuration.User;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.RetrieveObjectException;

/**
 * @version $Revision: 1.1 $, $Date: 2004/09/22 07:27:52 $
 * @author $Author: bob $
 * @module mcm_v1
 */

public final class ClientConfigurationObjectLoader implements ConfigurationObjectLoader {

	private CMServer	server;

	public ClientConfigurationObjectLoader(CMServer server) {
		this.server = server;
	}

	public CharacteristicType loadCharacteristicType(Identifier id) throws RetrieveObjectException,
			CommunicationException {
		throw new UnsupportedOperationException();
	}

	public EquipmentType loadEquipmentType(Identifier id) throws RetrieveObjectException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public PortType loadPortType(Identifier id) throws RetrieveObjectException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public MeasurementPortType loadMeasurementPortType(Identifier id) throws RetrieveObjectException,
			CommunicationException {
		throw new UnsupportedOperationException();
	}

	public Characteristic loadCharacteristic(Identifier id) throws RetrieveObjectException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	//	public PermissionAttributes loadPermissionAttributes(Identifier id)
	// throws DatabaseException {
	//		return new PermissionAttributes(id);
	//	}

	public User loadUser(Identifier id) throws RetrieveObjectException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public Domain loadDomain(Identifier id) throws RetrieveObjectException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public Server loadServer(Identifier id) throws RetrieveObjectException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public MCM loadMCM(Identifier id) throws RetrieveObjectException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public Equipment loadEquipment(Identifier id) throws RetrieveObjectException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public Port loadPort(Identifier id) throws RetrieveObjectException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public TransmissionPath loadTransmissionPath(Identifier id) throws RetrieveObjectException,
			CommunicationException {
		throw new UnsupportedOperationException();
	}

	public KIS loadKIS(Identifier id) throws RetrieveObjectException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public MeasurementPort loadMeasurementPort(Identifier id) throws RetrieveObjectException,
			CommunicationException {
		throw new UnsupportedOperationException();
	}

	public MonitoredElement loadMonitoredElement(Identifier id) throws RetrieveObjectException,
			CommunicationException {
		throw new UnsupportedOperationException();
	}

	public List loadCharacteristics(List ids) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public List loadCharacteristicTypes(List ids) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public List loadDomains(List ids) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public List loadEquipments(List ids) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public List loadEquipmentTypes(List ids) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public List loadKISs(List ids) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public List loadMCMs(List ids) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public List loadMeasurementPorts(List ids) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public List loadMeasurementPortTypes(List ids) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public List loadMonitoredElements(List ids) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public List loadPorts(List ids) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public List loadPortTypes(List ids) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public List loadServers(List ids) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public List loadTransmissionPaths(List ids) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public List loadUsers(List ids) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}
}
