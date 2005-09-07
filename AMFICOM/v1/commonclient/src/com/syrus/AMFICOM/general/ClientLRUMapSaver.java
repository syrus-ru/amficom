/*-
* $Id: ClientLRUMapSaver.java,v 1.2 2005/09/07 13:12:58 bob Exp $
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

import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.util.LRUMap;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.2 $, $Date: 2005/09/07 13:12:58 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module general
 */
final class ClientLRUMapSaver<V extends StorableObject> extends AbstractLRUMapSaver<V> {

	private static ClientLRUMapSaver<StorableObject> instance;

	private ClientLRUMapSaver() {
		super("SOLRUMap.serialized");
	}
	
	public static final ClientLRUMapSaver<StorableObject> getInstance() {
		if (instance == null) {
			synchronized (ClientLRUMapSaver.class) {
				if (instance == null) {
					instance = new ClientLRUMapSaver<StorableObject>();
				}
			}
		}
		return instance;
	}

	@Override
	protected Set<V> loading(final ObjectInputStream in) throws IOException, ClassNotFoundException {
		try {
			return StorableObjectPool.<V>fromTransferables((IdlStorableObject[]) in.readObject(), false);
		} catch (final ApplicationException ae) {
			Log.errorMessage("ClientLRUMapSaver.load | Error: " + ae.getMessage());
		}
		return null;
	}
	
	@Override
	protected Object saving(LRUMap<Identifier, V> lruMap) {
		final Set<Object> keys = new HashSet<Object>();
		for(final V v : lruMap) {
			keys.add(v.getTransferable(ClientSessionEnvironment.getInstance().getConnectionManager().getCORBAServer().getOrb()));
		}
		return keys.toArray(new IdlStorableObject[keys.size()]);
	}
	
}
