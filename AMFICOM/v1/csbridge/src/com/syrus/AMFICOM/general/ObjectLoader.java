/*
 * $Id: ObjectLoader.java,v 1.5 2005/06/10 11:06:15 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.util.HashSet;
import java.util.Set;

/**
 * @version $Revision: 1.5 $, $Date: 2005/06/10 11:06:15 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public abstract class ObjectLoader {

	/**
	 * Creates a set of identifiers as substract between identifiers from set <code>ids</code>
	 * and identifiers (of identifiables) from set <code>butIdentifiables</code>
	 * @param ids
	 * @param butIdentifiables
	 * @return Set of identifiers
	 * @deprecated Use Identifier#createSubstractionIdentifiers
	 */
	protected static final Set createLoadIds(final Set ids, final Set butIdentifiables) {
		assert ids != null && !ids.isEmpty() : ErrorMessages.NON_EMPTY_EXPECTED;
		assert butIdentifiables != null : ErrorMessages.NON_NULL_EXPECTED;

		final Set loadIds = new HashSet(ids);
		final Set butIds = Identifier.createIdentifiers(butIdentifiables);
		loadIds.removeAll(butIds);
		return loadIds;
	}

	/**
	 * Removes from set of identifiers <code>loadIds</code> those,
	 * which contained in set of identifiables <code>butIdentifiables</code>.
	 * (I. e., parameter <code>loadIds</code> is passed as &quot;inout&quot; argument.
	 * @param loadIds
	 * @param butIdentifiables
	 * @deprecated Use Identifier#substractFromIdentifiers
	 */
	protected static final void removeFromLoadIds(final Set loadIds, final Set butIdentifiables) {
		assert loadIds != null && !loadIds.isEmpty() : ErrorMessages.NON_EMPTY_EXPECTED;
		assert butIdentifiables != null : ErrorMessages.NON_NULL_EXPECTED;

		final Set butIds = Identifier.createIdentifiers(butIdentifiables);
		loadIds.removeAll(butIds);
	}

	/**
	 * Creates a set of identifiers as sum of identifiers from set <code>butIds</code>
	 * and identifiers (of identifiables) from set <code>alsoButIdentifiables</code>
	 * @param butIds
	 * @param alsoButIdentifiables
	 * @return Set of identifiers
	 * @deprecated Use Identifier#createSumIdentifiers
	 */
	protected static final Set createLoadButIds(final Set butIds, final Set alsoButIdentifiables) {
		assert butIds != null : ErrorMessages.NON_NULL_EXPECTED;
		assert alsoButIdentifiables != null : ErrorMessages.NON_NULL_EXPECTED;

		final Set loadButIds = new HashSet(butIds);
		final Set alsoButIds = Identifier.createIdentifiers(alsoButIdentifiables);
		loadButIds.addAll(alsoButIds);
		return loadButIds;
	}

	/**
	 * Adds to set of identifiers <code>loadButIds</code>
	 * identifiers from set of identifiables <code>alsoButIdentifiables</code>.
	 * (I. e., parameter <code>loadButIds</code> is passed as &quot;inout&quot; argument.
	 * @param loadButIds
	 * @param alsoButIdentifiables
	 * @deprecated Use Identifier#addToIdentifiers
	 */
	protected static final void addToLoadButIds(final Set loadButIds, final Set alsoButIdentifiables) {
		assert loadButIds != null : ErrorMessages.NON_NULL_EXPECTED;
		assert alsoButIdentifiables != null : ErrorMessages.NON_NULL_EXPECTED;

		final Set alsoButIds = Identifier.createIdentifiers(alsoButIdentifiables);
		loadButIds.addAll(alsoButIds);
	}
}
