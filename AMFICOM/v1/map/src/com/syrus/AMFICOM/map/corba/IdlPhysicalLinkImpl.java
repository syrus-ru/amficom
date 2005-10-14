/*-
 * $Id: IdlPhysicalLinkImpl.java,v 1.5 2005/10/14 11:57:19 krupenn Exp $
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
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: krupenn $
 * @version $Revision: 1.5 $, $Date: 2005/10/14 11:57:19 $
 * @module map
 */
final class IdlPhysicalLinkImpl extends IdlPhysicalLink {
	private static final long serialVersionUID = -3102011344317702001L;

	IdlPhysicalLinkImpl() {
		// empty
	}

	IdlPhysicalLinkImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final String name, 
			final String description,
			final IdlIdentifier physicalLinkTypeId,
			final IdlIdentifier startNodeId,
			final IdlIdentifier endNodeId,
			final String city,
			final String street,
			final String building,
			final IdlIdentifier nodeLinkIds[],
			final IdlIdentifier pipeBlockIds[]) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.name = name;
		this.description = description;
		this.physicalLinkTypeId = physicalLinkTypeId;
		this.startNodeId = startNodeId;
		this.endNodeId = endNodeId;
		this.city = city;
		this.street = street;
		this.building = building;
		this.nodeLinkIds = nodeLinkIds;
		this.pipeBlockIds = pipeBlockIds;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public PhysicalLink getNative() throws IdlCreateObjectException {
		try {
			return new PhysicalLink(this);
		} catch (final CreateObjectException coe) {
			Log.debugException(coe, SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}
}
