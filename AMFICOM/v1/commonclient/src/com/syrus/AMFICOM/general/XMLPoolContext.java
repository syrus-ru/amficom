/*
 * $Id: XMLPoolContext.java,v 1.12 2006/02/03 13:22:58 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.util.Map;

import com.syrus.util.LRUMap;
import com.syrus.util.LRUMapSaver;

/**
 * @version $Revision: 1.12 $, $Date: 2006/02/03 13:22:58 $
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
		StorableObjectPool.init(super.objectLoader);
		final long timeToLive = 120 * 60000000000L;
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.GENERAL_GROUP_CODE, 1000, timeToLive);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.ADMINISTRATION_GROUP_CODE, 1000, timeToLive);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.CONFIGURATION_GROUP_CODE, 1000, timeToLive);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.MEASUREMENT_GROUP_CODE, 1000, timeToLive);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.EVENT_GROUP_CODE, 1000, timeToLive);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.RESOURCE_GROUP_CODE, 1000, timeToLive);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.MAP_GROUP_CODE, 1000, timeToLive);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.SCHEME_GROUP_CODE, 1000, timeToLive);
		//StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.MAPVIEW_GROUP_CODE, 1000, timeToLive);
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
		protected Map<Identifier, StorableObject> getMap(final Object readObject) {
			// nothing
			return null;
		}

		@Override
		protected Object getObjectToWrite(final LRUMap<Identifier, StorableObject> lruMap) {
			// nothing
			return null;
		}
	}
}
