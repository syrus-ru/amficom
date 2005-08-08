/*
 * $Id: ConditionWrapper.java,v 1.8 2005/08/08 11:27:25 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Collection;


/**
 * @version $Revision: 1.8 $, $Date: 2005/08/08 11:27:25 $
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

	Collection getKeys();
	short getEntityCode();
}
