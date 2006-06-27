/*
 * $Id: LocalIdentifierGenerator.java,v 1.10.2.1 2006/06/27 15:55:49 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.util.HashSet;
import java.util.Set;
/**
 * @version $Revision: 1.10.2.1 $, $Date: 2006/06/27 15:55:49 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */

public class LocalIdentifierGenerator {
	private static final long MAX_COUNTER = 100000L;
	private static long counter = 0;

	private LocalIdentifierGenerator() {
		// singleton constructor
	}

	public static synchronized Identifier generateIdentifier(final short entityCode) throws IllegalObjectEntityException {
		final short major = generateMajor(entityCode);
		final long minor = generateMinor(entityCode);
		return new Identifier(major, minor);
	}

	public static synchronized Set<Identifier> generateIdentifierRange(final short entityCode, final int rangeSize)
			throws IllegalObjectEntityException {
		final Set<Identifier> ids = new HashSet<Identifier>();

		final short major = generateMajor(entityCode);
		for (int i = 0; i < rangeSize; i++) {
			final long minor = generateMinor(entityCode);
			ids.add(new Identifier(major, minor));
		}
		return ids;
	}

	private static short generateMajor(final short entityCode) throws IllegalObjectEntityException {
		if (ObjectEntities.isEntityCodeValid(entityCode)) {
			return entityCode;
		}
		throw new IllegalObjectEntityException(ErrorMessages.ILLEGAL_ENTITY_CODE + ": " + entityCode,
				IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
	}

	private static long generateMinor(final short entityCode) {
		if (counter >= MAX_COUNTER) {
			counter = 0;
		}

		return entityCode + System.currentTimeMillis() + (counter++);
	}
}
