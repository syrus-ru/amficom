/*-
 * $Id: CORBAClientImpl.java,v 1.7 2005/11/28 12:24:55 arseniy Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
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

import com.syrus.AMFICOM.client.event.PopupMessageReceiver;
import com.syrus.AMFICOM.eventv2.Event;
import com.syrus.AMFICOM.eventv2.corba.IdlEvent;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CORBAClientPOA;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.7 $, $Date: 2005/11/28 12:24:55 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module commonclient
 */
final class CORBAClientImpl extends CORBAClientPOA {
	private static final long serialVersionUID = -2562740055558726787L;

	private final String hostName;
	private final List<PopupMessageReceiver> popupMessageReceivers;

	public CORBAClientImpl() {
		String hostname;
		try {
			hostname = InetAddress.getLocalHost().getCanonicalHostName();
		} catch (UnknownHostException uhe) {
			hostname = "unknown";
			//Не бывать тому!
			Log.errorMessage(uhe);
		}
		this.hostName = hostname;

		this.popupMessageReceivers = Collections.synchronizedList(new LinkedList<PopupMessageReceiver>());
	}

	public String getHostName() {
		return this.hostName;
	}

	public void receiveMessages(final IdlEvent idlEvent) throws AMFICOMRemoteException {
		final Event event = idlEvent.getNativeEvent();
		synchronized (this.popupMessageReceivers) {
			for (final PopupMessageReceiver popupMessageReceiver : this.popupMessageReceivers) {
				popupMessageReceiver.receiveMessage(event);
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

	void addPopupMessageReceiver(final PopupMessageReceiver popupMessageReceiver) {
		this.popupMessageReceivers.add(popupMessageReceiver);
	}

	void removePopupMessageReceiver(final PopupMessageReceiver popupMessageReceiver) {
		this.popupMessageReceivers.remove(popupMessageReceiver);
	}

}
