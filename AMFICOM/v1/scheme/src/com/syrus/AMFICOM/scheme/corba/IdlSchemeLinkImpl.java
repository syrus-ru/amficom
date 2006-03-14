/*-
 * $Id: IdlSchemeLinkImpl.java,v 1.8 2006/03/14 10:47:55 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import static java.util.logging.Level.SEVERE;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.8 $, $Date: 2006/03/14 10:47:55 $
 * @module scheme
 */
final class IdlSchemeLinkImpl extends IdlSchemeLink {
	private static final long serialVersionUID = 1793249423917530247L;

	IdlSchemeLinkImpl() {
		// empty
	}

	IdlSchemeLinkImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final String name,
			final String description,
			final double physicalLength,
			final double opticalLength,
			final IdlIdentifier linkTypeId,
			final IdlIdentifier linkId,
			final IdlIdentifier siteNodeId,
			final IdlIdentifier sourceSchemePortId,
			final IdlIdentifier targetSchemePortId,
			final IdlIdentifier parentSchemeId,
			final IdlIdentifier parentSchemeElementId,
			final IdlIdentifier parentSchemeProtoElementId) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.name = name;
		this.description = description;
		this.physicalLength = physicalLength;
		this.opticalLength = opticalLength;
		this.linkTypeId = linkTypeId;
		this.linkId = linkId;
		this.siteNodeId = siteNodeId;
		this.sourceSchemePortId = sourceSchemePortId;
		this.targetSchemePortId = targetSchemePortId;
		this.parentSchemeId = parentSchemeId;
		this.parentSchemeElementId = parentSchemeElementId;
		this.parentSchemeProtoElementId = parentSchemeProtoElementId;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public SchemeLink getNative() throws IdlCreateObjectException {
		try {
			return new SchemeLink(this);
		} catch (final CreateObjectException coe) {
			Log.debugMessage(coe, SEVERE);
			throw coe.getIdlTransferable();
		}
	}
}
