/*
 * $Id: IdentifierPool.java,v 1.37 2005/10/31 12:30:18 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import gnu.trove.TShortObjectHashMap;
import gnu.trove.TShortObjectIterator;

import java.util.Map;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;
import com.syrus.io.FIFOSaver;
import com.syrus.util.Fifo;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.37 $, $Date: 2005/10/31 12:30:18 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public class IdentifierPool {
	private static final int DEFAULT_CAPACITY = 10;
	private static final int MAX_CAPACITY = 100;
	private static final double MIN_FILL_FACTOR = 0.2;
	private static final long MAX_TIME_WAIT = 5 * 1000; /* Maximim time to wait while identifiers are loading*/

	static IGSConnectionManager igsConnectionMananger;
	private static int capacity;

	protected static CORBAActionProcessor corbaActionProcessor;
	
	/* Map <short objectEntity, Fifo idPool> */
	private static TShortObjectHashMap idPoolMap;

	private IdentifierPool() {
		// empty private construcor
	}


	public static void init(final IGSConnectionManager igsConnectionMananger1) {
		init(igsConnectionMananger1, DEFAULT_CAPACITY, getDefaultCORBAActionProcessor());
	}

	public static void init(final IGSConnectionManager igsConnectionMananger1, final CORBAActionProcessor corbaActionProcessor1) {
		init(igsConnectionMananger1, DEFAULT_CAPACITY, corbaActionProcessor1);
	}

	public static void init(final IGSConnectionManager igsConnectionMananger1, final int capacity1) {
		init(igsConnectionMananger1, capacity1, getDefaultCORBAActionProcessor());
	}

	public static void init(final IGSConnectionManager igsConnectionMananger1,
			final int capacity1,
			final CORBAActionProcessor corbaActionProcessor1) {
		igsConnectionMananger = igsConnectionMananger1;
		capacity = (capacity1 <= MAX_CAPACITY) ? capacity1 : MAX_CAPACITY;
		corbaActionProcessor = corbaActionProcessor1;

		idPoolMap = new TShortObjectHashMap();
	}


	public static Identifier getGeneratedIdentifier(final short entityCode) throws IdentifierGenerationException {
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE + ": " + entityCode;

		Fifo fifo = (Fifo) idPoolMap.get(entityCode);

		/* Add new fifo if needed */
		if (fifo == null) {
			fifo = new Fifo(capacity);
			synchronized(idPoolMap) {
				idPoolMap.put(entityCode, fifo);
			}
		}

		synchronized (fifo) {
			/* Transfer ids when fifo filling minimum than minFillFactor */
			if (fifo.getNumber() < MIN_FILL_FACTOR * fifo.capacity()) {
				fillFifo(fifo, entityCode);
			}

			/* If identifiers available -- return one*/
			if (fifo.getNumber() >= 1) {
				return (Identifier) fifo.remove();
			}
		}

		/* Else - throw IdentifierGenerationException*/
		throw new IdentifierGenerationException("No more identifiers for entity '"
				+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
	}

	private static void fillFifo(final Fifo fifo, final short entityCode) throws IdentifierGenerationException {
		final CORBAAction corbaAction = new CORBAAction() {
			public void perform() throws AMFICOMRemoteException, ApplicationException {
				IdentifierGeneratorServer igServer = null;
				try {
					igServer = igsConnectionMananger.getIGSReference();
				} catch (CommunicationException ce) {
					throw new IdentifierGenerationException("Cannot obtain reference on identifier generator server", ce);
				}

				final IdentifierLoader identifierLoader = new IdentifierLoader(igServer, fifo, entityCode);
				identifierLoader.start();
				/*	Do not wait more than MAX_TIME_WAIT*/
				try {
					identifierLoader.join(MAX_TIME_WAIT);
				} catch (InterruptedException ie) {
					Log.errorMessage(ie);
				}
			}
		};

		try {
			corbaActionProcessor.performAction(corbaAction);
		} catch (final ApplicationException ae) {
			if (ae instanceof IdentifierGenerationException) {
				throw (IdentifierGenerationException) ae;
			}
			throw new IdentifierGenerationException("Cannot obtain reference on identifier generator server", ae);
		}

	}

	private static CORBAActionProcessor getDefaultCORBAActionProcessor() {
		return new CORBAActionProcessor() {
			public void performAction(final CORBAAction action) throws ApplicationException {
				try {
					action.perform();
				} catch (final IdentifierGenerationException e) {
					throw e; 
				} catch (final AMFICOMRemoteException e) {
					throw new IdentifierGenerationException("Cannot obtain reference on identifier generator server", e);
				}
			}
		};
	}

	protected static void serialize() {
		synchronized (idPoolMap) {
			for (final TShortObjectIterator iterator = idPoolMap.iterator(); iterator.hasNext();) {
				iterator.advance();
				final short entityCode = iterator.key();
				final String entityName = ObjectEntities.codeToString(entityCode);
				final Fifo fifo = (Fifo) iterator.value();
				FIFOSaver.save(fifo, entityName);
			}
		}

		FIFOSaver.touchFlagFile();
	}

	protected static void deserialize() {
		final Map<String, Fifo> codeNameFifo = FIFOSaver.load();
		for (final String entityName : codeNameFifo.keySet()) {
			final short entityCode = ObjectEntities.stringToCode(entityName);
			idPoolMap.put(entityCode, codeNameFifo.get(entityName));
		}
	}
}
