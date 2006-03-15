/*-
 * $Id: IdlCableLinkImpl.java,v 1.7.2.1 2006/03/15 13:53:17 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration.corba;

import static java.util.logging.Level.SEVERE;

import com.syrus.AMFICOM.configuration.CableLink;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.7.2.1 $, $Date: 2006/03/15 13:53:17 $
 * @module config
 */
final class IdlCableLinkImpl extends IdlCableLink {
	private static final long serialVersionUID = -3510136136010447117L;

	IdlCableLinkImpl() {
		// empty
	}

	IdlCableLinkImpl(final IdlIdentifier id,
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
			final String mark) {
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
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public CableLink getNative() throws IdlCreateObjectException {
		try {
			return new CableLink(this);
		} catch (final CreateObjectException coe) {
			Log.debugMessage(coe, SEVERE);
			throw coe.getIdlTransferable();
		}
	}
}
