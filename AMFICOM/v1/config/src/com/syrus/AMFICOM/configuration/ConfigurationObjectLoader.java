/*
 * $Id: ConfigurationObjectLoader.java,v 1.4 2004/09/14 14:53:47 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.IllegalDataException;

/**
 * @version $Revision: 1.4 $, $Date: 2004/09/14 14:53:47 $
 * @author $Author: max $
 * @module configuration_v1
 */

public interface ConfigurationObjectLoader {

	CharacteristicType loadCharacteristicType(Identifier id) throws DatabaseException, CommunicationException;

	EquipmentType loadEquipmentType(Identifier id) throws DatabaseException, CommunicationException;

	PortType loadPortType(Identifier id) throws DatabaseException, CommunicationException;

	MeasurementPortType loadMeasurementPortType(Identifier id) throws DatabaseException, CommunicationException;

	Characteristic loadCharacteristic(Identifier id) throws DatabaseException, CommunicationException;

//	PermissionAttributes loadPermissionAttributes(Identifier id) throws DatabaseException, CommunicationException;

	User loadUser(Identifier id) throws DatabaseException, CommunicationException;

	Domain loadDomain(Identifier id) throws DatabaseException, CommunicationException;

	Server loadServer(Identifier id) throws DatabaseException, CommunicationException;

	MCM loadMCM(Identifier id) throws DatabaseException, CommunicationException;

	Equipment loadEquipment(Identifier id) throws DatabaseException, CommunicationException;

	Port loadPort(Identifier id) throws DatabaseException, CommunicationException;

	TransmissionPath loadTransmissionPath(Identifier id) throws DatabaseException, CommunicationException;

	KIS loadKIS(Identifier id) throws DatabaseException, CommunicationException;

	MeasurementPort loadMeasurementPort(Identifier id) throws DatabaseException, CommunicationException;

	MonitoredElement loadMonitoredElement(Identifier id) throws DatabaseException, CommunicationException;
    
    // this block for multiple objects
    
    List loadCharacteristicTypes(List ids) throws DatabaseException, CommunicationException, IllegalDataException;

    List loadEquipmentTypes(List ids) throws DatabaseException, CommunicationException, IllegalDataException;

    List loadPortTypes(List ids) throws DatabaseException, CommunicationException, IllegalDataException;

    List loadMeasurementPortTypes(List ids) throws DatabaseException, CommunicationException, IllegalDataException;

    List loadCharacteristics(List ids) throws DatabaseException, CommunicationException, IllegalDataException;

//  PermissionAttributes loadPermissionAttributes(List ids) throws DatabaseException, CommunicationException;

    List loadUsers(List ids) throws DatabaseException, CommunicationException, IllegalDataException;

    List loadDomains(List ids) throws DatabaseException, CommunicationException, IllegalDataException;

    List loadServers(List ids) throws DatabaseException, CommunicationException, IllegalDataException;

    List loadMCMs(List ids) throws DatabaseException, CommunicationException, IllegalDataException;

    List loadEquipments(List ids) throws DatabaseException, CommunicationException, IllegalDataException;

    List loadPorts(List ids) throws DatabaseException, CommunicationException, IllegalDataException;

    List loadTransmissionPaths(List ids) throws DatabaseException, CommunicationException, IllegalDataException;

    List loadKISs(List ids) throws DatabaseException, CommunicationException, IllegalDataException;

    List loadMeasurementPorts(List ids) throws DatabaseException, CommunicationException, IllegalDataException;

    List loadMonitoredElements(List ids) throws DatabaseException, CommunicationException, IllegalDataException;
}
