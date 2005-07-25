/*
 * $Id: StorableObjectResizableLRUMap.java,v 1.6 2005/07/25 15:09:53 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.syrus.util.LRUMap;

/**
 * @version $Revision: 1.6 $, $Date: 2005/07/25 15:09:53 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public class StorableObjectResizableLRUMap extends LRUMap {
	private static final long serialVersionUID = 5983495252523370955L;
	
	private int initialCapacity;

	public StorableObjectResizableLRUMap() {
		super();
		this.initialCapacity = SIZE;
	}

	public StorableObjectResizableLRUMap(final int initialCapacity) {
		super(initialCapacity);
		this.initialCapacity = initialCapacity;
	}

	@Override
	public synchronized Object put(final Object key, final Object value) {
		assert (value instanceof StorableObject) : "Use only StorableObject as value";
		final StorableObject throwedObject = (StorableObject) super.put(key, value);
		if (throwedObject == null || !throwedObject.isChanged())
			return throwedObject;

		for (int i = super.array.length - 1; i >= 0; i--) {
			final StorableObject storableObject = (StorableObject) super.array[i].getValue();
			if (!storableObject.isChanged()) {
				for (int j = i; j < super.array.length - 1; j++)
					super.array[j] = super.array[j + 1];
				super.array[super.array.length - 1] = new Entry(throwedObject.getId(), throwedObject);
				return storableObject;
			}
		}

		super.entityCount++;
		final Entry[] array1 = new Entry[super.array.length + SIZE];
		System.arraycopy(super.array, 0, array1, 0, super.array.length);
		array1[super.array.length] = new Entry(throwedObject.getId(), throwedObject);
		super.array = array1;
		return null;
	}

	public synchronized Set<StorableObject> getChangedStorableObjects() {
		final Set<StorableObject> changedObjects = new HashSet<StorableObject>();

		for (int i = 0; i < super.array.length; i++) {
			final Entry entry = super.array[i];
			if (entry != null) {
				final Object value = entry.getValue();
				if (value instanceof StorableObject) {
					final StorableObject storableObject = (StorableObject) value;
					if (storableObject.isChanged())
						changedObjects.add(storableObject);
				}
			}
		}
		
		return changedObjects;
	}

	public synchronized void removeChangedStorableObjects() {
		for (int i = 0; i < super.array.length; i++) {
			final Entry entry = super.array[i];
			if (entry != null) {
				final Object value = entry.getValue();
				if (value instanceof StorableObject) {
					final StorableObject storableObject = (StorableObject) value;
					if (storableObject.isChanged())
						this.remove(entry.getKey());
				}
			}
		}
	}

	public synchronized void truncate(boolean removeAlsoUnchanged) {
		super.modCount++;

		final List<Entry> retainedEntries = new ArrayList<Entry>(super.array.length);

		for (int i = 0; i < super.array.length; i++) {
			final Entry entry = super.array[i];
			if (entry != null) {
				if (!removeAlsoUnchanged)
					retainedEntries.add(entry);
				else {
					final Object value = entry.getValue();
					if (value instanceof StorableObject) {
						final StorableObject storableObject = (StorableObject) value;
						if (storableObject.isChanged())
							retainedEntries.add(entry);
					}
				}
			}
		}

		final int size = retainedEntries.size();
		super.array = new Entry[(size > this.initialCapacity) ? size : this.initialCapacity];
		int i = 0;
		for (final Entry entry : retainedEntries) {
			super.array[i] = entry;
		}
		super.entityCount = size;
	}
}
