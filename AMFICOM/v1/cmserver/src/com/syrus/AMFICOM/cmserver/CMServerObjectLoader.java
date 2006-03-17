/*
 * $Id: CMServerObjectLoader.java,v 1.21.2.2 2006/03/17 12:33:05 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.cmserver;

import static com.syrus.AMFICOM.general.ErrorMessages.ILLEGAL_ENTITY_CODE;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ObjectEntities.ANALYSISRESULTPARAMETER_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.ANALYSIS_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTRESULTPARAMETER_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENT_CODE;

import java.util.Collections;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBAObjectLoader;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.21.2.2 $, $Date: 2006/03/17 12:33:05 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module cmserver
 * @todo Implement refresh (i. e. - method {@link com.syrus.AMFICOM.general.ObjectLoader#getRemoteVersions(Set)})
 * with timeout checking, using field {@link #refreshTimeout}
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
		assert ids != null: NON_NULL_EXPECTED;
		if (ids.isEmpty()) {
			return Collections.emptySet();
		}

		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		assert ObjectEntities.isEntityCodeValid(entityCode) : ILLEGAL_ENTITY_CODE;
		switch (entityCode) {
			case MEASUREMENT_CODE:
			case ANALYSIS_CODE:
			case MEASUREMENTRESULTPARAMETER_CODE:
			case ANALYSISRESULTPARAMETER_CODE:
				return this.loadStorableObjectsCustom(ids);
			default:
				return super.loadStorableObjects(ids);
		}
	}

	@Override
	public final <T extends StorableObject> Set<T> loadStorableObjectsButIdsByCondition(final Set<Identifier> ids,
			final StorableObjectCondition condition) throws ApplicationException {
		assert ids != null && condition != null: NON_NULL_EXPECTED;

		final short entityCode = condition.getEntityCode().shortValue();
		assert ids.isEmpty() || entityCode == StorableObject.getEntityCodeOfIdentifiables(ids);
		assert ObjectEntities.isEntityCodeValid(entityCode) : ILLEGAL_ENTITY_CODE;
		switch (entityCode) {
			case MEASUREMENT_CODE:
			case ANALYSIS_CODE:
			case MEASUREMENTRESULTPARAMETER_CODE:
			case ANALYSISRESULTPARAMETER_CODE:
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
			Log.errorMessage(ae);
		}

		if (loadedObjects != null && !loadedObjects.isEmpty()) {
			objects.addAll(loadedObjects);
			try {
				final short entityCode = StorableObject.getEntityCodeOfIdentifiables(loadedObjects);
				final StorableObjectDatabase<T> database = DatabaseContext.getDatabase(entityCode);
				assert (database != null) : NON_NULL_EXPECTED;
				database.save(loadedObjects);
			}
			catch (ApplicationException ae) {
				Log.errorMessage(ae);
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
			Log.errorMessage(ae);
		}

		if (loadedObjects != null && !loadedObjects.isEmpty()) {
			objects.addAll(loadedObjects);
			try {
				final short entityCode = StorableObject.getEntityCodeOfIdentifiables(loadedObjects);
				final StorableObjectDatabase<T> database = DatabaseContext.getDatabase(entityCode);
				assert (database != null) : NON_NULL_EXPECTED;
				database.save(loadedObjects);
			}
			catch (ApplicationException ae) {
				Log.errorMessage(ae);
			}
		}

		return objects;
	}

}
