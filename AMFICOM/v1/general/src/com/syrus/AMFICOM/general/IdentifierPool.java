/*
 * $Id: IdentifierPool.java,v 1.15 2005/04/15 19:19:16 arseniy Exp $
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
 * @version $Revision: 1.15 $, $Date: 2005/04/15 19:19:16 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public class IdentifierPool {
	private static final int DEFAULT_CAPACITY = 10;
	private static final int MAX_CAPACITY = 100;
	private static final long TIME_TO_SLEEP = 500;
	private static final double MIN_FILL_FACTOR = 0.2;

	private static IGServerReferenceSource igServerReferenceSource;
	private static int capacity;

	/* Map <Short objectEntity, LRUMap idPool> */
	private static Map idPoolMap;

	private IdentifierPool() {
		// empty private construcor
	}

	public static void init(IGServerReferenceSource igServerReferenceSource1, int capacity1) {
		igServerReferenceSource = igServerReferenceSource1;
		capacity = (capacity1 <= MAX_CAPACITY) ? capacity1 : MAX_CAPACITY;
		idPoolMap = Collections.synchronizedMap(new HashMap());
	}

	public static void init(IGServerReferenceSource igServerReferenceSource1) {
		init(igServerReferenceSource1, DEFAULT_CAPACITY);
	}

	public static Identifier getGeneratedIdentifier(final short entityCode) throws IdentifierGenerationException {
		assert ObjectEntities.codeIsValid(entityCode) : "Illegal or unknown entity code supplied: " + entityCode;

		final Short entityCodeShort = new Short(entityCode);
		Fifo fifo = (Fifo) idPoolMap.get(entityCodeShort);

		// Add new fifo if needed
		if (fifo == null) {
			fifo = new Fifo(capacity);
			fillFifo(fifo, entityCode);
			idPoolMap.put(entityCodeShort, fifo);
		}

		// Transfer ids when fifo filling minimum than minFillFactor
		if (fifo.getNumber() < MIN_FILL_FACTOR * fifo.capacity())
			fillFifo(fifo, entityCode);

		// Wait if fifo is empty yet
		while (fifo.getNumber() < 1)
			try {
				Log.debugMessage("IdentifierPool.getGeneratedIdentifier | Wait for fetching ids", Log.DEBUGLEVEL10); //$NON-NLS-1$
				Thread.sleep(TIME_TO_SLEEP);
			}
			catch (final InterruptedException ie) {
				Log.errorException(ie);
			}

		return (Identifier) fifo.remove();
	}

	private static void fillFifo(Fifo fifo, final short entityCode) throws IdentifierGenerationException {
		IdentifierGeneratorServer igServer = null;
		try {
			igServer = igServerReferenceSource.getVerifiedIGServerReference();
		}
		catch (CommunicationException ce) {
			throw new IdentifierGenerationException("Cannot obtain reference on identifier generator server", ce);
		}

		IdentifierLoader il = new IdentifierLoader(igServer, fifo, entityCode);
		il.start();
		try {
			il.join();
		}
		catch (InterruptedException ie) {
			Log.errorException(ie);
		}
	}
}
