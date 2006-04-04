/*-
 * $Id: LineMismatchEventProcessor.java,v 1.27 2006/04/04 06:08:46 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import static com.syrus.AMFICOM.eventv2.EventType.LINE_MISMATCH;
import static com.syrus.AMFICOM.general.CharacteristicTypeCodenames.USER_EMAIL;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_TYPE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CODENAME;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort.AND;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;
import static java.util.logging.Level.FINEST;
import static java.util.logging.Level.SEVERE;

import java.util.Collections;
import java.util.HashSet;
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
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.Severity;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.27 $, $Date: 2006/04/04 06:08:46 $
 * @module leserver
 */
final class LineMismatchEventProcessor implements EventProcessor {
	private static Identifier characteristicTypeId = null;

	private static StorableObjectCondition condition = null;

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
		@SuppressWarnings("unchecked")
		final LineMismatchEvent lineMismatchEvent = (LineMismatchEvent) event;
		Log.debugMessage("LineMismatchEvent: "
				+ lineMismatchEvent + " started being processed",
				FINEST);

		try {
			final LEServerServantManager servantManager = LEServerSessionEnvironment.getInstance().getLEServerServantManager();
			final ORB orb = servantManager.getCORBAServer().getOrb();

			/*
			 * Cast here is ok: RME may not always be a storable
			 * object, but storable object pulled by id with an
			 * appropriate major is always a RME.
			 */
			final ReflectogramMismatchEvent reflectogramMismatchEvent =
					(ReflectogramMismatchEvent) StorableObjectPool.getStorableObject(lineMismatchEvent.getReflectogramMismatchEventId(), true);
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
				final EmailNotificationEvent emailNotificationEvent =
							DefaultEmailNotificationEvent.valueOf(
									address,
									severity.getLocalizedName(),
									lineMismatchEvent.getMessage());
				notificationEvents[i++] = emailNotificationEvent.getIdlTransferable(orb);
			}

			servantManager.getEventServerReference().receiveEvents(notificationEvents);
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
		}
	}

	private static Set<String> getAddresses(final Set<Identifier> systemUserIds) throws ApplicationException {
		if (systemUserIds.isEmpty()) {
			return Collections.emptySet();
		}

		final Set<Characteristic> characteristics = StorableObjectPool.getStorableObjectsByCondition(
				new CompoundCondition(
						getCondition(),
						AND,
						new LinkedIdsCondition(
								systemUserIds,
								CHARACTERISTIC_CODE)),
				true);

		final Set<String> addresses = new HashSet<String>();
		for (final Characteristic characteristic : characteristics) {
			addresses.add(characteristic.getValue());
		}
		return addresses;
	}

	private static StorableObjectCondition getCondition() {
		synchronized (LineMismatchEventProcessor.class) {
			if (condition == null) {
				condition = new LinkedIdsCondition(getCharacteristicTypeId(), CHARACTERISTIC_CODE);
			}
			return condition;
		}
	}

	private static Identifier getCharacteristicTypeId() {
		synchronized (LineMismatchEventProcessor.class) {
			if (characteristicTypeId == null) {
				try {
					final Set<CharacteristicType> characteristicTypes = StorableObjectPool.getStorableObjectsByCondition(
							new TypicalCondition(
									USER_EMAIL,
									OPERATION_EQUALS,
									CHARACTERISTIC_TYPE_CODE,
									COLUMN_CODENAME),
							true);
					assert characteristicTypes != null : NON_NULL_EXPECTED;
					final int size = characteristicTypes.size();
					assert size == 1 : size;
					characteristicTypeId = characteristicTypes.iterator().next().getId();
				} catch (final ApplicationException ae) {
					Log.debugMessage(ae, SEVERE);
					characteristicTypeId = VOID_IDENTIFIER;
		
					/*
					 * Never. But log the exception prior to issuing an
					 * eror.
					 */
					assert false;
				}
			}
			return characteristicTypeId;
		}
	}
}
