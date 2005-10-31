/*-
 * $Id: LRUMapSaver.java,v 1.10 2005/10/31 12:30:18 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.syrus.util.LRUMap;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.10 $, $Date: 2005/10/31 12:30:18 $
 * @author $Author: bass $
 * @author Vladimir Dolzhenko
 * @module general
 */
public class LRUMapSaver extends AbstractLRUMapSaver {

	private static LRUMapSaver instance;

	private LRUMapSaver() {
		super("LRUMap.serialized");
	}

	public static final synchronized LRUMapSaver getInstance() {
		if (instance == null) {
			instance = new LRUMapSaver();
		}
		return instance;
	}
  
	@SuppressWarnings("unchecked")
	@Override
	protected Set<StorableObject> loading(final ObjectInputStream in) throws IOException, ClassNotFoundException {
		try {
			return StorableObjectPool.getStorableObjects((Set) in.readObject(), true);
		} catch (final ApplicationException ae) {
			Log.errorMessage("Error: " + ae.getMessage());
		}
		return Collections.emptySet();
	}

	@Override
	protected Set<Object> saving(LRUMap<Identifier, StorableObject> lruMap) {
		final Set<Object> keys = new HashSet<Object>();
		for (final Iterator<Identifier> it = lruMap.keyIterator(); it.hasNext();) {
			final Identifier key = it.next();
			keys.add(key);
		}
		return keys;
	}	
}
