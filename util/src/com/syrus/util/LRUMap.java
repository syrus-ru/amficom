/*-
 * $Id: LRUMap.java,v 1.50 2006/01/19 15:17:54 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;


/**
 * @version $Revision: 1.50 $, $Date: 2006/01/19 15:17:54 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module util
 */
public final class LRUMap<K, V extends LRUMap.Retainable> implements Map<K, V>, Cloneable, Serializable {
	private static final long serialVersionUID = 7021342098557013331L;

	private static final String ERRMESSG_DIFF_SIZES = "Index and data have different sizes.";

	public static final int CAPACITY = 10;
	public static final long TIME_TO_LIVE = 10 * 60 * 1000 * 1000 * 1000;	//10 min

	private int capacity;
	private long timeToLive;	//in ns
	private transient LinkedList<Entry<K, V>> data;
	private transient Map<K, Entry<K, V>> index;

	private transient int modCount;

	public LRUMap() {
		this(CAPACITY, TIME_TO_LIVE);
	}

	public LRUMap(final int capacity) {
		this(capacity, TIME_TO_LIVE);
	}

	public LRUMap(final long timeToLive) {
		this(CAPACITY, timeToLive);
	}

	public LRUMap(final int capacity, final long timeToLive) {
		this.capacity = capacity;
		this.timeToLive = timeToLive;

		this.data = new LinkedList<Entry<K, V>>();
		this.index = new HashMap<K, Entry<K, V>>();

		this.modCount = 0;

		//this.entriesToRemove = new LinkedList<Entry<K, V>>();
	}


	public boolean containsKey(final Object key) {
		assert this.index.size() == this.data.size() : ERRMESSG_DIFF_SIZES
				+ " Index[" + this.index.size() + "]: " + this.index + "; data[" + this.data.size() + "]: " + this.data;

		return getEntry(key) != null;
	}

	public boolean containsValue(final Object value) {
		assert this.index.size() == this.data.size() : ERRMESSG_DIFF_SIZES
				+ " Index[" + this.index.size() + "]: " + this.index + "; data[" + this.data.size() + "]: " + this.data;

		for (final Entry<K, V> entry : this.data) {
			if (equalsWithNull(entry.getValue(), value)) {
				return true;
			}
		}
		return false;
	}

	public V get(final Object key) {
		assert this.index.size() == this.data.size() : ERRMESSG_DIFF_SIZES
				+ " Index[" + this.index.size() + "]: " + this.index + "; data[" + this.data.size() + "]: " + this.data;

		final Entry<K, V> entry = this.getEntry(key);
		if (entry == null) {
			return null;
		}
		this.modCount++;
		this.moveToBegin(entry);
		entry.updateLastAccessTimeStamp();
		return entry.getValue();
	}

	public V unmodifiableGet(final Object key) {
		assert this.index.size() == this.data.size() : ERRMESSG_DIFF_SIZES
				+ " Index[" + this.index.size() + "]: " + this.index + "; data[" + this.data.size() + "]: " + this.data;

		final Entry<K, V> entry = this.getEntry(key);
		if (entry == null) {
			return null;
		}
		return entry.getValue();
	}

	public V put(final K key, final V value) {
		assert this.index.size() == this.data.size() : ERRMESSG_DIFF_SIZES
				+ " Index[" + this.index.size() + "]: " + this.index + "; data[" + this.data.size() + "]: " + this.data;

		this.modCount++;

		this.scanForRemove();

		final Entry<K, V> oldEntry = this.index.get(key);
		if (oldEntry == null) {
			final Entry<K, V> newEntry = new Entry<K, V>(key, value);
			this.index.put(key, newEntry);
			this.data.addFirst(newEntry);
			return null;
		}
		final V oldValue = oldEntry.getValue();
		oldEntry.setValue(value);
		oldEntry.updateLastAccessTimeStamp();
		this.moveToBegin(oldEntry);
		return oldValue;
	}

