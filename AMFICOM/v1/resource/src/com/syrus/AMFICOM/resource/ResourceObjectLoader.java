/*
* $Id: ResourceObjectLoader.java,v 1.8 2005/02/24 16:10:22 bob Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.resource;

import java.util.Collection;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;



/**
 * @version $Revision: 1.8 $, $Date: 2005/02/24 16:10:22 $
 * @author $Author: bob $
 * @module resource_v1
 */
public interface ResourceObjectLoader {
	StorableObject loadImageResource(Identifier id) throws ApplicationException;

	Collection loadImageResources(Collection ids) throws ApplicationException;

	Collection loadImageResourcesButIds (StorableObjectCondition condition, Collection ids) throws ApplicationException;
	
	Set refresh(Set storableObjects) throws ApplicationException;
	
	void saveImageResource(AbstractImageResource abstractImageResource, boolean force) throws ApplicationException;

	void saveImageResources(Collection list, boolean force) throws ApplicationException;
	
	void delete(Identifier id) throws IllegalDataException;

	void delete(Collection ids) throws IllegalDataException;

}
