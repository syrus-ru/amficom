/*
 * $Id: DatabaseEventObjectLoader.java,v 1.8 2005/05/26 19:13:24 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.event;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.8 $, $Date: 2005/05/26 19:13:24 $
 * @author $Author: bass $
 * @module csbridge_v1
 */
public class DatabaseEventObjectLoader extends DatabaseObjectLoader implements EventObjectLoader {

	/* Load multiple objects*/

	public Set loadEventTypes(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids);
	}

	public Set loadEvents(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids);
	}

	public Set loadEventSources(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids);
	}

	/* Load multiple objects but ids*/

	public Set loadEventTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIds(condition, ids);
	}

	public Set loadEventsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIds(condition, ids);
	}

	public Set loadEventSourcesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIds(condition, ids);
	}

	/* Save multiple objects*/

	public void saveEventTypes(Set objects, boolean force) throws ApplicationException {
		super.saveStorableObjects(objects, force);
	}

	public void saveEvents(Set objects, boolean force) throws ApplicationException {
		super.saveStorableObjects(objects, force);
	}

	public void saveEventSources(Set objects, boolean force) throws ApplicationException {
		super.saveStorableObjects(objects, force);
	}
}
