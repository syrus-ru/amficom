/*
 * $Id: StorableObjectResizableLRUMap.java,v 1.1 2005/03/18 16:52:10 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import com.syrus.util.LRUMap;

/**
 * @version $Revision: 1.1 $, $Date: 2005/03/18 16:52:10 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public class StorableObjectResizableLRUMap extends LRUMap {
	private static final long serialVersionUID = 3835158350253339954L;

	public StorableObjectResizableLRUMap() {
		super();
	}

	public StorableObjectResizableLRUMap(int capacity) {
		super(capacity);
	}

	public synchronized Object put(Object key, Object value) {
		assert (value instanceof StorableObject) : "Use only StorableObject as value";
		StorableObject throwedObject = (StorableObject) super.put(key, value);
		if (throwedObject == null || !throwedObject.isChanged())
			return throwedObject;

		StorableObject storableObject;
		for (int i = super.array.length - 1; i >= 0; i--) {
			storableObject = (StorableObject) super.array[i].getValue();
			if (!storableObject.isChanged()) {
				for (int j = i; j < super.array.length - 1; j++)
					super.array[j] = super.array[j + 1];
				super.array[super.array.length - 1] = new Entry(throwedObject.getId(), throwedObject);
				return storableObject;
			}
		}

		super.entityCount++;
		Entry[] array1 = new Entry[super.array.length + SIZE];
		System.arraycopy(super.array, 0, array1, 0, super.array.length);
		array1[super.array.length] = new Entry(throwedObject.getId(), throwedObject);
		super.array = array1;
		return null;
	}

	public synchronized Collection getChangedStorableObjects() {
		Collection collection = new LinkedList();
		
		Entry entry;
		Object value;
		StorableObject storableObject;
		for (int i = 0; i < super.array.length; i++) {
			entry = super.array[i];
			if (entry != null) {
				value = entry.getValue();
				if (value instanceof StorableObject) {
					storableObject = (StorableObject) value;
					if (storableObject.isChanged())
						collection.add(storableObject);
				}
			}
		}
		
		return collection;
	}

	public synchronized void removeChangedStorableObjects() {
		Entry entry;
		Object value;
		StorableObject storableObject;
		for (int i = 0; i < super.array.length; i++) {
			entry = super.array[i];
			if (entry != null) {
				value = entry.getValue();
				if (value instanceof StorableObject) {
					storableObject = (StorableObject) value;
					if (storableObject.isChanged())
						this.remove(entry.getKey());
				}
			}
		}
	}

	public synchronized void truncate(boolean removeAlsoUnchanged) {
		ArrayList retainedEntries = new ArrayList(super.array.length);

		Entry entry;
		Object value;
		StorableObject storableObject;
		for (int i = 0; i < super.array.length; i++) {
			entry = super.array[i];
			if (entry != null) {
				if (!removeAlsoUnchanged)
					retainedEntries.add(entry);
				else {
					value = entry.getValue();
					if (value instanceof StorableObject) {
						storableObject = (StorableObject) value;
						if (storableObject.isChanged())
							retainedEntries.add(entry);
					}
				}
			}
		}

		super.array = (Entry[]) retainedEntries.toArray(new Entry[retainedEntries.size()]);
	}
}
