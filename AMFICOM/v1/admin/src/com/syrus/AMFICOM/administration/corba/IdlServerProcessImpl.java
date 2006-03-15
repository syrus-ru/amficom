/*-
 * $Id: IdlServerProcessImpl.java,v 1.3 2006/03/15 14:47:31 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration.corba;

import com.syrus.AMFICOM.administration.ServerProcess;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2006/03/15 14:47:31 $
 * @module administration
 */
final class IdlServerProcessImpl extends IdlServerProcess {
	private static final long serialVersionUID = -8125827321782506888L;

	IdlServerProcessImpl() {
		// empty
	}

	IdlServerProcessImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final String codename,
			final IdlIdentifier serverId,
			final IdlIdentifier userId,
			final String description) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.codename = codename;
		this.serverId = serverId;
		this.userId = userId;
		this.description = description;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public StorableObject getNative() throws IdlCreateObjectException {
		try {
			return new ServerProcess(this);
		} catch (final CreateObjectException coe) {
			throw coe.getIdlTransferable();
		}
	}
}
