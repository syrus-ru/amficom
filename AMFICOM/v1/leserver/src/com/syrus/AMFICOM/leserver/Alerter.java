/*
 * $Id: Alerter.java,v 1.3 2005/07/03 19:16:18 bass Exp $
 * 
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
package com.syrus.AMFICOM.leserver;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.syrus.AMFICOM.event.corba.IdlEventTypePackage.AlertKind;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.Identifier;

/**
 * @version $Revision: 1.3 $, $Date: 2005/07/03 19:16:18 $
 * @author $Author: bass $
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
