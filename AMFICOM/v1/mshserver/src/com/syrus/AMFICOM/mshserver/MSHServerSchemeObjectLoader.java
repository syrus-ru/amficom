/*-
 * $Id: MSHServerSchemeObjectLoader.java,v 1.1 2005/04/01 15:12:43 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mshserver;

import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.*;
import java.util.*;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/04/01 15:12:43 $
 * @module mshserver_v1
 */
public final class MSHServerSchemeObjectLoader extends DatabaseSchemeObjectLoader {
	private long refreshTimeout;

	/**
	 * @param refreshTimeout
	 */
	public MSHServerSchemeObjectLoader(final long refreshTimeout) {
		this.refreshTimeout = refreshTimeout;
	}

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
