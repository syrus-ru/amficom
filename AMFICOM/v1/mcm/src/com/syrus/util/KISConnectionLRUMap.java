package com.syrus.util;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.mcm.KISConnection;

public class KISConnectionLRUMap extends LRUMap {
	static final long serialVersionUID = -1243965322879317241L;

	public KISConnectionLRUMap() {
		super();
	}

	public KISConnectionLRUMap(int capacity) {
		super(capacity);
	}

	public synchronized KISConnection put(Identifier kisId, KISConnection kisConnection) {
		KISConnection removedKISConnection = (KISConnection)super.put(kisId, kisConnection);
		if (removedKISConnection == null || ! removedKISConnection.isEstablished())
			return removedKISConnection;

		// Find the nearest to the right side of array non-established connection.
		//Return it if exists.
		KISConnection aKISConnection;
		for (int i = super.array.length - 1; i >= 0; i--) {
			aKISConnection = (KISConnection)super.array[i];
			if (! aKISConnection.isEstablished()) {
				for (int j = i; j < super.array.length - 1; j++)
					super.array[j] = super.array[j + 1];
				super.array[super.array.length - 1] = new Entry(removedKISConnection.getKISId(), removedKISConnection);
				return aKISConnection;
			}
		}

		// If non-established connection not found,
		// enlarge array and put removed connection to it's end.
		Entry[] array1 = new Entry[super.array.length + SIZE];
		System.arraycopy(super.array, 0, array1, 0, super.array.length);
		array1[super.array.length] = new Entry(removedKISConnection.getKISId(), removedKISConnection);
		super.array = array1;
		super.entityCount ++;
		return null;
	}
}
