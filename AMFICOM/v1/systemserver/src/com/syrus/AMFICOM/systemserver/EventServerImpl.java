/*-
 * $Id: EventServerImpl.java,v 1.1 2006/06/23 13:57:47 cvsadmin Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.systemserver;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

import com.syrus.AMFICOM.eventv2.Event;
import com.syrus.AMFICOM.eventv2.corba.IdlEvent;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.systemserver.corba.EventServerOperations;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2006/06/23 13:57:47 $
 * @author $Author: cvsadmin $
 * @author Tashoyan Arseniy Feliksovich
 * @module systemserver
 */
final class EventServerImpl implements EventServerOperations {

	EventServerImpl() {
		EventProcessorRegistry.registerEventProcessor(new ReflectogramMismatchEventProcessor());
		EventProcessorRegistry.registerEventProcessor(new LineMismatchEventProcessor());
		EventProcessorRegistry.registerEventProcessor(new PopupNotificationEventProcessor());
		EventProcessorRegistry.registerEventProcessor(new EmailNotificationEventProcessor());
		EventProcessorRegistry.registerEventProcessor(new SmsNotificationEventProcessor());
		EventProcessorRegistry.registerEventProcessor(new MeasurementStatusChangedEventProcessor());
		EventProcessorRegistry.startProcessors();
	}

	/**
	 * @param idlEvents
	 */
	public void receiveEvents(final IdlEvent[] idlEvents) {
		Log.debugMessage("Received " + idlEvents.length + " event(s)",
				INFO);
		for (final IdlEvent idlEvent : idlEvents) {
			try {
				final Event<?> event = idlEvent.getNativeEvent();
				EventProcessorRegistry.processEvent(event);
				Log.debugMessage("Event: " + event + " delivered successfully",
						INFO);
			} catch (final IdlCreateObjectException coe) {
				Log.debugMessage(coe.detailMessage, WARNING);
				Log.debugMessage(coe, WARNING);
			} catch (final Throwable t) {
				Log.debugMessage(t, SEVERE);
			}
		}
	}

	public void verify(final byte i) {
		Log.debugMessage("Verify value: " + i, Log.DEBUGLEVEL10);
	}
}
