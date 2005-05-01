/*
 * $Id: MServerImplementation.java,v 1.53 2005/05/01 19:17:31 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.mserver;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierGenerator;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.53 $, $Date: 2005/05/01 19:17:31 $
 * @author $Author: arseniy $
 * @module mserver_v1
 */

public class MServerImplementation extends MServerMeasurementTransmit {

	private static final long serialVersionUID = 395371850379497709L;

	/*	Identifier Generator */

	public Identifier_Transferable getGeneratedIdentifier(short entityCode) throws AMFICOMRemoteException {
		try {
			Identifier identifier = IdentifierGenerator.generateIdentifier(entityCode);
			return (Identifier_Transferable) identifier.getTransferable();
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
					"Cannot create major/minor entries of identifier for entity: '" + ObjectEntities.codeToString(entityCode)
							+ "' -- " + ige.getMessage());
		}
	}

	public Identifier_Transferable[] getGeneratedIdentifierRange(short entityCode, int size) throws AMFICOMRemoteException {
		try {
			Identifier[] identifiers = IdentifierGenerator.generateIdentifierRange(entityCode, size);
			Identifier_Transferable[] identifiersT = new Identifier_Transferable[identifiers.length];
			for (int i = 0; i < identifiersT.length; i++)
				identifiersT[i] = (Identifier_Transferable) identifiers[i].getTransferable();
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
					"Cannot create major/minor entries of identifier for entity: '" + ObjectEntities.codeToString(entityCode)
							+ "' -- " + ige.getMessage());
		}
	}



	/* Connection verificaton */
	public void verify(byte i) {
		Log.debugMessage("Verify value: " + i, Log.DEBUGLEVEL10);
	}

}
