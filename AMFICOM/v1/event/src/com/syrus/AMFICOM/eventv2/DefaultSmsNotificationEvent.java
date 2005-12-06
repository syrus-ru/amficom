/*-
 * $Id: DefaultSmsNotificationEvent.java,v 1.2 2005/12/06 09:42:28 bass Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/12/06 09:42:28 $
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
	 * @see com.syrus.util.IdlTransferableObject#getIdlTransferable(ORB)
	 */
	public IdlSmsNotificationEvent getIdlTransferable(final ORB orb) {
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
