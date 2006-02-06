/*
 * $Id: LocalIdentifierGenerator.java,v 1.10 2005/09/14 18:21:32 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.util.List;
import java.util.LinkedList;
/**
 * @version $Revision: 1.10 $, $Date: 2005/09/14 18:21:32 $
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

	public static synchronized Identifier[] generateIdentifierRange(final short entityCode, final int rangeSize)
			throws IllegalObjectEntityException {
		final List<Identifier> list = new LinkedList<Identifier>();
		final short major = generateMajor(entityCode);
		for (int i = 0; i < rangeSize; i++) {
			final long minor = generateMinor(entityCode);
			list.add(new Identifier(major, minor));
		}
		return list.toArray(new Identifier[list.size()]);
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
