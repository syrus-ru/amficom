/*-
 * $Id: IdlDomainMemberImpl.java,v 1.2 2005/09/14 19:01:24 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration.corba;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.2 $, $Date: 2005/09/14 19:01:24 $
 * @module administration
 */
final class IdlDomainMemberImpl extends IdlDomainMember {
	private static final long serialVersionUID = 5206526009305651979L;

	IdlDomainMemberImpl() {
		// empty
	}

	IdlDomainMemberImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final IdlIdentifier domainId) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.domainId = domainId;
	}

	/**
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public DomainMember getNative() {
		throw new UnsupportedOperationException();
	}
}
