/*
 * $Id: Alerter.java,v 1.2 2005/06/20 15:28:02 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.leserver;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.syrus.AMFICOM.event.corba.AlertKind;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.Identifier;

/**
 * @version $Revision: 1.2 $, $Date: 2005/06/20 15:28:02 $
 * @author $Author: arseniy $
 * @module leserver_v1
 */
abstract class Alerter {
	private static Map<AlertKind, Alerter> alertersMap;

	static {
		alertersMap = Collections.synchronizedMap(new HashMap<AlertKind, Alerter>());
	}

	static void registerAlerter(AlertKind alertKind, Alerter alerter) {
		alertersMap.put(alertKind, alerter);
	}

	protected static Alerter getAlerter(AlertKind alertKind) {
		Alerter alerter = alertersMap.get(alertKind);
		if (alerter == null)
			alerter = createAlerter(alertKind);
		return alerter;
	}

	private static Alerter createAlerter(AlertKind alertKind) {
		switch (alertKind.value()) {
			case AlertKind._ALERT_KIND_EMAIL:
				return new EMailAlerter();
			case AlertKind._ALERT_KIND_SMS:
				return new SMSAlerter();
			default:
				return null;
		}
	}

	void notifyUserIfLoggedIn(Identifier userId) throws EventNotificationException {
		CORBAServer corbaServer = LEServerSessionEnvironment.getInstance().getLEServerServantManager().getCORBAServer();
		
	}

	protected abstract void notifyUser(Identifier userId) throws EventNotificationException;
}
