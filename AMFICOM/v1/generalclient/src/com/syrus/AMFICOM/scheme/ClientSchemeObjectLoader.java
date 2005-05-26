/*-
 * $Id: ClientSchemeObjectLoader.java,v 1.5 2005/05/26 19:13:23 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.mshserver.corba.MSHServer;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/05/26 19:13:23 $
 * @module generalclient_v1
 */
public final class ClientSchemeObjectLoader implements SchemeObjectLoader {
	private MSHServer server;

	public ClientSchemeObjectLoader(final MSHServer server) {
		this.server = server;
	}

	/**
	 * @param ids
	 * @see SchemeObjectLoader#delete(Set)
	 */
	public void delete(final Set ids) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadCableChannelingItems(Set)
	 */
	public Set loadCableChannelingItems(final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadCableChannelingItemsButIds(StorableObjectCondition, Set)
	 */
	public Set loadCableChannelingItemsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadPathElements(Set)
	 */
	public Set loadPathElements(final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadPathElementsButIds(StorableObjectCondition, Set)
	 */
	public Set loadPathElementsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadSchemeCableLinks(Set)
	 */
	public Set loadSchemeCableLinks(final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadSchemeCableLinksButIds(StorableObjectCondition, Set)
	 */
	public Set loadSchemeCableLinksButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadSchemeCablePorts(Set)
	 */
	public Set loadSchemeCablePorts(final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadSchemeCablePortsButIds(StorableObjectCondition, Set)
	 */
	public Set loadSchemeCablePortsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadSchemeCableThreads(Set)
	 */
	public Set loadSchemeCableThreads(final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadSchemeCableThreadsButIds(StorableObjectCondition, Set)
	 */
	public Set loadSchemeCableThreadsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadSchemeDevices(Set)
	 */
	public Set loadSchemeDevices(final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadSchemeDevicesButIds(StorableObjectCondition, Set)
	 */
	public Set loadSchemeDevicesButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadSchemeElements(Set)
	 */
	public Set loadSchemeElements(final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadSchemeElementsButIds(StorableObjectCondition, Set)
	 */
	public Set loadSchemeElementsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadSchemeLinks(Set)
	 */
	public Set loadSchemeLinks(final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadSchemeLinksButIds(StorableObjectCondition, Set)
	 */
	public Set loadSchemeLinksButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadSchemeMonitoringSolutions(Set)
	 */
	public Set loadSchemeMonitoringSolutions(final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadSchemeMonitoringSolutionsButIds(StorableObjectCondition, Set)
	 */
	public Set loadSchemeMonitoringSolutionsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeOptimizeInfoRtus(java.util.Set)
	 */
	public Set loadSchemeOptimizeInfoRtus(final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeOptimizeInfoRtusButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set loadSchemeOptimizeInfoRtusButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadSchemeOptimizeInfos(Set)
	 */
	public Set loadSchemeOptimizeInfos(final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadSchemeOptimizeInfosButIds(StorableObjectCondition, Set)
	 */
	public Set loadSchemeOptimizeInfosButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeOptimizeInfoSwitches(java.util.Set)
	 */
	public Set loadSchemeOptimizeInfoSwitches(final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeOptimizeInfoSwitchesButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set loadSchemeOptimizeInfoSwitchesButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadSchemePaths(Set)
	 */
	public Set loadSchemePaths(final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadSchemePathsButIds(StorableObjectCondition, Set)
	 */
	public Set loadSchemePathsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadSchemePorts(Set)
	 */
	public Set loadSchemePorts(final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadSchemePortsButIds(StorableObjectCondition, Set)
	 */
	public Set loadSchemePortsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadSchemeProtoElements(Set)
	 */
	public Set loadSchemeProtoElements(final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadSchemeProtoElementsButIds(StorableObjectCondition, Set)
	 */
	public Set loadSchemeProtoElementsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadSchemeProtoGroups(Set)
	 */
	public Set loadSchemeProtoGroups(final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadSchemeProtoGroupsButIds(StorableObjectCondition, Set)
	 */
	public Set loadSchemeProtoGroupsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadSchemes(Set)
	 */
	public Set loadSchemes(final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadSchemesButIds(StorableObjectCondition, Set)
	 */
	public Set loadSchemesButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjects
	 * @throws CommunicationException
	 * @see SchemeObjectLoader#refresh(Set)
	 */
	public Set refresh(final Set storableObjects) throws CommunicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param cableChannelingItems
	 * @param force
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#saveCableChannelingItems(Set, boolean)
	 */
	public void saveCableChannelingItems(final Set cableChannelingItems, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param pathElements
	 * @param force
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#savePathElements(Set, boolean)
	 */
	public void savePathElements(final Set pathElements, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeCableLinks
	 * @param force
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#saveSchemeCableLinks(Set, boolean)
	 */
	public void saveSchemeCableLinks(final Set schemeCableLinks, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeCablePorts
	 * @param force
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#saveSchemeCablePorts(Set, boolean)
	 */
	public void saveSchemeCablePorts(final Set schemeCablePorts, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeCableThreads
	 * @param force
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#saveSchemeCableThreads(Set, boolean)
	 */
	public void saveSchemeCableThreads(final Set schemeCableThreads, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeDevices
	 * @param force
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#saveSchemeDevices(Set, boolean)
	 */
	public void saveSchemeDevices(final Set schemeDevices, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeElements
	 * @param force
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#saveSchemeElements(Set, boolean)
	 */
	public void saveSchemeElements(final Set schemeElements, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeLinks
	 * @param force
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#saveSchemeLinks(Set, boolean)
	 */
	public void saveSchemeLinks(final Set schemeLinks, final boolean force)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeMonitoringSolutions
	 * @param force
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#saveSchemeMonitoringSolutions(Set, boolean)
	 */
	public void saveSchemeMonitoringSolutions(final Set schemeMonitoringSolutions, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeOptimizeInfoRtus
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemeOptimizeInfoRtus(java.util.Set, boolean)
	 */
	public void saveSchemeOptimizeInfoRtus(final Set schemeOptimizeInfoRtus, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeOptimizeInfos
	 * @param force
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#saveSchemeOptimizeInfos(Set, boolean)
	 */
	public void saveSchemeOptimizeInfos(final Set schemeOptimizeInfos, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeOptimizeInfoSwitches
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemeOptimizeInfoSwitches(java.util.Set, boolean)
	 */
	public void saveSchemeOptimizeInfoSwitches(final Set schemeOptimizeInfoSwitches, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemePaths
	 * @param force
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#saveSchemePaths(Set, boolean)
	 */
	public void saveSchemePaths(final Set schemePaths, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemePorts
	 * @param force
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#saveSchemePorts(Set, boolean)
	 */
	public void saveSchemePorts(final Set schemePorts, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeProtoElements
	 * @param force
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#saveSchemeProtoElements(Set, boolean)
	 */
	public void saveSchemeProtoElements(final Set schemeProtoElements, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeProtoGroups
	 * @param force
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#saveSchemeProtoGroups(Set, boolean)
	 */
	public void saveSchemeProtoGroups(final Set schemeProtoGroups, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemes
	 * @param force
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#saveSchemes(Set, boolean)
	 */
	public void saveSchemes(final Set schemes, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	private void updateStorableObjectHeader(final Set storableObjects, final StorableObject_Transferable transferables[]) {
		for (final Iterator storableObjectIterator = storableObjects.iterator(); storableObjectIterator.hasNext();) {
			final StorableObject storableObject = (StorableObject) storableObjectIterator.next();
			for (int i = 0; i < transferables.length; i++)
				if (transferables[i].id.equals(storableObject.getId().getTransferable()))
					storableObject.updateFromHeaderTransferable(transferables[i]);
		}
	}
}
