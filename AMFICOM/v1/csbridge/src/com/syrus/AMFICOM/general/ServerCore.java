/*-
 * $Id: ServerCore.java,v 1.33 2005/08/08 11:38:11 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.general.corba.IdVersion;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlIdentifierHolder;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlCompletionStatus;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlErrorCode;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.33 $, $Date: 2005/08/08 11:38:11 $
 * @module csbridge
 * @todo Refactor ApplicationException descendants to be capable of generating
 *       an AMFICOMRemoteException.
 */
public abstract class ServerCore implements CommonServer {
	private static final long serialVersionUID = 2873567194611284256L;

	private LoginServerConnectionManager loginServerConnectionManager;
	private ORB orb;

	protected ServerCore(final LoginServerConnectionManager loginServerConnectionManager, final ORB orb) {
		this.loginServerConnectionManager = loginServerConnectionManager;
		this.orb = orb;
	}



	// ///////////////////////////// CommonServer ///////////////////////////////////////////////

	public final IdlStorableObject[] transmitStorableObjects(final IdlIdentifier[] idsT, final IdlSessionKey sessionKeyT)
			throws AMFICOMRemoteException {
		try {
			assert idsT != null && sessionKeyT != null: ErrorMessages.NON_NULL_EXPECTED;
			final int length = idsT.length;
			assert length != 0: ErrorMessages.NON_EMPTY_EXPECTED;
			assert StorableObject.hasSingleTypeEntities(idsT);

			final IdlIdentifierHolder userIdH = new IdlIdentifierHolder();
			final IdlIdentifierHolder domainIdH = new IdlIdentifierHolder();
			this.validateAccess(sessionKeyT, userIdH, domainIdH);

			final Set<Identifier> ids = Identifier.fromTransferables(idsT);

			Log.debugMessage("ServerCore.transmitStorableObjects | Requested '"
					+ ObjectEntities.codeToString(StorableObject.getEntityCodeOfIdentifiables(idsT)) + "'s for ids: " + ids, Level.FINEST);

			final Set<? extends StorableObject> storableObjects = StorableObjectPool.getStorableObjects(ids, true);
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

	public final IdlStorableObject[] transmitStorableObjectsButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		try {
			assert idsT != null && sessionKeyT != null && conditionT != null : ErrorMessages.NON_NULL_EXPECTED;

			final StorableObjectCondition condition = StorableObjectConditionBuilder.restoreCondition(conditionT);
			final short entityCode = condition.getEntityCode().shortValue();

			assert idsT.length == 0 || entityCode == StorableObject.getEntityCodeOfIdentifiables(idsT);
			assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE;

			final IdlIdentifierHolder userId = new IdlIdentifierHolder();
			final IdlIdentifierHolder domainId = new IdlIdentifierHolder();
			this.validateAccess(sessionKeyT, userId, domainId);

			final Set<Identifier> ids = Identifier.fromTransferables(idsT);

			Log.debugMessage("ServerCore.transmitStorableObjectsButIdsByCondition | Requested '"
					+ ObjectEntities.codeToString(entityCode) + "'s but ids: " + ids, Level.FINEST);

			/**
			 * NOTE: If it is impossible to load objects by Loader - return only those from Pool
			 */
			final Set<? extends StorableObject> storableObjects = StorableObjectPool.getStorableObjectsButIdsByCondition(ids,
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

	public final IdVersion[] transmitRemoteVersions(final IdlIdentifier[] idsT, final IdlSessionKey sessionKeyT)
			throws AMFICOMRemoteException {
		try {
			assert idsT != null && sessionKeyT != null : ErrorMessages.NON_NULL_EXPECTED;
			final int length = idsT.length;
			assert length != 0 : ErrorMessages.NON_EMPTY_EXPECTED;

			final IdlIdentifierHolder userId = new IdlIdentifierHolder();
			final IdlIdentifierHolder domainId = new IdlIdentifierHolder();
			this.validateAccess(sessionKeyT, userId, domainId);

			final Set<Identifier> ids = Identifier.fromTransferables(idsT);
			final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
			assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE;
			final StorableObjectDatabase<?> database = DatabaseContext.getDatabase(entityCode);
			assert (database != null) : ErrorMessages.NON_NULL_EXPECTED;

			Log.debugMessage("ServerCore.transmitRemoteVersions | Versions for '"
					+ ObjectEntities.codeToString(entityCode) + "'s: " + ids, Level.FINEST);

			final Map<Identifier, StorableObjectVersion> versionsMap = database.retrieveVersions(ids);
			final IdVersion[] idVersions = new IdVersion[versionsMap.size()];
			int i = 0;
			for (final Identifier id : versionsMap.keySet()) {
				idVersions[i++] = new IdVersion(id.getTransferable(), versionsMap.get(id).longValue());
			}
			return idVersions;
		} catch (ApplicationException ae) {
			throw this.processDefaultApplicationException(ae, IdlErrorCode.ERROR_RETRIEVE);
		} catch (final AMFICOMRemoteException are) {
			throw are;
		} catch (final Throwable throwable) {
			throw this.processDefaultThrowable(throwable);
		}
	}

	public final void receiveStorableObjects(final IdlStorableObject[] storableObjectsT, final IdlSessionKey sessionKeyT)
			throws AMFICOMRemoteException {
		try {
			assert storableObjectsT != null && sessionKeyT != null : ErrorMessages.NON_NULL_EXPECTED;
			final int length = storableObjectsT.length;
			assert length != 0 : ErrorMessages.NON_EMPTY_EXPECTED;

			final IdlIdentifierHolder userId = new IdlIdentifierHolder();
			final IdlIdentifierHolder domainId = new IdlIdentifierHolder();
			this.validateAccess(sessionKeyT, userId, domainId);

			final Set<StorableObject> storableObjects = StorableObjectPool.fromTransferables(storableObjectsT, true);
			final short entityCode = StorableObject.getEntityCodeOfIdentifiables(storableObjects);
			assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE;

			Log.debugMessage("ServerCore.receiveStorableObjects | Received '"
					+ ObjectEntities.codeToString(entityCode) + "'s: " + Identifier.createIdentifiers(storableObjects), Level.FINEST);

			final StorableObjectDatabase<StorableObject> database = DatabaseContext.getDatabase(entityCode);
			assert (database != null) : ErrorMessages.NON_NULL_EXPECTED;
			database.save(storableObjects);
		} catch (ApplicationException ae) {
			throw this.processDefaultApplicationException(ae, IdlErrorCode.ERROR_UPDATE);
		} catch (final AMFICOMRemoteException are) {
			throw are;
		} catch (final Throwable throwable) {
			throw this.processDefaultThrowable(throwable);
		}
	}

	public final IdlIdentifier[] transmitOldVersionIds(final IdVersion[] idVersions, final IdlSessionKey sessionKeyT)
			throws AMFICOMRemoteException {
		try {
			assert idVersions != null && sessionKeyT != null : ErrorMessages.NON_NULL_EXPECTED;
			final int length = idVersions.length;
			assert length != 0 : ErrorMessages.NON_EMPTY_EXPECTED;

			final IdlIdentifierHolder userId = new IdlIdentifierHolder();
			final IdlIdentifierHolder domainId = new IdlIdentifierHolder();
			this.validateAccess(sessionKeyT, userId, domainId);

			final Map<Identifier, StorableObjectVersion> versionsMap = new HashMap<Identifier, StorableObjectVersion>(idVersions.length);
			for (int i = 0; i < idVersions.length; i++) {
				versionsMap.put(new Identifier(idVersions[i].id), new StorableObjectVersion(idVersions[i].version));
			}
			final short entityCode = StorableObject.getEntityCodeOfIdentifiables(versionsMap.keySet());
			assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE;
			final StorableObjectDatabase<?> database = DatabaseContext.getDatabase(entityCode);
			assert (database != null) : ErrorMessages.NON_NULL_EXPECTED;
			final Set<Identifier> ids = database.getOldVersionIds(versionsMap);

			Log.debugMessage("ServerCore.transmitOldVersionIds | Old versions have '"
					+ ObjectEntities.codeToString(entityCode) + "'s: " + ids, Level.FINEST);

			StorableObjectPool.refresh(ids);

			return Identifier.createTransferables(ids);
		} catch (ApplicationException ae) {
			throw this.processDefaultApplicationException(ae, IdlErrorCode.ERROR_RETRIEVE);
		} catch (final AMFICOMRemoteException are) {
			throw are;
		} catch (final Throwable throwable) {
			throw this.processDefaultThrowable(throwable);
		}
	}

	public final void delete(final IdlIdentifier[] idsT, final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		try {
			assert idsT != null && sessionKeyT != null : ErrorMessages.NON_NULL_EXPECTED;
			final int length = idsT.length;
			assert length != 0 : ErrorMessages.NON_EMPTY_EXPECTED;

			final IdlIdentifierHolder userId = new IdlIdentifierHolder();
			final IdlIdentifierHolder domainId = new IdlIdentifierHolder();
			this.validateAccess(sessionKeyT, userId, domainId);

			final Set<Identifier> ids = Identifier.fromTransferables(idsT);
			final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
			assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE;

			Log.debugMessage("ServerCore.delete | Deleting '"
					+ ObjectEntities.codeToString(entityCode) + "'s: " + ids, Level.FINEST);

			StorableObjectPool.delete(ids);
			StorableObjectPool.flush(entityCode, LoginManager.getUserId(), false);
		} catch (final AMFICOMRemoteException are) {
			throw are;
		} catch (final Throwable throwable) {
			throw this.processDefaultThrowable(throwable);
		}
	}



	// /////////////////////////////// Verifiable /////////////////////////////////////////////

	public final void verify(final byte b) {
		try {
			Log.debugMessage("ServerCore.verify | Verify value: " + b, Level.CONFIG);
		} catch (final Throwable t) {
			Log.debugException(t, Level.SEVERE);
		}
	}



	// ///////////////////////////// helper methods ///////////////////////////////////////////////
	/**
	 * @param sessionKeyT an "in" parameter.
	 * @param userIdH an "out" parameter.
	 * @param domainIdH an "out" parameter.
	 * @throws AMFICOMRemoteException
	 */
	protected final void validateAccess(final IdlSessionKey sessionKeyT,
			final IdlIdentifierHolder userIdH,
			final IdlIdentifierHolder domainIdH)
			throws AMFICOMRemoteException {
		try {
			this.loginServerConnectionManager.getLoginServerReference().validateAccess(sessionKeyT, userIdH, domainIdH);
		}
		catch (final CommunicationException ce) {
			throw new AMFICOMRemoteException(IdlErrorCode.ERROR_ACCESS_VALIDATION, IdlCompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (AMFICOMRemoteException are) {
			throw are;
		}
		catch (final Throwable throwable) {
			throw this.processDefaultThrowable(throwable);
		}
	}

	private final AMFICOMRemoteException processDefaultApplicationException(final ApplicationException ae, final IdlErrorCode errorCode) {
		Log.debugException(ae, Level.SEVERE);
		return new AMFICOMRemoteException(errorCode, IdlCompletionStatus.COMPLETED_NO, ae.getMessage());
	}

	protected final AMFICOMRemoteException processDefaultThrowable(final Throwable throwable) {
		Log.debugException(throwable, Level.SEVERE);
		return new AMFICOMRemoteException(IdlErrorCode.ERROR_UNKNOWN, IdlCompletionStatus.COMPLETED_PARTIALLY, throwable.getMessage());
	}

}
