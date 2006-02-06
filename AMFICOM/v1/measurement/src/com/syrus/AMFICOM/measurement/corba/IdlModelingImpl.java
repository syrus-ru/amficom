/*-
 * $Id: IdlModelingImpl.java,v 1.7 2005/10/31 12:30:15 bass Exp $
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
 * @author $Author: bass $
 * @version $Revision: 1.7 $, $Date: 2005/10/31 12:30:15 $
 * @module measurement
 */
final class IdlModelingImpl extends IdlModeling {
	private static final long serialVersionUID = 3337212684669042941L;

	IdlModelingImpl() {
		// empty
	}

	IdlModelingImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final IdlModelingType type,
			final IdlIdentifier monitoredElementId,
			final String name,
			final IdlIdentifier argumentSetId) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.type = type;
		this.monitoredElementId = monitoredElementId;
		this.name = name;
		this.argumentSetId = argumentSetId;
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
