/*
 * $Id: EventServerImplementation.java,v 1.1 2005/04/28 10:33:08 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.leserver;

import com.syrus.AMFICOM.leserver.corba.EventServerPOA;
import com.syrus.AMFICOM.event.Event;
import com.syrus.AMFICOM.event.corba.Event_Transferable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2005/04/28 10:33:08 $
 * @author $Author: arseniy $
 * @module eserver_v1
 */
public class EventServerImplementation extends EventServerPOA {
	private static final long serialVersionUID = -5598344511722433000L;

	public void eventGeneration(Event_Transferable et) throws AMFICOMRemoteException {
		try {
			Event event = new Event(et);
			EventProcessor.addEventToQueue(event);
		}
		catch (CreateObjectException coe) {
			Log.errorException(coe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_DATA, CompletionStatus.COMPLETED_NO, "Cannot create event -- " + coe.getMessage());
		}
	}

	public void verify(byte i) {
		Log.debugMessage("Verify value: " + i, Log.DEBUGLEVEL10);
	}

}
