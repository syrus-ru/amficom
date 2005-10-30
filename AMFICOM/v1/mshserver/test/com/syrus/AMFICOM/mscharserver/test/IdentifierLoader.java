/*-
 * $Id: IdentifierLoader.java,v 1.5 2005/10/30 15:20:34 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mscharserver.test;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.mscharserver.corba.MscharServer;
import com.syrus.util.Fifo;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.5 $, $Date: 2005/10/30 15:20:34 $
 * @author $Author: bass $
 * @module mscharserver
 */
public class IdentifierLoader extends SleepButWorkThread {

	private MscharServer	server;
	private Fifo		idPool;
	private short		entityCode;
	private static int	timeToSleep	= 1000;

	public IdentifierLoader(MscharServer server, Fifo idPool, short entityCode) {
		super(timeToSleep);
		this.server = server;
		this.idPool = idPool;
		this.entityCode = entityCode;
	}

	protected void processFall() {
		assert Log.errorMessage("coundn't fetch ids");
	}

	protected void clearFalls() {
		super.clearFalls();
	}

	public void run() {
		IdlIdentifier[] generatedIdentifierRange = null;
		while (generatedIdentifierRange == null) {
			try {
				int size = this.idPool.capacity() - this.idPool.getNumber();
				generatedIdentifierRange = this.server.getGeneratedIdentifierRange(this.entityCode,
													size);
				assert Log.debugMessage("fetched " + generatedIdentifierRange.length + " identifiers for "
						+ this.entityCode, Log.DEBUGLEVEL10);
			} catch (AMFICOMRemoteException e) {
				assert Log.errorMessage(e.getMessage());
				sleepCauseOfFall();
			}
		}

		for (int i = 0; i < generatedIdentifierRange.length; i++) {
			Identifier id = new Identifier(generatedIdentifierRange[i]);
			this.idPool.push(id);
		}

	}
}
