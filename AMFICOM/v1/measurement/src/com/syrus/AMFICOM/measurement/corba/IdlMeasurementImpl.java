/*-
 * $Id: IdlMeasurementImpl.java,v 1.5 2005/10/30 14:49:05 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement.corba;

import static java.util.logging.Level.SEVERE;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementPackage.MeasurementStatus;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/10/30 14:49:05 $
 * @module measurement
 */
final class IdlMeasurementImpl extends IdlMeasurement {
	private static final long serialVersionUID = 4762899720517063763L;

	IdlMeasurementImpl() {
		// empty
	}

	IdlMeasurementImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final IdlMeasurementType type,
			final IdlIdentifier monitoredElementId,
			final String name,
			final IdlIdentifier setupId,
			final long startTime,
			final long duration,
			final MeasurementStatus status,
			final String localAddress,
			final IdlIdentifier testId) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.type = type;
		this.monitoredElementId = monitoredElementId;
		this.name = name;
		this.setupId = setupId;
		this.startTime = startTime;
		this.duration = duration;
		this.status = status;
		this.localAddress = localAddress;
		this.testId = testId;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public Measurement getNative() throws IdlCreateObjectException {
		try {
			return new Measurement(this);
		} catch (final CreateObjectException coe) {
			Log.debugMessage(coe, SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}
}
