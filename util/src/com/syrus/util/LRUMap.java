/*
 * $Id: LRUMap.java,v 1.12 2004/11/12 07:43:15 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.util;

import java.io.Serializable;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @version $Revision: 1.12 $, $Date: 2004/11/12 07:43:15 $
 * @author $Author: max $
 * @module util
 */

public class LRUMap implements Serializable {
	private static final long serialVersionUID = 5686622326974494326L;

	public static final int SIZE = 10;

	protected Entry[] array;
	
	transient protected int modCount = 0;
	
	protected int entityCount = 0;

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
		this.entityCount = 0;
	}
	
    public Iterator iterator() {
    	return new Itr();
    }
    
    public int indexOf(Object key){
    	int index = -1;
    	if (key != null){
	    	for(int i=0;i<this.array.length;i++){
	    		Entry entry = this.array[i];
	    		if ((entry != null) && (entry.key.equals(key))) {
	    				index = i;
	    				break;
	    			}
	    		}
	    	}    	
    	return index;
    }
	
	public synchronized Object put(Object key, Object value) {
		this.modCount++;
		this.entityCount += (this.entityCount == this.array.length) ? 0 : 1;
		this.remove(key);
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
		this.modCount++;
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
		this.modCount++;
		if (key != null) { 
			Object ret = null;
			for (int i = 0; i < this.array.length; i++) {
				Entry entry = this.array[i];
				if ((entry != null) && (entry.key != null) && (key.equals(entry.key))) {
					ret = this.array[i].value;					
					for (int j = i; j < this.array.length - 1; j++)
						this.array[j] = this.array[j + 1];
					this.entityCount -= (this.entityCount == 0) ? 0 : 1;
					break;
				}
			}				
			return ret;
		}
		throw new IllegalArgumentException("Key is NULL");
	}

	protected class Entry implements Serializable{
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
	
    protected class Itr implements Iterator, Serializable {

    	/**
    	 * Index of element to be returned by subsequent call to next.
    	 */
    	int cursor = 0;

    	/**
    	 * Index of element returned by most recent call to next or
    	 * previous.  Reset to -1 if this element is deleted by a call
    	 * to remove.
    	 */
    	int lastRet = -1;

    	/**
    	 * The modCount value that the iterator believes that the backing
    	 * List should have.  If this expectation is violated, the iterator
    	 * has detected concurrent modification.
    	 */
    	int expectedModCount = LRUMap.this.modCount;

    	public boolean hasNext() {
    	    return this.cursor != LRUMap.this.entityCount;
    	}

    	public Object next() {
                checkForComodification();
    	    try {
    		Object next = null;
    		while(next == null){
    			next = LRUMap.this.array[this.cursor].value;
    			this.lastRet = this.cursor++;
    		}
    		return next;
    	    } catch(IndexOutOfBoundsException e) {
    		checkForComodification();
    		throw new NoSuchElementException();
    	    }
    	}

    	public void remove() {
    	    if (this.lastRet == -1)
    		throw new IllegalStateException();
                checkForComodification();

    	    try {
    	    int modCountPrev = LRUMap.this.modCount; 
    	    LRUMap.this.remove(LRUMap.this.array[this.cursor].key);
    	    LRUMap.this.modCount = modCountPrev;
    		if (this.lastRet < this.cursor)
    		    this.cursor--;
    		this.lastRet = -1;
    		this.expectedModCount = LRUMap.this.modCount;
    	    } catch(IndexOutOfBoundsException e) {
    		throw new ConcurrentModificationException();
    	    }
    	}

    	final void checkForComodification() {
    	    if (LRUMap.this.modCount != this.expectedModCount)
    		throw new ConcurrentModificationException();
    	}
    }

}
