/*-
 * $Id: IdlMCMImpl.java,v 1.4 2005/09/14 19:01:24 arseniy Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration.corba;

import java.util.logging.Level;

import com.syrus.AMFICOM.administration.MCM;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.4 $, $Date: 2005/09/14 19:01:24 $
 * @module administration
 */
final class IdlMCMImpl extends IdlMCM {
	private static final long serialVersionUID = 1482992962278587987L;

	IdlMCMImpl() {
		// empty
	}

	IdlMCMImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final IdlIdentifier domainId,
			final String name,
			final String description,
			final String hostname,
			final IdlIdentifier userId,
			final IdlIdentifier serverId) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.domainId = domainId;
		this.name = name;
		this.description = description;
		this.hostname = hostname;
		this.userId = userId;
		this.serverId = serverId;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public MCM getNative() throws IdlCreateObjectException {
		try {
			return new MCM(this);
		} catch (final CreateObjectException coe) {
			Log.debugException(coe, Level.SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}
}
