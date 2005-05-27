/*
 * $Id: DatabaseContextSetup.java,v 1.10 2005/05/27 11:13:49 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.arserver;

import com.syrus.AMFICOM.administration.DomainDatabase;
import com.syrus.AMFICOM.administration.ServerDatabase;
import com.syrus.AMFICOM.administration.ServerProcessDatabase;
import com.syrus.AMFICOM.administration.UserDatabase;
import com.syrus.AMFICOM.general.CharacteristicDatabase;
import com.syrus.AMFICOM.general.CharacteristicTypeDatabase;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.resource.ImageResourceDatabase;

/**
 * @version $Revision: 1.10 $, $Date: 2005/05/27 11:13:49 $
 * @author $Author: bass $
 * @module mserver_v1
 */

final class DatabaseContextSetup {
	private DatabaseContextSetup() {
		assert false;
	}

	public static void initDatabaseContext() {
		DatabaseContext.registerDatabase(new CharacteristicDatabase());
		DatabaseContext.registerDatabase(new CharacteristicTypeDatabase());

		DatabaseContext.registerDatabase(new UserDatabase());
		DatabaseContext.registerDatabase(new DomainDatabase());
		DatabaseContext.registerDatabase(new ServerDatabase());
		DatabaseContext.registerDatabase(new ServerProcessDatabase());
		
		DatabaseContext.registerDatabase(new ImageResourceDatabase());
	}
}
