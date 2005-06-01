/*
 * $Id: MClientPoolContext.java,v 1.4 2005/06/01 16:55:11 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.measurement.CORBAMeasurementObjectLoader;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;

/**
 * @version $Revision: 1.4 $, $Date: 2005/06/01 16:55:11 $
 * @author $Author: arseniy $
 * @module generalclient_v1
 */
final class MClientPoolContext extends ClientPoolContext {

	public MClientPoolContext(final ServerConnectionManager serverConnectionManager) {
		super(serverConnectionManager);
	}

	public void init() {
		super.init();
		MeasurementStorableObjectPool.init(new CORBAMeasurementObjectLoader(super.serverConnectionManager),
				StorableObjectResizableLRUMap.class);
	}
}
