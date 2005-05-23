/*
 * $Id: MClientPoolContext.java,v 1.2 2005/05/23 08:40:15 bob Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.measurement.CORBAMeasurementObjectLoader;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;

/**
 * @version $Revision: 1.2 $, $Date: 2005/05/23 08:40:15 $
 * @author $Author: bob $
 * @module generalclient_v1
 */
final class MClientPoolContext extends ClientPoolContext {

	public MClientPoolContext(final CMServerConnectionManager cmServerConnectionManager) {
		super(cmServerConnectionManager);
	}

	public void init() {
		super.init();
		MeasurementStorableObjectPool.init(new CORBAMeasurementObjectLoader(super.cmServerConnectionManager),
				StorableObjectResizableLRUMap.class);
	}
}
