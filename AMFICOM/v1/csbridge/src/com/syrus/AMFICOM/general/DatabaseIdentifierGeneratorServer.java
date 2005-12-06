/*
 * $Id: DatabaseIdentifierGeneratorServer.java,v 1.11 2005/12/06 09:41:41 bass Exp $
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
 * @version $Revision: 1.11 $, $Date: 2005/12/06 09:41:41 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */
public class DatabaseIdentifierGeneratorServer implements IdentifierGeneratorServer, IGSConnectionManager {
	private static final long serialVersionUID = -7936229317009903277L;

	public IdlIdentifier getGeneratedIdentifier(final short entity) throws AMFICOMRemoteException {
		try {
			return IdentifierGenerator.generateIdentifier(entity).getIdlTransferable();
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
				transferables[i] = identifiers[i].getIdlTransferable();
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

	public IdentifierGeneratorServer getIGSReference() {
		return this;
	}

}
