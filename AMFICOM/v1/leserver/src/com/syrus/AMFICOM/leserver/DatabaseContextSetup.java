/*
 * $Id: DatabaseContextSetup.java,v 1.3 2005/04/28 15:00:30 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.leserver;

import com.syrus.AMFICOM.administration.AdministrationDatabaseContext;
import com.syrus.AMFICOM.administration.DomainDatabase;
import com.syrus.AMFICOM.administration.MCMDatabase;
import com.syrus.AMFICOM.administration.ServerDatabase;
import com.syrus.AMFICOM.administration.ServerProcessDatabase;
import com.syrus.AMFICOM.administration.UserDatabase;
import com.syrus.AMFICOM.event.EventDatabase;
import com.syrus.AMFICOM.event.EventDatabaseContext;
import com.syrus.AMFICOM.event.EventSourceDatabase;
import com.syrus.AMFICOM.event.EventTypeDatabase;
import com.syrus.AMFICOM.general.CharacteristicDatabase;
import com.syrus.AMFICOM.general.CharacteristicTypeDatabase;
import com.syrus.AMFICOM.general.GeneralDatabaseContext;
import com.syrus.AMFICOM.general.ParameterTypeDatabase;

/**
 * @version $Revision: 1.3 $, $Date: 2005/04/28 15:00:30 $
 * @author $Author: arseniy $
 * @module leserver_v1
 */
final class DatabaseContextSetup {

	private DatabaseContextSetup() {
		assert false; 
	}

	public static void initDatabaseContext() {
		GeneralDatabaseContext.init(new ParameterTypeDatabase(),
				new CharacteristicTypeDatabase(),
				new CharacteristicDatabase());

		AdministrationDatabaseContext.init(new UserDatabase(),
				new DomainDatabase(),
				new ServerDatabase(),
				new MCMDatabase(),
				new ServerProcessDatabase());

		EventDatabaseContext.init(new EventTypeDatabase(),
				 new EventDatabase(),
				 new EventSourceDatabase());
	}
	
}
