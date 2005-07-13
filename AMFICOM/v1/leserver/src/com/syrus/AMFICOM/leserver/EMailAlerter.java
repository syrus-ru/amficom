/*
 * $Id: EMailAlerter.java,v 1.3 2005/07/13 19:27:08 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.leserver;

import com.syrus.AMFICOM.event.corba.IdlEventTypePackage.AlertKind;
import com.syrus.AMFICOM.general.Identifier;

/**
 * @version $Revision: 1.3 $, $Date: 2005/07/13 19:27:08 $
 * @author $Author: arseniy $
 * @module leserver_v1
 */
final class EMailAlerter extends Alerter {

	protected EMailAlerter() {
		registerAlerter(AlertKind.ALERT_KIND_EMAIL, this);
	}

	@Override
	protected void notifyUser(Identifier userId) throws EventNotificationException {
	}

}
