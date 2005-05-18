/*
 * $Id: CORBAObjectLoader.java,v 1.9 2005/05/18 12:52:58 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.util.Set;

import com.syrus.AMFICOM.cmserver.corba.CMServer;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.9 $, $Date: 2005/05/18 12:52:58 $
 * @author $Author: bass $
 * @module csbridge_v1
 */
public abstract class CORBAObjectLoader extends ObjectLoader {
	protected CMServerConnectionManager cmServerConnectionManager;

	protected CORBAObjectLoader (CMServerConnectionManager cmServerConnectionManager) {
		this.cmServerConnectionManager = cmServerConnectionManager;
	}

	/*	Delete*/

	public void delete(Set identifiables) {
		try {
			CMServer cmServer = this.cmServerConnectionManager.getCMServerReference();
			SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

			Identifier_Transferable[] idsT = Identifier.createTransferables(identifiables);
			cmServer.delete(idsT, sessionKeyT);
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
		}
		catch (AMFICOMRemoteException are) {
			Log.errorMessage("CORBAGeneralObjectLoader.delete | Cannot delete objects '" + identifiables + "'" + are.message);
		}
	}

}
