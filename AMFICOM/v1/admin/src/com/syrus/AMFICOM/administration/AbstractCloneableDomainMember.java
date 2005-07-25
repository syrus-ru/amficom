/*
 * $Id: AbstractCloneableDomainMember.java,v 1.7 2005/07/25 20:49:23 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration;

import java.util.Date;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectVersion;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.7 $, $Date: 2005/07/25 20:49:23 $
 * @module admin_v1
 */
public abstract class AbstractCloneableDomainMember extends DomainMember
		implements Cloneable {
	private static final long serialVersionUID = -1126583003571701586L;

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
			/*
			 * Never.
			 */
			assert false;
			return null;
		}
	}
}
