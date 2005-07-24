/*-
 * $Id: IdlSchemeImpl.java,v 1.3 2005/07/24 17:40:35 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.corba.IdlSchemePackage.IdlKind;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/07/24 17:40:35 $
 * @module scheme
 */
final class IdlSchemeImpl extends IdlScheme {
	private static final long serialVersionUID = 329210659483248316L;

	IdlSchemeImpl() {
		// empty
	}

	IdlSchemeImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final String name,
			final String description,
			final String label,
			final int width,
			final int height,
			final IdlKind kind,
			final IdlIdentifier domainId,
			final IdlIdentifier mapId,
			final IdlIdentifier symbolId,
			final IdlIdentifier ugoCellId,
			final IdlIdentifier schemeCellId,
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
		this.width = width;
		this.height = height;
		this.kind = kind;
		this.domainId = domainId;
		this.mapId = mapId;
		this.symbolId = symbolId;
		this.ugoCellId = ugoCellId;
		this.schemeCellId = schemeCellId;
		this.parentSchemeElementId = parentSchemeElementId;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public Scheme getNative() throws IdlCreateObjectException {
		return new Scheme(this);
	}
}
