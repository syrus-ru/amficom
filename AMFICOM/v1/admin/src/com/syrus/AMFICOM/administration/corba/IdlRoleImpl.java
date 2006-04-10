/*-
 * $Id: IdlRoleImpl.java,v 1.6 2006/04/10 16:56:18 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration.corba;

import java.util.logging.Level;

import com.syrus.AMFICOM.administration.Role;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.6 $, $Date: 2006/04/10 16:56:18 $
 * @module administration
 */
final class IdlRoleImpl extends IdlRole {
	private static final long serialVersionUID = -851165842201552661L;

	IdlRoleImpl() {
		// empty
	}

	IdlRoleImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final String codename,
			final String description,
			final IdlIdentifier[] systemUserIds) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.codename = codename;
		this.description = description;
		this.systemUserIds = systemUserIds;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public Role getNative() throws IdlCreateObjectException {
		try {
			return new Role(this);
		} catch (final CreateObjectException coe) {
			Log.debugMessage(coe, Level.SEVERE);
			throw coe.getIdlTransferable();
		}
	}
}
