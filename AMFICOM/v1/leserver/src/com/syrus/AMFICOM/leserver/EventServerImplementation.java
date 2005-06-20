/*
 * $Id: EventServerImplementation.java,v 1.6 2005/06/20 15:28:02 arseniy Exp $
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

import com.syrus.AMFICOM.leserver.corba.EventServerPOA;
import com.syrus.AMFICOM.event.Event;
import com.syrus.AMFICOM.event.EventType;
import com.syrus.AMFICOM.event.corba.Event_Transferable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.CompletionStatus;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.ErrorCode;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.6 $, $Date: 2005/06/20 15:28:02 $
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

	public void eventGeneration(Event_Transferable et) throws AMFICOMRemoteException {
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
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_DATA, CompletionStatus.COMPLETED_NO, "Cannot create event -- "
					+ coe.getMessage());
		}
	}

	public void verify(byte i) {
		Log.debugMessage("Verify value: " + i, Log.DEBUGLEVEL10);
	}

}
