/*
 * $Id: EventServerImplementation.java,v 1.8 2005/07/28 13:59:58 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.leserver;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.syrus.AMFICOM.event.Event;
import com.syrus.AMFICOM.event.EventType;
import com.syrus.AMFICOM.event.corba.IdlEvent;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlCompletionStatus;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlErrorCode;
import com.syrus.AMFICOM.leserver.corba.EventServerPOA;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.8 $, $Date: 2005/07/28 13:59:58 $
 * @author $Author: arseniy $
 * @module leserver_v1
 */
public class EventServerImplementation extends EventServerPOA {
	private static final long serialVersionUID = 3257569516216398643L;

	/*	Map of event queues for every user*/
	private static Map<Identifier, UserEventNotifier> userEventNotifiersMap;

	static {
		userEventNotifiersMap = Collections.synchronizedMap(new HashMap<Identifier, UserEventNotifier>());
	}

	public void eventGeneration(IdlEvent et) throws AMFICOMRemoteException {
		try {
			final Event event = new Event(et);
			final EventType eventType = (EventType) event.getType();
			for (final Iterator it = eventType.getAlertedUserIds().iterator(); it.hasNext();) {
				final Identifier userId = (Identifier) it.next();
				UserEventNotifier userEventNotifier = userEventNotifiersMap.get(userId);
				if (userEventNotifier == null) {
					userEventNotifier = new UserEventNotifier(userId);
					userEventNotifier.start();
					userEventNotifiersMap.put(userId, userEventNotifier);
				}
				userEventNotifier.addEvent(event);
			}
		}
		catch (CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(IdlErrorCode.ERROR_ILLEGAL_DATA, IdlCompletionStatus.COMPLETED_NO, "Cannot create event -- "
					+ coe.getMessage());
		}
	}

	public void verify(byte i) {
		Log.debugMessage("Verify value: " + i, Log.DEBUGLEVEL10);
	}

}
