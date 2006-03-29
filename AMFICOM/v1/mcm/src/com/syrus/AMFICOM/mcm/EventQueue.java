/*-
 * $Id: EventQueue.java,v 1.7.2.4 2006/03/29 15:33:24 arseniy Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mcm;

import static com.syrus.AMFICOM.mcm.MeasurementControlModule.KEY_MAX_FALLS;
import static com.syrus.AMFICOM.mcm.MeasurementControlModule.KEY_TICK_TIME;
import static com.syrus.AMFICOM.mcm.MeasurementControlModule.TICK_TIME;
import static java.util.logging.Level.FINEST;

import java.util.Collections;
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
 * @version $Revision: 1.7.2.4 $, $Date: 2006/03/29 15:33:24 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */
final class EventQueue extends SleepButWorkThread {
	/*	Error codes for method processFall()	*/
	private static final int FALL_CODE_ESTABLISH_CONNECTION = 1;
	private static final int FALL_CODE_TRANSMIT_EVENTS = 2;

	private List<Event<?>> eventQueue;
	private List<Event<?>> sendingEvents;
	private volatile boolean running;

	public EventQueue() {
		super(ApplicationProperties.getInt(KEY_TICK_TIME, TICK_TIME) * 1000,
				ApplicationProperties.getInt(KEY_MAX_FALLS, MAX_FALLS));

		super.setName("EventQueue");

		this.eventQueue = Collections.synchronizedList(new LinkedList<Event<?>>());
		this.sendingEvents = new LinkedList<Event<?>>();
		this.running = true;
	}

	void addEvent(final Event<?> event) {
		Log.debugMessage("Event: " + event + " is being added to outbox", FINEST);
		synchronized (this.eventQueue) {
			this.eventQueue.add(event);
			this.eventQueue.notifyAll();
		}
	}

	@Override
	public void run() {
		final BaseConnectionManager connectionManager = MCMSessionEnvironment.getInstance().getConnectionManager();

		while (this.running) {
			synchronized (this.eventQueue) {
				while (this.eventQueue.isEmpty() && this.sendingEvents.isEmpty() && this.running) {
					try {
						this.eventQueue.wait(10000);
					} catch (final InterruptedException ie) {
						Log.debugMessage(this.getName() + " -- interrupted", Log.DEBUGLEVEL07);
					}
				}
			}

			synchronized (this.eventQueue) {
				this.sendingEvents.addAll(this.eventQueue);
				this.eventQueue.clear();
			}

			if (this.sendingEvents.isEmpty()) {
				continue;
			}

			final EventServer eventServer;
			try {
				eventServer = connectionManager.getEventServerReference();
			} catch (CommunicationException ce) {
				Log.errorMessage(ce);
				super.fallCode = FALL_CODE_ESTABLISH_CONNECTION;
				super.sleepCauseOfFall();
				continue;
			}

			final IdlEvent[] idlEvents = this.createIdlEventArray(connectionManager);
			final int length = idlEvents.length;
			Log.debugMessage("Sending " + length + " event(s) to the event server", FINEST);
			try {
				eventServer.receiveEvents(idlEvents);
				Log.debugMessage("Done sending " + length + " event(s) to the event server", FINEST);
				this.sendingEvents.clear();
			} catch (IdlEventProcessingException iepe) {
				Log.errorMessage(iepe);
				super.fallCode = FALL_CODE_TRANSMIT_EVENTS;
				super.sleepCauseOfFall();
			}
		}
	}

	private IdlEvent[] createIdlEventArray(final BaseConnectionManager connectionManager) {
		final ORB orb = connectionManager.getCORBAServer().getOrb();
		final IdlEvent[] idlEvents = new IdlEvent[this.sendingEvents.size()];
		int i = 0;
		for (final Event<?> event : this.sendingEvents) {
			idlEvents[i++] = event.getIdlTransferable(orb);
		}
		return idlEvents;
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
				Log.errorMessage("ERROR: Many errors during transmit event. Clearing sending events.");
				this.sendingEvents.clear();
				break;
			default:
				Log.errorMessage("ERROR: Unknown error code: " + super.fallCode);
		}
	}

	void shutdown() {
		this.running = false;
		synchronized (this.eventQueue) {
			this.eventQueue.notifyAll();
		}
	}
}
