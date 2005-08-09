/*
 * $Id: ConditionWrapper.java,v 1.1 2005/08/09 20:29:44 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Collection;

import com.syrus.AMFICOM.newFilter.ConditionKey;


/**
 * @version $Revision: 1.1 $, $Date: 2005/08/09 20:29:44 $
 * @author $Author: arseniy $
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

	Collection<ConditionKey> getKeys();
	short getEntityCode();
}
