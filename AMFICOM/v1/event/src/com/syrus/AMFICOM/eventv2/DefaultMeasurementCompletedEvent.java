/*-
 * $Id: DefaultMeasurementCompletedEvent.java,v 1.2 2006/02/21 10:50:32 arseniy Exp $
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
 * @version $Revision: 1.2 $, $Date: 2006/02/21 10:50:32 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module event
 */
public final class DefaultMeasurementCompletedEvent extends DefaultMeasurementStatusChangedEvent<IdlMeasurementCompletedEvent>
		implements MeasurementCompletedEvent {
	private static final long serialVersionUID = 1375151172371206455L;

	private double quality;

	private DefaultMeasurementCompletedEvent(final Identifier measurementId, final double d) {
		super(measurementId);
		this.quality = d;
	}

	private DefaultMeasurementCompletedEvent(final IdlMeasurementCompletedEvent idlMeasurementCompletedEvent) {
		super(idlMeasurementCompletedEvent);
		this.quality = idlMeasurementCompletedEvent.getQuality();
	}
	
	public double getQuality() {
		return this.quality;
	}

	public IdlMeasurementCompletedEvent getIdlTransferable(final ORB orb) {
		return IdlMeasurementCompletedEventHelper.init(orb, super.getMeasurementId().getIdlTransferable(), this.quality);
	}

	public static MeasurementCompletedEvent valueOf(final Identifier measurementId, final double d) {
		return new DefaultMeasurementCompletedEvent(measurementId, d);
	}

	public static MeasurementCompletedEvent valueOf(final IdlMeasurementCompletedEvent idlMeasurementCompletedEvent) {
		return new DefaultMeasurementCompletedEvent(idlMeasurementCompletedEvent);
	}

}
