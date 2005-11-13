/*-
 * $Id: EventProcessor.java,v 1.7 2005/11/13 06:29:01 bass Exp $
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
 * @version $Revision: 1.7 $, $Date: 2005/11/13 06:29:01 $
 * @module leserver
 */
interface EventProcessor {
	EventType getEventType();

	/**
	 * @param event
	 * @throws EventProcessingException
	 */
	void processEvent(final Event<?> event) throws EventProcessingException;
}
