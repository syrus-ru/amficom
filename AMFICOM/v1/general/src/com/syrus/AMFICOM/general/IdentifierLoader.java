/*
 * $Id: IdentifierLoader.java,v 1.13 2005/09/14 18:51:55 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.util.Fifo;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.13 $, $Date: 2005/09/14 18:51:55 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
final class IdentifierLoader extends SleepButWorkThread {
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

	@Override
	public void run() {
		int numberToLoad = this.idPool.capacity() - this.idPool.getNumber();
		while (this.running && numberToLoad > 0) {
			try {
				final IdlIdentifier[] identifiersT = this.igServer.getGeneratedIdentifierRange(this.entityCode, numberToLoad);
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

	@Override
	protected void processFall() {
		Log.errorMessage("Aboting load of identifiers for entity '"
				+ ObjectEntities.codeToString(this.entityCode) + "'/" + this.entityCode);
		this.running = false;
	}
}
