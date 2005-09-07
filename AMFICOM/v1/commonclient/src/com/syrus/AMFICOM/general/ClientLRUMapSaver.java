/*-
* $Id: ClientLRUMapSaver.java,v 1.3 2005/09/07 14:11:42 arseniy Exp $
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

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.util.LRUMap;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.3 $, $Date: 2005/09/07 14:11:42 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module general
 */
final class ClientLRUMapSaver extends AbstractLRUMapSaver {

	private static ClientLRUMapSaver instance;

	private ClientLRUMapSaver() {
		super("SOLRUMap.serialized");
	}
	
	public static final ClientLRUMapSaver getInstance() {
		if (instance == null) {
			synchronized (ClientLRUMapSaver.class) {
				if (instance == null) {
					instance = new ClientLRUMapSaver();
				}
			}
		}
		return instance;
	}

	@Override
	protected Set<StorableObject> loading(final ObjectInputStream in) throws IOException, ClassNotFoundException {
		try {
			return StorableObjectPool.fromTransferables((IdlStorableObject[]) in.readObject(), false);
		} catch (final ApplicationException ae) {
			Log.errorMessage("ClientLRUMapSaver.load | Error: " + ae.getMessage());
		}
		return null;
	}

	@Override
	protected Object saving(final LRUMap<Identifier, StorableObject> lruMap) {
		final ORB orb = ClientSessionEnvironment.getInstance().getConnectionManager().getCORBAServer().getOrb();
		final Set<Object> keys = new HashSet<Object>();
		for(final StorableObject storableObject : lruMap) {
			keys.add(storableObject.getTransferable(orb));
		}
		return keys.toArray(new IdlStorableObject[keys.size()]);
	}
	
}
