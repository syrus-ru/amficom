/*-
 * $Id: IdlNodeLinkImpl.java,v 1.6 2005/10/30 14:49:02 bass Exp $
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
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2005/10/30 14:49:02 $
 * @module map
 */
final class IdlNodeLinkImpl extends IdlNodeLink {
	private static final long serialVersionUID = 2357773396341884237L;

	IdlNodeLinkImpl() {
		// empty
	}

	IdlNodeLinkImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final IdlIdentifier physicalLinkId,
			final IdlIdentifier startNodeId,
			final IdlIdentifier endNodeId,
			final double length) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.physicalLinkId = physicalLinkId;
		this.startNodeId = startNodeId;
		this.endNodeId = endNodeId;
		this.length = length;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public NodeLink getNative() throws IdlCreateObjectException {
		try {
			return new NodeLink(this);
		} catch (final CreateObjectException coe) {
			Log.debugMessage(coe, SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}
}
