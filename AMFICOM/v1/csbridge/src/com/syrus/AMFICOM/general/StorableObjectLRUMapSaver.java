/*-
 * $Id: StorableObjectLRUMapSaver.java,v 1.1 2005/12/02 15:20:18 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import com.syrus.util.LRUMap;
import com.syrus.util.LRUMapSaver;

/**
 * @version $Revision: 1.1 $, $Date: 2005/12/02 15:20:18 $
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
	protected void populateLRUMap(final LRUMap<Identifier, StorableObject> lruMap, final Object readObject) {
		final Object[] readObjects = (Object[]) readObject;
		final StorableObject[] readStorableObjects = new StorableObject[readObjects.length];
		System.arraycopy(readObjects, 0, readStorableObjects, 0, readObjects.length);

		final Identifier[] ids = new Identifier[readStorableObjects.length];
		int i = 0;
		for (final StorableObject storableObject : readStorableObjects) {
			ids[i++] = storableObject.getId();
		}
		lruMap.populate(ids, readStorableObjects);
	}

	@Override
	protected Object getObjectToWrite(final LRUMap<Identifier, StorableObject> lruMap) {
		return lruMap.getValues();
	}

}
