/*-
 * $Id: EventReceiver.java,v 1.1 2006/03/01 20:46:53 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.event;

import com.syrus.AMFICOM.eventv2.Event;

/**
 * This is a Java counterpart for
 * {@link com.syrus.AMFICOM.eventv2.corba.EventReceiverOperations}.
 *
 * @version $Revision: 1.1 $, $Date: 2006/03/01 20:46:53 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module commonclient
 */
public interface EventReceiver {
	void receiveEvent(final Event<?> event);
}
