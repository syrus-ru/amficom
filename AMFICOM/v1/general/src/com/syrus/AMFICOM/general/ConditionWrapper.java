/*
 * $Id: ConditionWrapper.java,v 1.3 2005/03/25 10:48:33 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Collection;

/**
 * @version $Revision: 1.3 $, $Date: 2005/03/25 10:48:33 $
 * @author $Author: max $
 * @module general_v1
 */
public interface ConditionWrapper {

	public static final byte	INT			= 0;

	public static final byte	FLOAT		= 1;

	public static final byte	DOUBLE		= 2;

	public static final byte	STRING		= 3;

	public static final byte	LIST		= 4;

	public static final byte	CONSTRAINT	= 5;

	Object getLinkedObject(String key, int indexNumber)
			throws IllegalDataException;

	public byte[] getTypes();

	public String[] getKeys();

	public String[] getKeyNames();

	public String[] getLinkedNames(String key) throws IllegalDataException;

	public String getInitialName(StorableObject storableObject);

	public Collection getInitialEntities();

	short getEntityCode();
}
