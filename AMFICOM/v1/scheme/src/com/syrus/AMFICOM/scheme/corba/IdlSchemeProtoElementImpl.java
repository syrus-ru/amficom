/*-
 * $Id: IdlSchemeProtoElementImpl.java,v 1.1 2005/07/07 15:52:11 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import static com.syrus.util.Log.SEVERE;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/07/07 15:52:11 $
 * @module scheme_v1
 */
final class IdlSchemeProtoElementImpl extends IdlSchemeProtoElement {
	private static final long serialVersionUID = -1014317040348772308L;

	IdlSchemeProtoElementImpl() {
		// empty
	}

	IdlSchemeProtoElementImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final String name,
			final String description,
			final String label,
			final IdlIdentifier equipmentTypeId,
			final IdlIdentifier symbolId,
			final IdlIdentifier ugoCellId,
			final IdlIdentifier schemeCellId,
			final IdlIdentifier parentSchemeProtoGroupId,
			final IdlIdentifier parentSchemeProtoElementId,
			final IdlIdentifier characteristicIds[]) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.name = name;
		this.description = description;
		this.label = label;
		this.equipmentTypeId = equipmentTypeId;
		this.symbolId = symbolId;
		this.ugoCellId = ugoCellId;
		this.schemeCellId = schemeCellId;
		this.parentSchemeProtoGroupId = parentSchemeProtoGroupId;
		this.parentSchemeProtoElementId = parentSchemeProtoElementId;
		this.characteristicIds = characteristicIds;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public SchemeProtoElement getNative() throws IdlCreateObjectException {
		try {
			return new SchemeProtoElement(this);
		} catch (final CreateObjectException coe) {
			Log.debugException(coe, SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}
}
