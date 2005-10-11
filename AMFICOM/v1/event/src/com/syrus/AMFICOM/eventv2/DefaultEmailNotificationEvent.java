/*-
 * $Id: DefaultEmailNotificationEvent.java,v 1.1 2005/10/11 08:58:25 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.eventv2.corba.IdlEmailNotificationEvent;
import com.syrus.AMFICOM.eventv2.corba.IdlEmailNotificationEventHelper;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/10/11 08:58:25 $
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
