/*
 * $Id: CORBAObjectLoader.java,v 1.10 2005/05/27 16:24:44 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Set;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.10 $, $Date: 2005/05/27 16:24:44 $
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
}
