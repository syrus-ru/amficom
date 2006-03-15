/*-
 * $Id: IdlCharacteristicImpl.java,v 1.7 2006/03/14 10:48:00 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general.corba;

import java.util.logging.Level;

import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.7 $, $Date: 2006/03/14 10:48:00 $
 * @module general
 */
final class IdlCharacteristicImpl extends IdlCharacteristic {
	private static final long serialVersionUID = 200871055614775177L;

	IdlCharacteristicImpl() {
		//empty
	}

	IdlCharacteristicImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final IdlIdentifier typeId,
			final String name,
			final String description,
			final String value,
			final IdlIdentifier characterizableId,
			final boolean editable,
			final boolean visible) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this._typeId = typeId;
		this.name = name;
		this.description = description;
		this.value = value;
		this.characterizableId = characterizableId;
		this.editable = editable;
		this.visible = visible;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public Characteristic getNative() throws IdlCreateObjectException {
		try {
			return new Characteristic(this);
		} catch (final CreateObjectException coe) {
			Log.debugMessage(coe, Level.SEVERE);
			throw coe.getIdlTransferable();
		}
	}
}
