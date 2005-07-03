/*-
 * $Id: LEServerPoolContext.java,v 1.4 2005/06/15 15:52:48 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import com.syrus.AMFICOM.administration.AdministrationObjectLoader;
import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.DatabaseAdministrationObjectLoader;
import com.syrus.AMFICOM.event.DatabaseEventObjectLoader;
import com.syrus.AMFICOM.event.EventObjectLoader;
import com.syrus.AMFICOM.event.EventStorableObjectPool;
import com.syrus.AMFICOM.general.DatabaseGeneralObjectLoader;
import com.syrus.AMFICOM.general.GeneralObjectLoader;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.PoolContext;
import com.syrus.AMFICOM.general.StorableObjectResizableLRUMap;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.4 $, $Date: 2005/06/15 15:52:48 $
 * @author $Author: arseniy $
 * @module leserver_v1
 */
final class LEServerPoolContext extends PoolContext {
	public static final String KEY_GENERAL_POOL_SIZE = "GeneralPoolSize";
	public static final String KEY_ADMINISTRATION_POOL_SIZE = "AdministrationPoolSize";
	public static final String KEY_EVENT_POOL_SIZE = "EventPoolSize";

	public static final int GENERAL_POOL_SIZE = 1000;
	public static final int ADMINISTRATION_POOL_SIZE = 1000;
	public static final int EVENT_POOL_SIZE = 1000;

	public void init() {
		final GeneralObjectLoader generalObjectLoader = new DatabaseGeneralObjectLoader();
		final AdministrationObjectLoader administrationObjectLoader = new DatabaseAdministrationObjectLoader();
		final EventObjectLoader eventObjectLoader = new DatabaseEventObjectLoader();

		final Class lruMapClass = StorableObjectResizableLRUMap.class;

		final int generalPoolSize = ApplicationProperties.getInt(KEY_GENERAL_POOL_SIZE, GENERAL_POOL_SIZE);
		final int administrationPoolSize = ApplicationProperties.getInt(KEY_ADMINISTRATION_POOL_SIZE, ADMINISTRATION_POOL_SIZE);
		final int eventPoolSize = ApplicationProperties.getInt(KEY_EVENT_POOL_SIZE, EVENT_POOL_SIZE);

		GeneralStorableObjectPool.init(generalObjectLoader, lruMapClass, generalPoolSize);
		AdministrationStorableObjectPool.init(administrationObjectLoader, lruMapClass, administrationPoolSize);
		EventStorableObjectPool.init(eventObjectLoader, lruMapClass, eventPoolSize);
	}

}
