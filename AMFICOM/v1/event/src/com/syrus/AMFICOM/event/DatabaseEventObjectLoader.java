/*
 * $Id: DatabaseEventObjectLoader.java,v 1.2 2005/02/07 12:13:01 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.event;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identified;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.2 $, $Date: 2005/02/07 12:13:01 $
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



	public List loadEventTypes(List ids) throws DatabaseException, CommunicationException {
		EventTypeDatabase eventTypeDatabase = (EventTypeDatabase) EventDatabaseContext.eventTypeDatabase;
		List list = null;
		try {
			list = eventTypeDatabase.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseEventObjectLoader.loadEventTypes | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		return list;
	}

	public List loadEvents(List ids) throws DatabaseException, CommunicationException {
		EventDatabase eventDatabase = (EventDatabase) EventDatabaseContext.eventDatabase;
		List list = null;
		try {
			list = eventDatabase.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseEventObjectLoader.loadEvents | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		return list;
	}




	public List loadEventTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
		EventTypeDatabase eventTypeDatabase = (EventTypeDatabase) EventDatabaseContext.eventTypeDatabase;
		List list = null;
		try {
			list = eventTypeDatabase.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseEventObjectLoader.loadEventTypesButIds | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		return list;
	}

	public List loadEventsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
		EventDatabase eventDatabase = (EventDatabase) EventDatabaseContext.eventDatabase;
		List list = null;
		try {
			list = eventDatabase.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseEventObjectLoader.loadEventsButIds | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		return list;
	}




	public void saveEventType(EventType eventType, boolean force)
			throws VersionCollisionException,
				DatabaseException,
				CommunicationException {
		EventTypeDatabase eventTypeDatabase = (EventTypeDatabase) EventDatabaseContext.eventTypeDatabase;
		try {
			eventTypeDatabase.update(eventType, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
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
			eventDatabase.update(event, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
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




	public void saveEventTypes(List list, boolean force)
			throws VersionCollisionException,
				DatabaseException,
				CommunicationException {
		EventTypeDatabase eventTypeDatabase = (EventTypeDatabase) EventDatabaseContext.eventTypeDatabase;
		try {
			eventTypeDatabase.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
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

	public void saveEvents(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException {
		EventDatabase eventDatabase = (EventDatabase) EventDatabaseContext.eventDatabase;
		try {
			eventDatabase.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
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




	public void delete(Identifier id) throws CommunicationException, DatabaseException {
		this.delete(id, null);
	}

	public void delete(List ids) throws CommunicationException, DatabaseException {
		if (ids == null || ids.isEmpty())
			return;
		/**
		 * TODO: use Trove collection instead java.util.Map
		 */
		Map map = new HashMap();

		/**
		 * separate objects by kind of entity
		 */
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Object object = it.next();
			Identifier identifier = null;
			if (object instanceof Identifier)
				identifier = (Identifier) object;
			else
				if (object instanceof Identified)
					identifier = ((Identified) object).getId();
				else
					throw new DatabaseException("DatabaseEventObjectLoader.delete | Object "
							+ object.getClass().getName()
							+ " isn't Identifier or Identified");
			Short entityCode = new Short(identifier.getMajor());
			List list = (List) map.get(entityCode);
			if (list == null) {
				list = new LinkedList();
				map.put(entityCode, list);
			}
			list.add(object);
		}

		for (Iterator it = map.keySet().iterator(); it.hasNext();) {
			Short entityCode = (Short) it.next();
			List list = (List) map.get(entityCode);
			this.delete(null, list);
		}
	}

	private void delete(Identifier id, List ids) throws DatabaseException {
		short entityCode = (id != null) ? id.getMajor() : 0;
		if (id == null) {
			if (ids.isEmpty())
				return;
			Object obj = ids.iterator().next();
			if (obj instanceof Identifier)
				entityCode = ((Identifier) obj).getMajor();
			else
				if (obj instanceof Identified)
					entityCode = ((Identified) obj).getId().getMajor();
		}
		try {
			StorableObjectDatabase database = EventDatabaseContext.getDatabase(entityCode);
			if (database != null) {
				if (id != null)
					database.delete(id);
				else
					if (ids != null && !ids.isEmpty()) {
						database.delete(ids);
					}
			}
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseEventObjectLoader.delete | IllegalDataException: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
	}

}
