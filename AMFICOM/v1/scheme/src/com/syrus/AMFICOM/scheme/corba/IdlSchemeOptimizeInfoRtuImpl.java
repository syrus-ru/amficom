/*-
 * $Id: IdlSchemeOptimizeInfoRtuImpl.java,v 1.7 2006/03/15 16:42:48 bass Exp $
 *
 * Copyright ? 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.scheme.SchemeOptimizeInfoRtu;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.7 $, $Date: 2006/03/15 16:42:48 $
 * @module scheme
 */
final class IdlSchemeOptimizeInfoRtuImpl extends IdlSchemeOptimizeInfoRtu {
	private static final long serialVersionUID = 3258469617558290849L;

	IdlSchemeOptimizeInfoRtuImpl() {
		// empty
	}

	IdlSchemeOptimizeInfoRtuImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final String name,
			final int priceUsd,
			final float rangeDb,
			final IdlIdentifier parentSchemeOptimizeInfoId) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.name = name;
		this.priceUsd = priceUsd;
		this.rangeDb = rangeDb;
		this.parentSchemeOptimizeInfoId = parentSchemeOptimizeInfoId;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public SchemeOptimizeInfoRtu getNative() throws IdlCreateObjectException {
		try {
			return new SchemeOptimizeInfoRtu(this);
		} catch (final CreateObjectException coe) {
			throw coe.getIdlTransferable();
		}
	}
}
