/*
 * $Id: ConfigurationObjectLoader.java,v 1.26 2005/02/08 09:26:57 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.VersionCollisionException;

/**
 * @version $Revision: 1.26 $, $Date: 2005/02/08 09:26:57 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public interface ConfigurationObjectLoader {

	void delete(Identifier id) throws CommunicationException, DatabaseException;

	void delete(List objects) throws CommunicationException, DatabaseException, IllegalDataException;

	CableLinkType loadCableLinkType(Identifier id) throws DatabaseException, CommunicationException;

	List loadCableLinkTypes(List ids) throws DatabaseException, CommunicationException;

	List loadCableLinkTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException;

	CableThread loadCableThread(Identifier id) throws DatabaseException, CommunicationException;

	List loadCableThreads(List ids) throws DatabaseException, CommunicationException;

	List loadCableThreadsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException;

	CableThreadType loadCableThreadType(Identifier id) throws DatabaseException, CommunicationException;

	// this block for multiple objects

	List loadCableThreadTypes(List ids) throws DatabaseException, CommunicationException;

	/* Load Configuration StorableObject but argument ids */

	List loadCableThreadTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException;

	Equipment loadEquipment(Identifier id) throws DatabaseException, CommunicationException;

	List loadEquipments(List ids) throws DatabaseException, CommunicationException;

	List loadEquipmentsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException;

	EquipmentType loadEquipmentType(Identifier id) throws DatabaseException, CommunicationException;

	List loadEquipmentTypes(List ids) throws DatabaseException, CommunicationException;

	List loadEquipmentTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException;

	KIS loadKIS(Identifier id) throws DatabaseException, CommunicationException;

	List loadKISs(List ids) throws DatabaseException, CommunicationException;

	List loadKISsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	Link loadLink(Identifier id) throws DatabaseException, CommunicationException;

	List loadLinks(List ids) throws DatabaseException, CommunicationException;

	List loadLinksButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	LinkType loadLinkType(Identifier id) throws DatabaseException, CommunicationException;

	List loadLinkTypes(List ids) throws DatabaseException, CommunicationException;

	List loadLinkTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException;

	MeasurementPort loadMeasurementPort(Identifier id) throws DatabaseException, CommunicationException;

	List loadMeasurementPorts(List ids) throws DatabaseException, CommunicationException;

	List loadMeasurementPortsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException;

	MeasurementPortType loadMeasurementPortType(Identifier id) throws DatabaseException, CommunicationException;

	List loadMeasurementPortTypes(List ids) throws DatabaseException, CommunicationException;

	List loadMeasurementPortTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException;

	MonitoredElement loadMonitoredElement(Identifier id) throws DatabaseException, CommunicationException;

	List loadMonitoredElements(List ids) throws DatabaseException, CommunicationException;

	List loadMonitoredElementsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException;

	Port loadPort(Identifier id) throws DatabaseException, CommunicationException;

	List loadPorts(List ids) throws DatabaseException, CommunicationException;

	List loadPortsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	PortType loadPortType(Identifier id) throws DatabaseException, CommunicationException;

	List loadPortTypes(List ids) throws DatabaseException, CommunicationException;

	List loadPortTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException;

	TransmissionPath loadTransmissionPath(Identifier id) throws DatabaseException, CommunicationException;

	List loadTransmissionPaths(List ids) throws DatabaseException, CommunicationException;

	List loadTransmissionPathsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException;

	TransmissionPathType loadTransmissionPathType(Identifier id) throws DatabaseException, CommunicationException;

	List loadTransmissionPathTypes(List ids) throws DatabaseException, CommunicationException;

	List loadTransmissionPathTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException;

	Set refresh(Set storableObjects) throws CommunicationException, DatabaseException;

	void saveCableLinkType(CableLinkType cableLinkType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException;

	void saveCableLinkTypes(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveCableThread(CableThread cableThread, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveCableThreads(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveCableThreadType(CableThreadType cableThreadType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException;

	void saveCableThreadTypes(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveEquipment(Equipment equipment, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveEquipments(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveEquipmentType(EquipmentType equipmentType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException;

	void saveEquipmentTypes(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveKIS(KIS kis, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveKISs(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveLink(Link link, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveLinks(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveLinkType(LinkType linkType, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveLinkTypes(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveMeasurementPort(MeasurementPort measurementPort, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException;

	void saveMeasurementPorts(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveMeasurementPortType(MeasurementPortType measurementPortType, boolean force)
			throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveMeasurementPortTypes(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveMonitoredElement(MonitoredElement monitoredElement, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException;

	void saveMonitoredElements(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void savePort(Port port, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void savePorts(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void savePortType(PortType portType, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void savePortTypes(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveTransmissionPath(TransmissionPath transmissionPath, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException;

	void saveTransmissionPaths(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveTransmissionPathType(TransmissionPathType transmissionPathType, boolean force)
			throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveTransmissionPathTypes(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

}
