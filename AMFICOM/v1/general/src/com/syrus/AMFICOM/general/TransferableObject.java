/*-
 * $Id: TransferableObject.java,v 1.12 2005/10/06 15:19:44 bass Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.io.Serializable;

import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.IDLEntity;

/**
 * @version $Revision: 1.12 $, $Date: 2005/10/06 15:19:44 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public interface TransferableObject<T extends IDLEntity> extends Serializable {

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

	T getTransferable(final ORB orb);
}
