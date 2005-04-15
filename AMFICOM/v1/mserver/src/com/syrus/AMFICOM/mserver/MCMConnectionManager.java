/*
 * $Id: MCMConnectionManager.java,v 1.5 2005/04/15 22:13:26 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.mserver;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.omg.CORBA.SystemException;

import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.mcm.corba.MCM;
import com.syrus.AMFICOM.mcm.corba.MCMHelper;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.5 $, $Date: 2005/04/15 22:13:26 $
 * @author $Author: arseniy $
 * @module cmserver_v1
 */
public class MCMConnectionManager extends Thread {
	private CORBAServer corbaServer;
	private long mcmCheckTimeout;

	private Map mcmRefsMap;	//Map <Identifier mcmId, com.syrus.AMFICOM.mcm.corba.MCM mcmRef>
	private Set disconnectedMCMIds;	//Set <Identifier mcmId>

	public MCMConnectionManager(CORBAServer corbaServer, long mcmCheckTimeout) {
		assert corbaServer != null : "corbaServer is NULL";
		assert mcmCheckTimeout >= 10 * 60 * 1000 : "Too low timeToSleep"; //not less then 10 min

		this.corbaServer = corbaServer;
		this.mcmCheckTimeout = mcmCheckTimeout;

		this.mcmRefsMap = Collections.synchronizedMap(new HashMap());
		this.disconnectedMCMIds = Collections.synchronizedSet(new HashSet());
	}

	public void run() {
		Set mcmIds;
		Identifier mcmId;
		MCM mcmRef;
		while (true) {
			mcmIds = MeasurementServer.getMCMIds();
			
			for (Iterator it = mcmIds.iterator(); it.hasNext();) {
				mcmId = (Identifier) it.next();
				mcmRef = (MCM) this.mcmRefsMap.get(mcmId);
				
				if (mcmRef != null) {
					try {
						mcmRef.verify((byte) 0);
					}
					catch (SystemException se) {
						Log.errorException(se);
						this.activateMCMReferenceWithId(mcmId);
					}
				}
				else {
					this.activateMCMReferenceWithId(mcmId);
				}
				
			}

			try {
				sleep(this.mcmCheckTimeout);
			}
			catch (InterruptedException ie) {
				Log.errorException(ie);
			}

		}
	}

	protected MCM getVerifiedMCMReference(Identifier mcmId) throws CommunicationException, IllegalDataException {
		MCM mcmRef = (MCM) this.mcmRefsMap.get(mcmId);

		if (mcmRef == null)
			mcmRef = this.activateAndGet(mcmId);
		else {
			try {
				mcmRef.verify((byte) 1);
			}
			catch (SystemException se) {
				mcmRef = this.activateAndGet(mcmId);
			}
		}

		return mcmRef;
	}

	private MCM activateAndGet(Identifier mcmId) throws CommunicationException {
		this.activateMCMReferenceWithId(mcmId);
		MCM mcmRef = (MCM) this.mcmRefsMap.get(mcmId);
		if (mcmRef != null)
			return mcmRef;
		throw new CommunicationException("Cannot establish connection with MCM '" + mcmId + "'");
	}

	private void activateMCMReferenceWithId(Identifier mcmId) {
		try {
			MCM mcmRef = MCMHelper.narrow(this.corbaServer.resolveReference(mcmId.toString()));
			this.mcmRefsMap.put(mcmId, mcmRef);
			if (this.disconnectedMCMIds.remove(mcmId)) {
				//@todo Generate event "Connection with MCM mcmId restored"
			}
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			this.disconnectedMCMIds.add(mcmId);
			//@todo Generate event "Connection with MCM mcmId lost"
		}
	}

}
