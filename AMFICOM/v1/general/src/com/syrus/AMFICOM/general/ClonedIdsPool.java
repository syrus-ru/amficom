/*-
 * $Id: ClonedIdsPool.java,v 1.6 2005/08/31 13:24:21 bass Exp $
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
 * @version $Revision: 1.6 $, $Date: 2005/08/31 13:24:21 $
 * @author $Author: bass $
 * @module general
 */
@Shitlet
public final class ClonedIdsPool {
	private Map<XmlIdentifier, Identifier> clonedIds = new HashMap<XmlIdentifier, Identifier>();
	
	public Identifier getClonedId(final short entityCode,
			final XmlIdentifier id)
	throws IdentifierGenerationException {
		final Identifier clonedId = this.clonedIds.get(id);
		return clonedId == null ? this.cloneId(entityCode, id) : clonedId;
	}

	private Identifier cloneId(final short entityCode,
			final XmlIdentifier id)
	throws IdentifierGenerationException {
		final Identifier clonedId = IdentifierPool.getGeneratedIdentifier(entityCode);
		this.clonedIds.put(id, clonedId);
		return clonedId;
	}

	public void setExistingId(final XmlIdentifier id,
			final Identifier clonedId) {
		this.clonedIds.put(id, clonedId);
	}
}
