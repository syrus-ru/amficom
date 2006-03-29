/*-
 * $Id: EventQueue.java,v 1.8.2.8 2006/03/29 05:45:53 bass Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mcm;

import static java.util.logging.Level.FINEST;

import java.util.LinkedList;
import java.util.List;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.eventv2.Event;
import com.syrus.AMFICOM.eventv2.corba.IdlEvent;
import com.syrus.AMFICOM.general.BaseConnectionManager;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.AMFICOM.leserver.corba.EventServer;
import com.syrus.AMFICOM.leserver.corba.EventServerPackage.IdlEventProcessingException;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.8.2.8 $, $Date: 2006/03/29 05:45:53 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */
final class EventQueue extends SleepButWorkThread {
	/*	Error codes for method processFall()	*/
	private static final int FALL_CODE_ESTABLISH_CONNECTION = 1;
	private static final int FALL_CODE_TRANSMIT_EVENTS = 2;

	private List<Event<?>> eventQueue;
	private volatile boolean running;

	public EventQueue() {
		super(ApplicationProperties.getInt(MeasurementControlModule.KEY_TICK_TIME, MeasurementControlModule.TICK_TIME) * 1000,
				ApplicationProperties.getInt(MeasurementControlModule.KEY_MAX_FALLS, SleepButWorkThread.MAX_FALLS));

		super.setName("EventQueue");

		this.eventQueue = new LinkedList<Event<?>>();
		this.running = true;
	}

	@SuppressWarnings("unused")
	void addEvent(final Event<?> event) throws EventQueueFullException {
		Log.debugMessage("Event: " + event + " is being added to outbox", FINEST);
		synchronized (this.eventQueue) {
			this.eventQueue.add(event);
			this.eventQueue.notifyAll();
		}
	}

	@Override
	public void run() {
		final MCMSessionEnvironment mcmSessionEnvironment = MCMSessionEnvironment.getInstance();
		final BaseConnectionManager connectionManager = mcmSessionEnvironment.getConnectionManager();

		while (this.running) {

			synchronized (this.eventQueue) {
				while (this.eventQueue.isEmpty() && this.running) {
					try {
						this.eventQueue.wait(10000);
					} catch (final InterruptedException ie) {
						Log.debugMessage(this.getName() + " -- interrupted", Log.DEBUGLEVEL07);
					}
				}
			}

			if (this.eventQueue.isEmpty()) {
				continue;
			}

			try {
				final EventServer eventServer = connectionManager.getEventServerReference();

				final IdlEvent[] idlEvents = this.createIdlEventArray(connectionManager);
				final int length = idlEvents.length;
				Log.debugMessage("Sending " + length + " event(s) to the event server", FINEST);
				eventServer.receiveEvents(idlEvents);
				Log.debugMessage("Done sending " + length + " event(s) to the event server", FINEST);
			} catch (final CommunicationException ce) {
				Log.errorMessage(ce);
				super.fallCode = FALL_CODE_ESTABLISH_CONNECTION;
				super.sleepCauseOfFall();
			} catch (final IdlEventProcessingException epe) {
				Log.errorMessage("Cannot transmit events -- " + epe.message);
				super.fallCode = FALL_CODE_TRANSMIT_EVENTS;
				super.sleepCauseOfFall();
			}

		}
	}

	@Override
	protected void processFall() {
		switch (super.fallCode) {
			case FALL_CODE_NO_ERROR:
				break;
			case FALL_CODE_ESTABLISH_CONNECTION:
				Log.errorMessage("ERROR: Many errors during establishing connection. Чё делать - ума не приложу.");
				break;
			case FALL_CODE_TRANSMIT_EVENTS:
				Log.errorMessage("ERROR: Many errors during transmit event. Чё делать - ума не приложу.");
				break;
			default:
				Log.errorMessage("ERROR: Unknown error code: " + super.fallCode);
		}
	}

	private IdlEvent[] createIdlEventArray(final BaseConnectionManager connectionManager) {
		final ORB orb = connectionManager.getCORBAServer().getOrb();
		final IdlEvent[] idlEvents;
		synchronized (this.eventQueue) {
			idlEvents = new IdlEvent[this.eventQueue.size()];
			int i = 0;
			for (final Event<?> event : this.eventQueue) {
				idlEvents[i++] = event.getIdlTransferable(orb);
			}
			this.eventQueue.clear();
		}
		return idlEvents;
	}

	void shutdown() {
		this.running = false;
		synchronized (this.eventQueue) {
			this.eventQueue.notifyAll();
		}
	}
}
