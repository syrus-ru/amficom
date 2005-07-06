/*-
 * $Id: IdlEquipmentImpl.java,v 1.1 2005/07/06 15:49:25 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration.corba;

import static com.syrus.util.Log.SEVERE;

import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/07/06 15:49:25 $
 * @module config_v1
 */
final class IdlEquipmentImpl extends IdlEquipment {
	private static final long serialVersionUID = 7949529372291866235L;

	IdlEquipmentImpl() {
		// empty
	}

	IdlEquipmentImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final IdlIdentifier domainId,
			final IdlIdentifier typeId,
			final String name,
			final String description,
			final String supplier,
			final String supplierCode,
			final float longitude,
			final float latitude,
			final String hwSerial, 
			final String hwVersion, 
			final String swSerial,
			final String swVersion,
			final String inventoryNumber,
			final IdlIdentifier imageId,
			final IdlIdentifier characteristicIds[]) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.domainId = domainId;
		this._typeId = typeId;
		this.name = name;
		this.description = description;
		this.supplier = supplier;
		this.supplierCode = supplierCode;
		this.longitude = longitude;
		this.latitude = latitude;
		this.hwSerial = hwSerial;
		this.hwVersion = hwVersion;
		this.swSerial = swSerial;
		this.swVersion = swVersion;
		this.inventoryNumber = inventoryNumber;
		this.imageId = imageId;
		this.characteristicIds = characteristicIds;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public Equipment getNative() throws IdlCreateObjectException {
		try {
			return new Equipment(this);
		} catch (final CreateObjectException coe) {
			Log.debugException(coe, SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}
}
