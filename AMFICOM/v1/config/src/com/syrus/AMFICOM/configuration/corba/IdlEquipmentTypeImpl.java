/*-
 * $Id: IdlEquipmentTypeImpl.java,v 1.3 2005/07/17 05:19:01 arseniy Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration.corba;

import static java.util.logging.Level.SEVERE;

import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.3 $, $Date: 2005/07/17 05:19:01 $
 * @module config_v1
 */
final class IdlEquipmentTypeImpl extends IdlEquipmentType {
	private static final long serialVersionUID = 1282054745812003624L;

	IdlEquipmentTypeImpl() {
		// empty
	}

	IdlEquipmentTypeImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final String codename,
			final String description,
			final String name,
			final String manufacturer,
			final String manufacturerCode) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.codename = codename;
		this.description = description;
		this.name = name;
		this.manufacturer = manufacturer;
		this.manufacturerCode = manufacturerCode;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public EquipmentType getNative() throws IdlCreateObjectException {
		try {
			return new EquipmentType(this);
		} catch (final CreateObjectException coe) {
			Log.debugException(coe, SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}
}
