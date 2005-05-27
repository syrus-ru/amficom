/*
 * $Id: MClientPoolContext.java,v 1.3 2005/05/27 16:24:46 bass Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.measurement.CORBAMeasurementObjectLoader;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;

/**
 * @version $Revision: 1.3 $, $Date: 2005/05/27 16:24:46 $
 * @author $Author: bass $
 * @module generalclient_v1
 */
final class MClientPoolContext extends ClientPoolContext {

	public MClientPoolContext(final ServerConnectionManager cmServerConnectionManager) {
		super(cmServerConnectionManager);
	}

	public void init() {
		super.init();
		MeasurementStorableObjectPool.init(new CORBAMeasurementObjectLoader(super.cmServerConnectionManager),
				StorableObjectResizableLRUMap.class);
	}
}
