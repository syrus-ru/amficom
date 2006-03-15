/*-
 * $Id: IdlParameterTypeImpl.java,v 1.5.2.3 2006/03/15 13:26:10 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general.corba;

import com.syrus.AMFICOM.general.ParameterType;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.5.2.3 $, $Date: 2006/03/15 13:26:10 $
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
		return new ParameterType(this);
	}
}
