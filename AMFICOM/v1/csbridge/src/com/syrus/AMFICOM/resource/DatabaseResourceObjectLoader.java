/*
 * $Id: DatabaseResourceObjectLoader.java,v 1.5 2005/05/23 18:45:12 bass Exp $
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
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @version $Revision: 1.5 $, $Date: 2005/05/23 18:45:12 $
 * @author $Author: bass $
 * @module csbridge_v1
 */
public class DatabaseResourceObjectLoader extends DatabaseObjectLoader implements ResourceObjectLoader {
	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.resource.ResourceObjectLoader#loadImageResources(java.util.Set)
	 */
	public Set loadImageResources(final Set ids) throws ApplicationException {
		ImageResourceDatabase database = (ImageResourceDatabase) ResourceDatabaseContext.getDatabase(ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}

	/**
	 * @param condition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.resource.ResourceObjectLoader#loadImageResourcesButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set loadImageResourcesButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		ImageResourceDatabase database = (ImageResourceDatabase) ResourceDatabaseContext.getDatabase(ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	/**
	 * @param list
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.resource.ResourceObjectLoader#saveImageResources(java.util.Set, boolean)
	 */
	public void saveImageResources(final Set list, boolean force) throws ApplicationException {
		ImageResourceDatabase database = (ImageResourceDatabase) ResourceDatabaseContext.getDatabase(ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE);
		database.update(list, userId, force ? StorableObjectDatabase.UPDATE_FORCE
				: StorableObjectDatabase.UPDATE_CHECK);
	}

	/**
	 * @param storableObjects
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.resource.ResourceObjectLoader#refresh(java.util.Set)
	 */
	public Set refresh(final Set storableObjects) throws ApplicationException {
		if (storableObjects.isEmpty())
			return Collections.EMPTY_SET;

		short entityCode = ((StorableObject) storableObjects.iterator().next()).getId().getMajor();

		StorableObjectDatabase database = ResourceDatabaseContext.getDatabase(entityCode);

		if (database != null)
			return database.refresh(storableObjects);

		return Collections.EMPTY_SET;
	}

	/**
	 * @param identifiables
	 * @see com.syrus.AMFICOM.resource.ResourceObjectLoader#delete(java.util.Set)
	 */
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
