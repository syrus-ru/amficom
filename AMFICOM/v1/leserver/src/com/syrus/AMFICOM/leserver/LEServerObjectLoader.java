/*
 * $Id: LEServerObjectLoader.java,v 1.1 2006/03/30 12:11:12 bass Exp $
 * 
 * Copyright © 2004-2006 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.leserver;

import static com.syrus.AMFICOM.general.ObjectEntities.ANALYSIS_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.RESULT_CODE;

import java.util.Collections;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBAObjectLoader;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @version $Revision: 1.1 $, $Date: 2006/03/30 12:11:12 $
 * @author $Author: bass $
 * @author Andrew ``Bass'' Shcheglov
 * @module leserver
 */
final class LEServerObjectLoader extends DatabaseObjectLoader {
	private CORBAObjectLoader corbaObjectLoader;

	protected LEServerObjectLoader(final LEServerServantManager leServerServantManager) {
		this.corbaObjectLoader = new CORBAObjectLoader(leServerServantManager);
	}

	@Override
	public final <T extends StorableObject> Set<T> loadStorableObjects(
			final Set<Identifier> ids)
	throws ApplicationException {
		assert ids != null: ErrorMessages.NON_NULL_EXPECTED;
		if (ids.isEmpty()) {
			return Collections.emptySet();
		}

		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE;
		switch (entityCode) {
			case MEASUREMENT_CODE:
			case ANALYSIS_CODE:
			case RESULT_CODE:
				return this.loadStorableObjectsCustom(ids);
			default:
				return super.loadStorableObjects(ids);
		}
	}

	@Override
	public final <T extends StorableObject> Set<T> loadStorableObjectsButIdsByCondition(
			final Set<Identifier> ids,
			final StorableObjectCondition condition)
	throws ApplicationException {
		assert ids != null && condition != null: ErrorMessages.NON_NULL_EXPECTED;

		final short entityCode = condition.getEntityCode().shortValue();
		assert ids.isEmpty() || entityCode == StorableObject.getEntityCodeOfIdentifiables(ids);
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE;
		switch (entityCode) {
			case MEASUREMENT_CODE:
			case ANALYSIS_CODE:
			case RESULT_CODE:
				return this.loadStorableObjectsButIdsByConditionCustom(ids, condition);
			default:
				return super.loadStorableObjectsButIdsByCondition(ids, condition);
		}
	}

	private final <T extends StorableObject> Set<T> loadStorableObjectsCustom(
			final Set<Identifier> ids)
	throws ApplicationException {
		final Set<T> objects = super.loadStorableObjects(ids);

		final Set<Identifier> loadIds = Identifier.createSubtractionIdentifiers(ids, objects);
		if (loadIds.isEmpty()) {
			return objects;
		}

		Set<T> loadedObjects = this.corbaObjectLoader.loadStorableObjects(loadIds);

		if (loadedObjects != null && !loadedObjects.isEmpty()) {
			objects.addAll(loadedObjects);
			final short entityCode = StorableObject.getEntityCodeOfIdentifiables(loadedObjects);
			final StorableObjectDatabase<T> database = DatabaseContext.getDatabase(entityCode);
			assert (database != null) : ErrorMessages.NON_NULL_EXPECTED;
			database.save(loadedObjects);
		}

		return objects;
	}

	private final <T extends StorableObject> Set<T> loadStorableObjectsButIdsByConditionCustom(
			final Set<Identifier> ids,
			final StorableObjectCondition condition)
	throws ApplicationException {
		final Set<T> objects = super.loadStorableObjectsButIdsByCondition(ids, condition);

		final Set<Identifier> loadButIds = Identifier.createSumIdentifiers(ids, objects);

		Set<T> loadedObjects = this.corbaObjectLoader.loadStorableObjectsButIdsByCondition(loadButIds, condition);

		if (loadedObjects != null && !loadedObjects.isEmpty()) {
			objects.addAll(loadedObjects);
			final short entityCode = StorableObject.getEntityCodeOfIdentifiables(loadedObjects);
			final StorableObjectDatabase<T> database = DatabaseContext.getDatabase(entityCode);
			assert (database != null) : ErrorMessages.NON_NULL_EXPECTED;
			database.save(loadedObjects);
		}

		return objects;
	}
}
