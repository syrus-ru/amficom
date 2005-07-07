/*-
 * $Id: IdlAbstractSchemePortImpl.java,v 1.1 2005/07/07 15:52:10 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.scheme.AbstractSchemePort;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemePortPackage.DirectionType;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/07/07 15:52:10 $
 * @module scheme_v1
 */
final class IdlAbstractSchemePortImpl extends IdlAbstractSchemePort {
	private static final long serialVersionUID = -54357695778007687L;

	IdlAbstractSchemePortImpl() {
		// empty
	}

	IdlAbstractSchemePortImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final String name,
			final String description,
			final DirectionType directionType,
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
		this.measurementPortId = measurementPortId;
		this.parentSchemeDeviceId = parentSchemeDeviceId;
		this.characteristicIds = characteristicIds;
	}

	/**
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public AbstractSchemePort getNative() {
		throw new UnsupportedOperationException();
	}
}
