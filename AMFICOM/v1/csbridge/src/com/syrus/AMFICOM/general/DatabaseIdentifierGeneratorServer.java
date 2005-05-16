/*
 * $Id: DatabaseIdentifierGeneratorServer.java,v 1.3 2005/05/16 14:43:30 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.3 $, $Date: 2005/05/16 14:43:30 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public class DatabaseIdentifierGeneratorServer implements IdentifierGeneratorServer, IGSConnectionManager {
	private static final long serialVersionUID = -7936229317009903277L;

	public Identifier_Transferable getGeneratedIdentifier(short entity) throws AMFICOMRemoteException {
		try {
			return (Identifier_Transferable) IdentifierGenerator.generateIdentifier(entity).getTransferable();
		}
		catch (IllegalObjectEntityException e) {
			throw new AMFICOMRemoteException();
		}
		catch (IdentifierGenerationException e) {
			throw new AMFICOMRemoteException();
		}
	}

	public Identifier_Transferable[] getGeneratedIdentifierRange(short entity, int size) throws AMFICOMRemoteException {
		try {
			Identifier[] identifiers = IdentifierGenerator.generateIdentifierRange(entity, size);
			Identifier_Transferable[] transferables = new Identifier_Transferable[identifiers.length];
			for (int i = 0; i < identifiers.length; i++)
				transferables[i] = (Identifier_Transferable) identifiers[i].getTransferable();

			return transferables;
		}
		catch (IllegalObjectEntityException e) {
			throw new AMFICOMRemoteException();
		}
		catch (IdentifierGenerationException e) {
			throw new AMFICOMRemoteException();
		}
	}

	public void verify(byte i) {
		assert false : "It is dumb to call this method -- " + i;
	}

	public IdentifierGeneratorServer getIGSReference() throws CommunicationException {
		return this;
	}

}
