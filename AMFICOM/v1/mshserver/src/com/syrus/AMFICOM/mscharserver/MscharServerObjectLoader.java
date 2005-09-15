/*-
 * $Id: MscharServerObjectLoader.java,v 1.4 2005/09/15 00:54:17 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.mscharserver;

import com.syrus.AMFICOM.general.DatabaseObjectLoader;

/**
 * @version $Revision: 1.4 $, $Date: 2005/09/15 00:54:17 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mscharserver
 * @todo Implement refresh (i. e. - method {@link ObjectLoader#getRemoteVersions(Set)})
 * with timeout checking, using field {@link #refreshTimeout}
 */
final class MscharServerObjectLoader extends DatabaseObjectLoader {
	private long refreshTimeout;
	private long lastRefreshTime;

	protected MscharServerObjectLoader(final long refreshTimeout) {
		this.refreshTimeout = refreshTimeout;
		this.lastRefreshTime = System.currentTimeMillis();
	}

}
