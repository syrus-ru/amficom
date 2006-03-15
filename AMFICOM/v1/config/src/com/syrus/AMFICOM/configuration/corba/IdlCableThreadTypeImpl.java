/*-
 * $Id: IdlCableThreadTypeImpl.java,v 1.6.2.1 2006/03/15 13:53:17 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration.corba;

import static java.util.logging.Level.SEVERE;

import com.syrus.AMFICOM.configuration.CableThreadType;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.6.2.1 $, $Date: 2006/03/15 13:53:17 $
 * @module config
 */
final class IdlCableThreadTypeImpl extends IdlCableThreadType {
	private static final long serialVersionUID = -2155822800686695545L;

	IdlCableThreadTypeImpl() {
		// empty
	}

	IdlCableThreadTypeImpl(final IdlIdentifier id,
			final long created, 
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final String codename,
			final String description,
			final String name,
			final int color,
			final IdlIdentifier linkTypeId,
			final IdlIdentifier cableLinkTypeId) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.codename = codename;
		this.description = description;
		this.name = name;
		this.color = color;
		this.linkTypeId = linkTypeId;
		this.cableLinkTypeId = cableLinkTypeId;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public CableThreadType getNative() throws IdlCreateObjectException {
		try {
			return new CableThreadType(this);
		} catch (final CreateObjectException coe) {
			Log.debugMessage(coe, SEVERE);
			throw coe.getIdlTransferable();
		}
	}
}
