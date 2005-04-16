/*
 * $Id: RunnableVerifiedConnectionManager.java,v 1.1 2005/04/16 21:07:56 arseniy Exp $
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
 * @version $Revision: 1.1 $, $Date: 2005/04/16 21:07:56 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public abstract class RunnableVerifiedConnectionManager extends VerifiedConnectionManager implements Runnable {
	private long timeout;


	public RunnableVerifiedConnectionManager(CORBAServer corbaServer, String[] servantNames, long timeout) {
		this(corbaServer, new HashSet(Arrays.asList(servantNames)), timeout);
	}

	public RunnableVerifiedConnectionManager(CORBAServer corbaServer, Set servantNames, long timeout) {
		super(corbaServer, servantNames);

		this.timeout = timeout;
	}

	public void run() {
		while (true) {
			synchronized (super.referencesMap) {
				String servantName;
				Verifiable reference;
				for (Iterator it = super.referencesMap.keySet().iterator(); it.hasNext();) {
					servantName = (String) it.next();
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
