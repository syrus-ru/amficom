/*-
 * $Id: CORBAMapObjectLoader.java,v 1.1 2005/05/26 19:13:24 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBAObjectLoader;
import com.syrus.AMFICOM.general.MSHServerConnectionManager;
import com.syrus.AMFICOM.general.StorableObjectCondition;

public final class CORBAMapObjectLoader extends CORBAObjectLoader implements MapObjectLoader {
	public CORBAMapObjectLoader(MSHServerConnectionManager serverConnectionManager) {
		super()
	}

	public Set loadCollectors(Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public Set loadMaps(Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public Set loadMarks(Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public Set loadNodeLinks(Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public Set loadPhysicalLinks(Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public Set loadPhysicalLinkTypes(Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public Set loadSiteNodes(Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public Set loadSiteNodeTypes(Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public Set loadTopologicalNodes(Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public Set loadCollectorsButIds(StorableObjectCondition condition,
			Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public Set loadMapsButIds(StorableObjectCondition condition, Set ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public Set loadMarksButIds(StorableObjectCondition condition, Set ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public Set loadNodeLinksButIds(StorableObjectCondition condition,
			Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public Set loadPhysicalLinksButIds(StorableObjectCondition condition,
			Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public Set loadPhysicalLinkTypesButIds(
			StorableObjectCondition condition, Set ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public Set loadSiteNodesButIds(StorableObjectCondition condition,
			Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public Set loadSiteNodeTypesButIds(StorableObjectCondition condition,
			Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public Set loadTopologicalNodesButIds(
			StorableObjectCondition condition, Set ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public Set refresh(Set storableObjects) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public void saveCollectors(Set objects, boolean force)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public void saveMaps(Set objects, boolean force)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public void saveMarks(Set objects, boolean force)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public void saveNodeLinks(Set objects, boolean force)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public void savePhysicalLinks(Set objects, boolean force)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public void savePhysicalLinkTypes(Set objects, boolean force)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public void saveSiteNodes(Set objects, boolean force)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public void saveSiteNodeTypes(Set objects, boolean force)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public void saveTopologicalNodes(Set objects, boolean force)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

}
