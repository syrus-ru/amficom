/*
* $Id: MSHDatabaseMapObjectLoader.java,v 1.1 2004/12/09 12:20:41 cvsadmin Exp $
*
* Copyright ¿ 2004 Syrus Systems.
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
 * @version $Revision: 1.1 $, $Date: 2004/12/09 12:20:41 $
 * @author $Author: cvsadmin $
 * @module mshserver_1
 */
public final class MSHDatabaseMapObjectLoader extends DatabaseMapObjectLoader {

	public Set refresh(Set storableObjects) throws CommunicationException, DatabaseException {
		/**
		 * there are not resons for refresh due to all changes made using one MSHServer
		 */
		return Collections.EMPTY_SET;
	}
	
}
