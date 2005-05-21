/*-
 * $Id: MSHServerImpl.java,v 1.12 2005/05/21 19:43:37 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mshserver;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectGroupEntities;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.Identifier_TransferableHolder;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.map.MapDatabaseContext;
import com.syrus.AMFICOM.scheme.SchemeDatabaseContext;
import com.syrus.AMFICOM.security.SessionKey;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.12 $, $Date: 2005/05/21 19:43:37 $
 * @author $Author: bass $
 * @module mshserver_1
 */
public final class MSHServerImpl extends MSHServerSchemeTransmit {
	private static final long serialVersionUID = 3762810480783274295L;

	/**
	 * @param sessionKey
	 * @param userId
	 * @param domainId
	 * @throws AMFICOMRemoteException
	 * @see com.syrus.AMFICOM.general.ServerCore#validateAccess(com.syrus.AMFICOM.security.corba.SessionKey_Transferable, com.syrus.AMFICOM.general.corba.Identifier_TransferableHolder, com.syrus.AMFICOM.general.corba.Identifier_TransferableHolder)
	 */
	protected void validateAccess(final SessionKey_Transferable sessionKey,
			final Identifier_TransferableHolder userId,
			final Identifier_TransferableHolder domainId)
			throws AMFICOMRemoteException {
		try {
			MSHServerSessionEnvironment.getInstance()
					.getMSHServerServantManager()
					.getLoginServerReference()
					.validateAccess(sessionKey, userId, domainId);
		} catch (final CommunicationException ce) {
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ACCESS_VALIDATION, CompletionStatus.COMPLETED_NO, ce.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ACCESS_VALIDATION, CompletionStatus.COMPLETED_PARTIALLY, t.getMessage());
		}
	}

	/**
	 * @deprecated
	 * @param storableObjects
	 * @param sessionKey
	 * @param force
	 * @throws AMFICOMRemoteException
	 * @see MSHServerMapReceive#receiveStorableObjects(Set, SessionKey_Transferable, boolean)
	 */
	StorableObject_Transferable[] receiveStorableObjects(
			final Set storableObjects,
			final SessionKey_Transferable sessionKey,
			final boolean force)
			throws AMFICOMRemoteException {
		try {
			if (storableObjects.isEmpty())
				return new StorableObject_Transferable[0];
			assert StorableObject.hasSingleTypeEntities(storableObjects);
			final short entityCode = StorableObject.getEntityCodeOfIdentifiables(storableObjects);
			final Identifier userId = (new SessionKey(sessionKey)).getUserId();
			Log.debugMessage("MSHServerImpl.receiveStorableObjects | Receiving "
					+ storableObjects.size() + ' '
					+ ObjectEntities.codeToString(entityCode)
					+ "(s) as requested by user '" + userId + '\'',
					Log.INFO);
			StorableObjectDatabase storableObjectDatabase = null;
			if (ObjectGroupEntities.isInMapGroup(entityCode))
				storableObjectDatabase = MapDatabaseContext.getDatabase(entityCode);
			else if (ObjectGroupEntities.isInSchemeGroup(entityCode))
				storableObjectDatabase = SchemeDatabaseContext.getDatabase(entityCode);
			else
				assert false;
			storableObjectDatabase.update(storableObjects,
					userId,
					force
						? StorableObjectDatabase.UPDATE_FORCE
						: StorableObjectDatabase.UPDATE_CHECK);
			return getListHeaders(storableObjects);
		} catch (final UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
		} catch (final VersionCollisionException vce) {
			Log.errorException(vce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, vce.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}
}
