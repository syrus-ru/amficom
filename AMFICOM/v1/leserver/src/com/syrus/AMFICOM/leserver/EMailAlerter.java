/*
 * $Id: EMailAlerter.java,v 1.5 2005/08/08 11:42:21 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.leserver;

import com.syrus.AMFICOM.event.corba.IdlEventTypePackage.AlertKind;
import com.syrus.AMFICOM.general.Identifier;

/**
 * @version $Revision: 1.5 $, $Date: 2005/08/08 11:42:21 $
 * @author $Author: arseniy $
 * @module leserver
 */
final class EMailAlerter extends Alerter {

	protected EMailAlerter() {
		registerAlerter(AlertKind.ALERT_KIND_EMAIL, this);
	}

	@Override
	protected void notifyUser(final Identifier userId) throws EventNotificationException {
		/**
		 * @todo implement
		 */
	}

}
