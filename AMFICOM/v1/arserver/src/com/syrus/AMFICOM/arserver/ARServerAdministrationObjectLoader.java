/*-
 * $Id: ARServerAdministrationObjectLoader.java,v 1.1 2005/06/07 12:30:28 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.arserver;

import java.util.Collections;
import java.util.Set;

import com.syrus.AMFICOM.administration.DatabaseAdministrationObjectLoader;
import com.syrus.AMFICOM.general.ApplicationException;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/06/07 12:30:28 $
 * @module arserver_v1
 */
final class ARServerAdministrationObjectLoader extends DatabaseAdministrationObjectLoader {
	/**
	 * @param storableObjects
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.DatabaseObjectLoader#refresh(java.util.Set)
	 */
	public Set refresh(final Set storableObjects) throws ApplicationException {
		return Collections.EMPTY_SET;
	}
}
