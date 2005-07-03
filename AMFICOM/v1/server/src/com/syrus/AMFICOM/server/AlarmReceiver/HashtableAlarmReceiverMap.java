/*
 * $Id: HashtableAlarmReceiverMap.java,v 1.3 2004/06/29 07:12:57 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.server.AlarmReceiver;

import com.syrus.AMFICOM.CORBA.AMFICOMClient;
import com.syrus.AMFICOM.CORBA.Admin.AccessIdentity_Transferable;
import com.syrus.AMFICOM.CORBA.General.AMFICOMRemoteException;
import java.util.Hashtable;

/**
 * @version $Revision: 1.3 $, $Date: 2004/06/29 07:12:57 $
 * @author $Author: bass $
 * @module server_v1
 */
public final class HashtableAlarmReceiverMap extends AbstractAlarmReceiverMap {
	private Hashtable hashtable;

	public HashtableAlarmReceiverMap() {
		hashtable = new Hashtable();
	}

	public HashtableAlarmReceiverMap(int initialCapacity) {
		hashtable = new Hashtable(initialCapacity);
	}

	public HashtableAlarmReceiverMap(int initialCapacity, float loadFactor) {
		hashtable = new Hashtable(initialCapacity, loadFactor);
	}

	public boolean containsKey(AccessIdentity_Transferable key) throws AMFICOMRemoteException {
		return hashtable.containsKey(key);
	}

	public AMFICOMClient put(AccessIdentity_Transferable key, AMFICOMClient value) throws AMFICOMRemoteException {
		return (AMFICOMClient) hashtable.put(key, value);
	}

	public AMFICOMClient remove(AccessIdentity_Transferable key) throws AMFICOMRemoteException {
		return (AMFICOMClient) hashtable.remove(key);
	}
}
