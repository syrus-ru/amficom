/*
 * $Id: MClientPoolContext.java,v 1.6 2005/06/07 13:28:24 bob Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.measurement.CORBAMeasurementObjectLoader;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.XMLMeasurementObjectLoader;

/**
 * @version $Revision: 1.6 $, $Date: 2005/06/07 13:28:24 $
 * @author $Author: bob $
 * @module commonclient_v1
 */
final class MClientPoolContext extends ClientPoolContext {

	public MClientPoolContext(final ServerConnectionManager mClientServantManager) {
		super(mClientServantManager);
	}
	
	public MClientPoolContext(final String xmlPath) {
		super(xmlPath);
	}

	public void init() {
		super.init();
		if (super.xmlFile == null) {
			MeasurementStorableObjectPool.init(new CORBAMeasurementObjectLoader(this.clientServantManager),
					StorableObjectResizableLRUMap.class);
		} else {
			MeasurementStorableObjectPool.init(new XMLMeasurementObjectLoader(super.xmlFile),
				StorableObjectResizableLRUMap.class);
		}
	}
}
