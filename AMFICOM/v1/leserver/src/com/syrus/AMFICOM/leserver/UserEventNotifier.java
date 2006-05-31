/*-
 * $Id: UserEventNotifier.java,v 1.13 2006/05/31 07:45:19 bass Exp $
 * 
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import static com.syrus.AMFICOM.eventv2.DeliveryMethod.EMAIL;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.event.Event;
import com.syrus.AMFICOM.event.EventType;
import com.syrus.AMFICOM.eventv2.DeliveryMethod;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.13 $, $Date: 2006/05/31 07:45:19 $
 * @author Tashoyan Arseniy Feliksovich
 * @author $Author: bass $
 * @module leserver
 */
final class UserEventNotifier extends SleepButWorkThread {
	public static final String KEY_EVENT_NOTIFIER_TICK_TIME = "EventProcessorTickTime";
	public static final String KEY_EVENT_NOTIFIER_MAX_FALLS = "EventProcessorMaxFalls";

	public static final int EVENT_NOTIFIER_TICK_TIME = 5;	//sec

	private Identifier userId;
	private List<Event> eventQueue;

	protected UserEventNotifier(Identifier userId) {
		super(ApplicationProperties.getInt(KEY_EVENT_NOTIFIER_TICK_TIME, EVENT_NOTIFIER_TICK_TIME) * 1000,
				ApplicationProperties.getInt(KEY_EVENT_NOTIFIER_MAX_FALLS, MAX_FALLS));

		this.userId = userId;
		this.eventQueue = Collections.synchronizedList(new LinkedList<Event>());
	}

	protected void addEvent(final Event event) {
		if (!this.eventQueue.contains(event))
			this.eventQueue.add(event);
	}

	@Override
	public void run() {
		synchronized (this.eventQueue) {
			for (final Iterator<Event> it1 = this.eventQueue.iterator(); it1.hasNext();) {
				final Event event = it1.next();
				final EventType eventType = event.getType();
				boolean delivered = false;
				for (final DeliveryMethod deliveryMethod : eventType.getUserAlertKinds(this.userId)) {
					final Alerter alerter = Alerter.getAlerter(EMAIL);
					if (alerter != null) {
						try {
							alerter.notifyUser(this.userId);
							delivered = true;
						}
						catch (EventNotificationException ene) {
							Log.errorMessage(ene);
						}
					}
					else {
						Log.errorMessage("Unknown alert kind -- " + deliveryMethod.getCodename()
								+ " for event type '" + eventType.getId()
								+ "' and user '" + this.userId + "'");
					}
				}
				if (delivered)
					it1.remove();
			}
		}
	}

	@Override
	protected void processFall() {
		switch (super.fallCode) {
			case FALL_CODE_NO_ERROR:
				break;
			default:
				Log.errorMessage("processError | Unknown error code: " + super.fallCode);
		}
	}

}
