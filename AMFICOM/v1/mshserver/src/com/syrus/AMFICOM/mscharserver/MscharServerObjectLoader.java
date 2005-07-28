/*-
 * $Id: MscharServerObjectLoader.java,v 1.3 2005/07/28 17:01:59 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.mscharserver;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectVersion;

/**
 * @version $Revision: 1.3 $, $Date: 2005/07/28 17:01:59 $
 * @author $Author: arseniy $
 * @module mscharserver
 */
final class MscharServerObjectLoader extends DatabaseObjectLoader {
	private long refreshTimeout;
	private long lastRefreshTime;
	
	protected MscharServerObjectLoader(final long refreshTimeout) {
		this.refreshTimeout = refreshTimeout;
		this.lastRefreshTime = System.currentTimeMillis();
	}
	
	@Override
	public Set<Identifier> getOldVersionIds(final Map<Identifier, StorableObjectVersion> versionsMap) throws ApplicationException {
		if (System.currentTimeMillis() - this.lastRefreshTime <= this.refreshTimeout) {
			return Collections.emptySet();
		}
		return super.getOldVersionIds(versionsMap);
	}

	
}
