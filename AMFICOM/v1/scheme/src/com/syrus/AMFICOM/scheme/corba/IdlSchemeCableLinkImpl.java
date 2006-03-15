/*-
 * $Id: IdlSchemeCableLinkImpl.java,v 1.7.2.1 2006/03/15 15:47:49 arseniy Exp $
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
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.7.2.1 $, $Date: 2006/03/15 15:47:49 $
 * @module scheme
 */
final class IdlSchemeCableLinkImpl extends IdlSchemeCableLink {
	private static final long serialVersionUID = 206667335983795396L;

	IdlSchemeCableLinkImpl() {
		// empty
	}

	IdlSchemeCableLinkImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final String name,
			final String description,
			final double physicalLength,
			final double opticalLength,
			final IdlIdentifier cableLinkTypeId,
			final IdlIdentifier cableLinkId,
			final IdlIdentifier sourceSchemeCablePortId,
			final IdlIdentifier targetSchemeCablePortId,
			final IdlIdentifier parentSchemeId) {
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
		this.cableLinkTypeId = cableLinkTypeId;
		this.cableLinkId = cableLinkId;
		this.sourceSchemeCablePortId = sourceSchemeCablePortId;
		this.targetSchemeCablePortId = targetSchemeCablePortId;
		this.parentSchemeId = parentSchemeId;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public SchemeCableLink getNative() throws IdlCreateObjectException {
		try {
			return new SchemeCableLink(this);
		} catch (final CreateObjectException coe) {
			Log.debugMessage(coe, SEVERE);
			throw coe.getIdlTransferable();
		}
	}
}
