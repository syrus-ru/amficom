/*
* $Id: MscharServerMapObjectLoader.java,v 1.1 2005/06/07 16:47:00 bass Exp $
*
* Copyright � 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.mscharserver;

import java.util.Collections;
import java.util.Set;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.map.DatabaseMapObjectLoader;


/**
 * @version $Revision: 1.1 $, $Date: 2005/06/07 16:47:00 $
 * @author $Author: bass $
 * @module mscharserver_v1
 */
public final class MscharServerMapObjectLoader extends DatabaseMapObjectLoader {
	public Set refresh(Set storableObjects) throws CommunicationException, DatabaseException {
		/**
		 * there are not resons for refresh due to all changes made using one MSHServer
		 */
		return Collections.EMPTY_SET;
	}
}
