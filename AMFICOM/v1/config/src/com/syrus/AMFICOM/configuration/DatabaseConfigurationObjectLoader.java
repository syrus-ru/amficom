/*
 * $Id: DatabaseConfigurationObjectLoader.java,v 1.44 2005/04/01 07:57:28 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.SessionContext;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.44 $, $Date: 2005/04/01 07:57:28 $
 * @author $Author: bob $
 * @module config_v1
 */

public class DatabaseConfigurationObjectLoader implements ConfigurationObjectLoader {

	public EquipmentType loadEquipmentType(Identifier id) throws ApplicationException {
		return new EquipmentType(id);
	}

	public PortType loadPortType(Identifier id) throws ApplicationException {
		return new PortType(id);
	}

	public MeasurementPortType loadMeasurementPortType(Identifier id) throws ApplicationException {
		return new MeasurementPortType(id);
	}

	public LinkType loadLinkType(Identifier id) throws ApplicationException {
		return new LinkType(id);
	}

	public CableLinkType loadCableLinkType(Identifier id) throws ApplicationException {
		return new CableLinkType(id);
	}

	public CableThreadType loadCableThreadType(Identifier id) throws ApplicationException {
		return new CableThreadType(id);
	}

	public Equipment loadEquipment(Identifier id) throws ApplicationException {
		return new Equipment(id);
	}

	public Port loadPort(Identifier id) throws ApplicationException {
		return new Port(id);
	}

	public MeasurementPort loadMeasurementPort(Identifier id) throws ApplicationException {
		return new MeasurementPort(id);
	}

	public TransmissionPath loadTransmissionPath(Identifier id) throws ApplicationException {
		return new TransmissionPath(id);
	}

	public TransmissionPathType loadTransmissionPathType(Identifier id) throws ApplicationException {
		return new TransmissionPathType(id);
	}

	public KIS loadKIS(Identifier id) throws ApplicationException {
		return new KIS(id);
	}

	public MonitoredElement loadMonitoredElement(Identifier id) throws ApplicationException {
		return new MonitoredElement(id);
	}

	public Link loadLink(Identifier id) throws ApplicationException {
		return new Link(id);
	}

	public CableThread loadCableThread(Identifier id) throws ApplicationException {
		return new CableThread(id);
	}





	// for multiple objects

	public Set loadEquipmentTypes(Set ids) throws ApplicationException {
		EquipmentTypeDatabase database = (EquipmentTypeDatabase) ConfigurationDatabaseContext.equipmentTypeDatabase;
		Set objects = null;
		try {
			objects = database.retrieveByIdsByCondition(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadEquipmentTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadEquipmentTypes | Illegal Storable Object: "
					+ e.getMessage());
		}
		return objects;
	}

