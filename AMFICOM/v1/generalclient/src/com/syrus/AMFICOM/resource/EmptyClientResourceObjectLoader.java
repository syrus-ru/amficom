/*
 * $Id: EmptyClientResourceObjectLoader.java,v 1.4 2005/02/15 10:40:27 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.resource;

import java.util.Collections;
import java.util.Collection;
import java.util.Set;

import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.4 $, $Date: 2005/02/15 10:40:27 $
 * @author $Author: bob $
 * @module generalclient_v1
 */
public class EmptyClientResourceObjectLoader implements ResourceObjectLoader{
	
	public StorableObject loadImageResource(Identifier id) throws DatabaseException 
	{
		return null;
	}
	
	public Collection loadImageResources(Collection ids) throws DatabaseException {
		return Collections.EMPTY_LIST;
	}
	
	public Collection loadImageResourcesButIds (StorableObjectCondition condition, Collection ids) throws DatabaseException {
		return Collections.EMPTY_LIST;
	}
	
	public Set refresh(Set storableObjects) throws DatabaseException {
      return Collections.EMPTY_SET;
	}
	
	public void saveImageResource(AbstractImageResource abstractImageResource, boolean force) throws DatabaseException {
	}
	
	public void saveImageResources(Collection list, boolean force) throws DatabaseException {
	}
	
	public void delete(Identifier id) throws DatabaseException {
	}

	public void delete(Collection ids) throws DatabaseException {
	}

}
