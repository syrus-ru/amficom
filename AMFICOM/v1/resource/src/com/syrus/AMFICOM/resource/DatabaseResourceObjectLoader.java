/*
 * $Id: DatabaseResourceObjectLoader.java,v 1.12 2005/04/05 08:56:43 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.resource;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.SessionContext;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.resource.corba.ImageResource_TransferablePackage.ImageResourceDataPackage.ImageResourceSort;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.12 $, $Date: 2005/04/05 08:56:43 $
 * @author $Author: arseniy $
 * @module resource_v1
 */
public class DatabaseResourceObjectLoader implements ResourceObjectLoader {

	public StorableObject loadImageResource(Identifier id) throws ApplicationException {
		StorableObject storableObject;
		ImageResourceDatabase database = ResourceDatabaseContext.getImageResourceDatabase();
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
				throw new RetrieveObjectException("ResourceObjectLoader.loadImageResource | wrong sort" + sort); //$NON-NLS-1$
		}
	}





	public Set loadImageResources(Set ids) throws RetrieveObjectException {
		ImageResourceDatabase database = ResourceDatabaseContext.getImageResourceDatabase();
		Set set = Collections.EMPTY_SET;
		try {
			set = database.retrieveByIdsByCondition(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadImageResources | Illegal Storable Object: " + e.getMessage()); //$NON-NLS-1$
			throw new RetrieveObjectException("DatabaseMeasumentObjectLoader.loadImageResources | Illegal Storable Object: " + e.getMessage()); //$NON-NLS-1$
		}
		return set;
	}





	public Set loadImageResourcesButIds(StorableObjectCondition condition, Set ids) throws RetrieveObjectException {
		ImageResourceDatabase database = ResourceDatabaseContext.getImageResourceDatabase();
		Set list = Collections.EMPTY_SET;
		try {
			list = database.retrieveButIdsByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("ResourceObjectLoader.loadImageResourcesButIds | IllegalDataException: " + e.getMessage()); //$NON-NLS-1$
			throw new RetrieveObjectException("ResourceObjectLoader.loadImageResourcesButIds | IllegalDataException: " + e.getMessage()); //$NON-NLS-1$
		}
		return list;
	}






	public void saveImageResource(AbstractImageResource abstractImageResource, boolean force) throws ApplicationException {
		ImageResourceDatabase database = ResourceDatabaseContext.getImageResourceDatabase();
		database.update(abstractImageResource, SessionContext.getAccessIdentity().getUserId(), force
				? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveImageResources(Set list, boolean force) throws ApplicationException {
		ImageResourceDatabase database = ResourceDatabaseContext.getImageResourceDatabase();
		database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
				: StorableObjectDatabase.UPDATE_CHECK);
	}




	public Set refresh(Set storableObjects) throws ApplicationException {
		if (storableObjects.isEmpty())
			return Collections.EMPTY_SET;

		short entityCode = ((StorableObject) storableObjects.iterator().next()).getId().getMajor();

		StorableObjectDatabase database = ResourceDatabaseContext.getDatabase(entityCode);

		if (database != null)
			return database.refresh(storableObjects);

		return Collections.EMPTY_SET;
	}





	public void delete(Identifier id) throws IllegalDataException {
		short entityCode = id.getMajor();
		StorableObjectDatabase storableObjectDatabase = ResourceDatabaseContext.getDatabase(entityCode);
		if (storableObjectDatabase != null)
			storableObjectDatabase.delete(id);
	}

	public void delete(Set objects) throws IllegalDataException {
		if (objects == null || objects.isEmpty())
			return;
		/**
		 * TODO: use Trove collection instead java.util.Map
		 */
		Map map = new HashMap();

		/**
		 * separate objects by kind of entity
		 */
		Set entityObjects;
		Short entityCode;
		for (Iterator it = objects.iterator(); it.hasNext();) {
			Object object = it.next();
			Identifier identifier = null;
			if (object instanceof Identifier)
				identifier = (Identifier) object;
			else
				if (object instanceof Identifiable)
					identifier = ((Identifiable) object).getId();
				else
					throw new IllegalDataException("DatabaseResourceObjectLoader.delete | Object "
							+ object.getClass().getName() + " isn't Identifier or Identifiable");

			entityCode = new Short(identifier.getMajor());
			entityObjects = (Set) map.get(entityCode);
			if (entityObjects == null) {
				entityObjects = new HashSet();
				map.put(entityCode, entityObjects);
			}
			entityObjects.add(object);
		}

		StorableObjectDatabase storableObjectDatabase;
		for (Iterator it = map.keySet().iterator(); it.hasNext();) {
			entityCode = (Short) it.next();
			entityObjects = (Set) map.get(entityCode);
			storableObjectDatabase = ResourceDatabaseContext.getDatabase(entityCode);
			if (storableObjectDatabase != null)
				storableObjectDatabase.delete(entityObjects);
		}
	}

}
