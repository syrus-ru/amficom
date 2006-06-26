/*-
 * $Id: StorableObjectServerImpl.java,v 1.1.1.1 2006/06/26 10:49:41 cvsadmin Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.systemserver;

import static com.syrus.AMFICOM.general.ErrorMessages.ILLEGAL_ENTITY_CODE;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_EMPTY_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECTS_NOT_OF_THE_SAME_ENTITY;
import static java.util.logging.Level.FINEST;

import java.util.Map;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectConditionBuilder;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlErrorCode;
import com.syrus.AMFICOM.security.SessionKey;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;
import com.syrus.AMFICOM.systemserver.corba.IdVersion;
import com.syrus.AMFICOM.systemserver.corba.StorableObjectServerOperations;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1.1.1 $, $Date: 2006/06/26 10:49:41 $
 * @author $Author: cvsadmin $
 * @author Tashoyan Arseniy Feliksovich
 * @module systemserver
 */
final class StorableObjectServerImpl extends ServerCore implements StorableObjectServerOperations {


	// ///////////////////////////// CommonServer ///////////////////////////////////////////////

	public final IdlStorableObject[] transmitStorableObjects(final IdlIdentifier[] idlIds, final IdlSessionKey idlSessionKey)
			throws AMFICOMRemoteException {
		try {
			assert idlIds != null && idlSessionKey != null : NON_NULL_EXPECTED;
			final int length = idlIds.length;
			assert length != 0 : NON_EMPTY_EXPECTED;
			assert StorableObject.hasSingleTypeEntities(idlIds) : OBJECTS_NOT_OF_THE_SAME_ENTITY;

			this.validateLogin(SessionKey.valueOf(idlSessionKey));

			final Set<Identifier> ids = Identifier.fromTransferables(idlIds);

			Log.debugMessage("Requested"
					+ ObjectEntities.asString(StorableObject.getEntityCodeOfIdentifiables(idlIds)) + "for ids: " + ids, FINEST);

			final Set<StorableObject> storableObjects = StorableObjectPool.getStorableObjects(ids, true);
			final ORB orb = SystemServerServantManager.getInstance().getCORBAServer().getOrb();
			final IdlStorableObject[] transferables = StorableObject.createTransferables(storableObjects, orb);
			return transferables;
		} catch (ApplicationException ae) {
			throw this.processDefaultApplicationException(ae, IdlErrorCode.ERROR_RETRIEVE);
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable throwable) {
			throw this.processDefaultThrowable(throwable);
		}
	}

	public final IdlStorableObject[] transmitStorableObjectsButIdsByCondition(final IdlIdentifier[] idlIds,
			final IdlStorableObjectCondition idlStorableObjectCondition,
			final IdlSessionKey idlSessionKey) throws AMFICOMRemoteException {
		try {
			assert idlIds != null && idlSessionKey != null && idlStorableObjectCondition != null : NON_NULL_EXPECTED;

			final StorableObjectCondition condition = StorableObjectConditionBuilder.restoreCondition(idlStorableObjectCondition);
			final short entityCode = condition.getEntityCode().shortValue();

			assert idlIds.length == 0 || entityCode == StorableObject.getEntityCodeOfIdentifiables(idlIds) : ILLEGAL_ENTITY_CODE;
			assert ObjectEntities.isEntityCodeValid(entityCode) : ILLEGAL_ENTITY_CODE;

			this.validateLogin(SessionKey.valueOf(idlSessionKey));

			final Set<Identifier> ids = Identifier.fromTransferables(idlIds);

			Log.debugMessage("Requested"
					+ ObjectEntities.asString(entityCode) + "but ids: " + ids + " for condition: " + condition, FINEST);

			/**
			 * NOTE: If it is impossible to load objects by Loader - return only
			 * those from Pool
			 */
			final Set<StorableObject> storableObjects = StorableObjectPool.getStorableObjectsButIdsByCondition(ids,
					condition,
					true,
					false);
			final ORB orb = SystemServerServantManager.getInstance().getCORBAServer().getOrb();
			final IdlStorableObject[] transferables = StorableObject.createTransferables(storableObjects, orb);
			return transferables;
		} catch (final ApplicationException ae) {
			throw this.processDefaultApplicationException(ae, IdlErrorCode.ERROR_RETRIEVE);
		} catch (final AMFICOMRemoteException are) {
			throw are;
		} catch (final Throwable throwable) {
			throw this.processDefaultThrowable(throwable);
		}
	}

