/*
 * $Id: SMSAlerter.java,v 1.2 2005/07/03 19:16:18 bass Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.leserver;

import com.syrus.AMFICOM.event.corba.IdlEventTypePackage.AlertKind;
import com.syrus.AMFICOM.general.Identifier;

/**
 * @version $Revision: 1.2 $, $Date: 2005/07/03 19:16:18 $
 * @author $Author: bass $
 * @module leserver_v1
 */
final class SMSAlerter extends Alerter {

	protected SMSAlerter() {
		registerAlerter(AlertKind.ALERT_KIND_SMS, this);
	}

	protected void notifyUser(Identifier userId) throws EventNotificationException {
	}

}
