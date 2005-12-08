/*-
 * $Id: PoolContext.java,v 1.11 2005/12/08 15:30:54 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import com.syrus.util.LRUMapSaver;

/**
 * @version $Revision: 1.11 $, $Date: 2005/12/08 15:30:54 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */
public abstract class PoolContext {
	protected static final String KEY_GENERAL_POOL_SIZE = "GeneralPoolSize";
	protected static final String KEY_ADMINISTRATION_POOL_SIZE = "AdministrationPoolSize";
	protected static final String KEY_CONFIGURATION_POOL_SIZE = "ConfigurationPoolSize";
	protected static final String KEY_MEASUREMENT_POOL_SIZE = "MeasurementPoolSize";
	protected static final String KEY_EVENT_POOL_SIZE = "EventPoolSize";
	protected static final String KEY_RESOURCE_POOL_SIZE = "ResourcePoolSize";
	protected static final String KEY_REPORT_POOL_SIZE = "ReportPoolSize";
	protected static final String KEY_MAP_POOL_SIZE = "MapPoolSize";
	protected static final String KEY_SCHEME_POOL_SIZE = "SchemePoolSize";
	protected static final String KEY_MAPVIEW_POOL_SIZE = "MapViewPoolSize";

	protected static final String KEY_GENERAL_POOL_TIME_TO_LIVE = "GeneralPoolTimeToLive";
	protected static final String KEY_ADMINISTRATION_POOL_TIME_TO_LIVE = "AdministrationPoolTimeToLive";
	protected static final String KEY_CONFIGURATION_POOL_TIME_TO_LIVE = "ConfigurationPoolTimeToLive";
	protected static final String KEY_MEASUREMENT_POOL_TIME_TO_LIVE = "MeasurementPoolTimeToLive";
	protected static final String KEY_EVENT_POOL_TIME_TO_LIVE = "EventPoolTimeToLive";
	protected static final String KEY_RESOURCE_POOL_TIME_TO_LIVE = "ResourcePoolTimeToLive";
	protected static final String KEY_REPORT_POOL_TIME_TO_LIVE = "ReportPoolTimeToLive";
	protected static final String KEY_MAP_POOL_TIME_TO_LIVE = "MapPoolTimeToLive";
	protected static final String KEY_SCHEME_POOL_TIME_TO_LIVE = "SchemePoolTimeToLive";
	protected static final String KEY_MAPVIEW_POOL_TIME_TO_LIVE = "MapViewPoolTimeToLive";

	protected static final int GENERAL_POOL_SIZE = 1000;
	protected static final int ADMINISTRATION_POOL_SIZE = 1000;
	protected static final int CONFIGURATION_POOL_SIZE = 1000;
	protected static final int MEASUREMENT_POOL_SIZE = 1000;
	protected static final int EVENT_POOL_SIZE = 1000;
	protected static final int RESOURCE_POOL_SIZE = 1000;
	protected static final int REPORT_POOL_SIZE = 1000;
	protected static final int MAP_POOL_SIZE = 1000;
	protected static final int SCHEME_POOL_SIZE = 1000;
	protected static final int MAPVIEW_POOL_SIZE = 1000;

	protected static final int GENERAL_POOL_TIME_TO_LIVE = 120;	//min
	protected static final int ADMINISTRATION_POOL_TIME_TO_LIVE = 120;	//min
	protected static final int CONFIGURATION_POOL_TIME_TO_LIVE = 120;	//min
	protected static final int MEASUREMENT_POOL_TIME_TO_LIVE = 120;	//min
	protected static final int EVENT_POOL_TIME_TO_LIVE = 120;	//min
	protected static final int RESOURCE_POOL_TIME_TO_LIVE = 120;	//min
	protected static final int REPORT_POOL_TIME_TO_LIVE = 120;	//min
	protected static final int MAP_POOL_TIME_TO_LIVE = 120;	//min
	protected static final int SCHEME_POOL_TIME_TO_LIVE = 120;	//min
	protected static final int MAP_VIEW_POOL_TIME_TO_LIVE = 120;	//min

	protected ObjectLoader objectLoader;

	protected PoolContext(final ObjectLoader objectLoader) {
		this.objectLoader = objectLoader;
	}

	public abstract void init();

	public abstract LRUMapSaver<Identifier, StorableObject> getLRUMapSaver();
}
