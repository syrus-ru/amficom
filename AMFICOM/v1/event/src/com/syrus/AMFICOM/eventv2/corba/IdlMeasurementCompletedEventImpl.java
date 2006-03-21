/*-
 * $Id: IdlMeasurementCompletedEventImpl.java,v 1.2.4.2 2006/03/21 08:38:35 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.eventv2.corba;

import static com.syrus.AMFICOM.eventv2.corba.IdlMeasurementCompletedEventPackage.QClausePackage.QPresence.YES;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;

import com.syrus.AMFICOM.eventv2.DefaultMeasurementCompletedEvent;
import com.syrus.AMFICOM.eventv2.MeasurementCompletedEvent;
import com.syrus.AMFICOM.eventv2.corba.IdlEventPackage.IdlEventType;
import com.syrus.AMFICOM.eventv2.corba.IdlMeasurementCompletedEventPackage.QClause;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;

/**
 * @version $Revision: 1.2.4.2 $, $Date: 2006/03/21 08:38:35 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module event
 */
final class IdlMeasurementCompletedEventImpl extends IdlMeasurementCompletedEvent {
	private static final long serialVersionUID = -6932986352026091050L;

	IdlMeasurementCompletedEventImpl() {
		//empty
	}

	IdlMeasurementCompletedEventImpl(final long created, final IdlIdentifier measurementId, final QClause qClause) {
		final IdlIdentifier voidId = VOID_IDENTIFIER.getIdlTransferable();

		super.id = voidId;
		super.created = created;
		super.creatorId = voidId;
		super.modifierId = voidId;

		super.measurementId = measurementId;
		super.qClause = qClause;
	}

	public long getCreated() {
		return super.created;
	}

	public IdlIdentifier getMeasurementId() {
		return super.measurementId;
	}

	@Override
	public boolean hasQ() {
		return super.qClause.discriminator() == YES;
	}

	@Override
	public double getQ() {
		if (this.hasQ()) {
			return super.qClause.quality();
		}
		throw new IllegalStateException("Has not quality");
	}

	public IdlEventType getType() {
		return IdlEventType.MEASUREMENT_STATUS_CHANGED;
	}

	@Override
	public StorableObject getNative() throws IdlCreateObjectException {
		throw new UnsupportedOperationException();
	}

	public MeasurementCompletedEvent getNativeEvent() throws IdlCreateObjectException {
		try {
			return DefaultMeasurementCompletedEvent.valueOf(this);
		} catch (final CreateObjectException coe) {
			throw coe.getIdlTransferable();
		}
	}
}
