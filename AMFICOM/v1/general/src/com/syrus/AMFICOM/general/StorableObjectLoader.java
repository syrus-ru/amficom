/*
 * $Id: StorableObjectLoader.java,v 1.3 2004/12/17 18:44:23 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.3 $, $Date: 2004/12/17 18:44:23 $
 * @author $Author: arseniy $
 * @module general_v1
 */


public interface StorableObjectLoader {

	StorableObject loadStorableObject(Identifier objectId) throws DatabaseException, CommunicationException;

}
