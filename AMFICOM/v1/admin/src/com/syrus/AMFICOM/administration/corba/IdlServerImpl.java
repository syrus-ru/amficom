/*-
 * $Id: IdlServerImpl.java,v 1.8.4.1 2006/06/09 16:00:14 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration.corba;

import java.util.logging.Level;

import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.8.4.1 $, $Date: 2006/06/09 16:00:14 $
 * @module administration
 */
final class IdlServerImpl extends IdlServer {
	private static final long serialVersionUID = 935943680623660434L;

	IdlServerImpl() {
		// empty
	}

	IdlServerImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final IdlIdentifier domainId,
			final String name,
			final String description,
			final String hostname,
			final IdlIdentifier systemUserId) {
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
		this.systemUserId = systemUserId;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public Server getNative() throws IdlCreateObjectException {
		try {
			return new Server(this);
		} catch (final CreateObjectException coe) {
			Log.debugMessage(coe, Level.SEVERE);
			throw coe.getIdlTransferable();
		}
	}
}
