/*
 * $Id: LocalIdentifierGeneratorServer.java,v 1.12 2005/10/31 12:29:53 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlCompletionStatus;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlErrorCode;
import com.syrus.util.Log;
/**
 * @version $Revision: 1.12 $, $Date: 2005/10/31 12:29:53 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */

public class LocalIdentifierGeneratorServer implements IdentifierGeneratorServer, IGSConnectionManager {
	private static final long serialVersionUID = 4417075411484576241L;

	public LocalIdentifierGeneratorServer() {
		//nothing
	}

	public IdlIdentifier getGeneratedIdentifier(final short entityCode) throws AMFICOMRemoteException {
		try {
			final Identifier identifier = LocalIdentifierGenerator.generateIdentifier(entityCode);
			return identifier.getTransferable();
		} catch (IllegalObjectEntityException ioee) {
			Log.errorMessage(ioee);
			throw new AMFICOMRemoteException(IdlErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
					IdlCompletionStatus.COMPLETED_NO,
					"Illegal object entity: '" + ObjectEntities.codeToString(entityCode) + "'");
		}
	}

	public IdlIdentifier[] getGeneratedIdentifierRange(final short entityCode, final int size) throws AMFICOMRemoteException {
		try {
			final Identifier[] identifiers = LocalIdentifierGenerator.generateIdentifierRange(entityCode, size);
			final IdlIdentifier[] identifiersT = new IdlIdentifier[identifiers.length];
			for (int i = 0; i < identifiersT.length; i++) {
				identifiersT[i] = identifiers[i].getTransferable();
			}
			return identifiersT;
		} catch (IllegalObjectEntityException ioee) {
			Log.errorMessage(ioee);
			throw new AMFICOMRemoteException(IdlErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
					IdlCompletionStatus.COMPLETED_NO,
					"Illegal object entity: '" + ObjectEntities.codeToString(entityCode) + "'");
		}
	}

	public void verify(final byte i) {
		throw new UnsupportedOperationException("It is dumb to call this method -- " + i);
	}

	public IdentifierGeneratorServer getIGSReference() {
		return this;
	}
}
