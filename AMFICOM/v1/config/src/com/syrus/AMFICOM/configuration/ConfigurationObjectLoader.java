/*
 * $Id: ConfigurationObjectLoader.java,v 1.7 2004/09/28 06:44:07 bob Exp $
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

/**
 * @version $Revision: 1.7 $, $Date: 2004/09/28 06:44:07 $
 * @author $Author: bob $
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

	public void saveCharacteristicType(CharacteristicType characteristicType) throws DatabaseException, CommunicationException;

	public void saveEquipmentType(EquipmentType equipmentType) throws DatabaseException, CommunicationException;

	public void savePortType(PortType portType) throws DatabaseException, CommunicationException;

	public void saveMeasurementPortType(MeasurementPortType measurementPortType) throws DatabaseException, CommunicationException;

	public void saveCharacteristic(Characteristic characteristic) throws DatabaseException, CommunicationException;

//	public void savePermissionAttributes(PermissionAttributes permissionAttributes) throws DatabaseException, CommunicationException;

	public void saveUser(User user) throws DatabaseException, CommunicationException;

	public void saveDomain(Domain domain) throws DatabaseException, CommunicationException;

	public void saveServer(Server server) throws DatabaseException, CommunicationException;

	public void saveMCM(MCM mCM) throws DatabaseException, CommunicationException;

	public void saveEquipment(Equipment equipment) throws DatabaseException, CommunicationException;

	public void savePort(Port port) throws DatabaseException, CommunicationException;

	public void saveTransmissionPath(TransmissionPath transmissionPath) throws DatabaseException, CommunicationException;

	public void saveKIS(KIS kIS) throws DatabaseException, CommunicationException;

	public void saveMeasurementPort(MeasurementPort measurementPort) throws DatabaseException, CommunicationException;

	public void saveMonitoredElement(MonitoredElement monitoredElement) throws DatabaseException, CommunicationException;

	public void saveCharacteristicTypes(List list) throws DatabaseException, CommunicationException;

	public void saveEquipmentTypes(List list) throws DatabaseException, CommunicationException;

	public void savePortTypes(List list) throws DatabaseException, CommunicationException;

	public void saveMeasurementPortTypes(List list) throws DatabaseException, CommunicationException;

	public void saveCharacteristics(List list) throws DatabaseException, CommunicationException;

//	public void savePermissionAttributes(PermissionAttributes permissionAttributes) throws DatabaseException, CommunicationException;

	public void saveUsers(List list) throws DatabaseException, CommunicationException;

	public void saveDomains(List list) throws DatabaseException, CommunicationException;

	public void saveServers(List list) throws DatabaseException, CommunicationException;

	public void saveMCMs(List list) throws DatabaseException, CommunicationException;

	public void saveEquipments(List list) throws DatabaseException, CommunicationException;

	public void savePorts(List list) throws DatabaseException, CommunicationException;

	public void saveTransmissionPaths(List list) throws DatabaseException, CommunicationException;

	public void saveKISs(List list) throws DatabaseException, CommunicationException;

	public void saveMeasurementPorts(List list) throws DatabaseException, CommunicationException;

	public void saveMonitoredElements(List list) throws DatabaseException, CommunicationException;

}
