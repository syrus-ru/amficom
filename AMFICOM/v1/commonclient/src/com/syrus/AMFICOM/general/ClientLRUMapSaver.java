/*-
* $Id: ClientLRUMapSaver.java,v 1.7 2005/09/28 13:54:19 arseniy Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashSet;
import java.util.Set;

import com.syrus.util.LRUMap;


/**
 * Client LRU Map saver, save only unchanged storable objects
 * 
 * @version $Revision: 1.7 $, $Date: 2005/09/28 13:54:19 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module general
 */
final class ClientLRUMapSaver extends AbstractLRUMapSaver {

	private static ClientLRUMapSaver instance;

	private ClientLRUMapSaver() {
		super("SOLRUMap.serialized");
	}
	
	public static final synchronized ClientLRUMapSaver getInstance() {
		if (instance == null) {
			instance = new ClientLRUMapSaver();
		}
		return instance;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Set<StorableObject> loading(final ObjectInputStream in) 
	throws IOException, ClassNotFoundException {
		return (Set<StorableObject>) in.readObject();
	}

	@Override
	protected Object saving(final LRUMap<Identifier, StorableObject> lruMap) {
		final Set<StorableObject> keys = new HashSet<StorableObject>();
		for(final StorableObject storableObject : lruMap) {
			if (!storableObject.isChanged()) {
				keys.add(storableObject);
			}
		}
		return keys;
	}
	
}
