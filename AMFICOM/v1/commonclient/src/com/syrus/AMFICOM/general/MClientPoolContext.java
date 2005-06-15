/*
 * $Id: MClientPoolContext.java,v 1.9 2005/06/15 14:21:10 bob Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.measurement.CORBAMeasurementObjectLoader;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.XMLMeasurementObjectLoader;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.9 $, $Date: 2005/06/15 14:21:10 $
 * @author $Author: bob $
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
		int measurementPoolSize = ApplicationProperties.getInt(KEY_MEASUREMENT_POOL_SIZE, -1);
		if (super.xmlFile == null) {
			MeasurementStorableObjectPool.init(new CORBAMeasurementObjectLoader(this.clientServantManager),
					StorableObjectResizableLRUMap.class, measurementPoolSize);
		} else {
			MeasurementStorableObjectPool.init(new XMLMeasurementObjectLoader(super.xmlFile),
				StorableObjectResizableLRUMap.class, measurementPoolSize);
		}
	}
}
