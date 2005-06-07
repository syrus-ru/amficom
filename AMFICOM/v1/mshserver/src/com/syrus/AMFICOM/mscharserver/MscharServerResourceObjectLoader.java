/*
 * $Id: MscharServerResourceObjectLoader.java,v 1.1 2005/06/07 16:47:00 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mscharserver;

import java.util.Collections;
import java.util.Set;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.resource.DatabaseResourceObjectLoader;

/**
 * @version $Revision: 1.1 $, $Date: 2005/06/07 16:47:00 $
 * @author $Author: bass $
 * @module mscharserver_v1
 * @todo add methods.
 */
public class MscharServerResourceObjectLoader extends DatabaseResourceObjectLoader {
	public Set refresh(Set storableObjects) throws CommunicationException, DatabaseException {
		/**
		 * there are not resons for refresh due to all changes made using one MSHServer
		 */
		return Collections.EMPTY_SET;
	}
}
