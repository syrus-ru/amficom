/**
 * $Id: ClonedIdsPool.java,v 1.1 2005/05/30 14:50:23 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.general;

import java.util.HashMap;
import java.util.Map;

/**
 * @version $Revision: 1.1 $, $Date: 2005/05/30 14:50:23 $
 * @author $Author: krupenn $
 * @module map_v1
 */
public class ClonedIdsPool {

	private Map clonedIds = new HashMap();

	public final Identifier getClonedId(short entityCode, String id)
		throws IdentifierGenerationException
	{
		Identifier clonedId = (Identifier )this.clonedIds.get(id);
		if(clonedId == null)
			clonedId = cloneId(entityCode, id);
		return clonedId;
	}
	
	public final Identifier cloneId(short entityCode, String id)
		throws IdentifierGenerationException
	{
		Identifier clonedId;
		clonedId = IdentifierPool.getGeneratedIdentifier(entityCode);
		this.clonedIds.put(id, clonedId);
		return clonedId;
	}
}


