/*
 * $Id: DatabaseEventObjectLoader.java,v 1.6 2005/02/18 17:59:53 arseniy Exp $
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

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identified;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.SessionContext;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.6 $, $Date: 2005/02/18 17:59:53 $
 * @author $Author: arseniy $
 * @module event_v1
 */
public class DatabaseEventObjectLoader implements EventObjectLoader {

	public EventType loadEventType(Identifier id) throws DatabaseException, CommunicationException {
		return new EventType(id);
	}

	public Event loadEvent(Identifier id) throws DatabaseException, CommunicationException {
		return new Event(id);
	}

	public EventSource loadEventSource(Identifier id) throws DatabaseException, CommunicationException {
		return new EventSource(id);
	}



	public Collection loadEventTypes(Collection ids) throws DatabaseException, CommunicationException {
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

	public Collection loadEvents(Collection ids) throws DatabaseException, CommunicationException {
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

	public Collection loadEventSources(Collection ids) throws DatabaseException, CommunicationException {
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




	public Collection loadEventTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException, CommunicationException {
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

	public Collection loadEventsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException, CommunicationException {
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

	public Collection loadEventSourcesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException, CommunicationException {
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




	public void saveEventType(EventType eventType, boolean force)
			throws VersionCollisionException,
				DatabaseException,
				CommunicationException {
		EventTypeDatabase eventTypeDatabase = (EventTypeDatabase) EventDatabaseContext.eventTypeDatabase;
		try {
			eventTypeDatabase.update(eventType, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		}
		catch (UpdateObjectException e) {
			String mesg = "DatabaseEventObjectLoader.saveEventType | UpdateObjectException: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseEventObjectLoader.saveEventType | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		catch (VersionCollisionException e) {
			String mesg = "DatabaseEventObjectLoader.saveEventType | VersionCollisionException: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
	}

	public void saveEvent(Event event, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException {
		EventDatabase eventDatabase = (EventDatabase) EventDatabaseContext.eventDatabase;
		try {
			eventDatabase.update(event, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		}
		catch (UpdateObjectException e) {
			String mesg = "DatabaseEventObjectLoader.saveEvent | UpdateObjectException: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseEventObjectLoader.saveEvent | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		catch (VersionCollisionException e) {
			String mesg = "DatabaseEventObjectLoader.saveEvent | VersionCollisionException: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
	}

	public void saveEventSource(EventSource eventSource, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException {
		EventSourceDatabase eventSourceDatabase = (EventSourceDatabase) EventDatabaseContext.eventSourceDatabase;
		try {
			eventSourceDatabase.update(eventSource, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		}
		catch (UpdateObjectException e) {
			String mesg = "DatabaseEventObjectLoader.saveEventSource | UpdateObjectException: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseEventObjectLoader.saveEventSource | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		catch (VersionCollisionException e) {
			String mesg = "DatabaseEventObjectLoader.saveEventSource | VersionCollisionException: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
	}




	public void saveEventTypes(Collection collection, boolean force)
			throws VersionCollisionException,
				DatabaseException,
				CommunicationException {
		EventTypeDatabase eventTypeDatabase = (EventTypeDatabase) EventDatabaseContext.eventTypeDatabase;
		try {
			eventTypeDatabase.update(collection, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		}
		catch (UpdateObjectException e) {
			String mesg = "DatabaseEventObjectLoader.saveEventTypes | UpdateObjectException: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseEventObjectLoader.saveEventTypes | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		catch (VersionCollisionException e) {
			String mesg = "DatabaseEventObjectLoader.saveEventTypes | VersionCollisionException: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
	}

	public void saveEvents(Collection collection, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException {
		EventDatabase eventDatabase = (EventDatabase) EventDatabaseContext.eventDatabase;
		try {
			eventDatabase.update(collection, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		}
		catch (UpdateObjectException e) {
			String mesg = "DatabaseEventObjectLoader.saveEvents | UpdateObjectException: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseEventObjectLoader.saveEvents | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		catch (VersionCollisionException e) {
			String mesg = "DatabaseEventObjectLoader.saveEvents | VersionCollisionException: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
	}

	public void saveEventSources(Collection collection, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException {
		EventSourceDatabase eventSourceDatabase = (EventSourceDatabase) EventDatabaseContext.eventSourceDatabase;
		try {
			eventSourceDatabase.update(collection, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		}
		catch (UpdateObjectException e) {
			String mesg = "DatabaseEventObjectLoader.saveEventSources | UpdateObjectException: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseEventObjectLoader.saveEventSources | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		catch (VersionCollisionException e) {
			String mesg = "DatabaseEventObjectLoader.saveEventSources | VersionCollisionException: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
	}




	public Set refresh(Set storableObjects) throws CommunicationException, DatabaseException {
		if (storableObjects.isEmpty())
			return Collections.EMPTY_SET;

		short entityCode = ((StorableObject) storableObjects.iterator().next()).getId().getMajor();

		try {
			StorableObjectDatabase database = EventDatabaseContext.getDatabase(entityCode);

			if (database != null)
				return database.refresh(storableObjects);

			return Collections.EMPTY_SET;
		}
		catch (DatabaseException e) {
			Log.errorMessage("DatabaseEventObjectLoader.refresh | DatabaseException: " + e.getMessage());
			throw new DatabaseException("DatabaseEventObjectLoader.refresh | DatabaseException: " + e.getMessage());
		}
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
