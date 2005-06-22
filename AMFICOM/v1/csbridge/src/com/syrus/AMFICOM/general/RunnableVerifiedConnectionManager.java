/*
 * $Id: RunnableVerifiedConnectionManager.java,v 1.4 2005/06/22 17:01:10 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.general.corba.Verifiable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.4 $, $Date: 2005/06/22 17:01:10 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public class RunnableVerifiedConnectionManager extends VerifiedConnectionManager implements Runnable {
	private long timeout;


	public RunnableVerifiedConnectionManager(final CORBAServer corbaServer, final String[] servantNames, final long timeout) {
		this(corbaServer, new HashSet<String>(Arrays.asList(servantNames)), timeout);
	}

	public RunnableVerifiedConnectionManager(final CORBAServer corbaServer, final Set<String> servantNames, final long timeout) {
		super(corbaServer, servantNames);

		this.timeout = timeout;
	}

	public void run() {
		while (true) {
			synchronized (super.referencesMap) {
				String servantName;
				Verifiable reference;
				for (Iterator<String> it = super.referencesMap.keySet().iterator(); it.hasNext();) {
					servantName = it.next();
					try {
						reference = super.getVerifiableReference(servantName);
						Log.debugMessage("RunnableVerifiedConnectionManager.run | Verified reference to '" + servantName + "'", Log.DEBUGLEVEL08);
						Log.debugMessage("RunnableVerifiedConnectionManager.run | " + reference.toString(), Log.DEBUGLEVEL08);
					}
					catch (CommunicationException ce) {
						Log.errorMessage("RunnableVerifiedConnectionManager.run | Conection with '" + servantName + "' lost");
					}
					catch (IllegalDataException ide) {
						//Never
						Log.errorException(ide);
					}
				}
			}

			try {
				Thread.sleep(this.timeout);
			}
			catch (InterruptedException ie) {
				Log.errorException(ie);
			}
		}
	}

}
