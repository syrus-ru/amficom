/*
 * $Id: ConfigurationObjectLoader.java,v 1.34 2005/04/12 08:12:52 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.34 $, $Date: 2005/04/12 08:12:52 $
 * @author $Author: bass $
 * @module config_v1
 */

public interface ConfigurationObjectLoader {

	CableLinkType loadCableLinkType(Identifier id) throws ApplicationException;

	Set loadCableLinkTypes(Set ids) throws ApplicationException;

	Set loadCableLinkTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	CableThread loadCableThread(Identifier id) throws ApplicationException;

	Set loadCableThreads(Set ids) throws ApplicationException;

	Set loadCableThreadsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	CableThreadType loadCableThreadType(Identifier id) throws ApplicationException;

	// this block for multiple objects

	Set loadCableThreadTypes(Set ids) throws ApplicationException;

	/* Load Configuration StorableObject but argument ids */

	Set loadCableThreadTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	Equipment loadEquipment(Identifier id) throws ApplicationException;

	Set loadEquipments(Set ids) throws ApplicationException;

	Set loadEquipmentsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	EquipmentType loadEquipmentType(Identifier id) throws ApplicationException;

	Set loadEquipmentTypes(Set ids) throws ApplicationException;

	Set loadEquipmentTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	KIS loadKIS(Identifier id) throws ApplicationException;

	Set loadKISs(Set ids) throws ApplicationException;

	Set loadKISsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	Link loadLink(Identifier id) throws ApplicationException;

	Set loadLinks(Set ids) throws ApplicationException;

	Set loadLinksButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	LinkType loadLinkType(Identifier id) throws ApplicationException;

	Set loadLinkTypes(Set ids) throws ApplicationException;

	Set loadLinkTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	MeasurementPort loadMeasurementPort(Identifier id) throws ApplicationException;

	Set loadMeasurementPorts(Set ids) throws ApplicationException;

	Set loadMeasurementPortsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	MeasurementPortType loadMeasurementPortType(Identifier id) throws ApplicationException;

	Set loadMeasurementPortTypes(Set ids) throws ApplicationException;

	Set loadMeasurementPortTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	MonitoredElement loadMonitoredElement(Identifier id) throws ApplicationException;

	Set loadMonitoredElements(Set ids) throws ApplicationException;

	Set loadMonitoredElementsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	Port loadPort(Identifier id) throws ApplicationException;

	Set loadPorts(Set ids) throws ApplicationException;

	Set loadPortsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	PortType loadPortType(Identifier id) throws ApplicationException;

	Set loadPortTypes(Set ids) throws ApplicationException;

	Set loadPortTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	TransmissionPath loadTransmissionPath(Identifier id) throws ApplicationException;

	Set loadTransmissionPaths(Set ids) throws ApplicationException;

	Set loadTransmissionPathsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	TransmissionPathType loadTransmissionPathType(Identifier id) throws ApplicationException;

	Set loadTransmissionPathTypes(Set ids) throws ApplicationException;

	Set loadTransmissionPathTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;



	void saveCableLinkType(CableLinkType cableLinkType, boolean force) throws ApplicationException;

	void saveCableLinkTypes(Set list, boolean force) throws ApplicationException;

	void saveCableThread(CableThread cableThread, boolean force) throws ApplicationException;

	void saveCableThreads(Set list, boolean force) throws ApplicationException;

	void saveCableThreadType(CableThreadType cableThreadType, boolean force) throws ApplicationException;

	void saveCableThreadTypes(Set list, boolean force) throws ApplicationException;

	void saveEquipment(Equipment equipment, boolean force) throws ApplicationException;

	void saveEquipments(Set list, boolean force) throws ApplicationException;

	void saveEquipmentType(EquipmentType equipmentType, boolean force) throws ApplicationException;

	void saveEquipmentTypes(Set list, boolean force) throws ApplicationException;

	void saveKIS(KIS kis, boolean force) throws ApplicationException;

	void saveKISs(Set list, boolean force) throws ApplicationException;

	void saveLink(Link link, boolean force) throws ApplicationException;

	void saveLinks(Set list, boolean force) throws ApplicationException;

	void saveLinkType(LinkType linkType, boolean force) throws ApplicationException;

	void saveLinkTypes(Set list, boolean force) throws ApplicationException;

	void saveMeasurementPort(MeasurementPort measurementPort, boolean force) throws ApplicationException;

	void saveMeasurementPorts(Set list, boolean force) throws ApplicationException;

	void saveMeasurementPortType(MeasurementPortType measurementPortType, boolean force) throws ApplicationException;

	void saveMeasurementPortTypes(Set list, boolean force) throws ApplicationException;

	void saveMonitoredElement(MonitoredElement monitoredElement, boolean force) throws ApplicationException;

	void saveMonitoredElements(Set list, boolean force) throws ApplicationException;

	void savePort(Port port, boolean force) throws ApplicationException;

	void savePorts(Set list, boolean force) throws ApplicationException;

	void savePortType(PortType portType, boolean force) throws ApplicationException;

	void savePortTypes(Set list, boolean force) throws ApplicationException;

	void saveTransmissionPath(TransmissionPath transmissionPath, boolean force) throws ApplicationException;

	void saveTransmissionPaths(Set list, boolean force) throws ApplicationException;

	void saveTransmissionPathType(TransmissionPathType transmissionPathType, boolean force) throws ApplicationException;

	void saveTransmissionPathTypes(Set list, boolean force) throws ApplicationException;


	Set refresh(Set storableObjects) throws ApplicationException;


	void delete(Identifier id) throws IllegalDataException;

	void delete(final Set identifiables) throws IllegalDataException;

}
