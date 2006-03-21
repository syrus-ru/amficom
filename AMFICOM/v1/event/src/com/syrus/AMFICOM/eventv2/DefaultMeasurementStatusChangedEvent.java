/*-
 * $Id: DefaultMeasurementStatusChangedEvent.java,v 1.1.4.1.2.1 2006/03/21 13:46:43 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.eventv2;

import java.util.Date;

import com.syrus.AMFICOM.eventv2.corba.IdlMeasurementStatusChangedEvent;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.util.transport.idl.IdlConversionException;

/**
 * @version $Revision: 1.1.4.1.2.1 $, $Date: 2006/03/21 13:46:43 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module event
 */
public abstract class DefaultMeasurementStatusChangedEvent<T extends IdlMeasurementStatusChangedEvent> implements MeasurementStatusChangedEvent<T> {
	private Date created;
	private Identifier measurementId;

	DefaultMeasurementStatusChangedEvent(final Identifier measurementId) {
		this.created = new Date();
		this.measurementId = measurementId;
	}

	DefaultMeasurementStatusChangedEvent(/*IdlMeasurementStatusChangedEvent*/) {
		// super();
	}

	public final Date getCreated() {
		return this.created;
	}

	public final Identifier getMeasurementId() {
		return this.measurementId;
	}

	public final EventType getType() {
		return EventType.MEASUREMENT_STATUS_CHANGED;
	}

	/**
	 * @param measurementStatusChangedEvent
	 * @throws IdlConversionException
	 * @see com.syrus.util.transport.idl.IdlTransferableObjectExt#fromIdlTransferable(org.omg.CORBA.portable.IDLEntity)
	 */
	public void fromIdlTransferable(
			final T measurementStatusChangedEvent)
	throws IdlConversionException {
		synchronized (this) {
			this.created = new Date(measurementStatusChangedEvent.getCreated());
			this.measurementId = Identifier.valueOf(measurementStatusChangedEvent.getMeasurementId());
		}
	}
}
