/*
 * $Id: EmptyClientResourceObjectLoader.java,v 1.2 2005/01/18 10:26:04 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.resource;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.2 $, $Date: 2005/01/18 10:26:04 $
 * @author $Author: bob $
 * @module resource_v1
 */
public class EmptyClientResourceObjectLoader implements ResourceObjectLoader{
	
	public StorableObject loadImageResource(Identifier id) throws DatabaseException 
	{
		return null;
	}
	
	public List loadImageResources(List ids) throws DatabaseException {
		return Collections.EMPTY_LIST;
	}
	
	public List loadImageResourcesButIds (StorableObjectCondition condition, List ids) throws DatabaseException {
		return Collections.EMPTY_LIST;
	}
	
	public Set refresh(Set storableObjects) throws DatabaseException {
      return Collections.EMPTY_SET;
	}
	
	public void saveImageResource(AbstractImageResource abstractImageResource, boolean force) throws DatabaseException {
	}
	
	public void saveImageResources(List list, boolean force) throws DatabaseException {
	}
	
	public void delete(Identifier id) throws DatabaseException {
	}

	public void delete(List ids) throws DatabaseException {
	}

}
