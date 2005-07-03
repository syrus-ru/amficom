/*
 * $Id: ConditionWrapper.java,v 1.7 2005/04/12 13:19:12 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Collection;


/**
 * @version $Revision: 1.7 $, $Date: 2005/04/12 13:19:12 $
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

	Collection getKeys();
	short getEntityCode();
}
