/*
 * $Id: MClientPoolContext.java,v 1.1 2005/05/04 14:43:31 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.measurement.CORBAMeasurementObjectLoader;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;

/**
 * @version $Revision: 1.1 $, $Date: 2005/05/04 14:43:31 $
 * @author $Author: arseniy $
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

	public void deserialize() {
		super.deserialize();
		MeasurementStorableObjectPool.deserializePool();
	}

	public void serialize() {
		super.serialize();
		MeasurementStorableObjectPool.serializePool();
	}
}
