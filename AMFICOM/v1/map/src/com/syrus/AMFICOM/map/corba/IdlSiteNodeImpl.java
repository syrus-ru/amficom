/*-
 * $Id: IdlSiteNodeImpl.java,v 1.9 2005/10/31 12:30:13 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map.corba;

import static java.util.logging.Level.SEVERE;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.9 $, $Date: 2005/10/31 12:30:13 $
 * @module map
 */
final class IdlSiteNodeImpl extends IdlSiteNode {
	private static final long serialVersionUID = -4061274070448563526L;

	IdlSiteNodeImpl() {
		// empty
	}

	IdlSiteNodeImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final String name,
			final String description,
			final double longitude,
			final double latitude,
			final IdlIdentifier imageId,
			final IdlIdentifier siteNodeTypeId,
			final String city,
			final String street,
			final String building,
			final IdlIdentifier attachmentSiteNodeId) {
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
		this.imageId = imageId;
		this.siteNodeTypeId = siteNodeTypeId;
		this.city = city;
		this.street = street;
		this.building = building;
		this.attachmentSiteNodeId = attachmentSiteNodeId;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public SiteNode getNative() throws IdlCreateObjectException {
		try {
			return new SiteNode(this);
		} catch (final CreateObjectException coe) {
			Log.debugMessage(coe, SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}
}
