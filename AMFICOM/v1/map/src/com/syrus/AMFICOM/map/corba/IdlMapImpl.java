/*-
 * $Id: IdlMapImpl.java,v 1.6 2005/10/30 15:20:37 bass Exp $
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
import com.syrus.AMFICOM.map.Map;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2005/10/30 15:20:37 $
 * @module map
 */
final class IdlMapImpl extends IdlMap {
	private static final long serialVersionUID = -506084736755725640L;

	IdlMapImpl() {
		// empty
	}

	IdlMapImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final IdlIdentifier domainId,
			final String name,
			final String description,
			final IdlIdentifier siteNodeIds[],
			final IdlIdentifier topologicalNodeIds[],
			final IdlIdentifier nodeLinkIds[],
			final IdlIdentifier physicalLinkIds[],
			final IdlIdentifier markIds[],
			final IdlIdentifier collectorIds[],
			final IdlIdentifier mapIds[],
			final IdlIdentifier externalNodeIds[],
			final IdlIdentifier mapLibraryIds[]) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.domainId = domainId;
		this.name = name;
		this.description = description;
		this.siteNodeIds = siteNodeIds;
		this.topologicalNodeIds = topologicalNodeIds;
		this.nodeLinkIds = nodeLinkIds;
		this.physicalLinkIds = physicalLinkIds;
		this.markIds = markIds;
		this.collectorIds = collectorIds;
		this.mapIds = mapIds;
		this.externalNodeIds = externalNodeIds;
		this.mapLibraryIds = mapLibraryIds;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public Map getNative() throws IdlCreateObjectException {
		try {
			return new Map(this);
		} catch (final CreateObjectException coe) {
			assert Log.debugMessage(coe, SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}
}
