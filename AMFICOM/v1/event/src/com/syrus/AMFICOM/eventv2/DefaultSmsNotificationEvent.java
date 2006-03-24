/*-
 * $Id: DefaultSmsNotificationEvent.java,v 1.3.2.2 2006/03/23 10:48:43 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.eventv2.corba.IdlSmsNotificationEvent;
import com.syrus.AMFICOM.eventv2.corba.IdlSmsNotificationEventHelper;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.util.transport.idl.IdlConversionException;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.3.2.2 $, $Date: 2006/03/23 10:48:43 $
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
			final IdlSmsNotificationEvent smsNotificationEvent)
	throws CreateObjectException {
		try {
			this.fromIdlTransferable(smsNotificationEvent);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
	}

	/**
	 * @param orb
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(ORB)
	 */
	public IdlSmsNotificationEvent getIdlTransferable(final ORB orb) {
		return IdlSmsNotificationEventHelper.init(orb, this.cellular,
				this.message);
	}

	public void fromIdlTransferable(
			final IdlSmsNotificationEvent smsNotificationEvent)
	throws IdlConversionException {
		synchronized (this) {
			this.cellular = smsNotificationEvent.getCellular();
			this.message = smsNotificationEvent.getMessage();
		}
	}

	public static SmsNotificationEvent valueOf(
			final IdlSmsNotificationEvent smsNotificationEvent)
	throws CreateObjectException {
		return new DefaultSmsNotificationEvent(smsNotificationEvent);
	}

	/**
	 * @see SmsNotificationEvent#getCellular()
	 */
	public String getCellular() {
		return this.cellular;
	}

	/**
	 * @see SmsNotificationEvent#getMessage()
	 */
	public String getMessage() {
		return this.message;
	}
}
