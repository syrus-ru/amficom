/*-
 * $Id: EventProcessorRegistry.java,v 1.7 2006/04/19 14:13:46 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import static java.util.logging.Level.FINEST;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.eventv2.Event;
import com.syrus.AMFICOM.eventv2.EventType;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.7 $, $Date: 2006/04/19 14:13:46 $
 * @module leserver
 */
final class EventProcessorRegistry {
	static final Map<EventType, Set<EventProcessor>> REGISTRY =
			new EnumMap<EventType, Set<EventProcessor>>(EventType.class);

	private static final Set<EventType> QUIET_EVENT_TYPES =
			Collections.synchronizedSet(EnumSet.noneOf(EventType.class)); 

	private EventProcessorRegistry() {
		assert false;
	}

	/**
	 * @param eventProcessor
	 */
	static synchronized void registerEventProcessor(
			final EventProcessor eventProcessor) {
		if (eventProcessor == null) {
			throw new NullPointerException();
		}

		final EventType eventType = eventProcessor.getEventType();
		Set<EventProcessor> eventProcessors = REGISTRY.get(eventType);
		if (eventProcessors == null) {
			eventProcessors = new HashSet<EventProcessor>();
			REGISTRY.put(eventType, eventProcessors);
			
		}
		eventProcessors.add(eventProcessor);
	}

	static synchronized void startProcessors() {
		final Runtime runtime = Runtime.getRuntime();
		for (final Set<EventProcessor> eventProcessors : REGISTRY.values()) {
			for (final EventProcessor eventProcessor : eventProcessors) {
				eventProcessor.start();
				final String name = eventProcessor.getName();
				runtime.addShutdownHook(new Thread(name + "Shutdown") {
					@Override
					public void run() {
						Log.debugMessage(name
								+ " is being shut down",
								FINEST);
						final long t0 = System.nanoTime();
						eventProcessor.putPoison();
						try {
							eventProcessor.join();
							final long t1 = System.nanoTime();
							Log.debugMessage(name
									+ " has been shut down in "
									+ ((t1 - t0) / 1e9)
									+ " second(s)",
									FINEST);
						} catch (final InterruptedException ie) {
							Log.debugMessage(ie, SEVERE);
						}
					}
				});
			}
		}
	}

	/**
	 * Thread-safe provided all event processors have already got
	 * registered.
	 *
	 * @param event
	 */
	static void processEvent(final Event<?> event) {
		if (event == null) {
			throw new NullPointerException();
		}

		final EventType eventType = event.getType();
		final Set<EventProcessor> eventProcessors = REGISTRY.get(eventType);
		if (eventProcessors == null || eventProcessors.isEmpty()) {
			if (!QUIET_EVENT_TYPES.contains(eventType)) {
				QUIET_EVENT_TYPES.add(eventType);
				Log.debugMessage("no processor(s) found for event type: "
						+ eventType + " (this message will only be issued once)",
						INFO);
			}
			return;
		}
		for (final EventProcessor eventProcessor : eventProcessors) {
			eventProcessor.put(event);
		}
	}
}
