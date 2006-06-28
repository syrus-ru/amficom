/*-
 * $Id: PopupNotificationEvent.java,v 1.7 2006/03/28 10:17:19 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import com.syrus.AMFICOM.eventv2.corba.IdlPopupNotificationEvent;
import com.syrus.AMFICOM.general.Identifier;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.7 $, $Date: 2006/03/28 10:17:19 $
 * @module event
 */
public interface PopupNotificationEvent extends NotificationEvent<IdlPopupNotificationEvent> {
	Identifier getTargetUserId();

	Identifier getLineMismatchEventId();
}
