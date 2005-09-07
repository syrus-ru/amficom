/*-
 * $Id: LRUMapSaver.java,v 1.2 2005/09/07 13:12:48 bob Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.syrus.util.LRUMap;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.2 $, $Date: 2005/09/07 13:12:48 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module general
 */
public class LRUMapSaver<V extends StorableObject> extends AbstractLRUMapSaver<V> {

	private static LRUMapSaver<StorableObject> instance;
	
	private LRUMapSaver() {
		super("LRUMap.serialized");
	}
	
	public static final LRUMapSaver<StorableObject> getInstance() {
		if (instance == null) {
			synchronized (LRUMapSaver.class) {
				if (instance == null) {
					instance = new LRUMapSaver<StorableObject>();
				}
			}
		}
		return instance;
	}
	
	@Override
	protected Set<V> loading(final ObjectInputStream in) throws IOException, ClassNotFoundException {
		try {
			return StorableObjectPool.getStorableObjects((Set) in.readObject(), true);
		} catch (final ApplicationException ae) {
			Log.errorMessage("LRUMapSaver.load | Error: " + ae.getMessage());
		}
		return null;
	}
	
	@Override
	protected Set<Object> saving(LRUMap<Identifier, V> lruMap) {
		final Set<Object> keys = new HashSet<Object>();
		for (final Iterator it = lruMap.keyIterator(); it.hasNext();) {
			final Object key = it.next();
			keys.add(key);
		}
		return keys;
	}	
}
