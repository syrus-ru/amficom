/*-
 * $Id: PopupNotificationEvent.java,v 1.4 2005/10/19 07:50:31 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import java.util.Date;

import com.syrus.AMFICOM.eventv2.corba.IdlPopupNotificationEvent;
import com.syrus.AMFICOM.general.Identifier;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/10/19 07:50:31 $
 * @module event
 */
public interface PopupNotificationEvent extends NotificationEvent<IdlPopupNotificationEvent> {
	Identifier getTargetUserId();

	Identifier getResultId();

	/**
	 * @return optical distance from the mismatch event to the start of the
	 *         reflectogram.
	 */
	double getMismatchOpticalDistance();

	/**
	 * @return physical distance from the mismatch event to the start of the
	 *         reflectogram.
	 */
	double getMismatchPhysicalDistance();

	/**
	 * @return the date when mismatch event was created, that triggered
	 *         generation of this notification event. 
	 */
	Date getMismatchCreated();
}
