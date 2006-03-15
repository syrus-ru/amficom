/*-
 * $Id: IdlSchemeMonitoringSolutionImpl.java,v 1.5 2006/03/15 16:42:48 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.scheme.SchemeMonitoringSolution;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2006/03/15 16:42:48 $
 * @module scheme
 */
final class IdlSchemeMonitoringSolutionImpl extends IdlSchemeMonitoringSolution {
	private static final long serialVersionUID = -2973281910873931261L;

	IdlSchemeMonitoringSolutionImpl() {
		// empty
	}

	IdlSchemeMonitoringSolutionImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final String name,
			final String description,
			final int priceUsd,
			final boolean active,
			final IdlIdentifier parentSchemeId,
			final IdlIdentifier parentSchemeOptimizeInfoId) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.name = name;
		this.description = description;
		this.priceUsd = priceUsd;
		this.active = active;
		this.parentSchemeId = parentSchemeId;
		this.parentSchemeOptimizeInfoId = parentSchemeOptimizeInfoId;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public SchemeMonitoringSolution getNative() throws IdlCreateObjectException {
		try {
			return new SchemeMonitoringSolution(this);
		} catch (final CreateObjectException coe) {
			throw coe.getIdlTransferable();
		}
	}
}
