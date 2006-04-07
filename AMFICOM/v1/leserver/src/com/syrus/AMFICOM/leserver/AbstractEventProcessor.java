/*-
 * $Id: AbstractEventProcessor.java,v 1.1.2.2 2006/04/07 09:43:01 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import static java.util.logging.Level.SEVERE;

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
 * @version $Revision: 1.1.2.2 $, $Date: 2006/04/07 09:43:01 $
 * @module leserver
 */
abstract class AbstractEventProcessor implements EventProcessor, Runnable {
	private final BlockingQueue<Event<?>> queue;

	private final Thread executor;

	private final Object putLock = new Object();

	private boolean poisonPut = false;

	static final Event<?> POISON = newPoison();

	AbstractEventProcessor(final int capacity) {
		this.queue = new LinkedBlockingQueue<Event<?>>(capacity);
		this.executor = new Thread(this, this.getEventType().getCodename() + "EventQueue");
	}

	/**
	 * @param event
	 * @see EventProcessor#put(Event)
	 * @see BlockingQueue#put(Object)
	 */
	public final void put(final Event<?> event) {
		if (!this.executor.isAlive()) {
			Log.debugMessage(this.getName()
					+ " is in an illegal state: " + this.executor.getState()
					+ "; dropping event: " + event,
					SEVERE);
			return;
		}

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
			this.queue.put(event);
		} catch (final InterruptedException ie) {
			Log.debugMessage("Event: " + event
					+ " has just been droppped",
					SEVERE);
			Log.debugMessage(ie, SEVERE);
		}
	}

	/**
	 * @see EventProcessor#putPoison()
	 */
	public final void putPoison() {
		this.put(POISON);
	}

	/**
	 * @see Runnable#run()
	 */
	public final void run() {
		while (true) {
			try {
				final Event<?> event = this.queue.take();
				if (event == POISON) {
					break;
				}
				this.processEvent(event);
			} catch (final InterruptedException ie) {
				Log.debugMessage(ie, SEVERE);
			} catch (final Throwable t) {
				Log.debugMessage(t, SEVERE);
			}
		}
	}

	/**
	 * @see EventProcessor#start()
	 * @see Thread#start()
	 */
	public final void start() {
		this.executor.start();
	}

	/**
	 * @throws InterruptedException
	 * @see EventProcessor#join()
	 * @see Thread#join()
	 */
	public final void join() throws InterruptedException {
		this.executor.join();
	}

	/**
	 * @see EventProcessor#getName()
	 * @see Thread#getName()
	 */
	public final String getName() {
		return this.executor.getName();
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
