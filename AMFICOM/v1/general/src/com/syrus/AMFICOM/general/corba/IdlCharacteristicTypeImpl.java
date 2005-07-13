/*-
 * $Id: IdlCharacteristicTypeImpl.java,v 1.2 2005/07/13 16:01:31 arseniy Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general.corba;

import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.CharacteristicTypeSort;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.2 $, $Date: 2005/07/13 16:01:31 $
 * @module general_v1
 */
final class IdlCharacteristicTypeImpl extends IdlCharacteristicType {
	private static final long serialVersionUID = 2061150811286784269L;

	IdlCharacteristicTypeImpl() {
		// empty
	}

	IdlCharacteristicTypeImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final String codename,
			final String description,
			final String name,
			final IdlDataType dataType,
			final CharacteristicTypeSort sort) {
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
		this.sort = sort;
	}

	/**
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public CharacteristicType getNative() {
		return new CharacteristicType(this);
	}
}
