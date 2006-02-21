/*-
 * $Id: IdlMeasurementCompletedEventImpl.java,v 1.2 2006/02/21 10:50:32 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.eventv2.corba;

import com.syrus.AMFICOM.eventv2.DefaultMeasurementCompletedEvent;
import com.syrus.AMFICOM.eventv2.Event;
import com.syrus.AMFICOM.eventv2.corba.IdlEventPackage.IdlEventType;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;

/**
 * @version $Revision: 1.2 $, $Date: 2006/02/21 10:50:32 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module event
 */
final class IdlMeasurementCompletedEventImpl extends IdlMeasurementCompletedEvent {
	private static final long serialVersionUID = -8236403388373111017L;

	IdlMeasurementCompletedEventImpl() {
		//empty
	}

	IdlMeasurementCompletedEventImpl(final IdlIdentifier measurementId, final double quality) {
		super.measurementId = measurementId;
		super.quality = quality;
	}

	public IdlIdentifier getMeasurementId() {
		return super.measurementId;
	}

	@Override
	public double getQuality() {
		return super.quality;
	}

	public IdlEventType getType() {
		return IdlEventType.MEASUREMENT_STATUS_CHANGED;
	}

	@Override
	public StorableObject getNative() throws IdlCreateObjectException {
		throw new UnsupportedOperationException();
	}

	public Event getNativeEvent() {
		return DefaultMeasurementCompletedEvent.valueOf(this);
	}

}
