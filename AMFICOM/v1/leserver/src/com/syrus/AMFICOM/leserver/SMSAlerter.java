/*
 * $Id: SMSAlerter.java,v 1.1 2005/06/01 16:02:53 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.leserver;

import com.syrus.AMFICOM.event.corba.AlertKind;
import com.syrus.AMFICOM.general.Identifier;

/**
 * @version $Revision: 1.1 $, $Date: 2005/06/01 16:02:53 $
 * @author $Author: arseniy $
 * @module leserver_v1
 */
final class SMSAlerter extends Alerter {

	protected SMSAlerter() {
		registerAlerter(AlertKind.ALERT_KIND_SMS, this);
	}

	protected void notifyUser(Identifier userId) throws EventNotificationException {
	}

}
