/*-
 * $Id: CORBAClientImpl.java,v 1.6 2005/10/31 12:30:02 bass Exp $
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
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.6 $, $Date: 2005/10/31 12:30:02 $
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
			Log.debugMessage("Verify value: " + b, Level.CONFIG);
		} catch (final Throwable t) {
			Log.debugMessage(t, Level.SEVERE);
		}
	}

	/**
	 * @see CORBAClientOperations#getSystemUserId()
	 */
	public IdlIdentifier getSystemUserId() {
		return LoginManager.getUserId().getTransferable();
	}
}
