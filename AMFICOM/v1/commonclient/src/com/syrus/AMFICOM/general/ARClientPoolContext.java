/*-
 * $Id: ARClientPoolContext.java,v 1.1 2005/06/07 12:30:28 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.resource.CORBAResourceObjectLoader;
import com.syrus.AMFICOM.resource.ResourceStorableObjectPool;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/06/07 12:30:28 $
 * @module commonclient_v1
 */
final class ARClientPoolContext extends ClientPoolContext {
	private ARClientServantManager arClientServantManager;

	public ARClientPoolContext(final MClientServantManager mClientServantManager, final ARClientServantManager arClientServantManager) {
		super(mClientServantManager);
		this.arClientServantManager = arClientServantManager;
	}

	public void init() {
		super.init();
		ResourceStorableObjectPool.init(new CORBAResourceObjectLoader(this.arClientServantManager), StorableObjectResizableLRUMap.class);
	}
}
