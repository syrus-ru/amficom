/*-
 * $Id: EmptyClientSchemeObjectLoader.java,v 1.1 2005/04/15 18:04:08 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Collections;
import java.util.Set;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/04/15 18:04:08 $
 * @module generalclient_v1
 */
public final class EmptyClientSchemeObjectLoader implements SchemeObjectLoader {
	public EmptyClientSchemeObjectLoader() {
		// empty
	}

	/**
	 * @param identifiables
	 * @see SchemeObjectLoader#delete(Set)
	 */
	public void delete(final Set identifiables) {
		// empty
	}

	/**
	 * @param id
	 * @see SchemeObjectLoader#delete(Identifier)
	 */
	public void delete(final Identifier id) {
		// empty
	}

	/**
	 * @param id
	 * @see SchemeObjectLoader#loadCableChannelingItem(Identifier)
	 */
	public StorableObject loadCableChannelingItem(final Identifier id) {
		return null;
	}

	/**
	 * @param ids
	 * @see SchemeObjectLoader#loadCableChannelingItems(Set)
	 */
	public Set loadCableChannelingItems(final Set ids) {
		return Collections.EMPTY_SET;
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @see SchemeObjectLoader#loadCableChannelingItemsButIds(StorableObjectCondition, Set)
	 */
	public Set loadCableChannelingItemsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) {
		return Collections.EMPTY_SET;
	}

	/**
	 * @param id
	 * @see SchemeObjectLoader#loadPathElement(Identifier)
	 */
	public StorableObject loadPathElement(final Identifier id) {
		return null;
	}

	/**
	 * @param ids
	 * @see SchemeObjectLoader#loadPathElements(Set)
	 */
	public Set loadPathElements(final Set ids) {
		return Collections.EMPTY_SET;
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @see SchemeObjectLoader#loadPathElementsButIds(StorableObjectCondition, Set)
	 */
	public Set loadPathElementsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) {
		return Collections.EMPTY_SET;
	}

	/**
	 * @param id
	 * @see SchemeObjectLoader#loadScheme(Identifier)
	 */
	public StorableObject loadScheme(final Identifier id) {
		return null;
	}

	/**
	 * @param id
	 * @see SchemeObjectLoader#loadSchemeCableLink(Identifier)
	 */
	public StorableObject loadSchemeCableLink(final Identifier id) {
		return null;
	}

