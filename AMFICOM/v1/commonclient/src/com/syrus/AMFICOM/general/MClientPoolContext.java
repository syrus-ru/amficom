/*
 * $Id: MClientPoolContext.java,v 1.10 2005/06/15 15:52:22 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.measurement.CORBAMeasurementObjectLoader;
import com.syrus.AMFICOM.measurement.MeasurementObjectLoader;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.XMLMeasurementObjectLoader;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.10 $, $Date: 2005/06/15 15:52:22 $
 * @author $Author: arseniy $
 * @module commonclient_v1
 */
final class MClientPoolContext extends ClientPoolContext {

	public static final String KEY_MEASUREMENT_POOL_SIZE = "MeasurementPoolSize";

	public MClientPoolContext(final ServerConnectionManager mClientServantManager) {
		super(mClientServantManager);
	}

	public MClientPoolContext(final String xmlPath) {
		super(xmlPath);
	}

	public void init() {
		super.init();

		MeasurementObjectLoader measurementObjectLoader;
		if (super.xmlFile == null) {
			measurementObjectLoader = new CORBAMeasurementObjectLoader(this.clientServantManager);
		}
		else {
			measurementObjectLoader = new XMLMeasurementObjectLoader(super.xmlFile);
		}

		final Class lruMapClass = StorableObjectResizableLRUMap.class;

		int measurementPoolSize = ApplicationProperties.getInt(KEY_MEASUREMENT_POOL_SIZE, -1);

		MeasurementStorableObjectPool.init(measurementObjectLoader, lruMapClass, measurementPoolSize);
	}
}
