/*
 * $Id: ConfigurationObjectLoader.java,v 1.31 2005/02/18 17:53:23 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Collection;
import java.util.Set;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.VersionCollisionException;

/**
 * @version $Revision: 1.31 $, $Date: 2005/02/18 17:53:23 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public interface ConfigurationObjectLoader {

	CableLinkType loadCableLinkType(Identifier id) throws DatabaseException, CommunicationException;

	Collection loadCableLinkTypes(Collection ids) throws DatabaseException, CommunicationException;

	Collection loadCableLinkTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException;

	CableThread loadCableThread(Identifier id) throws DatabaseException, CommunicationException;

	Collection loadCableThreads(Collection ids) throws DatabaseException, CommunicationException;

	Collection loadCableThreadsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException;

	CableThreadType loadCableThreadType(Identifier id) throws DatabaseException, CommunicationException;

	// this block for multiple objects

	Collection loadCableThreadTypes(Collection ids) throws DatabaseException, CommunicationException;

	/* Load Configuration StorableObject but argument ids */

	Collection loadCableThreadTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException;

	Equipment loadEquipment(Identifier id) throws DatabaseException, CommunicationException;

	Collection loadEquipments(Collection ids) throws DatabaseException, CommunicationException;

	Collection loadEquipmentsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException;

	EquipmentType loadEquipmentType(Identifier id) throws DatabaseException, CommunicationException;

	Collection loadEquipmentTypes(Collection ids) throws DatabaseException, CommunicationException;

	Collection loadEquipmentTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException;

	KIS loadKIS(Identifier id) throws DatabaseException, CommunicationException;

	Collection loadKISs(Collection ids) throws DatabaseException, CommunicationException;

	Collection loadKISsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException, CommunicationException;

	Link loadLink(Identifier id) throws DatabaseException, CommunicationException;

	Collection loadLinks(Collection ids) throws DatabaseException, CommunicationException;

	Collection loadLinksButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException, CommunicationException;

	LinkType loadLinkType(Identifier id) throws DatabaseException, CommunicationException;

	Collection loadLinkTypes(Collection ids) throws DatabaseException, CommunicationException;

	Collection loadLinkTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException;

	MeasurementPort loadMeasurementPort(Identifier id) throws DatabaseException, CommunicationException;

	Collection loadMeasurementPorts(Collection ids) throws DatabaseException, CommunicationException;

	Collection loadMeasurementPortsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException;

	MeasurementPortType loadMeasurementPortType(Identifier id) throws DatabaseException, CommunicationException;

	Collection loadMeasurementPortTypes(Collection ids) throws DatabaseException, CommunicationException;

	Collection loadMeasurementPortTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException;

	MonitoredElement loadMonitoredElement(Identifier id) throws DatabaseException, CommunicationException;

	Collection loadMonitoredElements(Collection ids) throws DatabaseException, CommunicationException;

	Collection loadMonitoredElementsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException;

	Port loadPort(Identifier id) throws DatabaseException, CommunicationException;

	Collection loadPorts(Collection ids) throws DatabaseException, CommunicationException;

	Collection loadPortsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException, CommunicationException;

	PortType loadPortType(Identifier id) throws DatabaseException, CommunicationException;

	Collection loadPortTypes(Collection ids) throws DatabaseException, CommunicationException;

	Collection loadPortTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException;

	TransmissionPath loadTransmissionPath(Identifier id) throws DatabaseException, CommunicationException;

	Collection loadTransmissionPaths(Collection ids) throws DatabaseException, CommunicationException;

	Collection loadTransmissionPathsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException;

	TransmissionPathType loadTransmissionPathType(Identifier id) throws DatabaseException, CommunicationException;

	Collection loadTransmissionPathTypes(Collection ids) throws DatabaseException, CommunicationException;

	Collection loadTransmissionPathTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException;

	Set refresh(Set storableObjects) throws CommunicationException, DatabaseException;

	void saveCableLinkType(CableLinkType cableLinkType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException;

	void saveCableLinkTypes(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveCableThread(CableThread cableThread, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveCableThreads(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveCableThreadType(CableThreadType cableThreadType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException;

	void saveCableThreadTypes(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveEquipment(Equipment equipment, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveEquipments(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveEquipmentType(EquipmentType equipmentType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException;

	void saveEquipmentTypes(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveKIS(KIS kis, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveKISs(Collection list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveLink(Link link, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveLinks(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveLinkType(LinkType linkType, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveLinkTypes(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveMeasurementPort(MeasurementPort measurementPort, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException;

	void saveMeasurementPorts(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveMeasurementPortType(MeasurementPortType measurementPortType, boolean force)
			throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveMeasurementPortTypes(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveMonitoredElement(MonitoredElement monitoredElement, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException;

	void saveMonitoredElements(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void savePort(Port port, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void savePorts(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void savePortType(PortType portType, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void savePortTypes(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveTransmissionPath(TransmissionPath transmissionPath, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException;

	void saveTransmissionPaths(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveTransmissionPathType(TransmissionPathType transmissionPathType, boolean force)
			throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveTransmissionPathTypes(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;



	void delete(Identifier id) throws IllegalDataException;

	void delete(Collection objects) throws IllegalDataException;

}
