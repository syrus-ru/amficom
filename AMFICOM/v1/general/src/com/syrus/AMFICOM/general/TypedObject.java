/*
 * $Id: TypedObject.java,v 1.10 2005/10/07 09:51:16 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.10 $, $Date: 2005/10/07 09:51:16 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */

public interface TypedObject<T extends StorableObjectType> {
	T getType();
}
