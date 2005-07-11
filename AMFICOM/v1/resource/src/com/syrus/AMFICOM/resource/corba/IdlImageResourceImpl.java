/*-
 * $Id: IdlImageResourceImpl.java,v 1.2 2005/07/11 08:18:57 bass Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource.corba;

import static com.syrus.AMFICOM.resource.corba.IdlImageResourcePackage.ImageResourceDataPackage.ImageResourceSort._BITMAP;
import static com.syrus.AMFICOM.resource.corba.IdlImageResourcePackage.ImageResourceDataPackage.ImageResourceSort._FILE;
import static com.syrus.AMFICOM.resource.corba.IdlImageResourcePackage.ImageResourceDataPackage.ImageResourceSort._SCHEME;
import static java.util.logging.Level.SEVERE;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.resource.AbstractImageResource;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.FileImageResource;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.resource.corba.IdlImageResourcePackage.ImageResourceData;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/07/11 08:18:57 $
 * @module resource_v1
 */
final class IdlImageResourceImpl extends IdlImageResource {
	private static final long serialVersionUID = -5060050683432238759L;

	IdlImageResourceImpl() {
		// empty
	}

	IdlImageResourceImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final ImageResourceData data) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.data = data;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public AbstractImageResource getNative() throws IdlCreateObjectException {
		try {
			switch (this.data.discriminator().value()) {
				case _BITMAP:
					return new BitmapImageResource(this);
				case _FILE:
					return new FileImageResource(this);
				case _SCHEME:
					return new SchemeImageResource(this);
				default:
					assert false;
					return null;
			}
		} catch (final CreateObjectException coe) {
			Log.debugException(coe, SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}
}
