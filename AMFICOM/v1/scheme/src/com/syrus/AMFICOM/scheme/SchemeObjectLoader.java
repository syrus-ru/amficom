/*-
 * $Id: SchemeObjectLoader.java,v 1.1 2005/04/01 13:59:07 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.general.*;
import java.util.*;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/04/01 13:59:07 $
 * @module scheme_v1
 */
public interface SchemeObjectLoader {
	void delete(final Set ids) throws IllegalDataException;

	void delete(final Identifier id) throws IllegalDataException;

	StorableObject loadCableChannelingItem(final Identifier id) throws ApplicationException;

	Set loadCableChannelingItems(final Set ids) throws ApplicationException;

	Set loadCableChannelingItemsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException;

	StorableObject loadPathElement(final Identifier id) throws ApplicationException;

	Set loadPathElements(final Set ids) throws ApplicationException;

	Set loadPathElementsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException;

	StorableObject loadScheme(final Identifier id) throws ApplicationException;

	StorableObject loadSchemeCableLink(final Identifier id) throws ApplicationException;

	Set loadSchemeCableLinks(final Set ids) throws ApplicationException;

	Set loadSchemeCableLinksButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException;

	StorableObject loadSchemeCablePort(final Identifier id) throws ApplicationException;

	Set loadSchemeCablePorts(final Set ids) throws ApplicationException;

	Set loadSchemeCablePortsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException;

	StorableObject loadSchemeCableThread(final Identifier id) throws ApplicationException;

	Set loadSchemeCableThreads(final Set ids) throws ApplicationException;

	Set loadSchemeCableThreadsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException;

	StorableObject loadSchemeDevice(final Identifier id) throws ApplicationException;

	Set loadSchemeDevices(final Set ids) throws ApplicationException;

	Set loadSchemeDevicesButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException;

	StorableObject loadSchemeElement(final Identifier id) throws ApplicationException;

	Set loadSchemeElements(final Set ids) throws ApplicationException;

	Set loadSchemeElementsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException;

	StorableObject loadSchemeLink(final Identifier id) throws ApplicationException;

	Set loadSchemeLinks(final Set ids) throws ApplicationException;

	Set loadSchemeLinksButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException;

	StorableObject loadSchemeMonitoringSolution(final Identifier id) throws ApplicationException;

	Set loadSchemeMonitoringSolutions(final Set ids) throws ApplicationException;

	Set loadSchemeMonitoringSolutionsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException;

	StorableObject loadSchemeOptimizeInfo(final Identifier id) throws ApplicationException;

	Set loadSchemeOptimizeInfos(final Set ids) throws ApplicationException;

	Set loadSchemeOptimizeInfosButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException;

	StorableObject loadSchemePath(final Identifier id) throws ApplicationException;

	Set loadSchemePaths(final Set ids) throws ApplicationException;

	Set loadSchemePathsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException;

	StorableObject loadSchemePort(final Identifier id) throws ApplicationException;

	Set loadSchemePorts(final Set ids) throws ApplicationException;

	Set loadSchemePortsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException;

	StorableObject loadSchemeProtoElement(final Identifier id) throws ApplicationException;

	Set loadSchemeProtoElements(final Set ids) throws ApplicationException;

	Set loadSchemeProtoElementsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException;

	StorableObject loadSchemeProtoGroup(final Identifier id) throws ApplicationException;

	Set loadSchemeProtoGroups(final Set ids) throws ApplicationException;

	Set loadSchemeProtoGroupsButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException;

	Set loadSchemes(final Set ids) throws ApplicationException;

	Set loadSchemesButIds(final StorableObjectCondition storableObjectCondition, final Set ids) throws ApplicationException;
	Set refresh(final Set storableObjects) throws ApplicationException;

	void saveCableChannelingItem(final CableChannelingItem cableChannelingItem, final boolean force) throws ApplicationException;

	void saveCableChannelingItems(final Set cableChannelingItems, final boolean force) throws ApplicationException;

	void savePathElement(final PathElement pathElement, final boolean force) throws ApplicationException;

	void savePathElements(final Set pathElements, final boolean force) throws ApplicationException;

	void saveScheme(final Scheme scheme, final boolean force) throws ApplicationException;

	void saveSchemeCableLink(final SchemeCableLink schemeCableLink, final boolean force) throws ApplicationException;

	void saveSchemeCableLinks(final Set schemeCableLinks, final boolean force) throws ApplicationException;

	void saveSchemeCablePort(final SchemeCablePort schemeCablePort, final boolean force) throws ApplicationException;

	void saveSchemeCablePorts(final Set schemeCablePorts, final boolean force) throws ApplicationException;

	void saveSchemeCableThread(final SchemeCableThread schemeCableThread, final boolean force) throws ApplicationException;

	void saveSchemeCableThreads(final Set schemeCableThreads, final boolean force) throws ApplicationException;

	void saveSchemeDevice(final SchemeDevice schemeDevice, final boolean force) throws ApplicationException;

	void saveSchemeDevices(final Set schemeDevices, final boolean force) throws ApplicationException;

	void saveSchemeElement(final SchemeElement schemeElement, final boolean force) throws ApplicationException;

	void saveSchemeElements(final Set schemeElements, final boolean force) throws ApplicationException;

	void saveSchemeLink(final SchemeLink schemeLink, final boolean force) throws ApplicationException;

	void saveSchemeLinks(final Set schemeLinks, final boolean force) throws ApplicationException;

	void saveSchemeMonitoringSolution(final SchemeMonitoringSolution schemeMonitoringSolution, final boolean force) throws ApplicationException;

	void saveSchemeMonitoringSolutions(final Set schemeMonitoringSolutions, final boolean force) throws ApplicationException;

	void saveSchemeOptimizeInfo(final SchemeOptimizeInfo schemeOptimizeInfo, final boolean force) throws ApplicationException;

	void saveSchemeOptimizeInfos(final Set schemeOptimizeInfos, final boolean force) throws ApplicationException;

	void saveSchemePath(final SchemePath schemePath, final boolean force) throws ApplicationException;

	void saveSchemePaths(final Set schemePaths, final boolean force) throws ApplicationException;

	void saveSchemePort(final SchemePort schemePort, final boolean force) throws ApplicationException;

	void saveSchemePorts(final Set schemePorts, final boolean force) throws ApplicationException;

	void saveSchemeProtoElement(final SchemeProtoElement schemeProtoElement, final boolean force) throws ApplicationException;

	void saveSchemeProtoElements(final Set schemeProtoElements, final boolean force) throws ApplicationException;

	void saveSchemeProtoGroup(final SchemeProtoGroup schemeProtoGroup, final boolean force) throws ApplicationException;

	void saveSchemeProtoGroups(final Set schemeProtoGroups, final boolean force) throws ApplicationException;

	void saveSchemes(final Set schemes, final boolean force) throws ApplicationException;
}
