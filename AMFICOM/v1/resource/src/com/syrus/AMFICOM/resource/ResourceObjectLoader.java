/*
* $Id: ResourceObjectLoader.java,v 1.5 2005/01/18 10:24:45 bob Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.resource;

import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;



/**
 * @version $Revision: 1.5 $, $Date: 2005/01/18 10:24:45 $
 * @author $Author: bob $
 * @module resource_v1
 */
public interface ResourceObjectLoader {
	StorableObject loadImageResource(Identifier id) throws DatabaseException;

	List loadImageResources(List ids) throws DatabaseException;

	List loadImageResourcesButIds (StorableObjectCondition condition, List ids) throws DatabaseException;
	
	Set refresh(Set storableObjects) throws DatabaseException;
	
	void saveImageResource(AbstractImageResource abstractImageResource, boolean force) throws DatabaseException;

	void saveImageResources(List list, boolean force) throws DatabaseException;
	
	void delete(Identifier id) throws DatabaseException;

	void delete(List ids) throws DatabaseException;

}
