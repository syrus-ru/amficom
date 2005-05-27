/*
 * $Id: ARServerResourceObjectLoader.java,v 1.4 2005/05/27 11:13:49 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.arserver;

import java.util.Collections;
import java.util.Set;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.resource.DatabaseResourceObjectLoader;

/**
 * @version $Revision: 1.4 $, $Date: 2005/05/27 11:13:49 $
 * @author $Author: bass $
 * @module arserver_v1
 * @todo add methods.
 */
public class ARServerResourceObjectLoader extends DatabaseResourceObjectLoader {
	public Set refresh(Set storableObjects) throws CommunicationException, DatabaseException {
		/**
		 * there are not resons for refresh due to all changes made using one MSHServer
		 */
		return Collections.EMPTY_SET;
	}
}
