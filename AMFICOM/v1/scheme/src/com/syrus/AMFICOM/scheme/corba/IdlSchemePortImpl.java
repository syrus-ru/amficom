/*-
 * $Id: IdlSchemePortImpl.java,v 1.1 2005/07/07 15:52:11 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import static com.syrus.util.Log.SEVERE;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.scheme.SchemePort;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemePortPackage.DirectionType;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/07/07 15:52:11 $
 * @module scheme_v1
 */
final class IdlSchemePortImpl extends IdlSchemePort {
	private static final long serialVersionUID = 7729431000706846198L;

	IdlSchemePortImpl() {
		// empty
	}

	IdlSchemePortImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final String name,
			final String description,
			final DirectionType directionType,
			final IdlIdentifier portTypeId,
			final IdlIdentifier portId,
			final IdlIdentifier measurementPortId,
			final IdlIdentifier parentSchemeDeviceId,
			final IdlIdentifier characteristicIds[]) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.name = name;
		this.description = description;
		this.directionType = directionType;
		this.portTypeId = portTypeId;
		this.portId = portId;
		this.measurementPortId = measurementPortId;
		this.parentSchemeDeviceId = parentSchemeDeviceId;
		this.characteristicIds = characteristicIds;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public SchemePort getNative() throws IdlCreateObjectException {
		try {
			return new SchemePort(this);
		} catch (final CreateObjectException coe) {
			Log.debugException(coe, SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}
}
