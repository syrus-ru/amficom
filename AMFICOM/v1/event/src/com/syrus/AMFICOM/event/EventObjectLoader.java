/*
 * $Id: EventObjectLoader.java,v 1.9 2005/04/12 08:13:17 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.event;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.9 $, $Date: 2005/04/12 08:13:17 $
 * @author $Author: bass $
 * @module event_v1
 */

public interface EventObjectLoader {

	EventType loadEventType(Identifier id) throws ApplicationException;

//	AlarmType loadAlarmType(Identifier id) throws ApplicationException;

	Event loadEvent(Identifier id) throws ApplicationException;

	EventSource loadEventSource(Identifier id) throws ApplicationException;

//	Alarm loadAlarm(Identifier id) throws ApplicationException;


	Set loadEventTypes(Set ids) throws ApplicationException;

//	Set loadAlarmTypes(Set ids) throws ApplicationException;

	Set loadEvents(Set ids) throws ApplicationException;

	Set loadEventSources(Set ids) throws ApplicationException;

//	Set loadAlarms(Set ids) throws ApplicationException;


	Set loadEventTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

//	Set loadAlarmTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	Set loadEventsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	Set loadEventSourcesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

//	Set loadAlarmsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;


	void saveEventType(EventType eventType, boolean force) throws ApplicationException;

//	void saveAlarmType(AlarmType alarmType, boolean force) throws ApplicationException;

	void saveEvent(Event event, boolean force) throws ApplicationException;

	void saveEventSource(EventSource eventSource, boolean force) throws ApplicationException;

//	void saveAlarm(Alarm alarm, boolean force) throws ApplicationException;


	void saveEventTypes(Set collection, boolean force) throws ApplicationException;

//	void saveAlarmTypes(Set collection, boolean force) throws ApplicationException;

	void saveEvents(Set collection, boolean force) throws ApplicationException;

	void saveEventSources(Set collection, boolean force) throws ApplicationException;

//	void saveAlarms(Set collection, boolean force) throws ApplicationException;


	java.util.Set refresh(java.util.Set storableObjects) throws ApplicationException;


	void delete(Identifier id) throws IllegalDataException;

	void delete(Set objects);
}
