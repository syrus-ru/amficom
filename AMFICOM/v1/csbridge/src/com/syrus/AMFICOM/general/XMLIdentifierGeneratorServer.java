/*
 * $Id: XMLIdentifierGeneratorServer.java,v 1.7 2005/08/08 11:38:11 arseniy Exp $
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
 * @version $Revision: 1.7 $, $Date: 2005/08/08 11:38:11 $
 * @author $Author: arseniy $
 * @module csbridge
 */
public class XMLIdentifierGeneratorServer implements IdentifierGeneratorServer, IGSConnectionManager {
	private static final long serialVersionUID = -2081866914276561857L;

	private Map<Short, Long> entityCount = new HashMap<Short, Long>();

	public IdlIdentifier getGeneratedIdentifier(final short entity) {
		final Short code = new Short(entity);
		final Long count = this.entityCount.get(code);
		final long minor = (count == null) ? 0 : count.longValue() + 1;
		this.entityCount.put(code, new Long(minor));
		final Identifier identifier = new Identifier(ObjectEntities.codeToString(code) + Identifier.SEPARATOR + minor);
		return identifier.getTransferable();

	}

	public IdlIdentifier[] getGeneratedIdentifierRange(final short entity, final int size) {
		final IdlIdentifier[] transferables = new IdlIdentifier[size];
		for (int i = 0; i < transferables.length; i++) {
			transferables[i] = this.getGeneratedIdentifier(entity);
		}
		return transferables;
	}

	public void verify(final byte i) {
		throw new UnsupportedOperationException("It is dumb to call this method -- " + i);
	}

	public IdentifierGeneratorServer getIGSReference() {
		return this;
	}

}
