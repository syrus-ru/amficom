/*
 * $Id: AbstractCloneableDomainMember.java,v 1.1 2005/03/15 17:46:56 bass Exp $
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
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/03/15 17:46:56 $
 * @module admin_v1
 */
public abstract class AbstractCloneableDomainMember extends DomainMember
		implements CloneableStorableObject {

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
