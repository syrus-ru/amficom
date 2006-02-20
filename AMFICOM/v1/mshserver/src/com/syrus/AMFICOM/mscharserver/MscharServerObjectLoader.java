/*-
 * $Id: MscharServerObjectLoader.java,v 1.5 2006/02/20 10:22:14 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.mscharserver;

import com.syrus.AMFICOM.general.DatabaseObjectLoader;

/**
 * @version $Revision: 1.5 $, $Date: 2006/02/20 10:22:14 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module mscharserver
 * @todo Implement refresh (i. e. - method {@link com.syrus.AMFICOM.general.ObjectLoader#getRemoteVersions(java.util.Set)})
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
