/*
 * $Id: MServerConnectionManager.java,v 1.2 2005/04/15 22:15:09 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.IGServerReferenceSource;
import com.syrus.AMFICOM.general.VerifiedReferenceSource;
import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;
import com.syrus.AMFICOM.general.corba.Verifiable;
import com.syrus.AMFICOM.mserver.corba.MServer;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.2 $, $Date: 2005/04/15 22:15:09 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */
public class MServerConnectionManager extends VerifiedReferenceSource implements Runnable, IGServerReferenceSource {
	private long mServerCheckTimeout;

	private boolean connectionLost;

	public MServerConnectionManager(CORBAServer corbaServer, String mServerServantName, long mServerCheckTimeout) {
		super(corbaServer, mServerServantName);
		assert mServerCheckTimeout >= 10 * 60 * 1000 : "Too low timeToSleep"; //not less then 10 min

		this.mServerCheckTimeout = mServerCheckTimeout;

		this.connectionLost = false;
	}

	public void run() {
		while (true) {

			try {
				Verifiable reference = super.getVerifiedReference();
				Log.debugMessage("MServerConnectionManager.run | Verified reference to Measurement Server", Log.DEBUGLEVEL08);
				Log.debugMessage(reference.toString(), Log.DEBUGLEVEL08);
				this.checkIfConnectionRestored();
			}
			catch (CommunicationException ce) {
				this.connectionLost = true;
				//@todo Generate event "Cannot resolve Measurement Server"
			}

			try {
				Thread.sleep(this.mServerCheckTimeout);
			}
			catch (InterruptedException ie) {
				Log.errorException(ie);
			}

		}
	}

	public IdentifierGeneratorServer getVerifiedIGServerReference() throws CommunicationException {
		try {
			Verifiable reference = super.getVerifiedReference();
			this.checkIfConnectionRestored();
			return (IdentifierGeneratorServer) reference;
		}
		catch (CommunicationException ce) {
			this.connectionLost = true;
			//@todo Generate event "Cannot resolve Measurement Server"
			throw ce;
		}
	}

	protected MServer getVerifiedMServerReference() throws CommunicationException {
		try {
			Verifiable reference = super.getVerifiedReference();
			this.checkIfConnectionRestored();
			return (MServer) reference;
		}
		catch (CommunicationException ce) {
			this.connectionLost = true;
			//@todo Generate event "Cannot resolve Measurement Server"
			throw ce;
		}
	}

	private void checkIfConnectionRestored() {
		if (this.connectionLost) {
			this.connectionLost = false;
			//@todo Generate event "Connection with Measurement Server restored"
		}
	}

}
