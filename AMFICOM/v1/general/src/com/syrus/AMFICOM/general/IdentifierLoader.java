/*
 * $Id: IdentifierLoader.java,v 1.6 2005/05/06 05:19:21 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.util.Fifo;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.6 $, $Date: 2005/05/06 05:19:21 $
 * @author $Author: bob $
 * @module general_v1
 */
public class IdentifierLoader extends SleepButWorkThread {
	private static final long TIME_TO_SLEEP = 200;

	private IdentifierGeneratorServer	igServer;
	private Fifo idPool;
	private short entityCode;

	public IdentifierLoader(IdentifierGeneratorServer igServer, Fifo idPool, short entityCode) {
		super(TIME_TO_SLEEP);
		this.igServer = igServer;
		this.idPool = idPool;
		this.entityCode = entityCode;
	}

	protected void processFall() {
		Log.errorMessage("IdentifierLoader.processFall | coundn't fetch " + ObjectEntities.codeToString(this.entityCode) + " ids");
	}

	protected void clearFalls() {
		super.clearFalls();
	}

	public void run() {
		Identifier_Transferable[] generatedIdentifierRange = null;
		while (generatedIdentifierRange == null) {
			try {
				int size = this.idPool.capacity() - this.idPool.getNumber();
				generatedIdentifierRange = this.igServer.getGeneratedIdentifierRange(this.entityCode, size);
				Log.debugMessage("IdentifierLoader.run | fetched " + generatedIdentifierRange.length + " identifiers for " + ObjectEntities.codeToString(this.entityCode), Log.DEBUGLEVEL10);
			}
			catch (AMFICOMRemoteException e) {
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
