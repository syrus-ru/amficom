/*-
 * $Id: IdlSchemeElementImpl.java,v 1.13 2006/03/15 16:42:48 bass Exp $
 *
 * Copyright ? 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import static java.util.logging.Level.SEVERE;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeElementPackage.IdlSchemeElementKind;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.13 $, $Date: 2006/03/15 16:42:48 $
 * @module scheme
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
			final IdlSchemeElementKind kind,
			final IdlIdentifier protoEquipmentId,
			final IdlIdentifier equipmentId,
			final IdlIdentifier kisId,
			final IdlIdentifier siteNodeId,
			final IdlIdentifier symbolId,
			final IdlIdentifier ugoCellId,
			final IdlIdentifier schemeCellId,
			final IdlIdentifier parentSchemeId,
			final IdlIdentifier parentSchemeElementId) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.name = name;
		this.description = description;
		this.label = label;
		this.kind = kind;
		this.protoEquipmentId = protoEquipmentId;
		this.equipmentId = equipmentId;
		this.kisId = kisId;
		this.siteNodeId = siteNodeId;
		this.symbolId = symbolId;
		this.ugoCellId = ugoCellId;
		this.schemeCellId = schemeCellId;
		this.parentSchemeId = parentSchemeId;
		this.parentSchemeElementId = parentSchemeElementId;
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
			Log.debugMessage(coe, SEVERE);
			throw coe.getIdlTransferable();
		}
	}
}
