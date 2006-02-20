/*-
 * $Id: DefaultMeasurementStartedEvent.java,v 1.1 2006/02/20 17:14:56 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.eventv2;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.eventv2.corba.IdlMeasurementStartedEvent;
import com.syrus.AMFICOM.eventv2.corba.IdlMeasurementStartedEventHelper;
import com.syrus.AMFICOM.general.Identifier;

/**
 * @version $Revision: 1.1 $, $Date: 2006/02/20 17:14:56 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module event
 */
public final class DefaultMeasurementStartedEvent extends DefaultMeasurementStatusChangedEvent<IdlMeasurementStartedEvent> implements MeasurementStartedEvent {
	private static final long serialVersionUID = -2290640950287598171L;

	private DefaultMeasurementStartedEvent(final Identifier measurementId) {
		super(measurementId);
	}

	private DefaultMeasurementStartedEvent(final IdlMeasurementStartedEvent idlMeasurementStartedEvent) {
		super(idlMeasurementStartedEvent);
	}

	public IdlMeasurementStartedEvent getIdlTransferable(final ORB orb) {
		return IdlMeasurementStartedEventHelper.init(orb, super.getMeasurementId().getIdlTransferable());
	}

	public static MeasurementStartedEvent valueOf(final Identifier measurementId) {
		return new DefaultMeasurementStartedEvent(measurementId);
	}

	public static MeasurementStartedEvent valueOf(final IdlMeasurementStartedEvent idlMeasurementStartedEvent) {
		return new DefaultMeasurementStartedEvent(idlMeasurementStartedEvent);
	}
}
