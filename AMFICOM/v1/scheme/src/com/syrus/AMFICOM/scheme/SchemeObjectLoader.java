/*-
 * $Id: SchemeObjectLoader.java,v 1.7 2005/06/22 19:25:35 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.7 $, $Date: 2005/06/22 19:25:35 $
 * @module scheme_v1
 */
public interface SchemeObjectLoader {
	void delete(final Set<? extends Identifiable> objects);

	Set loadCableChannelingItems(final Set ids) throws ApplicationException;

	Set loadCableChannelingItemsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException;

	Set loadPathElements(final Set ids) throws ApplicationException;

	Set loadPathElementsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException;

	Set loadSchemeCableLinks(final Set ids) throws ApplicationException;

	Set loadSchemeCableLinksButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException;

	Set loadSchemeCablePorts(final Set ids) throws ApplicationException;

	Set loadSchemeCablePortsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException;

	Set loadSchemeCableThreads(final Set ids) throws ApplicationException;

	Set loadSchemeCableThreadsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException;

	Set loadSchemeDevices(final Set ids) throws ApplicationException;

	Set loadSchemeDevicesButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException;

	Set loadSchemeElements(final Set ids) throws ApplicationException;

	Set loadSchemeElementsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException;

	Set loadSchemeLinks(final Set ids) throws ApplicationException;

	Set loadSchemeLinksButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException;

	Set loadSchemeMonitoringSolutions(final Set ids) throws ApplicationException;

	Set loadSchemeMonitoringSolutionsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException;

	Set loadSchemeOptimizeInfos(final Set ids) throws ApplicationException;

	Set loadSchemeOptimizeInfosButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException;

	Set loadSchemeOptimizeInfoSwitches(final Set ids) throws ApplicationException;

	Set loadSchemeOptimizeInfoSwitchesButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException;

	Set loadSchemeOptimizeInfoRtus(final Set ids) throws ApplicationException;

	Set loadSchemeOptimizeInfoRtusButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException;

	Set loadSchemePaths(final Set ids) throws ApplicationException;

	Set loadSchemePathsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException;

	Set loadSchemePorts(final Set ids) throws ApplicationException;

	Set loadSchemePortsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException;

	Set loadSchemeProtoElements(final Set ids) throws ApplicationException;

	Set loadSchemeProtoElementsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException;

	Set loadSchemeProtoGroups(final Set ids) throws ApplicationException;

	Set loadSchemeProtoGroupsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException;

	Set loadSchemes(final Set ids) throws ApplicationException;

	Set loadSchemesButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException;

	Set refresh(final Set<? extends StorableObject> storableObjects) throws ApplicationException;

	void saveCableChannelingItems(final Set cableChannelingItems, final boolean force) throws ApplicationException;

	void savePathElements(final Set pathElements, final boolean force) throws ApplicationException;

	void saveSchemeCableLinks(final Set schemeCableLinks, final boolean force) throws ApplicationException;

	void saveSchemeCablePorts(final Set schemeCablePorts, final boolean force) throws ApplicationException;

	void saveSchemeCableThreads(final Set schemeCableThreads, final boolean force) throws ApplicationException;

	void saveSchemeDevices(final Set schemeDevices, final boolean force) throws ApplicationException;

	void saveSchemeElements(final Set schemeElements, final boolean force) throws ApplicationException;

	void saveSchemeLinks(final Set schemeLinks, final boolean force) throws ApplicationException;

	void saveSchemeMonitoringSolutions(final Set schemeMonitoringSolutions, final boolean force) throws ApplicationException;

	void saveSchemeOptimizeInfos(final Set schemeOptimizeInfos, final boolean force) throws ApplicationException;

	void saveSchemeOptimizeInfoSwitches(final Set schemeOptimizeInfoSwitches, final boolean force) throws ApplicationException;

	void saveSchemeOptimizeInfoRtus(final Set schemeOptimizeInfoRtus, final boolean force) throws ApplicationException;

	void saveSchemePaths(final Set schemePaths, final boolean force) throws ApplicationException;

	void saveSchemePorts(final Set schemePorts, final boolean force) throws ApplicationException;

	void saveSchemeProtoElements(final Set schemeProtoElements, final boolean force) throws ApplicationException;

	void saveSchemeProtoGroups(final Set schemeProtoGroups, final boolean force) throws ApplicationException;

	void saveSchemes(final Set schemes, final boolean force) throws ApplicationException;
}
