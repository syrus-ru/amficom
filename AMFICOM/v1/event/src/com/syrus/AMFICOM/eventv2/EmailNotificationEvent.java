/*-
 * $Id: EmailNotificationEvent.java,v 1.5 2006/05/18 19:37:22 bass Exp $
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
 * @version $Revision: 1.5 $, $Date: 2006/05/18 19:37:22 $
 * @module event
 */
public interface EmailNotificationEvent extends NotificationEvent<IdlEmailNotificationEvent> {
	String getEmail();

	String getSubject();

	String getPlainTextMessage();

	String getRichTextMessage();
}
