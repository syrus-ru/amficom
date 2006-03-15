/*-
 * $Id: IdlSchemeOptimizeInfoSwitchImpl.java,v 1.4.2.1 2006/03/15 15:47:49 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.scheme.SchemeOptimizeInfoSwitch;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.4.2.1 $, $Date: 2006/03/15 15:47:49 $
 * @module scheme
 */
final class IdlSchemeOptimizeInfoSwitchImpl extends IdlSchemeOptimizeInfoSwitch {
	private static final long serialVersionUID = -2026585260915595730L;

	IdlSchemeOptimizeInfoSwitchImpl() {
		// empty
	}

	IdlSchemeOptimizeInfoSwitchImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final String name,
			final int priceUsd,
			final byte noOfPorts,
			final IdlIdentifier parentSchemeOptimizeInfoId) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.name = name;
		this.priceUsd = priceUsd;
		this.noOfPorts = noOfPorts;
		this.parentSchemeOptimizeInfoId = parentSchemeOptimizeInfoId;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public SchemeOptimizeInfoSwitch getNative() throws IdlCreateObjectException {
		try {
			return new SchemeOptimizeInfoSwitch(this);
		} catch (final CreateObjectException coe) {
			throw coe.getIdlTransferable();
		}
	}
}
