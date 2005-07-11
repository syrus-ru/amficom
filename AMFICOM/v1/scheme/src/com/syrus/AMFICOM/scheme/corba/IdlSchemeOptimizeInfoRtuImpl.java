/*-
 * $Id: IdlSchemeOptimizeInfoRtuImpl.java,v 1.2 2005/07/11 08:19:03 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import static java.util.logging.Level.SEVERE;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.scheme.SchemeOptimizeInfoRtu;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/07/11 08:19:03 $
 * @module scheme_v1
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
			Log.debugException(coe, SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}
}
