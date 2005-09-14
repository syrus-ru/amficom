/*-
 * $Id: TransferableObject.java,v 1.11 2005/09/14 18:51:56 arseniy Exp $
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
 * @version $Revision: 1.11 $, $Date: 2005/09/14 18:51:56 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public interface TransferableObject extends Serializable {

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

	IDLEntity getTransferable(final ORB orb);
}
