/*-
 * $Id: IdlDeliveryAttributesImpl.java,v 1.1 2005/11/09 15:16:38 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.event.corba;

import java.util.logging.Level;

import com.syrus.AMFICOM.event.DeliveryAttributes;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.reflectometry.corba.IdlSeverity;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/11/09 15:16:38 $
 * @module event
 */
final class IdlDeliveryAttributesImpl extends IdlDeliveryAttributes {
	private static final long serialVersionUID = -4989950182742090486L;

	IdlDeliveryAttributesImpl() {
		// empty
	}

	/**
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param severity
	 * @param roleIds
	 * @param systemUserIds
	 */
	IdlDeliveryAttributesImpl(final IdlIdentifier id, final long created,
			final long modified, final IdlIdentifier creatorId,
			final IdlIdentifier modifierId, final long version,
			final IdlSeverity severity, final IdlIdentifier roleIds[],
			final IdlIdentifier systemUserIds[]) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.severity = severity;
		this.roleIds = roleIds;
		this.systemUserIds = systemUserIds;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public DeliveryAttributes getNative() throws IdlCreateObjectException {
		try {
			return new DeliveryAttributes(this);
		} catch (final CreateObjectException coe) {
			Log.debugMessage(coe, Level.SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}
}
