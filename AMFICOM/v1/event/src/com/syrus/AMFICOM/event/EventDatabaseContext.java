/*-
 * $Id: EventDatabaseContext.java,v 1.8 2005/04/01 11:08:47 bass Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.event;

import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.8 $, $Date: 2005/04/01 11:08:47 $
 * @author $Author: bass $
 * @module event_v1
 */
public final class EventDatabaseContext {
	private static EventTypeDatabase	eventTypeDatabase;
//	private static AlarmTypeDatabase	alarmTypeDatabase;

	private static EventDatabase		eventDatabase;
	private static EventSourceDatabase	eventSourceDatabase;
//	private static AlarmDatabase		alarmDatabase;

	private EventDatabaseContext() {
		assert false;
	}

	public static void init(
			final EventTypeDatabase		eventTypeDatabase1,
//			final AlarmTypeDatabase		alarmTypeDatabase1,
			final EventDatabase		eventDatabase1,
			final EventSourceDatabase	eventSourceDatabase1
//			final AlarmDatabase		alarmDatabase1
			) {
		if (eventTypeDatabase1 != null)
			eventTypeDatabase = eventTypeDatabase1;
//		if (alarmTypeDatabase1 != null)
//			alarmTypeDatabase = alarmTypeDatabase1;
		if (eventDatabase1 != null)
			eventDatabase = eventDatabase1;
		if (eventSourceDatabase1 != null)
			eventSourceDatabase = eventSourceDatabase1;
//		if (alarmDatabase1 != null)
//			alarmDatabase = alarmDatabase1;
	}

	public static StorableObjectDatabase getDatabase(final Short entityCode) {
		return getDatabase(entityCode.shortValue());
	}

	public static StorableObjectDatabase getDatabase(final short entityCode) {
		switch (entityCode) {
			case ObjectEntities.EVENTTYPE_ENTITY_CODE:
				return getEventTypeDatabase();
			case ObjectEntities.EVENT_ENTITY_CODE:
				return getEventDatabase();
			case ObjectEntities.EVENTSOURCE_ENTITY_CODE:
				return getEventSourceDatabase();
//			case ObjectEntities.ALARMTYPE_ENTITY_CODE:
//				return getAlarmTypeDatabase();
//			case ObjectEntities.ALARM_ENTITY_CODE:
//				return getAlarmDatabase();
			default:
				Log.errorMessage("EventDatabaseContext.getDatabase | Unknown entity: " + entityCode); //$NON-NLS-1$
				return null;
		}
	}

	public static EventTypeDatabase getEventTypeDatabase() {
		return eventTypeDatabase;
	}

	public static EventDatabase getEventDatabase() {
		return eventDatabase;
	}

	public static EventSourceDatabase getEventSourceDatabase() {
		return eventSourceDatabase;
	}

//	public static AlarmTypeDatabase getAlarmTypeDatabase() {
//		return alarmTypeDatabase;
//	}
//
//	public static AlarmDatabase getAlarmDatabase() {
//		return alarmDatabase;
//	}
}
