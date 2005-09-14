/*
 * $Id: Alerter.java,v 1.6 2005/09/14 18:18:39 arseniy Exp $
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
 * @version $Revision: 1.6 $, $Date: 2005/09/14 18:18:39 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module leserver
 */
abstract class Alerter {
	private static Map<AlertKind, Alerter> alertersMap;

	static {
		alertersMap = Collections.synchronizedMap(new HashMap<AlertKind, Alerter>());
	}

	static void registerAlerter(final AlertKind alertKind, final Alerter alerter) {
		alertersMap.put(alertKind, alerter);
	}

	protected static Alerter getAlerter(final AlertKind alertKind) {
		Alerter alerter = alertersMap.get(alertKind);
		if (alerter == null) {
			alerter = createAlerter(alertKind);
		}
		return alerter;
	}

	private static Alerter createAlerter(final AlertKind alertKind) {
		switch (alertKind.value()) {
			case AlertKind._ALERT_KIND_EMAIL:
				return new EMailAlerter();
			case AlertKind._ALERT_KIND_SMS:
				return new SMSAlerter();
			default:
				return null;
		}
	}

	void notifyUserIfLoggedIn(final Identifier userId) throws EventNotificationException {
		final CORBAServer corbaServer = LEServerSessionEnvironment.getInstance().getLEServerServantManager().getCORBAServer();
		/**
		 * @todo implement
		 */
	}

	protected abstract void notifyUser(Identifier userId) throws EventNotificationException;
}
