/*
 * $Id: ConfigurationObjectLoader.java,v 1.12 2004/10/03 12:43:06 bob Exp $
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
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.VersionCollisionException;

/**
 * @version $Revision: 1.12 $, $Date: 2004/10/03 12:43:06 $
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

    /* Load Configuration StorableObject but argument ids */
    
    List loadCharacteristicTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

    List loadEquipmentTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

    List loadPortTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

    List loadMeasurementPortTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

    List loadCharacteristicsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

//  PermissionAttributes loadPermissionAttributesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

    List loadUsersButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

    List loadDomainsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

    List loadServersButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

    List loadMCMsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

    List loadEquipmentsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

    List loadPortsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

    List loadTransmissionPathsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

    List loadKISsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

    List loadMeasurementPortsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

    List loadMonitoredElementsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;
    
	public void saveCharacteristicType(CharacteristicType characteristicType, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveEquipmentType(EquipmentType equipmentType, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void savePortType(PortType portType, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveMeasurementPortType(MeasurementPortType measurementPortType, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveCharacteristic(Characteristic characteristic, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

//	public void savePermissionAttributes(PermissionAttributes permissionAttributes, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveUser(User user, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveDomain(Domain domain, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveServer(Server server, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveMCM(MCM mcm, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveEquipment(Equipment equipment, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void savePort(Port port, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveTransmissionPath(TransmissionPath transmissionPath, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveKIS(KIS kis, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveMeasurementPort(MeasurementPort measurementPort, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveMonitoredElement(MonitoredElement monitoredElement, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveCharacteristicTypes(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveEquipmentTypes(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void savePortTypes(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveMeasurementPortTypes(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveCharacteristics(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

//	public void savePermissionAttributes(PermissionAttributes permissionAttributes, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveUsers(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveDomains(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveServers(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveMCMs(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveEquipments(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void savePorts(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveTransmissionPaths(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveKISs(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveMeasurementPorts(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	public void saveMonitoredElements(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

}
