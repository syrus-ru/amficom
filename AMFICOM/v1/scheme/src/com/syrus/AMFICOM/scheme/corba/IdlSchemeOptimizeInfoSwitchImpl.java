/*-
 * $Id: IdlSchemeOptimizeInfoSwitchImpl.java,v 1.4 2005/08/02 18:28:42 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.scheme.SchemeOptimizeInfoSwitch;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/08/02 18:28:42 $
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
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public SchemeOptimizeInfoSwitch getNative() {
		return new SchemeOptimizeInfoSwitch(this);
	}
}