	/**
	 * @param ids
	 * @see SchemeObjectLoader#loadSchemeCableLinks(Set)
	 */
	public Set loadSchemeCableLinks(final Set ids) {
		return Collections.EMPTY_SET;
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @see SchemeObjectLoader#loadSchemeCableLinksButIds(StorableObjectCondition, Set)
	 */
	public Set loadSchemeCableLinksButIds(final StorableObjectCondition storableObjectCondition, final Set ids) {
		return Collections.EMPTY_SET;
	}

	/**
	 * @param id
	 * @see SchemeObjectLoader#loadSchemeCablePort(Identifier)
	 */
	public StorableObject loadSchemeCablePort(final Identifier id) {
		return null;
	}

	/**
	 * @param ids
	 * @see SchemeObjectLoader#loadSchemeCablePorts(Set)
	 */
	public Set loadSchemeCablePorts(final Set ids) {
		return Collections.EMPTY_SET;
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @see SchemeObjectLoader#loadSchemeCablePortsButIds(StorableObjectCondition, Set)
	 */
	public Set loadSchemeCablePortsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) {
		return Collections.EMPTY_SET;
	}

	/**
	 * @param id
	 * @see SchemeObjectLoader#loadSchemeCableThread(Identifier)
	 */
	public StorableObject loadSchemeCableThread(final Identifier id) {
		return null;
	}

	/**
	 * @param ids
	 * @see SchemeObjectLoader#loadSchemeCableThreads(Set)
	 */
	public Set loadSchemeCableThreads(final Set ids) {
		return Collections.EMPTY_SET;
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @see SchemeObjectLoader#loadSchemeCableThreadsButIds(StorableObjectCondition, Set)
	 */
	public Set loadSchemeCableThreadsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) {
		return Collections.EMPTY_SET;
	}

	/**
	 * @param id
	 * @see SchemeObjectLoader#loadSchemeDevice(Identifier)
	 */
	public StorableObject loadSchemeDevice(final Identifier id) {
		return null;
	}

	/**
	 * @param ids
	 * @see SchemeObjectLoader#loadSchemeDevices(Set)
	 */
	public Set loadSchemeDevices(final Set ids) {
		return Collections.EMPTY_SET;
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @see SchemeObjectLoader#loadSchemeDevicesButIds(StorableObjectCondition, Set)
	 */
	public Set loadSchemeDevicesButIds(final StorableObjectCondition storableObjectCondition, final Set ids) {
		return Collections.EMPTY_SET;
	}

	/**
	 * @param id
	 * @see SchemeObjectLoader#loadSchemeElement(Identifier)
	 */
	public StorableObject loadSchemeElement(final Identifier id) {
		return null;
	}

	/**
	 * @param ids
	 * @see SchemeObjectLoader#loadSchemeElements(Set)
	 */
	public Set loadSchemeElements(final Set ids) {
		return Collections.EMPTY_SET;
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @see SchemeObjectLoader#loadSchemeElementsButIds(StorableObjectCondition, Set)
	 */
	public Set loadSchemeElementsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) {
		return Collections.EMPTY_SET;
	}

	/**
	 * @param id
	 * @see SchemeObjectLoader#loadSchemeLink(Identifier)
	 */
	public StorableObject loadSchemeLink(final Identifier id) {
		return null;
	}

	/**
	 * @param ids
	 * @see SchemeObjectLoader#loadSchemeLinks(Set)
	 */
	public Set loadSchemeLinks(final Set ids) {
		return Collections.EMPTY_SET;
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @see SchemeObjectLoader#loadSchemeLinksButIds(StorableObjectCondition, Set)
	 */
	public Set loadSchemeLinksButIds(final StorableObjectCondition storableObjectCondition, final Set ids) {
		return Collections.EMPTY_SET;
	}

	/**
	 * @param id
	 * @see SchemeObjectLoader#loadSchemeMonitoringSolution(Identifier)
	 */
	public StorableObject loadSchemeMonitoringSolution(final Identifier id) {
		return null;
	}

	/**
	 * @param ids
	 * @see SchemeObjectLoader#loadSchemeMonitoringSolutions(Set)
	 */
	public Set loadSchemeMonitoringSolutions(final Set ids) {
		return Collections.EMPTY_SET;
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @see SchemeObjectLoader#loadSchemeMonitoringSolutionsButIds(StorableObjectCondition, Set)
	 */
	public Set loadSchemeMonitoringSolutionsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) {
		return Collections.EMPTY_SET;
	}

	/**
	 * @param id
	 * @see SchemeObjectLoader#loadSchemeOptimizeInfo(Identifier)
	 */
	public StorableObject loadSchemeOptimizeInfo(final Identifier id) {
		return null;
	}

	/**
	 * @param ids
	 * @see SchemeObjectLoader#loadSchemeOptimizeInfos(Set)
	 */
	public Set loadSchemeOptimizeInfos(final Set ids) {
		return Collections.EMPTY_SET;
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @see SchemeObjectLoader#loadSchemeOptimizeInfosButIds(StorableObjectCondition, Set)
	 */
	public Set loadSchemeOptimizeInfosButIds(final StorableObjectCondition storableObjectCondition, final Set ids) {
		return Collections.EMPTY_SET;
	}

	/**
	 * @param id
	 * @see SchemeObjectLoader#loadSchemePath(Identifier)
	 */
	public StorableObject loadSchemePath(final Identifier id) {
		return null;
	}

	/**
	 * @param ids
	 * @see SchemeObjectLoader#loadSchemePaths(Set)
	 */
	public Set loadSchemePaths(final Set ids) {
		return Collections.EMPTY_SET;
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @see SchemeObjectLoader#loadSchemePathsButIds(StorableObjectCondition, Set)
	 */
	public Set loadSchemePathsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) {
		return Collections.EMPTY_SET;
	}

	/**
	 * @param id
	 * @see SchemeObjectLoader#loadSchemePort(Identifier)
	 */
	public StorableObject loadSchemePort(final Identifier id) {
		return null;
	}

	/**
	 * @param ids
	 * @see SchemeObjectLoader#loadSchemePorts(Set)
	 */
	public Set loadSchemePorts(final Set ids) {
		return Collections.EMPTY_SET;
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @see SchemeObjectLoader#loadSchemePortsButIds(StorableObjectCondition, Set)
	 */
	public Set loadSchemePortsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) {
		return Collections.EMPTY_SET;
	}

	/**
	 * @param id
	 * @see SchemeObjectLoader#loadSchemeProtoElement(Identifier)
	 */
	public StorableObject loadSchemeProtoElement(final Identifier id) {
		return null;
	}

	/**
	 * @param ids
	 * @see SchemeObjectLoader#loadSchemeProtoElements(Set)
	 */
	public Set loadSchemeProtoElements(final Set ids) {
		return Collections.EMPTY_SET;
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @see SchemeObjectLoader#loadSchemeProtoElementsButIds(StorableObjectCondition, Set)
	 */
	public Set loadSchemeProtoElementsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) {
		return Collections.EMPTY_SET;
	}

	/**
	 * @param id
	 * @see SchemeObjectLoader#loadSchemeProtoGroup(Identifier)
	 */
	public StorableObject loadSchemeProtoGroup(final Identifier id) {
		return null;
	}

	/**
	 * @param ids
	 * @see SchemeObjectLoader#loadSchemeProtoGroups(Set)
	 */
	public Set loadSchemeProtoGroups(final Set ids) {
		return Collections.EMPTY_SET;
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @see SchemeObjectLoader#loadSchemeProtoGroupsButIds(StorableObjectCondition, Set)
	 */
	public Set loadSchemeProtoGroupsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) {
		return Collections.EMPTY_SET;
	}

	/**
	 * @param ids
	 * @see SchemeObjectLoader#loadSchemes(Set)
	 */
	public Set loadSchemes(final Set ids) {
		return Collections.EMPTY_SET;
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @see SchemeObjectLoader#loadSchemesButIds(StorableObjectCondition, Set)
	 */
	public Set loadSchemesButIds(final StorableObjectCondition storableObjectCondition, final Set ids) {
		return Collections.EMPTY_SET;
	}

	/**
	 * @param storableObjects
	 * @see SchemeObjectLoader#refresh(Set)
	 */
	public Set refresh(final Set storableObjects) {
		return Collections.EMPTY_SET;
	}

	/**
	 * @param cableChannelingItem
	 * @param force
	 * @see SchemeObjectLoader#saveCableChannelingItem(CableChannelingItem, boolean)
	 */
	public void saveCableChannelingItem(final CableChannelingItem cableChannelingItem, final boolean force) {
		// empty
	}

	/**
	 * @param cableChannelingItems
	 * @param force
	 * @see SchemeObjectLoader#saveCableChannelingItems(Set, boolean)
	 */
	public void saveCableChannelingItems(final Set cableChannelingItems, final boolean force) {
		// empty
	}

	/**
	 * @param pathElement
	 * @param force
	 * @see SchemeObjectLoader#savePathElement(PathElement, boolean)
	 */
	public void savePathElement(final PathElement pathElement, final boolean force) {
		// empty
	}

	/**
	 * @param pathElements
	 * @param force
	 * @see SchemeObjectLoader#savePathElements(Set, boolean)
	 */
	public void savePathElements(final Set pathElements, final boolean force) {
		// empty
	}

	/**
	 * @param scheme
	 * @param force
	 * @see SchemeObjectLoader#saveScheme(Scheme, boolean)
	 */
	public void saveScheme(final Scheme scheme, final boolean force) {
		// empty
	}

	/**
	 * @param schemeCableLink
	 * @param force
	 * @see SchemeObjectLoader#saveSchemeCableLink(SchemeCableLink, boolean)
	 */
	public void saveSchemeCableLink(final SchemeCableLink schemeCableLink, final boolean force) {
		// empty
	}

	/**
	 * @param schemeCableLinks
	 * @param force
	 * @see SchemeObjectLoader#saveSchemeCableLinks(Set, boolean)
	 */
	public void saveSchemeCableLinks(final Set schemeCableLinks, final boolean force) {
		// empty
	}

	/**
	 * @param schemeCablePort
	 * @param force
	 * @see SchemeObjectLoader#saveSchemeCablePort(SchemeCablePort, boolean)
	 */
	public void saveSchemeCablePort(final SchemeCablePort schemeCablePort, final boolean force) {
		// empty
	}

	/**
	 * @param schemeCablePorts
	 * @param force
	 * @see SchemeObjectLoader#saveSchemeCablePorts(Set, boolean)
	 */
	public void saveSchemeCablePorts(final Set schemeCablePorts, final boolean force) {
		// empty
	}

	/**
	 * @param schemeCableThread
	 * @param force
	 * @see SchemeObjectLoader#saveSchemeCableThread(SchemeCableThread, boolean)
	 */
	public void saveSchemeCableThread(final SchemeCableThread schemeCableThread, final boolean force) {
		// empty
	}

	/**
	 * @param schemeCableThreads
	 * @param force
	 * @see SchemeObjectLoader#saveSchemeCableThreads(Set, boolean)
	 */
	public void saveSchemeCableThreads(final Set schemeCableThreads, final boolean force) {
		// empty
	}

	/**
	 * @param schemeDevice
	 * @param force
	 * @see SchemeObjectLoader#saveSchemeDevice(SchemeDevice, boolean)
	 */
	public void saveSchemeDevice(final SchemeDevice schemeDevice, final boolean force) {
		// empty
	}

	/**
	 * @param schemeDevices
	 * @param force
	 * @see SchemeObjectLoader#saveSchemeDevices(Set, boolean)
	 */
	public void saveSchemeDevices(final Set schemeDevices, final boolean force) {
		// empty
	}

	/**
	 * @param schemeElement
	 * @param force
	 * @see SchemeObjectLoader#saveSchemeElement(SchemeElement, boolean)
	 */
	public void saveSchemeElement(final SchemeElement schemeElement, final boolean force) {
		// empty
	}

	/**
	 * @param schemeElements
	 * @param force
	 * @see SchemeObjectLoader#saveSchemeElements(Set, boolean)
	 */
	public void saveSchemeElements(final Set schemeElements, final boolean force) {
		// empty
	}

	/**
	 * @param schemeLink
	 * @param force
	 * @see SchemeObjectLoader#saveSchemeLink(SchemeLink, boolean)
	 */
	public void saveSchemeLink(final SchemeLink schemeLink, final boolean force) {
		// empty
	}

	/**
	 * @param schemeLinks
	 * @param force
	 * @see SchemeObjectLoader#saveSchemeLinks(Set, boolean)
	 */
	public void saveSchemeLinks(final Set schemeLinks, final boolean force) {
		// empty
	}

	/**
	 * @param schemeMonitoringSolution
	 * @param force
	 * @see SchemeObjectLoader#saveSchemeMonitoringSolution(SchemeMonitoringSolution, boolean)
	 */
	public void saveSchemeMonitoringSolution(final SchemeMonitoringSolution schemeMonitoringSolution, final boolean force) {
		// empty
	}

	/**
	 * @param schemeMonitoringSolutions
	 * @param force
	 * @see SchemeObjectLoader#saveSchemeMonitoringSolutions(Set, boolean)
	 */
	public void saveSchemeMonitoringSolutions(final Set schemeMonitoringSolutions, final boolean force) {
		// empty
	}

	/**
	 * @param schemeOptimizeInfo
	 * @param force
	 * @see SchemeObjectLoader#saveSchemeOptimizeInfo(SchemeOptimizeInfo, boolean)
	 */
	public void saveSchemeOptimizeInfo(final SchemeOptimizeInfo schemeOptimizeInfo, final boolean force) {
		// empty
	}

	/**
	 * @param schemeOptimizeInfos
	 * @param force
	 * @see SchemeObjectLoader#saveSchemeOptimizeInfos(Set, boolean)
	 */
	public void saveSchemeOptimizeInfos(final Set schemeOptimizeInfos, final boolean force) {
		// empty
	}

	/**
	 * @param schemePath
	 * @param force
	 * @see SchemeObjectLoader#saveSchemePath(SchemePath, boolean)
	 */
	public void saveSchemePath(final SchemePath schemePath, final boolean force) {
		// empty
	}

	/**
	 * @param schemePaths
	 * @param force
	 * @see SchemeObjectLoader#saveSchemePaths(Set, boolean)
	 */
	public void saveSchemePaths(final Set schemePaths, final boolean force) {
		// empty
	}

	/**
	 * @param schemePort
	 * @param force
	 * @see SchemeObjectLoader#saveSchemePort(SchemePort, boolean)
	 */
	public void saveSchemePort(final SchemePort schemePort, final boolean force) {
		// empty
	}

	/**
	 * @param schemePorts
	 * @param force
	 * @see SchemeObjectLoader#saveSchemePorts(Set, boolean)
	 */
	public void saveSchemePorts(final Set schemePorts, final boolean force) {
		// empty
	}

	/**
	 * @param schemeProtoElement
	 * @param force
	 * @see SchemeObjectLoader#saveSchemeProtoElement(SchemeProtoElement, boolean)
	 */
	public void saveSchemeProtoElement(final SchemeProtoElement schemeProtoElement, final boolean force) {
		// empty
	}

	/**
	 * @param schemeProtoElements
	 * @param force
	 * @see SchemeObjectLoader#saveSchemeProtoElements(Set, boolean)
	 */
	public void saveSchemeProtoElements(final Set schemeProtoElements, final boolean force) {
		// empty
	}

	/**
	 * @param schemeProtoGroup
	 * @param force
	 * @see SchemeObjectLoader#saveSchemeProtoGroup(SchemeProtoGroup, boolean)
	 */
	public void saveSchemeProtoGroup(final SchemeProtoGroup schemeProtoGroup, final boolean force) {
		// empty
	}

	/**
	 * @param schemeProtoGroups
	 * @param force
	 * @see SchemeObjectLoader#saveSchemeProtoGroups(Set, boolean)
	 */
	public void saveSchemeProtoGroups(final Set schemeProtoGroups, final boolean force) {
		// empty
	}

	/**
	 * @param schemes
	 * @param force
	 * @see SchemeObjectLoader#saveSchemes(Set, boolean)
	 */
	public void saveSchemes(final Set schemes, final boolean force) {
		// empty
	}
}
