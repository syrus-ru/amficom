/*
 * $Id: LocalIdentifierGenerator.java,v 1.2 2004/12/20 13:29:37 krupenn Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.util.List;
import java.util.LinkedList;
/**
 * @version $Revision: 1.2 $, $Date: 2004/12/20 13:29:37 $
 * @author $Author: krupenn $
 * @module generalclient_v1
 */

public class LocalIdentifierGenerator {
	private static final long MAX_COUNTER = 100000L;
	private static long counter = 0;

	private LocalIdentifierGenerator() {
		// singleton constructor
	}

	public static synchronized Identifier generateIdentifier(short entityCode) throws IllegalObjectEntityException, IdentifierGenerationException {
		short major = generateMajor(entityCode);
		long minor = generateMinor(entityCode);
		return new Identifier(major, minor);
	}

	public static synchronized Identifier[] generateIdentifierRange(short entityCode, int rangeSize) throws IllegalObjectEntityException, IdentifierGenerationException {
		List list = new LinkedList();
		short major = generateMajor(entityCode);
		long minor;
		for (int i = 0; i < rangeSize; i++) {
			minor = generateMinor(entityCode);
			list.add(new Identifier(major, minor));
		}
		return (Identifier[])list.toArray(new Identifier[list.size()]);
	}

	private static short generateMajor(short entityCode) throws IllegalObjectEntityException {
		if (ObjectEntities.codeIsValid(entityCode))
			return entityCode;
		throw new IllegalObjectEntityException("Illegal or unknown entity code supplied: " + entityCode, IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
	}

	private static long generateMinor(short entityCode) throws IdentifierGenerationException {
		if (counter >= MAX_COUNTER)
			counter = 0;

		return System.currentTimeMillis() + (counter++);
	}
}
