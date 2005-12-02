/*
 * $Id: LRUMap.java,v 1.46 2005/12/02 15:16:56 arseniy Exp $
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
 * @version $Revision: 1.46 $, $Date: 2005/12/02 15:16:56 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module util
 */
public class LRUMap<K, V> implements Serializable, Iterable<V> {
	private static final long serialVersionUID = 5686622326974494326L;

	public static final int SIZE = 10;

	protected IEntry<K, V>[] array;

	protected transient int modCount = 0;

	protected int entityCount = 0;

	public LRUMap() {
		this(SIZE);
	}

	public LRUMap(final int capacity) {
		if (capacity > 0) {
			this.array = new IEntry[capacity];
		} else {
			throw new IllegalArgumentException("Illegal capacity: " + capacity);
		}
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
		return new Itr();
	}

	/**
	 * key iterator
	 */
	public Iterator<K> keyIterator() {
		return new KeyIterator();
	}

	public int size() {
		return this.entityCount;
	}

	public boolean isEmpty() {
		return this.entityCount == 0;
	}

	public int indexOf(final K key) {
		int index = -1;
		if (key != null) {
			for (int i = 0; i < this.array.length; i++) {
				final IEntry entry = this.array[i];
				if (entry != null && entry.getKey().equals(key)) {
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
		final IEntry<K, V> newEntry = new Entry(key, value);
		V ret = null;
		if (this.array[this.array.length - 1] != null) {
			ret = this.array[this.array.length - 1].getValue();
		}
		for (int i = this.array.length - 1; i > 0; i--) {
			this.array[i] = this.array[i - 1];
		}
		this.array[0] = newEntry;
		return ret;
	}

	public synchronized V get(final K key) {
		if (key != null) {
			int i = 0;
			IEntry<K, V> entry = null;
			for (; i < this.array.length; i++) {
				if (this.array[i] != null && key.equals(this.array[i].getKey())) {
					entry = this.array[i];
					break;
				}
			}
			if (entry != null) {
				this.modCount++;
				this.array[i] = this.array[0];
				this.array[0] = entry;
				return entry.getValue();
			}
			return null;
		}
		throw new IllegalArgumentException("Key is NULL");
	}

	public synchronized V unmodifiableGet(final K key) {
		if (key != null) {
			V ret = null;
			for (int i = 0; i < this.array.length; i++) {
				if (this.array[i] != null && key.equals(this.array[i].getKey())) {
					ret = this.array[i].getValue();
					break;
				}
			}
			return ret;
		}
		throw new IllegalArgumentException("Key is NULL");
	}

	public synchronized boolean containsKey(final K key) {
		if (key != null) {
			for (int i = 0; i < this.array.length; i++) {
				if (this.array[i] != null && key.equals(this.array[i].getKey())) {
					return true;
				}
			}
			return false;
		}
		throw new IllegalArgumentException("Key is NULL");
	}

	public synchronized V remove(final K key) {
		this.modCount++;
		if (key != null) {
			V ret = null;
			for (int i = 0; i < this.array.length; i++) {
				final IEntry<K, V> entry = this.array[i];
				if (entry != null && entry.getKey() != null && key.equals(entry.getKey())) {
					ret = entry.getValue();
					for (int j = i; j < this.array.length - 1; j++) {
						this.array[j] = this.array[j + 1];
					}
					this.array[this.array.length - 1] = null;
					this.entityCount -= (this.entityCount == 0) ? 0 : 1;
					break;
				}
			}				
			return ret;
		}
		throw new IllegalArgumentException("Key is NULL");
	}

	@Override
	public String toString() {
		final StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("{");
		for (int i = 0; i < this.entityCount; i++) {
			final IEntry<K, V> entry = this.array[i];
			final K key = entry.getKey();
			final V value = entry.getValue();

			stringBuffer.append(" ");

			if (key == this) {
				stringBuffer.append("This LRUMap");
			} else {
				stringBuffer.append(key);
			}

			stringBuffer.append(" : ");

			if (value == this) {
				stringBuffer.append("This LRUMap");
			} else {
				stringBuffer.append(value);
			}

			if (i < this.entityCount - 1) {
				stringBuffer.append(",");
			}
		}
		stringBuffer.append(" }");
		
		return stringBuffer.toString();
	}

	public K[] getKeys() {
		final K[] keys = (K[]) new Object[this.entityCount];
		for (int i = 0; i < this.entityCount; i++) {
			keys[i] = this.array[i].getKey();
		}
		return keys;
	}

	public V[] getValues() {
		final V[] values = (V[]) new Object[this.entityCount];
		for (int i = 0; i < this.entityCount; i++) {
			values[i] = this.array[i].getValue();
		}
		return values;
	}

	IEntry<K, V>[] getEntries() {
		return this.array;
	}

	void populate(final IEntry<K, V>[] entries) {
		if (entries == null) {
			throw new NullPointerException("entries are null");
		}

		this.entityCount = Math.min(entries.length, this.array.length);
		System.arraycopy(entries, 0, this.array, 0, this.entityCount);
	}

	public void populate(final K[] keys, final V[] values) {
		if (keys == null) {
			throw new NullPointerException("keys are null");
		}
		if (values == null) {
			throw new NullPointerException("values are null");
		}
		if (keys.length != values.length) {
			throw new IllegalArgumentException("keys.length: " + keys.length + ", values.length: " + values.length);
		}

		this.entityCount = Math.min(keys.length, this.array.length);
		for (int i = 0; i < this.entityCount; i++) {
			this.array[i] = new Entry(keys[i], values[i]);
		}
	}


	protected static interface IEntry<L, W> {
		L getKey();

		W getValue();
	}

	protected class Entry implements IEntry<K, V> {
		private K key;
		private V value;

		public Entry(final K key, final V value) {
			if (key != null) {
				if (value != null) {
					this.key = key;
					this.value = value;
				} else {
					throw new IllegalArgumentException("Value is NULL");
				}
			} else {
				throw new IllegalArgumentException("Key is NULL");
			}
		}

		public K getKey() {
			return this.key;
		}

		public V getValue() {
			return this.value;
		}
	}


	private abstract class AbstractIterator {
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

		public final boolean hasNext() {
			return this.cursor != LRUMap.this.entityCount;
		}

		public final void remove() {
			if (this.lastRet == -1) {
				throw new IllegalStateException();
			}
			this.checkForComodification();

			try {
				LRUMap.this.remove(LRUMap.this.array[this.lastRet].getKey());
				if (this.lastRet < this.cursor) {
					this.cursor--;
				}
				this.lastRet = -1;
				this.expectedModCount = LRUMap.this.modCount;
			} catch (IndexOutOfBoundsException e) {
				Log.errorMessage(e);
				throw new ConcurrentModificationException();
			}
		}

		final void checkForComodification() {
			if (LRUMap.this.modCount != this.expectedModCount) {
				throw new ConcurrentModificationException();
			}
		}
	}

	private class Itr extends AbstractIterator implements Iterator<V> {
		public V next() {
			this.checkForComodification();
			try {
				final IEntry<K, V> nextEntry = LRUMap.this.array[this.cursor];
				this.lastRet = this.cursor++;
				return nextEntry.getValue();
			} catch (NullPointerException npe) {
				this.checkForComodification();
				throw new NoSuchElementException();
			} catch (IndexOutOfBoundsException ioobe) {
				this.checkForComodification();
				throw new NoSuchElementException();
			}
		}
	}

	private class KeyIterator extends AbstractIterator implements Iterator<K> {
		public K next() {
			this.checkForComodification();
			try {
				final IEntry<K, V> nextEntry = LRUMap.this.array[this.cursor];
				this.lastRet = this.cursor++;
				return nextEntry.getKey();
			} catch (NullPointerException npe) {
				this.checkForComodification();
				throw new NoSuchElementException();
			} catch (IndexOutOfBoundsException ioobe) {
				this.checkForComodification();
				throw new NoSuchElementException();
			}
		}
	}

}