	public void putAll(final Map<? extends K, ? extends V> t) {
		assert this.index.size() == this.data.size() : ERRMESSG_DIFF_SIZES
				+ " Index[" + this.index.size() + "]: " + this.index + "; data[" + this.data.size() + "]: " + this.data;

		for (final Map.Entry<? extends K, ? extends V> entry : t.entrySet()) {
			this.put(entry.getKey(), entry.getValue());
		}
	}

	public V remove(final Object key) {
		assert this.index.size() == this.data.size() : ERRMESSG_DIFF_SIZES
				+ " Index[" + this.index.size() + "]: " + this.index + "; data[" + this.data.size() + "]: " + this.data;

		final Entry<K, V> entry = this.getEntry(key);
		if (entry == null) {
			return null;
		}

		final V oldValue = entry.getValue();
		this.deleteEntry(entry);
		return oldValue;
	}

	public void clear() {
		assert this.index.size() == this.data.size() : ERRMESSG_DIFF_SIZES
				+ " Index[" + this.index.size() + "]: " + this.index + "; data[" + this.data.size() + "]: " + this.data;

		this.modCount++;
		this.index.clear();
		this.data.clear();
	}

	public int size() {
		assert this.index.size() == this.data.size() : ERRMESSG_DIFF_SIZES
				+ " Index[" + this.index.size() + "]: " + this.index + "; data[" + this.data.size() + "]: " + this.data;

		return this.data.size();
	}

	public boolean isEmpty() {
		assert this.index.size() == this.data.size() : ERRMESSG_DIFF_SIZES
				+ " Index[" + this.index.size() + "]: " + this.index + "; data[" + this.data.size() + "]: " + this.data;

		return this.data.isEmpty();
	}


	private transient volatile Set<K> keySet = null;

	public Set<K> keySet() {
		if (this.keySet == null) {
			this.keySet = new AbstractSet<K>() {

				@Override
				public Iterator<K> iterator() {
					assert LRUMap.this.index.size() == LRUMap.this.data.size() : ERRMESSG_DIFF_SIZES
							+ " Index[" + LRUMap.this.index.size() + "]: " + LRUMap.this.index
							+ "; data[" + LRUMap.this.data.size() + "]: " + LRUMap.this.data;

					return new KeyLRUMapIterator();
				}

				@Override
				public boolean contains(final Object object) {
					assert LRUMap.this.index.size() == LRUMap.this.data.size() : ERRMESSG_DIFF_SIZES
							+ " Index[" + LRUMap.this.index.size() + "]: " + LRUMap.this.index
							+ "; data[" + LRUMap.this.data.size() + "]: " + LRUMap.this.data;

					return LRUMap.this.containsKey(object);
				}

				@Override
				public boolean remove(final Object object) {
					assert LRUMap.this.index.size() == LRUMap.this.data.size() : ERRMESSG_DIFF_SIZES
							+ " Index[" + LRUMap.this.index.size() + "]: " + LRUMap.this.index
							+ "; data[" + LRUMap.this.data.size() + "]: " + LRUMap.this.data;

					return LRUMap.this.remove(object) != null;
				}

				@Override
				public int size() {
					assert LRUMap.this.index.size() == LRUMap.this.data.size() : ERRMESSG_DIFF_SIZES
							+ " Index[" + LRUMap.this.index.size() + "]: " + LRUMap.this.index
							+ "; data[" + LRUMap.this.data.size() + "]: " + LRUMap.this.data;

					return LRUMap.this.size();
				}

				@Override
				public void clear() {
					assert LRUMap.this.index.size() == LRUMap.this.data.size() : ERRMESSG_DIFF_SIZES
							+ " Index[" + LRUMap.this.index.size() + "]: " + LRUMap.this.index
							+ "; data[" + LRUMap.this.data.size() + "]: " + LRUMap.this.data;

					LRUMap.this.clear();
				}

			};
		}
		return this.keySet;
	}


	private transient volatile Collection<V> values = null;

