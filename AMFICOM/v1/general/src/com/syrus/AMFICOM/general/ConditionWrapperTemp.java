/*
 * $Id: ConditionWrapperTemp.java,v 1.1 2005/03/16 08:05:07 max Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
package com.syrus.AMFICOM.general;

import java.util.Collection;

/**
 * @version $Revision: 1.1 $, $Date: 2005/03/16 08:05:07 $
 * @author $Author: max $
 * @module general_v1
 */
public interface ConditionWrapperTemp {
	
	
	public static final byte INT 		= 0;
	public static final byte FLOAT 		= 1;
	public static final byte DOUBLE 	= 2;
	public static final byte STRING 	= 3;
	public static final byte LIST 		= 4;
	public static final byte CONSTRAINT = 5;
	
	Object getLinkedObject(String key, int indexNumber) throws IllegalDataException;
	public byte getType(int index);
	
	public String getKey(int index);
	public String getKeyName(int key);
	public String getKeyName(String key);
	public String[] getKeys();
	public String[] getKeyNames();
	public String[] getLinkedNames(String key) throws IllegalDataException;
	public String[] getInitialNames();
	

	short getEntityCode();
	
}
