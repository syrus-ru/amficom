/*-
 * $Id: MscharServerSchemeObjectLoader.java,v 1.1 2005/06/07 16:47:00 bass Exp $
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
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/06/07 16:47:00 $
 * @module mscharserver_v1
 */
public final class MscharServerSchemeObjectLoader extends DatabaseSchemeObjectLoader {
	/**
	 * @param storableObjects
	 * @throws CommunicationException
	 * @throws DatabaseException
	 * @see SchemeObjectLoader#refresh(Set)
	 */
	public Set refresh(final Set storableObjects) throws CommunicationException, DatabaseException {
		return Collections.EMPTY_SET;
	}
}
