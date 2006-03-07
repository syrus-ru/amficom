/*-
 * $Id: PopupMessageReceiver.java,v 1.1 2005/10/12 12:21:41 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.event;

import com.syrus.AMFICOM.eventv2.Event;

/**
 * @version $Revision: 1.1 $, $Date: 2005/10/12 12:21:41 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module commonclient
 */
public interface PopupMessageReceiver {

	void receiveMessage(final Event event);
}
