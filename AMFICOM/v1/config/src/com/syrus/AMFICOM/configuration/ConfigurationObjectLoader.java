/*
 * $Id: ConfigurationObjectLoader.java,v 1.17 2004/11/16 11:00:35 arseniy Exp $
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
 * @version $Revision: 1.17 $, $Date: 2004/11/16 11:00:35 $
 * @author $Author: arseniy $
 * @module configuration_v1
 */

public interface ConfigurationObjectLoader {

	CharacteristicType loadCharacteristicType(Identifier id) throws DatabaseException, CommunicationException;

	EquipmentType loadEquipmentType(Identifier id) throws DatabaseException, CommunicationException;

	PortType loadPortType(Identifier id) throws DatabaseException, CommunicationException;

	MeasurementPortType loadMeasurementPortType(Identifier id) throws DatabaseException, CommunicationException;

	KISType loadKISType(Identifier id) throws DatabaseException, CommunicationException;

	Characteristic loadCharacteristic(Identifier id) throws DatabaseException, CommunicationException;

	LinkType loadLinkType(Identifier id) throws DatabaseException, CommunicationException;

	TransmissionPathType loadTransmissionPathType(Identifier id) throws DatabaseException, CommunicationException;

//	PermissionAttributes loadPermissionAttributes(Identifier id) throws DatabaseException, CommunicationException;

	User loadUser(Identifier id) throws DatabaseException, CommunicationException;

	Domain loadDomain(Identifier id) throws DatabaseException, CommunicationException;

	Server loadServer(Identifier id) throws DatabaseException, CommunicationException;

	MCM loadMCM(Identifier id) throws DatabaseException, CommunicationException;

	Equipment loadEquipment(Identifier id) throws DatabaseException, CommunicationException;

	Port loadPort(Identifier id) throws DatabaseException, CommunicationException;

	TransmissionPath loadTransmissionPath(Identifier id) throws DatabaseException, CommunicationException;

	KIS loadKIS(Identifier id) throws DatabaseException, CommunicationException;

	Link loadLink(Identifier id) throws DatabaseException, CommunicationException;

	MeasurementPort loadMeasurementPort(Identifier id) throws DatabaseException, CommunicationException;

	MonitoredElement loadMonitoredElement(Identifier id) throws DatabaseException, CommunicationException;

    // this block for multiple objects

	List loadCharacteristicTypes(List ids) throws DatabaseException, CommunicationException;

	List loadEquipmentTypes(List ids) throws DatabaseException, CommunicationException;

	List loadPortTypes(List ids) throws DatabaseException, CommunicationException;

	List loadMeasurementPortTypes(List ids) throws DatabaseException, CommunicationException;

	List loadCharacteristics(List ids) throws DatabaseException, CommunicationException;

	List loadKISTypes(List ids) throws DatabaseException, CommunicationException;

	List loadLinkTypes(List ids) throws DatabaseException, CommunicationException;

	List loadTransmissionPathTypes(List ids) throws DatabaseException, CommunicationException;

//  PermissionAttributes loadPermissionAttributes(List ids) throws DatabaseException, CommunicationException;

	List loadUsers(List ids) throws DatabaseException, CommunicationException;

	List loadDomains(List ids) throws DatabaseException, CommunicationException;

	List loadServers(List ids) throws DatabaseException, CommunicationException;

	List loadMCMs(List ids) throws DatabaseException, CommunicationException;

	List loadEquipments(List ids) throws DatabaseException, CommunicationException;

	List loadPorts(List ids) throws DatabaseException, CommunicationException;

	List loadTransmissionPaths(List ids) throws DatabaseException, CommunicationException;

	List loadKISs(List ids) throws DatabaseException, CommunicationException;

	List loadLinks(List ids) throws DatabaseException, CommunicationException;

	List loadMeasurementPorts(List ids) throws DatabaseException, CommunicationException;

	List loadMonitoredElements(List ids) throws DatabaseException, CommunicationException;

    /* Load Configuration StorableObject but argument ids */
    
	List loadCharacteristicTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

  List loadEquipmentTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	List loadPortTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	List loadMeasurementPortTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	List loadCharacteristicsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	List loadKISTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	List loadLinkTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	List loadTransmissionPathTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

//  PermissionAttributes loadPermissionAttributesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	List loadUsersButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	List loadDomainsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	List loadServersButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	List loadMCMsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	List loadEquipmentsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	List loadPortsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	List loadTransmissionPathsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	List loadKISsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	List loadLinksButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	List loadMeasurementPortsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	List loadMonitoredElementsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	void saveCharacteristicType(CharacteristicType characteristicType, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveEquipmentType(EquipmentType equipmentType, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void savePortType(PortType portType, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveMeasurementPortType(MeasurementPortType measurementPortType, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveCharacteristic(Characteristic characteristic, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveKISType(KISType kisType, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveLinkType(LinkType linkType, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveTransmissionPathType(TransmissionPathType transmissionPathType, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

//	void savePermissionAttributes(PermissionAttributes permissionAttributes, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveUser(User user, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveDomain(Domain domain, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveServer(Server server, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveMCM(MCM mcm, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveEquipment(Equipment equipment, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void savePort(Port port, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveTransmissionPath(TransmissionPath transmissionPath, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveKIS(KIS kis, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

  void saveLink(Link link, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveMeasurementPort(MeasurementPort measurementPort, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveMonitoredElement(MonitoredElement monitoredElement, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveCharacteristicTypes(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveEquipmentTypes(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void savePortTypes(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveMeasurementPortTypes(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveCharacteristics(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveKISTypes(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveLinkTypes(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveTransmissionPathTypes(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

//	void savePermissionAttributes(PermissionAttributes permissionAttributes, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveUsers(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveDomains(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveServers(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveMCMs(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveLinks(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveEquipments(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void savePorts(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveTransmissionPaths(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveKISs(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveMeasurementPorts(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveMonitoredElements(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

}
