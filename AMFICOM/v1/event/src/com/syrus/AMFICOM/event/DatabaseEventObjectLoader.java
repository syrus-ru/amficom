/*
 * $Id: DatabaseEventObjectLoader.java,v 1.12 2005/04/01 09:00:59 bob Exp $
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
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.SessionContext;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.12 $, $Date: 2005/04/01 09:00:59 $
 * @author $Author: bob $
 * @module event_v1
 */
public class DatabaseEventObjectLoader implements EventObjectLoader {

	public EventType loadEventType(Identifier id) throws ApplicationException {
		return new EventType(id);
	}

	public Event loadEvent(Identifier id) throws ApplicationException {
		return new Event(id);
	}

	public EventSource loadEventSource(Identifier id) throws ApplicationException {
		return new EventSource(id);
	}



	public Set loadEventTypes(Set ids) throws ApplicationException {
		EventTypeDatabase eventTypeDatabase = (EventTypeDatabase) EventDatabaseContext.eventTypeDatabase;
		Set collection = null;
		try {
			collection = eventTypeDatabase.retrieveByIdsByCondition(ids, null);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseEventObjectLoader.loadEventTypes | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		return collection;
	}

	public Set loadEvents(Set ids) throws ApplicationException {
		EventDatabase eventDatabase = (EventDatabase) EventDatabaseContext.eventDatabase;
		Set collection = null;
		try {
			collection = eventDatabase.retrieveByIdsByCondition(ids, null);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseEventObjectLoader.loadEvents | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		return collection;
	}

	public Set loadEventSources(Set ids) throws ApplicationException {
		EventSourceDatabase eventSourceDatabase = (EventSourceDatabase) EventDatabaseContext.eventSourceDatabase;
		Set collection = null;
		try {
			collection = eventSourceDatabase.retrieveByIdsByCondition(ids, null);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseEventObjectLoader.loadEventSources | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		return collection;
	}




	public Set loadEventTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		EventTypeDatabase eventTypeDatabase = (EventTypeDatabase) EventDatabaseContext.eventTypeDatabase;
		Set collection = null;
		try {
			collection = eventTypeDatabase.retrieveButIdsByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseEventObjectLoader.loadEventTypesButIds | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		return collection;
	}

	public Set loadEventsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		EventDatabase eventDatabase = (EventDatabase) EventDatabaseContext.eventDatabase;
		Set collection = null;
		try {
			collection = eventDatabase.retrieveButIdsByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseEventObjectLoader.loadEventsButIds | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		return collection;
	}

	public Set loadEventSourcesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		EventSourceDatabase eventSourceDatabase = (EventSourceDatabase) EventDatabaseContext.eventSourceDatabase;
		Set collection = null;
		try {
			collection = eventSourceDatabase.retrieveButIdsByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseEventObjectLoader.loadEventSourcesButIds | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		return collection;
	}




	public void saveEventType(EventType eventType, boolean force) throws ApplicationException {
		EventTypeDatabase eventTypeDatabase = (EventTypeDatabase) EventDatabaseContext.eventTypeDatabase;
		eventTypeDatabase.update(eventType, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveEvent(Event event, boolean force) throws ApplicationException {
		EventDatabase eventDatabase = (EventDatabase) EventDatabaseContext.eventDatabase;
		eventDatabase.update(event, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveEventSource(EventSource eventSource, boolean force) throws ApplicationException {
		EventSourceDatabase eventSourceDatabase = (EventSourceDatabase) EventDatabaseContext.eventSourceDatabase;
		eventSourceDatabase.update(eventSource, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}




	public void saveEventTypes(Set collection, boolean force) throws ApplicationException {
		EventTypeDatabase eventTypeDatabase = (EventTypeDatabase) EventDatabaseContext.eventTypeDatabase;
		eventTypeDatabase.update(collection, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveEvents(Set collection, boolean force) throws ApplicationException {
		EventDatabase eventDatabase = (EventDatabase) EventDatabaseContext.eventDatabase;
		eventDatabase.update(collection, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveEventSources(Set collection, boolean force) throws ApplicationException {
		EventSourceDatabase eventSourceDatabase = (EventSourceDatabase) EventDatabaseContext.eventSourceDatabase;
		eventSourceDatabase.update(collection, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
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




	public void delete(Identifier id) throws IllegalDataException {
		short entityCode = id.getMajor();
		StorableObjectDatabase storableObjectDatabase = EventDatabaseContext.getDatabase(entityCode);
		if (storableObjectDatabase != null)
			storableObjectDatabase.delete(id);
	}

	public void delete(Set objects) throws IllegalDataException {
		if (objects == null || objects.isEmpty())
			return;
		/**
		 * TODO: use Trove collection instead java.util.Map
		 */
		Map map = new HashMap();

		/**
		 * separate objects by kind of entity
		 */
		Set entityObjects;
		Short entityCode;
		for (Iterator it = objects.iterator(); it.hasNext();) {
			Object object = it.next();
			Identifier identifier = null;
			if (object instanceof Identifier)
				identifier = (Identifier) object;
			else
				if (object instanceof Identifiable)
					identifier = ((Identifiable) object).getId();
				else
					throw new IllegalDataException("DatabaseEventObjectLoader.delete | Object "
							+ object.getClass().getName() + " isn't Identifier or Identifiable");

			entityCode = new Short(identifier.getMajor());
			entityObjects = (Set) map.get(entityCode);
			if (entityObjects == null) {
				entityObjects = new HashSet();
				map.put(entityCode, entityObjects);
			}
			entityObjects.add(object);
		}

		StorableObjectDatabase storableObjectDatabase;
		for (Iterator it = map.keySet().iterator(); it.hasNext();) {
			entityCode = (Short) it.next();
			entityObjects = (Set) map.get(entityCode);
			storableObjectDatabase = EventDatabaseContext.getDatabase(entityCode);
			if (storableObjectDatabase != null)
				storableObjectDatabase.delete(entityObjects);
		}
	}

}
