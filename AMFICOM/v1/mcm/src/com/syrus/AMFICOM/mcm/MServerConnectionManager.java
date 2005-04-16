/*
 * $Id: MServerConnectionManager.java,v 1.3 2005/04/16 21:17:19 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.mcm;

import java.util.Collections;

import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.IGSConnectionManager;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.RunnableVerifiedConnectionManager;
import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;
import com.syrus.AMFICOM.general.corba.Verifiable;
import com.syrus.AMFICOM.mserver.corba.MServer;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.3 $, $Date: 2005/04/16 21:17:19 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */
public class MServerConnectionManager extends RunnableVerifiedConnectionManager implements IGSConnectionManager {
	private String mServerServantName;

	public MServerConnectionManager(CORBAServer corbaServer, String mServerServantName, long mServerCheckTimeout) {
		super(corbaServer, Collections.singleton(mServerServantName), mServerCheckTimeout);

		this.mServerServantName = mServerServantName;

		assert mServerCheckTimeout >= 10 * 60 * 1000 : "Too low timeToSleep"; //not less then 10 min
	}

	public IdentifierGeneratorServer getVerifiedIGSReference() throws CommunicationException {
		Verifiable reference = null;
		try {
			reference = super.getVerifiableReference(this.mServerServantName);
		}
		catch (IllegalDataException ide) {
			// Never
			Log.errorException(ide);
		}
		return (IdentifierGeneratorServer) reference;
	}

	protected MServer getVerifiedMServerReference() throws CommunicationException {
		Verifiable reference = null;
		try {
			reference = super.getVerifiableReference(this.mServerServantName);
		}
		catch (IllegalDataException ide) {
			// Never
			Log.errorException(ide);
		}
		return (MServer) reference;
	}

	protected void onLoseConnection(String servantName) {
		Log.debugMessage("MServerConnectionManager.onLoseConnection | Connection with '" + servantName + "' lost", Log.DEBUGLEVEL08);
		//@todo Generate event "Connection with Measurement Server lost"
	}

	protected void onRestoreConnection(String servantName) {
		Log.debugMessage("MServerConnectionManager.onRestoreConnection | Connection with '" + servantName + "' restored",
				Log.DEBUGLEVEL08);
		//@todo Generate event "Connection with Measurement Server restored"
	}

}
