/*
 * $Id: TransferableObject.java,v 1.6 2004/12/27 14:42:54 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.6 $, $Date: 2004/12/27 14:42:54 $
 * @author $Author: arseniy $
 * @module general_v1
 */

public interface TransferableObject {

	String KEY_VALUE_SEPERATOR = ":";
	String ID 				= "id" + KEY_VALUE_SEPERATOR;
	String ID_CREATED 		= "created" + KEY_VALUE_SEPERATOR;
	String ID_MODIFIED 		= "modified" + KEY_VALUE_SEPERATOR;
	String ID_CREATOR_ID 	= "creatorId" + KEY_VALUE_SEPERATOR;
	String ID_MODIFIER_ID 	= "modifierId" + KEY_VALUE_SEPERATOR;
	String ID_VERSION 		= "version" + KEY_VALUE_SEPERATOR;

	String NULL 				= "'null'";
	/**
	 * end of String line
	 */
	String EOSL				= ";\n";
	String OPEN_BLOCK			= " { \n";
	String CLOSE_BLOCK		= "\n}" + EOSL;

	Object getTransferable();
}
