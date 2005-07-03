/*
* $Id: MscharServerMapObjectLoader.java,v 1.2 2005/06/07 17:58:13 bass Exp $
*
* Copyright ¿ 2005 Syrus Systems.
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
 * @version $Revision: 1.2 $, $Date: 2005/06/07 17:58:13 $
 * @author $Author: bass $
 * @module mscharserver_v1
 */
public final class MscharServerMapObjectLoader extends DatabaseMapObjectLoader {
	public Set refresh(Set storableObjects) throws CommunicationException, DatabaseException {
		/*
		 * There's no reason to refresh since all changes are made by a
		 * single MscharServer.
		 */
		return Collections.EMPTY_SET;
	}
}
