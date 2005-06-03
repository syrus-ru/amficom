/*
 * $Id: MClientPoolContext.java,v 1.5 2005/06/03 09:03:42 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.measurement.CORBAMeasurementObjectLoader;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;

/**
 * @version $Revision: 1.5 $, $Date: 2005/06/03 09:03:42 $
 * @author $Author: arseniy $
 * @module commonclient_v1
 */
final class MClientPoolContext extends ClientPoolContext {

	public MClientPoolContext(final MClientServantManager mClientServantManager) {
		super(mClientServantManager);
	}

	public void init() {
		super.init();
		MeasurementStorableObjectPool.init(new CORBAMeasurementObjectLoader(this.clientServantManager),
				StorableObjectResizableLRUMap.class);
	}
}
