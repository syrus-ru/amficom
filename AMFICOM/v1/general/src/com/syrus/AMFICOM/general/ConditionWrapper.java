/*
 * $Id: ConditionWrapper.java,v 1.2 2005/03/16 17:08:00 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Collection;

/**
 * @version $Revision: 1.2 $, $Date: 2005/03/16 17:08:00 $
 * @author $Author: bob $
 * @module general_v1
 */
public interface ConditionWrapper {

	byte	INT			= 0;
	byte	FLOAT		= 1;
	byte	DOUBLE		= 2;
	byte	STRING		= 3;
	byte	LIST		= 4;
	byte	CONSTRAINT	= 5;

	Object getLinkedObject(	String key,
							int indexNumber) throws IllegalDataException;

	String getKey(String keyName);

	byte getType(String key);

	String[] getLinkedNames(String key) throws IllegalDataException;

	Collection getKeys();

	Collection getKeyNames();

	String[] getInitialNames();

	short getEntityCode();

}
