/*-
 * $Id: IdentifierLoader.java,v 1.6 2005/10/31 12:30:11 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
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
 * @version $Revision: 1.6 $, $Date: 2005/10/31 12:30:11 $
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
		Log.errorMessage("coundn't fetch ids");
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