/*-
 * $Id: DefaultEmailNotificationEvent.java,v 1.7 2006/05/18 19:37:22 bass Exp $
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
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.util.transport.idl.IdlConversionException;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.7 $, $Date: 2006/05/18 19:37:22 $
 * @module event
 */
public final class DefaultEmailNotificationEvent extends
		AbstractEmailNotificationEvent {
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
	private String plainTextMessage;

	/**
	 * @serial include
	 */
	private String richTextMessage;

	/**
	 * @param emailNotificationEvent
	 * @throws CreateObjectException
	 */
	private DefaultEmailNotificationEvent(
			final IdlEmailNotificationEvent emailNotificationEvent)
	throws CreateObjectException {
		try {
			this.fromIdlTransferable(emailNotificationEvent);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
	}

	private DefaultEmailNotificationEvent(final String address,
			final String subject,
			final String plainTextMessage,
			final String richTextMessage) {
		assert address != null : NON_NULL_EXPECTED;
		assert address.length() != 0 : NON_EMPTY_EXPECTED;

		this.email = address;
		this.subject = subject;
		this.plainTextMessage = plainTextMessage;
		this.richTextMessage = richTextMessage;
	}

	/**
	 * @param orb
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(ORB)
	 */
	public IdlEmailNotificationEvent getIdlTransferable(final ORB orb) {
		return IdlEmailNotificationEventHelper.init(orb, this.email,
				this.subject, this.plainTextMessage,
				this.richTextMessage);
	}

	public void fromIdlTransferable(final IdlEmailNotificationEvent emailNotificationEvent)
	throws IdlConversionException {
		synchronized (this) {
			this.email = emailNotificationEvent.getEmail();
			this.subject = emailNotificationEvent.getSubject();
			this.plainTextMessage = emailNotificationEvent.getPlainTextMessage();
			this.richTextMessage = emailNotificationEvent.getRichTextMessage();
		}
	}

	public static EmailNotificationEvent valueOf(
			final IdlEmailNotificationEvent emailNotificationEvent)
	throws CreateObjectException {
		return new DefaultEmailNotificationEvent(emailNotificationEvent);
	}

	public static EmailNotificationEvent valueOf(final String address,
			final String subject,
			final String plainTextMessage,
			final String richTextMessage) {
		return new DefaultEmailNotificationEvent(address, subject, plainTextMessage, richTextMessage);
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
	 * @see EmailNotificationEvent#getPlainTextMessage()
	 */
	public String getPlainTextMessage() {
		return this.plainTextMessage;
	}

	/**
	 * @see EmailNotificationEvent#getRichTextMessage()
	 */
	public String getRichTextMessage() {
		return this.richTextMessage;
	}
}
