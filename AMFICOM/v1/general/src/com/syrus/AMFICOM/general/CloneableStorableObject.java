/*
 * $Id: CloneableStorableObject.java,v 1.2 2005/03/12 20:09:58 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.util.Log;
import java.util.Date;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.2 $, $Date: 2005/03/12 20:09:58 $
 * @module general_v1
 */
public abstract class CloneableStorableObject extends StorableObject implements Cloneable {
	private static final long serialVersionUID = 8657340060738270948L;
	/**
	 * @param id
	 */
	protected CloneableStorableObject(final Identifier id) {
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
	protected CloneableStorableObject(final Identifier id,
			final Date created, final Date modified,
			final Identifier creatorId,
			final Identifier modifierId, final long version) {
		super(id, created, modified, creatorId, modifierId, version);
	}

	/**
	 * @param transferable
	 */
	protected CloneableStorableObject(
			final StorableObject_Transferable transferable) {
		super(transferable);
	}

	/**
	 * @see Object#clone()
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
