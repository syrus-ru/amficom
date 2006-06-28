/*-
 * $Id: SMSAlerter.java,v 1.6 2006/05/31 07:45:19 bass Exp $
 * 
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import static com.syrus.AMFICOM.eventv2.DeliveryMethod.SMS;

import com.syrus.AMFICOM.general.Identifier;

/**
 * @version $Revision: 1.6 $, $Date: 2006/05/31 07:45:19 $
 * @author Tashoyan Arseniy Feliksovich
 * @author $Author: bass $
 * @deprecated {@link SmsNotificationEventProcessor} should be used instead.
 * @see SmsNotificationEventProcessor
 * @module leserver
 */
@Deprecated
final class SMSAlerter extends Alerter {

	protected SMSAlerter() {
		registerAlerter(SMS, this);
	}

	@Override
	protected void notifyUser(final Identifier userId) throws EventNotificationException {
		/**
		 * @todo implement
		 */
	}

}
