/*
 * $Id: LRUMap.java,v 1.5 2004/09/14 14:24:17 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.util;

import java.io.Serializable;

/**
 * @version $Revision: 1.5 $, $Date: 2004/09/14 14:24:17 $
 * @author $Author: bob $
 * @module util
 */

public class LRUMap implements Serializable {
	private static final long serialVersionUID = 5686622326974494326L;

	public static final int SIZE = 10;

	protected Entry[] array;

	public LRUMap() {
		this (SIZE);
	}

	public LRUMap(int capacity) {
		if (capacity > 0) {
			this.array = new Entry[capacity];
		}
		else
			throw new IllegalArgumentException("Illegal capacity: " + capacity);
	}

	public synchronized void clear(){
		for (int i = 0; i < this.array.length; i++) {
			this.array[i] = null;
		}
	}
	
	public synchronized Object put(Object key, Object value) {
		Entry newEntry = new Entry(key, value);
		Object ret = null;
		if (this.array[this.array.length - 1] != null)
			ret = this.array[this.array.length - 1].value;
		for (int i = this.array.length - 1; i > 0; i--)
			this.array[i] = this.array[i - 1];
		this.array[0] = newEntry;
		return ret;
	}

	public synchronized Object get(Object key) {
		if (key != null) { 
			Object ret = null;
			for (int i = 0; i < this.array.length; i++)
				if (this.array[i] != null && key.equals(this.array[i].key))
					ret = this.array[i].value;
			return ret;
		}
		throw new IllegalArgumentException("Key is NULL");
	}

	public synchronized Object remove(Object key) {
		if (key != null) { 
			Object ret = null;
			for (int i = 0; i < this.array.length; i++)
				if (key.equals(this.array[i].key)) {
					ret = this.array[i].value;
					for (int j = i; j < this.array.length - 1; j++)
						this.array[j] = this.array[j + 1];
					break;
				}				
			return ret;
		}
		throw new IllegalArgumentException("Key is NULL");
	}

	protected static class Entry {
		Object key;
		Object value;

		Entry(Object key, Object value) {
			if (key != null) {
				if (value != null) {
					this.key = key;
					this.value = value;
				}
				else
					throw new IllegalArgumentException("Value is NULL");
			}
			else
				throw new IllegalArgumentException("Key is NULL");
		}
	}
}
