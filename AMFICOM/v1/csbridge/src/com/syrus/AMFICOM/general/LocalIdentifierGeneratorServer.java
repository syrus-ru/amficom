/*
 * $Id: LocalIdentifierGeneratorServer.java,v 1.3 2005/05/18 12:52:58 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.util.Log;
/**
 * @version $Revision: 1.3 $, $Date: 2005/05/18 12:52:58 $
 * @author $Author: bass $
 * @module csbridge_v1
 */

public class LocalIdentifierGeneratorServer implements IdentifierGeneratorServer, IGSConnectionManager {
	private static final long serialVersionUID = 4417075411484576241L;

	public LocalIdentifierGeneratorServer() {
		//nothing
	}

	public Identifier_Transferable getGeneratedIdentifier(short entityCode) throws AMFICOMRemoteException {
		try {
			Identifier identifier = LocalIdentifierGenerator.generateIdentifier(entityCode);
			return (Identifier_Transferable)identifier.getTransferable();
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
																			 CompletionStatus.COMPLETED_NO,
																			 "Illegal object entity: '" + ObjectEntities.codeToString(entityCode) + "'");
		}	
	}

	public Identifier_Transferable[] getGeneratedIdentifierRange(short entityCode, int size) throws AMFICOMRemoteException {
		try {
			Identifier[] identifiers = LocalIdentifierGenerator.generateIdentifierRange(entityCode, size);
			Identifier_Transferable[] identifiersT = new Identifier_Transferable[identifiers.length];
			for (int i = 0; i < identifiersT.length; i++)
				identifiersT[i] = (Identifier_Transferable)identifiers[i].getTransferable();
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
