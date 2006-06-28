/*-
 * $Id: IdlMeasurementStartedEventImpl.java,v 1.2 2006/03/28 10:17:19 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.eventv2.corba;

import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;

import com.syrus.AMFICOM.eventv2.DefaultMeasurementStartedEvent;
import com.syrus.AMFICOM.eventv2.MeasurementStartedEvent;
import com.syrus.AMFICOM.eventv2.corba.IdlEventPackage.IdlEventType;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;

/**
 * @version $Revision: 1.2 $, $Date: 2006/03/28 10:17:19 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module event
 */
final class IdlMeasurementStartedEventImpl extends IdlMeasurementStartedEvent {
	private static final long serialVersionUID = -6259639676043479973L;

	IdlMeasurementStartedEventImpl() {
		//empty
	}

	IdlMeasurementStartedEventImpl(final long created, final IdlIdentifier measurementId) {
		final IdlIdentifier voidId = VOID_IDENTIFIER.getIdlTransferable();

		super.id = voidId;
		super.created = created;
		super.creatorId = voidId;
		super.modifierId = voidId;

		super.measurementId = measurementId;
	}

	public long getCreated() {
		return super.created;
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

	public MeasurementStartedEvent getNativeEvent() throws IdlCreateObjectException {
		try {
			return DefaultMeasurementStartedEvent.valueOf(this);
		} catch (final CreateObjectException coe) {
			throw coe.getIdlTransferable();
		}
	}
}
