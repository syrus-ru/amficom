/*
 * $Id: IdentifierPool.java,v 1.21 2005/06/09 09:36:56 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import gnu.trove.TShortObjectHashMap;
import gnu.trove.TShortObjectIterator;

import java.util.Iterator;
import java.util.Map;

import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;
import com.syrus.io.FIFOSaver;
import com.syrus.util.Fifo;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.21 $, $Date: 2005/06/09 09:36:56 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public class IdentifierPool {
	private static final int DEFAULT_CAPACITY = 10;
	private static final int MAX_CAPACITY = 100;
	private static final double MIN_FILL_FACTOR = 0.2;
	private static final long MAX_TIME_WAIT = 5 * 1000; /* Maximim time to wait while identifiers are loading*/

	private static IGSConnectionManager igsConnectionMananger;
	private static int capacity;

	/* Map <short objectEntity, Fifo idPool> */
	private static TShortObjectHashMap idPoolMap;

	private IdentifierPool() {
		// empty private construcor
	}

	public static void init(IGSConnectionManager igsConnectionMananger1, int capacity1) {
		igsConnectionMananger = igsConnectionMananger1;
		capacity = (capacity1 <= MAX_CAPACITY) ? capacity1 : MAX_CAPACITY;
		idPoolMap = new TShortObjectHashMap();
	}

	public static void init(IGSConnectionManager igsConnectionMananger1) {
		init(igsConnectionMananger1, DEFAULT_CAPACITY);
	}

	public static Identifier getGeneratedIdentifier(final short entityCode) throws IdentifierGenerationException {
		assert ObjectEntities.isEntityCodeValid(entityCode) : "Illegal or unknown entity code supplied: " + entityCode;

		Fifo fifo = (Fifo) idPoolMap.get(entityCode);

		/* Add new fifo if needed */
		if (fifo == null) {
			fifo = new Fifo(capacity);
			fillFifo(fifo, entityCode);
			synchronized(idPoolMap) {
				idPoolMap.put(entityCode, fifo);
			}
		}

		/* Transfer ids when fifo filling minimum than minFillFactor */
		if (fifo.getNumber() < MIN_FILL_FACTOR * fifo.capacity())
			fillFifo(fifo, entityCode);

		/* If identifiers available -- return one*/
		if (fifo.getNumber() >= 1)
			return (Identifier) fifo.remove();

		/* Else - throw IdentifierGenerationException*/
		throw new IdentifierGenerationException("No more identifiers for entity '"
				+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
	}

	private static void fillFifo(final Fifo fifo, final short entityCode) throws IdentifierGenerationException {
		IdentifierGeneratorServer igServer = null;
		try {
			igServer = igsConnectionMananger.getIGSReference();
		}
		catch (CommunicationException ce) {
			throw new IdentifierGenerationException("Cannot obtain reference on identifier generator server", ce);
		}

		IdentifierLoader identifierLoader = new IdentifierLoader(igServer, fifo, entityCode);
		identifierLoader.start();
		/*	Do not wait more than MAX_TIME_WAIT*/
		try {
			identifierLoader.join(MAX_TIME_WAIT);
		}
		catch (InterruptedException ie) {
			Log.errorException(ie);
		}
	}

	protected static void serialize() {
		for (final TShortObjectIterator iterator = idPoolMap.iterator(); iterator.hasNext();) {
			iterator.advance();
			final short entityCode = iterator.key();
			final String entityName = ObjectEntities.codeToString(entityCode);
			final Fifo fifo = (Fifo) iterator.value();
			FIFOSaver.save(fifo, entityName);
		}
	}

	protected static void deserialize() {
		final Map codeNameFifo = FIFOSaver.load();
		for (final Iterator it = codeNameFifo.keySet().iterator(); it.hasNext();) {
			final String entityName = (String) it.next();
			final short entityCode = ObjectEntities.stringToCode(entityName);
			idPoolMap.put(entityCode, codeNameFifo.get(entityName));
		}
	}
}
