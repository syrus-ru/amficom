/*
* $Id: CMServerConfigurationObjectLoader.java,v 1.5 2005/04/01 17:39:02 arseniy Exp $
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
 * @version $Revision: 1.5 $, $Date: 2005/04/01 17:39:02 $
 * @author $Author: arseniy $
 * @module cmserver_v1
 */
public class CMServerConfigurationObjectLoader extends DatabaseConfigurationObjectLoader {

	 /**
	 * refresh timeout 
	 */
	private long refreshTimeout;

	public CMServerConfigurationObjectLoader(long refreshTimeout) {
		this.refreshTimeout = refreshTimeout;
	}

	public Set refresh(Set storableObjects) {
		/**
		 * there is no reason to refresh because configuration entities couldn't change out of cmserver
		 */
		return Collections.EMPTY_SET;
	}
}
