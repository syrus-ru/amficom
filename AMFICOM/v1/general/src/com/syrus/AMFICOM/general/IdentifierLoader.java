/*
 * $Id: IdentifierLoader.java,v 1.1 2004/09/30 10:06:54 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.cmserver.corba.CMServer;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.util.Fifo;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2004/09/30 10:06:54 $
 * @author $Author: bob $
 * @module module
 */
public class IdentifierLoader extends SleepButWorkThread {

	private CMServer	server;
	private Fifo		idPool;
	private short		entityCode;
	private static int	timeToSleep	= 1000;

	public IdentifierLoader(CMServer server, Fifo idPool, short entityCode) {
		super(timeToSleep);
		this.server = server;
		this.idPool = idPool;
		this.entityCode = entityCode;
	}

	protected void processFall() {
		Log.errorMessage("coundn't fetch ids");
	}

	protected void clearFalls() {
		super.clearFalls();
	}

	public void run() {
		Identifier_Transferable[] generatedIdentifierRange = null;
		while (generatedIdentifierRange == null) {
			try {
				int size = this.idPool.capacity() - this.idPool.getNumber();
				generatedIdentifierRange = this.server.getGeneratedIdentifierRange(this.entityCode,
													size);
				Log.debugMessage("IdentifierLoader.run | fetched " + generatedIdentifierRange.length + " identifiers for "
						+ this.entityCode, Log.DEBUGLEVEL10);
			} catch (AMFICOMRemoteException e) {
				Log.errorMessage(e.getMessage());
				sleepCauseOfFall();
			}
		}

		for (int i = 0; i < generatedIdentifierRange.length; i++) {
			Identifier id = new Identifier(generatedIdentifierRange[i]);
			this.idPool.push(id);
		}

	}
}
