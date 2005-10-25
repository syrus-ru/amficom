/*-
 * $Id: AbstractCloneableDomainMember.java,v 1.14 2005/10/25 19:53:15 bass Exp $
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
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.14 $, $Date: 2005/10/25 19:53:15 $
 * @module administration
 */
public abstract class AbstractCloneableDomainMember<T extends AbstractCloneableDomainMember<T>>
		extends DomainMember<T>
		implements CloneableStorableObject {
	private static final long serialVersionUID = -1126583003571701586L;

	/**
	 * Shouldn&apos;t be declared {@code transient} since the GUI often uses
	 * drag&apos;n&apos;drop. 
	 */
	protected Map<Identifier, Identifier> clonedIdMap;

	protected AbstractCloneableDomainMember(/*IdlAbstractCloneableDomainMember*/) {
		// super();
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
	 * Minimalistic constructor used when importing from XML.
	 *
	 * @param id
	 * @param importType
	 * @param entityCode
	 * @param created
	 * @param creatorId
	 * @throws IdentifierGenerationException
	 */
	protected AbstractCloneableDomainMember(final XmlIdentifier id,
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
