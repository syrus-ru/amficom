/*
 * $Id: EmptyClientResourceObjectLoader.java,v 1.1 2005/01/11 16:41:16 krupenn Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.resource;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identified;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.resource.corba.ImageResource_TransferablePackage.ImageResourceDataPackage.ImageResourceSort;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2005/01/11 16:41:16 $
 * @author $Author: krupenn $
 * @module resource_v1
 */
public class EmptyClientResourceObjectLoader extends ResourceObjectLoader{
	
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
