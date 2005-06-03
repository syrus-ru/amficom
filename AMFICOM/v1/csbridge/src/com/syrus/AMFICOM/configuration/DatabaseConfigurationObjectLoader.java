/*
 * $Id: DatabaseConfigurationObjectLoader.java,v 1.8 2005/06/03 15:23:58 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.8 $, $Date: 2005/06/03 15:23:58 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */

public class DatabaseConfigurationObjectLoader extends DatabaseObjectLoader implements ConfigurationObjectLoader {

	/* Load multiple objects*/

	public Set loadEquipmentTypes(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadPortTypes(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadMeasurementPortTypes(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadTransmissionPathTypes(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadLinkTypes(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadCableLinkTypes(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadCableThreadTypes(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadEquipments(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadPorts(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadMeasurementPorts(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadTransmissionPaths(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadKISs(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadMonitoredElements(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadLinks(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadCableThreads(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	/* Load multiple objects but ids*/

	public Set loadEquipmentTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIds(condition, ids);
	}

	public Set loadPortTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIds(condition, ids);
	}

	public Set loadMeasurementPortTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIds(condition, ids);
	}

	public Set loadTransmissionPathTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIds(condition, ids);
	}

	public Set loadLinkTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIds(condition, ids);
	}

	public Set loadCableLinkTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIds(condition, ids);
	}

	public Set loadCableThreadTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIds(condition, ids);
	}

	public Set loadEquipmentsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIds(condition, ids);
	}

	public Set loadPortsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIds(condition, ids);
	}

	public Set loadMeasurementPortsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIds(condition, ids);
	}

	public Set loadTransmissionPathsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIds(condition, ids);
	}

	public Set loadKISsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIds(condition, ids);
	}

	public Set loadMonitoredElementsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIds(condition, ids);
	}

	public Set loadLinksButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIds(condition, ids);
	}

	public Set loadCableThreadsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIds(condition, ids);
	}

//############################################################################
//############################################################################
//############################################################################

	/* Save multiple objects*/

	public void saveEquipmentTypes(Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void savePortTypes(Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveMeasurementPortTypes(Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveTransmissionPathTypes(Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveLinkTypes(Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveCableLinkTypes(Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveCableThreadTypes(Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveEquipments(Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void savePorts(Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveMeasurementPorts(Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveTransmissionPaths(Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveKISs(Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveMonitoredElements(Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveLinks(Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveCableThreads(final Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}
}
