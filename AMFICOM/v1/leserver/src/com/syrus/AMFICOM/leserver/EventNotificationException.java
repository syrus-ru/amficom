/*
 * $Id: EventNotificationException.java,v 1.3 2005/08/08 11:42:21 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.leserver;

import com.syrus.AMFICOM.general.ApplicationException;

/**
 * @version $Revision: 1.3 $, $Date: 2005/08/08 11:42:21 $
 * @author $Author: arseniy $
 * @module leserver
 */
final class EventNotificationException extends ApplicationException {
	private static final long serialVersionUID = 3617295614439076146L;

	public EventNotificationException(final String message) {
		super(message);
	}

	public EventNotificationException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public EventNotificationException(final Throwable cause) {
		super(cause);
	}
}
