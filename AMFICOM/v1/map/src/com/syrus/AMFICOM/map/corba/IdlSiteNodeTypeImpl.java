/*-
 * $Id: IdlSiteNodeTypeImpl.java,v 1.7 2005/10/30 14:49:02 bass Exp $
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
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.corba.IdlSiteNodeTypePackage.SiteNodeTypeSort;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.7 $, $Date: 2005/10/30 14:49:02 $
 * @module map
 */
final class IdlSiteNodeTypeImpl extends IdlSiteNodeType {
	private static final long serialVersionUID = 5779813368986403850L;

	IdlSiteNodeTypeImpl() {
		// empty
	}

	IdlSiteNodeTypeImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final SiteNodeTypeSort sort,
			final String codename,
			final String name,
			final String description,
			final IdlIdentifier imageId,
			final boolean topological,
			final IdlIdentifier mapLibraryId) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.sort = sort;
		this.codename = codename;
		this.name = name;
		this.description = description;
		this.imageId = imageId;
		this.topological = topological;
		this.mapLibraryId = mapLibraryId;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public SiteNodeType getNative() throws IdlCreateObjectException {
		try {
			return new SiteNodeType(this);
		} catch (final CreateObjectException coe) {
			Log.debugMessage(coe, SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}
}
