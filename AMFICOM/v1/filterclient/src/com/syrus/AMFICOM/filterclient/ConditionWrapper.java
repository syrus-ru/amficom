/*
 * $Id: ConditionWrapper.java,v 1.1 2005/03/30 14:23:32 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.filterclient;

import java.util.Collection;

import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObject;

/**
 * @version $Revision: 1.1 $, $Date: 2005/03/30 14:23:32 $
 * @author $Author: max $
 * @module general_v1
 */
public interface ConditionWrapper {

	byte INT = 0;
	byte FLOAT = 1;
	byte DOUBLE = 2;
	byte STRING = 3;
	byte LIST = 4;
	byte CONSTRAINT = 5;
	byte DATE	= 6;

	Object getLinkedObject(String key, int indexNumber) throws IllegalDataException;

	byte[] getTypes();

	String[] getKeys();

	String[] getKeyNames();

	String[] getLinkedNames(String key) throws IllegalDataException;

	String getInitialName(StorableObject storableObject);

	Collection getInitialEntities();

	short getEntityCode();
}
