/*
 * $Id: DatabaseResourceObjectLoader.java,v 1.1 2005/01/17 17:01:33 max Exp $
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
 * @version $Revision: 1.1 $, $Date: 2005/01/17 17:01:33 $
 * @author $Author: max $
 * @module resource_v1
 */
public class DatabaseResourceObjectLoader {
	
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
		List list = Collections.EMPTY_LIST;
		try {
			list = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadImageResources | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.loadImageResources | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}
	
	public List loadImageResourcesButIds (StorableObjectCondition condition, List ids) throws DatabaseException {
		ImageResourceDatabase database = (ImageResourceDatabase)ResourceDatabaseContext.getImageResourceDatabase();
		List list = Collections.EMPTY_LIST;
		try {
			list = database.retrieveByCondition(ids, condition);
		} catch (RetrieveObjectException e) {
			Log.errorMessage("ResourceObjectLoader.loadImageResourcesButIds | RetrieveObjectException: " + e.getMessage());
			throw new DatabaseException("ResourceObjectLoader.loadImageResourcesButIds | RetrieveObjectException: " + e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("ResourceObjectLoader.loadImageResourcesButIds | IllegalDataException: " + e.getMessage());
			throw new DatabaseException("ResourceObjectLoader.loadImageResourcesButIds | IllegalDataException: " + e.getMessage());
		}
		return list;
	}
	
	public Set refresh(Set storableObjects) throws DatabaseException {
		if (storableObjects.isEmpty())
			return Collections.EMPTY_SET;

		short entityCode = ((StorableObject) storableObjects.iterator().next()).getId().getMajor();

		StorableObjectDatabase database = null;
		try {
			switch (entityCode) {
				case ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE:
					database = ResourceDatabaseContext.getImageResourceDatabase();
					break;
				default:
					Log.errorMessage("ResourceObjectLoader.refresh | Unknown entity: " + entityCode);                
			}
			if (database != null)
				return database.refresh(storableObjects);

      return Collections.EMPTY_SET;
		}
		catch (DatabaseException e) {
			Log.errorMessage("ResourceObjectLoader.refresh | DatabaseException: " + e.getMessage());
			throw new DatabaseException("ResourceObjectLoader.refresh | DatabaseException: " + e.getMessage(), e);
		}
	}
	
	public void saveImageResource(AbstractImageResource abstractImageResource, boolean force) throws DatabaseException {
		ImageResourceDatabase database = (ImageResourceDatabase)ResourceDatabaseContext.getImageResourceDatabase();
		try {
			database.update(abstractImageResource, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("ResourceObjectLoader.saveImageResource | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("ResourceObjectLoader.saveImageResource | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("ResourceObjectLoader.saveImageResource | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("ResourceObjectLoader.saveImageResource | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("ResourceObjectLoader.saveImageResource | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("ResourceObjectLoader.saveImageResource | VersionCollisionException: " + e.getMessage());
		}
	}
	
	public void saveImageResources(List list, boolean force) throws DatabaseException {
		ImageResourceDatabase database = (ImageResourceDatabase)ResourceDatabaseContext.getImageResourceDatabase();
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("ResourceObjectLoader.saveImageResources | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("ResourceObjectLoader.saveImageResources | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("ResourceObjectLoader.saveImageResources | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("ResourceObjectLoader.saveImageResources | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("ResourceObjectLoader.saveImageResources | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("ResourceObjectLoader.saveImageResources | VersionCollisionException: " + e.getMessage());
		}
	}
	
	public void delete(Identifier id) throws DatabaseException {
		delete(id, null);		
	}

	public void delete(List ids) throws DatabaseException {
		if (ids == null || ids.isEmpty())
			return;
		/**
		 * TODO: use Trove collection instead java.util.Map 
		 */
		Map map = new HashMap();

		/**
		 * separate objects by kind of entity 
		 */
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Object object = it.next();
			Identifier identifier = null;
			if (object instanceof Identifier)
				identifier = (Identifier)object;
			else
				if (object instanceof Identified)
					identifier = ((Identified)object).getId();
				else
					throw new DatabaseException("ResourceObjectLoader.delete | Object " + object.getClass().getName() + " isn't Identifier or Identified");
			Short entityCode = new Short(identifier.getMajor());
			List list = (List)map.get(entityCode);
			if (list == null) {
				list = new LinkedList();
				map.put(entityCode, list);
			}
			list.add(object);

		}

		for (Iterator it = map.keySet().iterator(); it.hasNext();) {
			Short entityCode = (Short) it.next();
			List list = (List)map.get(entityCode);
			delete(null, list);
		}

	}

	/**
	 * delete storable objects of one kind of entity
	 * @param id
	 * @param ids
	 * @throws DatabaseException
	 */
	private void delete(Identifier id, List ids) throws DatabaseException {
		short entityCode = (id != null) ? id.getMajor() : 0;
		if (id == null) {
			if (ids.isEmpty())
				return;
			Object obj = ids.iterator().next();
			if (obj instanceof Identifier)
				entityCode = ((Identifier)obj).getMajor();
			else
				if (obj instanceof Identified)
					entityCode = ((Identified)obj).getId().getMajor();
		}
		try {
			StorableObjectDatabase database = null;
			switch (entityCode) {
				case ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE:
					database = ResourceDatabaseContext.getImageResourceDatabase();
					break;
				default:
					Log.errorMessage("ResourceObjectLoader.delete | Unknown entity: " + entityCode);                
			}

			if (database != null) {
				if (id != null)
					database.delete(id);
				else
					if (ids != null && !ids.isEmpty()) {
						database.delete(ids);
					}
			}
		}
		catch (IllegalDataException e) {
			Log.errorMessage("ResourceObjectLoader.delete | DatabaseException: " + e.getMessage());
			throw new DatabaseException("ResourceObjectLoader.delete | DatabaseException: " + e.getMessage());
		}
	}
}
