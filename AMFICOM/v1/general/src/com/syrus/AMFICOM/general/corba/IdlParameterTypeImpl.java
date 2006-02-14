/*-
 * $Id: IdlParameterTypeImpl.java,v 1.5.2.2 2006/02/14 01:26:42 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general.corba;

import static java.util.logging.Level.SEVERE;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.5.2.2 $, $Date: 2006/02/14 01:26:42 $
 * @module general
 */
final class IdlParameterTypeImpl extends IdlParameterType {
	private static final long serialVersionUID = -7493188669027635556L;

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
			final IdlDataType idlDataType,
			final IdlMeasurementUnit idlMeasurementUnit) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.codename = codename;
		this.description = description;
		this.idlDataType = idlDataType;
		this.idlMeasurementUnit = idlMeasurementUnit;
	}

	/**
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public ParameterType getNative() throws IdlCreateObjectException {
		try {
			return new ParameterType(this);
		} catch (final CreateObjectException coe) {
			Log.debugMessage(coe, SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}
}
