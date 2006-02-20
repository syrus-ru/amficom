/*-
 * $Id: IdlMeasurementStartedEventImpl.java,v 1.1 2006/02/20 17:14:56 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.eventv2.corba;

import com.syrus.AMFICOM.eventv2.DefaultMeasurementStartedEvent;
import com.syrus.AMFICOM.eventv2.MeasurementStartedEvent;
import com.syrus.AMFICOM.eventv2.corba.IdlEventPackage.IdlEventType;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;

/**
 * @version $Revision: 1.1 $, $Date: 2006/02/20 17:14:56 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module event
 */
final class IdlMeasurementStartedEventImpl extends IdlMeasurementStartedEvent {
	private static final long serialVersionUID = 474945152832104872L;

	IdlMeasurementStartedEventImpl() {
		//empty
	}

	IdlMeasurementStartedEventImpl(final IdlIdentifier measurementId) {
		super.measurementId = measurementId;
	}

	public IdlIdentifier getMeasurementId() {
		return super.measurementId;
	}

	public IdlEventType getType() {
		return IdlEventType.MEASUREMENT_STATUS_CHANGED;
	}

	@Override
	public StorableObject getNative() throws IdlCreateObjectException {
		throw new UnsupportedOperationException();
	}

	public MeasurementStartedEvent getNativeEvent() {
		return DefaultMeasurementStartedEvent.valueOf(this);
	}

}
