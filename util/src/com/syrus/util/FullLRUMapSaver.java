/*-
 * $Id: FullLRUMapSaver.java,v 1.1 2005/12/02 15:14:24 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.util;

import com.syrus.util.LRUMap.IEntry;

/**
 * @version $Revision: 1.1 $, $Date: 2005/12/02 15:14:24 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module util
 */
public final class FullLRUMapSaver<K, V> extends LRUMapSaver<K, V> {
	private static final String FILE_SUFFIX = ".lru";

	public FullLRUMapSaver() {
		super(FILE_SUFFIX);
	}

	@Override
	protected void populateLRUMap(final LRUMap<K, V> lruMap, final Object readObject) {
		final Object[] readObjects = (Object[]) readObject;
		final IEntry<K, V>[] readEntries = new IEntry[readObjects.length];
		System.arraycopy(readObjects, 0, readEntries, 0, readObjects.length);

		lruMap.populate(readEntries);
	}

	@Override
	protected Object getObjectToWrite(final LRUMap<K, V> lruMap) {
		return lruMap.getEntries();
	}
}
