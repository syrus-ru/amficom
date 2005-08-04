/*
 * $Id: DatabaseContextSetup.java,v 1.8 2005/08/04 14:04:27 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.leserver;

import com.syrus.AMFICOM.administration.DomainDatabase;
import com.syrus.AMFICOM.administration.MCMDatabase;
import com.syrus.AMFICOM.administration.PermissionAttributesDatabase;
import com.syrus.AMFICOM.administration.ServerDatabase;
import com.syrus.AMFICOM.administration.ServerProcessDatabase;
import com.syrus.AMFICOM.administration.SystemUserDatabase;
import com.syrus.AMFICOM.event.EventDatabase;
import com.syrus.AMFICOM.event.EventSourceDatabase;
import com.syrus.AMFICOM.event.EventTypeDatabase;
import com.syrus.AMFICOM.general.CharacteristicDatabase;
import com.syrus.AMFICOM.general.CharacteristicTypeDatabase;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ParameterTypeDatabase;

/**
 * @version $Revision: 1.8 $, $Date: 2005/08/04 14:04:27 $
 * @author $Author: bob $
 * @module leserver_v1
 */
final class DatabaseContextSetup {
	private DatabaseContextSetup() {
		assert false;
	}

	public static void initDatabaseContext() {
		DatabaseContext.registerDatabase(new ParameterTypeDatabase());
		DatabaseContext.registerDatabase(new CharacteristicTypeDatabase());
		DatabaseContext.registerDatabase(new CharacteristicDatabase());
		DatabaseContext.registerDatabase(new SystemUserDatabase());
		DatabaseContext.registerDatabase(new PermissionAttributesDatabase());
		DatabaseContext.registerDatabase(new DomainDatabase());
		DatabaseContext.registerDatabase(new ServerDatabase());
		DatabaseContext.registerDatabase(new MCMDatabase());
		DatabaseContext.registerDatabase(new ServerProcessDatabase());
		DatabaseContext.registerDatabase(new EventTypeDatabase());
		DatabaseContext.registerDatabase(new EventDatabase());
		DatabaseContext.registerDatabase(new EventSourceDatabase());
	}	
}
