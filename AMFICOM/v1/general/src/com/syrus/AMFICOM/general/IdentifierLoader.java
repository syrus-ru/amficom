/*
 * $Id: IdentifierLoader.java,v 1.8 2005/06/17 12:38:53 bass Exp $
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
 * @version $Revision: 1.8 $, $Date: 2005/06/17 12:38:53 $
 * @author $Author: bass $
 * @module general_v1
 */
public class IdentifierLoader extends SleepButWorkThread {
	private static final long TIME_TO_SLEEP = 200;

	private IdentifierGeneratorServer	igServer;
	private Fifo idPool;
	private short entityCode;
	private boolean running;

	public IdentifierLoader(final IdentifierGeneratorServer igServer, final Fifo idPool, final short entityCode) {
		super(TIME_TO_SLEEP);
		this.igServer = igServer;
		this.idPool = idPool;
		this.entityCode = entityCode;
		this.running = true;
	}

	public void run() {
		synchronized (this.idPool) {
			int numberToLoad = this.idPool.capacity() - this.idPool.getNumber();
			while (this.running && numberToLoad > 0) {
				try {
					final Identifier_Transferable[] identifiersT = this.igServer.getGeneratedIdentifierRange(this.entityCode, numberToLoad);
					for (int i = 0; i < identifiersT.length; i++) {
						Identifier id = new Identifier(identifiersT[i]);
						this.idPool.push(id);
					}
					numberToLoad -= identifiersT.length;
				} catch (AMFICOMRemoteException are) {
					Log.errorMessage(are.message);
					super.sleepCauseOfFall();
				}
			}
		}
	}

	protected void processFall() {
		Log.errorMessage("Aboting load of identifiers for entity '"
				+ ObjectEntities.codeToString(this.entityCode) + "'/" + this.entityCode);
		this.running = false;
	}
}
