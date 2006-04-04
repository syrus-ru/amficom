/*-
 * $Id: EventProcessor.java,v 1.8 2006/04/04 06:08:46 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import com.syrus.AMFICOM.eventv2.Event;
import com.syrus.AMFICOM.eventv2.EventType;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.8 $, $Date: 2006/04/04 06:08:46 $
 * @module leserver
 */
interface EventProcessor {
	EventType getEventType();

	/**
	 * @param event
	 */
	void processEvent(final Event<?> event);
}
