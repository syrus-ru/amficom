/*-
 * $Id: IdlSystemUserImpl.java,v 1.5 2005/10/10 15:48:25 bob Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
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
 * @author $Author: bob $
 * @version $Revision: 1.5 $, $Date: 2005/10/10 15:48:25 $
 * @module administration
 */
final class IdlSystemUserImpl extends IdlSystemUser {
	private static final long serialVersionUID = 1550106907023939289L;

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
			final String description,
			final IdlIdentifier[] roleIds) {
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
		this.roleIds = roleIds;
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
			Log.debugException(coe, Level.SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}
}
