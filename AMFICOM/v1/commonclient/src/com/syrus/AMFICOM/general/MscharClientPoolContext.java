/*
 * $Id: MscharClientPoolContext.java,v 1.2 2005/06/08 16:11:41 arseniy Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/06/08 16:11:41 $
 * @author $Author: arseniy $
 * @module commonclient_v1
 */
final class MscharClientPoolContext extends ClientPoolContext {
	private MscharClientServantManager	mscharClientServantManager;

	public MscharClientPoolContext(
			final MClientServantManager mClientServantManager,
			final MscharClientServantManager mscharClientServantManager) {
		super(mClientServantManager);
		this.mscharClientServantManager = mscharClientServantManager;
	}
//
//	public MscharClientPoolContext(final String xmlPath) {
//		super(xmlPath);
//	}

	public void init() {
		super.init();
		ResourceStorableObjectPool.init(
				new CORBAResourceObjectLoader(this.mscharClientServantManager),
				StorableObjectResizableLRUMap.class);
		MapStorableObjectPool.init(
				new CORBAMapObjectLoader(this.mscharClientServantManager),
				StorableObjectResizableLRUMap.class);
		SchemeStorableObjectPool.init(
				new CORBASchemeObjectLoader(this.mscharClientServantManager),
				StorableObjectResizableLRUMap.class);
		MapViewStorableObjectPool.init(
				new CORBAMapViewObjectLoader(this.mscharClientServantManager),
				StorableObjectResizableLRUMap.class);
//		if (this.xmlFile != null) {
//			ResourceStorableObjectPool.init(
//					new XMLResourceObjectLoader(this.xmlFile),
//					StorableObjectResizableLRUMap.class);
//			MapStorableObjectPool.init(
//					new XMLMapObjectLoader(this.xmlFile),
//					StorableObjectResizableLRUMap.class);
//			SchemeStorableObjectPool.init(
//					new XMLSchemeObjectLoader(this.xmlFile),
//					StorableObjectResizableLRUMap.class);
//			MapViewStorableObjectPool.init(
//					new XMLMapViewObjectLoader(this.xmlFile),
//					StorableObjectResizableLRUMap.class);
//		} else {
//			ResourceStorableObjectPool.init(
//					new CORBAResourceObjectLoader(this.mscharClientServantManager),
//					StorableObjectResizableLRUMap.class);
//			MapStorableObjectPool.init(
//					new CORBAMapObjectLoader(this.mscharClientServantManager),
//					StorableObjectResizableLRUMap.class);
//			SchemeStorableObjectPool.init(
//					new CORBASchemeObjectLoader(this.mscharClientServantManager),
//					StorableObjectResizableLRUMap.class);
//			MapViewStorableObjectPool.init(
//					new CORBAMapViewObjectLoader(this.mscharClientServantManager),
//					StorableObjectResizableLRUMap.class);
//		}
	}
}
