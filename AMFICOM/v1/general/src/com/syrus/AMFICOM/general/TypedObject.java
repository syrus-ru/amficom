/*
 * $Id: TypedObject.java,v 1.8 2005/09/11 15:27:42 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.8 $, $Date: 2005/09/11 15:27:42 $
 * @author $Author: bass $
 * @module general
 */

public interface TypedObject<T extends StorableObjectType> {
	String ID_CODENAME = "codename" + TransferableObject.KEY_VALUE_SEPERATOR;
	String ID_DESCRIPTION = "description"  + TransferableObject.KEY_VALUE_SEPERATOR ;

	T getType();
}
