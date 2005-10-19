/*-
 * $Id: LineMismatchEventProcessor.java,v 1.2 2005/10/19 13:50:15 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import static com.syrus.AMFICOM.eventv2.EventType.LINE_MISMATCH;
import static com.syrus.AMFICOM.general.ObjectEntities.SYSTEMUSER_CODE;

import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.eventv2.DefaultPopupNotificationEvent;
import com.syrus.AMFICOM.eventv2.Event;
import com.syrus.AMFICOM.eventv2.EventType;
import com.syrus.AMFICOM.eventv2.LineMismatchEvent;
import com.syrus.AMFICOM.eventv2.PopupNotificationEvent;
import com.syrus.AMFICOM.eventv2.corba.IdlEvent;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.leserver.corba.EventServerPackage.IdlEventProcessingException;
import com.syrus.util.Log;
import static java.util.logging.Level.*;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/10/19 13:50:15 $
 * @module leserver
 */
final class LineMismatchEventProcessor implements EventProcessor {
	/**
	 * @see EventProcessor#getEventType()
	 */
	public EventType getEventType() {
		return LINE_MISMATCH;
	}

	/**
	 * @param event
	 * @throws EventProcessingException
	 * @see EventProcessor#processEvent(Event)
	 */
	public void processEvent(final Event event) throws EventProcessingException {
		final LineMismatchEvent lineMismatchEvent = (LineMismatchEvent) event;
		Log.debugMessage("LineMismatchEventProcessor.processEvent() | LineMismatchEvent: "
				+ lineMismatchEvent + " started being processed",
				SEVERE);

		try {
			final LEServerServantManager servantManager = LEServerSessionEnvironment.getInstance().getLEServerServantManager();
			final ORB orb = servantManager.getCORBAServer().getOrb();

			final Set<SystemUser> systemUsers =
					StorableObjectPool.getStorableObjectsByCondition(
							new EquivalentCondition(SYSTEMUSER_CODE),
							true);
			final IdlEvent popupNotificationEvents[] = new IdlEvent[systemUsers.size()];
			int i = 0; 
			for (final SystemUser systemUser : systemUsers) {
				final PopupNotificationEvent popupNotificationEvent =
						DefaultPopupNotificationEvent.valueOf(
								lineMismatchEvent,
								systemUser.getId());
				popupNotificationEvents[i++] = popupNotificationEvent.getTransferable(orb);
			}

			servantManager.getEventServerReference().receiveEvents(popupNotificationEvents);
		} catch (final ApplicationException ae) {
			throw new EventProcessingException(ae);
		} catch (final IdlEventProcessingException epe) {
			/*
			 * Almost certainly, this will never happen, as the post
			 * office should't blame the reflectometer for users
			 * being unavailable ;-)
			 * 
			 *     -- Old Wise Saa.
			 */
			throw new EventProcessingException(epe.message, epe);
		}
	}
}
