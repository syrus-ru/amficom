/*-
 * $Id: DatabaseSchemeObjectLoader.java,v 1.9 2005/06/07 13:18:51 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.9 $, $Date: 2005/06/07 13:18:51 $
 * @module csbridge_v1
 */
public class DatabaseSchemeObjectLoader extends DatabaseObjectLoader implements SchemeObjectLoader {
	public Set loadCableChannelingItems(final Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadCableChannelingItemsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(storableObjectCondition, ids);
	}

	public Set loadPathElements(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadPathElementsButIds(StorableObjectCondition storableObjectCondition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(storableObjectCondition, ids);
	}

	public Set loadSchemeCableLinks(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadSchemeCableLinksButIds(StorableObjectCondition storableObjectCondition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(storableObjectCondition, ids);
	}

	public Set loadSchemeCablePorts(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadSchemeCablePortsButIds(StorableObjectCondition storableObjectCondition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(storableObjectCondition, ids);
	}

	public Set loadSchemeCableThreads(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadSchemeCableThreadsButIds(StorableObjectCondition storableObjectCondition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(storableObjectCondition, ids);
	}

	public Set loadSchemeDevices(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadSchemeDevicesButIds(StorableObjectCondition storableObjectCondition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(storableObjectCondition, ids);
	}

	public Set loadSchemeElements(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadSchemeElementsButIds(StorableObjectCondition storableObjectCondition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(storableObjectCondition, ids);
	}

	public Set loadSchemeLinks(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadSchemeLinksButIds(StorableObjectCondition storableObjectCondition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(storableObjectCondition, ids);
	}

	public Set loadSchemeMonitoringSolutions(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadSchemeMonitoringSolutionsButIds(StorableObjectCondition storableObjectCondition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(storableObjectCondition, ids);
	}

	public Set loadSchemeOptimizeInfoRtus(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadSchemeOptimizeInfoRtusButIds(StorableObjectCondition storableObjectCondition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(storableObjectCondition, ids);
	}

	public Set loadSchemeOptimizeInfos(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadSchemeOptimizeInfosButIds(StorableObjectCondition storableObjectCondition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(storableObjectCondition, ids);
	}

	public Set loadSchemeOptimizeInfoSwitches(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadSchemeOptimizeInfoSwitchesButIds(StorableObjectCondition storableObjectCondition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(storableObjectCondition, ids);
	}

	public Set loadSchemePaths(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadSchemePathsButIds(StorableObjectCondition storableObjectCondition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(storableObjectCondition, ids);
	}

	public Set loadSchemePorts(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadSchemePortsButIds(StorableObjectCondition storableObjectCondition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(storableObjectCondition, ids);
	}

	public Set loadSchemeProtoElements(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadSchemeProtoElementsButIds(StorableObjectCondition storableObjectCondition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(storableObjectCondition, ids);
	}

	public Set loadSchemeProtoGroups(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadSchemeProtoGroupsButIds(StorableObjectCondition storableObjectCondition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(storableObjectCondition, ids);
	}

	public Set loadSchemes(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadSchemesButIds(StorableObjectCondition storableObjectCondition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(storableObjectCondition, ids);
	}

	public void saveCableChannelingItems(Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void savePathElements(Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveSchemeCableLinks(Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveSchemeCablePorts(Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveSchemeCableThreads(Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveSchemeDevices(Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveSchemeElements(Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveSchemeLinks(Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveSchemeMonitoringSolutions(Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveSchemeOptimizeInfoRtus(Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveSchemeOptimizeInfos(Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveSchemeOptimizeInfoSwitches(Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveSchemePaths(Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveSchemePorts(Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveSchemeProtoElements(Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveSchemeProtoGroups(Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveSchemes(Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}
}
