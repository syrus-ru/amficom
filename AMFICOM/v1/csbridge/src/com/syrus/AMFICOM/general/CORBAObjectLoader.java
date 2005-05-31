/*
 * $Id: CORBAObjectLoader.java,v 1.11 2005/05/31 14:54:42 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Collections;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.11 $, $Date: 2005/05/31 14:54:42 $
 * @author $Author: bass $
 * @module csbridge_v1
 */
public abstract class CORBAObjectLoader extends ObjectLoader {
	protected ServerConnectionManager serverConnectionManager;

	protected CORBAObjectLoader (final ServerConnectionManager serverConnectionManager) {
		this.serverConnectionManager = serverConnectionManager;
	}

	/*	Delete*/

	public final void delete(final Set identifiables) {
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

	public final Set refresh(final Set storableObjects) throws ApplicationException {
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

	protected interface TransmitProcedure {
		IDLEntity[] transmitStorableObjects(
				final CommonServer server,
				final Identifier_Transferable ids[],
				final SessionKey_Transferable sessionKey)
				throws AMFICOMRemoteException;
	}

	protected final Set loadStorableObjects(final Set ids,
			final short entityCode,
			final TransmitProcedure transmitProcedure)
			throws ApplicationException {
		final CommonServer server = this.serverConnectionManager.getServerReference();
		final Identifier_Transferable ids1[] = Identifier.createTransferables(ids);
		final SessionKey_Transferable sessionKey = LoginManager.getSessionKeyTransferable();
		int numEfforts = 0;
		while (true) {
			try {
				final IDLEntity transferables[] = transmitProcedure.transmitStorableObjects(server, ids1, sessionKey);
				return StorableObjectPool.fromTransferablesLocal(entityCode, transferables, true);
			} catch (final AMFICOMRemoteException are) {
				switch (are.error_code.value()) {
					case ErrorCode._ERROR_NOT_LOGGED_IN:
						if (++numEfforts == 1) {
							if (LoginManager.restoreLogin()) {
								continue;
							}
							Log.debugMessage("CORBAMapObjectLoader.loadStorableObjects() | Login restoration cancelled by user", Log.INFO);
							return Collections.EMPTY_SET;
						}
						throw new LoginException(are.message);
					default:
						throw new RetrieveObjectException(are.message);
				}
			}
		}
	}
}
