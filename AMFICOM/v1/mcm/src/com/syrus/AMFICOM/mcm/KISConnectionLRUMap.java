package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.util.LRUMap;

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
		if (removedKISConnection != null)
			removedKISConnection.drop();
		return removedKISConnection;
	}
}
