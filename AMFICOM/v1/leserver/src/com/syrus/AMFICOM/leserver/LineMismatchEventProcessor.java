/*-
 * $Id: LineMismatchEventProcessor.java,v 1.32 2006/07/03 06:26:11 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import static com.syrus.AMFICOM.eventv2.EventType.LINE_MISMATCH;
import static java.util.logging.Level.FINEST;
import static java.util.logging.Level.SEVERE;

import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.event.DeliveryAttributes;
import com.syrus.AMFICOM.eventv2.DefaultEmailNotificationEvent;
import com.syrus.AMFICOM.eventv2.DefaultPopupNotificationEvent;
import com.syrus.AMFICOM.eventv2.EmailNotificationEvent;
import com.syrus.AMFICOM.eventv2.Event;
import com.syrus.AMFICOM.eventv2.EventType;
import com.syrus.AMFICOM.eventv2.LineMismatchEvent;
import com.syrus.AMFICOM.eventv2.PopupNotificationEvent;
import com.syrus.AMFICOM.eventv2.ReflectogramMismatchEvent;
import com.syrus.AMFICOM.eventv2.corba.IdlEvent;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.Severity;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.32 $, $Date: 2006/07/03 06:26:11 $
 * @module leserver
 */
final class LineMismatchEventProcessor extends AbstractEventProcessor {
	LineMismatchEventProcessor(final int capacity) {
		super(capacity);
	}

	LineMismatchEventProcessor() {
		this(Integer.MAX_VALUE);
	}

	/**
	 * @see EventProcessor#getEventType()
	 */
	public EventType getEventType() {
		return LINE_MISMATCH;
	}

	/**
	 * @param event
	 * @see EventProcessor#processEvent(Event)
	 */
	public void processEvent(final Event<?> event) {
		final long t0 = System.nanoTime();

		@SuppressWarnings("unchecked")
		final LineMismatchEvent lineMismatchEvent = (LineMismatchEvent) event;
		Log.debugMessage("LineMismatchEvent: "
				+ lineMismatchEvent + " started being processed",
				FINEST);

		try {
			final LEServerServantManager servantManager = LEServerSessionEnvironment.getInstance().getLEServerServantManager();
			final ORB orb = servantManager.getCORBAServer().getOrb();

			final ReflectogramMismatchEvent reflectogramMismatchEvent =
					lineMismatchEvent.getReflectogramMismatchEvent();
			final Severity severity = reflectogramMismatchEvent.getSeverity();
			final Set<SystemUser> systemUsers = DeliveryAttributes
					.getInstance(LoginManager.getUserId(), severity)
					.getSystemUsersRecursively();
			final Set<Identifier> systemUserIds = Identifier.createIdentifiers(systemUsers);

			final Set<String> addresses = getAddresses(systemUserIds);

			final IdlEvent notificationEvents[] = new IdlEvent[systemUserIds.size() + addresses.size()];
			int i = 0;
			for (final Identifier systemUserId : systemUserIds) {
				final PopupNotificationEvent popupNotificationEvent =
						DefaultPopupNotificationEvent.valueOf(
								systemUserId,
								lineMismatchEvent);
				notificationEvents[i++] = popupNotificationEvent.getIdlTransferable(orb);
			}
			for (final String address : addresses) {
				final String subject = capitalizeFirstChar(
						severity.getLocalizedName()
						+ ": "
						+ reflectogramMismatchEvent.getAlarmType().getLocalizedName());
				final EmailNotificationEvent emailNotificationEvent =
							DefaultEmailNotificationEvent.valueOf(
									address,
									subject,
									lineMismatchEvent.getPlainTextMessage(),
									lineMismatchEvent.getRichTextMessage());
				notificationEvents[i++] = emailNotificationEvent.getIdlTransferable(orb);
			}

			servantManager.getEventServerReference().receiveEvents(notificationEvents);
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
		}

		final long t1 = System.nanoTime();
		Log.debugMessage(((t1 - t0) / 1e9) + " second(s)", FINEST);
	}

	private static String capitalizeFirstChar(final String s) {
		return s.length() > 0
				? Character.toUpperCase(s.charAt(0)) + s.substring(1)
				: s;
	}
}
