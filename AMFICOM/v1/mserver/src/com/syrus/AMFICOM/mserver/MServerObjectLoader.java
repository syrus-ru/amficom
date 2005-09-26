/*-
 * $Id: MServerObjectLoader.java,v 1.9 2005/09/26 14:28:03 arseniy Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.mserver;

import static com.syrus.AMFICOM.general.ObjectEntities.ANALYSIS_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.RESULT_CODE;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
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
import com.syrus.AMFICOM.mcm.corba.MCM;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.9 $, $Date: 2005/09/26 14:28:03 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mserver
 */
final class MServerObjectLoader extends DatabaseObjectLoader {
	private Identifier preferredMCMId;

	@Override
	public <T extends StorableObject> Set<T> loadStorableObjects(final Set<Identifier> ids) throws ApplicationException {
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
	public <T extends StorableObject> Set<T> loadStorableObjectsButIdsByCondition(final Set<Identifier> ids,
			final StorableObjectCondition condition) throws ApplicationException {
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

	private final <T extends StorableObject> Set<T> loadStorableObjectsCustom(final Set<Identifier> ids) throws ApplicationException {
		final Set<T> objects = super.loadStorableObjects(ids);

		final Set<Identifier> loadIds = Identifier.createSubtractionIdentifiers(ids, objects);
		if (loadIds.isEmpty()) {
			return objects;
		}

		final Set<T> loadedObjects = new HashSet<T>();

		if (this.preferredMCMId != null) {
			Log.debugMessage("MServerObjectLoader.loadStorableObjectsCustom | Trying to load from MCM '" + this.preferredMCMId + "'",
					Log.DEBUGLEVEL08);
			try {
				this.loadStorableObjectsFromMCM(this.preferredMCMId, loadIds, loadedObjects);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}

		if (!loadIds.isEmpty()) {
			Log.debugMessage("MServerObjectLoader.loadStorableObjectsCustom | Searching on all MCMs", Log.DEBUGLEVEL08);
			for (final Iterator<Identifier> it = MeasurementServer.getMCMIds().iterator(); it.hasNext() && !loadIds.isEmpty();) {
				final Identifier mcmId = it.next();
				if (this.preferredMCMId != null && mcmId.equals(this.preferredMCMId)) {
					continue;
				}
				try {
					this.loadStorableObjectsFromMCM(mcmId, loadIds, loadedObjects);
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
			}
		}

		if (!loadedObjects.isEmpty()) {
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
		final Set<T> loadedObjects = new HashSet<T>();
		for (final Identifier mcmId : MeasurementServer.getMCMIds()) {
			try {
				this.loadStorableObjectsButIdsByConditionFromMCM(mcmId, loadButIds, condition, loadedObjects);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}

		if (!loadedObjects.isEmpty()) {
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

	private final <T extends StorableObject> void loadStorableObjectsFromMCM(final Identifier mcmId,
			final Set<Identifier> loadIds,
			final Set<T> loadedObjects) throws ApplicationException {
		MCM mcmRef = null;
		try {
			mcmRef = MServerSessionEnvironment.getInstance().getMServerServantManager().getVerifiedMCMReference(mcmId);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			return;
		}

		Log.debugMessage("MServerObjectLoader.loadStorableObjectsFromMCM | Loading from MCM '" + mcmId + "' '"
				+ ObjectEntities.codeToString(StorableObject.getEntityCodeOfIdentifiables(loadIds)) + "'s for ids: " + loadIds,
				Log.DEBUGLEVEL10);

		final Set<T> mcmLoadedObjects = CORBAObjectLoader.loadStorableObjects(mcmRef, loadIds);

		Log.debugMessage("MServerObjectLoader.loadStorableObjectsFromMCM | Loaded: " + Identifier.createStrings(mcmLoadedObjects),
				Log.DEBUGLEVEL10);

		Identifier.subtractFromIdentifiers(loadIds, mcmLoadedObjects);
		loadedObjects.addAll(mcmLoadedObjects);
	}

	private final <T extends StorableObject> void loadStorableObjectsButIdsByConditionFromMCM(final Identifier mcmId,
			final Set<Identifier> loadButIds,
			final StorableObjectCondition condition,
			final Set<T> loadedObjects)
			throws ApplicationException {
		MCM mcmRef = null;
		try {
			mcmRef = MServerSessionEnvironment.getInstance().getMServerServantManager().getVerifiedMCMReference(mcmId);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			return;
		}

		Log.debugMessage("MServerObjectLoader.loadStorableObjectsButIdsByConditionFromMCM | Loading from MCM '" + mcmId + "' '"
				+ ObjectEntities.codeToString(condition.getEntityCode().shortValue()) + "'s but ids: " + loadButIds,
				Log.DEBUGLEVEL10);

		final Set<T> mcmLoadedObjects = CORBAObjectLoader.loadStorableObjectsButIdsByCondition(mcmRef, loadButIds, condition);

		Log.debugMessage("MServerObjectLoader.loadStorableObjectsButIdsByConditionFromMCM | Loaded: "
				+ Identifier.createStrings(mcmLoadedObjects), Log.DEBUGLEVEL10);

		Identifier.addToIdentifiers(loadButIds, mcmLoadedObjects);
		loadedObjects.addAll(mcmLoadedObjects);
	}

}
