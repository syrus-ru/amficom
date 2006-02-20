/*-
 * $Id: DefaultMeasurementCompletedEvent.java,v 1.1 2006/02/20 17:14:56 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.eventv2;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.eventv2.corba.IdlMeasurementCompletedEvent;
import com.syrus.AMFICOM.eventv2.corba.IdlMeasurementCompletedEventHelper;
import com.syrus.AMFICOM.general.Identifier;

/**
 * @version $Revision: 1.1 $, $Date: 2006/02/20 17:14:56 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module event
 */
public final class DefaultMeasurementCompletedEvent extends DefaultMeasurementStatusChangedEvent<IdlMeasurementCompletedEvent>
		implements MeasurementCompletedEvent {
	private static final long serialVersionUID = -5304598177296370524L;

	private double d;

	private DefaultMeasurementCompletedEvent(final Identifier measurementId, final double d) {
		super(measurementId);
		this.d = d;
	}

	private DefaultMeasurementCompletedEvent(final IdlMeasurementCompletedEvent idlMeasurementCompletedEvent) {
		super(idlMeasurementCompletedEvent);
		this.d = idlMeasurementCompletedEvent.getD();
	}
	
	public double getD() {
		return this.d;
	}

	public IdlMeasurementCompletedEvent getIdlTransferable(final ORB orb) {
		return IdlMeasurementCompletedEventHelper.init(orb, super.getMeasurementId().getIdlTransferable(), this.d);
	}

	public static MeasurementCompletedEvent valueOf(final Identifier measurementId, final double d) {
		return new DefaultMeasurementCompletedEvent(measurementId, d);
	}

	public static MeasurementCompletedEvent valueOf(final IdlMeasurementCompletedEvent idlMeasurementCompletedEvent) {
		return new DefaultMeasurementCompletedEvent(idlMeasurementCompletedEvent);
	}

}
