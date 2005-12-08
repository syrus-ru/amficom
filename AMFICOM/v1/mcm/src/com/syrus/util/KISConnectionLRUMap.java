/*-
 * $Id: KISConnectionLRUMap.java,v 1.7 2005/12/08 15:31:56 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.mcm.KISConnection;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.7 $, $Date: 2005/12/08 15:31:56 $
 * @module mcm
 */
public class KISConnectionLRUMap extends ArrayLRUMap<Identifier, KISConnection> {
	private static final long serialVersionUID = 2946433898920082018L;

	public KISConnectionLRUMap() {
		super();
	}

	public KISConnectionLRUMap(final int capacity) {
		super(capacity);
	}

	@Override
	public synchronized KISConnection put(final Identifier kisId, final KISConnection kisConnection) {
		assert (kisConnection != null) : ErrorMessages.NON_NULL_EXPECTED;

		final KISConnection removedKISConnection = super.put(kisId, kisConnection);
		if (removedKISConnection == null || !removedKISConnection.isEstablished()) {
			return removedKISConnection;
		}

		// Find the nearest to the right side of array non-established connection.
		//Return it if exists.
		assert (super.array.length == super.entityCount) : "ERROR before ArrayLRUMap resize: entity count " + super.entityCount
				+ " does not match array length " + super.array.length;

		for (int i = super.array.length - 1; i >= 0; i--) {
			final KISConnection aKISConnection = super.array[i].getValue();
			if (!aKISConnection.isEstablished()) {
				for (int j = i; j < super.array.length - 1; j++) {
					super.array[j] = super.array[j + 1];
				}
				super.array[super.array.length - 1] = new Entry(removedKISConnection.getKISId(), removedKISConnection);
				return aKISConnection;
			}
		}

		// If non-established connection not found,
		// enlarge array and put removed connection to it's end.
		final IEntry<Identifier, KISConnection>[] array1 = new IEntry[super.array.length + SIZE];
		System.arraycopy(super.array, 0, array1, 0, super.array.length);
		array1[super.array.length] = new Entry(removedKISConnection.getKISId(), removedKISConnection);
		super.array = array1;
		super.entityCount ++;
		return null;
	}
}
