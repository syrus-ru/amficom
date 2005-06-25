/*-
 * $Id: CORBAObjectLoader.java,v 1.37 2005/06/25 17:07:53 bass Exp $
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
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.ErrorCode;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.37 $, $Date: 2005/06/25 17:07:53 $
 * @author $Author: bass $
 * @module csbridge_v1
 */
public abstract class CORBAObjectLoader {
	protected ServerConnectionManager serverConnectionManager;

	protected CORBAObjectLoader (final ServerConnectionManager serverConnectionManager) {
		this.serverConnectionManager = serverConnectionManager;
	}

	/*	Delete*/

	/**
	 * Overridden in <code>MCMObjectLoader</code>.
	 */
	public void delete(final Set<? extends Identifiable> identifiables) {
		try {
			this.serverConnectionManager.getServerReference().delete(Identifier.createTransferables(identifiables),
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
	public Set refresh(final Set<? extends StorableObject> storableObjects) throws ApplicationException {
		try {
			final CommonServer commonServer = this.serverConnectionManager.getServerReference();
			final IdlStorableObject[] headers = StorableObject.createHeadersTransferable(this.serverConnectionManager.getCORBAServer().getOrb(), storableObjects);
			return Identifier.fromTransferables(commonServer.transmitRefreshedStorableObjects(headers,
					LoginManager.getSessionKeyTransferable()));
		}
		catch (final AMFICOMRemoteException are) {
			if (are.errorCode.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new ApplicationException(are);
		}
	}

	/**
	 * @author $Author: bass $
	 * @version $Revision: 1.37 $, $Date: 2005/06/25 17:07:53 $
	 * @module csbridge_v1
	 */
	public interface TransmitProcedure {
		IDLEntity[] transmitStorableObjects(final CommonServer commonServer,
				final IdlIdentifier[] idsT,
				final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException;
	}

	/**
	 * @author $Author: bass $
	 * @version $Revision: 1.37 $, $Date: 2005/06/25 17:07:53 $
	 * @see CORBAObjectLoader#loadStorableObjectsButIdsByCondition(short, Set,
	 *      StorableObjectCondition,
	 *      com.syrus.AMFICOM.general.CORBAObjectLoader.TransmitButIdsByConditionProcedure)
	 * @module csbridge_v1
	 */
	public interface TransmitButIdsByConditionProcedure {
		IDLEntity[] transmitStorableObjectsButIdsCondition(final CommonServer commonServer,
				final IdlIdentifier[] idsT,
				final IdlSessionKey sessionKeyT,
				final IdlStorableObjectCondition conditionT) throws AMFICOMRemoteException;
	}

	/**
	 * @author Andrew ``Bass'' Shcheglov
	 * @author $Author: bass $
	 * @version $Revision: 1.37 $, $Date: 2005/06/25 17:07:53 $
	 * @module csbridge_v1
	 */
	protected interface ReceiveProcedure {
		IdlStorableObject[] receiveStorableObjects(final CommonServer commonServer,
				final IDLEntity[] transferables,
				final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException;
	}

	/**
	 * <p>
	 * Overridden in <code>MCMObjectLoader</code> and <code>CORBACMServerObjectLoader</code>.
	 * </p>
	 * 
	 * @param ids
	 * @param entityCode
	 * @param transmitProcedure
	 * @throws ApplicationException
	 */
	protected Set loadStorableObjects(final short entityCode, final Set<Identifier> ids, final TransmitProcedure transmitProcedure)
			throws ApplicationException {
		final CommonServer server = this.serverConnectionManager.getServerReference();
		final IdlIdentifier[] idsT = Identifier.createTransferables(ids);
		int numEfforts = 0;
		while (true) {
			try {
				final IdlSessionKey sessionKeyT = LoginManager.getSessionKeyTransferable();
				final IDLEntity[] transferables = transmitProcedure.transmitStorableObjects(server, idsT, sessionKeyT);
				return StorableObjectPool.fromTransferables(entityCode, transferables, true);
			}
			catch (final AMFICOMRemoteException are) {
				switch (are.errorCode.value()) {
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
	 * {@link #loadStorableObjects(short, Set, com.syrus.AMFICOM.general.CORBAObjectLoader.TransmitProcedure)},
	 * since any particular <code>load...ButIdsByCondition()</code> method can
	 * be implemented using <em>only</em>
	 * <code>loadStorableObjects(...)</code> and {@link TransmitProcedure}.
	 * </p>
	 * 
	 * <p>For instance, the <code>loadUsersButIds(...)</code> method can
	 * have two different implementations, and both of them are correct:</p>
	 * 
	 * <pre>
	 * public Set loadSystemUsersButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
	 * 	return super.loadStorableObjects(ids, ObjectEntities.SYSTEMUSER_CODE, new TransmitProcedure() {
	 * 		public IDLEntity[] transmitStorableObjects(
	 * 				final CommonServer server,
	 * 				final IdlIdentifier[] idsT,
	 * 				final IdlSessionKey sessionKeyT)
	 * 				throws AMFICOMRemoteException {
	 * 			return ((CMServer) server).transmitUsersButIdsCondition(ids1, sessionKeyT, (StorableObjectCondition_Transferable) condition.getTransferable());
	 * 		}
	 * 	});
	 * }
	 * 
	 * public Set loadSystemUsersButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
	 * 	return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.SYSTEMUSER_CODE, new TransmitButIdsByConditionProcedure() {
	 * 		public IDLEntity[] transmitStorableObjectsButIdsCondition(
	 * 				final CommonServer server,
	 * 				final IdlIdentifier[] idsT,
	 * 				final IdlSessionKey sessionKeyT,
	 * 				final StorableObjectCondition_Transferable[] conditionT)
	 * 				throws AMFICOMRemoteException {
	 * 			return ((CMServer) server).transmitUsersButIdsCondition(ids1, sessionKeyT, condition1);
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
	 * <p>Overridden in <code>MCMObjectLoader</code> and <code>CORBACMServerObjectLoader</code>.</p>
	 */
	protected Set loadStorableObjectsButIdsByCondition(final short entityCode,
			final Set<Identifier> ids,
			final StorableObjectCondition condition,
			final TransmitButIdsByConditionProcedure transmitButIdsConditionProcedure) throws ApplicationException {
		final CommonServer server = this.serverConnectionManager.getServerReference();
		final IdlIdentifier[] idsT = Identifier.createTransferables(ids);
		final IdlStorableObjectCondition conditionT = (IdlStorableObjectCondition) condition.getTransferable();
		int numEfforts = 0;
		while (true) {
			try {
				final IdlSessionKey sessionKeyT = LoginManager.getSessionKeyTransferable();
				final IDLEntity[] transferables = transmitButIdsConditionProcedure.transmitStorableObjectsButIdsCondition(server,
						idsT,
						sessionKeyT,
						conditionT);
				return StorableObjectPool.fromTransferables(entityCode, transferables, true);
			}
			catch (final AMFICOMRemoteException are) {
				switch (are.errorCode.value()) {
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
	protected final void saveStorableObjects(final short entityCode,
			final Set<? extends StorableObject> storableObjects,
			final ReceiveProcedure receiveProcedure) throws ApplicationException {
		final CommonServer server = this.serverConnectionManager.getServerReference();

		final IDLEntity[] transferables = StorableObject.allocateArrayOfTransferables(entityCode, storableObjects.size());
		int i = 0;
		for (final Iterator storableObjectIterator = storableObjects.iterator(); storableObjectIterator.hasNext(); i++) {
			transferables[i] = ((StorableObject) storableObjectIterator.next()).getTransferable(this.serverConnectionManager.getCORBAServer().getOrb());
		}

		try {
			final IdlSessionKey sessionKeyT = LoginManager.getSessionKeyTransferable();
			final IdlStorableObject[] headers = receiveProcedure.receiveStorableObjects(server, transferables, sessionKeyT);
			StorableObject.updateHeaders(storableObjects, headers);
		}
		catch (final AMFICOMRemoteException are) {
			final String mesg = "Cannot save objects -- ";
			if (are.errorCode.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			if (are.errorCode.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new UpdateObjectException(mesg + are.message);
		}
	}
}
