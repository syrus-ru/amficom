/*
 * $Id: ConfigurationObjectLoader.java,v 1.32 2005/02/24 14:59:52 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Collection;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.32 $, $Date: 2005/02/24 14:59:52 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public interface ConfigurationObjectLoader {

	CableLinkType loadCableLinkType(Identifier id) throws ApplicationException;

	Collection loadCableLinkTypes(Collection ids) throws ApplicationException;

	Collection loadCableLinkTypesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException;

	CableThread loadCableThread(Identifier id) throws ApplicationException;

	Collection loadCableThreads(Collection ids) throws ApplicationException;

	Collection loadCableThreadsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException;

	CableThreadType loadCableThreadType(Identifier id) throws ApplicationException;

	// this block for multiple objects

	Collection loadCableThreadTypes(Collection ids) throws ApplicationException;

	/* Load Configuration StorableObject but argument ids */

	Collection loadCableThreadTypesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException;

	Equipment loadEquipment(Identifier id) throws ApplicationException;

	Collection loadEquipments(Collection ids) throws ApplicationException;

	Collection loadEquipmentsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException;

	EquipmentType loadEquipmentType(Identifier id) throws ApplicationException;

	Collection loadEquipmentTypes(Collection ids) throws ApplicationException;

	Collection loadEquipmentTypesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException;

	KIS loadKIS(Identifier id) throws ApplicationException;

	Collection loadKISs(Collection ids) throws ApplicationException;

	Collection loadKISsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException;

	Link loadLink(Identifier id) throws ApplicationException;

	Collection loadLinks(Collection ids) throws ApplicationException;

	Collection loadLinksButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException;

	LinkType loadLinkType(Identifier id) throws ApplicationException;

	Collection loadLinkTypes(Collection ids) throws ApplicationException;

	Collection loadLinkTypesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException;

	MeasurementPort loadMeasurementPort(Identifier id) throws ApplicationException;

	Collection loadMeasurementPorts(Collection ids) throws ApplicationException;

	Collection loadMeasurementPortsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException;

	MeasurementPortType loadMeasurementPortType(Identifier id) throws ApplicationException;

	Collection loadMeasurementPortTypes(Collection ids) throws ApplicationException;

	Collection loadMeasurementPortTypesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException;

	MonitoredElement loadMonitoredElement(Identifier id) throws ApplicationException;

	Collection loadMonitoredElements(Collection ids) throws ApplicationException;

	Collection loadMonitoredElementsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException;

	Port loadPort(Identifier id) throws ApplicationException;

	Collection loadPorts(Collection ids) throws ApplicationException;

	Collection loadPortsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException;

	PortType loadPortType(Identifier id) throws ApplicationException;

	Collection loadPortTypes(Collection ids) throws ApplicationException;

	Collection loadPortTypesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException;

	TransmissionPath loadTransmissionPath(Identifier id) throws ApplicationException;

	Collection loadTransmissionPaths(Collection ids) throws ApplicationException;

	Collection loadTransmissionPathsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException;

	TransmissionPathType loadTransmissionPathType(Identifier id) throws ApplicationException;

	Collection loadTransmissionPathTypes(Collection ids) throws ApplicationException;

	Collection loadTransmissionPathTypesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException;



	void saveCableLinkType(CableLinkType cableLinkType, boolean force) throws ApplicationException;

	void saveCableLinkTypes(Collection list, boolean force) throws ApplicationException;

	void saveCableThread(CableThread cableThread, boolean force) throws ApplicationException;

	void saveCableThreads(Collection list, boolean force) throws ApplicationException;

	void saveCableThreadType(CableThreadType cableThreadType, boolean force) throws ApplicationException;

	void saveCableThreadTypes(Collection list, boolean force) throws ApplicationException;

	void saveEquipment(Equipment equipment, boolean force) throws ApplicationException;

	void saveEquipments(Collection list, boolean force) throws ApplicationException;

	void saveEquipmentType(EquipmentType equipmentType, boolean force) throws ApplicationException;

	void saveEquipmentTypes(Collection list, boolean force) throws ApplicationException;

	void saveKIS(KIS kis, boolean force) throws ApplicationException;

	void saveKISs(Collection list, boolean force) throws ApplicationException;

	void saveLink(Link link, boolean force) throws ApplicationException;

	void saveLinks(Collection list, boolean force) throws ApplicationException;

	void saveLinkType(LinkType linkType, boolean force) throws ApplicationException;

	void saveLinkTypes(Collection list, boolean force) throws ApplicationException;

	void saveMeasurementPort(MeasurementPort measurementPort, boolean force) throws ApplicationException;

	void saveMeasurementPorts(Collection list, boolean force) throws ApplicationException;

	void saveMeasurementPortType(MeasurementPortType measurementPortType, boolean force) throws ApplicationException;

	void saveMeasurementPortTypes(Collection list, boolean force) throws ApplicationException;

	void saveMonitoredElement(MonitoredElement monitoredElement, boolean force) throws ApplicationException;

	void saveMonitoredElements(Collection list, boolean force) throws ApplicationException;

	void savePort(Port port, boolean force) throws ApplicationException;

	void savePorts(Collection list, boolean force) throws ApplicationException;

	void savePortType(PortType portType, boolean force) throws ApplicationException;

	void savePortTypes(Collection list, boolean force) throws ApplicationException;

	void saveTransmissionPath(TransmissionPath transmissionPath, boolean force) throws ApplicationException;

	void saveTransmissionPaths(Collection list, boolean force) throws ApplicationException;

	void saveTransmissionPathType(TransmissionPathType transmissionPathType, boolean force) throws ApplicationException;

	void saveTransmissionPathTypes(Collection list, boolean force) throws ApplicationException;


	Set refresh(Set storableObjects) throws ApplicationException;


	void delete(Identifier id) throws IllegalDataException;

	void delete(Collection objects) throws IllegalDataException;

}
