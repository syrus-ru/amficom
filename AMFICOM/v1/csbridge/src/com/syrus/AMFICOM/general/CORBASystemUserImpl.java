/*-
 * $Id: CORBASystemUserImpl.java,v 1.1.2.1 2006/06/27 15:52:13 arseniy Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import static java.util.logging.Level.SEVERE;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import com.syrus.AMFICOM.event.EventReceiver;
import com.syrus.AMFICOM.eventv2.Event;
import com.syrus.AMFICOM.eventv2.corba.IdlEvent;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.systemserver.corba.CORBASystemUserOperations;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1.2.1 $, $Date: 2006/06/27 15:52:13 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */
public final class CORBASystemUserImpl implements CORBASystemUserOperations {
	private final String hostName;
	private final List<EventReceiver> eventReceivers;

	public CORBASystemUserImpl() {
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

	public void receiveEvent(final IdlEvent idlEvent) {
		final Event<?> event;

		try {
			event = idlEvent.getNativeEvent();
		} catch (final IdlCreateObjectException coe) {
			/**
			 * @todo Возможно, нужно уведомить пользователя о том, что пришедшее
			 *       событие невозожно отобразить.
			 */
			Log.debugMessage(coe, SEVERE);
			return;
		}

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
