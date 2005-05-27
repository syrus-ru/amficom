/*
* $Id: CMServerConfigurationObjectLoader.java,v 1.7 2005/05/27 11:13:49 bass Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.cmserver;

import java.util.Collections;
import java.util.Set;

import com.syrus.AMFICOM.configuration.DatabaseConfigurationObjectLoader;


/**
 * @version $Revision: 1.7 $, $Date: 2005/05/27 11:13:49 $
 * @author $Author: bass $
 * @module cmserver_v1
 */
public class CMServerConfigurationObjectLoader extends DatabaseConfigurationObjectLoader {
	public Set refresh(Set storableObjects) {
		/**
		 * there is no reason to refresh because configuration entities couldn't change out of cmserver
		 */
		return Collections.EMPTY_SET;
	}
}
