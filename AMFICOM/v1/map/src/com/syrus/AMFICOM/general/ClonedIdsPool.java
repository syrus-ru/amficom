/**
 * $Id: ClonedIdsPool.java,v 1.4 2005/08/02 18:05:46 arseniy Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.general;

import java.util.HashMap;
import java.util.Map;

/**
 * @version $Revision: 1.4 $, $Date: 2005/08/02 18:05:46 $
 * @author $Author: arseniy $
 * @module map_v1
 */
public class ClonedIdsPool {

	private Map<String, Identifier> clonedIds = new HashMap<String, Identifier>();

	public final Identifier getClonedId(final short entityCode, final String id) throws IdentifierGenerationException {
		Identifier clonedId = this.clonedIds.get(id);
		if (clonedId == null) {
			clonedId = this.cloneId(entityCode, id);
		}
		return clonedId;
	}
	
	public final Identifier cloneId(final short entityCode, final String id) throws IdentifierGenerationException {
		final Identifier clonedId = IdentifierPool.getGeneratedIdentifier(entityCode);
		this.clonedIds.put(id, clonedId);
		return clonedId;
	}
}


