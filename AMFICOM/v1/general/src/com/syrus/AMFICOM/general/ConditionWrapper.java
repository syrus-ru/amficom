/*
 * $Id: ConditionWrapper.java,v 1.4 2005/03/29 16:56:21 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Collection;

/**
 * @version $Revision: 1.4 $, $Date: 2005/03/29 16:56:21 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public interface ConditionWrapper {

	byte INT = 0;
	byte FLOAT = 1;
	byte DOUBLE = 2;
	byte STRING = 3;
	byte LIST = 4;
	byte CONSTRAINT = 5;

	Object getLinkedObject(String key, int indexNumber) throws IllegalDataException;

	byte[] getTypes();

	String[] getKeys();

	String[] getKeyNames();

	String[] getLinkedNames(String key) throws IllegalDataException;

	String getInitialName(StorableObject storableObject);

	Collection getInitialEntities();

	short getEntityCode();
}
