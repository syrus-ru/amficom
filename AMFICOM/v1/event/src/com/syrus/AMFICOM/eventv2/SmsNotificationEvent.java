/*-
 * $Id: SmsNotificationEvent.java,v 1.3 2005/10/11 08:58:25 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import com.syrus.AMFICOM.eventv2.corba.IdlSmsNotificationEvent;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/10/11 08:58:25 $
 * @module event
 */
public interface SmsNotificationEvent extends NotificationEvent<IdlSmsNotificationEvent> {
	String getCellular();
}
