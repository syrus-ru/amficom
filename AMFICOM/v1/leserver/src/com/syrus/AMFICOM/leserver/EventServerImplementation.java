/*-
 * $Id: EventServerImplementation.java,v 1.23 2005/10/30 15:20:46 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import static java.util.logging.Level.INFO;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.syrus.AMFICOM.eventv2.Event;
import com.syrus.AMFICOM.eventv2.corba.IdlEvent;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlCompletionStatus;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlErrorCode;
import com.syrus.AMFICOM.leserver.corba.EventServerPOA;
import com.syrus.AMFICOM.leserver.corba.EventServerPackage.IdlEventProcessingException;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.23 $, $Date: 2005/10/30 15:20:46 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module leserver
 */
final class EventServerImplementation extends EventServerPOA {
	private static final long serialVersionUID = 3257569516216398643L;

	/**
	 * Map of event queues for every user
	 */
	private static final Map<Identifier, UserEventNotifier> USER_EVENT_NOTIFIERS_MAP =
		Collections.synchronizedMap(new HashMap<Identifier, UserEventNotifier>());

	EventServerImplementation() {
		EventProcessorRegistry.registerEventProcessor(new ReflectogramMismatchEventProcessor());
		EventProcessorRegistry.registerEventProcessor(new LineMismatchEventProcessor());
		EventProcessorRegistry.registerEventProcessor(new PopupNotificationEventProcessor());
		EventProcessorRegistry.registerEventProcessor(new EmailNotificationEventProcessor());
		EventProcessorRegistry.registerEventProcessor(new SmsNotificationEventProcessor());
	}

	/**
	 * @param idlEvent
	 * @throws AMFICOMRemoteException
	 * @see com.syrus.AMFICOM.leserver.corba.EventServerOperations#eventGeneration(com.syrus.AMFICOM.event.corba.IdlEvent)
	 */
	public void eventGeneration(
			final com.syrus.AMFICOM.event.corba.IdlEvent idlEvent)
	throws AMFICOMRemoteException {
		try {
			final com.syrus.AMFICOM.event.Event event = new com.syrus.AMFICOM.event.Event(idlEvent);
			final com.syrus.AMFICOM.event.EventType eventType = event.getType();
			for (final Identifier userId : eventType.getAlertedUserIds()) {
				UserEventNotifier userEventNotifier = USER_EVENT_NOTIFIERS_MAP.get(userId);
				if (userEventNotifier == null) {
					userEventNotifier = new UserEventNotifier(userId);
					userEventNotifier.start();
					USER_EVENT_NOTIFIERS_MAP.put(userId, userEventNotifier);
				}
				userEventNotifier.addEvent(event);
			}
		} catch (final CreateObjectException coe) {
			assert Log.errorMessage(coe);
			throw new AMFICOMRemoteException(
					IdlErrorCode.ERROR_ILLEGAL_DATA,
					IdlCompletionStatus.COMPLETED_NO,
					"Cannot create event -- " + coe.getMessage());
		}
	}

	/**
	 * @param idlEvents
	 * @throws IdlEventProcessingException
	 * @see com.syrus.AMFICOM.leserver.corba.EventServerOperations#receiveEvents(IdlEvent[])
	 */
	public void receiveEvents(final IdlEvent[] idlEvents) throws IdlEventProcessingException {
		assert Log.debugMessage("Received "
				+ idlEvents.length + " event(s)",
				INFO);
		for (final IdlEvent idlEvent : idlEvents) {
			@SuppressWarnings(value = {"unchecked"})
			final Event<? extends IdlEvent> event = idlEvent.getNativeEvent();
			try {
				EventProcessorRegistry.processEvent(event);
				assert Log.debugMessage("Event: " + event + " delivered successfully", INFO);
			} catch (EventProcessingException e) {
				assert Log.debugMessage(e, Log.DEBUGLEVEL07);
			}
		}
	}

	public void verify(byte i) {
		assert Log.debugMessage("Verify value: " + i, Log.DEBUGLEVEL10);
	}
}
