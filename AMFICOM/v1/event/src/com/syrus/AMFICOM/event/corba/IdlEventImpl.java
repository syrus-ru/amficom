/*-
 * $Id: IdlEventImpl.java,v 1.3 2005/08/08 11:32:37 arseniy Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.event.corba;

import static java.util.logging.Level.SEVERE;

import com.syrus.AMFICOM.event.Event;
import com.syrus.AMFICOM.event.corba.IdlEventPackage.IdlEventParameter;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.3 $, $Date: 2005/08/08 11:32:37 $
 * @module event
 */
final class IdlEventImpl extends IdlEvent {
	private static final long serialVersionUID = -1503179497333743516L;

	IdlEventImpl() {
		// empty
	}

	IdlEventImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final IdlIdentifier typeId,
			final String description,
			final IdlEventParameter parameters[],
			final IdlIdentifier eventSourceIds[]) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this._typeId = typeId;
		this.description = description;
		this.parameters = parameters;
		this.eventSourceIds = eventSourceIds;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public Event getNative() throws IdlCreateObjectException {
		try {
			return new Event(this);
		} catch (final CreateObjectException coe) {
			Log.debugException(coe, SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}
}
