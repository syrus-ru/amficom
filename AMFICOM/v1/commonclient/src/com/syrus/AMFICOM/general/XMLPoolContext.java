/*
 * $Id: XMLPoolContext.java,v 1.7 2005/09/07 13:05:39 bob Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.io.File;
import java.util.Collections;
import java.util.Set;

import com.syrus.io.LRUSaver;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.LRUMap;

/**
 * @version $Revision: 1.7 $, $Date: 2005/09/07 13:05:39 $
 * @author $Author: bob $
 * @module commonclient
 */
public final class XMLPoolContext implements PoolContext {
	private static final String KEY_CACHE_PATH = "CachePath";
	private static final String CACHE_PATH = "cache";

	private XMLObjectLoader objectLoader;
	
	private static XMLStubLRUSaver instance;
	
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

	public LRUSaver<Identifier, StorableObject> getLRUSaver() {
		if (instance == null) {
			synchronized (XMLStubLRUSaver.class) {
				if (instance == null) {
					instance = new XMLStubLRUSaver();
				}
			}
		}
		return instance;
	}
	
	final XMLObjectLoader getObjectLoader() {
		return this.objectLoader;
	}
	
	private class XMLStubLRUSaver implements LRUSaver<Identifier, StorableObject> {

		public Set<StorableObject> load(String objectEntityName) {
			return Collections.emptySet();
		}
		
		public void save(	LRUMap<Identifier, StorableObject> lruMap,
							String objectEntityName,
							boolean cleanLRUMap) {
			// nothing yopta			
		}
	}
}
