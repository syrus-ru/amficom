/*
 * $Id: IdentifierPool.java,v 1.9 2004/12/09 16:30:50 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;
import com.syrus.util.Fifo;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.9 $, $Date: 2004/12/09 16:30:50 $
 * @author $Author: arseniy $
 * @module cmserver_v1
 */
public class IdentifierPool {
	private static final int DEFAULT_CAPACITY = 10;
	private static final int MAX_CAPACITY = 100;
	private static final long TIME_TO_SLEEP = 500;
	private static final double MIN_FILL_FACTOR = 0.2;

	static IdentifierGeneratorServer igServer;
	static int capacity;

	/* Map <Short objectEntity, LRUMap idPool> */
	private static Map idPoolMap;

	private IdentifierPool() {
		// empty private construcor
	}

	public static void init(IdentifierGeneratorServer igServer1, int capacity1) {
		igServer = igServer1;
		capacity = (capacity1 <= MAX_CAPACITY) ? capacity1 : MAX_CAPACITY;
		idPoolMap = Collections.synchronizedMap(new HashMap());
	}

	public static void init(IdentifierGeneratorServer igServer1) {
		init(igServer1, DEFAULT_CAPACITY);
	}

	public static synchronized void setIdentifierGeneratorServer(IdentifierGeneratorServer igServer1) {
		igServer = igServer1;
	}

	public static synchronized Identifier getGeneratedIdentifier(final short entityCode) throws IllegalObjectEntityException {
		if (ObjectEntities.codeIsValid(entityCode)) {
			Short entityCodeShort = new Short(entityCode);
			Fifo fifo = (Fifo) idPoolMap.get(entityCodeShort);

			// Add new fifo if need
			if (fifo == null) {
				fifo = new Fifo(capacity);
				fillFifo(fifo, entityCode);
				idPoolMap.put(entityCodeShort, fifo);
			}

			// Transfer ids when fifo filling minimum than minFillFactor
			if (fifo.getNumber() < MIN_FILL_FACTOR * fifo.capacity())
				fillFifo(fifo, entityCode);

			// Wait if fifo is empty yet
			while (fifo.getNumber() < 1) {
				try {
					Log.debugMessage("IdentifierPool.generateId | Wait for fetching ids", Log.DEBUGLEVEL10);
					Thread.sleep(TIME_TO_SLEEP);
				}
				catch (InterruptedException ie) {
					Log.errorException(ie);
				}
			}

			return (Identifier)fifo.remove();
		}
		throw new IllegalObjectEntityException("Illegal or unknown entity code supplied: " + entityCode, IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
	}

	private static void fillFifo(Fifo fifo, final short entityCode) {
		IdentifierLoader il = new IdentifierLoader(igServer, fifo, entityCode);
		il.start();
		try {
			il.join();
		}
		catch (InterruptedException ie) {
			Log.errorException(ie);
		}
	}

	public static synchronized com.syrus.AMFICOM.general.corba.Identifier generateIdImpl(final short entityCode) {
		throw new UnsupportedOperationException();
	}
}
