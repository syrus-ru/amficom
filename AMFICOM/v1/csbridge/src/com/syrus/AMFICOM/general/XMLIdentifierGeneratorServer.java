/*
 * $Id: XMLIdentifierGeneratorServer.java,v 1.4 2005/06/17 13:06:58 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.HashMap;
import java.util.Map;

import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;

/**
 * @version $Revision: 1.4 $, $Date: 2005/06/17 13:06:58 $
 * @author $Author: bass $
 * @module csbridge_v1
 */
public class XMLIdentifierGeneratorServer implements IdentifierGeneratorServer, IGSConnectionManager {
	private static final long serialVersionUID = -2081866914276561857L;

	private Map entityCount = new HashMap();

	public IdlIdentifier getGeneratedIdentifier(short entity) {
		long minor;
		Short code = new Short(entity);
		Long count = (Long) this.entityCount.get(code);
		if (count == null)
			minor = 0;
		else
			minor = count.longValue() + 1;
		this.entityCount.put(code, new Long(minor));
		Identifier identifier = new Identifier(ObjectEntities.codeToString(code) + Identifier.SEPARATOR + minor);
		return (IdlIdentifier) identifier.getTransferable();

	}

	public IdlIdentifier[] getGeneratedIdentifierRange(short entity, int size) {
		IdlIdentifier[] transferables = new IdlIdentifier[size];
		for (int i = 0; i < transferables.length; i++) {
			transferables[i] = this.getGeneratedIdentifier(entity);
		}
		return transferables;
	}

	public void verify(byte i) {
		throw new UnsupportedOperationException("It is dumb to call this method -- " + i);
	}

	public IdentifierGeneratorServer getIGSReference() {
		return this;
	}

}
