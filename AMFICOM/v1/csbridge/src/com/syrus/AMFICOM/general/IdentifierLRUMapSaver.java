/*-
 * $Id: IdentifierLRUMapSaver.java,v 1.1 2005/12/02 15:20:09 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.syrus.util.LRUMap;
import com.syrus.util.LRUMapSaver;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2005/12/02 15:20:09 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */
public final class IdentifierLRUMapSaver extends LRUMapSaver<Identifier, StorableObject> {
	private static final String FILE_SUFFIX = ".lri";
	private static final Identifier[] EMPTY_IDENTIFIERS = new Identifier[0];
	private static final StorableObject[] EMPTY_STORABLE_OBJECTS = new StorableObject[0];


	private ObjectLoader objectLoader;

	public IdentifierLRUMapSaver(final ObjectLoader objectLoader) {
		super(FILE_SUFFIX);
		this.objectLoader = objectLoader;
	}

	@Override
	protected void populateLRUMap(final LRUMap<Identifier, StorableObject> lruMap, final Object readObject) {
		final Object[] readObjects = (Object[]) readObject;
		final Identifier[] readIds = new Identifier[readObjects.length];
		System.arraycopy(readObjects, 0, readIds, 0, readObjects.length);

		final Set<Identifier> ids = new HashSet<Identifier>(Arrays.asList(readIds));
		try {
			final Set<StorableObject> storableObjects = this.objectLoader.loadStorableObjects(ids);
			final Identifier[] keys = new Identifier[storableObjects.size()];
			final StorableObject[] values = new StorableObject[keys.length];
			int i = 0;
			for (final StorableObject storableObject : storableObjects) {
				keys[i] = storableObject.getId();
				values[i] = storableObject;
				i++;
			}
			lruMap.populate(keys, values);
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
			lruMap.populate(EMPTY_IDENTIFIERS, EMPTY_STORABLE_OBJECTS);
		}
	}

	@Override
	protected Object getObjectToWrite(final LRUMap<Identifier, StorableObject> lruMap) {
		return lruMap.getKeys();
	}

}
