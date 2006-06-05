/*-
 * $Id: ServerCore.java,v 1.54 2006/06/05 13:42:20 arseniy Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.ErrorMessages.ILLEGAL_ENTITY_CODE;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_EMPTY_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECTS_NOT_OF_THE_SAME_ENTITY;
import static java.util.logging.Level.CONFIG;
import static java.util.logging.Level.FINEST;
import static java.util.logging.Level.SEVERE;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.omg.CORBA.LongHolder;
import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CommonServerOperations;
import com.syrus.AMFICOM.general.corba.IdVersion;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlCompletionStatus;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlErrorCode;
import com.syrus.AMFICOM.security.SessionKey;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.54 $, $Date: 2006/06/05 13:42:20 $
 * @module csbridge
 * @todo Refactor ApplicationException descendants to be capable of generating
 *       an AMFICOMRemoteException.
 */
public abstract class ServerCore implements CommonServerOperations {

	private LoginServerConnectionManager loginServerConnectionManager;
	private ORB orb;
	private String hostName;

	private Map<SessionKey, Date> loginValidationComingDates;

	protected ServerCore(final LoginServerConnectionManager loginServerConnectionManager, final ORB orb) {
		this.loginServerConnectionManager = loginServerConnectionManager;
		this.orb = orb;

		String hostname;
		try {
			hostname = InetAddress.getLocalHost().getCanonicalHostName();
		} catch (UnknownHostException uhe) {
			hostname = "unknown";
			//Не бывать тому!
			Log.errorMessage(uhe);
		}
		this.hostName = hostname;

		this.loginValidationComingDates = new HashMap<SessionKey, Date>();
	}


	// ///////////////////////////// CommonUser ///////////////////////////////////////////////

	public final String getHostName() {
		return this.hostName;
	}


	// ///////////////////////////// CommonServer ///////////////////////////////////////////////

	public final IdlStorableObject[] transmitStorableObjects(final IdlIdentifier[] idlIds, final IdlSessionKey idlSessionKey)
			throws AMFICOMRemoteException {
		try {
			assert idlIds != null && idlSessionKey != null : NON_NULL_EXPECTED;
			final int length = idlIds.length;
			assert length != 0: NON_EMPTY_EXPECTED;
			assert StorableObject.hasSingleTypeEntities(idlIds) : OBJECTS_NOT_OF_THE_SAME_ENTITY;

			this.validateLogin(SessionKey.valueOf(idlSessionKey));

			final Set<Identifier> ids = Identifier.fromTransferables(idlIds);

			Log.debugMessage("Requested '"
					+ ObjectEntities.codeToString(StorableObject.getEntityCodeOfIdentifiables(idlIds)) + "'s for ids: "
					+ ids, FINEST);

			final Set<StorableObject> storableObjects = StorableObjectPool.getStorableObjects(ids, true);
			final IdlStorableObject[] transferables = StorableObject.createTransferables(storableObjects, this.orb);
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

			Log.debugMessage("Requested '" + ObjectEntities.codeToString(entityCode) + "'s but ids: " + ids + " for condition: " + condition, FINEST);

			/**
			 * NOTE: If it is impossible to load objects by Loader - return only those
			 * from Pool
			 */
			final Set<StorableObject> storableObjects = StorableObjectPool.getStorableObjectsButIdsByCondition(ids,
					condition,
					true,
					false);
			final IdlStorableObject[] transferables = StorableObject.createTransferables(storableObjects, this.orb);
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

			Log.debugMessage("Requested identifiers of '" + ObjectEntities.codeToString(entityCode) + "'s but ids: " + ids + " for condition: " + condition, FINEST);

			/**
			 * NOTE: If it is impossible to load identifiers by Loader - return only those
			 * from Pool
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

			Log.debugMessage("Versions for '" + ObjectEntities.codeToString(entityCode) + "'s: " + ids, FINEST);

			final Map<Identifier, StorableObjectVersion> versionsMap = database.retrieveVersions(ids);
			final IdVersion[] idVersions = new IdVersion[versionsMap.size()];
			int i = 0;
			for (final Identifier id : versionsMap.keySet()) {
				idVersions[i++] = new IdVersion(id.getIdlTransferable(), versionsMap.get(id).longValue());
			}

			//-Before return, ensure, that objects in pool are up to date.
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

			Log.debugMessage("Received '"
					+ ObjectEntities.codeToString(entityCode) + "'s: "
					+ Identifier.createIdentifiers(storableObjects), FINEST);

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

			Log.debugMessage("Deleting '" + ObjectEntities.codeToString(entityCode) + "'s: " + ids, FINEST);

			StorableObjectPool.delete(ids);
			StorableObjectPool.flush(ids, LoginManager.getUserId(), false);
		} catch (final AMFICOMRemoteException are) {
			throw are;
		} catch (final Throwable throwable) {
			throw this.processDefaultThrowable(throwable);
		}
	}



	// /////////////////////////////// Verifiable /////////////////////////////////////////////

	public final void verify(final byte b) {
		try {
			Log.debugMessage("Verify value: " + b, CONFIG);
		} catch (final Throwable t) {
			Log.debugMessage(t, SEVERE);
		}
	}



	// ///////////////////////////// helper methods ///////////////////////////////////////////////

	protected final void validateLogin(final SessionKey sessionKey) throws AMFICOMRemoteException {
		final Date comingDate = this.loginValidationComingDates.get(sessionKey);
		if (comingDate == null || comingDate.getTime() <= System.currentTimeMillis()) {
			final LongHolder loginValidationTimeoutHolder = new LongHolder();
			try {
				this.loginServerConnectionManager.getLoginServerReference().validateLogin(sessionKey.getIdlTransferable(),
						loginValidationTimeoutHolder);
				this.loginValidationComingDates.put(sessionKey, new Date(System.currentTimeMillis() + loginValidationTimeoutHolder.value));
			} catch (final CommunicationException ce) {
				throw new AMFICOMRemoteException(IdlErrorCode.ERROR_ACCESS_VALIDATION,
						IdlCompletionStatus.COMPLETED_NO,
						ce.getMessage());
			} catch (AMFICOMRemoteException are) {
				throw are;
			} catch (final Throwable throwable) {
				throw this.processDefaultThrowable(throwable);
			}
		}
	}

	private final AMFICOMRemoteException processDefaultApplicationException(final ApplicationException ae, final IdlErrorCode errorCode) {
		Log.debugMessage(ae, SEVERE);
		return new AMFICOMRemoteException(errorCode, IdlCompletionStatus.COMPLETED_NO, ae.getMessage());
	}

	protected final AMFICOMRemoteException processDefaultThrowable(final Throwable throwable) {
		Log.debugMessage(throwable, SEVERE);
		return new AMFICOMRemoteException(IdlErrorCode.ERROR_UNKNOWN, IdlCompletionStatus.COMPLETED_PARTIALLY, throwable.getMessage());
	}

}