	public Set loadPortTypes(Set ids) throws ApplicationException {
		PortTypeDatabase database = (PortTypeDatabase) ConfigurationDatabaseContext.portTypeDatabase;
		Set objects = null;
		try {
			objects = database.retrieveByIdsByCondition(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadPortTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadPortTypes | Illegal Storable Object: " + e.getMessage());
		}
		return objects;
	}

	public Set loadMeasurementPortTypes(Set ids) throws ApplicationException {
		MeasurementPortTypeDatabase database = (MeasurementPortTypeDatabase) ConfigurationDatabaseContext.measurementPortTypeDatabase;
		Set objects = null;
		try {
			objects = database.retrieveByIdsByCondition(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadMeasurementPortTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadMeasurementPortTypes | Illegal Storable Object: "
					+ e.getMessage());
		}
		return objects;
	}

	public Set loadLinkTypes(Set ids) throws ApplicationException {
		LinkTypeDatabase database = (LinkTypeDatabase) ConfigurationDatabaseContext.linkTypeDatabase;
		Set objects = null;
		try {
			objects = database.retrieveByIdsByCondition(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadLinkType | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadLinkTypes | Illegal Storable Object: " + e.getMessage());
		}
		return objects;
	}

	public Set loadCableLinkTypes(Set ids) throws ApplicationException {
		CableLinkTypeDatabase database = (CableLinkTypeDatabase) ConfigurationDatabaseContext.cableLinkTypeDatabase;
		Set objects = null;
		try {
			objects = database.retrieveByIdsByCondition(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadCableLinkTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadCableLinkTypes | Illegal Storable Object: "
					+ e.getMessage());
		}
		return objects;
	}

	public Set loadCableThreadTypes(Set ids) throws ApplicationException {
		CableThreadTypeDatabase database = (CableThreadTypeDatabase) ConfigurationDatabaseContext.cableThreadTypeDatabase;
		Set objects = null;
		try {
			objects = database.retrieveByIdsByCondition(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadCharacteristicTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadCharacteristicTypes | Illegal Storable Object: "
					+ e.getMessage());
		}
		return objects;
	}

	public Set loadEquipments(Set ids) throws ApplicationException {
		EquipmentDatabase database = (EquipmentDatabase) ConfigurationDatabaseContext.equipmentDatabase;
		Set objects = null;
		try {
			objects = database.retrieveByIdsByCondition(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadEquipments | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadEquipments | Illegal Storable Object: " + e.getMessage());
		}
		return objects;
	}

	public Set loadPorts(Set ids) throws ApplicationException {
		PortDatabase database = (PortDatabase) ConfigurationDatabaseContext.portDatabase;
		Set objects = null;
		try {
			objects = database.retrieveByIdsByCondition(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadPorts | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadPorts | Illegal Storable Object: " + e.getMessage());
		}
		return objects;
	}

	public Set loadMeasurementPorts(Set ids) throws ApplicationException {
		MeasurementPortDatabase database = (MeasurementPortDatabase) ConfigurationDatabaseContext.measurementPortDatabase;
		Set objects = null;
		try {
			objects = database.retrieveByIdsByCondition(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadMeasurementPorts | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadMeasurementPorts | Illegal Storable Object: "
					+ e.getMessage());
		}
		return objects;
	}

	public Set loadTransmissionPaths(Set ids) throws ApplicationException {
		TransmissionPathDatabase database = (TransmissionPathDatabase) ConfigurationDatabaseContext.transmissionPathDatabase;
		Set objects = null;
		try {
			objects = database.retrieveByIdsByCondition(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadTransmissionPaths | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadTransmissionPaths | Illegal Storable Object: "
					+ e.getMessage());
		}
		return objects;
	}

	public Set loadTransmissionPathTypes(Set ids) throws ApplicationException {
		TransmissionPathTypeDatabase database = (TransmissionPathTypeDatabase) ConfigurationDatabaseContext.transmissionPathTypeDatabase;
		Set objects = null;
		try {
			objects = database.retrieveByIdsByCondition(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadTransmissionPathTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadTransmissionPathTypes | Illegal Storable Object: "
					+ e.getMessage());
		}
		return objects;
	}

	public Set loadKISs(Set ids) throws ApplicationException {
		KISDatabase database = (KISDatabase) ConfigurationDatabaseContext.kisDatabase;
		Set objects = null;
		try {
			objects = database.retrieveByIdsByCondition(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadKISs | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadKISs | Illegal Storable Object: " + e.getMessage());
		}
		return objects;
	}

	public Set loadMonitoredElements(Set ids) throws ApplicationException {
		MonitoredElementDatabase database = (MonitoredElementDatabase) ConfigurationDatabaseContext.monitoredElementDatabase;
		Set objects = null;
		try {
			objects = database.retrieveByIdsByCondition(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadMonitoredElements | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadMonitoredElements | Illegal Storable Object: "
					+ e.getMessage());
		}
		return objects;
	}

	public Set loadLinks(Set ids) throws ApplicationException {
		LinkDatabase database = (LinkDatabase) ConfigurationDatabaseContext.linkDatabase;
		Set objects = null;
		try {
			objects = database.retrieveByIdsByCondition(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadLinks | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadLinks | Illegal Storable Object: " + e.getMessage());
		}
		return objects;
	}

	public Set loadCableThreads(Set ids) throws ApplicationException {
		CableThreadDatabase database = (CableThreadDatabase) ConfigurationDatabaseContext.cableThreadDatabase;
		Set objects = null;
		try {
			objects = database.retrieveByIdsByCondition(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadCableThreads | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadCableThreads | Illegal Storable Object: "
					+ e.getMessage());
		}
		return objects;
	}




	public Set loadCableLinkTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CableLinkTypeDatabase database = (CableLinkTypeDatabase) ConfigurationDatabaseContext.cableLinkTypeDatabase;
		Set objects = null;
		try {
			objects = database.retrieveButIdsByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadCableLinkTypesButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadCableLinkTypesButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return objects;
	}

	public Set loadCableThreadsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CableThreadDatabase database = (CableThreadDatabase) ConfigurationDatabaseContext.cableThreadDatabase;
		Set objects = null;
		try {
			objects = database.retrieveButIdsByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadCableThreadsButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadCableThreadsButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return objects;
	}

	/* Load Configuration StorableObject but argument ids */

	public Set loadCableThreadTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CableThreadTypeDatabase database = (CableThreadTypeDatabase) ConfigurationDatabaseContext.cableThreadTypeDatabase;
		Set objects = null;
		try {
			objects = database.retrieveButIdsByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadCableThreadTypesButIds | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadCableThreadTypesButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return objects;
	}

	public Set loadEquipmentsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		EquipmentDatabase database = (EquipmentDatabase) ConfigurationDatabaseContext.equipmentDatabase;
		Set objects = null;
		try {
			objects = database.retrieveButIdsByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadEquipmentsButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadEquipmentsButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return objects;
	}

	public Set loadEquipmentTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		EquipmentTypeDatabase database = (EquipmentTypeDatabase) ConfigurationDatabaseContext.equipmentTypeDatabase;
		Set objects = null;
		try {
			objects = database.retrieveButIdsByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadEquipmentTypesButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadEquipmentTypesButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return objects;
	}

	public Set loadKISsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		KISDatabase database = (KISDatabase) ConfigurationDatabaseContext.kisDatabase;
		Set objects = null;
		try {
			objects = database.retrieveButIdsByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadKISsButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadKISsButIds | Illegal Storable Object: " + e.getMessage());
		}
		return objects;
	}

	public Set loadLinksButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		LinkDatabase database = (LinkDatabase) ConfigurationDatabaseContext.linkDatabase;
		Set objects = null;
		try {
			objects = database.retrieveButIdsByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadLinksButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadLinksButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return objects;
	}

	public Set loadLinkTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		LinkTypeDatabase database = (LinkTypeDatabase) ConfigurationDatabaseContext.linkTypeDatabase;
		Set objects = null;
		try {
			objects = database.retrieveButIdsByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadLinkTypesButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadLinkTypesButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return objects;
	}

	public Set loadMeasurementPortsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		MeasurementPortDatabase database = (MeasurementPortDatabase) ConfigurationDatabaseContext.measurementPortDatabase;
		Set objects = null;
		try {
			objects = database.retrieveButIdsByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadMeasurementPortsButIds | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadMeasurementPortsButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return objects;
	}

	public Set loadMeasurementPortTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		MeasurementPortTypeDatabase database = (MeasurementPortTypeDatabase) ConfigurationDatabaseContext.measurementPortTypeDatabase;
		Set objects = null;
		try {
			objects = database.retrieveButIdsByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadMeasurementPortTypesButIds | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadMeasurementPortTypesButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return objects;
	}

	public Set loadMonitoredElementsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		MonitoredElementDatabase database = (MonitoredElementDatabase) ConfigurationDatabaseContext.monitoredElementDatabase;
		Set objects = null;
		try {
			objects = database.retrieveButIdsByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadMonitoredElementsButIds | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadMonitoredElementsButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return objects;
	}

	public Set loadPortsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		PortDatabase database = (PortDatabase) ConfigurationDatabaseContext.portDatabase;
		Set objects = null;
		try {
			objects = database.retrieveButIdsByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadPortsButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadPortsButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return objects;
	}

	public Set loadPortTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		PortTypeDatabase database = (PortTypeDatabase) ConfigurationDatabaseContext.portTypeDatabase;
		Set objects = null;
		try {
			objects = database.retrieveButIdsByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadPortTypesButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadPortTypesButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return objects;
	}

	public Set loadTransmissionPathsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		TransmissionPathDatabase database = (TransmissionPathDatabase) ConfigurationDatabaseContext.transmissionPathDatabase;
		Set objects = null;
		try {
			objects = database.retrieveButIdsByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadTransmissionPathsButIds | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadTransmissionPathsButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return objects;
	}

	public Set loadTransmissionPathTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		TransmissionPathTypeDatabase database = (TransmissionPathTypeDatabase) ConfigurationDatabaseContext.transmissionPathTypeDatabase;
		Set objects = null;
		try {
			objects = database.retrieveButIdsByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadTransmissionPathTypesButIds | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadTransmissionPathTypesButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return objects;
	}

//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$


	public void saveEquipmentType(EquipmentType equipmentType, boolean force) throws ApplicationException {
		EquipmentTypeDatabase database = (EquipmentTypeDatabase) ConfigurationDatabaseContext.equipmentTypeDatabase;
		database.update(equipmentType, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void savePortType(PortType portType, boolean force) throws ApplicationException {
		PortTypeDatabase database = (PortTypeDatabase) ConfigurationDatabaseContext.portTypeDatabase;
		database.update(portType, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveMeasurementPortType(MeasurementPortType measurementPortType, boolean force) throws ApplicationException {
		MeasurementPortTypeDatabase database = (MeasurementPortTypeDatabase) ConfigurationDatabaseContext.measurementPortTypeDatabase;
		database.update(measurementPortType, SessionContext.getAccessIdentity().getUserId(),
				force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveLinkType(LinkType linkType, boolean force) throws ApplicationException {
		LinkTypeDatabase database = (LinkTypeDatabase) ConfigurationDatabaseContext.linkTypeDatabase;
		database.update(linkType, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveCableLinkType(CableLinkType cableLinkType, boolean force) throws ApplicationException {
		StorableObjectDatabase database = ConfigurationDatabaseContext.cableLinkTypeDatabase;
		database.update(cableLinkType, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveCableThreadType(CableThreadType cableThreadType, boolean force) throws ApplicationException {
		CableThreadTypeDatabase database = (CableThreadTypeDatabase) ConfigurationDatabaseContext.cableThreadTypeDatabase;
		database.update(cableThreadType, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveEquipment(Equipment equipment, boolean force) throws ApplicationException {
		EquipmentDatabase database = (EquipmentDatabase) ConfigurationDatabaseContext.equipmentDatabase;
		database.update(equipment, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void savePort(Port port, boolean force) throws ApplicationException {
		PortDatabase database = (PortDatabase) ConfigurationDatabaseContext.portDatabase;
		database.update(port, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveMeasurementPort(MeasurementPort measurementPort, boolean force)
			throws ApplicationException {
		MeasurementPortDatabase database = (MeasurementPortDatabase) ConfigurationDatabaseContext.measurementPortDatabase;
		database.update(measurementPort, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveTransmissionPath(TransmissionPath transmissionPath, boolean force)
			throws ApplicationException {
		TransmissionPathDatabase database = (TransmissionPathDatabase) ConfigurationDatabaseContext.transmissionPathDatabase;
		database.update(transmissionPath, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveTransmissionPathType(TransmissionPathType transmissionPathType, boolean force)
			throws ApplicationException {
		TransmissionPathTypeDatabase database = (TransmissionPathTypeDatabase) ConfigurationDatabaseContext.transmissionPathTypeDatabase;
		database.update(transmissionPathType, SessionContext.getAccessIdentity().getUserId(),
				force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveKIS(KIS kis, boolean force) throws ApplicationException {
		KISDatabase database = (KISDatabase) ConfigurationDatabaseContext.kisDatabase;
		database.update(kis, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveMonitoredElement(MonitoredElement monitoredElement, boolean force)
			throws ApplicationException {
		MonitoredElementDatabase database = (MonitoredElementDatabase) ConfigurationDatabaseContext.monitoredElementDatabase;
		database.update(monitoredElement, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveLink(Link link, boolean force) throws ApplicationException {
		LinkDatabase database = (LinkDatabase) ConfigurationDatabaseContext.linkDatabase;
		database.update(link, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveCableThread(CableThread cableThread, boolean force)
			throws ApplicationException {
		CableThreadDatabase database = (CableThreadDatabase) ConfigurationDatabaseContext.cableThreadDatabase;
		database.update(cableThread, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

//############################################################################
//############################################################################
//############################################################################


	public void saveCableLinkTypes(Set objects, boolean force) throws ApplicationException {
		StorableObjectDatabase database = ConfigurationDatabaseContext.cableLinkTypeDatabase;
		database.update(objects, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveCableThreads(Set objects, boolean force) throws ApplicationException {
		StorableObjectDatabase database = ConfigurationDatabaseContext.cableThreadDatabase;
		database.update(objects, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveCableThreadTypes(Set objects, boolean force) throws ApplicationException {
		CableThreadTypeDatabase database = (CableThreadTypeDatabase) ConfigurationDatabaseContext.cableThreadTypeDatabase;
		database.update(objects, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveEquipments(Set objects, boolean force) throws ApplicationException {
		EquipmentDatabase database = (EquipmentDatabase) ConfigurationDatabaseContext.equipmentDatabase;
		database.update(objects, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveEquipmentTypes(Set objects, boolean force) throws ApplicationException {
		EquipmentTypeDatabase database = (EquipmentTypeDatabase) ConfigurationDatabaseContext.equipmentTypeDatabase;
		database.update(objects, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveKISs(Set objects, boolean force) throws ApplicationException {
		KISDatabase database = (KISDatabase) ConfigurationDatabaseContext.kisDatabase;
		database.update(objects, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveLinks(Set objects, boolean force) throws ApplicationException {
		LinkDatabase database = (LinkDatabase) ConfigurationDatabaseContext.linkDatabase;
		database.update(objects, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveLinkTypes(Set objects, boolean force) throws ApplicationException {
		LinkTypeDatabase database = (LinkTypeDatabase) ConfigurationDatabaseContext.linkTypeDatabase;
		database.update(objects, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveMeasurementPorts(Set objects, boolean force) throws ApplicationException {
		MeasurementPortDatabase database = (MeasurementPortDatabase) ConfigurationDatabaseContext.measurementPortDatabase;
		database.update(objects, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveMeasurementPortTypes(Set objects, boolean force) throws ApplicationException {
		MeasurementPortTypeDatabase database = (MeasurementPortTypeDatabase) ConfigurationDatabaseContext.measurementPortTypeDatabase;
		database.update(objects, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveMonitoredElements(Set objects, boolean force) throws ApplicationException {
		MonitoredElementDatabase database = (MonitoredElementDatabase) ConfigurationDatabaseContext.monitoredElementDatabase;
		database.update(objects, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void savePorts(Set objects, boolean force) throws ApplicationException {
		PortDatabase database = (PortDatabase) ConfigurationDatabaseContext.portDatabase;
		database.update(objects, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void savePortTypes(Set objects, boolean force) throws ApplicationException {
		PortTypeDatabase database = (PortTypeDatabase) ConfigurationDatabaseContext.portTypeDatabase;
		database.update(objects, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveTransmissionPaths(Set objects, boolean force) throws ApplicationException {
		TransmissionPathDatabase database = (TransmissionPathDatabase) ConfigurationDatabaseContext.transmissionPathDatabase;
		database.update(objects, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveTransmissionPathTypes(Set objects, boolean force) throws ApplicationException {
		TransmissionPathTypeDatabase database = (TransmissionPathTypeDatabase) ConfigurationDatabaseContext.transmissionPathTypeDatabase;
		database.update(objects, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}




	public Set refresh(Set storableObjects) throws ApplicationException {
		if (storableObjects.isEmpty())
			return Collections.EMPTY_SET;

		short entityCode = ((StorableObject) storableObjects.iterator().next()).getId().getMajor();

		StorableObjectDatabase database = ConfigurationDatabaseContext.getDatabase(entityCode);

		if (database != null)
			return database.refresh(storableObjects);

		return Collections.EMPTY_SET;
	}




	public void delete(Identifier id) throws IllegalDataException {
		short entityCode = id.getMajor();
		StorableObjectDatabase storableObjectDatabase = ConfigurationDatabaseContext.getDatabase(entityCode);
		if (storableObjectDatabase != null)
			storableObjectDatabase.delete(id);
	}

	public void delete(Set objects) throws IllegalDataException {
		if (objects == null || objects.isEmpty())
			return;
		/**
		 * TODO: use Trove collection instead java.util.Map
		 */
		Map map = new HashMap();

		/**
		 * separate objects by kind of entity
		 */
		Set entityObjects;
		Short entityCode;
		for (Iterator it = objects.iterator(); it.hasNext();) {
			Object object = it.next();
			Identifier identifier = null;
			if (object instanceof Identifier)
				identifier = (Identifier) object;
			else
				if (object instanceof Identifiable)
					identifier = ((Identifiable) object).getId();
				else
					throw new IllegalDataException("DatabaseConfigurationObjectLoader.delete | Object "
							+ object.getClass().getName() + " isn't Identifier or Identifiable");

			entityCode = new Short(identifier.getMajor());
			entityObjects = (Set) map.get(entityCode);
			if (entityObjects == null) {
				entityObjects = new HashSet();
				map.put(entityCode, entityObjects);
			}
			entityObjects.add(object);
		}

		StorableObjectDatabase storableObjectDatabase;
		for (Iterator it = map.keySet().iterator(); it.hasNext();) {
			entityCode = (Short) it.next();
			entityObjects = (Set) map.get(entityCode);
			storableObjectDatabase = ConfigurationDatabaseContext.getDatabase(entityCode);
			if (storableObjectDatabase != null)
				storableObjectDatabase.delete(entityObjects);
		}
	}

}
