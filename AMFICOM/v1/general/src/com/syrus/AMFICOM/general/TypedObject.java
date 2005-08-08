/*
 * $Id: TypedObject.java,v 1.7 2005/08/08 11:27:26 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.7 $, $Date: 2005/08/08 11:27:26 $
 * @author $Author: arseniy $
 * @module general
 */

public interface TypedObject {
	String ID_CODENAME 		= "codename" + TransferableObject.KEY_VALUE_SEPERATOR;
	String ID_DESCRIPTION 	= "description"  + TransferableObject.KEY_VALUE_SEPERATOR ;

	StorableObjectType getType();
}
