/*
 * $Id: XMLPoolContext.java,v 1.5 2005/08/02 13:03:22 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.io.File;

import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.5 $, $Date: 2005/08/02 13:03:22 $
 * @author $Author: arseniy $
 * @module commonclient
 */
public final class XMLPoolContext extends PoolContext {
	private static final String KEY_CACHE_PATH = "CachePath";
	private static final String CACHE_PATH = "cache";

	@Override
	public void init() {
		final File cachePath = new File(ApplicationProperties.getString(KEY_CACHE_PATH, CACHE_PATH));
		final Class cacheClass = StorableObjectResizableLRUMap.class;

		final ObjectLoader objectLoader = new XMLObjectLoader(cachePath);

		StorableObjectPool.init(objectLoader, cacheClass);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.GENERAL_GROUP_CODE, 1000);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.ADMINISTRATION_GROUP_CODE, 1000);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.CONFIGURATION_GROUP_CODE, 1000);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.MEASUREMENT_GROUP_CODE, 1000);
		//StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.EVENT_GROUP_CODE, 1000);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.RESOURCE_GROUP_CODE, 1000);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.MAP_GROUP_CODE, 1000);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.SCHEME_GROUP_CODE, 1000);
		//StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.MAPVIEW_GROUP_CODE, 1000);
	}
}
