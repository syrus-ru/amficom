/*-
 * $Id: ClonedIdsPool.java,v 1.5 2005/08/26 10:05:07 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.HashMap;
import java.util.Map;

/**
 * @version $Revision: 1.5 $, $Date: 2005/08/26 10:05:07 $
 * @author $Author: bass $
 * @module general
 */
public final class ClonedIdsPool {
	private Map<String, Identifier> clonedIds = new HashMap<String, Identifier>();

	public Identifier getClonedId(final short entityCode, final String id) throws IdentifierGenerationException {
		Identifier clonedId = this.clonedIds.get(id);
		if (clonedId == null) {
			clonedId = this.cloneId(entityCode, id);
		}
		return clonedId;
	}

	public Identifier cloneId(final short entityCode, final String id) throws IdentifierGenerationException {
		final Identifier clonedId = IdentifierPool.getGeneratedIdentifier(entityCode);
		this.clonedIds.put(id, clonedId);
		return clonedId;
	}

	public final void setExistingId(final String id, final Identifier clonedId) {
		this.clonedIds.put(id, clonedId);
	}
}
