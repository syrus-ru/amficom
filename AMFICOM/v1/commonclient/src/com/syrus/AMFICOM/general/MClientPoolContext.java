/*
 * $Id: MClientPoolContext.java,v 1.12 2005/07/28 19:45:42 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.12 $, $Date: 2005/07/28 19:45:42 $
 * @author $Author: arseniy $
 * @module commonclient_v1
 */
final class MClientPoolContext extends ClientPoolContext {
	private static final String KEY_MEASUREMENT_POOL_SIZE = "MeasurementPoolSize";

	private static final int MEASUREMENT_POOL_SIZE = 1000;

	public MClientPoolContext(final ServerConnectionManager mClientServantManager) {
		super(mClientServantManager);
	}

	public MClientPoolContext(final String xmlPath) {
		super(xmlPath);
	}

	@Override
	public void init() {
		super.init();

		final int measurementPoolSize = ApplicationProperties.getInt(KEY_MEASUREMENT_POOL_SIZE, MEASUREMENT_POOL_SIZE);

		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.MEASUREMENT_GROUP_CODE, measurementPoolSize);
	}
}
