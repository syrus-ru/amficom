/*
 * $Id: ClientLRUMap.java,v 1.3 2004/11/04 13:16:56 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.util;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.util.LRUMap;

/**
 * @version $Revision: 1.3 $, $Date: 2004/11/04 13:16:56 $
 * @author $Author: bob $
 * @module util
 */

public class ClientLRUMap extends LRUMap {

	public ClientLRUMap() {
		super();
	}

	public ClientLRUMap(int capacity) {
		super(capacity);
	}

	
	public synchronized Object put(Object key, Object value) {
		super.modCount++;
		Entry newEntry = new Entry(key, value);
		Object ret = null;
		// what additional entry(ies) at array;
		int add = (super.entityCount == super.array.length) ? 0 : 1;
		if (super.array[super.array.length - 1] != null){
			ret = super.array[super.array.length - 1].value;
			if (ret instanceof StorableObject){
				StorableObject storableObject = (StorableObject) ret;
				// changed Storable cannot be removed
				// enlarge array when changed StorableObject at end of array
				 
				if (storableObject.isChanged()){
					add = 1;
					super.entityCount++;
					Entry[] array1 = new Entry[super.array.length + 10];
					System.arraycopy(super.array, 0, array1, 0, super.array.length);
					super.array = array1;
					ret = null;
				}
			}
		}
		
		super.entityCount += add;
		for (int i = super.array.length - 1; i > 0; i--)
			super.array[i] = super.array[i - 1];
		super.array[0] = newEntry;	
		
		// save ret
		if (ret instanceof Serializable){
			Serializable obj = (Serializable)ret;
			/**
			 * TODO save return object
			 */
		}
		return ret;
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
