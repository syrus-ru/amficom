/*
 * $Id: ConditionWrapperTemp.java,v 1.3 2005/03/16 17:08:00 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.3 $, $Date: 2005/03/16 17:08:00 $
 * @author $Author: bob $
 * @module general_v1
 */
public interface ConditionWrapperTemp {

	byte	INT			= 0;
	byte	FLOAT		= 1;
	byte	DOUBLE		= 2;
	byte	STRING		= 3;
	byte	LIST		= 4;
	byte	CONSTRAINT	= 5;

	Object getLinkedObject(	String key,
							int indexNumber) throws IllegalDataException;

	byte getType(int index);

	String getKey(int index);

	String getKeyName(int key);

	String getKeyName(String key);

	String[] getKeys();

	String[] getKeyNames();

	String[] getLinkedNames(String key) throws IllegalDataException;

	String[] getInitialNames();

	short getEntityCode();

}
