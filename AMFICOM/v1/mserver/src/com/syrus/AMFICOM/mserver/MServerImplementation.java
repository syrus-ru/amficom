/*
 * $Id: MServerImplementation.java,v 1.3 2004/07/28 17:03:50 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mserver;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerator;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.AMFICOM.mserver.corba._MServerImplBase;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.3 $, $Date: 2004/07/28 17:03:50 $
 * @author $Author: arseniy $
 * @module mserver_v1
 */

public class MServerImplementation extends _MServerImplBase {

	public MServerImplementation() {
		
	}
	
	public Identifier_Transferable getGeneratedIdentifier(String entity) throws AMFICOMRemoteException {
		try {
			Identifier identifier = IdentifierGenerator.generateIdentifier(entity);
			return (Identifier_Transferable)identifier.getTransferable();
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
																			 CompletionStatus.COMPLETED_NO,
																			 "Illegal object entity: '" + entity + "'");
		}
		catch (IdentifierGenerationException ige) {
			Log.errorException(ige);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE,
																			 CompletionStatus.COMPLETED_NO,
																			 "Cannot create major/minor entries of identifier for entity: '" + entity + "'");
		}	
	}
	
	public Identifier_Transferable[] getGeneratedIdentifierRange(String entity, int size) throws AMFICOMRemoteException {
		try {
			Identifier[] identifiers = IdentifierGenerator.generateIdentifierRange(entity, size);
			Identifier_Transferable[] identifiersT = new Identifier_Transferable[identifiers.length];
			for (int i = 0; i < identifiersT.length; i++)
				identifiersT[i] = (Identifier_Transferable)identifiers[i].getTransferable();
			return identifiersT;
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
																			 CompletionStatus.COMPLETED_NO,
																			 "Illegal object entity: '" + entity + "'");
		}
		catch (IdentifierGenerationException ige) {
			Log.errorException(ige);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE,
																			 CompletionStatus.COMPLETED_NO,
																			 "Cannot create major/minor entries of identifier for entity: '" + entity + "'");
		}
	}

	public void transmitTests(Test_Transferable[] tests) throws AMFICOMRemoteException {
		System.out.println("length: " + tests.length);
		for (int i = 0; i < tests.length; i++) {
			
		}
	}
	
	public void ping(int i) throws AMFICOMRemoteException {
		System.out.println("i == " + i);
	}
}
