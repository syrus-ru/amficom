/*-
 * $Id: IdlDomainImpl.java,v 1.1 2005/07/05 15:23:17 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration.corba;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/07/05 15:23:17 $
 * @module admin_v1
 */
final class IdlDomainImpl extends IdlDomain {
	private static final long serialVersionUID = -825118372985236838L;

	IdlDomainImpl() {
		// empty
	}

	IdlDomainImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final IdlIdentifier domainId,
			final String name,
			final String description,
			final IdlIdentifier characteristicIds[]) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.domainId = domainId;
		this.name = name;
		this.description = description;
		this.characteristicIds = characteristicIds;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public Domain getNative() throws IdlCreateObjectException {
		try {
			return new Domain(this);
		} catch (final CreateObjectException coe) {
			Log.debugException(coe, Log.SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}
}
