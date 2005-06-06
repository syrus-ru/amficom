/*
 * $Id: ObjectLoader.java,v 1.3 2005/06/06 14:35:24 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;

/**
 * @version $Revision: 1.3 $, $Date: 2005/06/06 14:35:24 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public abstract class ObjectLoader {

	/**
	 * NOTE: this method removes updated objects from set, thus modifying the set.
	 * If you are planning to use the set somewhere after this method call -
	 * create a copy of the set to supply to this method.
	 * @todo make static
	 * @param storableObjects
	 * @param headers
	 */
	protected final void updateHeaders(final Set storableObjects, final StorableObject_Transferable[] headers) {
		for (int i = 0; i < headers.length; i++) {
			final Identifier id = new Identifier(headers[i].id);
			for (Iterator it = storableObjects.iterator(); it.hasNext();) {
				StorableObject storableObject = (StorableObject) it.next();
				if (storableObject.getId().equals(id)) {
					storableObject.updateFromHeaderTransferable(headers[i]);
					it.remove();
					break;
				}
			}
		}
	}

	protected static final Set createLoadIds(final Set ids, final Set butIdentifiables) {
		assert ids != null && !ids.isEmpty() : ErrorMessages.NON_EMPTY_EXPECTED;
		assert butIdentifiables != null : ErrorMessages.NON_NULL_EXPECTED;

		final Set loadIds = new HashSet(ids);
		for (final Iterator it = butIdentifiables.iterator(); it.hasNext();) {
			final Identifiable identifiable = (StorableObject) it.next();
			final Identifier id = identifiable.getId();
			loadIds.remove(id);
		}
		return loadIds;
	}

	protected static final void removeFromLoadIds(final Set loadIds, final Set butIdentifiables) {
		assert loadIds != null && !loadIds.isEmpty() : ErrorMessages.NON_EMPTY_EXPECTED;
		assert butIdentifiables != null : ErrorMessages.NON_NULL_EXPECTED;

		for (final Iterator it = butIdentifiables.iterator(); it.hasNext();) {
			final Identifiable identifiable = (Identifiable) it.next();
			final Identifier id = identifiable.getId();
			loadIds.remove(id);
		}
	}

	protected static final Set createLoadButIds(final Set butIds, final Set alsoButIdentifiables) {
		assert butIds != null : ErrorMessages.NON_NULL_EXPECTED;
		assert alsoButIdentifiables != null : ErrorMessages.NON_NULL_EXPECTED;

		final Set loadButIds = new HashSet(butIds);
		for (final Iterator it = alsoButIdentifiables.iterator(); it.hasNext();) {
			final Identifiable identifiable = (Identifiable) it.next();
			final Identifier id = identifiable.getId();
			loadButIds.add(id);
		}
		return loadButIds;
	}

	protected static final void addToLoadButIds(final Set loadButIds, final Set alsoButIdentifiables) {
		assert loadButIds != null : ErrorMessages.NON_NULL_EXPECTED;
		assert alsoButIdentifiables != null : ErrorMessages.NON_NULL_EXPECTED;

		for (final Iterator it = alsoButIdentifiables.iterator(); it.hasNext();) {
			final Identifiable identifiable = (Identifiable) it.next();
			final Identifier id = identifiable.getId();
			loadButIds.add(id);
		}
	}
}
