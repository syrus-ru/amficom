/*
 * $Id: TransferableObject.java,v 1.4 2004/08/17 14:33:42 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.4 $, $Date: 2004/08/17 14:33:42 $
 * @author $Author: bob $
 * @module general_v1
 */

public interface TransferableObject {

	final String KEY_VALUE_SEPERATOR = ":";
	final String ID 				= "id"+KEY_VALUE_SEPERATOR;
	final String ID_CREATED 		= "created"+KEY_VALUE_SEPERATOR;
	final String ID_MODIFIED 		= "modified"+KEY_VALUE_SEPERATOR;
	final String ID_CREATOR_ID 	= "creatorId"+KEY_VALUE_SEPERATOR;
	final String ID_MODIFIER_ID 	= "modifierId"+KEY_VALUE_SEPERATOR;
	final String ID_VERSION 		= "version"+KEY_VALUE_SEPERATOR;
	
	final String NULL 				= "'null'";
	/**
	 * end of String line
	 */
	final String EOSL				= ";\n";
	final String OPEN_BLOCK			= " { \n";
	final String CLOSE_BLOCK		= "\n}" + EOSL;
	
	Object getTransferable();
}
