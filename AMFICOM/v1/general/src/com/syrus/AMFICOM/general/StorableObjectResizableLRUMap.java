/*
 * $Id: StorableObjectResizableLRUMap.java,v 1.11 2005/09/12 19:53:00 arseniy Exp $
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
 * @version $Revision: 1.11 $, $Date: 2005/09/12 19:53:00 $
 * @author $Author: arseniy $
 * @module general
 */
public class StorableObjectResizableLRUMap extends LRUMap<Identifier, StorableObject> {
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
	public synchronized StorableObject put(final Identifier key, final StorableObject value) {
		assert (value != null) : ErrorMessages.NON_NULL_EXPECTED;

		final StorableObject thrownObject = super.put(key, value);
		if (thrownObject == null || !thrownObject.isChanged()) {
			return thrownObject;
		}

		assert (super.array.length == super.entityCount) : "ERROR before LRUMap resize: entity count " + super.entityCount
				+ " does not match array length " + super.array.length;

		for (int i = super.array.length - 1; i >= 0; i--) {
			final StorableObject storableObject = super.array[i].getValue();
			if (!storableObject.isChanged()) {
				for (int j = i; j < super.array.length - 1; j++) {
					super.array[j] = super.array[j + 1];
				}
				super.array[super.array.length - 1] = new Entry(thrownObject.getId(), thrownObject);
				return storableObject;
			}
		}

		final IEntry<Identifier, StorableObject>[] array1 = new IEntry[super.array.length + SIZE];
		System.arraycopy(super.array, 0, array1, 0, super.array.length);
		array1[super.array.length] = new Entry(thrownObject.getId(), thrownObject);
		super.array = array1;
		super.entityCount++;
		return null;
	}

	public synchronized Set<StorableObject> getChangedStorableObjects() {
		final Set<StorableObject> changedObjects = new HashSet<StorableObject>();

		for (int i = 0; i < super.array.length; i++) {
			final IEntry<Identifier, StorableObject> entry = super.array[i];
			if (entry != null) {
				final StorableObject value = entry.getValue();
				if (value != null && value.isChanged()) {
					changedObjects.add(value);
				}
			}
		}
		
		return changedObjects;
	}

	public synchronized void removeChangedStorableObjects() {
		for (int i = 0; i < super.array.length; i++) {
			final IEntry<Identifier, StorableObject> entry = super.array[i];
			if (entry != null) {
				final StorableObject value = entry.getValue();
				if (value != null && value.isChanged()) {
					this.remove(entry.getKey());
				}
			}
		}
	}

	public synchronized void truncate(boolean removeAlsoUnchanged) {
		super.modCount++;

		final List<IEntry<Identifier, StorableObject>> retainedEntries = new ArrayList<IEntry<Identifier, StorableObject>>(super.array.length);

		for (int i = 0; i < super.array.length; i++) {
			final IEntry<Identifier, StorableObject> entry = super.array[i];
			if (entry != null) {
				if (!removeAlsoUnchanged)
					retainedEntries.add(entry);
				else {
					final StorableObject value = entry.getValue();
					if (value != null && value.isChanged()) {
						retainedEntries.add(entry);
					}
				}
			}
		}

		final int size = retainedEntries.size();
		super.array = new IEntry[(size > this.initialCapacity) ? size : this.initialCapacity];
		int i = 0;
		for (final IEntry<Identifier, StorableObject> entry : retainedEntries) {
			super.array[i] = entry;
		}
		super.entityCount = size;
	}
}
