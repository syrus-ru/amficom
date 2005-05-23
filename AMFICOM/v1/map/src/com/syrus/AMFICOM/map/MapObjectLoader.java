/*
 * $Id: MapObjectLoader.java,v 1.11 2005/05/23 13:51:17 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.map;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.11 $, $Date: 2005/05/23 13:51:17 $
 * @author $Author: bass $
 * @module map_v1
 */
public interface MapObjectLoader {
	Set loadCollectors(final Set ids) throws ApplicationException;

	Set loadMaps(final Set ids) throws ApplicationException;

	Set loadMarks(final Set ids) throws ApplicationException;

	Set loadNodeLinks(final Set ids) throws ApplicationException;

	Set loadPhysicalLinks(final Set ids) throws ApplicationException;

	Set loadPhysicalLinkTypes(final Set ids) throws ApplicationException;

	Set loadSiteNodes(final Set ids) throws ApplicationException;

	Set loadSiteNodeTypes(final Set ids) throws ApplicationException;

	Set loadTopologicalNodes(final Set ids) throws ApplicationException;

	Set loadCollectorsButIds(final StorableObjectCondition condition,
			final Set ids) throws ApplicationException;

	Set loadMapsButIds(final StorableObjectCondition condition,
			final Set ids) throws ApplicationException;

	Set loadMarksButIds(final StorableObjectCondition condition,
			final Set ids) throws ApplicationException;

	Set loadNodeLinksButIds(final StorableObjectCondition condition,
			final Set ids) throws ApplicationException;

	Set loadPhysicalLinksButIds(final StorableObjectCondition condition,
			final Set ids) throws ApplicationException;

	Set loadPhysicalLinkTypesButIds(final StorableObjectCondition condition,
			final Set ids) throws ApplicationException;

	Set loadSiteNodesButIds(final StorableObjectCondition condition,
			final Set ids) throws ApplicationException;

	Set loadSiteNodeTypesButIds(final StorableObjectCondition condition,
			final Set ids) throws ApplicationException;

	Set loadTopologicalNodesButIds(final StorableObjectCondition condition,
			final Set ids) throws ApplicationException;

	java.util.Set refresh(final Set storableObjects) throws ApplicationException;

	void saveCollectors(final Set objects,
			final boolean force) throws ApplicationException;

	void saveMaps(final Set objects,
			final boolean force) throws ApplicationException;

	void saveMarks(final Set objects,
			final boolean force) throws ApplicationException;

	void saveNodeLinks(final Set objects,
			final boolean force) throws ApplicationException;

	void savePhysicalLinks(final Set objects,
			final boolean force) throws ApplicationException;

	void savePhysicalLinkTypes(final Set objects,
			final boolean force) throws ApplicationException;

	void saveSiteNodes(final Set objects,
			final boolean force) throws ApplicationException;

	void saveSiteNodeTypes(final Set objects,
			final boolean force) throws ApplicationException;

	void saveTopologicalNodes(final Set objects,
			final boolean force) throws ApplicationException;

	void delete(final Set identifiables);
}
