/*
 * $Id: MSHClientPoolContext.java,v 1.4 2005/06/07 13:28:24 bob Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.map.CORBAMapObjectLoader;
import com.syrus.AMFICOM.map.MapStorableObjectPool;
import com.syrus.AMFICOM.map.XMLMapObjectLoader;
import com.syrus.AMFICOM.mapview.MapViewStorableObjectPool;
import com.syrus.AMFICOM.mapview.XMLMapViewObjectLoader;
import com.syrus.AMFICOM.resource.CORBAResourceObjectLoader;
import com.syrus.AMFICOM.resource.ResourceStorableObjectPool;
import com.syrus.AMFICOM.resource.XMLResourceObjectLoader;
import com.syrus.AMFICOM.scheme.CORBASchemeObjectLoader;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.AMFICOM.scheme.XMLSchemeObjectLoader;

/**
 * @version $Revision: 1.4 $, $Date: 2005/06/07 13:28:24 $
 * @author $Author: bob $
 * @module commonclient_v1
 */
final class MSHClientPoolContext extends ClientPoolContext {

	private ARClientServantManager	arClientServantManager;

	private MSHClientServantManager	mshClientServantManager;

	public MSHClientPoolContext(final MClientServantManager mClientServantManager,
			final ARClientServantManager arClientServantManager,
			final MSHClientServantManager mshClientServantManager) {
		super(mClientServantManager);
		this.arClientServantManager = arClientServantManager;
		this.mshClientServantManager = mshClientServantManager;
	}

	public MSHClientPoolContext(final String xmlPath) {
		super(xmlPath);
	}

	public void init() {
		super.init();
		if (this.xmlFile != null) {
			ResourceStorableObjectPool.init(new XMLResourceObjectLoader(this.xmlFile),
				StorableObjectResizableLRUMap.class);

			MapStorableObjectPool.init(new XMLMapObjectLoader(this.xmlFile), StorableObjectResizableLRUMap.class);
			SchemeStorableObjectPool.init(new XMLSchemeObjectLoader(this.xmlFile), StorableObjectResizableLRUMap.class);

			MapViewStorableObjectPool.init(new XMLMapViewObjectLoader(this.xmlFile),
				StorableObjectResizableLRUMap.class);

		} else {
			ResourceStorableObjectPool.init(new CORBAResourceObjectLoader(this.arClientServantManager),
				StorableObjectResizableLRUMap.class);
			MapStorableObjectPool.init(new CORBAMapObjectLoader(this.mshClientServantManager),
				StorableObjectResizableLRUMap.class);
			SchemeStorableObjectPool.init(new CORBASchemeObjectLoader(this.mshClientServantManager),
				StorableObjectResizableLRUMap.class);
		}
	}
}
