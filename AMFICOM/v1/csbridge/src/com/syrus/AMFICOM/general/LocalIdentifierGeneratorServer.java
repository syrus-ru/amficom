/*
 * $Id: LocalIdentifierGeneratorServer.java,v 1.6 2005/07/03 19:16:25 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.CompletionStatus;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.ErrorCode;
import com.syrus.util.Log;
/**
 * @version $Revision: 1.6 $, $Date: 2005/07/03 19:16:25 $
 * @author $Author: bass $
 * @module csbridge_v1
 */

public class LocalIdentifierGeneratorServer implements IdentifierGeneratorServer, IGSConnectionManager {
	private static final long serialVersionUID = 4417075411484576241L;

	public LocalIdentifierGeneratorServer() {
		//nothing
	}

	public IdlIdentifier getGeneratedIdentifier(short entityCode) throws AMFICOMRemoteException {
		try {
			Identifier identifier = LocalIdentifierGenerator.generateIdentifier(entityCode);
			return identifier.getTransferable();
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
																			 CompletionStatus.COMPLETED_NO,
																			 "Illegal object entity: '" + ObjectEntities.codeToString(entityCode) + "'");
		}	
	}

	public IdlIdentifier[] getGeneratedIdentifierRange(short entityCode, int size) throws AMFICOMRemoteException {
		try {
			Identifier[] identifiers = LocalIdentifierGenerator.generateIdentifierRange(entityCode, size);
			IdlIdentifier[] identifiersT = new IdlIdentifier[identifiers.length];
			for (int i = 0; i < identifiersT.length; i++)
				identifiersT[i] = identifiers[i].getTransferable();
			return identifiersT;
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
																			 CompletionStatus.COMPLETED_NO,
																			 "Illegal object entity: '" + ObjectEntities.codeToString(entityCode) + "'");
		}
	}

	public void verify(byte i) {
		throw new UnsupportedOperationException("It is dumb to call this method -- " + i);
	}

	public IdentifierGeneratorServer getIGSReference() {
		return this;
	}
}
