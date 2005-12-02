/*
 * $Id: XMLPoolContext.java,v 1.10 2005/12/02 15:21:00 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.util.LRUMap;
import com.syrus.util.LRUMapSaver;

/**
 * @version $Revision: 1.10 $, $Date: 2005/12/02 15:21:00 $
 * @author $Author: arseniy $
 * @module commonclient
 */
public final class XMLPoolContext extends PoolContext {
	private static XMLStubLRUMapSaver instance;

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
	public LRUMapSaver<Identifier, StorableObject> getLRUMapSaver() {
		if (instance == null) {
			synchronized (XMLStubLRUMapSaver.class) {
				if (instance == null) {
					instance = new XMLStubLRUMapSaver();
				}
			}
		}
		return null;
	}

	final XMLObjectLoader getObjectLoader() {
		return (XMLObjectLoader) super.objectLoader;
	}

	
	private static class XMLStubLRUMapSaver extends LRUMapSaver<Identifier, StorableObject> {

		XMLStubLRUMapSaver() {
			super("");
		}

		@Override
		protected void populateLRUMap(final LRUMap<Identifier, StorableObject> lruMap, final Object readObject) {
			// nothing
		}

		@Override
		protected Object getObjectToWrite(final LRUMap<Identifier, StorableObject> lruMap) {
			// nothing
			return null;
		}
	}
}
