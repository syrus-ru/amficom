/*
 * $Id: AbstractCloneableStorableObject.java,v 1.5 2005/06/21 15:10:05 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.Date;

import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/06/21 15:10:05 $
 * @module general_v1
 */
public abstract class AbstractCloneableStorableObject extends StorableObject implements CloneableStorableObject {
	private static final long serialVersionUID = 8657340060738270948L;

	/**
	 * @param id
	 */
	protected AbstractCloneableStorableObject(final Identifier id) {
		super(id);
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
			final Date created, final Date modified,
			final Identifier creatorId,
			final Identifier modifierId, final long version) {
		super(id, created, modified, creatorId, modifierId, version);
	}

	protected AbstractCloneableStorableObject() {
		// super();
	}

	/**
	 * @see Object#clone()
	 * @see CloneableStorableObject#clone()
	 */
	@Override
	public AbstractCloneableStorableObject clone() {
		try {
			return (AbstractCloneableStorableObject) super.clone();
		} catch (final CloneNotSupportedException cnse) {
			/*
			 * Never.
			 */
			Log.debugException(cnse, Log.SEVERE);
			return null;
		}
	}
}
