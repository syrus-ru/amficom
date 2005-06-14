/*
 * $Id: CORBAMServerObjectLoader.java,v 1.4 2005/06/14 11:26:44 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.mserver;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.ErrorCode;
import com.syrus.AMFICOM.mcm.corba.MCM;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.4 $, $Date: 2005/06/14 11:26:44 $
 * @author $Author: arseniy $
 * @module mserver_v1
 */
final class CORBAMServerObjectLoader {
	protected static Identifier preferredMCMId;
	protected static Object lock;

	static {
		lock = new Object();
	}

	private CORBAMServerObjectLoader() {
		//singleton
		assert false;
	}


	/**
	 * @author $Author: arseniy $
	 * @version $Revision: 1.4 $, $Date: 2005/06/14 11:26:44 $
	 * @see CORBAMServerObjectLoader#loadStorableObjects(short, Set, com.syrus.AMFICOM.mserver.CORBAMServerObjectLoader.TransmitProcedure)
	 * @module mserver_v1
	 */
	interface TransmitProcedure {
		IDLEntity[] transmitStorableObjects(final MCM mcmRef,
				final Identifier_Transferable loadIdsT[],
				final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException;
	}

	/**
	 * @author $Author: arseniy $
	 * @version $Revision: 1.4 $, $Date: 2005/06/14 11:26:44 $
	 * @see CORBAMServerObjectLoader#loadStorableObjectsButIdsByCondition(short, Set, StorableObjectCondition, com.syrus.AMFICOM.mserver.CORBAMServerObjectLoader.TransmitButIdsByConditionProcedure)
	 * @module mserver_v1
	 */
	interface TransmitButIdsByConditionProcedure {
		IDLEntity[] transmitStorableObjectsButIdsByCondition(final MCM mcmRef,
				final Identifier_Transferable loadButIdsT[],
				final StorableObjectCondition_Transferable conditionT,
				final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException;
	}



