/*
 * $Id: MClientPoolContext.java,v 1.14 2005/08/02 13:03:22 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.14 $, $Date: 2005/08/02 13:03:22 $
 * @author $Author: arseniy $
 * @module commonclient
 */
final class MClientPoolContext extends ClientPoolContext {
	private static final String KEY_MEASUREMENT_POOL_SIZE = "MeasurementPoolSize";

	private static final int MEASUREMENT_POOL_SIZE = 1000;

	public MClientPoolContext(final ObjectLoader objectLoader) {
		super(objectLoader);
	}

	@Override
	public void init() {
		super.init();

		final int measurementPoolSize = ApplicationProperties.getInt(KEY_MEASUREMENT_POOL_SIZE, MEASUREMENT_POOL_SIZE);

		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.MEASUREMENT_GROUP_CODE, measurementPoolSize);
	}
}
