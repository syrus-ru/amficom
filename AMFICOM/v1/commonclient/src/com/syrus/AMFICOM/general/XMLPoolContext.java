/*
 * $Id: XMLPoolContext.java,v 1.6 2005/08/12 10:09:32 bob Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.io.File;

import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.6 $, $Date: 2005/08/12 10:09:32 $
 * @author $Author: bob $
 * @module commonclient
 */
public final class XMLPoolContext extends PoolContext {
	private static final String KEY_CACHE_PATH = "CachePath";
	private static final String CACHE_PATH = "cache";

	private XMLObjectLoader objectLoader;
	
	@Override
	public void init() {
		final File cachePath = new File(ApplicationProperties.getString(KEY_CACHE_PATH, CACHE_PATH));
		final Class cacheClass = StorableObjectResizableLRUMap.class;

		this.objectLoader = new XMLObjectLoader(cachePath);

		StorableObjectPool.init(this.objectLoader, cacheClass);
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

	
	final XMLObjectLoader getObjectLoader() {
		return this.objectLoader;
	}
}
