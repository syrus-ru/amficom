package com.syrus.AMFICOM.server.AlarmReceiver;

import com.syrus.AMFICOM.CORBA.*;
import com.syrus.AMFICOM.CORBA.Admin.*;
import com.syrus.AMFICOM.CORBA.General.*;
import java.util.*;

/**
 * @version $Revision: 1.1 $, $Date: 2004/05/27 11:20:11 $
 * @author $Author: bass $
 */
public class HashtableAlarmReceiverMap extends AbstractAlarmReceiverMap
{
	private Hashtable hashtable;

	public HashtableAlarmReceiverMap()
	{
		hashtable = new Hashtable();
	}

	public HashtableAlarmReceiverMap(int initialCapacity)
	{
		hashtable = new Hashtable(initialCapacity);
	}

	public HashtableAlarmReceiverMap(int initialCapacity, float loadFactor)
	{
		hashtable = new Hashtable(initialCapacity, loadFactor);
	}

	public boolean containsKey(AccessIdentity_Transferable key) throws AMFICOMRemoteException
	{
		return hashtable.containsKey(key);
	}

	public AMFICOMClient put(AccessIdentity_Transferable key, AMFICOMClient value) throws AMFICOMRemoteException
	{
		return (AMFICOMClient) hashtable.put(key, value);
	}

	public AMFICOMClient remove(AccessIdentity_Transferable key) throws AMFICOMRemoteException
	{
		return (AMFICOMClient) hashtable.remove(key);
	}
}