	public final IdlIdentifier[] transmitIdentifiersButIdsByCondition(final IdlIdentifier[] idlIds,
			final IdlStorableObjectCondition idlStorableObjectCondition,
			final IdlSessionKey idlSessionKey) throws AMFICOMRemoteException {
		try {
			assert idlIds != null && idlSessionKey != null && idlStorableObjectCondition != null : NON_NULL_EXPECTED;

			final StorableObjectCondition condition = StorableObjectConditionBuilder.restoreCondition(idlStorableObjectCondition);
			final short entityCode = condition.getEntityCode().shortValue();

			assert idlIds.length == 0 || entityCode == StorableObject.getEntityCodeOfIdentifiables(idlIds);
			assert ObjectEntities.isEntityCodeValid(entityCode) : ILLEGAL_ENTITY_CODE;

			this.validateLogin(SessionKey.valueOf(idlSessionKey));

			final Set<Identifier> ids = Identifier.fromTransferables(idlIds);

			Log.debugMessage("Requested identifiers of"
					+ ObjectEntities.asString(entityCode) + "but ids: " + ids + " for condition: " + condition, FINEST);

			/**
			 * NOTE: If it is impossible to load identifiers by Loader - return
			 * only those from Pool
			 */
			final Set<Identifier> identifiers = StorableObjectPool.getIdentifiersButIdsByCondition(ids, condition, true, false);
			final IdlIdentifier[] transferables = Identifier.createTransferables(identifiers);
			return transferables;
		} catch (ApplicationException ae) {
			throw this.processDefaultApplicationException(ae, IdlErrorCode.ERROR_RETRIEVE);
		} catch (final AMFICOMRemoteException are) {
			throw are;
		} catch (final Throwable throwable) {
			throw this.processDefaultThrowable(throwable);
		}
	}

	public final IdVersion[] transmitRemoteVersions(final IdlIdentifier[] idlIds, final IdlSessionKey idlSessionKey)
			throws AMFICOMRemoteException {
		try {
			assert idlIds != null && idlSessionKey != null : NON_NULL_EXPECTED;
			final int length = idlIds.length;
			assert length != 0 : NON_EMPTY_EXPECTED;

			this.validateLogin(SessionKey.valueOf(idlSessionKey));

			final Set<Identifier> ids = Identifier.fromTransferables(idlIds);
			final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
			assert ObjectEntities.isEntityCodeValid(entityCode) : ILLEGAL_ENTITY_CODE;
			final StorableObjectDatabase<?> database = DatabaseContext.getDatabase(entityCode);
			assert (database != null) : NON_NULL_EXPECTED + "; entity: " + ObjectEntities.codeToString(entityCode);

			Log.debugMessage("Versions for" + ObjectEntities.asString(entityCode) + ": " + ids, FINEST);

			final Map<Identifier, StorableObjectVersion> versionsMap = database.retrieveVersions(ids);
			final IdVersion[] idVersions = new IdVersion[versionsMap.size()];
			int i = 0;
			for (final Identifier id : versionsMap.keySet()) {
				idVersions[i++] = new IdVersion(id.getIdlTransferable(), versionsMap.get(id).longValue());
			}

			// Before return, ensure, that objects in pool are up to date.
			StorableObjectPool.refresh(ids);

			return idVersions;
		} catch (ApplicationException ae) {
			throw this.processDefaultApplicationException(ae, IdlErrorCode.ERROR_RETRIEVE);
		} catch (final AMFICOMRemoteException are) {
			throw are;
		} catch (final Throwable throwable) {
			throw this.processDefaultThrowable(throwable);
		}
	}

	public final void receiveStorableObjects(final IdlStorableObject[] idlStorableObjects, final IdlSessionKey idlSessionKey)
			throws AMFICOMRemoteException {
		try {
			assert idlStorableObjects != null && idlSessionKey != null : NON_NULL_EXPECTED;
			final int length = idlStorableObjects.length;
			assert length != 0 : NON_EMPTY_EXPECTED;

			this.validateLogin(SessionKey.valueOf(idlSessionKey));

			final Set<StorableObject> storableObjects = StorableObjectPool.fromTransferables(idlStorableObjects, true);
			final short entityCode = StorableObject.getEntityCodeOfIdentifiables(storableObjects);
			assert ObjectEntities.isEntityCodeValid(entityCode) : ILLEGAL_ENTITY_CODE;

			Log.debugMessage("Received"
					+ ObjectEntities.asString(entityCode) + ": " + Identifier.createIdentifiers(storableObjects), FINEST);

			final StorableObjectDatabase<StorableObject> database = DatabaseContext.getDatabase(entityCode);
			assert (database != null) : NON_NULL_EXPECTED + "; entity: " + ObjectEntities.codeToString(entityCode);
			database.save(storableObjects);
		} catch (ApplicationException ae) {
			throw this.processDefaultApplicationException(ae, IdlErrorCode.ERROR_UPDATE);
		} catch (final AMFICOMRemoteException are) {
			throw are;
		} catch (final Throwable throwable) {
			throw this.processDefaultThrowable(throwable);
		}
	}

	public final void delete(final IdlIdentifier[] idlIds, final IdlSessionKey idlSessionKey) throws AMFICOMRemoteException {
		try {
			assert idlIds != null && idlSessionKey != null : NON_NULL_EXPECTED;
			final int length = idlIds.length;
			assert length != 0 : NON_EMPTY_EXPECTED;

			this.validateLogin(SessionKey.valueOf(idlSessionKey));

			final Set<Identifier> ids = Identifier.fromTransferables(idlIds);
			final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
			assert ObjectEntities.isEntityCodeValid(entityCode) : ILLEGAL_ENTITY_CODE;

			Log.debugMessage("Deleting" + ObjectEntities.asString(entityCode) + ": " + ids, FINEST);

			StorableObjectPool.delete(ids);
			StorableObjectPool.flush(ids, LoginManager.getUserId(), false);
		} catch (final AMFICOMRemoteException are) {
			throw are;
		} catch (final Throwable throwable) {
			throw this.processDefaultThrowable(throwable);
		}
	}


}
