/*-
 * $Id: AbstractCloneableDomainMember.java,v 1.8 2005/07/29 13:07:00 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

import com.syrus.AMFICOM.general.CloneableStorableObject;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectVersion;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.8 $, $Date: 2005/07/29 13:07:00 $
 * @module admin
 */
public abstract class AbstractCloneableDomainMember extends DomainMember
		implements CloneableStorableObject {
	private static final long serialVersionUID = -1126583003571701586L;

	protected transient Map<Identifier, Identifier> clonedIdMap;

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
	 * @see Object#clone()
	 */
	@Override
	public AbstractCloneableDomainMember clone() {
		try {
			return (AbstractCloneableDomainMember) super.clone();
		} catch (final CloneNotSupportedException cnse) {
			/*-
			 * Never.
			 */
			assert false;
			return null;
		}
	}

	/**
	 * @see CloneableStorableObject#getClonedIdMap()
	 */
	public final Map<Identifier, Identifier> getClonedIdMap() {
		return (this.clonedIdMap == null)
				? Collections.<Identifier, Identifier>emptyMap()
				: Collections.unmodifiableMap(this.clonedIdMap);
	}

}
