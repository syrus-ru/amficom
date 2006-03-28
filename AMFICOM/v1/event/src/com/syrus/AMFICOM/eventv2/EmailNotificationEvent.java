/*-
 * $Id: EmailNotificationEvent.java,v 1.4 2006/03/28 10:17:19 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import com.syrus.AMFICOM.eventv2.corba.IdlEmailNotificationEvent;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2006/03/28 10:17:19 $
 * @module event
 */
public interface EmailNotificationEvent extends NotificationEvent<IdlEmailNotificationEvent> {
	String getEmail();

	String getSubject();

	String getMessage();
}
