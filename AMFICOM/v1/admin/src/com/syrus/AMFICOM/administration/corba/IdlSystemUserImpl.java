/*-
 * $Id: IdlSystemUserImpl.java,v 1.10 2006/04/10 16:56:18 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration.corba;

import java.util.logging.Level;

import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.administration.corba.IdlSystemUserPackage.SystemUserSort;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.10 $, $Date: 2006/04/10 16:56:18 $
 * @module administration
 */
final class IdlSystemUserImpl extends IdlSystemUser {
	private static final long serialVersionUID = -8576064038640118164L;

	IdlSystemUserImpl() {
		// empty
	}

	IdlSystemUserImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final String login,
			final SystemUserSort sort,
			final String name,
			final String description) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.login = login;
		this.sort = sort;
		this.name = name;
		this.description = description;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public SystemUser getNative() throws IdlCreateObjectException {
		try {
			return new SystemUser(this);
		} catch (final CreateObjectException coe) {
			Log.debugMessage(coe, Level.SEVERE);
			throw coe.getIdlTransferable();
		}
	}
}
