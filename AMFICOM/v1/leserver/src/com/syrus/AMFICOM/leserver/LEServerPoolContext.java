/*-
 * $Id: LEServerPoolContext.java,v 1.7 2005/08/08 11:42:21 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.ObjectGroupEntities;
import com.syrus.AMFICOM.general.ObjectLoader;
import com.syrus.AMFICOM.general.PoolContext;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectResizableLRUMap;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.7 $, $Date: 2005/08/08 11:42:21 $
 * @author $Author: arseniy $
 * @module leserver
 */
final class LEServerPoolContext extends PoolContext {
	private static final String KEY_GENERAL_POOL_SIZE = "GeneralPoolSize";
	private static final String KEY_ADMINISTRATION_POOL_SIZE = "AdministrationPoolSize";
	private static final String KEY_EVENT_POOL_SIZE = "EventPoolSize";

	private static final int GENERAL_POOL_SIZE = 1000;
	private static final int ADMINISTRATION_POOL_SIZE = 1000;
	private static final int EVENT_POOL_SIZE = 1000;

	@Override
	public void init() {
		final ObjectLoader objectLoader = new DatabaseObjectLoader();

		final Class lruMapClass = StorableObjectResizableLRUMap.class;

		final int generalPoolSize = ApplicationProperties.getInt(KEY_GENERAL_POOL_SIZE, GENERAL_POOL_SIZE);
		final int administrationPoolSize = ApplicationProperties.getInt(KEY_ADMINISTRATION_POOL_SIZE, ADMINISTRATION_POOL_SIZE);
		final int eventPoolSize = ApplicationProperties.getInt(KEY_EVENT_POOL_SIZE, EVENT_POOL_SIZE);

		StorableObjectPool.init(objectLoader, lruMapClass);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.GENERAL_GROUP_CODE, generalPoolSize);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.ADMINISTRATION_GROUP_CODE, administrationPoolSize);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.EVENT_GROUP_CODE, eventPoolSize);
	}

}
