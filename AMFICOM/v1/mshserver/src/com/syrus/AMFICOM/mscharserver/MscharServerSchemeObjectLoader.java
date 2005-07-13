/*-
 * $Id: MscharServerSchemeObjectLoader.java,v 1.2 2005/07/13 19:17:12 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mscharserver;

import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.*;
import java.util.*;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.2 $, $Date: 2005/07/13 19:17:12 $
 * @module mscharserver_v1
 */
public final class MscharServerSchemeObjectLoader extends DatabaseSchemeObjectLoader {
	/**
	 * @param storableObjects
	 * @throws CommunicationException
	 * @throws DatabaseException
	 * @see SchemeObjectLoader#refresh(Set)
	 */
	@Override
	public Set refresh(final Set<? extends StorableObject> storableObjects) throws CommunicationException, DatabaseException {
		return Collections.emptySet();
	}
}
