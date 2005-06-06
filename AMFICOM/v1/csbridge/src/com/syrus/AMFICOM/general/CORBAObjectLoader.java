/*-
 * $Id: CORBAObjectLoader.java,v 1.23 2005/06/06 14:35:26 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.ErrorCode;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.23 $, $Date: 2005/06/06 14:35:26 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public abstract class CORBAObjectLoader extends ObjectLoader {
	protected ServerConnectionManager serverConnectionManager;

	protected CORBAObjectLoader (final ServerConnectionManager serverConnectionManager) {
		this.serverConnectionManager = serverConnectionManager;
	}

	/*	Delete*/

	/**
	 * Overridden in <code>MCMObjectLoader</code>.
	 */
	public void delete(final Set identifiables) {
		try {
			this.serverConnectionManager.getServerReference().delete(
					Identifier.createTransferables(identifiables),
					LoginManager.getSessionKeyTransferable());
		}
		catch (final CommunicationException ce) {
			Log.errorException(ce);
		}
		catch (final AMFICOMRemoteException are) {
			Log.errorMessage("CORBAObjectLoader.delete | Cannot delete objects '" + identifiables + "'" + are.message);
		}
	}

	/**
	 * Overridden in <code>MCMObjectLoader</code>.
	 */
	public Set refresh(final Set storableObjects) throws ApplicationException {
		try {
			return Identifier.fromTransferables(
					this.serverConnectionManager.getServerReference().transmitRefreshedStorableObjects(
							StorableObject.createHeadersTransferable(storableObjects),
							LoginManager.getSessionKeyTransferable()));
		}
		catch (final AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new ApplicationException(are);
		}
	}

	/**
	 * @author Andrew ``Bass'' Shcheglov
	 * @author $Author: arseniy $
	 * @version $Revision: 1.23 $, $Date: 2005/06/06 14:35:26 $
	 * @module csbridge_v1
	 */
	protected interface TransmitProcedure {
		IDLEntity[] transmitStorableObjects(
				final CommonServer server,
				final Identifier_Transferable ids[],
				final SessionKey_Transferable sessionKey)
				throws AMFICOMRemoteException;
	}

	/**
	 * @author Andrew ``Bass'' Shcheglov
	 * @author $Author: arseniy $
	 * @version $Revision: 1.23 $, $Date: 2005/06/06 14:35:26 $
	 * @see CORBAObjectLoader#loadStorableObjectsButIdsCondition(Set, StorableObjectCondition, short, com.syrus.AMFICOM.general.CORBAObjectLoader.TransmitButIdsConditionProcedure)
	 * @module csbridge_v1
	 */
	protected interface TransmitButIdsConditionProcedure {
		IDLEntity[] transmitStorableObjectsButIdsCondition(
				final CommonServer server,
				final Identifier_Transferable ids[],
				final SessionKey_Transferable sessionKey,
				final StorableObjectCondition_Transferable condition)
				throws AMFICOMRemoteException;
	}

	/**
	 * @author Andrew ``Bass'' Shcheglov
	 * @author $Author: arseniy $
	 * @version $Revision: 1.23 $, $Date: 2005/06/06 14:35:26 $
	 * @module csbridge_v1
	 */
	protected interface ReceiveProcedure {
		StorableObject_Transferable[] receiveStorableObjects(
				final CommonServer server,
				final IDLEntity transferables[],
				final SessionKey_Transferable sessionKey)
				throws AMFICOMRemoteException;
	}

	/**
	 * <p>Overridden in <code>MCMObjectLoader</code>.</p>
	 *
	 * @param ids
	 * @param entityCode
	 * @param transmitProcedure
	 * @throws ApplicationException
	 */
	protected Set loadStorableObjects(final Set ids, final short entityCode, final TransmitProcedure transmitProcedure)
			throws ApplicationException {
		final CommonServer server = this.serverConnectionManager.getServerReference();
		final Identifier_Transferable ids1[] = Identifier.createTransferables(ids);
		final SessionKey_Transferable sessionKey = LoginManager.getSessionKeyTransferable();
		int numEfforts = 0;
		while (true) {
			try {
				final IDLEntity transferables[] = transmitProcedure.transmitStorableObjects(server, ids1, sessionKey);
				return StorableObjectPool.fromTransferables(entityCode, transferables, true);
			}
			catch (final AMFICOMRemoteException are) {
				switch (are.error_code.value()) {
					case ErrorCode._ERROR_NOT_LOGGED_IN:
						if (++numEfforts == 1) {
							if (LoginManager.restoreLogin()) {
								continue;
							}
							Log.debugMessage("CORBAObjectLoader.loadStorableObjects() | Login restoration cancelled by user", Log.INFO);
							return Collections.EMPTY_SET;
						}
						throw new LoginException(are.message);
					default:
						throw new RetrieveObjectException(are.message);
				}
			}
		}
	}

	/**
	 * <p>This method can be considered a duplicate of
	 * {@link #loadStorableObjects(Set, short, com.syrus.AMFICOM.general.CORBAObjectLoader.TransmitProcedure)},
	 * since any particular <code>load...ButIdsCondition()</code> method can
	 * be implemented using <em>only</em>
	 * <code>loadStorableObjects(...)</code> and {@link TransmitProcedure}.
	 * </p>
	 * 
	 * <p>For instance, the <code>loadUsersButIds(...)</code> method can
	 * have two different implementations, and both of them are correct:</p>
	 * 
	 * <pre>
	 * public Set loadUsersButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
	 * 	return super.loadStorableObjects(ids, ObjectEntities.USER_ENTITY_CODE, new TransmitProcedure() {
	 * 		public IDLEntity[] transmitStorableObjects(
	 * 				final CommonServer server,
	 * 				final Identifier_Transferable ids1[],
	 * 				final SessionKey_Transferable sessionKey)
	 * 				throws AMFICOMRemoteException {
	 * 			return ((CMServer) server).transmitUsersButIdsCondition(ids1, sessionKey, (StorableObjectCondition_Transferable) condition.getTransferable());
	 * 		}
	 * 	});
	 * }
	 * 
	 * public Set loadUsersButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
	 * 	return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.USER_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
	 * 		public IDLEntity[] transmitStorableObjectsButIdsCondition(
	 * 				final CommonServer server,
	 * 				final Identifier_Transferable ids1[],
	 * 				final SessionKey_Transferable sessionKey,
	 * 				final StorableObjectCondition_Transferable condition1)
	 * 				throws AMFICOMRemoteException {
	 * 			return ((CMServer) server).transmitUsersButIdsCondition(ids1, sessionKey, condition1);
	 * 		}
	 * 	});
	 * }
	 * </pre>
	 * 
	 * <p>However, the choice was made to discourage the first
	 * implementation in favor of the second one, in order not to fool a
	 * programmer unintentionally, since the <code>ids</code> parameter in
	 * the above two cases has <em>different</em> meanings.</p>
	 * 
	 * <p>Overridden in <code>MCMObjectLoader</code>.</p>
	 */
	protected Set loadStorableObjectsButIdsCondition(final Set ids,
			final StorableObjectCondition condition,
			final short entityCode,
			final TransmitButIdsConditionProcedure transmitButIdsConditionProcedure)
			throws ApplicationException {
		final CommonServer server = this.serverConnectionManager.getServerReference();
		final Identifier_Transferable ids1[] = Identifier.createTransferables(ids);
		final SessionKey_Transferable sessionKey = LoginManager.getSessionKeyTransferable();
		final StorableObjectCondition_Transferable condition1 = (StorableObjectCondition_Transferable) condition.getTransferable();
		int numEfforts = 0;
		while (true) {
			try {
				final IDLEntity transferables[] = transmitButIdsConditionProcedure.transmitStorableObjectsButIdsCondition(server,
						ids1,
						sessionKey,
						condition1);
				return StorableObjectPool.fromTransferables(entityCode, transferables, true);
			} catch (final AMFICOMRemoteException are) {
				switch (are.error_code.value()) {
					case ErrorCode._ERROR_NOT_LOGGED_IN:
						if (++numEfforts == 1) {
							if (LoginManager.restoreLogin()) {
								continue;
							}
							Log.debugMessage("CORBAObjectLoader.loadStorableObjectsButIdsCondition() | Login restoration cancelled by user",
									Log.INFO);
							return Collections.EMPTY_SET;
						}
						throw new LoginException(are.message);
					default:
						throw new RetrieveObjectException(are.message);
				}
			}
		}
	}

	/**
	 * @todo Login restoration & error handling.
	 */
	protected final void saveStorableObjects(final Set storableObjects,
			final short entityCode,
			final ReceiveProcedure receiveProcedure)
			throws ApplicationException {
		final CommonServer server = this.serverConnectionManager.getServerReference();
		final SessionKey_Transferable sessionKey = LoginManager.getSessionKeyTransferable();

		final IDLEntity transferables[] = StorableObject.allocateArrayOfTransferables(entityCode, storableObjects.size());
		int i = 0;
		for (final Iterator storableObjectIterator = storableObjects.iterator(); storableObjectIterator.hasNext(); i++) {
			transferables[i] = ((StorableObject) storableObjectIterator.next()).getTransferable();
		}

		try {
			final StorableObject_Transferable headers[] = receiveProcedure.receiveStorableObjects(server,
					transferables,
					sessionKey);
			super.updateHeaders(storableObjects, headers);
		} catch (final AMFICOMRemoteException are) {
			final String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new UpdateObjectException(mesg + are.message);
		}
	}
}
