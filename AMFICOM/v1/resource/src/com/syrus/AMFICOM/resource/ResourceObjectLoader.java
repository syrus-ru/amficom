/*
 * $Id: ResourceObjectLoader.java,v 1.1 2004/12/27 16:16:56 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.resource;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.resource.corba.ImageResource_TransferablePackage.ImageResourceDataPackage.ImageResourceSort;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2004/12/27 16:16:56 $
 * @author $Author: max $
 * @module resource_v1
 */
public class ResourceObjectLoader {
	
	public StorableObject loadImageResource(Identifier id) throws DatabaseException {
		StorableObject storableObject;
		ImageResourceDatabase database = (ImageResourceDatabase)ResourceDatabaseContext.getImageResourceDatabase();
		try {
			int sort = database.getSort(id);
			switch (sort) {
			case ImageResourceSort._BITMAP:
				storableObject = new BitmapImageResource(id);
				return storableObject;
			case ImageResourceSort._FILE:
				storableObject = new FileImageResource(id);
				return storableObject;
			case ImageResourceSort._SCHEME:
				storableObject = new SchemeImageResource(id);
				return storableObject;
			default:
				throw new RetrieveObjectException("ResourceObjectLoader.loadImageResource | wrong sort" + sort);
			}			
		} catch (RetrieveObjectException e) {
			Log.errorMessage("ResourceObjectLoader.loadImageResource | RetrieveObjectException: " + e.getMessage());
			throw new DatabaseException("ResourceObjectLoader.loadImageResource | RetrieveObjectException: " + e.getMessage());
		} catch (ObjectNotFoundException e) {
			Log.errorMessage("ResourceObjectLoader.loadImageResource | ObjectNotFoundException: " + e.getMessage());
			throw new DatabaseException("ResourceObjectLoader.loadImageResource | ObjectNotFoundException: " + e.getMessage());
		}
	}
	
	public List loadImageResources(List ids) throws DatabaseException {
		ImageResourceDatabase database = (ImageResourceDatabase)ResourceDatabaseContext.getImageResourceDatabase();
		List storableObjects = new LinkedList();
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			int sort = database.getSort(id);
			switch (sort) {
			case ImageResourceSort._BITMAP:
				BitmapImageResource bitmapImageResource = new BitmapImageResource(id);
				storableObjects.add(bitmapImageResource);
				break;				
			case ImageResourceSort._FILE:
				FileImageResource fileImageResource = new FileImageResource(id);
				storableObjects.add(fileImageResource);
				break;
			case ImageResourceSort._SCHEME:
				SchemeImageResource schemeImageResource = new SchemeImageResource(id);
				storableObjects.add(schemeImageResource);
				break;
			default:
				throw new RetrieveObjectException("ResourceObjectLoader.loadImageResource | wrong sort" + sort);
			}	
		}
		return storableObjects;
	}	
}
