/*
 * $Id: DatabaseContextSetup.java,v 1.5 2005/05/24 13:24:56 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.leserver;

import gnu.trove.TShortObjectHashMap;

import com.syrus.AMFICOM.administration.DomainDatabase;
import com.syrus.AMFICOM.administration.MCMDatabase;
import com.syrus.AMFICOM.administration.ServerDatabase;
import com.syrus.AMFICOM.administration.ServerProcessDatabase;
import com.syrus.AMFICOM.administration.UserDatabase;
import com.syrus.AMFICOM.event.EventDatabase;
import com.syrus.AMFICOM.event.EventSourceDatabase;
import com.syrus.AMFICOM.event.EventTypeDatabase;
import com.syrus.AMFICOM.general.CharacteristicDatabase;
import com.syrus.AMFICOM.general.CharacteristicTypeDatabase;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterTypeDatabase;

/**
 * @version $Revision: 1.5 $, $Date: 2005/05/24 13:24:56 $
 * @author $Author: bass $
 * @module leserver_v1
 */
final class DatabaseContextSetup {
	private DatabaseContextSetup() {
		assert false;
	}

	public static void initDatabaseContext() {
		final TShortObjectHashMap entityCodeDatabaseMap = new TShortObjectHashMap();
		entityCodeDatabaseMap.put(ObjectEntities.PARAMETERTYPE_ENTITY_CODE, new ParameterTypeDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE, new CharacteristicTypeDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.CHARACTERISTIC_ENTITY_CODE, new CharacteristicDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.USER_ENTITY_CODE, new UserDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.DOMAIN_ENTITY_CODE, new DomainDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.SERVER_ENTITY_CODE, new ServerDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.MCM_ENTITY_CODE, new MCMDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.SERVERPROCESS_ENTITY_CODE, new ServerProcessDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.EVENTTYPE_ENTITY_CODE, new EventTypeDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.EVENT_ENTITY_CODE, new EventDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.EVENTSOURCE_ENTITY_CODE, new EventSourceDatabase());
		DatabaseContext.init(entityCodeDatabaseMap);
	}	
}
