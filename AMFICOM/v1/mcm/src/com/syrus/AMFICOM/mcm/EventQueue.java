/*-
 * $Id: EventQueue.java,v 1.1 2005/10/12 12:24:50 bass Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mcm;

import static java.util.logging.Level.INFO;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.eventv2.ReflectogramMismatchEvent;
import com.syrus.AMFICOM.eventv2.corba.IdlEvent;
import com.syrus.AMFICOM.general.BaseConnectionManager;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.leserver.corba.EventServer;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2005/10/12 12:24:50 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */
final class EventQueue extends SleepButWorkThread {
	/*	Error codes for method processFall()	*/
	private static final int FALL_CODE_ESTABLISH_CONNECTION = 1;
	private static final int FALL_CODE_TRANSMIT_EVENTS = 2;

	private List<ReflectogramMismatchEvent> eventEqueue;
	private volatile boolean running;

	public EventQueue() {
		super(ApplicationProperties.getInt(MeasurementControlModule.KEY_TICK_TIME, MeasurementControlModule.TICK_TIME) * 1000,
				ApplicationProperties.getInt(MeasurementControlModule.KEY_MAX_FALLS, SleepButWorkThread.MAX_FALLS));

		super.setName("EventQueue");

		this.eventEqueue = Collections.synchronizedList(new LinkedList<ReflectogramMismatchEvent>());
		this.running = true;
	}

	@SuppressWarnings("unused")
	synchronized void addEvent(final ReflectogramMismatchEvent event)
	throws EventQueueFullException {
		Log.debugMessage("EventQueue.addEvent() | Event: " + event + " added to outbox", INFO);
		this.eventEqueue.add(event);
		this.notifyAll();
	}

	@Override
	public void run() {
		final MCMSessionEnvironment mcmSessionEnvironment = MCMSessionEnvironment.getInstance();
		final BaseConnectionManager connectionManager = mcmSessionEnvironment.getConnectionManager();

		while (this.running) {

			synchronized (this) {
				while (this.eventEqueue.isEmpty()) {
					try {
						this.wait(10000);
					} catch (InterruptedException ie) {
						Log.debugMessage(this.getName() + " -- interrupted", Log.DEBUGLEVEL07);
					}
				}
			}

			try {
				final EventServer eventServer = connectionManager.getEventServerReference();

				final IdlEvent[] idlEvents = this.createIdlEventArray(connectionManager);
				eventServer.receiveEvents(idlEvents);
				this.eventEqueue.clear();
			} catch (CommunicationException ce) {
				Log.errorException(ce);
				super.fallCode = FALL_CODE_ESTABLISH_CONNECTION;
				super.sleepCauseOfFall();
			} catch (AMFICOMRemoteException are) {
				Log.errorMessage("EventQueue.run() | Cannot transmit events -- " + are.message);
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
				Log.errorMessage("EventQueue.processFall() | ERROR: Many errors during establishing connection. �� ������ - ��� �� �������.");
				break;
			case FALL_CODE_TRANSMIT_EVENTS:
				Log.errorMessage("EventQueue.processFall() | ERROR: Many errors during transmit event. �� ������ - ��� �� �������.");
				break;
			default:
				Log.errorMessage("EventQueue.processFall() | ERROR: Unknown error code: " + super.fallCode);
		}
	}

	private IdlEvent[] createIdlEventArray(final BaseConnectionManager connectionManager) {
		final ORB orb = connectionManager.getCORBAServer().getOrb();
		final IdlEvent[] idlEvents = new IdlEvent[this.eventEqueue.size()];
		int i = 0;
		for (final ReflectogramMismatchEvent event : this.eventEqueue) {
			idlEvents[i++] = event.getTransferable(orb);
		}
		return idlEvents;
	}

	synchronized void shutdown() {
		this.running = false;
		this.notifyAll();
	}
}