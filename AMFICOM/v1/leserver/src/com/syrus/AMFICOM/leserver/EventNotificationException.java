/*
 * $Id: EventNotificationException.java,v 1.1 2005/06/01 16:02:04 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.leserver;

import com.syrus.AMFICOM.general.ApplicationException;

/**
 * @version $Revision: 1.1 $, $Date: 2005/06/01 16:02:04 $
 * @author $Author: arseniy $
 * @module leserver_v1
 */
final class EventNotificationException extends ApplicationException {
	private static final long serialVersionUID = 3617295614439076146L;

	public EventNotificationException(String message) {
		super(message);
	}

	public EventNotificationException(String message, Throwable cause) {
		super(message, cause);
	}

	public EventNotificationException(Throwable cause) {
		super(cause);
	}
}