	public Collection<V> values() {
		if (this.values == null) {
			this.values = new AbstractCollection<V>() {

				@Override
				public Iterator<V> iterator() {
					assert LRUMap.this.index.size() == LRUMap.this.data.size() : ERRMESSG_DIFF_SIZES
							+ " Index[" + LRUMap.this.index.size() + "]: " + LRUMap.this.index
							+ "; data[" + LRUMap.this.data.size() + "]: " + LRUMap.this.data;

					return new ValueLRUMapIterator();
				}

				@Override
				public boolean contains(final Object object) {
					assert LRUMap.this.index.size() == LRUMap.this.data.size() : ERRMESSG_DIFF_SIZES
							+ " Index[" + LRUMap.this.index.size() + "]: " + LRUMap.this.index
							+ "; data[" + LRUMap.this.data.size() + "]: " + LRUMap.this.data;

					for (final Entry<K, V> entry : LRUMap.this.data) {
						if (equalsWithNull(object, entry.getValue())) {
							return true;
						}
					}
					return false;
				}

				@Override
				public boolean remove(final Object object) {
					assert LRUMap.this.index.size() == LRUMap.this.data.size() : ERRMESSG_DIFF_SIZES
							+ " Index[" + LRUMap.this.index.size() + "]: " + LRUMap.this.index
							+ "; data[" + LRUMap.this.data.size() + "]: " + LRUMap.this.data;

					for (final Iterator<Entry<K, V>> it = LRUMap.this.data.iterator(); it.hasNext();) {
						final Entry<K, V> entry = it.next();
						if (equalsWithNull(object, entry.getValue())) {
							it.remove();
							LRUMap.this.index.remove(entry.getKey());
							LRUMap.this.modCount++;
							return true;
						}
					}
					return false;
				}

				@Override
				public int size() {
					assert LRUMap.this.index.size() == LRUMap.this.data.size() : ERRMESSG_DIFF_SIZES
							+ " Index[" + LRUMap.this.index.size() + "]: " + LRUMap.this.index
							+ "; data[" + LRUMap.this.data.size() + "]: " + LRUMap.this.data;

					return LRUMap.this.size();
				}

				@Override
				public void clear() {
					assert LRUMap.this.index.size() == LRUMap.this.data.size() : ERRMESSG_DIFF_SIZES
							+ " Index[" + LRUMap.this.index.size() + "]: " + LRUMap.this.index
							+ "; data[" + LRUMap.this.data.size() + "]: " + LRUMap.this.data;

					LRUMap.this.clear();
				}

			};
		}
		return this.values;
	}


	private transient volatile Set<Map.Entry<K,V>> entrySet = null;

