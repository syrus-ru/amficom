/*
 * $Id: DatabaseEventObjectLoader.java,v 1.9 2005/06/03 15:23:58 arseniy Exp $
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
 * @version $Revision: 1.9 $, $Date: 2005/06/03 15:23:58 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public class DatabaseEventObjectLoader extends DatabaseObjectLoader implements EventObjectLoader {

	/* Load multiple objects*/

	public Set loadEventTypes(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadEvents(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadEventSources(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	/* Load multiple objects but ids*/

	public Set loadEventTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIds(condition, ids);
	}

	public Set loadEventsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIds(condition, ids);
	}

	public Set loadEventSourcesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIds(condition, ids);
	}

	/* Save multiple objects*/

	public void saveEventTypes(Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveEvents(Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveEventSources(Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}
}
