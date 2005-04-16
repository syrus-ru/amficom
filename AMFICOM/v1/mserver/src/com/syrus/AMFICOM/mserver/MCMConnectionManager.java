/*
 * $Id: MCMConnectionManager.java,v 1.6 2005/04/16 21:18:29 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.mserver;

import java.util.Set;

import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.RunnableVerifiedConnectionManager;
import com.syrus.AMFICOM.general.corba.Verifiable;
import com.syrus.AMFICOM.mcm.corba.MCM;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.6 $, $Date: 2005/04/16 21:18:29 $
 * @author $Author: arseniy $
 * @module cmserver_v1
 */

public class MCMConnectionManager extends RunnableVerifiedConnectionManager {

	public MCMConnectionManager(CORBAServer corbaServer, Set mcmIdStrings, long mcmCheckTimeout) {
		super(corbaServer, mcmIdStrings, mcmCheckTimeout);

		assert mcmCheckTimeout >= 10 * 60 * 1000 : "Too low timeToSleep"; //not less then 10 min
	}

	public MCM getVerifiedMCMReference(Identifier mcmId) throws CommunicationException, IllegalDataException {
		Verifiable reference = super.getVerifiableReference(mcmId.toString());
		return (MCM) reference;
	}

	protected void onLoseConnection(String servantName) {
		Log.debugMessage("MCMConnectionManager.onLoseConnection | Connection with '" + servantName + "' lost", Log.DEBUGLEVEL08);
		//@todo Generate event "Connection with MCM servantName lost"
	}

	protected void onRestoreConnection(String servantName) {
		Log.debugMessage("MCMConnectionManager.onRestoreConnection | Connection with '" + servantName + "' restored",
				Log.DEBUGLEVEL08);
		//@todo Generate event "Connection with MCM servantName restored"
	}
}