	public Set<Map.Entry<K,V>> entrySet() {
		if (this.entrySet == null) {
			this.entrySet = new AbstractSet<Map.Entry<K,V>>() {

				@Override
				public Iterator<Map.Entry<K,V>> iterator() {
					assert LRUMap.this.index.size() == LRUMap.this.data.size() : ERRMESSG_DIFF_SIZES
							+ " Index[" + LRUMap.this.index.size() + "]: " + LRUMap.this.index
							+ "; data[" + LRUMap.this.data.size() + "]: " + LRUMap.this.data;

					return new EntryLRUMapIterator();
				}

				@Override
				public boolean contains(final Object object) {
					assert LRUMap.this.index.size() == LRUMap.this.data.size() : ERRMESSG_DIFF_SIZES
							+ " Index[" + LRUMap.this.index.size() + "]: " + LRUMap.this.index
							+ "; data[" + LRUMap.this.data.size() + "]: " + LRUMap.this.data;

					if (!(object instanceof Map.Entry)) {
						return false;
					}
					final Map.Entry<K,V> thatEntry = (Map.Entry<K,V>) object;
					final Entry<K, V> thisEntry = LRUMap.this.getEntry(thatEntry.getKey());
					return thisEntry != null && equalsWithNull(thisEntry.getValue(), thatEntry.getValue());
				}

				@Override
				public boolean remove(final Object object) {
					assert LRUMap.this.index.size() == LRUMap.this.data.size() : ERRMESSG_DIFF_SIZES
							+ " Index[" + LRUMap.this.index.size() + "]: " + LRUMap.this.index
							+ "; data[" + LRUMap.this.data.size() + "]: " + LRUMap.this.data;

					if (!(object instanceof Map.Entry)) {
						return false;
					}
					final Map.Entry<K,V> thatEntry = (Map.Entry<K,V>) object;
					final Entry<K, V> thisEntry = LRUMap.this.getEntry(thatEntry.getKey());
					if (thisEntry != null && equalsWithNull(thisEntry.getValue(), thatEntry.getValue())) {
						LRUMap.this.deleteEntry(thisEntry);
						return true;
					}
					return false;
				}

				@Override
				public int size() {
					assert LRUMap.this.index.size() == LRUMap.this.data.size() : ERRMESSG_DIFF_SIZES
							+ " Index[" + LRUMap.this.index.size() + "]: " + LRUMap.this.index
							+ "; data[" + LRUMap.this.data.size() + "]: " + LRUMap.this.data;

					return LRUMap.this.size();
				}

				@Override
				public void clear() {
					assert LRUMap.this.index.size() == LRUMap.this.data.size() : ERRMESSG_DIFF_SIZES
							+ " Index[" + LRUMap.this.index.size() + "]: " + LRUMap.this.index
							+ "; data[" + LRUMap.this.data.size() + "]: " + LRUMap.this.data;

					LRUMap.this.clear();
				}
			};
		}
		return this.entrySet;
	}

	@Override
	public Object clone() {
		try {
			final LRUMap<K, V> clone = (LRUMap<K, V>) super.clone();

			clone.capacity = this.capacity;
			clone.timeToLive = this.timeToLive;
			clone.data = new LinkedList<Entry<K, V>>(this.data);
			clone.index = new HashMap<K, Entry<K, V>>(this.index);
			clone.modCount = 0;
			clone.keySet = null;
			clone.values = null;
			clone.entrySet = null;

			return clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
	}


	private Entry<K, V> getEntry(final Object key) {
		return this.index.get(key);
	}

	private void moveToBegin(final Entry<K, V> entry) {
		this.data.remove(entry);
		this.data.addFirst(entry);
	}

	private void scanForRemove() {
		if (this.data.size() < this.capacity) {
			return;
		}

		for (final ListIterator<Entry<K, V>> it = this.data.listIterator(this.data.size()); it.hasPrevious();) {
			final Entry<K, V> entry = it.previous();
			if (entry.getIdleTime() < this.timeToLive) {
				return;
			}

			if (entry.getValue().retain()) {
				continue;
			}
			this.modCount++;
			this.index.remove(entry.getKey());
			it.remove();
		}
	}

	private void deleteEntry(final Entry<K, V> entry) {
		this.modCount++;
		this.index.remove(entry.getKey());
		this.data.remove(entry);
	}

	private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
		objectOutputStream.defaultWriteObject();

		objectOutputStream.writeInt(this.data.size());
		for (final Map.Entry entry : this.data) {
			objectOutputStream.writeObject(entry.getKey());
			objectOutputStream.writeObject(entry.getValue());
		}
	}

	private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
		objectInputStream.defaultReadObject();

