/*-
 * $Id: EventProcessor.java,v 1.8.2.1 2006/04/07 08:44:07 bass Exp $
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
 * @version $Revision: 1.8.2.1 $, $Date: 2006/04/07 08:44:07 $
 * @module leserver
 */
interface EventProcessor {
	EventType getEventType();

	/**
	 * @param event
	 */
	void processEvent(final Event<?> event);

	/**
	 * @param event
	 * @see java.util.concurrent.BlockingQueue#put(Object)
	 */
	void put(final Event<?> event);

	void putPoison();

	/**
	 * @see Thread#start()
	 */
	void start();

	/**
	 * @see Thread#join()
	 */
	void join() throws InterruptedException;

	/**
	 * @see Thread#getName()
	 */
	String getName();
}
