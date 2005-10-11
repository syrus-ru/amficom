/*-
 * $Id: DefaultSmsNotificationEvent.java,v 1.1 2005/10/11 08:58:25 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.eventv2.corba.IdlSmsNotificationEvent;
import com.syrus.AMFICOM.eventv2.corba.IdlSmsNotificationEventHelper;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/10/11 08:58:25 $
 * @module event
 */
public final class DefaultSmsNotificationEvent extends
		AbstractSmsNotificationEvent {
	private static final long serialVersionUID = 4952154893053173010L;

	/**
	 * @serial include 
	 */
	private String cellular;

	/**
	 * @serial include 
	 */
	private String message;

	private DefaultSmsNotificationEvent(
			final IdlSmsNotificationEvent smsNotificationEvent) {
		this.cellular = smsNotificationEvent.getCellular();
		this.message = smsNotificationEvent.getMessage();
	}

	/**
	 * @param orb
	 * @see com.syrus.util.TransferableObject#getTransferable(ORB)
	 */
	public IdlSmsNotificationEvent getTransferable(final ORB orb) {
		return IdlSmsNotificationEventHelper.init(orb, this.cellular,
				this.message);
	}

	public static SmsNotificationEvent valueOf(
			final IdlSmsNotificationEvent smsNotificationEvent) {
		return new DefaultSmsNotificationEvent(smsNotificationEvent);
	}

	/**
	 * @see SmsNotificationEvent#getCellular()
	 */
	public String getCellular() {
		return this.cellular;
	}

	/**
	 * @see NotificationEvent#getMessage()
	 */
	public String getMessage() {
		return this.message;
	}
}
