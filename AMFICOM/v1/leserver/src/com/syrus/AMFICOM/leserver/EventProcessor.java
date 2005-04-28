/*
 * $Id: EventProcessor.java,v 1.1 2005/04/28 10:32:35 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.leserver;

import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.event.Event;
import com.syrus.AMFICOM.event.EventType;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2005/04/28 10:32:35 $
 * @author $Author: arseniy $
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
		this.running = true;
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
