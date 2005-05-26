/*-
 * $Id: DatabaseMapObjectLoader.java,v 1.6 2005/05/26 19:13:24 bass Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.map;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.6 $, $Date: 2005/05/26 19:13:24 $
 * @author $Author: bass $
 * @module csbridge_v1
 */
public class DatabaseMapObjectLoader extends DatabaseObjectLoader implements MapObjectLoader {
	public Set loadCollectors(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids);
	}

	public Set loadMaps(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids);
	}

	public Set loadMarks(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids);
	}

	public Set loadNodeLinks(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids);
	}

	public Set loadPhysicalLinks(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids);
	}

	public Set loadPhysicalLinkTypes(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids);
	}

	public Set loadSiteNodes(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids);
	}

	public Set loadSiteNodeTypes(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids);
	}	

	public Set loadTopologicalNodes(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids);
	}

	public Set loadCollectorsButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIds(condition, ids);
	}

	public Set loadMapsButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIds(condition, ids);
	}

	public Set loadMarksButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIds(condition, ids);
	}

	public Set loadNodeLinksButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIds(condition, ids);
	}

	public Set loadPhysicalLinksButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIds(condition, ids);
	}

	public Set loadPhysicalLinkTypesButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIds(condition, ids);
	}
	
	public Set loadSiteNodesButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIds(condition, ids);
	}

	public Set loadSiteNodeTypesButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIds(condition, ids);
	}

	public Set loadTopologicalNodesButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIds(condition, ids);
	}

	public void saveCollectors(final Set objects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(objects, force);
	}

	public void saveMaps(final Set objects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(objects, force);
	}

	public void saveMarks(final Set objects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(objects, force);
	}

	public void saveNodeLinks(final Set objects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(objects, force);
	}

	public void savePhysicalLinks(final Set objects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(objects, force);
	}

	public void savePhysicalLinkTypes(final Set objects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(objects, force);
	}

	public void saveSiteNodes(final Set objects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(objects, force);
	}

	public void saveSiteNodeTypes(final Set objects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(objects, force);
	}

	public void saveTopologicalNodes(final Set objects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(objects, force);
	}
}
