/*-
 * $Id: ClonedIdsPool.java,v 1.7 2005/09/04 14:57:32 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.HashMap;
import java.util.Map;

import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.util.Shitlet;

/**
 * @version $Revision: 1.7 $, $Date: 2005/09/04 14:57:32 $
 * @author $Author: bass $
 * @module general
 */
@Shitlet
public final class ClonedIdsPool {
	private Map<String, Identifier> clonedIds = new HashMap<String, Identifier>();
	
	public Identifier getClonedId(final short entityCode,
			final XmlIdentifier id)
	throws IdentifierGenerationException {
		final Identifier clonedId = this.clonedIds.get(id.getStringValue());
		return clonedId == null ? this.cloneId(entityCode, id) : clonedId;
	}

	private Identifier cloneId(final short entityCode,
			final XmlIdentifier id)
	throws IdentifierGenerationException {
		final Identifier clonedId = IdentifierPool.getGeneratedIdentifier(entityCode);
		this.clonedIds.put(id.getStringValue(), clonedId);
		return clonedId;
	}

	public void setExistingId(final XmlIdentifier id,
			final Identifier clonedId) {
		this.clonedIds.put(id.getStringValue(), clonedId);
	}
}
