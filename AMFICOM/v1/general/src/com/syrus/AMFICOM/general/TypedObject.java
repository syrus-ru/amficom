/*
 * $Id: TypedObject.java,v 1.4 2004/08/06 13:43:44 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.4 $, $Date: 2004/08/06 13:43:44 $
 * @author $Author: arseniy $
 * @module general_v1
 */

public interface TypedObject {
	StorableObjectType getType();
}
