/*
 * $Id: XMLPoolContext.java,v 1.9 2005/11/10 13:24:27 bob Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.util.Collections;
import java.util.Set;

import com.syrus.io.LRUSaver;
import com.syrus.util.LRUMap;

/**
 * @version $Revision: 1.9 $, $Date: 2005/11/10 13:24:27 $
 * @author $Author: bob $
 * @module commonclient
 */
public final class XMLPoolContext extends PoolContext {
	private static XMLStubLRUSaver instance;

	public XMLPoolContext(final XMLObjectLoader objectLoader) {
		super(objectLoader);
	}

	@Override
	public void init() {
		final Class cacheClass = StorableObjectResizableLRUMap.class;

		StorableObjectPool.init(super.objectLoader, cacheClass);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.GENERAL_GROUP_CODE, 1000);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.ADMINISTRATION_GROUP_CODE, 1000);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.CONFIGURATION_GROUP_CODE, 1000);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.MEASUREMENT_GROUP_CODE, 1000);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.EVENT_GROUP_CODE, 1000);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.RESOURCE_GROUP_CODE, 1000);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.MAP_GROUP_CODE, 1000);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.SCHEME_GROUP_CODE, 1000);
		//StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.MAPVIEW_GROUP_CODE, 1000);
	}

	@Override
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
		return (XMLObjectLoader) super.objectLoader;
	}

	private class XMLStubLRUSaver implements LRUSaver<Identifier, StorableObject> {

		public Set<StorableObject> load(String objectEntityName) {
			return Collections.emptySet();
		}

		public void save(LRUMap<Identifier, StorableObject> lruMap, String objectEntityName, boolean cleanLRUMap) {
			// nothing yopta
		}
	}
}
