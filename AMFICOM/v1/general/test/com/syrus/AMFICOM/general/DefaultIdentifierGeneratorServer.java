/*
 * $Id: DefaultIdentifierGeneratorServer.java,v 1.1 2005/02/04 14:21:00 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierGenerator;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.1 $, $Date: 2005/02/04 14:21:00 $
 * @author $Author: bob $
 * @module tools
 */
public class DefaultIdentifierGeneratorServer implements IdentifierGeneratorServer {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3832901065850040630L;

	public Identifier_Transferable getGeneratedIdentifier(short entity) throws AMFICOMRemoteException {
		try {
			return (Identifier_Transferable) IdentifierGenerator.generateIdentifier(entity).getTransferable();
		} catch (IllegalObjectEntityException e) {
			throw new AMFICOMRemoteException();
		} catch (IdentifierGenerationException e) {
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
		} catch (IllegalObjectEntityException e) {
			throw new AMFICOMRemoteException();
		} catch (IdentifierGenerationException e) {
			throw new AMFICOMRemoteException();
		}
	}

}
