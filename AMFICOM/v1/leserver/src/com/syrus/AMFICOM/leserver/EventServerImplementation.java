/*-
 * $Id: EventServerImplementation.java,v 1.31 2006/04/04 11:32:56 bass Exp $
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
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.leserver.corba.EventServerPOA;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.31 $, $Date: 2006/04/04 11:32:56 $
 * @author $Author: bass $
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
		EventProcessorRegistry.registerEventProcessor(new MeasurementStatusChangedEventProcessor());
		EventProcessorRegistry.startProcessors();
	}

	/**
	 * @param idlEvents
	 * @see com.syrus.AMFICOM.leserver.corba.EventServerOperations#receiveEvents(IdlEvent[])
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
