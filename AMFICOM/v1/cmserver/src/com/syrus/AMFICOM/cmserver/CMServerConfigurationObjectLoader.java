/*
* $Id: CMServerConfigurationObjectLoader.java,v 1.8 2005/07/13 19:35:43 arseniy Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.cmserver;

import java.util.Collections;
import java.util.Set;

import com.syrus.AMFICOM.configuration.DatabaseConfigurationObjectLoader;
import com.syrus.AMFICOM.general.StorableObject;


/**
 * @version $Revision: 1.8 $, $Date: 2005/07/13 19:35:43 $
 * @author $Author: arseniy $
 * @module cmserver_v1
 */
public class CMServerConfigurationObjectLoader extends DatabaseConfigurationObjectLoader {
	@Override
	@SuppressWarnings("unused")
	public Set refresh(final Set<? extends StorableObject> storableObjects) {
		/**
		 * there is no reason to refresh because configuration entities couldn't change out of cmserver
		 */
		return Collections.emptySet();
	}
}
