/*-
 * $Id: DefaultMeasurementStatusChangedEvent.java,v 1.1 2006/02/20 17:14:56 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.eventv2;

import com.syrus.AMFICOM.eventv2.corba.IdlMeasurementStatusChangedEvent;
import com.syrus.AMFICOM.general.Identifier;

/**
 * @version $Revision: 1.1 $, $Date: 2006/02/20 17:14:56 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module event
 */
public abstract class DefaultMeasurementStatusChangedEvent<T extends IdlMeasurementStatusChangedEvent> implements MeasurementStatusChangedEvent<T> {
	private Identifier measurementId;

	DefaultMeasurementStatusChangedEvent(final Identifier measurementId) {
		this.measurementId = measurementId;
	}

	DefaultMeasurementStatusChangedEvent(final IdlMeasurementStatusChangedEvent idlMeasurementStatusChangedEvent) {
		this.measurementId = Identifier.valueOf(idlMeasurementStatusChangedEvent.getMeasurementId());
	}

	public final Identifier getMeasurementId() {
		return this.measurementId;
	}

	public final EventType getType() {
		return EventType.MEASUREMENT_STATUS_CHANGED;
	}

}
