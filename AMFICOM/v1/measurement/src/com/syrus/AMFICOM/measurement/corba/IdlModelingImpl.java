/*-
 * $Id: IdlModelingImpl.java,v 1.7.2.1 2006/02/11 18:40:47 arseniy Exp $
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
import com.syrus.AMFICOM.measurement.Modeling;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.7.2.1 $, $Date: 2006/02/11 18:40:47 $
 * @module measurement
 */
final class IdlModelingImpl extends IdlModeling {

	IdlModelingImpl() {
		// empty
	}

	IdlModelingImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final IdlIdentifier actionTypeId,
			final IdlIdentifier monitoredElementId,
			final IdlIdentifier actionTemplateId,
			final String name,
			final long startTime,
			final long duration,
			final IdlActionStatus status) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.actionTypeId = actionTypeId;
		this.monitoredElementId = monitoredElementId;
		this.actionTemplateId = actionTemplateId;
		this.name = name;
		this.startTime = startTime;
		this.duration = duration;
		this.status = status;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public Modeling getNative() throws IdlCreateObjectException {
		try {
			return new Modeling(this);
		} catch (final CreateObjectException coe) {
			Log.debugMessage(coe, SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}
}
