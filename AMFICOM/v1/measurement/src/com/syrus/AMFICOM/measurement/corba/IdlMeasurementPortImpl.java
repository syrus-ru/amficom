/*-
 * $Id: IdlMeasurementPortImpl.java,v 1.4 2005/10/30 15:20:39 bass Exp $
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
import com.syrus.AMFICOM.measurement.MeasurementPort;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/10/30 15:20:39 $
 * @module measurement
 */
final class IdlMeasurementPortImpl extends IdlMeasurementPort {
	private static final long serialVersionUID = -8293952646193111078L;

	IdlMeasurementPortImpl() {
		// empty
	}

	IdlMeasurementPortImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final IdlIdentifier typeId,
			final String name,
			final String description,
			final IdlIdentifier kisId,
			final IdlIdentifier portId) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this._typeId = typeId;
		this.name = name;
		this.description = description;
		this.kisId = kisId;
		this.portId = portId;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public MeasurementPort getNative() throws IdlCreateObjectException {
		try {
			return new MeasurementPort(this);
		} catch (final CreateObjectException coe) {
			assert Log.debugMessage(coe, SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}
}
