/**
 * $Id: ClonedIdsPool.java,v 1.2 2005/07/24 16:27:05 arseniy Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.general;

import java.util.HashMap;
import java.util.Map;

/**
 * @version $Revision: 1.2 $, $Date: 2005/07/24 16:27:05 $
 * @author $Author: arseniy $
 * @module map_v1
 */
public class ClonedIdsPool {

	private Map<String, Identifier> clonedIds = new HashMap<String, Identifier>();

	public final Identifier getClonedId(short entityCode, String id)
		throws IdentifierGenerationException
	{
		Identifier clonedId = this.clonedIds.get(id);
		if(clonedId == null)
			clonedId = cloneId(entityCode, id);
		return clonedId;
	}
	
	public final Identifier cloneId(short entityCode, final String id)
		throws IdentifierGenerationException
	{
		final Identifier clonedId = IdentifierPool.getGeneratedIdentifier(entityCode);
		this.clonedIds.put(id, clonedId);
		return clonedId;
	}
}


