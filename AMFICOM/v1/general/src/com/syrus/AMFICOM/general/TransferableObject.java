/*-
 * $Id: TransferableObject.java,v 1.7 2005/04/04 13:07:03 bass Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import org.omg.CORBA.portable.IDLEntity;

/**
 * @version $Revision: 1.7 $, $Date: 2005/04/04 13:07:03 $
 * @author $Author: bass $
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

	IDLEntity getTransferable();
}
