/*
 * $Id: AbstractCloneableDomainMember.java,v 1.2 2005/03/17 09:12:05 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration;

import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.util.Log;
import java.util.*;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.2 $, $Date: 2005/03/17 09:12:05 $
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

	/**
	 * @param transferable
	 * @param domainId
	 */
	protected AbstractCloneableDomainMember(
			final StorableObject_Transferable transferable,
			final Identifier domainId) {
		super(transferable, domainId);
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
