/*-
 * $Id: IdlServerProcessImpl.java,v 1.2 2005/09/14 19:01:24 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration.corba;

import com.syrus.AMFICOM.administration.ServerProcess;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.2 $, $Date: 2005/09/14 19:01:24 $
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
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public StorableObject getNative() {
		return new ServerProcess(this);
	}
}
