/*-
 * $Id: AbstractCloneableDomainMember.java,v 1.10 2005/08/02 08:19:37 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.syrus.AMFICOM.general.CloneableStorableObject;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectVersion;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.10 $, $Date: 2005/08/02 08:19:37 $
 * @module admin
 */
public abstract class AbstractCloneableDomainMember extends DomainMember
		implements CloneableStorableObject {
	private static final long serialVersionUID = -1126583003571701586L;

	/**
	 * Shouldn&apos;t be declared {@code transient} since the GUI often uses
	 * drag&apos;n&apos;drop. 
	 */
	protected Map<Identifier, Identifier> clonedIdMap;

	/**
	 * @param id
	 */
	protected AbstractCloneableDomainMember(final Identifier id) {
		super(id);
	}

	protected AbstractCloneableDomainMember() {
		super();
	}

	/**
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param domainId
	 */
	protected AbstractCloneableDomainMember(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final Identifier domainId) {
		super(id, created, modified, creatorId, modifierId, version, domainId);
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
