/*-
 * $Id: EventDatabaseContext.java,v 1.11 2005/05/23 18:45:10 bass Exp $
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
 * @version $Revision: 1.11 $, $Date: 2005/05/23 18:45:10 $
 * @author $Author: bass $
 * @module event_v1
 */
public final class EventDatabaseContext {
	private static EventTypeDatabase eventTypeDatabase;

	private static EventDatabase eventDatabase;

	private static EventSourceDatabase eventSourceDatabase;

	private EventDatabaseContext() {
		assert false;
	}

	public static void init(final EventTypeDatabase eventTypeDatabase1,
			final EventDatabase eventDatabase1,
			final EventSourceDatabase eventSourceDatabase1) {
		if (eventTypeDatabase1 != null)
			eventTypeDatabase = eventTypeDatabase1;
		if (eventDatabase1 != null)
			eventDatabase = eventDatabase1;
		if (eventSourceDatabase1 != null)
			eventSourceDatabase = eventSourceDatabase1;
	}

	public static StorableObjectDatabase getDatabase(final Short entityCode) {
		return getDatabase(entityCode.shortValue());
	}

	public static StorableObjectDatabase getDatabase(final short entityCode) {
		switch (entityCode) {
			case ObjectEntities.EVENTTYPE_ENTITY_CODE:
				return eventTypeDatabase;
			case ObjectEntities.EVENT_ENTITY_CODE:
				return eventDatabase;
			case ObjectEntities.EVENTSOURCE_ENTITY_CODE:
				return eventSourceDatabase;
			default:
				Log.errorMessage("EventDatabaseContext.getDatabase | Unknown entity: " + entityCode);
				return null;
		}
	}
}
