/*
 * $Id: ClientLRUMap.java,v 1.6 2004/12/07 10:31:58 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.util;

import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.util.LRUMap;

/**
 * @version $Revision: 1.6 $, $Date: 2004/12/07 10:31:58 $
 * @author $Author: arseniy $
 * @module generalclient_v1
 */

public class ClientLRUMap extends LRUMap {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3257285820741662513L;

	public ClientLRUMap() {
		super();
	}

	public ClientLRUMap(int capacity) {
		super(capacity);
	}
 
	public synchronized Object put(Object key, Object value) {
		StorableObject trowedOutObject = (StorableObject) super.put(key, value);
		if(trowedOutObject == null || !trowedOutObject.isChanged()) {
			return trowedOutObject;
		}
		for (int i = super.array.length - 1; i >= 0; i--) {
			StorableObject storableObject = (StorableObject) super.array[i].value;
			if (!storableObject.isChanged()) {
				for(int j = i; j < super.array.length - 1; j++)
					super.array[j] = super.array[j + 1];
				super.array[super.array.length - 1] = new Entry(trowedOutObject.getId(), trowedOutObject);
				return storableObject;
			}
		}
		//  array enlargement
		super.entityCount++;
		Entry[] array1 = new Entry[super.array.length + 10];
		System.arraycopy(super.array, 0, array1, 0, super.array.length);
		array1[super.array.length] = new Entry(trowedOutObject.getId(), trowedOutObject);
		super.array = array1;
		return null;
	}
	
	public synchronized List getChanged(){
		List list = null;
		for (int i = 0; i < super.array.length; i++){
			Entry entry = super.array[i];
			if ((entry != null) && (entry.value instanceof StorableObject)){
				if (list == null)
					list = new LinkedList();
				StorableObject object = (StorableObject)entry.value;
				if (object.isChanged())
					list.add(entry.value);				
			}
		}		
		return list;
	}
	
	public synchronized void removeChanged(){
		for (int i = 0; i < super.array.length; i++){
			Entry entry = super.array[i];
			if ((entry != null) && (entry.value instanceof StorableObject)){
				StorableObject object = (StorableObject)entry.value;
				if (object.isChanged())
					remove(entry.key);
			}
		}
	}
	
}
