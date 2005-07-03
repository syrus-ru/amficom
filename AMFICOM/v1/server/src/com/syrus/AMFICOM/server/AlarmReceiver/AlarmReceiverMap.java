/*
 * $Id: AlarmReceiverMap.java,v 1.3 2004/06/29 07:12:57 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.server.AlarmReceiver;

import com.syrus.AMFICOM.CORBA.*;
import com.syrus.AMFICOM.CORBA.Admin.*;
import com.syrus.AMFICOM.CORBA.General.*;

/**
 * @version $Revision: 1.3 $, $Date: 2004/06/29 07:12:57 $
 * @author $Author: bass $
 * @module server_v1
 */
public interface AlarmReceiverMap {
	/**
	 * Returns <tt>true</tt> if this map contains a mapping for the specified
	 * key.  More formally, returns <tt>true</tt> if and only if
	 * this map contains at a mapping for a key <tt>k</tt> such that
	 * <tt>(key==null ? k==null : key.equals(k))</tt>.  (There can be
	 * at most one such mapping.)
	 *
	 * @param key key whose presence in this map is to be tested.
	 * @return <tt>true</tt> if this map contains a mapping for the specified
	 *         key.
	 * @throws NullPointerException if the key is <tt>null</tt>.
	 * @throws AMFICOMRemoteException
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	boolean containsKey(AccessIdentity_Transferable key)
			throws AMFICOMRemoteException;

	/**
	 * Associates the specified value with the specified key in this map
	 * (optional operation).  If the map previously contained a mapping for
	 * this key, the old value is replaced by the specified value.  (A map
	 * <tt>m</tt> is said to contain a mapping for a key <tt>k</tt> if and only
	 * if {@link
	 * #containsKey(com.syrus.AMFICOM.CORBA.Admin.AccessIdentity_Transferable)
	 * m.containsKey(k)} would return <tt>true</tt>.)) 
	 *
	 * @param key key with which the specified value is to be associated.
	 * @param value value to be associated with the specified key.
	 * @return previous value associated with specified key, or <tt>null</tt>
	 *	       if there was no mapping for key.
	 * @throws NullPointerException if the specified key or value is
	 *            <tt>null</tt>.
	 * @throws AMFICOMRemoteException
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	AMFICOMClient put(AccessIdentity_Transferable key, AMFICOMClient value)
			throws AMFICOMRemoteException;

	/**
	 * Removes the mapping for this key from this map if it is present
	 * (optional operation).   More formally, if this map contains a mapping
	 * from key <tt>k</tt> to value <tt>v</tt> such that
	 * <code>(key==null ?  k==null : key.equals(k))</code>, that mapping
	 * is removed.  (The map can contain at most one such mapping.)
	 *
	 * <p>Returns the value to which the map previously associated the key, or
	 * <tt>null</tt> if the map contained no mapping for this key. The map will
	 * not contain a mapping for the specified  key once the call returns.
	 *
	 * @param key key whose mapping is to be removed from the map.
	 * @return previous value associated with specified key, or <tt>null</tt>
	 *	       if there was no mapping for key.
	 * @throws NullPointerException if the key is <tt>null</tt>.
	 * @throws AMFICOMRemoteException
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	AMFICOMClient remove(AccessIdentity_Transferable key)
			throws AMFICOMRemoteException;
}
