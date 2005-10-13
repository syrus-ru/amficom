/*-
 * $Id: CORBAClientImpl.java,v 1.2 2005/10/13 09:57:25 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.event.PopupMessageReceiver;
import com.syrus.AMFICOM.eventv2.Event;
import com.syrus.AMFICOM.eventv2.corba.IdlEvent;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CORBAClientOperations;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.2 $, $Date: 2005/10/13 09:57:25 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module commonclient
 */
final class CORBAClientImpl implements CORBAClientOperations {
	private static final long serialVersionUID = -2562740055558726787L;

	final List<PopupMessageReceiver> popupMessageReceivers;

	public CORBAClientImpl() {
		this.popupMessageReceivers = Collections.synchronizedList(new LinkedList<PopupMessageReceiver>());
	}

	public void receiveMessages(final IdlEvent idlEvent) throws AMFICOMRemoteException {
		final Event event = idlEvent.getNativeEvent();
		synchronized (this.popupMessageReceivers) {
			for (final PopupMessageReceiver popupMessageReceiver : this.popupMessageReceivers) {
				popupMessageReceiver.receiveMessage(event);
			}
		}
	}

	void addPopupMessageReceiver(final PopupMessageReceiver popupMessageReceiver) {
		this.popupMessageReceivers.add(popupMessageReceiver);
	}

	void removePopupMessageReceiver(final PopupMessageReceiver popupMessageReceiver) {
		this.popupMessageReceivers.remove(popupMessageReceiver);
	}

	public void verify(final byte b) {
		try {
			Log.debugMessage("CORBAClientImpl.verify | Verify value: " + b, Level.CONFIG);
		} catch (final Throwable t) {
			Log.debugException(t, Level.SEVERE);
		}
	}


}
