/*
* $Id: ResourceObjectLoader.java,v 1.7 2005/02/15 08:13:16 bob Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.resource;

import java.util.Collection;
import java.util.Set;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;



/**
 * @version $Revision: 1.7 $, $Date: 2005/02/15 08:13:16 $
 * @author $Author: bob $
 * @module resource_v1
 */
public interface ResourceObjectLoader {
	StorableObject loadImageResource(Identifier id) throws DatabaseException;

	Collection loadImageResources(Collection ids) throws DatabaseException;

	Collection loadImageResourcesButIds (StorableObjectCondition condition, Collection ids) throws DatabaseException;
	
	Set refresh(Set storableObjects) throws DatabaseException;
	
	void saveImageResource(AbstractImageResource abstractImageResource, boolean force) throws DatabaseException;

	void saveImageResources(Collection list, boolean force) throws DatabaseException;
	
	void delete(Identifier id) throws DatabaseException, CommunicationException;

	void delete(Collection ids) throws DatabaseException, CommunicationException;

}
