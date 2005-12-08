/*-
 * $Id: IdentifierLRUMapSaver.java,v 1.2 2005/12/08 15:30:54 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.syrus.util.LRUMap;
import com.syrus.util.LRUMapSaver;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.2 $, $Date: 2005/12/08 15:30:54 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */
public final class IdentifierLRUMapSaver extends LRUMapSaver<Identifier, StorableObject> {
	private static final String FILE_SUFFIX = ".lri";

	private ObjectLoader objectLoader;

	public IdentifierLRUMapSaver(final ObjectLoader objectLoader) {
		super(FILE_SUFFIX);
		this.objectLoader = objectLoader;
	}

	@Override
	protected Map<Identifier, StorableObject> getMap(final Object readObject) {
		final HashSet<Identifier> readIds = (HashSet<Identifier>) readObject;
		final Map<Identifier, StorableObject> map = new HashMap<Identifier, StorableObject>(readIds.size());
		try {
			final Set<StorableObject> storableObjects = this.objectLoader.loadStorableObjects(readIds);
			for (final StorableObject storableObject : storableObjects) {
				map.put(storableObject.getId(), storableObject);
			}
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
		}
		return map;
	}

	@Override
	protected Object getObjectToWrite(final LRUMap<Identifier, StorableObject> lruMap) {
		return new HashSet<Identifier>(lruMap.keySet());
	}

}
