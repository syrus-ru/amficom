/*
 * $Id: CMServerObjectLoader.java,v 1.5 2005/07/28 17:04:56 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.cmserver;

import static com.syrus.AMFICOM.general.ObjectEntities.ANALYSIS_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.EVALUATION_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.RESULT_CODE;

import java.util.Collections;
import java.util.Map;
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
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.5 $, $Date: 2005/07/28 17:04:56 $
 * @author $Author: arseniy $
 * @module cmserver_v1
 */
final class CMServerObjectLoader extends DatabaseObjectLoader {
	private CORBAObjectLoader corbaObjectLoader;
	private long refreshTimeout;
	private long lastRefreshTime;

	protected CMServerObjectLoader(final CMServerServantManager cmServerServantManager, final long refreshTimeout) {
		this.corbaObjectLoader = new CORBAObjectLoader(cmServerServantManager);
		this.refreshTimeout = refreshTimeout;
		this.lastRefreshTime = System.currentTimeMillis();
	}

	@Override
	public final <T extends StorableObject> Set<T> loadStorableObjects(final Set<Identifier> ids) throws ApplicationException {
		assert ids != null: ErrorMessages.NON_NULL_EXPECTED;
		if (ids.isEmpty()) {
			return Collections.emptySet();
		}

		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE;
		switch (entityCode) {
			case MEASUREMENT_CODE:
			case ANALYSIS_CODE:
			case EVALUATION_CODE:
			case RESULT_CODE:
				return this.loadStorableObjectsCustom(ids);
			default:
				return super.loadStorableObjects(ids);
		}
	}

	@Override
	public final <T extends StorableObject> Set<T> loadStorableObjectsButIdsByCondition(final Set<Identifier> ids,
			final StorableObjectCondition condition) throws ApplicationException {
		assert ids != null && condition != null: ErrorMessages.NON_NULL_EXPECTED;

		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE;
		switch (entityCode) {
			case MEASUREMENT_CODE:
			case ANALYSIS_CODE:
			case EVALUATION_CODE:
			case RESULT_CODE:
				return this.loadStorableObjectsButIdsByConditionCustom(ids, condition);
			default:
				return super.loadStorableObjectsButIdsByCondition(ids, condition);
		}
	}

	private final <T extends StorableObject> Set<T> loadStorableObjectsCustom(final Set<Identifier> ids) throws ApplicationException {
		final Set<T> objects = super.loadStorableObjects(ids);

		final Set<Identifier> loadIds = Identifier.createSubtractionIdentifiers(ids, objects);
		if (loadIds.isEmpty()) {
			return objects;
		}

		Set<T> loadedObjects = null;
		try {
			loadedObjects = this.corbaObjectLoader.loadStorableObjects(loadIds);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
		}

		if (loadedObjects != null && !loadedObjects.isEmpty()) {
			objects.addAll(loadedObjects);
			try {
				final short entityCode = StorableObject.getEntityCodeOfIdentifiables(loadedObjects);
				final StorableObjectDatabase<T> database = DatabaseContext.getDatabase(entityCode);
				assert (database != null) : ErrorMessages.NON_NULL_EXPECTED;
				database.save(loadedObjects);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}

		return objects;
	}

	private final <T extends StorableObject> Set<T> loadStorableObjectsButIdsByConditionCustom(final Set<Identifier> ids,
			final StorableObjectCondition condition) throws ApplicationException {
		final Set<T> objects = super.loadStorableObjectsButIdsByCondition(ids, condition);

		final Set<Identifier> loadButIds = Identifier.createSumIdentifiers(ids, objects);

		Set<T> loadedObjects = null;
		try {
			loadedObjects = this.corbaObjectLoader.loadStorableObjectsButIdsByCondition(loadButIds, condition);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
		}

		if (loadedObjects != null && !loadedObjects.isEmpty()) {
			objects.addAll(loadedObjects);
			try {
				final short entityCode = StorableObject.getEntityCodeOfIdentifiables(loadedObjects);
				final StorableObjectDatabase<T> database = DatabaseContext.getDatabase(entityCode);
				assert (database != null) : ErrorMessages.NON_NULL_EXPECTED;
				database.save(loadedObjects);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}

		return objects;
	}

	@Override
	public final Set<Identifier> getOldVersionIds(final Map<Identifier, StorableObjectVersion> versionsMap) throws ApplicationException {
		if (System.currentTimeMillis() - this.lastRefreshTime <= this.refreshTimeout) {
			return Collections.emptySet();
		}
		return super.getOldVersionIds(versionsMap);
	}

}
