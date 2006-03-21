/*-
 * $Id: DefaultMeasurementCompletedEvent.java,v 1.2.4.1.2.1 2006/03/21 13:46:43 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.eventv2;

import static com.syrus.AMFICOM.eventv2.corba.IdlMeasurementCompletedEventPackage.QClausePackage.QPresence.NO;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.eventv2.corba.IdlMeasurementCompletedEvent;
import com.syrus.AMFICOM.eventv2.corba.IdlMeasurementCompletedEventHelper;
import com.syrus.AMFICOM.eventv2.corba.IdlMeasurementCompletedEventPackage.QClause;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.util.transport.idl.IdlConversionException;

/**
 * @version $Revision: 1.2.4.1.2.1 $, $Date: 2006/03/21 13:46:43 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module event
 */
public final class DefaultMeasurementCompletedEvent extends DefaultMeasurementStatusChangedEvent<IdlMeasurementCompletedEvent>
		implements MeasurementCompletedEvent {
	private static final long serialVersionUID = -1535111953086544661L;

	private boolean hasQuality;
	private double quality;

	private DefaultMeasurementCompletedEvent(final Identifier measurementId) {
		super(measurementId);
		this.hasQuality = false;
	}

	private DefaultMeasurementCompletedEvent(final Identifier measurementId, final double d) {
		super(measurementId);
		this.hasQuality = true;
		this.quality = d;
	}

	private DefaultMeasurementCompletedEvent(
			final IdlMeasurementCompletedEvent measurementCompletedEvent)
	throws CreateObjectException {
		try {
			this.fromIdlTransferable(measurementCompletedEvent);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
	}

	public boolean hasQ() {
		return this.hasQuality;
	}
	
	public double getQ() {
		if (this.hasQuality) {
			return this.quality;
		}
		throw new IllegalStateException("Has not quality");
	}

	public IdlMeasurementCompletedEvent getIdlTransferable(final ORB orb) {
		final QClause qClause = new QClause();
		if (this.hasQuality) {
			qClause.quality(this.quality);
		} else {
			qClause._default(NO);
		}
		return IdlMeasurementCompletedEventHelper.init(orb,
				super.getCreated().getTime(),
				super.getMeasurementId().getIdlTransferable(),
				qClause);
	}

	@Override
	public void fromIdlTransferable(
			final IdlMeasurementCompletedEvent measurementCompletedEvent)
	throws IdlConversionException {
		synchronized (this) {
			super.fromIdlTransferable(measurementCompletedEvent);

			if (measurementCompletedEvent.hasQ()) {
				this.hasQuality = true;
				this.quality = measurementCompletedEvent.getQ();
			} else {
				this.hasQuality = false;
			}
		}
	}

	public static MeasurementCompletedEvent valueOf(final Identifier measurementId) {
		return new DefaultMeasurementCompletedEvent(measurementId);
	}

	public static MeasurementCompletedEvent valueOf(final Identifier measurementId, final double d) {
		return new DefaultMeasurementCompletedEvent(measurementId, d);
	}

	public static MeasurementCompletedEvent valueOf(
			final IdlMeasurementCompletedEvent measurementCompletedEvent)
	throws CreateObjectException {
		return new DefaultMeasurementCompletedEvent(measurementCompletedEvent);
	}
}
