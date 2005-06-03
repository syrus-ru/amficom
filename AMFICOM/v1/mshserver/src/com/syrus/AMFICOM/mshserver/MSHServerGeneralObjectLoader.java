/*-
 * $Id: MSHServerGeneralObjectLoader.java,v 1.1 2005/06/03 14:53:14 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mshserver;

import java.util.Collections;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseGeneralObjectLoader;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/06/03 14:53:14 $
 * @module mshserver_v1
 */
final class MSHServerGeneralObjectLoader extends DatabaseGeneralObjectLoader {
	/**
	 * @param storableObjects
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.DatabaseObjectLoader#refresh(java.util.Set)
	 */
	public Set refresh(final Set storableObjects) throws ApplicationException {
		return Collections.EMPTY_SET;
	}
}
