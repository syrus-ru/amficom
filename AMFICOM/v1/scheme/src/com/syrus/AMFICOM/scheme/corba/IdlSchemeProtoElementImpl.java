/*-
 * $Id: IdlSchemeProtoElementImpl.java,v 1.8.2.1 2006/03/15 15:47:49 arseniy Exp $
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
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.8.2.1 $, $Date: 2006/03/15 15:47:49 $
 * @module scheme
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
			final IdlIdentifier protoEquipmentId,
			final IdlIdentifier symbolId,
			final IdlIdentifier ugoCellId,
			final IdlIdentifier schemeCellId,
			final IdlIdentifier parentSchemeProtoGroupId,
			final IdlIdentifier parentSchemeProtoElementId) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.name = name;
		this.description = description;
		this.label = label;
		this.protoEquipmentId = protoEquipmentId;
		this.symbolId = symbolId;
		this.ugoCellId = ugoCellId;
		this.schemeCellId = schemeCellId;
		this.parentSchemeProtoGroupId = parentSchemeProtoGroupId;
		this.parentSchemeProtoElementId = parentSchemeProtoElementId;
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
			Log.debugMessage(coe, SEVERE);
			throw coe.getIdlTransferable();
		}
	}
}
