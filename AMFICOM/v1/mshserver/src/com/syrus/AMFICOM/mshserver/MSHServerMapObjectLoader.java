/*
* $Id: MSHServerMapObjectLoader.java,v 1.2 2005/05/27 11:13:49 bass Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.mshserver;

import java.util.Collections;
import java.util.Set;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.map.DatabaseMapObjectLoader;


/**
 * @version $Revision: 1.2 $, $Date: 2005/05/27 11:13:49 $
 * @author $Author: bass $
 * @module mshserver_1
 */
public final class MSHServerMapObjectLoader extends DatabaseMapObjectLoader {
	public Set refresh(Set storableObjects) throws CommunicationException, DatabaseException {
		/**
		 * there are not resons for refresh due to all changes made using one MSHServer
		 */
		return Collections.EMPTY_SET;
	}
}
