/*-
 * $Id: IdlEventTypeImpl.java,v 1.2 2005/07/11 08:18:57 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.event.corba;

import static java.util.logging.Level.SEVERE;

import com.syrus.AMFICOM.event.EventType;
import com.syrus.AMFICOM.event.corba.IdlEventTypePackage.UserAlertKinds;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/07/11 08:18:57 $
 * @module event_v1
 */
final class IdlEventTypeImpl extends IdlEventType {
	private static final long serialVersionUID = -7624987454620297508L;

	IdlEventTypeImpl() {
		// empty
	}

	IdlEventTypeImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final String codename,
			final String description,
			final IdlIdentifier parameterTypeIds[],
			final UserAlertKinds userAlertKinds[]) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.codename = codename;
		this.description = description;
		this.parameterTypeIds = parameterTypeIds;
		this.userAlertKinds = userAlertKinds;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public EventType getNative() throws IdlCreateObjectException {
		try {
			return new EventType(this);
		} catch (final CreateObjectException coe) {
			Log.debugException(coe, SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}
}
