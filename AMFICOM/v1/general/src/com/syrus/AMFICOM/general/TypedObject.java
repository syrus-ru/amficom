/*
 * $Id: TypedObject.java,v 1.6 2004/08/18 11:28:07 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.6 $, $Date: 2004/08/18 11:28:07 $
 * @author $Author: arseniy $
 * @module general_v1
 */

public interface TypedObject {
	String ID_CODENAME 		= "codename" + TransferableObject.KEY_VALUE_SEPERATOR;
	String ID_DESCRIPTION 	= "description"  + TransferableObject.KEY_VALUE_SEPERATOR ;

	StorableObjectType getType();
}
