/*
 * $Id: DatabaseConfigurationObjectLoader.java,v 1.47 2005/04/05 10:32:44 arseniy Exp $
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

import com.syrus.AMFICOM.general.AbstractObjectLoader;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.SessionContext;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @version $Revision: 1.47 $, $Date: 2005/04/05 10:32:44 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public class DatabaseConfigurationObjectLoader extends AbstractObjectLoader implements ConfigurationObjectLoader {

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

	public Set loadTransmissionPathTypes(Set ids) throws ApplicationException {
		TransmissionPathTypeDatabase database = ConfigurationDatabaseContext.getTransmissionPathTypeDatabase();
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




	/* Load Configuration StorableObject but argument ids */

	public Set loadCableLinkTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CableLinkTypeDatabase database = ConfigurationDatabaseContext.getCableLinkTypeDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadCableThreadsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CableThreadDatabase database = ConfigurationDatabaseContext.getCableThreadDatabase();
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

	public Set loadEquipmentTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		EquipmentTypeDatabase database = ConfigurationDatabaseContext.getEquipmentTypeDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadKISsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		KISDatabase database = ConfigurationDatabaseContext.getKISDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadLinksButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		LinkDatabase database = ConfigurationDatabaseContext.getLinkDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadLinkTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		LinkTypeDatabase database = ConfigurationDatabaseContext.getLinkTypeDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadMeasurementPortsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		MeasurementPortDatabase database = ConfigurationDatabaseContext.getMeasurementPortDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadMeasurementPortTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		MeasurementPortTypeDatabase database = ConfigurationDatabaseContext.getMeasurementPortTypeDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadMonitoredElementsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		MonitoredElementDatabase database = ConfigurationDatabaseContext.getMonitoredElementDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadPortsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		PortDatabase database = ConfigurationDatabaseContext.getPortDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadPortTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		PortTypeDatabase database = ConfigurationDatabaseContext.getPortTypeDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadTransmissionPathsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		TransmissionPathDatabase database = ConfigurationDatabaseContext.getTransmissionPathDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadTransmissionPathTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		TransmissionPathTypeDatabase database = ConfigurationDatabaseContext.getTransmissionPathTypeDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$


	public void saveEquipmentType(EquipmentType equipmentType, boolean force) throws ApplicationException {
		EquipmentTypeDatabase database = ConfigurationDatabaseContext.getEquipmentTypeDatabase();
		database.update(equipmentType, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void savePortType(PortType portType, boolean force) throws ApplicationException {
		PortTypeDatabase database = ConfigurationDatabaseContext.getPortTypeDatabase();
		database.update(portType, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveMeasurementPortType(MeasurementPortType measurementPortType, boolean force) throws ApplicationException {
		MeasurementPortTypeDatabase database = ConfigurationDatabaseContext.getMeasurementPortTypeDatabase();
		database.update(measurementPortType, SessionContext.getAccessIdentity().getUserId(),
				force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveLinkType(LinkType linkType, boolean force) throws ApplicationException {
		LinkTypeDatabase database = ConfigurationDatabaseContext.getLinkTypeDatabase();
		database.update(linkType, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveCableLinkType(CableLinkType cableLinkType, boolean force) throws ApplicationException {
		StorableObjectDatabase database = ConfigurationDatabaseContext.getCableLinkTypeDatabase();
		database.update(cableLinkType, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveCableThreadType(CableThreadType cableThreadType, boolean force) throws ApplicationException {
		CableThreadTypeDatabase database = ConfigurationDatabaseContext.getCableThreadTypeDatabase();
		database.update(cableThreadType, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveEquipment(Equipment equipment, boolean force) throws ApplicationException {
		EquipmentDatabase database = ConfigurationDatabaseContext.getEquipmentDatabase();
		database.update(equipment, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void savePort(Port port, boolean force) throws ApplicationException {
		PortDatabase database = ConfigurationDatabaseContext.getPortDatabase();
		database.update(port, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveMeasurementPort(MeasurementPort measurementPort, boolean force)
			throws ApplicationException {
		MeasurementPortDatabase database = ConfigurationDatabaseContext.getMeasurementPortDatabase();
		database.update(measurementPort, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveTransmissionPath(TransmissionPath transmissionPath, boolean force)
			throws ApplicationException {
		TransmissionPathDatabase database = ConfigurationDatabaseContext.getTransmissionPathDatabase();
		database.update(transmissionPath, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveTransmissionPathType(TransmissionPathType transmissionPathType, boolean force)
			throws ApplicationException {
		TransmissionPathTypeDatabase database = ConfigurationDatabaseContext.getTransmissionPathTypeDatabase();
		database.update(transmissionPathType, SessionContext.getAccessIdentity().getUserId(),
				force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveKIS(KIS kis, boolean force) throws ApplicationException {
		KISDatabase database = ConfigurationDatabaseContext.getKISDatabase();
		database.update(kis, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveMonitoredElement(MonitoredElement monitoredElement, boolean force)
			throws ApplicationException {
		MonitoredElementDatabase database = ConfigurationDatabaseContext.getMonitoredElementDatabase();
		database.update(monitoredElement, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveLink(Link link, boolean force) throws ApplicationException {
		LinkDatabase database = ConfigurationDatabaseContext.getLinkDatabase();
		database.update(link, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveCableThread(CableThread cableThread, boolean force)
			throws ApplicationException {
		CableThreadDatabase database = ConfigurationDatabaseContext.getCableThreadDatabase();
		database.update(cableThread, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

//############################################################################
//############################################################################
//############################################################################


	public void saveCableLinkTypes(Set objects, boolean force) throws ApplicationException {
		StorableObjectDatabase database = ConfigurationDatabaseContext.getCableLinkTypeDatabase();
		database.update(objects, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveCableThreads(Set objects, boolean force) throws ApplicationException {
		StorableObjectDatabase database = ConfigurationDatabaseContext.getCableThreadDatabase();
		database.update(objects, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveCableThreadTypes(Set objects, boolean force) throws ApplicationException {
		CableThreadTypeDatabase database = ConfigurationDatabaseContext.getCableThreadTypeDatabase();
		database.update(objects, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveEquipments(Set objects, boolean force) throws ApplicationException {
		EquipmentDatabase database = ConfigurationDatabaseContext.getEquipmentDatabase();
		database.update(objects, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveEquipmentTypes(Set objects, boolean force) throws ApplicationException {
		EquipmentTypeDatabase database = ConfigurationDatabaseContext.getEquipmentTypeDatabase();
		database.update(objects, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveKISs(Set objects, boolean force) throws ApplicationException {
		KISDatabase database = ConfigurationDatabaseContext.getKISDatabase();
		database.update(objects, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveLinks(Set objects, boolean force) throws ApplicationException {
		LinkDatabase database = ConfigurationDatabaseContext.getLinkDatabase();
		database.update(objects, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveLinkTypes(Set objects, boolean force) throws ApplicationException {
		LinkTypeDatabase database = ConfigurationDatabaseContext.getLinkTypeDatabase();
		database.update(objects, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveMeasurementPorts(Set objects, boolean force) throws ApplicationException {
		MeasurementPortDatabase database = ConfigurationDatabaseContext.getMeasurementPortDatabase();
		database.update(objects, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveMeasurementPortTypes(Set objects, boolean force) throws ApplicationException {
		MeasurementPortTypeDatabase database = ConfigurationDatabaseContext.getMeasurementPortTypeDatabase();
		database.update(objects, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveMonitoredElements(Set objects, boolean force) throws ApplicationException {
		MonitoredElementDatabase database = ConfigurationDatabaseContext.getMonitoredElementDatabase();
		database.update(objects, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void savePorts(Set objects, boolean force) throws ApplicationException {
		PortDatabase database = ConfigurationDatabaseContext.getPortDatabase();
		database.update(objects, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void savePortTypes(Set objects, boolean force) throws ApplicationException {
		PortTypeDatabase database = ConfigurationDatabaseContext.getPortTypeDatabase();
		database.update(objects, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveTransmissionPaths(Set objects, boolean force) throws ApplicationException {
		TransmissionPathDatabase database = ConfigurationDatabaseContext.getTransmissionPathDatabase();
		database.update(objects, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveTransmissionPathTypes(Set objects, boolean force) throws ApplicationException {
		TransmissionPathTypeDatabase database = ConfigurationDatabaseContext.getTransmissionPathTypeDatabase();
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
