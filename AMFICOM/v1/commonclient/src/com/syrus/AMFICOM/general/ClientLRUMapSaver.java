/*-
* $Id: ClientLRUMapSaver.java,v 1.4 2005/09/08 05:33:33 bob Exp $
*
* Copyright © 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.util.LRUMap;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.4 $, $Date: 2005/09/08 05:33:33 $
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
	@Override
	protected Set<StorableObject> loading(final ObjectInputStream in) 
	throws IOException, ClassNotFoundException {
		try {
			return StorableObjectPool.fromTransferables((IdlStorableObject[]) in.readObject(), false);
		} catch (final ApplicationException ae) {
			Log.errorMessage("ClientLRUMapSaver.load | Error: " + ae.getMessage());
		}
		return null;
	}

//	 Арсений, заебал своими COSMETICs и менять постоянно code style
	@Override
	protected Object saving(final LRUMap<Identifier, StorableObject> lruMap) {
		final ORB orb = 
			ClientSessionEnvironment.getInstance().getConnectionManager().getCORBAServer().getOrb();
		final ArrayList<Object> keys = new ArrayList<Object>();
		for(final StorableObject storableObject : lruMap) {
			keys.add(storableObject.getTransferable(orb));
		}
		keys.trimToSize();
		return !keys.isEmpty() ? 
				keys.toArray(new IdlStorableObject[keys.size()]) :
				null;
	}
	
}
