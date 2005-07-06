/*-
 * $Id: IdlAbstractLinkImpl.java,v 1.1 2005/07/06 15:49:24 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration.corba;

import com.syrus.AMFICOM.configuration.AbstractLink;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/07/06 15:49:24 $
 * @module config_v1
 */
final class IdlAbstractLinkImpl extends IdlAbstractLink {
	private static final long serialVersionUID = 6150235882869445595L;

	IdlAbstractLinkImpl() {
		// empty
	}

	IdlAbstractLinkImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final IdlIdentifier domainId,
			final String name,
			final String description,
			final IdlIdentifier typeId,
			final String inventoryNo,
			final String supplier,
			final String supplierCode,
			final int color,
			final String mark,
			final IdlIdentifier characteristicIds[]) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.domainId = domainId;
		this.name = name;
		this.description = description;
		this._typeId = typeId;
		this.inventoryNo = inventoryNo;
		this.supplier = supplier;
		this.supplierCode = supplierCode;
		this.color = color;
		this.mark = mark;
		this.characteristicIds = characteristicIds;
	}

	/**
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public AbstractLink getNative() {
		throw new UnsupportedOperationException();
	}
}
