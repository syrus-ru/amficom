/*-
 * $Id: IdlMarkImpl.java,v 1.3 2005/07/17 05:20:44 arseniy Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map.corba;

import static java.util.logging.Level.SEVERE;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.3 $, $Date: 2005/07/17 05:20:44 $
 * @module map_v1
 */
final class IdlMarkImpl extends IdlMark {
	private static final long serialVersionUID = -3957013612993745890L;

	IdlMarkImpl() {
		// empty
	}

	IdlMarkImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final String name,
			final String description,
			final double longitude,
			final double latitude,
			final IdlIdentifier physicalLinkId,
			final double distance,
			final String city,
			final String street,
			final String building) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.name = name;
		this.description = description;
		this.longitude = longitude;
		this.latitude = latitude;
		this.physicalLinkId = physicalLinkId;
		this.distance = distance;
		this.city = city;
		this.street = street;
		this.building = building;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public Mark getNative() throws IdlCreateObjectException {
		try {
			return new Mark(this);
		} catch (final CreateObjectException coe) {
			Log.debugException(coe, SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}
}
