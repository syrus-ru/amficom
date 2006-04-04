/*-
 * $Id: EventProcessorRegistry.java,v 1.6 2006/04/04 11:32:56 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.eventv2.Event;
import com.syrus.AMFICOM.eventv2.EventType;
import com.syrus.util.Log;
import com.syrus.util.transport.idl.IdlConversionException;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2006/04/04 11:32:56 $
 * @module leserver
 */
final class EventProcessorRegistry {
	static final Map<EventType, Queue> REGISTRY =
			new EnumMap<EventType, Queue>(EventType.class);

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
		Queue queue = REGISTRY.get(eventType);
		if (queue == null) {
			queue = new Queue(eventType);
			REGISTRY.put(eventType, queue);
			
		}
		queue.addConsumer(eventProcessor);
	}

	static synchronized void startProcessors() {
		for (final Queue queue : REGISTRY.values()) {
			queue.start();
		}
		Runtime.getRuntime().addShutdownHook(new Thread("EventProcessorRegistryShutdown") {
			@Override
			public void run() {
				for (final Queue queue : REGISTRY.values()) {
					queue.put(Queue.POISON);
				}
				for (final Queue queue : REGISTRY.values()) {
					try {
						queue.join();
					} catch (final InterruptedException ie) {
						Log.debugMessage(ie, SEVERE);
					}
				}
			}
		});
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
		final Queue queue = REGISTRY.get(eventType);
		if (queue == null) {
			if (!QUIET_EVENT_TYPES.contains(eventType)) {
				QUIET_EVENT_TYPES.add(eventType);
				Log.debugMessage("no processor(s) found for event type: "
						+ eventType + " (this message will only be issued once)",
						INFO);
			}
			return;
		}
		if (queue.isAlive()) {
			queue.put(event);
		} else {
			Log.debugMessage(queue.getName()
					+ " is in an illegal state: " + queue.getState()
					+ "; dropping event: " + event,
					SEVERE);
		}
	}

	/**
	 * @author Andrew ``Bass'' Shcheglov
	 * @author $Author: bass $
	 * @version $Revision: 1.6 $, $Date: 2006/04/04 11:32:56 $
	 * @module leserver
	 */
	private static final class Queue extends Thread {
		/**
		 * Initialized at object creation, since a new queue is created
		 * if at least one {@link EventProcessor} gets registered for a
		 * given {@link EventType}.
		 */
		private final Set<EventProcessor> consumers = new HashSet<EventProcessor>();

		private final Object putLock = new Object();

		private final BlockingQueue<Event<?>> queueEntries;

		private boolean poisonPut = false;

		static final Event<?> POISON = newPoison();

		private Queue(final EventType eventType, final int capacity) {
			super(eventType.getCodename() + "EventQueue");
			this.queueEntries = new LinkedBlockingQueue<Event<?>>(capacity);
		}

		Queue(final EventType eventType) {
			this(eventType, Integer.MAX_VALUE);
		}

		/**
		 * Thread-compatible, but not thread-safe.
		 *
		 * @param processor
		 */
		void addConsumer(final EventProcessor processor) {
			this.consumers.add(processor);
		}

		/**
		 * @param event
		 * @see BlockingQueue#put(Object)
		 */
		void put(final Event<?> event) {
			synchronized (this.putLock) {
				if (event == POISON) {
					this.poisonPut = true;
				} else if (this.poisonPut) {
					Log.debugMessage("Event: " + event
							+ " has just been dropped, for the queue is already shut down",
							SEVERE);
					return;
				}
			}

			try {
				this.queueEntries.put(event);
			} catch (final InterruptedException ie) {
				Log.debugMessage("Event: " + event
						+ " has just been droppped",
						SEVERE);
				Log.debugMessage(ie, SEVERE);
			}
		}

		@Override
		public void run() {
			while (true) {
				try {
					final Event<?> event = this.queueEntries.take();
					if (event == POISON) {
						break;
					}
					/**
					 * @todo rewrite in order for processors
					 * to run asynchronously; currently the
					 * code is ok only under an assumption
					 * we have ho more than 1 processor per
					 * event type.
					 */
					for (final EventProcessor processor : this.consumers) {
						processor.processEvent(event);
					}
				} catch (final InterruptedException ie) {
					Log.debugMessage(ie, SEVERE);
				}
			}
		}

		@SuppressWarnings("unchecked")
		private static Event<?> newPoison() {
			return new Event() {
				public EventType getType() {
					throw new UnsupportedOperationException();
				}

				public void fromIdlTransferable(final IDLEntity transferable)
				throws IdlConversionException{
					throw new UnsupportedOperationException();
				}

				public IDLEntity getIdlTransferable(final ORB orb){
					throw new UnsupportedOperationException();
				}
			};
		}
	}
}
