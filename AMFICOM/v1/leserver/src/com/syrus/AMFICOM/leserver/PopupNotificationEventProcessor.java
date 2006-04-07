/*-
 * $Id: PopupNotificationEventProcessor.java,v 1.11.2.2 2006/04/07 10:04:23 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import static java.util.logging.Level.FINEST;
import static java.util.logging.Level.SEVERE;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.omg.CORBA.Object;
import org.omg.CORBA.SystemException;

import com.syrus.AMFICOM.eventv2.NotificationEvent;
import com.syrus.AMFICOM.eventv2.PopupNotificationEvent;
import com.syrus.AMFICOM.eventv2.corba.EventReceiver;
import com.syrus.AMFICOM.eventv2.corba.EventReceiverHelper;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.security.SessionKey;
import com.syrus.AMFICOM.security.UserLogin;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.11.2.2 $, $Date: 2006/04/07 10:04:23 $
 * @module leserver
 */
final class PopupNotificationEventProcessor extends AbstractNotificationEventProcessor {
	private final Map<SessionKey, ExecutorService> executors =
			Collections.synchronizedMap(new HashMap<SessionKey, ExecutorService>());

	PopupNotificationEventProcessor(final int capacity) {
		super(capacity);
	}

	PopupNotificationEventProcessor() {
		this(Integer.MAX_VALUE);
	}

	/**
	 * @param notificationEvent
	 * @see AbstractNotificationEventProcessor#processEvent(NotificationEvent)
	 */
	@Override
	void processEvent(final NotificationEvent<?> notificationEvent) {
		final long t0 = System.nanoTime();

		if (!(notificationEvent instanceof PopupNotificationEvent)) {
			return;
		}

		@SuppressWarnings("unchecked")
		final PopupNotificationEvent popupNotificationEvent = (PopupNotificationEvent) notificationEvent;

		Log.debugMessage(popupNotificationEvent
				+ " started being processed",
				FINEST);
		final Identifier targetUserId = popupNotificationEvent.getTargetUserId();
		Log.debugMessage("Message will be delivered to: " + targetUserId, FINEST);
		final CORBAServer corbaServer = LEServerSessionEnvironment.getInstance().getLEServerServantManager().getCORBAServer();
		for (final UserLogin userLogin : LoginProcessor.getUserLogins(targetUserId)) {
			final SessionKey sessionKey = userLogin.getSessionKey();
			ExecutorService executorService = this.executors.get(sessionKey);
			if (executorService == null) {
				executorService = Executors.newSingleThreadExecutor();
				this.executors.put(sessionKey, executorService);
			}
			executorService.submit(new Runnable() {
				public void run() {
					try {
						final Object object = corbaServer.stringToObject(userLogin.getUserIOR());
						if (!object._is_a(EventReceiverHelper.id())) {
							Log.debugMessage("Object: " + object + " is not an EventReceiver; skipping", FINEST);
							return;
						}
						final EventReceiver eventReceiver = EventReceiverHelper.narrow(object);
						eventReceiver.receiveEvent(popupNotificationEvent.getIdlTransferable(corbaServer.getOrb()));
						final long t1 = System.nanoTime();
						Log.debugMessage(popupNotificationEvent
								+ " delivered in "
								+ ((t1 - t0) / 1e9)
								+ " second(s)",
								FINEST);
					} catch (final SystemException se) {
						/**
						 * @todo catch COMM_FAILURE separately
						 * and request failed session removal.
						 */
						Log.debugMessage(se, SEVERE);
					} catch (final Throwable t) {
						/**
						 * @todo EventReceiver#receiveEvent(...)
						 * should't throw an ARE: there's
						 * no need to, and I'm unsure
						 * which specific handling there
						 * should be.
						 */
						Log.debugMessage(t, SEVERE);
					}
				}
			});
		}

		final long t1 = System.nanoTime();
		Log.debugMessage(popupNotificationEvent + " submitted in "
				+ ((t1 - t0) / 1e9) + " second(s); delivery pending",
				FINEST);
	}
}
