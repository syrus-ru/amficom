/*
 * $Id: DatabaseConfigurationObjectLoader.java,v 1.33 2005/02/08 09:26:57 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identified;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.33 $, $Date: 2005/02/08 09:26:57 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public class DatabaseConfigurationObjectLoader implements ConfigurationObjectLoader {

	public EquipmentType loadEquipmentType(Identifier id) throws DatabaseException, CommunicationException {
		return new EquipmentType(id);
	}

	public PortType loadPortType(Identifier id) throws DatabaseException, CommunicationException {
		return new PortType(id);
	}

	public MeasurementPortType loadMeasurementPortType(Identifier id) throws DatabaseException, CommunicationException {
		return new MeasurementPortType(id);
	}

	public LinkType loadLinkType(Identifier id) throws DatabaseException, CommunicationException {
		return new LinkType(id);
	}

	public CableLinkType loadCableLinkType(Identifier id) throws DatabaseException, CommunicationException {
		return new CableLinkType(id);
	}

	public CableThreadType loadCableThreadType(Identifier id) throws DatabaseException, CommunicationException {
		return new CableThreadType(id);
	}

	public Equipment loadEquipment(Identifier id) throws DatabaseException, CommunicationException {
		return new Equipment(id);
	}

	public Port loadPort(Identifier id) throws DatabaseException, CommunicationException {
		return new Port(id);
	}

	public MeasurementPort loadMeasurementPort(Identifier id) throws DatabaseException, CommunicationException {
		return new MeasurementPort(id);
	}

	public TransmissionPath loadTransmissionPath(Identifier id) throws DatabaseException, CommunicationException {
		return new TransmissionPath(id);
	}

	public TransmissionPathType loadTransmissionPathType(Identifier id) throws DatabaseException, CommunicationException {
		return new TransmissionPathType(id);
	}

	public KIS loadKIS(Identifier id) throws DatabaseException, CommunicationException {
		return new KIS(id);
	}

	public MonitoredElement loadMonitoredElement(Identifier id) throws DatabaseException, CommunicationException {
		return new MonitoredElement(id);
	}

	public Link loadLink(Identifier id) throws DatabaseException, CommunicationException {
		return new Link(id);
	}

	public CableThread loadCableThread(Identifier id) throws DatabaseException, CommunicationException {
		return new CableThread(id);
	}





	// for multiple objects

	public List loadEquipmentTypes(List ids) throws DatabaseException, CommunicationException {
		EquipmentTypeDatabase database = (EquipmentTypeDatabase) ConfigurationDatabaseContext.equipmentTypeDatabase;
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadEquipmentTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadEquipmentTypes | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public List loadPortTypes(List ids) throws DatabaseException, CommunicationException {
		PortTypeDatabase database = (PortTypeDatabase) ConfigurationDatabaseContext.portTypeDatabase;
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadPortTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadPortTypes | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadMeasurementPortTypes(List ids) throws DatabaseException, CommunicationException {
		MeasurementPortTypeDatabase database = (MeasurementPortTypeDatabase) ConfigurationDatabaseContext.measurementPortTypeDatabase;
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadMeasurementPortTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadMeasurementPortTypes | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public List loadLinkTypes(List ids) throws DatabaseException, CommunicationException {
		LinkTypeDatabase database = (LinkTypeDatabase) ConfigurationDatabaseContext.linkTypeDatabase;
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadLinkType | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadLinkTypes | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadCableLinkTypes(List ids) throws DatabaseException, CommunicationException {
		CableLinkTypeDatabase database = (CableLinkTypeDatabase) ConfigurationDatabaseContext.cableLinkTypeDatabase;
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadCableLinkTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadCableLinkTypes | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public List loadCableThreadTypes(List ids) throws DatabaseException, CommunicationException {
		CableThreadTypeDatabase database = (CableThreadTypeDatabase) ConfigurationDatabaseContext.cableThreadTypeDatabase;
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadCharacteristicTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadCharacteristicTypes | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public List loadEquipments(List ids) throws DatabaseException, CommunicationException {
		EquipmentDatabase database = (EquipmentDatabase) ConfigurationDatabaseContext.equipmentDatabase;
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadEquipments | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadEquipments | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadPorts(List ids) throws DatabaseException, CommunicationException {
		PortDatabase database = (PortDatabase) ConfigurationDatabaseContext.portDatabase;
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadPorts | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadPorts | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadMeasurementPorts(List ids) throws DatabaseException, CommunicationException {
		MeasurementPortDatabase database = (MeasurementPortDatabase) ConfigurationDatabaseContext.measurementPortDatabase;
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadMeasurementPorts | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadMeasurementPorts | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public List loadTransmissionPaths(List ids) throws DatabaseException, CommunicationException {
		TransmissionPathDatabase database = (TransmissionPathDatabase) ConfigurationDatabaseContext.transmissionPathDatabase;
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadTransmissionPaths | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadTransmissionPaths | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public List loadTransmissionPathTypes(List ids) throws DatabaseException, CommunicationException {
		TransmissionPathTypeDatabase database = (TransmissionPathTypeDatabase) ConfigurationDatabaseContext.transmissionPathTypeDatabase;
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadTransmissionPathTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadTransmissionPathTypes | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public List loadKISs(List ids) throws DatabaseException, CommunicationException {
		KISDatabase database = (KISDatabase) ConfigurationDatabaseContext.kisDatabase;
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadKISs | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadKISs | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadMonitoredElements(List ids) throws DatabaseException, CommunicationException {
		MonitoredElementDatabase database = (MonitoredElementDatabase) ConfigurationDatabaseContext.monitoredElementDatabase;
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadMonitoredElements | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadMonitoredElements | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public List loadLinks(List ids) throws DatabaseException, CommunicationException {
		LinkDatabase database = (LinkDatabase) ConfigurationDatabaseContext.linkDatabase;
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadLinks | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadLinks | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadCableThreads(List ids) throws DatabaseException, CommunicationException {
		CableThreadDatabase database = (CableThreadDatabase) ConfigurationDatabaseContext.cableThreadDatabase;
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadCableThreads | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadCableThreads | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}




	public List loadCableLinkTypesButIds(StorableObjectCondition condition, List ids)
			throws DatabaseException,
				CommunicationException {
		CableLinkTypeDatabase database = (CableLinkTypeDatabase) ConfigurationDatabaseContext.cableLinkTypeDatabase;
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadCableLinkTypesButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadCableLinkTypesButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public List loadCableThreadsButIds(StorableObjectCondition condition, List ids)
			throws DatabaseException,
				CommunicationException {
		CableThreadDatabase database = (CableThreadDatabase) ConfigurationDatabaseContext.cableThreadDatabase;
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadCableThreadsButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadCableThreadsButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	/* Load Configuration StorableObject but argument ids */

	public List loadCableThreadTypesButIds(StorableObjectCondition condition, List ids)
			throws DatabaseException,
				CommunicationException {
		CableThreadTypeDatabase database = (CableThreadTypeDatabase) ConfigurationDatabaseContext.cableThreadTypeDatabase;
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadCableThreadTypesButIds | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadCableThreadTypesButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public List loadEquipmentsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
		EquipmentDatabase database = (EquipmentDatabase) ConfigurationDatabaseContext.equipmentDatabase;
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadEquipmentsButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadEquipmentsButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public List loadEquipmentTypesButIds(StorableObjectCondition condition, List ids)
			throws DatabaseException,
				CommunicationException {
		EquipmentTypeDatabase database = (EquipmentTypeDatabase) ConfigurationDatabaseContext.equipmentTypeDatabase;
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadEquipmentTypesButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadEquipmentTypesButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public List loadKISsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
		KISDatabase database = (KISDatabase) ConfigurationDatabaseContext.kisDatabase;
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadKISsButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadKISsButIds | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadLinksButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
		LinkDatabase database = (LinkDatabase) ConfigurationDatabaseContext.linkDatabase;
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadLinksButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadLinksButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public List loadLinkTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
		LinkTypeDatabase database = (LinkTypeDatabase) ConfigurationDatabaseContext.linkTypeDatabase;
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadLinkTypesButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadLinkTypesButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public List loadMeasurementPortsButIds(StorableObjectCondition condition, List ids)
			throws DatabaseException,
				CommunicationException {
		MeasurementPortDatabase database = (MeasurementPortDatabase) ConfigurationDatabaseContext.measurementPortDatabase;
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadMeasurementPortsButIds | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadMeasurementPortsButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public List loadMeasurementPortTypesButIds(StorableObjectCondition condition, List ids)
			throws DatabaseException,
				CommunicationException {
		MeasurementPortTypeDatabase database = (MeasurementPortTypeDatabase) ConfigurationDatabaseContext.measurementPortTypeDatabase;
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadMeasurementPortTypesButIds | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadMeasurementPortTypesButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public List loadMonitoredElementsButIds(StorableObjectCondition condition, List ids)
			throws DatabaseException,
				CommunicationException {
		MonitoredElementDatabase database = (MonitoredElementDatabase) ConfigurationDatabaseContext.monitoredElementDatabase;
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadMonitoredElementsButIds | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadMonitoredElementsButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public List loadPortsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
		PortDatabase database = (PortDatabase) ConfigurationDatabaseContext.portDatabase;
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadPortsButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadPortsButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public List loadPortTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
		PortTypeDatabase database = (PortTypeDatabase) ConfigurationDatabaseContext.portTypeDatabase;
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadPortTypesButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadPortTypesButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public List loadTransmissionPathsButIds(StorableObjectCondition condition, List ids)
			throws DatabaseException,
				CommunicationException {
		TransmissionPathDatabase database = (TransmissionPathDatabase) ConfigurationDatabaseContext.transmissionPathDatabase;
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadTransmissionPathsButIds | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadTransmissionPathsButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public List loadTransmissionPathTypesButIds(StorableObjectCondition condition, List ids)
			throws DatabaseException,
				CommunicationException {
		TransmissionPathTypeDatabase database = (TransmissionPathTypeDatabase) ConfigurationDatabaseContext.transmissionPathTypeDatabase;
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.loadTransmissionPathTypesButIds | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.loadTransmissionPathTypesButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$


	public void saveEquipmentType(EquipmentType equipmentType, boolean force) throws DatabaseException, CommunicationException {
		EquipmentTypeDatabase database = (EquipmentTypeDatabase) ConfigurationDatabaseContext.equipmentTypeDatabase;
		try {
			database.update(equipmentType, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveEquipmentType | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveEquipmentType | UpdateObjectException: "
					+ e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveEquipmentType | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveEquipmentType | Illegal Storable Object: "
					+ e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveEquipmentType | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveEquipmentType | VersionCollisionException: "
					+ e.getMessage());
		}
	}

	public void savePortType(PortType portType, boolean force) throws DatabaseException, CommunicationException {
		PortTypeDatabase database = (PortTypeDatabase) ConfigurationDatabaseContext.portTypeDatabase;
		try {
			database.update(portType, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.savePortType | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.savePortType | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.savePortType | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.savePortType | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.savePortType | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.savePortType | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveMeasurementPortType(MeasurementPortType measurementPortType, boolean force)
			throws DatabaseException,
				CommunicationException {
		MeasurementPortTypeDatabase database = (MeasurementPortTypeDatabase) ConfigurationDatabaseContext.measurementPortTypeDatabase;
		try {
			database.update(measurementPortType,
					force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK,
					null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveMeasurementPortType | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveMeasurementPortType | UpdateObjectException: "
					+ e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveMeasurementPortType | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveMeasurementPortType | Illegal Storable Object: "
					+ e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveMeasurementPortType | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveMeasurementPortType | VersionCollisionException: "
					+ e.getMessage());
		}
	}

	public void saveLinkType(LinkType linkType, boolean force)
			throws VersionCollisionException,
				DatabaseException,
				CommunicationException {
		LinkTypeDatabase database = (LinkTypeDatabase) ConfigurationDatabaseContext.linkTypeDatabase;
		try {
			database.update(linkType, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveLinkType | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveLinkType | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveLinkType | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveLinkType | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveLinkType | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveLinkType | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveCableLinkType(CableLinkType cableLinkType, boolean force)
			throws VersionCollisionException,
				DatabaseException,
				CommunicationException {
		StorableObjectDatabase database = ConfigurationDatabaseContext.cableLinkTypeDatabase;
		try {
			database.update(cableLinkType, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveCableLinkType | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveCableLinkType | UpdateObjectException: "
					+ e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveCableLinkType | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveCableLinkType | Illegal Storable Object: "
					+ e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveCableLinkType | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveCableLinkType | VersionCollisionException: "
					+ e.getMessage());
		}
	}

	public void saveCableThreadType(CableThreadType cableThreadType, boolean force)
			throws VersionCollisionException,
				DatabaseException,
				CommunicationException {
		CableThreadTypeDatabase database = (CableThreadTypeDatabase) ConfigurationDatabaseContext.cableThreadTypeDatabase;
		try {
			database.update(cableThreadType, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveCableThreadType | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveCableThreadType | UpdateObjectException: "
					+ e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveCableThreadType | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveCableThreadType | Illegal Storable Object: "
					+ e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveCableThreadType | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveCableThreadType | VersionCollisionException: "
					+ e.getMessage());
		}
	}

	public void saveEquipment(Equipment equipment, boolean force) throws DatabaseException, CommunicationException {
		EquipmentDatabase database = (EquipmentDatabase) ConfigurationDatabaseContext.equipmentDatabase;
		try {
			database.update(equipment, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveEquipment | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveEquipment | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveEquipment | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveEquipment | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveEquipment | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveEquipment | VersionCollisionException: "
					+ e.getMessage());
		}
	}

	public void savePort(Port port, boolean force) throws DatabaseException, CommunicationException {
		PortDatabase database = (PortDatabase) ConfigurationDatabaseContext.portDatabase;
		try {
			database.update(port, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.savePort | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.savePort | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.savePort | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.savePort | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.savePort | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.savePort | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveMeasurementPort(MeasurementPort measurementPort, boolean force)
			throws DatabaseException,
				CommunicationException {
		MeasurementPortDatabase database = (MeasurementPortDatabase) ConfigurationDatabaseContext.measurementPortDatabase;
		try {
			database.update(measurementPort, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveMeasurementPort | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveMeasurementPort | UpdateObjectException: "
					+ e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveMeasurementPort | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveMeasurementPort | Illegal Storable Object: "
					+ e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveMeasurementPort | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveMeasurementPort | VersionCollisionException: "
					+ e.getMessage());
		}
	}

	public void saveTransmissionPath(TransmissionPath transmissionPath, boolean force)
			throws DatabaseException,
				CommunicationException {
		TransmissionPathDatabase database = (TransmissionPathDatabase) ConfigurationDatabaseContext.transmissionPathDatabase;
		try {
			database.update(transmissionPath, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveTransmissionPath | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveTransmissionPath | UpdateObjectException: "
					+ e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveTransmissionPath | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveTransmissionPath | Illegal Storable Object: "
					+ e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveTransmissionPath | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveTransmissionPath | VersionCollisionException: "
					+ e.getMessage());
		}
	}

	public void saveTransmissionPathType(TransmissionPathType transmissionPathType, boolean force)
			throws VersionCollisionException,
				DatabaseException,
				CommunicationException {
		TransmissionPathTypeDatabase database = (TransmissionPathTypeDatabase) ConfigurationDatabaseContext.transmissionPathTypeDatabase;
		try {
			database.update(transmissionPathType,
					force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK,
					null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveTransmissionPathType | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveTransmissionPathType | UpdateObjectException: "
					+ e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveTransmissionPathType | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveTransmissionPathType | Illegal Storable Object: "
					+ e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveTransmissionPathType | VersionCollisionException: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveTransmissionPathType | VersionCollisionException: "
					+ e.getMessage());
		}
	}

	public void saveKIS(KIS kis, boolean force) throws DatabaseException, CommunicationException {
		KISDatabase database = (KISDatabase) ConfigurationDatabaseContext.kisDatabase;
		try {
			database.update(kis, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveKIS | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveKIS | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveKIS | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveKIS | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveKIS | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveKIS | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveMonitoredElement(MonitoredElement monitoredElement, boolean force)
			throws DatabaseException,
				CommunicationException {
		MonitoredElementDatabase database = (MonitoredElementDatabase) ConfigurationDatabaseContext.monitoredElementDatabase;
		try {
			database.update(monitoredElement, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveMonitoredElement | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveMonitoredElement | UpdateObjectException: "
					+ e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveMonitoredElement | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveMonitoredElement | Illegal Storable Object: "
					+ e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveMonitoredElement | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveMonitoredElement | VersionCollisionException: "
					+ e.getMessage());
		}
	}

	public void saveLink(Link link, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException {
		LinkDatabase database = (LinkDatabase) ConfigurationDatabaseContext.linkDatabase;
		try {
			database.update(link, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveLink | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveLink | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveLink | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveLink | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveLink | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveLink | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveCableThread(CableThread cableThread, boolean force)
			throws VersionCollisionException,
				DatabaseException,
				CommunicationException {
		StorableObjectDatabase database = ConfigurationDatabaseContext.cableThreadDatabase;
		try {
			database.update(cableThread, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveCableThread | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveCableThread | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveCableThread | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveCableThread | Illegal Storable Object: "
					+ e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveCableThread | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveCableThread | VersionCollisionException: "
					+ e.getMessage());
		}
	}

//############################################################################
//############################################################################
//############################################################################


	public void saveCableLinkTypes(List list, boolean force)
			throws VersionCollisionException,
				DatabaseException,
				CommunicationException {
		StorableObjectDatabase database = ConfigurationDatabaseContext.cableLinkTypeDatabase;
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveCableLinkTypes | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveCableLinkTypes | UpdateObjectException: "
					+ e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveCableLinkTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveCableLinkTypes | Illegal Storable Object: "
					+ e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveCableLinkTypes | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveCableLinkTypes | VersionCollisionException: "
					+ e.getMessage());
		}
	}

	public void saveCableThreads(List list, boolean force)
			throws VersionCollisionException,
				DatabaseException,
				CommunicationException {
		StorableObjectDatabase database = ConfigurationDatabaseContext.cableThreadDatabase;
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveCableThreads | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveCableThreads | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveCableThreads | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveCableThreads | Illegal Storable Object: "
					+ e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveCableThreads | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveCableThreads | VersionCollisionException: "
					+ e.getMessage());
		}
	}

	public void saveCableThreadTypes(List list, boolean force) throws DatabaseException, CommunicationException {
		CableThreadTypeDatabase database = (CableThreadTypeDatabase) ConfigurationDatabaseContext.cableThreadTypeDatabase;
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveCableThreadTypes | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveCableThreadTypes | UpdateObjectException: "
					+ e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveCableThreadTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveCableThreadTypes | Illegal Storable Object: "
					+ e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveCableThreadTypes | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveCableThreadTypes | VersionCollisionException: "
					+ e.getMessage());
		}
	}

	public void saveEquipments(List list, boolean force) throws DatabaseException, CommunicationException {
		EquipmentDatabase database = (EquipmentDatabase) ConfigurationDatabaseContext.equipmentDatabase;
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveEquipments | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveEquipments | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveEquipments | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveEquipments | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveEquipments | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveEquipments | VersionCollisionException: "
					+ e.getMessage());
		}
	}

	public void saveEquipmentTypes(List list, boolean force) throws DatabaseException, CommunicationException {
		EquipmentTypeDatabase database = (EquipmentTypeDatabase) ConfigurationDatabaseContext.equipmentTypeDatabase;
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveEquipmentTypes | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveEquipmentTypes | UpdateObjectException: "
					+ e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveEquipmentTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveEquipmentTypes | Illegal Storable Object: "
					+ e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveEquipmentTypes | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveEquipmentTypes | VersionCollisionException: "
					+ e.getMessage());
		}
	}

	public void saveKISs(List list, boolean force) throws DatabaseException, CommunicationException {
		KISDatabase database = (KISDatabase) ConfigurationDatabaseContext.kisDatabase;
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveKISs | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveKISs | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveKISs | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveKISs | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveKISs | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveKISs | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveLinks(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException {
		LinkDatabase database = (LinkDatabase) ConfigurationDatabaseContext.linkDatabase;
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveLinks | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveLinks | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveLinks | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveLinks | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveLinks | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveLinks | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveLinkTypes(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException {
		LinkTypeDatabase database = (LinkTypeDatabase) ConfigurationDatabaseContext.linkTypeDatabase;
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveLinkTypes | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveLinkTypes | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveLinkTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveLinkTypes | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveLinkTypes | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveLinkTypes | VersionCollisionException: "
					+ e.getMessage());
		}
	}

	public void saveMeasurementPorts(List list, boolean force) throws DatabaseException, CommunicationException {
		MeasurementPortDatabase database = (MeasurementPortDatabase) ConfigurationDatabaseContext.measurementPortDatabase;
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveMeasurementPorts | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveMeasurementPorts | UpdateObjectException: "
					+ e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveMeasurementPorts | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveMeasurementPorts | Illegal Storable Object: "
					+ e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveMeasurementPorts | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveMeasurementPorts | VersionCollisionException: "
					+ e.getMessage());
		}
	}

	public void saveMeasurementPortTypes(List list, boolean force) throws DatabaseException, CommunicationException {
		MeasurementPortTypeDatabase database = (MeasurementPortTypeDatabase) ConfigurationDatabaseContext.measurementPortTypeDatabase;
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveMeasurementPortTypes | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveMeasurementPortTypes | UpdateObjectException: "
					+ e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveMeasurementPortTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveMeasurementPortTypes | Illegal Storable Object: "
					+ e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveMeasurementPortTypes | VersionCollisionException: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveMeasurementPortTypes | VersionCollisionException: "
					+ e.getMessage());
		}
	}

	public void saveMonitoredElements(List list, boolean force) throws DatabaseException, CommunicationException {
		MonitoredElementDatabase database = (MonitoredElementDatabase) ConfigurationDatabaseContext.monitoredElementDatabase;
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveMonitoredElements | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveMonitoredElements | UpdateObjectException: "
					+ e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveMonitoredElements | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveMonitoredElements | Illegal Storable Object: "
					+ e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveMonitoredElements | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveMonitoredElements | VersionCollisionException: "
					+ e.getMessage());
		}
	}

	public void savePorts(List list, boolean force) throws DatabaseException, CommunicationException {
		PortDatabase database = (PortDatabase) ConfigurationDatabaseContext.portDatabase;
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.savePorts | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.savePorts | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.savePorts | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.savePorts | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.savePorts | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.savePorts | VersionCollisionException: " + e.getMessage());
		}
	}

	public void savePortTypes(List list, boolean force) throws DatabaseException, CommunicationException {
		PortTypeDatabase database = (PortTypeDatabase) ConfigurationDatabaseContext.portTypeDatabase;
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.savePortTypes | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.savePortTypes | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.savePortTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.savePortTypes | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.savePortTypes | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.savePortTypes | VersionCollisionException: "
					+ e.getMessage());
		}
	}

	public void saveTransmissionPaths(List list, boolean force) throws DatabaseException, CommunicationException {
		TransmissionPathDatabase database = (TransmissionPathDatabase) ConfigurationDatabaseContext.transmissionPathDatabase;
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveTransmissionPaths | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveTransmissionPaths | UpdateObjectException: "
					+ e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveTransmissionPaths | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveTransmissionPaths | Illegal Storable Object: "
					+ e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveTransmissionPaths | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveTransmissionPaths | VersionCollisionException: "
					+ e.getMessage());
		}
	}

	public void saveTransmissionPathTypes(List list, boolean force)
			throws VersionCollisionException,
				DatabaseException,
				CommunicationException {
		TransmissionPathTypeDatabase database = (TransmissionPathTypeDatabase) ConfigurationDatabaseContext.transmissionPathTypeDatabase;
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveTransmissionPathTypes | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveTransmissionPathTypes | UpdateObjectException: "
					+ e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveTransmissionPathTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveTransmissionPathTypes | Illegal Storable Object: "
					+ e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.saveTransmissionPathTypes | VersionCollisionException: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.saveTransmissionPathTypes | VersionCollisionException: "
					+ e.getMessage());
		}
	}




	public Set refresh(Set storableObjects) throws CommunicationException, DatabaseException {
		if (storableObjects.isEmpty())
			return Collections.EMPTY_SET;

		short entityCode = ((StorableObject) storableObjects.iterator().next()).getId().getMajor();

		try {
			StorableObjectDatabase database = ConfigurationDatabaseContext.getDatabase(entityCode);

			if (database != null)
				return database.refresh(storableObjects);

			return Collections.EMPTY_SET;
		}
		catch (DatabaseException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.refresh | DatabaseException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.refresh | DatabaseException: " + e.getMessage());
		}
	}




	public void delete(Identifier id) throws CommunicationException, DatabaseException {
		delete(id, null);
	}

	public void delete(List objects) throws CommunicationException, DatabaseException, IllegalDataException {
		if (objects == null || objects.isEmpty())
			return;
		/**
		 * TODO: use Trove collection instead java.util.Map
		 */
		Map map = new HashMap();

		/**
		 * separate objects by kind of entity
		 */
		for (Iterator it = objects.iterator(); it.hasNext();) {
			Object object = it.next();
			Identifier identifier = null;
			if (object instanceof Identifier)
				identifier = (Identifier) object;
			else
				if (object instanceof Identified)
					identifier = ((Identified) object).getId();
				else
					throw new IllegalDataException("DatabaseConfigurationObjectLoader.delete | Object "
							+ object.getClass().getName()
							+ " isn't Identifier or Identified");
			Short entityCode = new Short(identifier.getMajor());
			List list = (List) map.get(entityCode);
			if (list == null) {
				list = new LinkedList();
				map.put(entityCode, list);
			}
			list.add(object);
		}

		for (Iterator it = map.keySet().iterator(); it.hasNext();) {
			Short entityCode = (Short) it.next();
			List list = (List) map.get(entityCode);
			this.delete(null, list);
		}
	}

	private void delete(Identifier id, List objects) throws DatabaseException {
		short entityCode = (id != null) ? id.getMajor() : 0;
		if (id == null) {
			if (objects.isEmpty())
				return;
			Object obj = objects.iterator().next();
			if (obj instanceof Identifier)
				entityCode = ((Identifier) obj).getMajor();
			else
				if (obj instanceof Identified)
					entityCode = ((Identified) obj).getId().getMajor();
		}
		try {
			StorableObjectDatabase database = ConfigurationDatabaseContext.getDatabase(entityCode);
			if (database != null) {
				if (id != null)
					database.delete(id);
				else
					if (objects != null && !objects.isEmpty()) {
						database.delete(objects);
					}
			}
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseConfigurationObjectLoader.delete | IllegalDataException: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
	}

}
