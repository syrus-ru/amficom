/*
 * $Id: ConfigurationObjectLoader.java,v 1.5 2004/09/14 15:49:09 max Exp $
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
 * @version $Revision: 1.5 $, $Date: 2004/09/14 15:49:09 $
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
    
    List loadCharacteristicTypes(List ids) throws DatabaseException, CommunicationException;

    List loadEquipmentTypes(List ids) throws DatabaseException, CommunicationException;

    List loadPortTypes(List ids) throws DatabaseException, CommunicationException;

    List loadMeasurementPortTypes(List ids) throws DatabaseException, CommunicationException;

    List loadCharacteristics(List ids) throws DatabaseException, CommunicationException;

//  PermissionAttributes loadPermissionAttributes(List ids) throws DatabaseException, CommunicationException;

    List loadUsers(List ids) throws DatabaseException, CommunicationException;

    List loadDomains(List ids) throws DatabaseException, CommunicationException;

    List loadServers(List ids) throws DatabaseException, CommunicationException;

    List loadMCMs(List ids) throws DatabaseException, CommunicationException;

    List loadEquipments(List ids) throws DatabaseException, CommunicationException;

    List loadPorts(List ids) throws DatabaseException, CommunicationException;

    List loadTransmissionPaths(List ids) throws DatabaseException, CommunicationException;

    List loadKISs(List ids) throws DatabaseException, CommunicationException;

    List loadMeasurementPorts(List ids) throws DatabaseException, CommunicationException;

    List loadMonitoredElements(List ids) throws DatabaseException, CommunicationException;
}
