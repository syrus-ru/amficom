/*-
 * $Id: IdentifierLoader.java,v 1.7 2006/04/19 13:39:14 bass Exp $
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
 * @version $Revision: 1.7 $, $Date: 2006/04/19 13:39:14 $
 * @author $Author: bass $
 * @module mscharserver
 */
public class IdentifierLoader extends SleepButWorkThread {

	private MscharServer server;
	private Fifo<Identifier> idPool;
	private short entityCode;
	private static int timeToSleep	= 1000;

	public IdentifierLoader(MscharServer server, Fifo<Identifier> idPool, short entityCode) {
		super(timeToSleep);
		this.server = server;
		this.idPool = idPool;
		this.entityCode = entityCode;
	}

	@Override
	protected void processFall() {
		Log.errorMessage("coundn't fetch ids");
	}

	@Override
	protected void clearFalls() {
		super.clearFalls();
	}

	@Override
	public void run() {
		IdlIdentifier[] generatedIdentifierRange = null;
		while (generatedIdentifierRange == null) {
			try {
				int size = this.idPool.capacity() - this.idPool.size();
				generatedIdentifierRange = this.server.getGeneratedIdentifierRange(this.entityCode,
													size);
				Log.debugMessage("fetched " + generatedIdentifierRange.length + " identifiers for "
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
