/*-
 * $Id: MscharServerPoolContext.java,v 1.10 2005/12/02 15:22:04 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mscharserver;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierLRUMapSaver;
import com.syrus.AMFICOM.general.ObjectGroupEntities;
import com.syrus.AMFICOM.general.ObjectLoader;
import com.syrus.AMFICOM.general.PoolContext;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectResizableLRUMap;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.LRUMapSaver;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.10 $, $Date: 2005/12/02 15:22:04 $
 * @module mscharserver
 */
final class MscharServerPoolContext extends PoolContext {
	private static final String KEY_GENERAL_POOL_SIZE = "GeneralPoolSize"; 
	private static final String KEY_ADMINISTRATION_POOL_SIZE = "AdministrationPoolSize";
	private static final String KEY_RESOURCE_POOL_SIZE = "ResourcePoolSize";
	private static final String KEY_MAP_POOL_SIZE = "MapPoolSize";
	private static final String KEY_SCHEME_POOL_SIZE = "SchemePoolSize";
	private static final String KEY_MAP_VIEW_POOL_SIZE = "MapViewPoolSize";

	private static final int GENERAL_POOL_SIZE = 1000;
	private static final int ADMINISTRATION_POOL_SIZE = 1000;
	private static final int RESOURCE_POOL_SIZE = 1000;
	private static final int MAP_POOL_SIZE = 1000;
	private static final int SCHEME_POOL_SIZE = 1000;
	private static final int MAP_VIEW_POOL_SIZE = 1000;

	private static LRUMapSaver<Identifier, StorableObject> LRU_MAP_SAVER;

	public MscharServerPoolContext(final ObjectLoader objectLoader) {
		super(objectLoader);
	}

	@Override
	public void init() {
		final Class lruMapClass = StorableObjectResizableLRUMap.class;

		final int generalPoolSize = ApplicationProperties.getInt(KEY_GENERAL_POOL_SIZE, GENERAL_POOL_SIZE);
		final int administrationPoolSize = ApplicationProperties.getInt(KEY_ADMINISTRATION_POOL_SIZE, ADMINISTRATION_POOL_SIZE);
		final int resourcePoolSize = ApplicationProperties.getInt(KEY_RESOURCE_POOL_SIZE, RESOURCE_POOL_SIZE);
		final int mapPoolSize = ApplicationProperties.getInt(KEY_MAP_POOL_SIZE, MAP_POOL_SIZE);
		final int schemePoolSize = ApplicationProperties.getInt(KEY_SCHEME_POOL_SIZE, SCHEME_POOL_SIZE);
		final int mapViewPoolSize = ApplicationProperties.getInt(KEY_MAP_VIEW_POOL_SIZE, MAP_VIEW_POOL_SIZE);

		StorableObjectPool.init(super.objectLoader, lruMapClass);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.GENERAL_GROUP_CODE, generalPoolSize);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.ADMINISTRATION_GROUP_CODE, administrationPoolSize);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.RESOURCE_GROUP_CODE, resourcePoolSize);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.MAP_GROUP_CODE, mapPoolSize);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.SCHEME_GROUP_CODE, schemePoolSize);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.MAPVIEW_GROUP_CODE, mapViewPoolSize);

		if (LRU_MAP_SAVER == null) {
			LRU_MAP_SAVER = new IdentifierLRUMapSaver(super.objectLoader);
		}
	}

	@Override
	public LRUMapSaver<Identifier, StorableObject> getLRUMapSaver() {
		return LRU_MAP_SAVER;
	}
}
