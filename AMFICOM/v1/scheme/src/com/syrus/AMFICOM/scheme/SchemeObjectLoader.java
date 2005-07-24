/*-
 * $Id: SchemeObjectLoader.java,v 1.9 2005/07/24 17:10:19 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.9 $, $Date: 2005/07/24 17:10:19 $
 * @module scheme
 */
public interface SchemeObjectLoader {
	void delete(final Set<? extends Identifiable> objects);

	Set<CableChannelingItem> loadCableChannelingItems(final Set<Identifier> ids) throws ApplicationException;

	Set<CableChannelingItem> loadCableChannelingItemsButIds(final StorableObjectCondition storableObjectCondition, final Set<Identifier> ids) throws ApplicationException;

	Set<PathElement> loadPathElements(final Set<Identifier> ids) throws ApplicationException;

	Set<PathElement> loadPathElementsButIds(final StorableObjectCondition storableObjectCondition, final Set<Identifier> ids) throws ApplicationException;

	Set<SchemeCableLink> loadSchemeCableLinks(final Set<Identifier> ids) throws ApplicationException;

	Set<SchemeCableLink> loadSchemeCableLinksButIds(final StorableObjectCondition storableObjectCondition, final Set<Identifier> ids) throws ApplicationException;

	Set<SchemeCablePort> loadSchemeCablePorts(final Set<Identifier> ids) throws ApplicationException;

	Set<SchemeCablePort> loadSchemeCablePortsButIds(final StorableObjectCondition storableObjectCondition, final Set<Identifier> ids) throws ApplicationException;

	Set<SchemeCableThread> loadSchemeCableThreads(final Set<Identifier> ids) throws ApplicationException;

	Set<SchemeCableThread> loadSchemeCableThreadsButIds(final StorableObjectCondition storableObjectCondition, final Set<Identifier> ids) throws ApplicationException;

	Set<SchemeDevice> loadSchemeDevices(final Set<Identifier> ids) throws ApplicationException;

	Set<SchemeDevice> loadSchemeDevicesButIds(final StorableObjectCondition storableObjectCondition, final Set<Identifier> ids) throws ApplicationException;

	Set<SchemeElement> loadSchemeElements(final Set<Identifier> ids) throws ApplicationException;

	Set<SchemeElement> loadSchemeElementsButIds(final StorableObjectCondition storableObjectCondition, final Set<Identifier> ids) throws ApplicationException;

	Set<SchemeLink> loadSchemeLinks(final Set<Identifier> ids) throws ApplicationException;

	Set<SchemeLink> loadSchemeLinksButIds(final StorableObjectCondition storableObjectCondition, final Set<Identifier> ids) throws ApplicationException;

	Set<SchemeMonitoringSolution> loadSchemeMonitoringSolutions(final Set<Identifier> ids) throws ApplicationException;

	Set<SchemeMonitoringSolution> loadSchemeMonitoringSolutionsButIds(final StorableObjectCondition storableObjectCondition, final Set<Identifier> ids) throws ApplicationException;

	Set<SchemeOptimizeInfo> loadSchemeOptimizeInfos(final Set<Identifier> ids) throws ApplicationException;

	Set<SchemeOptimizeInfo> loadSchemeOptimizeInfosButIds(final StorableObjectCondition storableObjectCondition, final Set<Identifier> ids) throws ApplicationException;

	Set<SchemeOptimizeInfoSwitch> loadSchemeOptimizeInfoSwitches(final Set<Identifier> ids) throws ApplicationException;

	Set<SchemeOptimizeInfoSwitch> loadSchemeOptimizeInfoSwitchesButIds(final StorableObjectCondition storableObjectCondition, final Set<Identifier> ids) throws ApplicationException;

	Set<SchemeOptimizeInfoRtu> loadSchemeOptimizeInfoRtus(final Set<Identifier> ids) throws ApplicationException;

