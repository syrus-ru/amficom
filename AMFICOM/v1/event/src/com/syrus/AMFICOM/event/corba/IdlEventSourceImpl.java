/*-
 * $Id: IdlEventSourceImpl.java,v 1.2 2005/08/08 11:32:37 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.event.corba;

import com.syrus.AMFICOM.event.EventSource;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.2 $, $Date: 2005/08/08 11:32:37 $
 * @module event
 */
final class IdlEventSourceImpl extends IdlEventSource {
	private static final long serialVersionUID = 6938924910013150430L;

	IdlEventSourceImpl() {
		// empty
	}

	IdlEventSourceImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final IdlIdentifier sourceEntityId) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.sourceEntityId = sourceEntityId;
	}

	/**
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public EventSource getNative() {
		return new EventSource(this);
	}
}
