/*
 * $Id: LRUMap.java,v 1.30 2005/07/26 20:09:21 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

import java.io.Serializable;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @version $Revision: 1.30 $, $Date: 2005/07/26 20:09:21 $
 * @author $Author: bass $
 * @module util
 */

public class LRUMap<K, V> implements Serializable {
	private static final long serialVersionUID = 5686622326974494326L;

	public static final int SIZE = 10;

	protected Entry<?, ?>[] array;

	protected transient int modCount = 0;

	protected int entityCount = 0;

	public LRUMap() {
		this(SIZE);
	}

	public LRUMap(final int capacity) {
		if (capacity > 0) {
			this.array = new Entry[capacity];
		} else
			throw new IllegalArgumentException("Illegal capacity: " + capacity);
	}

	public synchronized void clear() {
		for (int i = 0; i < this.array.length; i++) {
			this.array[i] = null;
		}
		this.entityCount = 0;
	}

	/**
	 * value iterator
	 */
	public Iterator<V> iterator() {
		return new Itr(false);
	}

	/**
	 * key iterator
	 */
	public Iterator<K> keyIterator() {
		return new Itr(true);
	}

	public int size() {
		return this.entityCount;
	}

	public int indexOf(final K key) {
		int index = -1;
		if (key != null) {
			for (int i = 0; i < this.array.length; i++) {
				Entry entry = this.array[i];
				if ((entry != null) && (entry.key.equals(key))) {
					index = i;
					break;
				}
			}
		}
		return index;
	}

	public synchronized V put(final K key, final V value) {
		this.modCount++;
		this.entityCount += (this.entityCount == this.array.length) ? 0 : 1;
		this.remove(key);
		Entry<K, V> newEntry = new Entry<K, V>(key, value);
		V ret = null;
		if (this.array[this.array.length - 1] != null)
			ret = (V) this.array[this.array.length - 1].value;
		for (int i = this.array.length - 1; i > 0; i--)
			this.array[i] = this.array[i - 1];
		this.array[0] = newEntry;
		return ret;
	}

	public synchronized V get(final K key) {
		this.modCount++;
		if (key != null) {
			V ret = null;
			for (int i = 0; i < this.array.length; i++)
				if (this.array[i] != null && key.equals(this.array[i].key)) {
					ret = (V) this.array[i].value;
					break;
				}
			return ret;
		}
		throw new IllegalArgumentException("Key is NULL");
	}

	public synchronized boolean containsKey(K key) {
		if (key != null) {
			for (int i = 0; i < this.array.length; i++)
				if (this.array[i] != null && key.equals(this.array[i].key))
					return true;
			return false;
		}
		throw new IllegalArgumentException("Key is NULL");
	}

	public synchronized V remove(final K key) {
		this.modCount++;
		if (key != null) {
			V ret = null;
			for (int i = 0; i < this.array.length; i++) {
				Entry entry = this.array[i];
				if ((entry != null) && (entry.key != null) && (key.equals(entry.key))) {
					ret = (V) this.array[i].value;					
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

	protected class Entry<L, W> /*implements Serializable */{
		L key;
		W value;

		public Entry(final L key, final W value) {
			if (key != null) {
				if (value != null) {
					this.key = key;
					this.value = value;
				} else
					throw new IllegalArgumentException("Value is NULL");
			} else
				throw new IllegalArgumentException("Key is NULL");
		}

		public L getKey() {
			return this.key;
		}

		public W getValue() {
			return this.value;
		}
	}

	protected class Itr implements Iterator/*, Serializable */{

		/**
		 * Index of element to be returned by subsequent call to next.
		 */
		int cursor = 0;
		
		/**
		 * Index of element returned by most recent call to next or previous.
		 * Reset to -1 if this element is deleted by a call to remove.
		 */
		int lastRet = -1;

		/**
		 * The modCount value that the iterator believes that the backing List
		 * should have. If this expectation is violated, the iterator has detected
		 * concurrent modification.
		 */
		int expectedModCount = LRUMap.this.modCount;

		private boolean keyIterator = false;

		public Itr(final boolean keyIterator) {
			this.keyIterator = keyIterator;
		}

		public boolean hasNext() {
			return this.cursor != LRUMap.this.entityCount;
		}

		public Object next() {
			this.checkForComodification();
			try {
				Object next = null;
				while (next == null) {
					next = this.keyIterator ? LRUMap.this.array[this.cursor].key : LRUMap.this.array[this.cursor].value;
					this.lastRet = this.cursor++;
				}
				return next;
			} catch (IndexOutOfBoundsException e) {
				this.checkForComodification();
				throw new NoSuchElementException();
			}
		}
		
		public void remove() {
			if (this.lastRet == -1)
				throw new IllegalStateException();
			this.checkForComodification();
			
			try {
				int modCountPrev = LRUMap.this.modCount;
				LRUMap.this.remove((K) LRUMap.this.array[this.lastRet].key);
				LRUMap.this.modCount = modCountPrev;
				if (this.lastRet < this.cursor)
					this.cursor--;
				this.lastRet = -1;
				this.expectedModCount = LRUMap.this.modCount;
			} catch (IndexOutOfBoundsException e) {
				throw new ConcurrentModificationException();
			}
		}
		
		private final void checkForComodification() {
			if (LRUMap.this.modCount != this.expectedModCount)
				throw new ConcurrentModificationException();
		}
	}

}
