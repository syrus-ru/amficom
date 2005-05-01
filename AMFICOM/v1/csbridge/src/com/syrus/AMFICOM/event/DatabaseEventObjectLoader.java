/*
 * $Id: DatabaseEventObjectLoader.java,v 1.3 2005/05/01 16:53:15 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.event;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @version $Revision: 1.3 $, $Date: 2005/05/01 16:53:15 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public class DatabaseEventObjectLoader extends DatabaseObjectLoader implements EventObjectLoader {

	/* Load multiple objects*/

	public Set loadEventTypes(Set ids) throws ApplicationException {
		EventTypeDatabase database = EventDatabaseContext.getEventTypeDatabase();
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadEvents(Set ids) throws ApplicationException {
		EventDatabase database = EventDatabaseContext.getEventDatabase();
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadEventSources(Set ids) throws ApplicationException {
		EventSourceDatabase database = EventDatabaseContext.getEventSourceDatabase();
		return super.retrieveFromDatabase(database, ids);
	}



	/* Load multiple objects but ids*/

	public Set loadEventTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		EventTypeDatabase database = EventDatabaseContext.getEventTypeDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadEventsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		EventDatabase database = EventDatabaseContext.getEventDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadEventSourcesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		EventSourceDatabase database = EventDatabaseContext.getEventSourceDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}



	/* Save multiple objects*/

	public void saveEventTypes(Set collection, boolean force) throws ApplicationException {
		EventTypeDatabase eventTypeDatabase = EventDatabaseContext.getEventTypeDatabase();
		eventTypeDatabase.update(collection, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveEvents(Set collection, boolean force) throws ApplicationException {
		EventDatabase eventDatabase = EventDatabaseContext.getEventDatabase();
		eventDatabase.update(collection, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveEventSources(Set collection, boolean force) throws ApplicationException {
		EventSourceDatabase eventSourceDatabase = EventDatabaseContext.getEventSourceDatabase();
		eventSourceDatabase.update(collection, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}



	public Set refresh(Set storableObjects) throws ApplicationException {
		if (storableObjects.isEmpty())
			return Collections.EMPTY_SET;

		short entityCode = ((StorableObject) storableObjects.iterator().next()).getId().getMajor();

		StorableObjectDatabase database = EventDatabaseContext.getDatabase(entityCode);

		if (database != null)
			return database.refresh(storableObjects);

		return Collections.EMPTY_SET;
	}



	public void delete(final Set identifiables) {
		if (identifiables == null || identifiables.isEmpty())
			return;
		/**
		 * @todo: use Trove collection instead java.util.Map
		 */
		final Map map = new HashMap();

		/**
		 * separate objects by kind of entity
		 */
		for (final Iterator identifiableIterator = identifiables.iterator(); identifiableIterator.hasNext();) {
			final Identifiable identifiable = (Identifiable) identifiableIterator.next();

			final Short entityCode = new Short(identifiable.getId().getMajor());
			Set entityObjects = (Set) map.get(entityCode);
			if (entityObjects == null) {
				entityObjects = new HashSet();
				map.put(entityCode, entityObjects);
			}
			entityObjects.add(identifiable);
		}

		for (final Iterator entityCodeIterator = map.keySet().iterator(); entityCodeIterator.hasNext();) {
			final Short entityCode = (Short) entityCodeIterator.next();
			final Set entityObjects = (Set) map.get(entityCode);
			final StorableObjectDatabase storableObjectDatabase = EventDatabaseContext.getDatabase(entityCode);
			if (storableObjectDatabase != null)
				storableObjectDatabase.delete(entityObjects);
		}
	}

}
