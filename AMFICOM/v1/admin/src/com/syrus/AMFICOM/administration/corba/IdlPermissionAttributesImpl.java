/*-
 * $Id: IdlPermissionAttributesImpl.java,v 1.8 2005/10/30 15:20:23 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration.corba;

import java.util.logging.Level;

import com.syrus.AMFICOM.administration.PermissionAttributes;
import com.syrus.AMFICOM.administration.corba.IdlPermissionAttributesPackage.IdlModule;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.8 $, $Date: 2005/10/30 15:20:23 $
 * @module administration
 */
final class IdlPermissionAttributesImpl extends IdlPermissionAttributes {
	private static final long serialVersionUID = -825118372985236838L;

	IdlPermissionAttributesImpl() {
		// empty
	}

	IdlPermissionAttributesImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final IdlIdentifier domainId,
			final IdlIdentifier userId,
			final IdlModule _module,  
			final byte[] permissionMask,
			final byte[] denyMask) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.domainId = domainId;
		this.userId = userId;
		this._module = _module;
		this.permissionMask = permissionMask;
		this.denyMask = denyMask;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public PermissionAttributes getNative() throws IdlCreateObjectException {
		try {
			return new PermissionAttributes(this);
		} catch (final CreateObjectException coe) {
			assert Log.debugMessage(coe, Level.SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}
}
