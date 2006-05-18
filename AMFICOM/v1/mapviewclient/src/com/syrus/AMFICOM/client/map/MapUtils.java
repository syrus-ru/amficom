/*-
 * $$Id: MapUtils.java,v 1.3 2005/09/30 16:08:36 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map;

import java.util.HashSet;
import java.util.Set;

import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.3 $, $Date: 2005/09/30 16:08:36 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
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
