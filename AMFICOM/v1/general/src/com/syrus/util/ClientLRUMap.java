/*
 * $Id: ClientLRUMap.java,v 1.1 2004/09/23 13:47:03 bob Exp $
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
 * @version $Revision: 1.1 $, $Date: 2004/09/23 13:47:03 $
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
		Entry newEntry = new Entry(key, value);
		Object ret = null;
		if (this.array[this.array.length - 1] != null){
			ret = this.array[this.array.length - 1].value;
			if (ret instanceof StorableObject){
				StorableObject storableObject = (StorableObject) ret;
				// changed Storable cannot be removed
				// enlarge array when changed StorableObject at end of array
				 
				if (storableObject.isChanged()){
					Entry[] array = new Entry[this.array.length + 10];
					System.arraycopy(this.array, 0, array, 0, this.array.length);
					this.array = array;
					ret = null;
				}
			}
		}
		
		for (int i = this.array.length - 1; i > 0; i--)
			this.array[i] = this.array[i - 1];
		this.array[0] = newEntry;	
		
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
		for (int i = 0; i < this.array.length; i++){
			Entry entry = this.array[i];
			if ((entry != null) && (entry.value instanceof StorableObject)){
				if (list == null)
					list = new LinkedList();
				list.add(entry.value);
			}
		}		
		return list;
	}
	
	public synchronized void removeChanged(){
		for (int i = 0; i < this.array.length; i++){
			Entry entry = this.array[i];
			if ((entry != null) && (entry.value instanceof StorableObject)){
				StorableObject object = (StorableObject)entry.value;
				if (object.isChanged())
					remove(entry.key);
			}
		}
	}
	
}
