/*
 * $Id: EventObjectLoader.java,v 1.7 2005/02/24 15:00:07 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.event;

import java.util.Collection;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.7 $, $Date: 2005/02/24 15:00:07 $
 * @author $Author: arseniy $
 * @module event_v1
 */

public interface EventObjectLoader {

	EventType loadEventType(Identifier id) throws ApplicationException;

//	AlarmType loadAlarmType(Identifier id) throws ApplicationException;

	Event loadEvent(Identifier id) throws ApplicationException;

	EventSource loadEventSource(Identifier id) throws ApplicationException;

//	Alarm loadAlarm(Identifier id) throws ApplicationException;


	Collection loadEventTypes(Collection ids) throws ApplicationException;

//	Collection loadAlarmTypes(Collection ids) throws ApplicationException;

	Collection loadEvents(Collection ids) throws ApplicationException;

	Collection loadEventSources(Collection ids) throws ApplicationException;

//	Collection loadAlarms(Collection ids) throws ApplicationException;


	Collection loadEventTypesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException;

//	Collection loadAlarmTypesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException;

	Collection loadEventsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException;

	Collection loadEventSourcesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException;

//	Collection loadAlarmsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException;


	void saveEventType(EventType eventType, boolean force) throws ApplicationException;

//	void saveAlarmType(AlarmType alarmType, boolean force) throws ApplicationException;

	void saveEvent(Event event, boolean force) throws ApplicationException;

	void saveEventSource(EventSource eventSource, boolean force) throws ApplicationException;

//	void saveAlarm(Alarm alarm, boolean force) throws ApplicationException;


	void saveEventTypes(Collection collection, boolean force) throws ApplicationException;

//	void saveAlarmTypes(Collection collection, boolean force) throws ApplicationException;

	void saveEvents(Collection collection, boolean force) throws ApplicationException;

	void saveEventSources(Collection collection, boolean force) throws ApplicationException;

//	void saveAlarms(Collection collection, boolean force) throws ApplicationException;


	java.util.Set refresh(java.util.Set storableObjects) throws ApplicationException;


	void delete(Identifier id) throws IllegalDataException;

	void delete(Collection objects) throws IllegalDataException;
}
