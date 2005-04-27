/*
 * $Id: LocalIdentifierGeneratorServer.java,v 1.2 2005/04/27 15:30:21 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.util.Log;
/**
 * @version $Revision: 1.2 $, $Date: 2005/04/27 15:30:21 $
 * @author $Author: arseniy $
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
		catch (IdentifierGenerationException ige) {
			Log.errorException(ige);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE,
																			 CompletionStatus.COMPLETED_NO,
																			 "Cannot create major/minor entries of identifier for entity: '" + ObjectEntities.codeToString(entityCode) + "' -- " + ige.getMessage());
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
		catch (IdentifierGenerationException ige) {
			Log.errorException(ige);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE,
																			 CompletionStatus.COMPLETED_NO,
																			 "Cannot create major/minor entries of identifier for entity: '" + ObjectEntities.codeToString(entityCode) + "' -- " + ige.getMessage());
		}
	}

	public void verify(byte i) {
		throw new UnsupportedOperationException("It is dumb to call this method -- " + i);
	}

	public IdentifierGeneratorServer getIGSReference() throws CommunicationException {
		return this;
	}
}
