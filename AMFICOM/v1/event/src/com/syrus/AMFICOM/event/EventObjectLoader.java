/*
 * $Id: EventObjectLoader.java,v 1.3 2005/02/08 09:33:06 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.event;

import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.VersionCollisionException;

/**
 * @version $Revision: 1.3 $, $Date: 2005/02/08 09:33:06 $
 * @author $Author: arseniy $
 * @module event_v1
 */

public interface EventObjectLoader {

	EventType loadEventType(Identifier id) throws DatabaseException, CommunicationException;

//	AlarmType loadAlarmType(Identifier id) throws DatabaseException, CommunicationException;

	Event loadEvent(Identifier id) throws DatabaseException, CommunicationException;

//	Alarm loadAlarm(Identifier id) throws DatabaseException, CommunicationException;


	List loadEventTypes(List ids) throws DatabaseException, CommunicationException;

//	List loadAlarmTypes(List ids) throws DatabaseException, CommunicationException;

	List loadEvents(List ids) throws DatabaseException, CommunicationException;

//	List loadAlarms(List ids) throws DatabaseException, CommunicationException;


	List loadEventTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

//	List loadAlarmTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	List loadEventsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

//	List loadAlarmsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;


	void saveEventType(EventType eventType, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

//	void saveAlarmType(AlarmType alarmType, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveEvent(Event event, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

//	void saveAlarm(Alarm alarm, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;


	void saveEventTypes(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

//	void saveAlarmTypes(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveEvents(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

//	void saveAlarms(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;


	java.util.Set refresh(java.util.Set storableObjects) throws CommunicationException, DatabaseException;


	void delete(Identifier id) throws CommunicationException, DatabaseException;

	void delete(List objects) throws CommunicationException, DatabaseException, IllegalDataException;
}
