/*
 * $Id: XMLIdentifierGeneratorServer.java,v 1.1 2005/02/04 14:21:00 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.HashMap;
import java.util.Map;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.1 $, $Date: 2005/02/04 14:21:00 $
 * @author $Author: bob $
 * @module tools
 */
public class XMLIdentifierGeneratorServer implements IdentifierGeneratorServer {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3832901065850040630L;

	private Map					entityCount			= new HashMap();

	public Identifier_Transferable getGeneratedIdentifier(short entity) throws AMFICOMRemoteException {
		long minor;
		Short code = new Short(entity);
		Long count = (Long) this.entityCount.get(code);
		if (count == null)
			minor = 0;
		else
			minor = count.longValue() + 1;
		this.entityCount.put(code, new Long(minor));
		Identifier identifier = new Identifier(ObjectEntities.codeToString(code) + Identifier.SEPARATOR + minor);
		return (Identifier_Transferable) identifier.getTransferable();

	}

	public Identifier_Transferable[] getGeneratedIdentifierRange(short entity, int size) throws AMFICOMRemoteException {
		Identifier_Transferable[] transferables = new Identifier_Transferable[size];
		for (int i = 0; i < transferables.length; i++) {
			transferables[i] = this.getGeneratedIdentifier(entity);
		}
		return transferables;
	}

}
