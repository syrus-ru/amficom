/*
 * $Id: MSHClientPoolContext.java,v 1.2 2005/06/03 09:36:16 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.map.CORBAMapObjectLoader;
import com.syrus.AMFICOM.map.MapStorableObjectPool;
import com.syrus.AMFICOM.scheme.CORBASchemeObjectLoader;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;

/**
 * @version $Revision: 1.2 $, $Date: 2005/06/03 09:36:16 $
 * @author $Author: arseniy $
 * @module commonclient_v1
 */
final class MSHClientPoolContext extends ClientPoolContext {
	private MSHClientServantManager mshClientServantManager;

	public MSHClientPoolContext(final MClientServantManager mClientServantManager, final MSHClientServantManager mshClientServantManager) {
		super(mClientServantManager);
		this.mshClientServantManager = mshClientServantManager;
	}

	public void init() {
		super.init();
		MapStorableObjectPool.init(new CORBAMapObjectLoader(this.mshClientServantManager), StorableObjectResizableLRUMap.class);
		SchemeStorableObjectPool.init(new CORBASchemeObjectLoader(this.mshClientServantManager), StorableObjectResizableLRUMap.class);
	}

}
