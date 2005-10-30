/*-
 * $Id: EventProcessorRegistry.java,v 1.2 2005/10/30 14:49:11 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import static java.util.logging.Level.INFO;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.eventv2.Event;
import com.syrus.AMFICOM.eventv2.EventType;
import com.syrus.AMFICOM.eventv2.corba.IdlEvent;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/10/30 14:49:11 $
 * @module leserver
 */
final class EventProcessorRegistry {
	private static Map<EventType, List<EventProcessor>> REGISTRY =
			new HashMap<EventType, List<EventProcessor>>();

	private static Set<EventType> QUIET_EVENT_TYPES = EnumSet.noneOf(EventType.class); 

	private EventProcessorRegistry() {
		assert false;
	}

	/**
	 * @param eventProcessor
	 */
	static void registerEventProcessor(
			final EventProcessor eventProcessor) {
		if (eventProcessor == null) {
			throw new NullPointerException();
		}

		final EventType eventType = eventProcessor.getEventType();
		List<EventProcessor> eventProcessors = REGISTRY.get(eventProcessor.getEventType());
		if (eventProcessors == null) {
			eventProcessors = Collections.synchronizedList(new LinkedList<EventProcessor>());
			REGISTRY.put(eventType, eventProcessors);
		}
		if (!eventProcessors.contains(eventProcessor)) {
			eventProcessors.add(eventProcessor);
		}
	}

	/**
	 * @param event
	 * @throws EventProcessingException
	 */
	static void processEvent(final Event<? extends IdlEvent> event)
	throws EventProcessingException {
		if (event == null) {
			throw new NullPointerException();
		}

		final EventType eventType = event.getType();
		final List<EventProcessor> eventProcessors = REGISTRY.get(eventType);
		if (eventProcessors == null || eventProcessors.isEmpty()) {
			if (!QUIET_EVENT_TYPES.contains(eventType)) {
				QUIET_EVENT_TYPES.add(eventType);
				Log.debugMessage("no processor(s) found for event type: "
						+ eventType + " (this message will only be issued once)",
						INFO);
			}
			return;
		}
		synchronized (eventProcessors) {
			for (final EventProcessor eventProcessor : eventProcessors) {
				eventProcessor.processEvent(event);
			}
		}
	}
}