	Set<SchemeOptimizeInfoRtu> loadSchemeOptimizeInfoRtusButIds(final StorableObjectCondition storableObjectCondition, final Set<Identifier> ids) throws ApplicationException;

	Set<SchemePath> loadSchemePaths(final Set<Identifier> ids) throws ApplicationException;

	Set<SchemePath> loadSchemePathsButIds(final StorableObjectCondition storableObjectCondition, final Set<Identifier> ids) throws ApplicationException;

	Set<SchemePort> loadSchemePorts(final Set<Identifier> ids) throws ApplicationException;

	Set<SchemePort> loadSchemePortsButIds(final StorableObjectCondition storableObjectCondition, final Set<Identifier> ids) throws ApplicationException;

	Set<SchemeProtoElement> loadSchemeProtoElements(final Set<Identifier> ids) throws ApplicationException;

	Set<SchemeProtoElement> loadSchemeProtoElementsButIds(final StorableObjectCondition storableObjectCondition, final Set<Identifier> ids) throws ApplicationException;

	Set<SchemeProtoGroup> loadSchemeProtoGroups(final Set<Identifier> ids) throws ApplicationException;

	Set<SchemeProtoGroup> loadSchemeProtoGroupsButIds(final StorableObjectCondition storableObjectCondition, final Set<Identifier> ids) throws ApplicationException;

	Set<Scheme> loadSchemes(final Set<Identifier> ids) throws ApplicationException;

	Set<Scheme> loadSchemesButIds(final StorableObjectCondition storableObjectCondition, final Set<Identifier> ids) throws ApplicationException;

	Set<Identifier> refresh(final Set<? extends StorableObject> storableObjects) throws ApplicationException;

	void saveCableChannelingItems(final Set<CableChannelingItem> cableChannelingItems, final boolean force) throws ApplicationException;

	void savePathElements(final Set<PathElement> pathElements, final boolean force) throws ApplicationException;

	void saveSchemeCableLinks(final Set<SchemeCableLink> schemeCableLinks, final boolean force) throws ApplicationException;

	void saveSchemeCablePorts(final Set<SchemeCablePort> schemeCablePorts, final boolean force) throws ApplicationException;

	void saveSchemeCableThreads(final Set<SchemeCableThread> schemeCableThreads, final boolean force) throws ApplicationException;

	void saveSchemeDevices(final Set<SchemeDevice> schemeDevices, final boolean force) throws ApplicationException;

	void saveSchemeElements(final Set<SchemeElement> schemeElements, final boolean force) throws ApplicationException;

	void saveSchemeLinks(final Set<SchemeLink> schemeLinks, final boolean force) throws ApplicationException;

	void saveSchemeMonitoringSolutions(final Set<SchemeMonitoringSolution> schemeMonitoringSolutions, final boolean force) throws ApplicationException;

	void saveSchemeOptimizeInfos(final Set<SchemeOptimizeInfo> schemeOptimizeInfos, final boolean force) throws ApplicationException;

	void saveSchemeOptimizeInfoSwitches(final Set<SchemeOptimizeInfoSwitch> schemeOptimizeInfoSwitches, final boolean force) throws ApplicationException;

	void saveSchemeOptimizeInfoRtus(final Set<SchemeOptimizeInfoRtu> schemeOptimizeInfoRtus, final boolean force) throws ApplicationException;

	void saveSchemePaths(final Set<SchemePath> schemePaths, final boolean force) throws ApplicationException;

	void saveSchemePorts(final Set<SchemePort> schemePorts, final boolean force) throws ApplicationException;

	void saveSchemeProtoElements(final Set<SchemeProtoElement> schemeProtoElements, final boolean force) throws ApplicationException;

	void saveSchemeProtoGroups(final Set<SchemeProtoGroup> schemeProtoGroups, final boolean force) throws ApplicationException;

	void saveSchemes(final Set<Scheme> schemes, final boolean force) throws ApplicationException;
}
