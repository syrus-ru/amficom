/*
 * $Id: CORBAObjectLoader.java,v 1.1 2005/04/22 17:14:49 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.cmserver.corba.CMServer;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2005/04/22 17:14:49 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public abstract class CORBAObjectLoader {
	protected CMServerConnectionManager cmServerConnectionManager;

	protected CORBAObjectLoader (CMServerConnectionManager cmServerConnectionManager) {
		this.cmServerConnectionManager = cmServerConnectionManager;
	}

	public void delete(Identifier id) {
		try {
			CMServer cmServer = this.cmServerConnectionManager.getCMServerReference();
			AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

			Identifier_Transferable idT = (Identifier_Transferable) id.getTransferable();
			cmServer.delete(idT, ait);
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
		}
		catch (AMFICOMRemoteException are) {
			Log.errorMessage("CORBAGeneralObjectLoader.delete | Cannot delete object '" + id + "'" + are.message);
		}
	}

	public void delete(Set identifiables) {
		try {
			CMServer cmServer = this.cmServerConnectionManager.getCMServerReference();
			AccessIdentifier_Transferable ait = ClientSession.getAccessIdentifierTransferable();

			Identifier_Transferable[] idsT = Identifier.createTransferables(identifiables);
			cmServer.deleteList(idsT, ait);
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
		}
		catch (AMFICOMRemoteException are) {
			Log.errorMessage("CORBAGeneralObjectLoader.delete | Cannot delete objects '" + identifiables + "'" + are.message);
		}
	}

	/**
	 * NOTE: this method removes updated objects from set, thus modifying the set.
	 * If you are planning to use the set somewhere after this method call - 
	 * create a copy of the set to supply to this method.  
	 * @param storableObjects
	 * @param headers
	 */
	protected final void updateHeaders(final Set storableObjects, final StorableObject_Transferable[] headers) {
		for (int i = 0; i < headers.length; i++) {
			final Identifier id = new Identifier(headers[i].id);
			for (Iterator it = storableObjects.iterator(); it.hasNext();) {
				StorableObject storableObject = (StorableObject) it.next();
				if (storableObject.getId().equals(id)) {
					storableObject.updateFromHeaderTransferable(headers[i]);
					it.remove();
					break;
				}
			}
		}
	}

}
