/*
 * $Id: TypedObject.java,v 1.5 2004/08/17 14:33:42 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.5 $, $Date: 2004/08/17 14:33:42 $
 * @author $Author: bob $
 * @module general_v1
 */

public interface TypedObject {
	final String ID_CODENAME 		= "codename" + TransferableObject.KEY_VALUE_SEPERATOR;
	final String ID_DESCRIPTION 	= "description"  + TransferableObject.KEY_VALUE_SEPERATOR ;

	StorableObjectType getType();
}
