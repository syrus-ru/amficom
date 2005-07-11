/*-
 * $Id: IdlSchemeElementImpl.java,v 1.2 2005/07/11 08:19:03 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import static java.util.logging.Level.SEVERE;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/07/11 08:19:03 $
 * @module scheme_v1
 */
final class IdlSchemeElementImpl extends IdlSchemeElement {
	private static final long serialVersionUID = 941228240066170382L;

	IdlSchemeElementImpl() {
		// empty
	}

	IdlSchemeElementImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final String name,
			final String description,
			final String label,
			final IdlIdentifier equipmentTypeId,
			final IdlIdentifier equipmentId,
			final IdlIdentifier kisId,
			final IdlIdentifier siteNodeId,
			final IdlIdentifier symbolId,
			final IdlIdentifier ugoCellId,
			final IdlIdentifier schemeCellId,
			final IdlIdentifier parentSchemeId,
			final IdlIdentifier parentSchemeElementId,
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
		this.equipmentId = equipmentId;
		this.kisId = kisId;
		this.siteNodeId = siteNodeId;
		this.symbolId = symbolId;
		this.ugoCellId = ugoCellId;
		this.schemeCellId = schemeCellId;
		this.parentSchemeId = parentSchemeId;
		this.parentSchemeElementId = parentSchemeElementId;
		this.characteristicIds = characteristicIds;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public SchemeElement getNative() throws IdlCreateObjectException {
		try {
			return new SchemeElement(this);
		} catch (final CreateObjectException coe) {
			Log.debugException(coe, SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}
}
