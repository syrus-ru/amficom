/*
 * $Id: DatabaseResourceObjectLoader.java,v 1.17 2005/04/22 17:10:51 arseniy Exp $
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

import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.SessionContext;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.resource.corba.ImageResource_TransferablePackage.ImageResourceDataPackage.ImageResourceSort;

/**
 * @version $Revision: 1.17 $, $Date: 2005/04/22 17:10:51 $
 * @author $Author: arseniy $
 * @module resource_v1
 */
public class DatabaseResourceObjectLoader extends DatabaseObjectLoader implements ResourceObjectLoader {

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
				throw new IllegalDataException("ResourceObjectLoader.loadImageResource | wrong sort" + sort); //$NON-NLS-1$
		}
	}





	public Set loadImageResources(Set ids) throws ApplicationException {
		ImageResourceDatabase database = ResourceDatabaseContext.getImageResourceDatabase();
		return super.retrieveFromDatabase(database, ids);
	}





	public Set loadImageResourcesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		ImageResourceDatabase database = ResourceDatabaseContext.getImageResourceDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
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





	public void delete(Identifier id) {
		short entityCode = id.getMajor();
		StorableObjectDatabase storableObjectDatabase = ResourceDatabaseContext.getDatabase(entityCode);
		if (storableObjectDatabase != null)
			storableObjectDatabase.delete(id);
	}

	public void delete(final Set identifiables) {
		if (identifiables == null || identifiables.isEmpty())
			return;
		/**
		 * @todo: use Trove collection instead java.util.Map
		 */
		final Map map = new HashMap();

		/**
		 * separate objects by kind of entity
		 */
		for (final Iterator identifiableIterator = identifiables.iterator(); identifiableIterator.hasNext();) {
			final Identifiable identifiable = (Identifiable) identifiableIterator.next();
			final Short entityCode = new Short(identifiable.getId().getMajor());
			Set entityObjects = (Set) map.get(entityCode);
			if (entityObjects == null) {
				entityObjects = new HashSet();
				map.put(entityCode, entityObjects);
			}
			entityObjects.add(identifiable);
		}

		for (final Iterator entityCodeIterator = map.keySet().iterator(); entityCodeIterator.hasNext();) {
			final Short entityCode = (Short) entityCodeIterator.next();
			final Set entityObjects = (Set) map.get(entityCode);
			final StorableObjectDatabase storableObjectDatabase = ResourceDatabaseContext.getDatabase(entityCode);
			if (storableObjectDatabase != null)
				storableObjectDatabase.delete(entityObjects);
		}
	}

}
