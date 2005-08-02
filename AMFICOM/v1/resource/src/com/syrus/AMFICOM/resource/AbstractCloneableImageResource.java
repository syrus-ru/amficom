/*-
 * $Id: AbstractCloneableImageResource.java,v 1.2 2005/08/02 08:19:37 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CloneableStorableObject;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.resource.corba.IdlImageResource;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/08/02 08:19:37 $
 * @module resource
 */
public abstract class AbstractCloneableImageResource extends AbstractImageResource
		implements CloneableStorableObject {
	/**
	 * Shouldn&apos;t be declared {@code transient} since the GUI often uses
	 * drag&apos;n&apos;drop. 
	 */
	protected Map<Identifier, Identifier> clonedIdMap;

	/**
	 * @param id
	 */
	AbstractCloneableImageResource(final Identifier id)
	throws ApplicationException {
		super(id);
	}

	AbstractCloneableImageResource(final IdlImageResource imageResource)
	throws CreateObjectException {
		super(imageResource);
	}

	/**
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 */
	AbstractCloneableImageResource(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version) {
		super(id, created, modified, creatorId, modifierId, version);
	}

	/**
	 * @see CloneableStorableObject#getClonedIdMap()
	 */
	public final Map<Identifier, Identifier> getClonedIdMap() {
		if (this.clonedIdMap == null || this.clonedIdMap.isEmpty()) {
			 return Collections.emptyMap();
		}
		try {
			return new HashMap<Identifier, Identifier>(this.clonedIdMap);
		} finally {
			this.clonedIdMap.clear();
		}
	}
}
