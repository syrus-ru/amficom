/*
 * $Id: DatabaseEventObjectLoader.java,v 1.8 2005/02/24 15:23:06 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.event;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identified;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.SessionContext;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.8 $, $Date: 2005/02/24 15:23:06 $
 * @author $Author: arseniy $
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



	public Collection loadEventTypes(Collection ids) throws ApplicationException {
		EventTypeDatabase eventTypeDatabase = (EventTypeDatabase) EventDatabaseContext.eventTypeDatabase;
		Collection collection = null;
		try {
			collection = eventTypeDatabase.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseEventObjectLoader.loadEventTypes | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		return collection;
	}

	public Collection loadEvents(Collection ids) throws ApplicationException {
		EventDatabase eventDatabase = (EventDatabase) EventDatabaseContext.eventDatabase;
		Collection collection = null;
		try {
			collection = eventDatabase.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseEventObjectLoader.loadEvents | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		return collection;
	}

	public Collection loadEventSources(Collection ids) throws ApplicationException {
		EventSourceDatabase eventSourceDatabase = (EventSourceDatabase) EventDatabaseContext.eventSourceDatabase;
		Collection collection = null;
		try {
			collection = eventSourceDatabase.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseEventObjectLoader.loadEventSources | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		return collection;
	}




	public Collection loadEventTypesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		EventTypeDatabase eventTypeDatabase = (EventTypeDatabase) EventDatabaseContext.eventTypeDatabase;
		Collection collection = null;
		try {
			collection = eventTypeDatabase.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseEventObjectLoader.loadEventTypesButIds | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		return collection;
	}

	public Collection loadEventsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		EventDatabase eventDatabase = (EventDatabase) EventDatabaseContext.eventDatabase;
		Collection collection = null;
		try {
			collection = eventDatabase.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseEventObjectLoader.loadEventsButIds | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		return collection;
	}

	public Collection loadEventSourcesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		EventSourceDatabase eventSourceDatabase = (EventSourceDatabase) EventDatabaseContext.eventSourceDatabase;
		Collection collection = null;
		try {
			collection = eventSourceDatabase.retrieveByCondition(ids, condition);
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




	public void saveEventTypes(Collection collection, boolean force) throws ApplicationException {
		EventTypeDatabase eventTypeDatabase = (EventTypeDatabase) EventDatabaseContext.eventTypeDatabase;
		eventTypeDatabase.update(collection, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveEvents(Collection collection, boolean force) throws ApplicationException {
		EventDatabase eventDatabase = (EventDatabase) EventDatabaseContext.eventDatabase;
		eventDatabase.update(collection, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveEventSources(Collection collection, boolean force) throws ApplicationException {
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

	public void delete(Collection objects) throws IllegalDataException {
		if (objects == null || objects.isEmpty())
			return;
		/**
		 * TODO: use Trove collection instead java.util.Map
		 */
		Map map = new HashMap();

		/**
		 * separate objects by kind of entity
		 */
		Collection entityObjects;
		Short entityCode;
		for (Iterator it = objects.iterator(); it.hasNext();) {
			Object object = it.next();
			Identifier identifier = null;
			if (object instanceof Identifier)
				identifier = (Identifier) object;
			else
				if (object instanceof Identified)
					identifier = ((Identified) object).getId();
				else
					throw new IllegalDataException("DatabaseEventObjectLoader.delete | Object "
							+ object.getClass().getName() + " isn't Identifier or Identified");

			entityCode = new Short(identifier.getMajor());
			entityObjects = (Collection) map.get(entityCode);
			if (entityObjects == null) {
				entityObjects = new LinkedList();
				map.put(entityCode, entityObjects);
			}
			entityObjects.add(object);
		}

		StorableObjectDatabase storableObjectDatabase;
		for (Iterator it = map.keySet().iterator(); it.hasNext();) {
			entityCode = (Short) it.next();
			entityObjects = (Collection) map.get(entityCode);
			storableObjectDatabase = EventDatabaseContext.getDatabase(entityCode);
			if (storableObjectDatabase != null)
				storableObjectDatabase.delete(entityObjects);
		}
	}

}
