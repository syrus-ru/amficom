/**
 * $Id: ClonedIdsPool.java,v 1.3 2005/07/26 08:57:21 arseniy Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.general;

import java.util.HashMap;
import java.util.Map;

/**
 * @version $Revision: 1.3 $, $Date: 2005/07/26 08:57:21 $
 * @author $Author: arseniy $
 * @module map_v1
 */
public class ClonedIdsPool {

	private Map<String, Identifier> clonedIds = new HashMap<String, Identifier>();

	public final Identifier getClonedId(short entityCode, String id) throws IdentifierGenerationException {
		Identifier clonedId = this.clonedIds.get(id);
		if (clonedId == null) {
			clonedId = this.cloneId(entityCode, id);
		}
		return clonedId;
	}
	
	public final Identifier cloneId(short entityCode, final String id) throws IdentifierGenerationException {
		final Identifier clonedId = IdentifierPool.getGeneratedIdentifier(entityCode);
		this.clonedIds.put(id, clonedId);
		return clonedId;
	}
}


