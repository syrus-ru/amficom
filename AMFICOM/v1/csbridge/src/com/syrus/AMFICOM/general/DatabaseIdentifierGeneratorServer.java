/*
 * $Id: DatabaseIdentifierGeneratorServer.java,v 1.7 2005/07/27 13:40:45 arseniy Exp $
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
 * @version $Revision: 1.7 $, $Date: 2005/07/27 13:40:45 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public class DatabaseIdentifierGeneratorServer implements IdentifierGeneratorServer, IGSConnectionManager {
	private static final long serialVersionUID = -7936229317009903277L;

	public IdlIdentifier getGeneratedIdentifier(final short entity) throws AMFICOMRemoteException {
		try {
			return IdentifierGenerator.generateIdentifier(entity).getTransferable();
		}
		catch (IllegalObjectEntityException e) {
			throw new AMFICOMRemoteException();
		}
		catch (IdentifierGenerationException e) {
			throw new AMFICOMRemoteException();
		}
	}

	public IdlIdentifier[] getGeneratedIdentifierRange(final short entity, final int size) throws AMFICOMRemoteException {
		try {
			final Identifier[] identifiers = IdentifierGenerator.generateIdentifierRange(entity, size);
			final IdlIdentifier[] transferables = new IdlIdentifier[identifiers.length];
			for (int i = 0; i < identifiers.length; i++) {
				transferables[i] = identifiers[i].getTransferable();
			}

			return transferables;
		}
		catch (IllegalObjectEntityException e) {
			throw new AMFICOMRemoteException();
		}
		catch (IdentifierGenerationException e) {
			throw new AMFICOMRemoteException();
		}
	}

	public void verify(final byte i) {
		assert false : "It is dumb to call this method -- " + i;
	}

	public IdentifierGeneratorServer getIGSReference() {
		return this;
	}

}
