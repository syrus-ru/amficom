/*
 * $Id: EventObjectLoader.java,v 1.5 2005/02/11 18:42:17 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.event;

import java.util.Collection;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.VersionCollisionException;

/**
 * @version $Revision: 1.5 $, $Date: 2005/02/11 18:42:17 $
 * @author $Author: arseniy $
 * @module event_v1
 */

public interface EventObjectLoader {

	EventType loadEventType(Identifier id) throws DatabaseException, CommunicationException;

//	AlarmType loadAlarmType(Identifier id) throws DatabaseException, CommunicationException;

	Event loadEvent(Identifier id) throws DatabaseException, CommunicationException;

	EventSource loadEventSource(Identifier id) throws DatabaseException, CommunicationException;

//	Alarm loadAlarm(Identifier id) throws DatabaseException, CommunicationException;


	Collection loadEventTypes(Collection ids) throws DatabaseException, CommunicationException;

//	Collection loadAlarmTypes(Collection ids) throws DatabaseException, CommunicationException;

	Collection loadEvents(Collection ids) throws DatabaseException, CommunicationException;

	Collection loadEventSources(Collection ids) throws DatabaseException, CommunicationException;

//	Collection loadAlarms(Collection ids) throws DatabaseException, CommunicationException;


	Collection loadEventTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException, CommunicationException;

//	Collection loadAlarmTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException, CommunicationException;

	Collection loadEventsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException, CommunicationException;

	Collection loadEventSourcesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException, CommunicationException;

//	Collection loadAlarmsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException, CommunicationException;


	void saveEventType(EventType eventType, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

//	void saveAlarmType(AlarmType alarmType, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveEvent(Event event, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveEventSource(EventSource eventSource, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

//	void saveAlarm(Alarm alarm, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;


	void saveEventTypes(Collection collection, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

//	void saveAlarmTypes(Collection collection, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveEvents(Collection collection, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveEventSources(Collection collection, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

//	void saveAlarms(Collection collection, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;


	java.util.Set refresh(java.util.Set storableObjects) throws CommunicationException, DatabaseException;


	void delete(Identifier id) throws CommunicationException, DatabaseException;

	void delete(Collection objects) throws CommunicationException, DatabaseException, IllegalDataException;
}
