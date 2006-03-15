/*-
 * $Id: AbstractCloneableStorableObject.java,v 1.12.2.1 2006/03/15 13:28:07 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.syrus.AMFICOM.general.xml.XmlIdentifier;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.12.2.1 $, $Date: 2006/03/15 13:28:07 $
 * @module general
 */
public abstract class AbstractCloneableStorableObject
		extends StorableObject
		implements CloneableStorableObject {
	/**
	 * Shouldn&apos;t be declared {@code transient} since the GUI often uses
	 * drag&apos;n&apos;drop. 
	 */
	protected Map<Identifier, Identifier> clonedIdMap;

	protected AbstractCloneableStorableObject(/*IdlAbstractCloneableStorableObject*/) {
		// super();
	}

	/**
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 */
	protected AbstractCloneableStorableObject(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version) {
		super(id, created, modified, creatorId, modifierId, version);
	}

	/**
	 * Minimalistic constructor used when importing from XML.
	 *
	 * @param id
	 * @param importType
	 * @param created
	 * @param creatorId
	 * @throws IdentifierGenerationException
	 */
	protected AbstractCloneableStorableObject(final XmlIdentifier id,
			final String importType, final short entityCode,
			final Date created, final Identifier creatorId)
	throws IdentifierGenerationException {
		super(id, importType, entityCode, created, creatorId);
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
