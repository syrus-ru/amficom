/*
 * $Id: LocalIdentifierGenerator.java,v 1.7 2005/07/14 11:29:53 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.util.List;
import java.util.LinkedList;
/**
 * @version $Revision: 1.7 $, $Date: 2005/07/14 11:29:53 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */

public class LocalIdentifierGenerator {
	private static final long MAX_COUNTER = 100000L;
	private static long counter = 0;

	private LocalIdentifierGenerator() {
		// singleton constructor
	}

	public static synchronized Identifier generateIdentifier(short entityCode) throws IllegalObjectEntityException {
		short major = generateMajor(entityCode);
		long minor = generateMinor(entityCode);
		return new Identifier(major, minor);
	}

	public static synchronized Identifier[] generateIdentifierRange(short entityCode, int rangeSize) throws IllegalObjectEntityException {
		List<Identifier> list = new LinkedList<Identifier>();
		short major = generateMajor(entityCode);
		long minor;
		for (int i = 0; i < rangeSize; i++) {
			minor = generateMinor(entityCode);
			list.add(new Identifier(major, minor));
		}
		return list.toArray(new Identifier[list.size()]);
	}

	private static short generateMajor(short entityCode) throws IllegalObjectEntityException {
		if (ObjectEntities.isEntityCodeValid(entityCode))
			return entityCode;
		throw new IllegalObjectEntityException(ErrorMessages.ILLEGAL_ENTITY_CODE + ": " + entityCode, IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
	}

	private static long generateMinor(short entityCode) {
		if (counter >= MAX_COUNTER)
			counter = 0;

		return entityCode + System.currentTimeMillis() + (counter++);
	}
}
