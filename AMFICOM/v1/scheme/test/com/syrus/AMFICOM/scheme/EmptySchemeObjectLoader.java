/*-
 * $Id: EmptySchemeObjectLoader.java,v 1.1 2005/07/15 08:47:17 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Collections;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/07/15 08:47:17 $
 * @module scheme_v1
 */
final class EmptySchemeObjectLoader implements SchemeObjectLoader {

	/**
	 * @param objects
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#delete(java.util.Set)
	 */
	public void delete(Set< ? extends Identifiable> objects) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadCableChannelingItems(java.util.Set)
	 */
	public Set<CableChannelingItem> loadCableChannelingItems(
			Set<Identifier> ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadCableChannelingItemsButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set<CableChannelingItem> loadCableChannelingItemsButIds(
			StorableObjectCondition storableObjectCondition,
			Set<Identifier> ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadPathElements(java.util.Set)
	 */
	public Set<PathElement> loadPathElements(Set<Identifier> ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadPathElementsButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set<PathElement> loadPathElementsButIds(
			StorableObjectCondition storableObjectCondition,
			Set<Identifier> ids) throws ApplicationException {
		return Collections.emptySet();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeCableLinks(java.util.Set)
	 */
	public Set<SchemeCableLink> loadSchemeCableLinks(Set<Identifier> ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeCableLinksButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set<SchemeCableLink> loadSchemeCableLinksButIds(
			StorableObjectCondition storableObjectCondition,
			Set<Identifier> ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeCablePorts(java.util.Set)
	 */
	public Set<SchemeCablePort> loadSchemeCablePorts(Set<Identifier> ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeCablePortsButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set<SchemeCablePort> loadSchemeCablePortsButIds(
			StorableObjectCondition storableObjectCondition,
			Set<Identifier> ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeCableThreads(java.util.Set)
	 */
	public Set<SchemeCableThread> loadSchemeCableThreads(Set<Identifier> ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeCableThreadsButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set<SchemeCableThread> loadSchemeCableThreadsButIds(
			StorableObjectCondition storableObjectCondition,
			Set<Identifier> ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeDevices(java.util.Set)
	 */
	public Set<SchemeDevice> loadSchemeDevices(Set<Identifier> ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeDevicesButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set<SchemeDevice> loadSchemeDevicesButIds(
			StorableObjectCondition storableObjectCondition,
			Set<Identifier> ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeElements(java.util.Set)
	 */
	public Set<SchemeElement> loadSchemeElements(Set<Identifier> ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeElementsButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set<SchemeElement> loadSchemeElementsButIds(
			StorableObjectCondition storableObjectCondition,
			Set<Identifier> ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeLinks(java.util.Set)
	 */
	public Set<SchemeLink> loadSchemeLinks(Set<Identifier> ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeLinksButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set<SchemeLink> loadSchemeLinksButIds(
			StorableObjectCondition storableObjectCondition,
			Set<Identifier> ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeMonitoringSolutions(java.util.Set)
	 */
	public Set<SchemeMonitoringSolution> loadSchemeMonitoringSolutions(
			Set<Identifier> ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeMonitoringSolutionsButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set<SchemeMonitoringSolution> loadSchemeMonitoringSolutionsButIds(
			StorableObjectCondition storableObjectCondition,
			Set<Identifier> ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeOptimizeInfos(java.util.Set)
	 */
	public Set<SchemeOptimizeInfo> loadSchemeOptimizeInfos(
			Set<Identifier> ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeOptimizeInfosButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set<SchemeOptimizeInfo> loadSchemeOptimizeInfosButIds(
			StorableObjectCondition storableObjectCondition,
			Set<Identifier> ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeOptimizeInfoSwitches(java.util.Set)
	 */
	public Set<SchemeOptimizeInfoSwitch> loadSchemeOptimizeInfoSwitches(
			Set<Identifier> ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeOptimizeInfoSwitchesButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set<SchemeOptimizeInfoSwitch> loadSchemeOptimizeInfoSwitchesButIds(
			StorableObjectCondition storableObjectCondition,
			Set<Identifier> ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeOptimizeInfoRtus(java.util.Set)
	 */
	public Set<SchemeOptimizeInfoRtu> loadSchemeOptimizeInfoRtus(
			Set<Identifier> ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeOptimizeInfoRtusButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set<SchemeOptimizeInfoRtu> loadSchemeOptimizeInfoRtusButIds(
			StorableObjectCondition storableObjectCondition,
			Set<Identifier> ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemePaths(java.util.Set)
	 */
	public Set<SchemePath> loadSchemePaths(Set<Identifier> ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemePathsButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set<SchemePath> loadSchemePathsButIds(
			StorableObjectCondition storableObjectCondition,
			Set<Identifier> ids) throws ApplicationException {
		return Collections.emptySet();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemePorts(java.util.Set)
	 */
	public Set<SchemePort> loadSchemePorts(Set<Identifier> ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemePortsButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set<SchemePort> loadSchemePortsButIds(
			StorableObjectCondition storableObjectCondition,
			Set<Identifier> ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeProtoElements(java.util.Set)
	 */
	public Set<SchemeProtoElement> loadSchemeProtoElements(
			Set<Identifier> ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeProtoElementsButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set<SchemeProtoElement> loadSchemeProtoElementsButIds(
			StorableObjectCondition storableObjectCondition,
			Set<Identifier> ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeProtoGroups(java.util.Set)
	 */
	public Set<SchemeProtoGroup> loadSchemeProtoGroups(Set<Identifier> ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeProtoGroupsButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set<SchemeProtoGroup> loadSchemeProtoGroupsButIds(
			StorableObjectCondition storableObjectCondition,
			Set<Identifier> ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemes(java.util.Set)
	 */
	public Set<Scheme> loadSchemes(Set<Identifier> ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemesButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set<Scheme> loadSchemesButIds(
			StorableObjectCondition storableObjectCondition,
			Set<Identifier> ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjects
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#refresh(java.util.Set)
	 */
	public Set<Identifier> refresh(
			Set< ? extends StorableObject> storableObjects)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param cableChannelingItems
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveCableChannelingItems(java.util.Set, boolean)
	 */
	public void saveCableChannelingItems(
			Set<CableChannelingItem> cableChannelingItems,
			boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param pathElements
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#savePathElements(java.util.Set, boolean)
	 */
	public void savePathElements(Set<PathElement> pathElements,
			boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeCableLinks
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemeCableLinks(java.util.Set, boolean)
	 */
	public void saveSchemeCableLinks(Set<SchemeCableLink> schemeCableLinks,
			boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeCablePorts
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemeCablePorts(java.util.Set, boolean)
	 */
	public void saveSchemeCablePorts(Set<SchemeCablePort> schemeCablePorts,
			boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeCableThreads
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemeCableThreads(java.util.Set, boolean)
	 */
	public void saveSchemeCableThreads(
			Set<SchemeCableThread> schemeCableThreads, boolean force)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeDevices
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemeDevices(java.util.Set, boolean)
	 */
	public void saveSchemeDevices(Set<SchemeDevice> schemeDevices,
			boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeElements
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemeElements(java.util.Set, boolean)
	 */
	public void saveSchemeElements(Set<SchemeElement> schemeElements,
			boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeLinks
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemeLinks(java.util.Set, boolean)
	 */
	public void saveSchemeLinks(Set<SchemeLink> schemeLinks, boolean force)
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
			Set<SchemeMonitoringSolution> schemeMonitoringSolutions,
			boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeOptimizeInfos
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemeOptimizeInfos(java.util.Set, boolean)
	 */
	public void saveSchemeOptimizeInfos(
			Set<SchemeOptimizeInfo> schemeOptimizeInfos,
			boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeOptimizeInfoSwitches
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemeOptimizeInfoSwitches(java.util.Set, boolean)
	 */
	public void saveSchemeOptimizeInfoSwitches(
			Set<SchemeOptimizeInfoSwitch> schemeOptimizeInfoSwitches,
			boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeOptimizeInfoRtus
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemeOptimizeInfoRtus(java.util.Set, boolean)
	 */
	public void saveSchemeOptimizeInfoRtus(
			Set<SchemeOptimizeInfoRtu> schemeOptimizeInfoRtus,
			boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemePaths
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemePaths(java.util.Set, boolean)
	 */
	public void saveSchemePaths(Set<SchemePath> schemePaths, boolean force)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemePorts
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemePorts(java.util.Set, boolean)
	 */
	public void saveSchemePorts(Set<SchemePort> schemePorts, boolean force)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeProtoElements
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemeProtoElements(java.util.Set, boolean)
	 */
	public void saveSchemeProtoElements(
			Set<SchemeProtoElement> schemeProtoElements,
			boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeProtoGroups
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemeProtoGroups(java.util.Set, boolean)
	 */
	public void saveSchemeProtoGroups(
			Set<SchemeProtoGroup> schemeProtoGroups, boolean force)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemes
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemes(java.util.Set, boolean)
	 */
	public void saveSchemes(Set<Scheme> schemes, boolean force)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

}
