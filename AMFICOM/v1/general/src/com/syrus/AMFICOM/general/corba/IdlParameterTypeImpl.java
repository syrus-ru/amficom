/*-
 * $Id: IdlParameterTypeImpl.java,v 1.1 2005/07/04 15:23:09 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general.corba;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/07/04 15:23:09 $
 * @module general_v1
 */
final class IdlParameterTypeImpl extends IdlParameterType {
	private static final long serialVersionUID = 6364129720469699973L;

	IdlParameterTypeImpl() {
		// empty
	}

	IdlParameterTypeImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final String codename,
			final String description,
			final String name,
			final DataType dataType,
			final IdlIdentifier characteristicIds[]) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.codename = codename;
		this.description = description;
		this.name = name;
		this.dataType = dataType;
		this.characteristicIds = characteristicIds;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public ParameterType getNative() throws IdlCreateObjectException {
		try {
			return new ParameterType(this);
		} catch (final CreateObjectException coe) {
			Log.debugException(coe, Log.SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}
}
