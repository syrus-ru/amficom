/*
 * $Id: DatabaseConfigurationObjectLoader.java,v 1.5 2005/05/23 18:45:12 bass Exp $
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
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @version $Revision: 1.5 $, $Date: 2005/05/23 18:45:12 $
 * @author $Author: bass $
 * @module csbridge_v1
 */

public class DatabaseConfigurationObjectLoader extends DatabaseObjectLoader implements ConfigurationObjectLoader {

	/* Load multiple objects*/

	public Set loadEquipmentTypes(Set ids) throws ApplicationException {
		EquipmentTypeDatabase database = (EquipmentTypeDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadPortTypes(Set ids) throws ApplicationException {
		PortTypeDatabase database = (PortTypeDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.PORTTYPE_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadMeasurementPortTypes(Set ids) throws ApplicationException {
		MeasurementPortTypeDatabase database = (MeasurementPortTypeDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadTransmissionPathTypes(Set ids) throws ApplicationException {
		TransmissionPathTypeDatabase database = (TransmissionPathTypeDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.TRANSPATHTYPE_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadLinkTypes(Set ids) throws ApplicationException {
		LinkTypeDatabase database = (LinkTypeDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.LINKTYPE_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadCableLinkTypes(Set ids) throws ApplicationException {
		CableLinkTypeDatabase database = (CableLinkTypeDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.CABLELINKTYPE_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadCableThreadTypes(Set ids) throws ApplicationException {
		CableThreadTypeDatabase database = (CableThreadTypeDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.CABLETHREADTYPE_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}



	public Set loadEquipments(Set ids) throws ApplicationException {
		EquipmentDatabase database = (EquipmentDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.EQUIPMENT_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadPorts(Set ids) throws ApplicationException {
		PortDatabase database = (PortDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.PORT_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadMeasurementPorts(Set ids) throws ApplicationException {
		MeasurementPortDatabase database = (MeasurementPortDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.MEASUREMENTPORT_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadTransmissionPaths(Set ids) throws ApplicationException {
		TransmissionPathDatabase database = (TransmissionPathDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.TRANSPATH_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadKISs(Set ids) throws ApplicationException {
		KISDatabase database = (KISDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.KIS_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadMonitoredElements(Set ids) throws ApplicationException {
		MonitoredElementDatabase database = (MonitoredElementDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.MONITORED_ELEMENT_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadLinks(Set ids) throws ApplicationException {
		LinkDatabase database = (LinkDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.LINK_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadCableThreads(Set ids) throws ApplicationException {
		CableThreadDatabase database = (CableThreadDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.CABLETHREAD_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}



	/* Load multiple objects but ids*/

	public Set loadEquipmentTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		EquipmentTypeDatabase database = (EquipmentTypeDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadPortTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		PortTypeDatabase database = (PortTypeDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.PORTTYPE_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadMeasurementPortTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		MeasurementPortTypeDatabase database = (MeasurementPortTypeDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadTransmissionPathTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		TransmissionPathTypeDatabase database = (TransmissionPathTypeDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.TRANSPATHTYPE_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadLinkTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		LinkTypeDatabase database = (LinkTypeDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.LINKTYPE_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadCableLinkTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CableLinkTypeDatabase database = (CableLinkTypeDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.CABLELINKTYPE_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadCableThreadTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CableThreadTypeDatabase database = (CableThreadTypeDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.CABLETHREADTYPE_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}



	public Set loadEquipmentsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		EquipmentDatabase database = (EquipmentDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.EQUIPMENT_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadPortsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		PortDatabase database = (PortDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.PORT_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadMeasurementPortsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		MeasurementPortDatabase database = (MeasurementPortDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.MEASUREMENTPORT_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadTransmissionPathsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		TransmissionPathDatabase database = (TransmissionPathDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.TRANSPATH_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadKISsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		KISDatabase database = (KISDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.KIS_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadMonitoredElementsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		MonitoredElementDatabase database = (MonitoredElementDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.MONITORED_ELEMENT_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadLinksButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		LinkDatabase database = (LinkDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.LINK_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadCableThreadsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CableThreadDatabase database = (CableThreadDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.CABLETHREAD_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

//############################################################################
//############################################################################
//############################################################################

	/* Save multiple objects*/

	public void saveEquipmentTypes(Set objects, boolean force) throws ApplicationException {
		EquipmentTypeDatabase database = (EquipmentTypeDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE);
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void savePortTypes(Set objects, boolean force) throws ApplicationException {
		PortTypeDatabase database = (PortTypeDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.PORTTYPE_ENTITY_CODE);
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveMeasurementPortTypes(Set objects, boolean force) throws ApplicationException {
		MeasurementPortTypeDatabase database = (MeasurementPortTypeDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE);
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveTransmissionPathTypes(Set objects, boolean force) throws ApplicationException {
		TransmissionPathTypeDatabase database = (TransmissionPathTypeDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.TRANSPATHTYPE_ENTITY_CODE);
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveLinkTypes(Set objects, boolean force) throws ApplicationException {
		LinkTypeDatabase database = (LinkTypeDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.LINKTYPE_ENTITY_CODE);
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveCableLinkTypes(Set objects, boolean force) throws ApplicationException {
		StorableObjectDatabase database = (CableLinkTypeDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.CABLELINKTYPE_ENTITY_CODE);
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveCableThreadTypes(Set objects, boolean force) throws ApplicationException {
		CableThreadTypeDatabase database = (CableThreadTypeDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.CABLETHREADTYPE_ENTITY_CODE);
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}



	public void saveEquipments(Set objects, boolean force) throws ApplicationException {
		EquipmentDatabase database = (EquipmentDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.EQUIPMENT_ENTITY_CODE);
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void savePorts(Set objects, boolean force) throws ApplicationException {
		PortDatabase database = (PortDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.PORT_ENTITY_CODE);
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveMeasurementPorts(Set objects, boolean force) throws ApplicationException {
		MeasurementPortDatabase database = (MeasurementPortDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.MEASUREMENTPORT_ENTITY_CODE);
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveTransmissionPaths(Set objects, boolean force) throws ApplicationException {
		TransmissionPathDatabase database = (TransmissionPathDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.TRANSPATH_ENTITY_CODE);
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveKISs(Set objects, boolean force) throws ApplicationException {
		KISDatabase database = (KISDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.KIS_ENTITY_CODE);
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveMonitoredElements(Set objects, boolean force) throws ApplicationException {
		MonitoredElementDatabase database = (MonitoredElementDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.MONITORED_ELEMENT_ENTITY_CODE);
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveLinks(Set objects, boolean force) throws ApplicationException {
		LinkDatabase database = (LinkDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.LINK_ENTITY_CODE);
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveCableThreads(Set objects, boolean force) throws ApplicationException {
		StorableObjectDatabase database = (CableThreadDatabase) ConfigurationDatabaseContext.getDatabase(ObjectEntities.CABLETHREAD_ENTITY_CODE);
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}



	/*	Refresh*/

	public Set refresh(Set storableObjects) throws ApplicationException {
		if (storableObjects.isEmpty())
			return Collections.EMPTY_SET;

		short entityCode = ((StorableObject) storableObjects.iterator().next()).getId().getMajor();

		StorableObjectDatabase database = ConfigurationDatabaseContext.getDatabase(entityCode);

		if (database != null)
			return database.refresh(storableObjects);

		return Collections.EMPTY_SET;
	}



	/*	Delete*/

	public void delete(final Set identifiables) {
		if (identifiables == null || identifiables.isEmpty())
			return;
		/**
		 * @todo: use Trove collection instead java.util.Map
		 */
		final Map map = new HashMap();

		/**
		 * separate objects by kind of entity
		 */
		for (final Iterator identifiableIterator = identifiables.iterator(); identifiableIterator.hasNext();) {
			final Identifiable identifiable = (Identifiable) identifiableIterator.next();
			final Short entityCode = new Short(identifiable.getId().getMajor());
			Set entityObjects = (Set) map.get(entityCode);
			if (entityObjects == null) {
				entityObjects = new HashSet();
				map.put(entityCode, entityObjects);
			}
			entityObjects.add(identifiable);
		}

		for (final Iterator entityCodeIterator = map.keySet().iterator(); entityCodeIterator.hasNext();) {
			final Short entityCode = (Short) entityCodeIterator.next();
			final Set entityObjects = (Set) map.get(entityCode);
			final StorableObjectDatabase storableObjectDatabase = ConfigurationDatabaseContext.getDatabase(entityCode);
			if (storableObjectDatabase != null)
				storableObjectDatabase.delete(entityObjects);
		}
	}

}
