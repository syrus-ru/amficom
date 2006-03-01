/*-
 * $Id: CORBAClientImpl.java,v 1.8 2006/03/01 20:46:53 bass Exp $
 *
 * Copyright © 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.event.EventReceiver;
import com.syrus.AMFICOM.eventv2.Event;
import com.syrus.AMFICOM.eventv2.corba.IdlEvent;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CORBAClientPOA;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.8 $, $Date: 2006/03/01 20:46:53 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module commonclient
 */
final class CORBAClientImpl extends CORBAClientPOA {
	private static final long serialVersionUID = -2562740055558726787L;

	private final String hostName;
	private final List<EventReceiver> eventReceivers;

	public CORBAClientImpl() {
		String hostname;
		try {
			hostname = InetAddress.getLocalHost().getCanonicalHostName();
		} catch (final UnknownHostException uhe) {
			hostname = "localhost";
			//Не бывать тому!
			Log.errorMessage(uhe);
		}
		this.hostName = hostname;

		this.eventReceivers = Collections.synchronizedList(new LinkedList<EventReceiver>());
	}

	public String getHostName() {
		return this.hostName;
	}

	/**
	 * @param idlEvent
	 * @throws AMFICOMRemoteException
	 * @see com.syrus.AMFICOM.eventv2.corba.EventReceiverOperations#receiveEvent(IdlEvent)
	 */
	public void receiveEvent(final IdlEvent idlEvent)
	throws AMFICOMRemoteException {
		final Event<?> event = idlEvent.getNativeEvent();
		synchronized (this.eventReceivers) {
			for (final EventReceiver eventReceiver : this.eventReceivers) {
				eventReceiver.receiveEvent(event);
			}
		}
	}

	public void verify(final byte b) {
		try {
			Log.debugMessage("Verify value: " + b, Level.CONFIG);
		} catch (final Throwable t) {
			Log.debugMessage(t, Level.SEVERE);
		}
	}

	void addEventReceiver(final EventReceiver eventReceiver) {
		this.eventReceivers.add(eventReceiver);
	}

	void removeEventReceiver(final EventReceiver eventReceiver) {
		this.eventReceivers.remove(eventReceiver);
	}
}
