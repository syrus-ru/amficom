/*
 * $Id: EventProcessor.java,v 1.4 2005/05/18 13:29:31 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.leserver;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.event.Event;
import com.syrus.AMFICOM.event.EventType;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.4 $, $Date: 2005/05/18 13:29:31 $
 * @author $Author: bass $
 * @module leserver_v1
 */
final class EventProcessor extends SleepButWorkThread {
	public static final String KEY_EVENT_PROCESSOR_TICK_TIME = "EventProcessorTickTime";
	public static final String KEY_EVENT_PROCESSOR_MAX_FALLS = "EventProcessorMaxFalls";

	public static final int EVENT_PROCESSOR_TICK_TIME = 5;	//sec

	/*	Event queue*/
	private static List eventQueue;

	private boolean running;

	public EventProcessor() {
		super(ApplicationProperties.getInt(KEY_EVENT_PROCESSOR_TICK_TIME, EVENT_PROCESSOR_TICK_TIME) * 1000,
				ApplicationProperties.getInt(KEY_EVENT_PROCESSOR_MAX_FALLS, MAX_FALLS));
		eventQueue = Collections.synchronizedList(new LinkedList());
		this.running = true;

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				EventProcessor.this.shutdown();
			}
		});
	}

	public void run() {
		while (this.running) {
			synchronized (eventQueue) {
				Event event;
				EventType eventType;
				for (Iterator it = eventQueue.iterator(); it.hasNext();) {
					event = (Event) it.next();
					eventType = (EventType) event.getType();
				}
			}

			try {
				sleep(super.initialTimeToSleep);
			}
			catch (InterruptedException ie) {
				Log.errorException(ie);
			}
		}
	}

	protected void processFall() {
		switch (super.fallCode) {
			case FALL_CODE_NO_ERROR:
				break;
			default:
				Log.errorMessage("processError | Unknown error code: " + super.fallCode);
		}
	}

	protected static void addEventToQueue(Event event) {
		eventQueue.add(event);
	}

	protected void shutdown() {
		this.running = false;
	}

}
