/*-
 * $Id: XMLIdentifierFactory.java,v 1.1.2.1 2006/06/27 15:50:50 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.Identifier.SEPARATOR;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.1.2.1 $, $Date: 2006/06/27 15:50:50 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */
public final class XMLIdentifierFactory implements IdentifierFactory {
	private final XMLObjectLoader objectLoader;
	private final Map<Short, Long> entityCount;

	public XMLIdentifierFactory(final XMLObjectLoader objectLoader) {
		this.objectLoader = objectLoader;
		this.entityCount = new HashMap<Short, Long>();
	}

	public Identifier getGeneratedIdentifier(final short entityCode)
			throws CommunicationException,
				IdentifierGenerationException,
				IllegalObjectEntityException {
		final Short code = new Short(entityCode);

		Long count = this.entityCount.get(code);
		if (count == null) {
			final StorableObjectXML storableObjectXML = this.objectLoader.getStorableObjectXML();
			try {
				final SortedSet<Identifier> ids = new TreeSet<Identifier>(storableObjectXML.getIdentifiers(entityCode));
				final Identifier lastId;
				if (!ids.isEmpty()) {
					lastId = ids.last();
					Log.debugMessage("last id is " + lastId.getIdentifierString(),
							Log.DEBUGLEVEL10);
				} else {
					lastId = null;
				}

				count = Long.valueOf((lastId != null) ? lastId.getMinor() : -1);

			} catch (IllegalDataException e) {
				// nothing;
			}
		}

		final long minor = count.longValue() + 1;
		this.entityCount.put(code, Long.valueOf(minor));
		final Identifier identifier = Identifier.valueOf(ObjectEntities.codeToString(code) + SEPARATOR + minor);
		return identifier;
	}

	public Set<Identifier> getGeneratedIdentifierRange(final short entityCode, final int size)
			throws CommunicationException,
				IdentifierGenerationException,
				IllegalObjectEntityException {
		final Set<Identifier> ids = new HashSet<Identifier>();
		for (int i = 0; i < size; i++) {
			ids.add(this.getGeneratedIdentifier(entityCode));
		}
		return ids;
	}

}
