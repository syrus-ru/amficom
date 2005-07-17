/*-
 * $Id: IdlParameterTypeImpl.java,v 1.4 2005/07/17 05:17:13 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general.corba;

import java.util.logging.Level;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.4 $, $Date: 2005/07/17 05:17:13 $
 * @module general
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
			final IdlDataType dataType) {
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
			Log.debugException(coe, Level.SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}
}
