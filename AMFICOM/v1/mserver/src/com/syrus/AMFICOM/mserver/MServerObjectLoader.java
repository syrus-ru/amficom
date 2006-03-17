/*-
 * $Id: MServerObjectLoader.java,v 1.19.2.2 2006/03/17 12:33:05 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.mserver;

import static com.syrus.AMFICOM.general.ObjectEntities.ANALYSISRESULTPARAMETER_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.ANALYSIS_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTRESULTPARAMETER_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENT_CODE;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBAObjectLoader;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ServerConnectionManager;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.19.2.2 $, $Date: 2006/03/17 12:33:05 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mserver
 */
final class MServerObjectLoader extends DatabaseObjectLoader {
	private Map<Identifier, CORBAObjectLoader> mcmObjectLoaders;
	private Identifier preferredMCMId;

	public MServerObjectLoader() {
		this.mcmObjectLoaders = new HashMap<Identifier, CORBAObjectLoader>();
	}

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
			case MEASUREMENTRESULTPARAMETER_CODE:
			case ANALYSISRESULTPARAMETER_CODE:
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

		final Set<T> loadedObjects = new HashSet<T>();

		if (this.preferredMCMId != null) {
			Log.debugMessage("Trying to load from MCM '" + this.preferredMCMId + "'",
					Log.DEBUGLEVEL08);
			try {
				this.loadStorableObjectsFromMCM(this.preferredMCMId, loadIds, loadedObjects);
			}
			catch (ApplicationException ae) {
				Log.errorMessage(ae);
			}
		}

		if (!loadIds.isEmpty()) {
			Log.debugMessage("Searching on all MCMs", Log.DEBUGLEVEL08);
			for (final Iterator<Identifier> it = MeasurementServer.getMCMIds().iterator(); it.hasNext() && !loadIds.isEmpty();) {
				final Identifier mcmId = it.next();
				if (this.preferredMCMId != null && mcmId.equals(this.preferredMCMId)) {
					continue;
				}
				try {
					this.loadStorableObjectsFromMCM(mcmId, loadIds, loadedObjects);
				}
				catch (ApplicationException ae) {
					Log.errorMessage(ae);
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
				Log.errorMessage(ae);
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
				Log.errorMessage(ae);
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
				Log.errorMessage(ae);
			}
		}

		return objects;
	}

	private final <T extends StorableObject> void loadStorableObjectsFromMCM(final Identifier mcmId,
			final Set<Identifier> loadIds,
			final Set<T> loadedObjects) throws ApplicationException {
		Log.debugMessage("Loading from MCM '" + mcmId + "' '"
				+ ObjectEntities.codeToString(StorableObject.getEntityCodeOfIdentifiables(loadIds)) + "'s for ids: " + loadIds,
				Log.DEBUGLEVEL10);

		final CORBAObjectLoader corbaObjectLoader = this.getCORBAObjectLoaderForMCMId(mcmId);
		final Set<T> mcmLoadedObjects = corbaObjectLoader.loadStorableObjects(loadIds);

		Log.debugMessage("Loaded: " + Identifier.toString(mcmLoadedObjects), Log.DEBUGLEVEL10);

		Identifier.subtractFromIdentifiers(loadIds, mcmLoadedObjects);
		loadedObjects.addAll(mcmLoadedObjects);
	}

	private final <T extends StorableObject> void loadStorableObjectsButIdsByConditionFromMCM(final Identifier mcmId,
			final Set<Identifier> loadButIds,
			final StorableObjectCondition condition,
			final Set<T> loadedObjects)
			throws ApplicationException {
		Log.debugMessage("Loading from MCM '" + mcmId + "' '"
				+ ObjectEntities.codeToString(condition.getEntityCode().shortValue()) + "'s but ids: " + loadButIds,
				Log.DEBUGLEVEL10);

		final CORBAObjectLoader corbaObjectLoader = this.getCORBAObjectLoaderForMCMId(mcmId);
		final Set<T> mcmLoadedObjects = corbaObjectLoader.loadStorableObjectsButIdsByCondition(loadButIds, condition);

		Log.debugMessage("Loaded: " + Identifier.toString(mcmLoadedObjects), Log.DEBUGLEVEL10);

		Identifier.addToIdentifiers(loadButIds, mcmLoadedObjects);
		loadedObjects.addAll(mcmLoadedObjects);
	}


	private class MCMConnectionManager implements ServerConnectionManager {
		private Identifier mcmId;

		MCMConnectionManager(final Identifier mcmId) {
			this.mcmId = mcmId;
		}

		public CommonServer getServerReference() throws CommunicationException {
			return MServerSessionEnvironment.getInstance().getMServerServantManager().getVerifiedMCMReference(this.mcmId);
		}

		public CORBAServer getCORBAServer() {
			return MServerSessionEnvironment.getInstance().getMServerServantManager().getCORBAServer();
		}
	}

	private CORBAObjectLoader getCORBAObjectLoaderForMCMId(final Identifier mcmId) {
		CORBAObjectLoader corbaObjectLoader = this.mcmObjectLoaders.get(mcmId);
		if (corbaObjectLoader != null) {
			return corbaObjectLoader;
		}

		final ServerConnectionManager serverConnectionManager = new MCMConnectionManager(mcmId);

		corbaObjectLoader = new CORBAObjectLoader(serverConnectionManager);
		this.mcmObjectLoaders.put(mcmId, corbaObjectLoader);
		return corbaObjectLoader;
	}

}
