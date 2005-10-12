/*-
 * $Id: EventServerImplementation.java,v 1.14 2005/10/12 07:23:24 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import static java.util.logging.Level.INFO;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.syrus.AMFICOM.eventv2.Event;
import com.syrus.AMFICOM.eventv2.corba.IdlEvent;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlCompletionStatus;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlErrorCode;
import com.syrus.AMFICOM.leserver.corba.EventServerPOA;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.14 $, $Date: 2005/10/12 07:23:24 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module leserver
 */
public class EventServerImplementation extends EventServerPOA {
	private static final long serialVersionUID = 3257569516216398643L;

	/**
	 * Map of event queues for every user
	 */
	private static final Map<Identifier, UserEventNotifier> USER_EVENT_NOTIFIERS_MAP =
		Collections.synchronizedMap(new HashMap<Identifier, UserEventNotifier>());

	/**
	 * @param idlEvent
	 * @throws AMFICOMRemoteException
	 * @see com.syrus.AMFICOM.leserver.corba.EventServerOperations#eventGeneration(com.syrus.AMFICOM.event.corba.IdlEvent)
	 */
	public void eventGeneration(
			final com.syrus.AMFICOM.event.corba.IdlEvent idlEvent)
	throws AMFICOMRemoteException {
		try {
			final com.syrus.AMFICOM.event.Event event = new com.syrus.AMFICOM.event.Event(idlEvent);
			final com.syrus.AMFICOM.event.EventType eventType = event.getType();
			for (final Identifier userId : eventType.getAlertedUserIds()) {
				UserEventNotifier userEventNotifier = USER_EVENT_NOTIFIERS_MAP.get(userId);
				if (userEventNotifier == null) {
					userEventNotifier = new UserEventNotifier(userId);
					userEventNotifier.start();
					USER_EVENT_NOTIFIERS_MAP.put(userId, userEventNotifier);
				}
				userEventNotifier.addEvent(event);
			}
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(
					IdlErrorCode.ERROR_ILLEGAL_DATA,
					IdlCompletionStatus.COMPLETED_NO,
					"Cannot create event -- " + coe.getMessage());
		}
	}

	/**
	 * @param idlEvent
	 * @throws AMFICOMRemoteException
	 * @see com.syrus.AMFICOM.leserver.corba.EventServerOperations#yetAnotherEventGeneration(IdlEvent)
	 */
	public void yetAnotherEventGeneration(final IdlEvent idlEvent)
	throws AMFICOMRemoteException {
		final Event event = idlEvent.getNative();
		Log.debugMessage("EventServerImplementation.yetAnotherEventGeneration() | Event: "
				+ event + " delivered successfully",
				INFO);
	}

	public void receiveEvents(final IdlEvent[] idlEvents) throws AMFICOMRemoteException {
		Log.debugMessage("EventServerImplementation.receiveEvents | Received " + idlEvents.length + " events", Log.DEBUGLEVEL09);
	}
		

	public void verify(byte i) {
		Log.debugMessage("Verify value: " + i, Log.DEBUGLEVEL10);
	}

}
