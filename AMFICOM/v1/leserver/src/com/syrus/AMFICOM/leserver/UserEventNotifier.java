/*
 * $Id: UserEventNotifier.java,v 1.1 2005/05/30 14:40:07 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.leserver;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.event.Event;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2005/05/30 14:40:07 $
 * @author $Author: arseniy $
 * @module leserver_v1
 */
final class UserEventNotifier extends SleepButWorkThread {
	public static final String KEY_EVENT_NOTIFIER_TICK_TIME = "EventProcessorTickTime";
	public static final String KEY_EVENT_NOTIFIER_MAX_FALLS = "EventProcessorMaxFalls";

	public static final int EVENT_NOTIFIER_TICK_TIME = 5;	//sec

	private Identifier userId;
	private List eventQueue;

	protected UserEventNotifier(Identifier userId) {
		super(ApplicationProperties.getInt(KEY_EVENT_NOTIFIER_TICK_TIME, EVENT_NOTIFIER_TICK_TIME) * 1000,
				ApplicationProperties.getInt(KEY_EVENT_NOTIFIER_MAX_FALLS, MAX_FALLS));

		this.userId = userId;
		this.eventQueue = Collections.synchronizedList(new LinkedList());
	}

	protected void addEvent(final Event event) {
		if (!this.eventQueue.contains(event))
			this.eventQueue.add(event);
	}

	public void run() {
		
	}

	protected void processFall() {
		switch (super.fallCode) {
			case FALL_CODE_NO_ERROR:
				break;
			default:
				Log.errorMessage("processError | Unknown error code: " + super.fallCode);
		}
	}
}

}
