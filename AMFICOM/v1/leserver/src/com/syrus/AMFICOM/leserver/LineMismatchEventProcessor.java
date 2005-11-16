/*-
 * $Id: LineMismatchEventProcessor.java,v 1.8 2005/11/16 10:21:46 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import static com.syrus.AMFICOM.administration.SystemUserWrapper.COLUMN_SORT;
import static com.syrus.AMFICOM.administration.corba.IdlSystemUserPackage.SystemUserSort._USER_SORT_REGULAR;
import static com.syrus.AMFICOM.administration.corba.IdlSystemUserPackage.SystemUserSort._USER_SORT_SYSADMIN;
import static com.syrus.AMFICOM.eventv2.EventType.LINE_MISMATCH;
import static com.syrus.AMFICOM.general.ObjectEntities.SYSTEMUSER_CODE;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort.OR;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;
import static java.util.logging.Level.SEVERE;

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
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.leserver.corba.EventServerPackage.IdlEventProcessingException;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.8 $, $Date: 2005/11/16 10:21:46 $
 * @module leserver
 */
final class LineMismatchEventProcessor implements EventProcessor {
	private static final StorableObjectCondition CONDITION =
			new CompoundCondition(
					new TypicalCondition(
							_USER_SORT_SYSADMIN,
							OPERATION_EQUALS,
							SYSTEMUSER_CODE,
							COLUMN_SORT),
					OR,
					new TypicalCondition(
							_USER_SORT_REGULAR,
							OPERATION_EQUALS,
							SYSTEMUSER_CODE,
							COLUMN_SORT));

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
	public void processEvent(final Event<?> event) throws EventProcessingException {
		@SuppressWarnings("unchecked")
		final LineMismatchEvent lineMismatchEvent = (LineMismatchEvent) event;
		Log.debugMessage("LineMismatchEvent: "
				+ lineMismatchEvent + " started being processed",
				SEVERE);

		try {
			final LEServerServantManager servantManager = LEServerSessionEnvironment.getInstance().getLEServerServantManager();
			final ORB orb = servantManager.getCORBAServer().getOrb();

			/*@todo Instead of load all client users make real check of delivery attributes*/
			final Set<SystemUser> systemUsers =
					StorableObjectPool.getStorableObjectsByCondition(CONDITION, true);
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
