/*-
 * $Id: IdlPhysicalLinkTypeImpl.java,v 1.9 2005/10/31 12:30:13 bass Exp $
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
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.corba.IdlPhysicalLinkTypePackage.PhysicalLinkTypeSort;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.9 $, $Date: 2005/10/31 12:30:13 $
 * @module map
 */
final class IdlPhysicalLinkTypeImpl extends IdlPhysicalLinkType {
	private static final long serialVersionUID = 3945944923809605842L;

	IdlPhysicalLinkTypeImpl() {
		// empty
	}

	IdlPhysicalLinkTypeImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final PhysicalLinkTypeSort sort,
			final String codename,
			final String name,
			final String description,
			final int dimensionX,
			final int dimensionY,
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
		this.dimensionX = dimensionX;
		this.dimensionY = dimensionY;
		this.topological = topological;
		this.mapLibraryId = mapLibraryId;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public PhysicalLinkType getNative() throws IdlCreateObjectException {
		try {
			return new PhysicalLinkType(this);
		} catch (final CreateObjectException coe) {
			Log.debugMessage(coe, SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}
}