	/**
	 * Loads objects of type, specified by <code>entityCode</code>, with identifiers in set <code>ids</code>.
	 * At first loads from local database. Then, if there are identifiers in set <code>ids</code>,
	 * for which objects from database have not beel loaded, tries to load from MCM specified by <code>preferredMCMId</code>.
	 * If <code>preferredMCMId</code> is <code>null</code> or some of objects from set <code>ids</code> have not yet been loaded,
	 * searches on all MCMs, registered with this MServer.
	 * @param entityCode
	 * @param ids
	 * @param transmitProcedure
	 * @return <code>Set</code> of <code>StorableObject</code>
	 * @throws ApplicationException
	 */
	protected static final Set loadStorableObjects(final short entityCode, final Set ids, final TransmitProcedure transmitProcedure)
			throws ApplicationException {
		final Set objects = DatabaseObjectLoader.loadStorableObjects(ids);

		final Set loadIds = Identifier.createSubstractionIdentifiers(ids, objects);
		if (loadIds.isEmpty())
			return objects;

		final Set loadedObjects = new HashSet();

		if (preferredMCMId != null) {
			Log.debugMessage("CORBAMServerObjectLoader.loadStorableObjects | Trying to load from MCM '" + preferredMCMId + "'",
					Log.DEBUGLEVEL08);
			try {
				loadStorableObjectsFromMCM(preferredMCMId, entityCode, transmitProcedure, loadIds, loadedObjects);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}

		if (!loadIds.isEmpty()) {
			Log.debugMessage("CORBAMServerObjectLoader.loadStorableObjects | Searching on all MCMs", Log.DEBUGLEVEL08);
			final Set mcmIds = MeasurementServer.getMCMIds();
			for (final Iterator it = mcmIds.iterator(); it.hasNext() && !loadIds.isEmpty();) {
				final Identifier mcmId = (Identifier) it.next();
				try {
					loadStorableObjectsFromMCM(mcmId, entityCode, transmitProcedure, loadIds, loadedObjects);
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
			}
		}

		if (!loadedObjects.isEmpty()) {
			objects.addAll(loadedObjects);
			final StorableObjectDatabase database = DatabaseContext.getDatabase(entityCode);
			try {
				database.insert(loadedObjects);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}

		return objects;
	}

	/**
	 * Loads objects of type, specified by <code>entityCode</code>
	 * from MCM, specified by <code>mcmId</code>,
	 * using CORBA call, specified by <code>transmitProcedure</code>.
	 * Identifiers objects to load belong to set <code>loadIds</code>.
	 * After call identifiers of loaded objects are removed from <code>loadIds</code>
	 * and loaded objects are added to set <code>loadedObjects</code>.
	 * (Thus, parameters <code>loadIds</code> and <code>loadedObjects</code> are passed as &quot;inout&quot; arguments.)
	 * If it is impossible to obtain CORBA reference to MCM, simply returns.
	 * @param mcmId
	 * @param entityCode
	 * @param transmitProcedure
	 * @param loadIds
	 * @param loadedObjects
	 * @throws ApplicationException
	 */
	private static final void loadStorableObjectsFromMCM(final Identifier mcmId,
			final short entityCode,
			final TransmitProcedure transmitProcedure,
			final Set loadIds,
			final Set loadedObjects)
			throws ApplicationException {
		MCM mcmRef = null;
		try {
			mcmRef = MServerSessionEnvironment.getInstance().getMServerServantManager().getVerifiedMCMReference(mcmId);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			return;
		}

		/*	Just debug output -- nothing more*/
		Log.debugMessage("CORBAMServerObjectLoader.loadStorableObjectsFromMCM | Loading from MCM '" + mcmId + "'", Log.DEBUGLEVEL10);
		Log.debugMessage("CORBAMServerObjectLoader.loadStorableObjectsFromMCM | Objects '"
				+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode, Log.DEBUGLEVEL10);
		Log.debugMessage("CORBAMServerObjectLoader.loadStorableObjectsFromMCM | For set: " + loadIds, Log.DEBUGLEVEL10);
		/*	^Just debug output -- nothing more^*/

		final Identifier_Transferable[] loadIdsT = Identifier.createTransferables(loadIds);
		int numEfforts = 0;
		while (true) {
			try {
				final SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();
				final IDLEntity[] transferables = transmitProcedure.transmitStorableObjects(mcmRef, loadIdsT, sessionKeyT);

				final Set mcmLoadedObjects = StorableObjectPool.fromTransferables(entityCode, transferables, true);
				Identifier.substractFromIdentifiers(loadIds, mcmLoadedObjects);
				loadedObjects.addAll(mcmLoadedObjects);

				/*	Just debug output -- nothing more*/
				StringBuffer stringBuffer = new StringBuffer();
				for (final Iterator it = mcmLoadedObjects.iterator(); it.hasNext();) {
					final StorableObject storableObject = (StorableObject) it.next();
					if (stringBuffer.length() != 0)
						stringBuffer.append(", ");
					stringBuffer.append(storableObject.getId());
				}
				Log.debugMessage("CORBAMServerObjectLoader.loadStorableObjectsFromMCM | Loaded: " + stringBuffer, Log.DEBUGLEVEL10);
				/*	^Just debug output -- nothing more^*/

				break;
			}
			catch (AMFICOMRemoteException are) {
				switch (are.error_code.value()) {
					case ErrorCode._ERROR_NOT_LOGGED_IN:
						if (++numEfforts == 1) {
							if (LoginManager.restoreLogin()) {
								continue;
							}
							Log.debugMessage("CORBAMServerObjectLoader.loadStorableObjectsFromMCM() | Login restoration cancelled", Log.INFO);
							return;
						}
						throw new LoginException(are.message);
					default:
						throw new RetrieveObjectException(are.message);
				}
			}
		}
	}

	/**
	 * Loads objects of type, specified by <code>entityCode</code>,
	 * with identifiers not in set <code>ids</code>
	 * and matching condition <code>condition</code>.
	 * Sequentially searches on all MCMs, registered with this MServer.
	 * @param entityCode
	 * @param ids
	 * @param condition
	 * @param transmitButIdsByConditionProcedure
	 * @return <code>Set</code> of <code>StorableObject</code>
	 * @throws ApplicationException
	 */
	protected static final Set loadStorableObjectsButIdsByCondition(final short entityCode,
			final Set ids,
			final StorableObjectCondition condition,
			final TransmitButIdsByConditionProcedure transmitButIdsByConditionProcedure) throws ApplicationException {
		final Set objects = DatabaseObjectLoader.loadStorableObjectsButIdsByCondition(condition, ids);

		final Set loadButIds = Identifier.createSumIdentifiers(ids, objects);
		final Set loadedObjects = new HashSet();
		final Set mcmIds = MeasurementServer.getMCMIds();
		for (final Iterator it = mcmIds.iterator(); it.hasNext();) {
			final Identifier mcmId = (Identifier) it.next();
			try {
				loadStorableObjectsButIdsByConditionFromMCM(mcmId,
						entityCode,
						transmitButIdsByConditionProcedure,
						condition,
						loadButIds,
						loadedObjects);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}

		if (!loadedObjects.isEmpty()) {
			objects.addAll(loadedObjects);
			final StorableObjectDatabase database = DatabaseContext.getDatabase(entityCode);
			try {
				database.insert(loadedObjects);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}
		
		return objects;
	}

	/**
	 * Loads objects of type, specified by <code>entityCode</code>
	 * from MCM, specified by <code>mcmId</code>,
	 * using CORBA call, specified by <code>transmitProcedure</code>.
	 * Identifiers objects to load not belong to set <code>loadButIds</code>.
	 * After call identifiers of loaded objects are added to <code>loadButIds</code>
	 * and loaded objects are added to set <code>loadedObjects</code>.
	 * (Thus, parameters <code>loadButIds</code> and <code>loadedObjects</code> are passed as &quot;inout&quot; arguments.)
	 * If it is impossible to obtain CORBA reference to MCM, simply returns.
	 * @param mcmId
	 * @param entityCode
	 * @param transmitButIdsByConditionProcedure
	 * @param condition
	 * @param loadButIds
	 * @param loadedObjects
	 * @throws ApplicationException
	 */
	private static final void loadStorableObjectsButIdsByConditionFromMCM(final Identifier mcmId,
			final short entityCode,
			final TransmitButIdsByConditionProcedure transmitButIdsByConditionProcedure,
			final StorableObjectCondition condition,
			final Set loadButIds,
			final Set loadedObjects)
			throws ApplicationException {
		MCM mcmRef = null;
		try {
			mcmRef = MServerSessionEnvironment.getInstance().getMServerServantManager().getVerifiedMCMReference(mcmId);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			return;
		}

		/*	Just debug output -- nothing more*/
		Log.debugMessage("CORBAMServerObjectLoader.loadStorableObjectsButIdsByConditionFromMCM | Loading from MCM '" + mcmId + "'",
				Log.DEBUGLEVEL10);
		Log.debugMessage("CORBAMServerObjectLoader.loadStorableObjectsButIdsByConditionFromMCM | Objects '"
				+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode, Log.DEBUGLEVEL10);
		Log.debugMessage("CORBAMServerObjectLoader.loadStorableObjectsButIdsByConditionFromMCM | But ids: " + loadButIds,
				Log.DEBUGLEVEL10);
		/*	^Just debug output -- nothing more^*/

		final Identifier_Transferable[] loadButIdsT = Identifier.createTransferables(loadButIds);
		final StorableObjectCondition_Transferable conditionT = (StorableObjectCondition_Transferable) condition.getTransferable();
		int numEfforts = 0;
		while (true) {
			try {
				final SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();
				final IDLEntity[] transferables = transmitButIdsByConditionProcedure.transmitStorableObjectsButIdsByCondition(mcmRef,
						loadButIdsT,
						conditionT,
						sessionKeyT);

				final Set mcmLoadedObjects = StorableObjectPool.fromTransferables(entityCode, transferables, true);
				Identifier.addToIdentifiers(loadButIds, mcmLoadedObjects);
				loadedObjects.addAll(mcmLoadedObjects);

				/*	Just debug output -- nothing more*/
				StringBuffer stringBuffer = new StringBuffer();
				for (final Iterator it = mcmLoadedObjects.iterator(); it.hasNext();) {
					final StorableObject storableObject = (StorableObject) it.next();
					if (stringBuffer.length() != 0)
						stringBuffer.append(", ");
					stringBuffer.append(storableObject.getId());
				}
				Log.debugMessage("CORBAMServerObjectLoader.loadStorableObjectsButIdsByConditionFromMCM | Loaded: " + stringBuffer,
						Log.DEBUGLEVEL10);
				/*	^Just debug output -- nothing more^*/

				break;
			}
			catch (AMFICOMRemoteException are) {
				switch (are.error_code.value()) {
					case ErrorCode._ERROR_NOT_LOGGED_IN:
						if (++numEfforts == 1) {
							if (LoginManager.restoreLogin()) {
								continue;
							}
							Log.debugMessage("CORBAMServerObjectLoader.loadStorableObjectsButIdsByConditionFromMCM | Login restoration cancelled",
									Log.INFO);
							return;
						}
						throw new LoginException(are.message);
					default:
						throw new RetrieveObjectException(are.message);
				}
			}
		}
	}

}
