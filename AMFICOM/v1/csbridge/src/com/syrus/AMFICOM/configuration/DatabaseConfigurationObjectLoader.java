/*
 * $Id: DatabaseConfigurationObjectLoader.java,v 1.10 2005/06/22 19:29:31 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.10 $, $Date: 2005/06/22 19:29:31 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */

public class DatabaseConfigurationObjectLoader extends DatabaseObjectLoader implements ConfigurationObjectLoader {

	/* Load multiple objects*/

	public Set loadEquipmentTypes(final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadPortTypes(final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadMeasurementPortTypes(final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadTransmissionPathTypes(final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadLinkTypes(final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadCableLinkTypes(final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadCableThreadTypes(final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}



	public Set loadEquipments(final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadPorts(final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadMeasurementPorts(final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadTransmissionPaths(final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadKISs(final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadMonitoredElements(final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadLinks(final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadCableLinks(final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadCableThreads(final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}



	/* Load multiple objects but ids*/

	public Set loadEquipmentTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadPortTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadMeasurementPortTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadTransmissionPathTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadLinkTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadCableLinkTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadCableThreadTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}



	public Set loadEquipmentsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadPortsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadMeasurementPortsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadTransmissionPathsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadKISsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadMonitoredElementsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadLinksButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadCableLinksButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadCableThreadsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

//############################################################################
//############################################################################
//############################################################################

	/* Save multiple objects*/

	public void saveEquipmentTypes(final Set<EquipmentType> objects, final boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void savePortTypes(final Set<PortType> objects, final boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveMeasurementPortTypes(final Set<MeasurementPortType> objects, final boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveTransmissionPathTypes(final Set<TransmissionPathType> objects, final boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveLinkTypes(final Set<LinkType> objects, final boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveCableLinkTypes(final Set<CableLinkType> objects, final boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveCableThreadTypes(final Set<CableThreadType> objects, final boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}



	public void saveEquipments(final Set<Equipment> objects, final boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void savePorts(final Set<Port> objects, final boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveMeasurementPorts(final Set<MeasurementPort> objects, final boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveTransmissionPaths(final Set<TransmissionPath> objects, final boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveKISs(final Set<KIS> objects, final boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveMonitoredElements(final Set<MonitoredElement> objects, final boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveLinks(final Set<Link> objects, final boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveCableLinks(final Set<CableLink> objects, final boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveCableThreads(final Set<CableThread> objects, final boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}
}
