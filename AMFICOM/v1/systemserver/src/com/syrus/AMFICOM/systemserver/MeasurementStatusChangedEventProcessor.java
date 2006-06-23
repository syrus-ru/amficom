/*-
 * $Id: MeasurementStatusChangedEventProcessor.java,v 1.1 2006/06/23 13:17:58 cvsadmin Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.systemserver;

import static com.syrus.AMFICOM.eventv2.EventType.MEASUREMENT_STATUS_CHANGED;
import static java.util.logging.Level.FINEST;

import com.syrus.AMFICOM.eventv2.Event;
import com.syrus.AMFICOM.eventv2.EventType;
import com.syrus.AMFICOM.eventv2.MeasurementStatusChangedEvent;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: cvsadmin $
 * @version $Revision: 1.1 $, $Date: 2006/06/23 13:17:58 $
 * @module systemserver
 */
final class MeasurementStatusChangedEventProcessor extends AbstractEventProcessor {
	MeasurementStatusChangedEventProcessor(final int capacity) {
		super(capacity);
	}

	MeasurementStatusChangedEventProcessor() {
		this(Integer.MAX_VALUE);
	}

	/**
	 * @see EventProcessor#getEventType()
	 */
	public EventType getEventType() {
		return MEASUREMENT_STATUS_CHANGED;
	}

	/**
	 * @param event
	 * @see EventProcessor#processEvent(Event)
	 */
	public void processEvent(final Event<?> event) {
		@SuppressWarnings("unchecked")
		final MeasurementStatusChangedEvent<?> measurementStatusChangedEvent = (MeasurementStatusChangedEvent) event;

		Log.debugMessage(measurementStatusChangedEvent
				+ " | Event started being processed",
				FINEST);

		this.deliverToClients(measurementStatusChangedEvent, LoginProcessor.getInstance().getUserLogins());
	}
}
