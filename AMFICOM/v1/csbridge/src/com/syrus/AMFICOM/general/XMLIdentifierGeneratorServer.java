/*
 * $Id: XMLIdentifierGeneratorServer.java,v 1.14 2005/10/31 12:29:53 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.14 $, $Date: 2005/10/31 12:29:53 $
 * @author $Author: bass $
 * @author Voffka
 * @module csbridge
 */
public class XMLIdentifierGeneratorServer implements IdentifierGeneratorServer, IGSConnectionManager {
	private static final long serialVersionUID = -2081866914276561857L;

	private Map<Short, Long> entityCount = new HashMap<Short, Long>();
	
	private XMLObjectLoader	objectLoader;

	/**
	 * @param objectLoader
	 */
	public XMLIdentifierGeneratorServer(final XMLObjectLoader objectLoader) {
		this.objectLoader = objectLoader;
	}

	public IdlIdentifier getGeneratedIdentifier(final short entityCode) {
		final Short code = new Short(entityCode);

		Long count = this.entityCount.get(code);
		if (count == null) {
			StorableObjectXML storableObjectXML = this.objectLoader.getStorableObjectXML();
			try {
				final SortedSet<Identifier> ids = new TreeSet<Identifier>(storableObjectXML.getIdentifiers(entityCode));
				Identifier id = null;
				if (!ids.isEmpty()) {
					id = ids.last();
					Log.debugMessage("last id is " + id.getIdentifierString(),
							Log.DEBUGLEVEL10);
				}

				count = new Long((id != null) ? id.getMinor() : -1);

			} catch (IllegalDataException e) {
				// nothing;
			}
		}

		final long minor = count.longValue() + 1;
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
