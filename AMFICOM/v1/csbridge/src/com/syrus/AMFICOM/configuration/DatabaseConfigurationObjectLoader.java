/*
 * $Id: DatabaseConfigurationObjectLoader.java,v 1.1 2005/04/27 13:42:22 arseniy Exp $
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
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @version $Revision: 1.1 $, $Date: 2005/04/27 13:42:22 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public class DatabaseConfigurationObjectLoader extends DatabaseObjectLoader implements ConfigurationObjectLoader {

	/* Load single object*/

	public EquipmentType loadEquipmentType(Identifier id) throws ApplicationException {
		return new EquipmentType(id);
	}

	public PortType loadPortType(Identifier id) throws ApplicationException {
		return new PortType(id);
	}

	public MeasurementPortType loadMeasurementPortType(Identifier id) throws ApplicationException {
		return new MeasurementPortType(id);
	}

	public TransmissionPathType loadTransmissionPathType(Identifier id) throws ApplicationException {
		return new TransmissionPathType(id);
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



	/* Load multiple objects*/

	public Set loadEquipmentTypes(Set ids) throws ApplicationException {
		EquipmentTypeDatabase database = ConfigurationDatabaseContext.getEquipmentTypeDatabase();
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadPortTypes(Set ids) throws ApplicationException {
		PortTypeDatabase database = ConfigurationDatabaseContext.getPortTypeDatabase();
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadMeasurementPortTypes(Set ids) throws ApplicationException {
		MeasurementPortTypeDatabase database = ConfigurationDatabaseContext.getMeasurementPortTypeDatabase();
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadTransmissionPathTypes(Set ids) throws ApplicationException {
		TransmissionPathTypeDatabase database = ConfigurationDatabaseContext.getTransmissionPathTypeDatabase();
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadLinkTypes(Set ids) throws ApplicationException {
		LinkTypeDatabase database = ConfigurationDatabaseContext.getLinkTypeDatabase();
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadCableLinkTypes(Set ids) throws ApplicationException {
		CableLinkTypeDatabase database = ConfigurationDatabaseContext.getCableLinkTypeDatabase();
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadCableThreadTypes(Set ids) throws ApplicationException {
		CableThreadTypeDatabase database = ConfigurationDatabaseContext.getCableThreadTypeDatabase();
		return super.retrieveFromDatabase(database, ids);
	}



	public Set loadEquipments(Set ids) throws ApplicationException {
		EquipmentDatabase database = ConfigurationDatabaseContext.getEquipmentDatabase();
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadPorts(Set ids) throws ApplicationException {
		PortDatabase database = ConfigurationDatabaseContext.getPortDatabase();
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadMeasurementPorts(Set ids) throws ApplicationException {
		MeasurementPortDatabase database = ConfigurationDatabaseContext.getMeasurementPortDatabase();
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadTransmissionPaths(Set ids) throws ApplicationException {
		TransmissionPathDatabase database = ConfigurationDatabaseContext.getTransmissionPathDatabase();
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadKISs(Set ids) throws ApplicationException {
		KISDatabase database = ConfigurationDatabaseContext.getKISDatabase();
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadMonitoredElements(Set ids) throws ApplicationException {
		MonitoredElementDatabase database = ConfigurationDatabaseContext.getMonitoredElementDatabase();
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadLinks(Set ids) throws ApplicationException {
		LinkDatabase database = ConfigurationDatabaseContext.getLinkDatabase();
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadCableThreads(Set ids) throws ApplicationException {
		CableThreadDatabase database = ConfigurationDatabaseContext.getCableThreadDatabase();
		return super.retrieveFromDatabase(database, ids);
	}



	/* Load multiple objects but ids*/

	public Set loadEquipmentTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		EquipmentTypeDatabase database = ConfigurationDatabaseContext.getEquipmentTypeDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadPortTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		PortTypeDatabase database = ConfigurationDatabaseContext.getPortTypeDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadMeasurementPortTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		MeasurementPortTypeDatabase database = ConfigurationDatabaseContext.getMeasurementPortTypeDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadTransmissionPathTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		TransmissionPathTypeDatabase database = ConfigurationDatabaseContext.getTransmissionPathTypeDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadLinkTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		LinkTypeDatabase database = ConfigurationDatabaseContext.getLinkTypeDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadCableLinkTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CableLinkTypeDatabase database = ConfigurationDatabaseContext.getCableLinkTypeDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadCableThreadTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CableThreadTypeDatabase database = ConfigurationDatabaseContext.getCableThreadTypeDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}



	public Set loadEquipmentsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		EquipmentDatabase database = ConfigurationDatabaseContext.getEquipmentDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadPortsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		PortDatabase database = ConfigurationDatabaseContext.getPortDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadMeasurementPortsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		MeasurementPortDatabase database = ConfigurationDatabaseContext.getMeasurementPortDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadTransmissionPathsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		TransmissionPathDatabase database = ConfigurationDatabaseContext.getTransmissionPathDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadKISsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		KISDatabase database = ConfigurationDatabaseContext.getKISDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadMonitoredElementsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		MonitoredElementDatabase database = ConfigurationDatabaseContext.getMonitoredElementDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadLinksButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		LinkDatabase database = ConfigurationDatabaseContext.getLinkDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadCableThreadsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CableThreadDatabase database = ConfigurationDatabaseContext.getCableThreadDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

	/* Save single object*/

	public void saveEquipmentType(EquipmentType equipmentType, boolean force) throws ApplicationException {
		EquipmentTypeDatabase database = ConfigurationDatabaseContext.getEquipmentTypeDatabase();
		database.update(equipmentType, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void savePortType(PortType portType, boolean force) throws ApplicationException {
		PortTypeDatabase database = ConfigurationDatabaseContext.getPortTypeDatabase();
		database.update(portType, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveMeasurementPortType(MeasurementPortType measurementPortType, boolean force) throws ApplicationException {
		MeasurementPortTypeDatabase database = ConfigurationDatabaseContext.getMeasurementPortTypeDatabase();
		database.update(measurementPortType, userId,
				force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveTransmissionPathType(TransmissionPathType transmissionPathType, boolean force)
			throws ApplicationException {
		TransmissionPathTypeDatabase database = ConfigurationDatabaseContext.getTransmissionPathTypeDatabase();
		database.update(transmissionPathType, userId,
				force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveLinkType(LinkType linkType, boolean force) throws ApplicationException {
		LinkTypeDatabase database = ConfigurationDatabaseContext.getLinkTypeDatabase();
		database.update(linkType, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveCableLinkType(CableLinkType cableLinkType, boolean force) throws ApplicationException {
		StorableObjectDatabase database = ConfigurationDatabaseContext.getCableLinkTypeDatabase();
		database.update(cableLinkType, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveCableThreadType(CableThreadType cableThreadType, boolean force) throws ApplicationException {
		CableThreadTypeDatabase database = ConfigurationDatabaseContext.getCableThreadTypeDatabase();
		database.update(cableThreadType, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}



	public void saveEquipment(Equipment equipment, boolean force) throws ApplicationException {
		EquipmentDatabase database = ConfigurationDatabaseContext.getEquipmentDatabase();
		database.update(equipment, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void savePort(Port port, boolean force) throws ApplicationException {
		PortDatabase database = ConfigurationDatabaseContext.getPortDatabase();
		database.update(port, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveMeasurementPort(MeasurementPort measurementPort, boolean force)
			throws ApplicationException {
		MeasurementPortDatabase database = ConfigurationDatabaseContext.getMeasurementPortDatabase();
		database.update(measurementPort, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveTransmissionPath(TransmissionPath transmissionPath, boolean force)
			throws ApplicationException {
		TransmissionPathDatabase database = ConfigurationDatabaseContext.getTransmissionPathDatabase();
		database.update(transmissionPath, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveKIS(KIS kis, boolean force) throws ApplicationException {
		KISDatabase database = ConfigurationDatabaseContext.getKISDatabase();
		database.update(kis, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveMonitoredElement(MonitoredElement monitoredElement, boolean force)
			throws ApplicationException {
		MonitoredElementDatabase database = ConfigurationDatabaseContext.getMonitoredElementDatabase();
		database.update(monitoredElement, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveLink(Link link, boolean force) throws ApplicationException {
		LinkDatabase database = ConfigurationDatabaseContext.getLinkDatabase();
		database.update(link, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveCableThread(CableThread cableThread, boolean force)
			throws ApplicationException {
		CableThreadDatabase database = ConfigurationDatabaseContext.getCableThreadDatabase();
		database.update(cableThread, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

//############################################################################
//############################################################################
//############################################################################

	/* Save multiple objects*/

	public void saveEquipmentTypes(Set objects, boolean force) throws ApplicationException {
		EquipmentTypeDatabase database = ConfigurationDatabaseContext.getEquipmentTypeDatabase();
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void savePortTypes(Set objects, boolean force) throws ApplicationException {
		PortTypeDatabase database = ConfigurationDatabaseContext.getPortTypeDatabase();
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveMeasurementPortTypes(Set objects, boolean force) throws ApplicationException {
		MeasurementPortTypeDatabase database = ConfigurationDatabaseContext.getMeasurementPortTypeDatabase();
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveTransmissionPathTypes(Set objects, boolean force) throws ApplicationException {
		TransmissionPathTypeDatabase database = ConfigurationDatabaseContext.getTransmissionPathTypeDatabase();
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveLinkTypes(Set objects, boolean force) throws ApplicationException {
		LinkTypeDatabase database = ConfigurationDatabaseContext.getLinkTypeDatabase();
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveCableLinkTypes(Set objects, boolean force) throws ApplicationException {
		StorableObjectDatabase database = ConfigurationDatabaseContext.getCableLinkTypeDatabase();
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveCableThreadTypes(Set objects, boolean force) throws ApplicationException {
		CableThreadTypeDatabase database = ConfigurationDatabaseContext.getCableThreadTypeDatabase();
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}



	public void saveEquipments(Set objects, boolean force) throws ApplicationException {
		EquipmentDatabase database = ConfigurationDatabaseContext.getEquipmentDatabase();
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void savePorts(Set objects, boolean force) throws ApplicationException {
		PortDatabase database = ConfigurationDatabaseContext.getPortDatabase();
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveMeasurementPorts(Set objects, boolean force) throws ApplicationException {
		MeasurementPortDatabase database = ConfigurationDatabaseContext.getMeasurementPortDatabase();
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveTransmissionPaths(Set objects, boolean force) throws ApplicationException {
		TransmissionPathDatabase database = ConfigurationDatabaseContext.getTransmissionPathDatabase();
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveKISs(Set objects, boolean force) throws ApplicationException {
		KISDatabase database = ConfigurationDatabaseContext.getKISDatabase();
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveMonitoredElements(Set objects, boolean force) throws ApplicationException {
		MonitoredElementDatabase database = ConfigurationDatabaseContext.getMonitoredElementDatabase();
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveLinks(Set objects, boolean force) throws ApplicationException {
		LinkDatabase database = ConfigurationDatabaseContext.getLinkDatabase();
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveCableThreads(Set objects, boolean force) throws ApplicationException {
		StorableObjectDatabase database = ConfigurationDatabaseContext.getCableThreadDatabase();
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
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




	public void delete(Identifier id) {
		short entityCode = id.getMajor();
		StorableObjectDatabase storableObjectDatabase = ConfigurationDatabaseContext.getDatabase(entityCode);
		if (storableObjectDatabase != null)
			storableObjectDatabase.delete(id);
	}

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
