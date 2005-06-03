/*
 * $Id: MSHClientPoolContext.java,v 1.1 2005/06/03 09:00:40 arseniy Exp $
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
 * @version $Revision: 1.1 $, $Date: 2005/06/03 09:00:40 $
 * @author $Author: arseniy $
 * @module commonclient_v1
 */
final class MSHClientPoolContext extends ClientPoolContext {

	public MSHClientPoolContext(final MSHClientServantManager mshClientServantManager) {
		super(mshClientServantManager);
	}

	public void init() {
		super.init();
		MapStorableObjectPool.init(new CORBAMapObjectLoader(this.clientServantManager), StorableObjectResizableLRUMap.class);
		SchemeStorableObjectPool.init(new CORBASchemeObjectLoader(this.clientServantManager), StorableObjectResizableLRUMap.class);
	}

}
