/*
 * $Id: RunnableVerifiedConnectionManager.java,v 1.12.2.1 2006/06/27 15:54:53 arseniy Exp $
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

import com.syrus.AMFICOM.systemserver.corba.Verifiable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.12.2.1 $, $Date: 2006/06/27 15:54:53 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */
public class RunnableVerifiedConnectionManager extends VerifiedConnectionManager implements Runnable {
	public static final int SERVANT_CHECK_TIMEOUT = 10;		//min
	public static final String KEY_SERVANT_CHECK_TIMEOUT = "ServantCheckTimeout";

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
						Log.debugMessage("Verified reference to '" + servantName + "'", Log.DEBUGLEVEL08);
						Log.debugMessage(reference.toString(), Log.DEBUGLEVEL08);
					} catch (final CommunicationException ce) {
						Log.errorMessage("Conection with '" + servantName + "' lost");
					}
				}
			}

			try {
				Thread.sleep(this.timeout);
			}
			catch (InterruptedException ie) {
				Log.errorMessage(ie);
			}
		}
	}

}
