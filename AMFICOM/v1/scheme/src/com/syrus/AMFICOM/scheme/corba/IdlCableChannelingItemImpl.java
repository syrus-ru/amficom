/*-
 * $Id: IdlCableChannelingItemImpl.java,v 1.3 2005/10/16 18:18:24 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.scheme.CableChannelingItem;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/10/16 18:18:24 $
 * @module scheme
 */
final class IdlCableChannelingItemImpl extends IdlCableChannelingItem {
	private static final long serialVersionUID = 1934926866900901617L;

	IdlCableChannelingItemImpl() {
		// empty
	}

	IdlCableChannelingItemImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final double startSpare,
			final double endSpare,
			final int rowX,
			final int placeY,
			final int sequentialNumber,
			final IdlIdentifier physicalLinkId,
			final IdlIdentifier pipeBlockId,
			final IdlIdentifier startSiteNodeId,
			final IdlIdentifier endSiteNodeId,
			final IdlIdentifier parentSchemeCableLinkId) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.startSpare = startSpare;
		this.endSpare = endSpare;
		this.rowX = rowX;
		this.placeY = placeY;
		this.sequentialNumber = sequentialNumber;
		this.physicalLinkId = physicalLinkId;
		this.pipeBlockId = pipeBlockId;
		this.startSiteNodeId = startSiteNodeId;
		this.endSiteNodeId = endSiteNodeId;
		this.parentSchemeCableLinkId = parentSchemeCableLinkId;
	}

	/**
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public CableChannelingItem getNative() {
		return new CableChannelingItem(this);
	}
}
