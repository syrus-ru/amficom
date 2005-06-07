/*
 * $Id: MSHClientPoolContext.java,v 1.3 2005/06/07 12:30:28 bass Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.map.CORBAMapObjectLoader;
import com.syrus.AMFICOM.map.MapStorableObjectPool;
import com.syrus.AMFICOM.mapview.CORBAMapViewObjectLoader;
import com.syrus.AMFICOM.mapview.MapViewStorableObjectPool;
import com.syrus.AMFICOM.resource.CORBAResourceObjectLoader;
import com.syrus.AMFICOM.resource.ResourceStorableObjectPool;
import com.syrus.AMFICOM.scheme.CORBASchemeObjectLoader;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;

/**
 * @version $Revision: 1.3 $, $Date: 2005/06/07 12:30:28 $
 * @author $Author: bass $
 * @module commonclient_v1
 */
final class MSHClientPoolContext extends ClientPoolContext {
	private ARClientServantManager arClientServantManager;

	private MSHClientServantManager mshClientServantManager;

	public MSHClientPoolContext(
			final MClientServantManager mClientServantManager,
			final ARClientServantManager arClientServantManager,
			final MSHClientServantManager mshClientServantManager) {
		super(mClientServantManager);
		this.arClientServantManager = arClientServantManager;
		this.mshClientServantManager = mshClientServantManager;
	}

	public void init() {
		super.init();
		ResourceStorableObjectPool.init(new CORBAResourceObjectLoader(this.arClientServantManager), StorableObjectResizableLRUMap.class);
		MapStorableObjectPool.init(new CORBAMapObjectLoader(this.mshClientServantManager), StorableObjectResizableLRUMap.class);
		SchemeStorableObjectPool.init(new CORBASchemeObjectLoader(this.mshClientServantManager), StorableObjectResizableLRUMap.class);
		MapViewStorableObjectPool.init(new CORBAMapViewObjectLoader(this.mshClientServantManager), StorableObjectResizableLRUMap.class);
	}
}
