/**
 * $Id: MapUtils.java,v 1.2 2005/08/22 15:54:38 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map;

import java.util.HashSet;
import java.util.Set;

import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * 
 * @version $Revision: 1.2 $, $Date: 2005/08/22 15:54:38 $
 * @author krupenn
 * @author $Author: krupenn $
 * @module mapviewclient
 */
public class MapUtils {

	public static Set<StorableObject> applyCondition(Set<StorableObject> objects, StorableObjectCondition condition) throws IllegalObjectEntityException {
		Set<StorableObject> resultingObjects = new HashSet<StorableObject>(); 
		for(StorableObject storableObject : objects) {
			if(condition.isConditionTrue(storableObject)) {
				resultingObjects.add(storableObject);
			}
		}
		return resultingObjects;
	}

}
