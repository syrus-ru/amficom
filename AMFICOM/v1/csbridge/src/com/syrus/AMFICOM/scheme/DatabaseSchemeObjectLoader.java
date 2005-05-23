/*-
 * $Id: DatabaseSchemeObjectLoader.java,v 1.5 2005/05/23 18:45:12 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/05/23 18:45:12 $
 * @module csbridge_v1
 */
public class DatabaseSchemeObjectLoader extends DatabaseObjectLoader implements SchemeObjectLoader {
	/**
	 * @param identifiables
	 * @see SchemeObjectLoader#delete(Set)
	 */
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
			final StorableObjectDatabase storableObjectDatabase = SchemeDatabaseContext.getDatabase(entityCode);
			if (storableObjectDatabase != null)
				storableObjectDatabase.delete(entityObjects);
		}
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadCableChannelingItems(Set)
	 */
	public Set loadCableChannelingItems(final Set ids) throws ApplicationException {
		try {
			return ((CableChannelingItemDatabase) SchemeDatabaseContext.getDatabase(ObjectEntities.CABLE_CHANNELING_ITEM_ENTITY_CODE)).retrieveByIdsByCondition(ids, null);
		} catch (final IllegalDataException ide) {
			throw new DatabaseException("DatabaseSchemeObjectLoader.loadCableChannelingItems | Illegal Storable Object: " + ide.getMessage());
		}
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @see SchemeObjectLoader#loadCableChannelingItemsButIds(StorableObjectCondition, Set)
	 */
	public Set loadCableChannelingItemsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) {
//		try {
//			return SchemeDatabaseContext.getCableChannelingItemDatabase()
//		}
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadPathElements(java.util.Set)
	 */
	public Set loadPathElements(Set ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadPathElementsButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set loadPathElementsButIds(
			StorableObjectCondition storableObjectCondition,
			Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeCableLinks(java.util.Set)
	 */
	public Set loadSchemeCableLinks(Set ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeCableLinksButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set loadSchemeCableLinksButIds(
			StorableObjectCondition storableObjectCondition,
			Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeCablePorts(java.util.Set)
	 */
	public Set loadSchemeCablePorts(Set ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeCablePortsButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set loadSchemeCablePortsButIds(
			StorableObjectCondition storableObjectCondition,
			Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeCableThreads(java.util.Set)
	 */
	public Set loadSchemeCableThreads(Set ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeCableThreadsButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set loadSchemeCableThreadsButIds(
			StorableObjectCondition storableObjectCondition,
			Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeDevices(java.util.Set)
	 */
	public Set loadSchemeDevices(Set ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeDevicesButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set loadSchemeDevicesButIds(
			StorableObjectCondition storableObjectCondition,
			Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeElements(java.util.Set)
	 */
	public Set loadSchemeElements(Set ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeElementsButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set loadSchemeElementsButIds(
			StorableObjectCondition storableObjectCondition,
			Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeLinks(java.util.Set)
	 */
	public Set loadSchemeLinks(Set ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeLinksButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set loadSchemeLinksButIds(
			StorableObjectCondition storableObjectCondition,
			Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeMonitoringSolutions(java.util.Set)
	 */
	public Set loadSchemeMonitoringSolutions(Set ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeMonitoringSolutionsButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set loadSchemeMonitoringSolutionsButIds(
			StorableObjectCondition storableObjectCondition,
			Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeOptimizeInfoRtus(java.util.Set)
	 */
	public Set loadSchemeOptimizeInfoRtus(Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeOptimizeInfoRtusButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set loadSchemeOptimizeInfoRtusButIds(StorableObjectCondition storableObjectCondition, Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeOptimizeInfos(java.util.Set)
	 */
	public Set loadSchemeOptimizeInfos(Set ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeOptimizeInfosButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set loadSchemeOptimizeInfosButIds(
			StorableObjectCondition storableObjectCondition,
			Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeOptimizeInfoSwitches(java.util.Set)
	 */
	public Set loadSchemeOptimizeInfoSwitches(Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeOptimizeInfoSwitchesButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set loadSchemeOptimizeInfoSwitchesButIds(StorableObjectCondition storableObjectCondition, Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemePaths(java.util.Set)
	 */
	public Set loadSchemePaths(Set ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemePathsButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set loadSchemePathsButIds(
			StorableObjectCondition storableObjectCondition,
			Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemePorts(java.util.Set)
	 */
	public Set loadSchemePorts(Set ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemePortsButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set loadSchemePortsButIds(
			StorableObjectCondition storableObjectCondition,
			Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeProtoElements(java.util.Set)
	 */
	public Set loadSchemeProtoElements(Set ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeProtoElementsButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set loadSchemeProtoElementsButIds(
			StorableObjectCondition storableObjectCondition,
			Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeProtoGroups(java.util.Set)
	 */
	public Set loadSchemeProtoGroups(Set ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeProtoGroupsButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set loadSchemeProtoGroupsButIds(
			StorableObjectCondition storableObjectCondition,
			Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemes(java.util.Set)
	 */
	public Set loadSchemes(Set ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemesButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set loadSchemesButIds(
			StorableObjectCondition storableObjectCondition,
			Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjects
	 * @throws CommunicationException
	 * @throws DatabaseException
	 * @see SchemeObjectLoader#refresh(Set)
	 */
	public Set refresh(final Set storableObjects) throws CommunicationException, DatabaseException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param cableChannelingItems
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveCableChannelingItems(java.util.Set, boolean)
	 */
	public void saveCableChannelingItems(Set cableChannelingItems,
			boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param pathElements
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#savePathElements(java.util.Set, boolean)
	 */
	public void savePathElements(Set pathElements, boolean force)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeCableLinks
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemeCableLinks(java.util.Set, boolean)
	 */
	public void saveSchemeCableLinks(Set schemeCableLinks,
			boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeCablePorts
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemeCablePorts(java.util.Set, boolean)
	 */
	public void saveSchemeCablePorts(Set schemeCablePorts,
			boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeCableThreads
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemeCableThreads(java.util.Set, boolean)
	 */
	public void saveSchemeCableThreads(Set schemeCableThreads,
			boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeDevices
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemeDevices(java.util.Set, boolean)
	 */
	public void saveSchemeDevices(Set schemeDevices, boolean force)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeElements
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemeElements(java.util.Set, boolean)
	 */
	public void saveSchemeElements(Set schemeElements, boolean force)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeLinks
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemeLinks(java.util.Set, boolean)
	 */
	public void saveSchemeLinks(Set schemeLinks, boolean force)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeMonitoringSolutions
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemeMonitoringSolutions(java.util.Set, boolean)
	 */
	public void saveSchemeMonitoringSolutions(
			Set schemeMonitoringSolutions, boolean force)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeOptimizeInfoRtus
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemeOptimizeInfoRtus(java.util.Set, boolean)
	 */
	public void saveSchemeOptimizeInfoRtus(Set schemeOptimizeInfoRtus, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeOptimizeInfos
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemeOptimizeInfos(java.util.Set, boolean)
	 */
	public void saveSchemeOptimizeInfos(Set schemeOptimizeInfos,
			boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeOptimizeInfoSwitches
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemeOptimizeInfoSwitches(java.util.Set, boolean)
	 */
	public void saveSchemeOptimizeInfoSwitches(Set schemeOptimizeInfoSwitches, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemePaths
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemePaths(java.util.Set, boolean)
	 */
	public void saveSchemePaths(Set schemePaths, boolean force)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemePorts
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemePorts(java.util.Set, boolean)
	 */
	public void saveSchemePorts(Set schemePorts, boolean force)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeProtoElement
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemeProtoElement(com.syrus.AMFICOM.scheme.SchemeProtoElement, boolean)
	 */
	public void saveSchemeProtoElement(
			SchemeProtoElement schemeProtoElement, boolean force)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeProtoElements
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemeProtoElements(java.util.Set, boolean)
	 */
	public void saveSchemeProtoElements(Set schemeProtoElements,
			boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeProtoGroups
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemeProtoGroups(java.util.Set, boolean)
	 */
	public void saveSchemeProtoGroups(Set schemeProtoGroups,
			boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemes
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemes(java.util.Set, boolean)
	 */
	public void saveSchemes(Set schemes, boolean force)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}
}
