/*
 * $Id: IdlProtoEquipmentImpl.java,v 1.7 2006/04/19 13:22:15 bass Exp $
 *
 * Copyright ? 2004 Syrus Systems.
 * ??????-??????????? ?????.
 * ??????: ???????.
 */
package com.syrus.AMFICOM.configuration.corba;

import static java.util.logging.Level.SEVERE;

import com.syrus.AMFICOM.configuration.ProtoEquipment;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.7 $, $Date: 2006/04/19 13:22:15 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module config
 */
final class IdlProtoEquipmentImpl extends IdlProtoEquipment {
	private static final long serialVersionUID = -431255669988017143L;

	IdlProtoEquipmentImpl() {
		// empty
	}

	IdlProtoEquipmentImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final IdlIdentifier _typeId,
			final String name,
			final String description,
			final String manufacturer,
			final String manufacturerCode) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this._typeId = _typeId;
		this.name = name;
		this.description = description;
		this.manufacturer = manufacturer;
		this.manufacturerCode = manufacturerCode;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public ProtoEquipment getNative() throws IdlCreateObjectException {
		try {
			return new ProtoEquipment(this);
		} catch (final CreateObjectException coe) {
			Log.debugMessage(coe, SEVERE);
			throw coe.getIdlTransferable();
		}
	}
}
