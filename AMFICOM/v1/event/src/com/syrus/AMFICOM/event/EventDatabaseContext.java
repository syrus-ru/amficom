/*
 * $Id: EventDatabaseContext.java,v 1.2 2004/12/24 18:49:03 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.event;

import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @version $Revision: 1.2 $, $Date: 2004/12/24 18:49:03 $
 * @author $Author: arseniy $
 * @module event_v1
 */

public class EventDatabaseContext {
	protected static StorableObjectDatabase	eventTypeDatabase;
	protected static StorableObjectDatabase	alarmTypeDatabase;

	protected static StorableObjectDatabase	eventDatabase;
	protected static StorableObjectDatabase	alarmDatabase;

	private EventDatabaseContext() {
		//singleton
	}

	public static void init(StorableObjectDatabase eventTypeDatabase1,
													StorableObjectDatabase alarmTypeDatabase1,
													StorableObjectDatabase eventDatabase1,
													StorableObjectDatabase alarmDatabase1) {

		if (eventTypeDatabase1 != null)
			eventTypeDatabase = eventTypeDatabase1;

		if (alarmTypeDatabase1 != null)
			alarmTypeDatabase = alarmTypeDatabase1;

		if (eventDatabase1 != null)
			eventDatabase = eventDatabase1;

		if (alarmDatabase1 != null)
			alarmDatabase = alarmDatabase1;
	}

	public static StorableObjectDatabase getDatabase(short entityCode) {
		switch (entityCode) {

			case ObjectEntities.EVENTTYPE_ENTITY_CODE:
				return eventTypeDatabase;

			case ObjectEntities.EVENT_ENTITY_CODE:
				return eventDatabase;

			case ObjectEntities.ALARMTYPE_ENTITY_CODE:
				return alarmTypeDatabase;

			case ObjectEntities.ALARM_ENTITY_CODE:
				return alarmDatabase;

			default:
				return null;
			}
	}

	public static StorableObjectDatabase getEventTypeDatabase() {
		return eventTypeDatabase;
	}

	public static StorableObjectDatabase getEventDatabase() {
		return eventDatabase;
	}

	public static StorableObjectDatabase getAlarmTypeDatabase() {
		return alarmTypeDatabase;
	}

	public static StorableObjectDatabase getAlarmDatabase() {
		return alarmDatabase;
	}
}
