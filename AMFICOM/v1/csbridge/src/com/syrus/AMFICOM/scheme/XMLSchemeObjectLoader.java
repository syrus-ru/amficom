/*-
 * $Id: XMLSchemeObjectLoader.java,v 1.3 2005/05/16 16:04:19 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.general.*;

import java.io.File;
import java.util.*;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.3 $, $Date: 2005/05/16 16:04:19 $
 * @module csbridge_v1
 */
public final class XMLSchemeObjectLoader extends XMLObjectLoader implements SchemeObjectLoader {
	private StorableObjectXML schemeXML;

	public XMLSchemeObjectLoader(final File path) {
		StorableObjectXMLDriver driver = new StorableObjectXMLDriver(path, "scheme");
		this.schemeXML = new StorableObjectXML(driver);
	}

	/**
	 * @param ids
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#delete(java.util.Set)
	 */
	public void delete(Set ids) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param id
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#delete(com.syrus.AMFICOM.general.Identifier)
	 */
	public void delete(Identifier id) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param id
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadCableChannelingItem(com.syrus.AMFICOM.general.Identifier)
	 */
	public StorableObject loadCableChannelingItem(Identifier id)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadCableChannelingItems(java.util.Set)
	 */
	public Set loadCableChannelingItems(Set ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadCableChannelingItemsButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set loadCableChannelingItemsButIds(
			StorableObjectCondition storableObjectCondition,
			Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param id
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadPathElement(com.syrus.AMFICOM.general.Identifier)
	 */
	public StorableObject loadPathElement(Identifier id)
			throws ApplicationException {
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
	 * @param id
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadScheme(com.syrus.AMFICOM.general.Identifier)
	 */
	public StorableObject loadScheme(Identifier id)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param id
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeCableLink(com.syrus.AMFICOM.general.Identifier)
	 */
	public StorableObject loadSchemeCableLink(Identifier id)
			throws ApplicationException {
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
	 * @param id
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeCablePort(com.syrus.AMFICOM.general.Identifier)
	 */
	public StorableObject loadSchemeCablePort(Identifier id)
			throws ApplicationException {
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
	 * @param id
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeCableThread(com.syrus.AMFICOM.general.Identifier)
	 */
	public StorableObject loadSchemeCableThread(Identifier id)
			throws ApplicationException {
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
	 * @param id
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeDevice(com.syrus.AMFICOM.general.Identifier)
	 */
	public StorableObject loadSchemeDevice(Identifier id)
			throws ApplicationException {
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
	 * @param id
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeElement(com.syrus.AMFICOM.general.Identifier)
	 */
	public StorableObject loadSchemeElement(Identifier id)
			throws ApplicationException {
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
	 * @param id
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeLink(com.syrus.AMFICOM.general.Identifier)
	 */
	public StorableObject loadSchemeLink(Identifier id)
			throws ApplicationException {
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
	 * @param id
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeMonitoringSolution(com.syrus.AMFICOM.general.Identifier)
	 */
	public StorableObject loadSchemeMonitoringSolution(Identifier id)
			throws ApplicationException {
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
	 * @param id
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeOptimizeInfo(com.syrus.AMFICOM.general.Identifier)
	 */
	public StorableObject loadSchemeOptimizeInfo(Identifier id)
			throws ApplicationException {
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
	 * @param id
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemePath(com.syrus.AMFICOM.general.Identifier)
	 */
	public StorableObject loadSchemePath(Identifier id)
			throws ApplicationException {
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
	 * @param id
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemePort(com.syrus.AMFICOM.general.Identifier)
	 */
	public StorableObject loadSchemePort(Identifier id)
			throws ApplicationException {
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
	 * @param id
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeProtoElement(com.syrus.AMFICOM.general.Identifier)
	 */
	public StorableObject loadSchemeProtoElement(Identifier id)
			throws ApplicationException {
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
	 * @param id
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#loadSchemeProtoGroup(com.syrus.AMFICOM.general.Identifier)
	 */
	public StorableObject loadSchemeProtoGroup(Identifier id)
			throws ApplicationException {
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
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#refresh(java.util.Set)
	 */
	public Set refresh(Set storableObjects) throws ApplicationException {
		return Collections.EMPTY_SET;
	}

	/**
	 * @param cableChannelingItem
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveCableChannelingItem(com.syrus.AMFICOM.scheme.CableChannelingItem, boolean)
	 */
	public void saveCableChannelingItem(
			CableChannelingItem cableChannelingItem, boolean force)
			throws ApplicationException {
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
	 * @param pathElement
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#savePathElement(com.syrus.AMFICOM.scheme.PathElement, boolean)
	 */
	public void savePathElement(PathElement pathElement, boolean force)
			throws ApplicationException {
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
	 * @param scheme
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveScheme(com.syrus.AMFICOM.scheme.Scheme, boolean)
	 */
	public void saveScheme(Scheme scheme, boolean force)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeCableLink
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemeCableLink(com.syrus.AMFICOM.scheme.SchemeCableLink, boolean)
	 */
	public void saveSchemeCableLink(SchemeCableLink schemeCableLink,
			boolean force) throws ApplicationException {
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
	 * @param schemeCablePort
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemeCablePort(com.syrus.AMFICOM.scheme.SchemeCablePort, boolean)
	 */
	public void saveSchemeCablePort(SchemeCablePort schemeCablePort,
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
	 * @param schemeCableThread
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemeCableThread(com.syrus.AMFICOM.scheme.SchemeCableThread, boolean)
	 */
	public void saveSchemeCableThread(SchemeCableThread schemeCableThread,
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
	 * @param schemeDevice
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemeDevice(com.syrus.AMFICOM.scheme.SchemeDevice, boolean)
	 */
	public void saveSchemeDevice(SchemeDevice schemeDevice, boolean force)
			throws ApplicationException {
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
	 * @param schemeElement
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemeElement(com.syrus.AMFICOM.scheme.SchemeElement, boolean)
	 */
	public void saveSchemeElement(SchemeElement schemeElement, boolean force)
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
	 * @param schemeLink
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemeLink(com.syrus.AMFICOM.scheme.SchemeLink, boolean)
	 */
	public void saveSchemeLink(SchemeLink schemeLink, boolean force)
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
	 * @param schemeMonitoringSolution
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemeMonitoringSolution(com.syrus.AMFICOM.scheme.SchemeMonitoringSolution, boolean)
	 */
	public void saveSchemeMonitoringSolution(
			SchemeMonitoringSolution schemeMonitoringSolution,
			boolean force) throws ApplicationException {
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
	 * @param schemeOptimizeInfo
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemeOptimizeInfo(com.syrus.AMFICOM.scheme.SchemeOptimizeInfo, boolean)
	 */
	public void saveSchemeOptimizeInfo(
			SchemeOptimizeInfo schemeOptimizeInfo, boolean force)
			throws ApplicationException {
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
	 * @param schemePath
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemePath(com.syrus.AMFICOM.scheme.SchemePath, boolean)
	 */
	public void saveSchemePath(SchemePath schemePath, boolean force)
			throws ApplicationException {
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
	 * @param schemePort
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemePort(com.syrus.AMFICOM.scheme.SchemePort, boolean)
	 */
	public void saveSchemePort(SchemePort schemePort, boolean force)
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
	 * @param schemeProtoGroup
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.scheme.SchemeObjectLoader#saveSchemeProtoGroup(com.syrus.AMFICOM.scheme.SchemeProtoGroup, boolean)
	 */
	public void saveSchemeProtoGroup(SchemeProtoGroup schemeProtoGroup,
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
