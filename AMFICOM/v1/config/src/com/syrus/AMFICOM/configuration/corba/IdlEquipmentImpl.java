/*-
 * $Id: IdlEquipmentImpl.java,v 1.9 2005/10/31 12:29:56 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration.corba;

import static java.util.logging.Level.SEVERE;

import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.9 $, $Date: 2005/10/31 12:29:56 $
 * @module config
 */
final class IdlEquipmentImpl extends IdlEquipment {
	private static final long serialVersionUID = -4909941019131393773L;

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
			final IdlIdentifier protoEquipmentId,
			final String name,
			final String description,
			final String supplier,
			final String supplierCode,
			final float latitude,
			final float longitude,
			final String hwSerial, 
			final String hwVersion, 
			final String swSerial,
			final String swVersion,
			final String inventoryNumber,
			final IdlIdentifier imageId) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.domainId = domainId;
		this.protoEquipmentId = protoEquipmentId;
		this.name = name;
		this.description = description;
		this.supplier = supplier;
		this.supplierCode = supplierCode;
		this.latitude = latitude;
		this.longitude = longitude;
		this.hwSerial = hwSerial;
		this.hwVersion = hwVersion;
		this.swSerial = swSerial;
		this.swVersion = swVersion;
		this.inventoryNumber = inventoryNumber;
		this.imageId = imageId;
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
			Log.debugMessage(coe, SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}
}
