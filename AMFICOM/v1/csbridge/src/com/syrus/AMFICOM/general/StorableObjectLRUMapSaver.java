/*-
 * $Id: StorableObjectLRUMapSaver.java,v 1.3 2005/12/12 07:43:09 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.syrus.util.LRUMap;
import com.syrus.util.LRUMapSaver;

/**
 * @version $Revision: 1.3 $, $Date: 2005/12/12 07:43:09 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */
public final class StorableObjectLRUMapSaver extends LRUMapSaver<Identifier, StorableObject> {
	private static final String FILE_SUFFIX = ".lrso";

	public StorableObjectLRUMapSaver() {
		super(FILE_SUFFIX);
	}

	@Override
	protected Map<Identifier, StorableObject> getMap(final Object readObject) {
		final HashSet<StorableObject> readStorableObjects = (HashSet<StorableObject>) readObject;
		final Map<Identifier, StorableObject> map = new HashMap<Identifier, StorableObject>(readStorableObjects.size());
		for (final StorableObject storableObject : readStorableObjects) {
			map.put(storableObject.getId(), storableObject);
		}
		return map;
	}

	@Override
	protected Object getObjectToWrite(final LRUMap<Identifier, StorableObject> lruMap) {
		final HashSet<StorableObject> storableObjectsToWrite = new HashSet<StorableObject>(lruMap.size());
		for (final StorableObject storableObject : lruMap.values()) {
			if (!storableObject.isChanged()) {
				storableObjectsToWrite.add(storableObject);
			}
		}
		return storableObjectsToWrite;
	}

}
