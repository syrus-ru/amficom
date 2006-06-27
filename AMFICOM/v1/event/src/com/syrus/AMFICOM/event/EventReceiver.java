/*-
 * $Id: EventReceiver.java,v 1.1.2.1 2006/06/27 15:41:02 arseniy Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.event;

import com.syrus.AMFICOM.eventv2.Event;

/**
 * This is a Java counterpart for
 * {@link com.syrus.AMFICOM.eventv2.corba.EventReceiverOperations}.
 *
 * @version $Revision: 1.1.2.1 $, $Date: 2006/06/27 15:41:02 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module event
 */
public interface EventReceiver {
	void receiveEvent(final Event<?> event);
}
