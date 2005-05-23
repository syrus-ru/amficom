/*-
 * $Id: EmptyClientSchemeObjectLoader.java,v 1.3 2005/05/23 12:52:54 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Collections;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/05/23 12:52:54 $
 * @module generalclient_v1
 */
public final class EmptyClientSchemeObjectLoader implements SchemeObjectLoader {
	/**
	 * @param identifiables
	 * @see SchemeObjectLoader#delete(Set)
	 */
	public void delete(final Set identifiables) {
		// empty
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
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeOptimizeInfoRtus(java.util.Set)
	 */
	public Set loadSchemeOptimizeInfoRtus(final Set ids) throws ApplicationException {
		return Collections.EMPTY_SET;
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeOptimizeInfoRtusButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set loadSchemeOptimizeInfoRtusButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException {
		return Collections.EMPTY_SET;
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
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeOptimizeInfoSwitches(java.util.Set)
	 */
	public Set loadSchemeOptimizeInfoSwitches(final Set ids) throws ApplicationException {
		return Collections.EMPTY_SET;
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeOptimizeInfoSwitchesButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set loadSchemeOptimizeInfoSwitchesButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException {
		return Collections.EMPTY_SET;
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
	 * @param cableChannelingItems
	 * @param force
	 * @see SchemeObjectLoader#saveCableChannelingItems(Set, boolean)
	 */
	public void saveCableChannelingItems(final Set cableChannelingItems, final boolean force) {
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
	 * @param schemeCableLinks
	 * @param force
	 * @see SchemeObjectLoader#saveSchemeCableLinks(Set, boolean)
	 */
	public void saveSchemeCableLinks(final Set schemeCableLinks, final boolean force) {
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
	 * @param schemeCableThreads
	 * @param force
	 * @see SchemeObjectLoader#saveSchemeCableThreads(Set, boolean)
	 */
	public void saveSchemeCableThreads(final Set schemeCableThreads, final boolean force) {
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
	 * @param schemeElements
	 * @param force
	 * @see SchemeObjectLoader#saveSchemeElements(Set, boolean)
	 */
	public void saveSchemeElements(final Set schemeElements, final boolean force) {
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
	 * @param schemeMonitoringSolutions
	 * @param force
	 * @see SchemeObjectLoader#saveSchemeMonitoringSolutions(Set, boolean)
	 */
	public void saveSchemeMonitoringSolutions(final Set schemeMonitoringSolutions, final boolean force) {
		// empty
	}

	/**
	 * @param schemeOptimizeInfoRtus
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemeOptimizeInfoRtus(java.util.Set, boolean)
	 */
	public void saveSchemeOptimizeInfoRtus(final Set schemeOptimizeInfoRtus, final boolean force) throws ApplicationException {
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
	 * 
	 * @param schemeOptimizeInfoSwitches
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemeOptimizeInfoSwitches(java.util.Set, boolean)
	 */
	public void saveSchemeOptimizeInfoSwitches(final Set schemeOptimizeInfoSwitches, final boolean force) throws ApplicationException {
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
