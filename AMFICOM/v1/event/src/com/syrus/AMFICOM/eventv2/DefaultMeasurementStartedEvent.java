/*-
 * $Id: DefaultMeasurementStartedEvent.java,v 1.1.4.3 2006/03/27 11:21:40 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.eventv2;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.eventv2.corba.IdlMeasurementStartedEvent;
import com.syrus.AMFICOM.eventv2.corba.IdlMeasurementStartedEventHelper;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.util.transport.idl.IdlConversionException;

/**
 * @version $Revision: 1.1.4.3 $, $Date: 2006/03/27 11:21:40 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module event
 */
public final class DefaultMeasurementStartedEvent extends DefaultMeasurementStatusChangedEvent<IdlMeasurementStartedEvent> implements MeasurementStartedEvent {
	private DefaultMeasurementStartedEvent(final Identifier measurementId) {
		super(measurementId);
	}

	private DefaultMeasurementStartedEvent(
			final IdlMeasurementStartedEvent idlMeasurementStartedEvent)
	throws CreateObjectException {
		try {
			this.fromIdlTransferable(idlMeasurementStartedEvent);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
	}

	public IdlMeasurementStartedEvent getIdlTransferable(final ORB orb) {
		return IdlMeasurementStartedEventHelper.init(orb,
				super.getCreated().getTime(),
				super.getMeasurementId().getIdlTransferable());
	}

	public static MeasurementStartedEvent valueOf(final Identifier measurementId) {
		return new DefaultMeasurementStartedEvent(measurementId);
	}

	public static MeasurementStartedEvent valueOf(
			final IdlMeasurementStartedEvent measurementStartedEvent)
	throws CreateObjectException {
		return new DefaultMeasurementStartedEvent(measurementStartedEvent);
	}
}
