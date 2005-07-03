/*
 * $Id: DatabaseIdentifierGeneratorServer.java,v 1.5 2005/06/17 13:06:58 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;

/**
 * @version $Revision: 1.5 $, $Date: 2005/06/17 13:06:58 $
 * @author $Author: bass $
 * @module csbridge_v1
 */
public class DatabaseIdentifierGeneratorServer implements IdentifierGeneratorServer, IGSConnectionManager {
	private static final long serialVersionUID = -7936229317009903277L;

	public IdlIdentifier getGeneratedIdentifier(short entity) throws AMFICOMRemoteException {
		try {
			return (IdlIdentifier) IdentifierGenerator.generateIdentifier(entity).getTransferable();
		}
		catch (IllegalObjectEntityException e) {
			throw new AMFICOMRemoteException();
		}
		catch (IdentifierGenerationException e) {
			throw new AMFICOMRemoteException();
		}
	}

	public IdlIdentifier[] getGeneratedIdentifierRange(short entity, int size) throws AMFICOMRemoteException {
		try {
			Identifier[] identifiers = IdentifierGenerator.generateIdentifierRange(entity, size);
			IdlIdentifier[] transferables = new IdlIdentifier[identifiers.length];
			for (int i = 0; i < identifiers.length; i++)
				transferables[i] = (IdlIdentifier) identifiers[i].getTransferable();

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

	public IdentifierGeneratorServer getIGSReference() {
		return this;
	}

}
