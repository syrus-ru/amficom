/*-
 * $Id: MClientPoolContext.java,v 1.4 2005/05/13 19:08:27 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.measurement.CORBAMeasurementObjectLoader;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;

/**
 * @version $Revision: 1.4 $, $Date: 2005/05/13 19:08:27 $
 * @author $Author: bass $
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
