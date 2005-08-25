/*
 * $Id: ConditionWrapper.java,v 1.2 2005/08/25 10:33:41 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.List;

import com.syrus.AMFICOM.newFilter.ConditionKey;


/**
 * @version $Revision: 1.2 $, $Date: 2005/08/25 10:33:41 $
 * @author $Author: max $
 * @module general
 */
public interface ConditionWrapper {

	byte INT = 0;
	byte FLOAT = 1;
	byte DOUBLE = 2;
	byte STRING = 3;
	byte LIST = 4;
	byte CONSTRAINT = 5;
	byte DATE	= 6;

	List<ConditionKey> getKeys();
	short getEntityCode();
}
