/*-
 * $Id: IdlMeasurementImpl.java,v 1.7.2.3 2006/02/14 01:26:42 arseniy Exp $
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
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.7.2.3 $, $Date: 2006/02/14 01:26:42 $
 * @module measurement
 */
final class IdlMeasurementImpl extends IdlMeasurement {
	private static final long serialVersionUID = 5424791828299142935L;

	IdlMeasurementImpl() {
		// empty
	}

	IdlMeasurementImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final IdlIdentifier _typeId,
			final IdlIdentifier monitoredElementId,
			final IdlIdentifier actionTemplateId,
			final String name,
			final long startTime,
			final long duration,
			final IdlActionStatus status,
			final IdlIdentifier testId) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this._typeId = _typeId;
		this.monitoredElementId = monitoredElementId;
		this.actionTemplateId = actionTemplateId;
		this.name = name;
		this.startTime = startTime;
		this.duration = duration;
		this.status = status;
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
