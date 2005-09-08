/*-
* $Id: ClientLRUMapSaver.java,v 1.5 2005/09/08 07:28:17 bob Exp $
*
* Copyright © 2005 Syrus Systems.
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
 * @version $Revision: 1.5 $, $Date: 2005/09/08 07:28:17 $
 * @author $Author: bob $
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

//	 Арсений, заебал своими COSMETICs и менять постоянно code style
	@SuppressWarnings("unchecked")
	@Override
	protected Set<StorableObject> loading(final ObjectInputStream in) 
	throws IOException, ClassNotFoundException {
		return (Set<StorableObject>) in.readObject();
	}

//	 Арсений, заебал своими COSMETICs и менять постоянно code style
	@Override
	protected Object saving(final LRUMap<Identifier, StorableObject> lruMap) {
		final Set<StorableObject> keys = new HashSet<StorableObject>();
		for(final StorableObject storableObject : lruMap) {
			keys.add(storableObject);
		}
		return !keys.isEmpty() ? keys :	null;
	}
	
}
