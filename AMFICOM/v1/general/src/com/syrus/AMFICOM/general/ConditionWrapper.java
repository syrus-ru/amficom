/*
 * $Id: ConditionWrapper.java,v 1.1 2005/03/16 08:02:19 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.util.Collection;

/**
 * @version $Revision: 1.1 $, $Date: 2005/03/16 08:02:19 $
 * @author $Author: max $
 * @module general_v1
 */
public interface ConditionWrapper {
	
	
	public static final byte INT 		= 0;
	public static final byte FLOAT 		= 1;
	public static final byte DOUBLE 	= 2;
	public static final byte STRING 	= 3;
	public static final byte LIST 		= 4;
	public static final byte CONSTRAINT = 5;
	
	Object getLinkedObject(String key, int indexNumber) throws IllegalDataException;
	
	public String getKey(String keyName);
	public byte getType(String key);
	public String[] getLinkedNames(String key) throws IllegalDataException;
	
	public Collection getKeys();
	public Collection getKeyNames();
	public String[] getInitialNames();
	

	short getEntityCode();
	
}
