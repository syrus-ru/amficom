/*
 * $Id: PoolContext.java,v 1.1.1.1 2005/04/28 07:35:33 cvsadmin Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.loginserver;

import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.DatabaseAdministrationObjectLoader;
import com.syrus.AMFICOM.event.DatabaseEventObjectLoader;
import com.syrus.AMFICOM.event.EventStorableObjectPool;
import com.syrus.AMFICOM.general.DatabaseGeneralObjectLoader;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectResizableLRUMap;

/**
 * @version $Revision: 1.1.1.1 $, $Date: 2005/04/28 07:35:33 $
 * @author $Author: cvsadmin $
 * @module loginserver_v1
 */
final class PoolContext {

	public PoolContext() {
		// singleton
		assert false;
	}

	public static void init() {
		GeneralStorableObjectPool.init(new DatabaseGeneralObjectLoader(), StorableObjectResizableLRUMap.class);
		AdministrationStorableObjectPool.init(new DatabaseAdministrationObjectLoader(), StorableObjectResizableLRUMap.class);
		EventStorableObjectPool.init(new DatabaseEventObjectLoader(), StorableObjectResizableLRUMap.class);
	}

	public static void deserialize() {
		GeneralStorableObjectPool.deserializePool();
		AdministrationStorableObjectPool.deserializePool();
		EventStorableObjectPool.deserializePool();
	}

	public static void serialize() {
		GeneralStorableObjectPool.serializePool();
		AdministrationStorableObjectPool.serializePool();
		EventStorableObjectPool.serializePool();
	}
}
