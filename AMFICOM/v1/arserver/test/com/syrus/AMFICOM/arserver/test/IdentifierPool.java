/*
 * $Id: IdentifierPool.java,v 1.1 2005/01/21 06:56:58 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.arserver.test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.syrus.AMFICOM.arserver.corba.ARServer;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.util.Fifo;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2005/01/21 06:56:58 $
 * @author $Author: max $
 * @module cmserver_v1
 */
public class IdentifierPool {

	static ARServer		server;

	static int		capacity	= 10;

	private static double	minFilling	= 0.2;

	/* Map <Short objectEntity, LRUMap idPool> */
	private static Map	idPoolMap;

	private static long	timeToSleep	= 200;

	private IdentifierPool() {
		// empty private construcor
	}

	public static void init(ARServer server, int capacity) {
		IdentifierPool.server = server;
		IdentifierPool.capacity = capacity;
		idPoolMap = Collections.synchronizedMap(new HashMap());

	}

	public static void init(ARServer server) {
		init(server, 10);
	}

	public static synchronized Identifier generateId(short entityCode) {
		Short entityCodeShort = new Short(entityCode);
		Fifo fifo = (Fifo) idPoolMap.get(entityCodeShort);
		if (fifo == null) {
			fifo = new Fifo(capacity);
			new IdentifierLoader(server, fifo, entityCode).start();
			idPoolMap.put(entityCodeShort, fifo);
		}

		// tranfer ids then fifo filling minimum than minFilling
		if (fifo.getNumber() < minFilling * fifo.capacity()) {
			new IdentifierLoader(server, fifo, entityCode).start();
		}

		while (fifo.getNumber() < 1) {
			try {
				Log.debugMessage("IdentifierPool.generateId | Wait for fetching ids", Log.DEBUGLEVEL10);
				Thread.sleep(timeToSleep);
			} catch (InterruptedException ie) {
				Log.errorException(ie);
			}
		}

		Identifier id = (Identifier) fifo.remove();
		return id;
	}
}
