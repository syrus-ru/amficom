/*-
 * $Id: Alerter.java,v 1.8 2006/05/31 07:45:19 bass Exp $
 * 
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import com.syrus.AMFICOM.eventv2.DeliveryMethod;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.Identifier;

/**
 * @version $Revision: 1.8 $, $Date: 2006/05/31 07:45:19 $
 * @author Tashoyan Arseniy Feliksovich
 * @author $Author: bass $
 * @module leserver
 */
abstract class Alerter {
	private static final Map<DeliveryMethod, Alerter> ALERTERS_MAP;

	static {
		ALERTERS_MAP = Collections.synchronizedMap(new EnumMap<DeliveryMethod, Alerter>(DeliveryMethod.class));
	}

	static void registerAlerter(final DeliveryMethod deliveryMethod, final Alerter alerter) {
		ALERTERS_MAP.put(deliveryMethod, alerter);
	}

	protected static Alerter getAlerter(final DeliveryMethod deliveryMethod) {
		Alerter alerter = ALERTERS_MAP.get(deliveryMethod);
		if (alerter == null) {
			alerter = createAlerter(deliveryMethod);
		}
		return alerter;
	}

	private static Alerter createAlerter(final DeliveryMethod deliveryMethod) {
		switch (deliveryMethod) {
			case EMAIL:
				@SuppressWarnings("deprecation")
				final Alerter emailAlerter = new EMailAlerter();
				return emailAlerter;
			case SMS:
				@SuppressWarnings("deprecation")
				final Alerter smsAlerter = new SMSAlerter();
				return smsAlerter;
			case POPUP:
			default:
				return null;
		}
	}

	@SuppressWarnings(value = {"unused"})
	void notifyUserIfLoggedIn(final Identifier userId) throws EventNotificationException {
		final CORBAServer corbaServer = LEServerSessionEnvironment.getInstance().getLEServerServantManager().getCORBAServer();
		/**
		 * @todo implement
		 */
	}

	protected abstract void notifyUser(Identifier userId) throws EventNotificationException;
}
