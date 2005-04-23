/*-
 * $Id: ClientSchemeObjectLoader.java,v 1.3 2005/04/23 13:58:27 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.mshserver.corba.MSHServer;
import java.util.*;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.3 $, $Date: 2005/04/23 13:58:27 $
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
	 * @param id
	 * @see SchemeObjectLoader#delete(Identifier)
	 */
	public void delete(final Identifier id) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param id
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadCableChannelingItem(Identifier)
	 */
	public StorableObject loadCableChannelingItem(final Identifier id) throws ApplicationException {
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
	 * @param id
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadPathElement(Identifier)
	 */
	public StorableObject loadPathElement(final Identifier id) throws ApplicationException {
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
	 * @param id
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadScheme(Identifier)
	 */
	public StorableObject loadScheme(final Identifier id) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param id
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadSchemeCableLink(Identifier)
	 */
	public StorableObject loadSchemeCableLink(final Identifier id) throws ApplicationException {
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
	 * @param id
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadSchemeCablePort(Identifier)
	 */
	public StorableObject loadSchemeCablePort(final Identifier id) throws ApplicationException {
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
	 * @param id
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadSchemeCableThread(Identifier)
	 */
	public StorableObject loadSchemeCableThread(final Identifier id) throws ApplicationException {
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
	 * @param id
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadSchemeDevice(Identifier)
	 */
	public StorableObject loadSchemeDevice(final Identifier id) throws ApplicationException {
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
	 * @param id
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadSchemeElement(Identifier)
	 */
	public StorableObject loadSchemeElement(final Identifier id) throws ApplicationException {
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
	 * @param id
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadSchemeLink(Identifier)
	 */
	public StorableObject loadSchemeLink(final Identifier id) throws ApplicationException {
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
	 * @param id
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadSchemeMonitoringSolution(Identifier)
	 */
	public StorableObject loadSchemeMonitoringSolution(final Identifier id) throws ApplicationException {
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
	 * @param id
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadSchemeOptimizeInfo(Identifier)
	 */
	public StorableObject loadSchemeOptimizeInfo(final Identifier id) throws ApplicationException {
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
	 * @param id
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadSchemePath(Identifier)
	 */
	public StorableObject loadSchemePath(final Identifier id) throws ApplicationException {
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
	 * @param id
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadSchemePort(Identifier)
	 */
	public StorableObject loadSchemePort(final Identifier id) throws ApplicationException {
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
	 * @param id
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadSchemeProtoElement(Identifier)
	 */
	public StorableObject loadSchemeProtoElement(final Identifier id) throws ApplicationException {
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
	 * @param id
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#loadSchemeProtoGroup(Identifier)
	 */
	public StorableObject loadSchemeProtoGroup(final Identifier id) throws ApplicationException {
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
	 * @param cableChannelingItem
	 * @param force
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#saveCableChannelingItem(CableChannelingItem, boolean)
	 */
	public void saveCableChannelingItem(final CableChannelingItem cableChannelingItem, final boolean force) throws ApplicationException {
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
	 * @param pathElement
	 * @param force
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#savePathElement(PathElement, boolean)
	 */
	public void savePathElement(final PathElement pathElement, final boolean force) throws ApplicationException {
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
	 * @param scheme
	 * @param force
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#saveScheme(Scheme, boolean)
	 */
	public void saveScheme(final Scheme scheme, final boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeCableLink
	 * @param force
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#saveSchemeCableLink(SchemeCableLink, boolean)
	 */
	public void saveSchemeCableLink(final SchemeCableLink schemeCableLink, final boolean force) throws ApplicationException {
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
	 * @param schemeCablePort
	 * @param force
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#saveSchemeCablePort(SchemeCablePort, boolean)
	 */
	public void saveSchemeCablePort(final SchemeCablePort schemeCablePort, boolean force) throws ApplicationException {
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
	 * @param schemeCableThread
	 * @param force
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#saveSchemeCableThread(SchemeCableThread, boolean)
	 */
	public void saveSchemeCableThread(final SchemeCableThread schemeCableThread, final boolean force) throws ApplicationException {
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
	 * @param schemeDevice
	 * @param force
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#saveSchemeDevice(SchemeDevice, boolean)
	 */
	public void saveSchemeDevice(final SchemeDevice schemeDevice, final boolean force) throws ApplicationException {
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
	 * @param schemeElement
	 * @param force
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#saveSchemeElement(SchemeElement, boolean)
	 */
	public void saveSchemeElement(final SchemeElement schemeElement, final boolean force) throws ApplicationException {
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
	 * @param schemeLink
	 * @param force
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#saveSchemeLink(SchemeLink, boolean)
	 */
	public void saveSchemeLink(final SchemeLink schemeLink, final boolean force)
			throws ApplicationException {
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
	 * @param schemeMonitoringSolution
	 * @param force
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#saveSchemeMonitoringSolution(SchemeMonitoringSolution, boolean)
	 */
	public void saveSchemeMonitoringSolution(final SchemeMonitoringSolution schemeMonitoringSolution, final boolean force) throws ApplicationException {
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
	 * @param schemeOptimizeInfo
	 * @param force
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#saveSchemeOptimizeInfo(SchemeOptimizeInfo, boolean)
	 */
	public void saveSchemeOptimizeInfo(final SchemeOptimizeInfo schemeOptimizeInfo, final boolean force) throws ApplicationException {
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
	 * @param schemePath
	 * @param force
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#saveSchemePath(SchemePath, boolean)
	 */
	public void saveSchemePath(final SchemePath schemePath, final boolean force) throws ApplicationException {
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
	 * @param schemePort
	 * @param force
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#saveSchemePort(SchemePort, boolean)
	 */
	public void saveSchemePort(final SchemePort schemePort, final boolean force) throws ApplicationException {
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
	 * @param schemeProtoElement
	 * @param force
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#saveSchemeProtoElement(SchemeProtoElement, boolean)
	 */
	public void saveSchemeProtoElement(final SchemeProtoElement schemeProtoElement, final boolean force) throws ApplicationException {
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
	 * @param schemeProtoGroup
	 * @param force
	 * @throws ApplicationException
	 * @see SchemeObjectLoader#saveSchemeProtoGroup(SchemeProtoGroup, boolean)
	 */
	public void saveSchemeProtoGroup(final SchemeProtoGroup schemeProtoGroup, final boolean force) throws ApplicationException {
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