		final int size = objectInputStream.readInt();
		this.data = new LinkedList<Entry<K, V>>();
		this.index = new HashMap<K, Entry<K, V>>(size);
		for (int i = 0; i < size; i++) {
			final K key = (K) objectInputStream.readObject();
			final V value = (V) objectInputStream.readObject();
			final Entry<K, V> entry = new Entry<K, V>(key, value);
			this.data.add(entry);
			this.index.put(key, entry);
		}
	}

	private static boolean equalsWithNull(final Object object1, final Object object2) {
		return (object1 == null) ? (object2 == null) : object1.equals(object2);
	}

	@Override
	public String toString() {
		final StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("{");
		for (final Iterator<Entry<K, V>> it = this.data.iterator(); it.hasNext();) {
			final Entry<K, V> entry = it.next();
			final K key = entry.getKey();
			final V value = entry.getValue();

			stringBuffer.append(" ");

			if (key == this) {
				stringBuffer.append("This LRUMap");
			} else {
				stringBuffer.append(key);
			}

			stringBuffer.append(" : ");

			stringBuffer.append(value);

			if (it.hasNext()) {
				stringBuffer.append(";");
			}
		}
		stringBuffer.append(" }");
		
		return stringBuffer.toString();
	}


	public static interface Retainable {
		boolean retain();
	}

	private static class Entry<L, W extends Retainable> implements Map.Entry<L, W> {
		private L key;
		private W value;
		private long creationTimeStamp;
		private long lastAccessTimeStamp;

		Entry(final L key, final W value) {
			this.key = key;
			this.value = value;
			final long timeStamp = System.nanoTime();
			this.creationTimeStamp = this.lastAccessTimeStamp = timeStamp;
		}

		public L getKey() {
			return this.key;
		}

		public W getValue() {
			return this.value;
		}

		public W setValue(final W value) {
			final W oldValue = this.value;
			this.value = value;
			return oldValue;
		}

		@Override
		public boolean equals(final Object object) {
			if (!(object instanceof Map.Entry)) {
				return false;
			}

			final Map.Entry that = (Map.Entry) object;
			return ((this.key == null ? that.getKey() == null : this.key.equals(that.getKey()))
					&& (this.value == null ? that.getValue() == null : this.value.equals(that.getValue())));
		}

    @Override
		public int hashCode() {
			int keyHash = (this.key == null ? 0 : this.key.hashCode());
			int valueHash = (this.value == null ? 0 : this.value.hashCode());
			return keyHash ^ valueHash;
		}

    @Override
		public String toString() {
        return this.key + " : " + this.value;
    }

		long getLifeTime() {
			return System.nanoTime() - this.creationTimeStamp;
		}

		long getIdleTime() {
			return System.nanoTime() - this.lastAccessTimeStamp;
		}

		void updateLastAccessTimeStamp() {
			this.lastAccessTimeStamp = System.nanoTime();
		}

	}

	private abstract class LRUMapIterator<T> implements Iterator<T> {
		private int expectedModCount;
		private Iterator<Entry<K, V>> dataIterator;
		private Entry<K, V> lastReturned;
		
		LRUMapIterator() {
			this.expectedModCount = LRUMap.this.modCount;
			this.dataIterator = LRUMap.this.data.iterator();
		}

		public final boolean hasNext() {
			return this.dataIterator.hasNext();
		}

		final Entry<K, V> nextEntry() {
			this.checkForComodification();
			if (this.dataIterator.hasNext()) {
				this.lastReturned = this.dataIterator.next();
				return this.lastReturned;
			}
			throw new NoSuchElementException();
		}

		public final void remove() {
			if (this.lastReturned == null) {
				throw new IllegalStateException();
			}
			this.checkForComodification();
			this.dataIterator.remove();
			LRUMap.this.index.remove(this.lastReturned.getKey());
			this.lastReturned = null;
		}

		private void checkForComodification() {
			if (LRUMap.this.modCount != this.expectedModCount) {
				throw new ConcurrentModificationException();
			}
		}
	}

	private final class EntryLRUMapIterator extends LRUMapIterator<Map.Entry<K,V>> {
		public Entry<K, V> next() {
			return this.nextEntry();
		}
	}

	private final class KeyLRUMapIterator extends LRUMapIterator<K> {
		public K next() {
			return this.nextEntry().getKey();
		}
	}

	private final class ValueLRUMapIterator extends LRUMapIterator<V> {
		public V next() {
			return this.nextEntry().getValue();
		}
	}

}
