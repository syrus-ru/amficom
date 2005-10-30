/*
 * $Id: RunnableVerifiedConnectionManager.java,v 1.10 2005/10/30 15:20:13 bass Exp $
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
 * @version $Revision: 1.10 $, $Date: 2005/10/30 15:20:13 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
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
						reference = this.getVerifiableReference(servantName);
						assert Log.debugMessage("Verified reference to '" + servantName + "'", Log.DEBUGLEVEL08);
						assert Log.debugMessage(reference.toString(), Log.DEBUGLEVEL08);
					} catch (final CommunicationException ce) {
						assert Log.errorMessage("Conection with '" + servantName + "' lost");
					}
				}
			}

			try {
				Thread.sleep(this.timeout);
			}
			catch (InterruptedException ie) {
				assert Log.errorMessage(ie);
			}
		}
	}

}
