/*-
 * $Id: EmailNotificationEvent.java,v 1.4.4.1 2006/06/07 09:07:09 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import com.syrus.AMFICOM.eventv2.corba.IdlEmailNotificationEvent;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.4.4.1 $, $Date: 2006/06/07 09:07:09 $
 * @module event
 */
public interface EmailNotificationEvent extends NotificationEvent<IdlEmailNotificationEvent> {
	String getEmail();

	String getSubject();

	String getPlainTextMessage();

	String getRichTextMessage();
}
