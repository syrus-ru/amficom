/*
 * $Id: AbstractCloneableDomainMember.java,v 1.3 2005/04/01 14:41:33 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration;

import java.util.Date;

import com.syrus.AMFICOM.general.CloneableStorableObject;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bob $
 * @version $Revision: 1.3 $, $Date: 2005/04/01 14:41:33 $
 * @module admin_v1
 */
public abstract class AbstractCloneableDomainMember extends DomainMember
		implements CloneableStorableObject {
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
			final Date created, final Date modified,
			final Identifier creatorId,
			final Identifier modifierId, final long version,
			final Identifier domainId) {
		super(id, created, modified, creatorId, modifierId, version,
				domainId);
	}

	/**
	 * @see Object#clone()
	 * @see CloneableStorableObject#clone()
	 */
	public Object clone() {
		try {
			return super.clone();
		} catch (final CloneNotSupportedException cnse) {
			/*
			 * Never.
			 */
			Log.debugException(cnse, Log.SEVERE);
			return null;
		}
	}
}
