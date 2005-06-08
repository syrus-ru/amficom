/*
 * $Id: MClientPoolContext.java,v 1.7 2005/06/08 16:11:41 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.measurement.CORBAMeasurementObjectLoader;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;

/**
 * @version $Revision: 1.7 $, $Date: 2005/06/08 16:11:41 $
 * @author $Author: arseniy $
 * @module commonclient_v1
 */
final class MClientPoolContext extends ClientPoolContext {

	public MClientPoolContext(final ServerConnectionManager mClientServantManager) {
		super(mClientServantManager);
	}
//
//	public MClientPoolContext(final String xmlPath) {
//		super(xmlPath);
//	}

	public void init() {
		super.init();
		MeasurementStorableObjectPool.init(new CORBAMeasurementObjectLoader(this.clientServantManager),
				StorableObjectResizableLRUMap.class);
//		if (super.xmlFile == null) {
//			MeasurementStorableObjectPool.init(new CORBAMeasurementObjectLoader(this.clientServantManager),
//					StorableObjectResizableLRUMap.class);
//		} else {
//			MeasurementStorableObjectPool.init(new XMLMeasurementObjectLoader(super.xmlFile),
//				StorableObjectResizableLRUMap.class);
//		}
	}
}
