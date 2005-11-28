/*-
 * $Id: EventServerImplementation.java,v 1.26 2005/11/28 12:30:28 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

import com.syrus.AMFICOM.eventv2.Event;
import com.syrus.AMFICOM.eventv2.corba.IdlEvent;
import com.syrus.AMFICOM.leserver.corba.EventServerPOA;
import com.syrus.AMFICOM.leserver.corba.EventServerPackage.IdlEventProcessingException;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.26 $, $Date: 2005/11/28 12:30:28 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module leserver
 */
final class EventServerImplementation extends EventServerPOA {
	private static final long serialVersionUID = 3257569516216398643L;

	EventServerImplementation() {
		EventProcessorRegistry.registerEventProcessor(new ReflectogramMismatchEventProcessor());
		EventProcessorRegistry.registerEventProcessor(new LineMismatchEventProcessor());
		EventProcessorRegistry.registerEventProcessor(new PopupNotificationEventProcessor());
		EventProcessorRegistry.registerEventProcessor(new EmailNotificationEventProcessor());
		EventProcessorRegistry.registerEventProcessor(new SmsNotificationEventProcessor());
	}

	/**
	 * @param idlEvents
	 * @throws IdlEventProcessingException
	 * @see com.syrus.AMFICOM.leserver.corba.EventServerOperations#receiveEvents(IdlEvent[])
	 */
	public void receiveEvents(final IdlEvent[] idlEvents) throws IdlEventProcessingException {
		Log.debugMessage("Received " + idlEvents.length + " event(s)",
				INFO);
		for (final IdlEvent idlEvent : idlEvents) {
			@SuppressWarnings(value = {"unchecked"})
			final Event<? extends IdlEvent> event = idlEvent.getNativeEvent();
			try {
				EventProcessorRegistry.processEvent(event);
				Log.debugMessage("Event: " + event + " delivered successfully",
						INFO);
			} catch (final EventProcessingException epe) {
				Log.debugMessage(epe, WARNING);
			} catch (final Throwable t) {
				Log.debugMessage(t, SEVERE);
			}
		}
	}

	public void verify(final byte i) {
		Log.debugMessage("Verify value: " + i, Log.DEBUGLEVEL10);
	}
}
