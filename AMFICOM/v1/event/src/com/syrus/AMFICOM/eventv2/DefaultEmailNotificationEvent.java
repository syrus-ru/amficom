/*-
 * $Id: DefaultEmailNotificationEvent.java,v 1.2 2005/11/17 16:22:31 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_EMPTY_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.eventv2.corba.IdlEmailNotificationEvent;
import com.syrus.AMFICOM.eventv2.corba.IdlEmailNotificationEventHelper;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/11/17 16:22:31 $
 * @module event
 */
public final class DefaultEmailNotificationEvent extends
		AbstractEmailNotificationEvent {
	private static final long serialVersionUID = -3744378922196321279L;

	/**
	 * @serial include 
	 */
	private String email;

	/**
	 * @serial include 
	 */
	private String subject;

	/**
	 * @serial include 
	 */
	private String message;

	private DefaultEmailNotificationEvent(
			final IdlEmailNotificationEvent emailNotificationEvent) {
		this.email = emailNotificationEvent.getEmail();
		this.subject = emailNotificationEvent.getSubject();
		this.message = emailNotificationEvent.getMessage();
	}

	private DefaultEmailNotificationEvent(
			final LineMismatchEvent lineMismatchEvent,
			final String address) {
		assert address != null : NON_NULL_EXPECTED;
		assert address.length() != 0 : NON_EMPTY_EXPECTED;

		this.email = address;
		this.subject = lineMismatchEvent.getSeverity().getLocalizedName();
		this.message = lineMismatchEvent.getMessage();
	}

	/**
	 * @param orb
	 * @see com.syrus.util.TransferableObject#getTransferable(ORB)
	 */
	public IdlEmailNotificationEvent getTransferable(final ORB orb) {
		return IdlEmailNotificationEventHelper.init(orb, this.email,
				this.subject, this.message);
	}

	public static EmailNotificationEvent valueOf(
			final IdlEmailNotificationEvent emailNotificationEvent) {
		return new DefaultEmailNotificationEvent(emailNotificationEvent);
	}

	public static EmailNotificationEvent valueOf(
			final LineMismatchEvent lineMismatchEvent,
			final String address) {
		return new DefaultEmailNotificationEvent(lineMismatchEvent, address);
	}

	/**
	 * @see EmailNotificationEvent#getEmail()
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * @see EmailNotificationEvent#getSubject()
	 */
	public String getSubject() {
		return this.subject;
	}

	/**
	 * @see NotificationEvent#getMessage()
	 */
	public String getMessage() {
		return this.message;
	}
}
