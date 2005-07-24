/*-
 * $Id: IdlSchemeCablePortImpl.java,v 1.5 2005/07/24 17:40:35 bass Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import static java.util.logging.Level.SEVERE;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.scheme.SchemeCablePort;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemePortPackage.IdlDirectionType;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/07/24 17:40:35 $
 * @module scheme
 */
final class IdlSchemeCablePortImpl extends IdlSchemeCablePort {
	private static final long serialVersionUID = 4363812351650218787L;

	IdlSchemeCablePortImpl() {
		// empty
	}

	IdlSchemeCablePortImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final String name,
			final String description,
			final IdlDirectionType directionType,
			final IdlIdentifier cablePortTypeId,
			final IdlIdentifier cablePortId,
			final IdlIdentifier measurementPortId,
			final IdlIdentifier parentSchemeDeviceId) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.name = name;
		this.description = description;
		this.directionType = directionType;
		this.cablePortTypeId = cablePortTypeId;
		this.cablePortId = cablePortId;
		this.measurementPortId = measurementPortId;
		this.parentSchemeDeviceId = parentSchemeDeviceId;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public SchemeCablePort getNative() throws IdlCreateObjectException {
		try {
			return new SchemeCablePort(this);
		} catch (final CreateObjectException coe) {
			Log.debugException(coe, SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}
}
