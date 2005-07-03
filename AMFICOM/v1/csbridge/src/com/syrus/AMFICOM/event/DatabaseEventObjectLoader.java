/*
 * $Id: DatabaseEventObjectLoader.java,v 1.11 2005/06/22 19:29:31 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.event;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.11 $, $Date: 2005/06/22 19:29:31 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public class DatabaseEventObjectLoader extends DatabaseObjectLoader implements EventObjectLoader {

	/* Load multiple objects*/

	public Set loadEventTypes(final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadEvents(final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadEventSources(final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	/* Load multiple objects but ids*/

	public Set loadEventTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadEventsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadEventSourcesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	/* Save multiple objects*/

	public void saveEventTypes(Set<EventType> objects, final boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveEvents(Set<Event> objects, final boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveEventSources(Set<EventSource> objects, final boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}
}
