/*
 * $Id: MServerConnectionManager.java,v 1.1 2005/04/01 21:51:15 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.mcm;

import org.omg.CORBA.SystemException;

import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.mserver.corba.MServer;
import com.syrus.AMFICOM.mserver.corba.MServerHelper;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2005/04/01 21:51:15 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */
public class MServerConnectionManager extends Thread {
	public static final String KEY_MSERVER_SERVANT_NAME = "MServerServantName";
	public static final String MSERVER_SERVANT_NAME = "MServer";

	private CORBAServer corbaServer;
	private String mServerServantName;
	private long mServerCheckTimeout;

	private MServer mServerRef;
	private Object lock;

	public MServerConnectionManager(CORBAServer corbaServer, String mServerServantName, long mServerCheckTimeout) {
		assert corbaServer != null : "corbaServer is NULL";
		assert mServerServantName != null : "Measurement Server servant name is NULL";
		assert mServerCheckTimeout >= 10 * 60 * 1000 : "Too low timeToSleep"; //not less then 10 min

		this.corbaServer = corbaServer;
		this.mServerServantName = mServerServantName;
		this.mServerCheckTimeout = mServerCheckTimeout;

		this.lock = new Object();
	}

	public void run() {
		while (true) {
			synchronized (this.lock) {

				if (this.mServerRef != null) {
					try {
						this.mServerRef.ping((byte) 0);
					}
					catch (SystemException se) {
						Log.errorException(se);
						this.resetMServerConnection();
					}
				}
				else
					this.resetMServerConnection();

			}	//synchronized (this.lock)

			try {
				sleep(this.mServerCheckTimeout);
			}
			catch (InterruptedException ie) {
				Log.errorException(ie);
			}

		}
	}

	protected MServer getVerifiedMServerReference() throws CommunicationException {
		synchronized (this.lock) {

			if (this.mServerRef == null)
				this.resetMServerConnection();
			else {
				try {
					this.mServerRef.ping((byte) 1);
				}
				catch (SystemException se) {
					this.resetMServerConnection();
				}
			}

			if (this.mServerRef != null)
				return this.mServerRef;
			throw new CommunicationException("Cannot establish connection with Measurement Server");

		}	//synchronized (this.lock)
	}

	private void resetMServerConnection() {
		try {
			boolean notConnectedBefore = (this.mServerRef == null);
			this.mServerRef = MServerHelper.narrow(this.corbaServer.resolveReference(this.mServerServantName));
			IdentifierPool.setIdentifierGeneratorServer(this.mServerRef);
			if (notConnectedBefore) {
				//@todo Generate event "Connection with Measurement Server mServerServantName restored"
			}
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			Log.errorMessage("Cannot resolve Measurement Server '" + this.mServerServantName + "'");
			//@todo Generate event "Cannot resolve Measurement Server"
			this.mServerRef = null;
		}
	}
}
