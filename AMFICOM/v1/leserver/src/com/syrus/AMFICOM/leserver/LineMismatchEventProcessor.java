/*-
 * $Id: LineMismatchEventProcessor.java,v 1.10 2005/11/18 15:13:49 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import static com.syrus.AMFICOM.administration.SystemUserWrapper.COLUMN_LOGIN;
import static com.syrus.AMFICOM.administration.SystemUserWrapper.LOGINPROCESSOR_LOGIN;
import static com.syrus.AMFICOM.eventv2.EventType.LINE_MISMATCH;
import static com.syrus.AMFICOM.general.CharacteristicTypeCodenames.USER_EMAIL;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SYSTEMUSER_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CODENAME;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort.AND;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;
import static java.util.logging.Level.FINEST;
import static java.util.logging.Level.SEVERE;

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
import com.syrus.AMFICOM.eventv2.corba.IdlEvent;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.leserver.corba.EventServerPackage.IdlEventProcessingException;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.10 $, $Date: 2005/11/18 15:13:49 $
 * @module leserver
 */
final class LineMismatchEventProcessor implements EventProcessor {
	private static Identifier creatorId = null;

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
	 * @throws EventProcessingException
	 * @see EventProcessor#processEvent(Event)
	 */
	public void processEvent(final Event<?> event) throws EventProcessingException {
		@SuppressWarnings("unchecked")
		final LineMismatchEvent lineMismatchEvent = (LineMismatchEvent) event;
		Log.debugMessage("LineMismatchEvent: "
				+ lineMismatchEvent + " started being processed",
				FINEST);

		try {
			final LEServerServantManager servantManager = LEServerSessionEnvironment.getInstance().getLEServerServantManager();
			final ORB orb = servantManager.getCORBAServer().getOrb();

			final Set<SystemUser> systemUsers = DeliveryAttributes
					.getInstance(getCreatorId(), lineMismatchEvent.getSeverity())
					.getSystemUsersRecursively();
			final Set<Identifier> systemUserIds = Identifier.createIdentifiers(systemUsers);

			final Set<String> addresses = getAddresses(systemUserIds);

			final IdlEvent notificationEvents[] = new IdlEvent[systemUserIds.size() + addresses.size()];
			int i = 0;
			for (final Identifier systemUserId : systemUserIds) {
				final PopupNotificationEvent popupNotificationEvent =
						DefaultPopupNotificationEvent.valueOf(
								lineMismatchEvent,
								systemUserId);
				notificationEvents[i++] = popupNotificationEvent.getTransferable(orb);
			}
			for (final String address : addresses) {
				final EmailNotificationEvent emailNotificationEvent =
							DefaultEmailNotificationEvent.valueOf(
									lineMismatchEvent,
									address);
				notificationEvents[i++] = emailNotificationEvent.getTransferable(orb);
			}

			servantManager.getEventServerReference().receiveEvents(notificationEvents);
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

	private Set<String> getAddresses(final Set<Identifier> systemUserIds) throws ApplicationException {
		final StorableObjectCondition characteristicCondition;
		if (systemUserIds.isEmpty()) {
			characteristicCondition = getCondition();
		} else {
			characteristicCondition = new CompoundCondition(
					getCondition(),
					AND,
					new LinkedIdsCondition(
							systemUserIds,
							CHARACTERISTIC_CODE));
		}
		final Set<Characteristic> characteristics = StorableObjectPool.getStorableObjectsByCondition(characteristicCondition, true);
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

	private static Identifier getCreatorId() {
		synchronized (LineMismatchEventProcessor.class) {
			if (creatorId == null) {
				try {
					final Set<SystemUser> systemUsers = StorableObjectPool.getStorableObjectsByCondition(
							new TypicalCondition(
									LOGINPROCESSOR_LOGIN,
									OPERATION_EQUALS,
									SYSTEMUSER_CODE,
									COLUMN_LOGIN),
							true);
					assert systemUsers != null : NON_NULL_EXPECTED;
					final int size = systemUsers.size();
					assert size == 1 : size;
					creatorId = systemUsers.iterator().next().getId();
				} catch (final ApplicationException ae) {
					Log.debugMessage(ae, SEVERE);
					creatorId = VOID_IDENTIFIER;
		
					/*
					 * Never. But log the exception prior to issuing an
					 * eror.
					 */
					assert false;
				}
			}
			return creatorId;
		}
	}
}
