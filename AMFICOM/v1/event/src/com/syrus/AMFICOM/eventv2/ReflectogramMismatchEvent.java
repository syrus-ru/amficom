/*-
 * $Id: ReflectogramMismatchEvent.java,v 1.6.4.1 2006/03/21 13:46:43 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import java.util.Date;

import com.syrus.AMFICOM.eventv2.corba.IdlReflectogramMismatchEvent;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.6.4.1 $, $Date: 2006/03/21 13:46:43 $
 * @module event
 */
public interface ReflectogramMismatchEvent
		extends Event<IdlReflectogramMismatchEvent>,
		ReflectogramMismatch {
	Date getCreated();

	/**
	 * Nullable if {@code Result} referenced is deleted; a void identifier
	 * is returned in this case.
	 *
	 * @see PopupNotificationEvent#getResultId()
	 */
	Identifier getResultId();

	/**
	 * Nullable if {@code MonitoredElement} referenced is deleted; a void
	 * identifier is returned in this case.
	 */
	Identifier getMonitoredElementId();
}
