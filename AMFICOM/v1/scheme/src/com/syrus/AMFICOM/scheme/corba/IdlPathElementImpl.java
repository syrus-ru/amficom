/*-
 * $Id: IdlPathElementImpl.java,v 1.3.2.1 2006/03/15 15:47:49 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.IdlData;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.3.2.1 $, $Date: 2006/03/15 15:47:49 $
 * @module scheme
 */
final class IdlPathElementImpl extends IdlPathElement {
	private static final long serialVersionUID = -7371044684847182096L;

	IdlPathElementImpl() {
		// empty
	}

	IdlPathElementImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final IdlIdentifier parentSchemePathId,
			final int sequentialNumber,
			final IdlData data) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.parentSchemePathId = parentSchemePathId;
		this.sequentialNumber = sequentialNumber;
		this.data = data;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public PathElement getNative() throws IdlCreateObjectException {
		try {
			return new PathElement(this);
		} catch (final CreateObjectException coe) {
			throw coe.getIdlTransferable();
		}
	}
}
