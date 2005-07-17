/*
 * $Id: ObjectLoader.java,v 1.7 2005/07/17 01:40:03 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.util.HashSet;
import java.util.Set;

/**
 * @version $Revision: 1.7 $, $Date: 2005/07/17 01:40:03 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 * @deprecated No really need to us this.
 */
@Deprecated
public abstract class ObjectLoader {

	/**
	 * Creates a set of identifiers as substract between identifiers from set <code>ids</code>
	 * and identifiers (of identifiables) from set <code>butIdentifiables</code>
	 * @param ids
	 * @param butIdentifiables
	 * @return Set of identifiers
	 * @deprecated Use Identifier#createSubstractionIdentifiers
	 */
	@Deprecated
	protected static final Set createLoadIds(final Set<Identifier> ids, final Set<Identifiable> butIdentifiables) {
		assert ids != null && !ids.isEmpty() : ErrorMessages.NON_EMPTY_EXPECTED;
		assert butIdentifiables != null : ErrorMessages.NON_NULL_EXPECTED;

		final Set<Identifier> loadIds = new HashSet<Identifier>(ids);
		final Set<Identifier> butIds = Identifier.createIdentifiers(butIdentifiables);
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
	@Deprecated
	protected static final void removeFromLoadIds(final Set<Identifier> loadIds, final Set<Identifiable> butIdentifiables) {
		assert loadIds != null && !loadIds.isEmpty() : ErrorMessages.NON_EMPTY_EXPECTED;
		assert butIdentifiables != null : ErrorMessages.NON_NULL_EXPECTED;

		final Set<Identifier> butIds = Identifier.createIdentifiers(butIdentifiables);
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
	@Deprecated
	protected static final Set createLoadButIds(final Set<Identifier> butIds, final Set<Identifiable> alsoButIdentifiables) {
		assert butIds != null : ErrorMessages.NON_NULL_EXPECTED;
		assert alsoButIdentifiables != null : ErrorMessages.NON_NULL_EXPECTED;

		final Set<Identifier> loadButIds = new HashSet<Identifier>(butIds);
		final Set<Identifier> alsoButIds = Identifier.createIdentifiers(alsoButIdentifiables);
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
	@Deprecated
	protected static final void addToLoadButIds(final Set<Identifier> loadButIds, final Set<Identifiable> alsoButIdentifiables) {
		assert loadButIds != null : ErrorMessages.NON_NULL_EXPECTED;
		assert alsoButIdentifiables != null : ErrorMessages.NON_NULL_EXPECTED;

		final Set<Identifier> alsoButIds = Identifier.createIdentifiers(alsoButIdentifiables);
		loadButIds.addAll(alsoButIds);
	}
}
