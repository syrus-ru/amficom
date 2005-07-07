/*-
 * $Id: IdlPathElementImpl.java,v 1.1 2005/07/07 15:52:11 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.Data;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/07/07 15:52:11 $
 * @module scheme_v1
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
			final Data data) {
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
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public PathElement getNative() {
		return new PathElement(this);
	}
}
