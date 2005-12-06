/*-
 * $Id: LineMismatchEventProcessor.java,v 1.18 2005/12/06 09:43:07 bass Exp $
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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

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
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.util.EasyDateFormatter;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.18 $, $Date: 2005/12/06 09:43:07 $
 * @module leserver
 */
final class LineMismatchEventProcessor implements EventProcessor {
	/*-********************************************************************
	 * String constants & i18n.                                           *
	 **********************************************************************/

	private static final char NEWLINE = '\n';

	private static final char SPACE = ' ';

	private static final String COLON_TAB = ":\t";

	private static final String RANGE = " .. ";

	private static final String PHYSICAL_DISTANCE_TO = I18N.getString("NotificationEvent.PhysicalDistanceTo");

	private static final String END_OF = I18N.getString("NotificationEvent.EndOf");

	private static final String START_OF = I18N.getString("NotificationEvent.StartOf");

	private static final String PATH_ELEMENT_GENITIVE = I18N.getString("NotificationEvent.PathElementGenitive");

	private static final String PATH_ELEMENT = I18N.getString("NotificationEvent.PathElement");

	private static final String AFFECTED = I18N.getString("NotificationEvent.Affected");

	private static final String MISMATCH_LEVEL = I18N.getString("NotificationEvent.MismatchLevel");

	private static final String MISMATCH_CREATED = I18N.getString("NotificationEvent.MismatchCreated");


	private static final String METER_SINGULAR_NOMINATIVE = I18N.getString("NotificationEvent.MeterSingularNominative");

	private static final String METER_SINGULAR_GENITIVE = I18N.getString("NotificationEvent.MeterSingularGenitive");

	private static final String METER_PLURAL_GENITIVE = I18N.getString("NotificationEvent.MeterPluralGenitive");


	private static final Pattern METER_SINGULAR_NOMINATIVE_REGEXP = Pattern.compile("([0-9]*[^1])?1");

	private static final Pattern METER_SINGULAR_GENITIVE_REGEXP = Pattern.compile("([0-9]*[^1])?[2-4]");

	private static final Pattern METER_PLURAL_GENITIVE_REGEXP = Pattern.compile("(([0-9]*[^1])?[0,5-9]|[0-9]*1[0-9])");


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
								createMessage(lineMismatchEvent),
								systemUserId);
				notificationEvents[i++] = popupNotificationEvent.getIdlTransferable(orb);
			}
			for (final String address : addresses) {
				final EmailNotificationEvent emailNotificationEvent =
							DefaultEmailNotificationEvent.valueOf(
									lineMismatchEvent,
									address,
									createMessage(lineMismatchEvent));
				notificationEvents[i++] = emailNotificationEvent.getIdlTransferable(orb);
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

	/**
	 * @return a plain-text, human-readable, localized representaion of
	 *         the {@link ReflectogramMismatch mismatch} that triggerred
	 *         this event's generation.
	 */
	private static String createMessage(final LineMismatchEvent lineMismatchEvent) {
		return MISMATCH_CREATED + COLON_TAB + EasyDateFormatter.formatDate(lineMismatchEvent.getMismatchCreated())
				+ NEWLINE
				+ lineMismatchEvent.getSeverity().getLocalizedDescription() + NEWLINE
				+ lineMismatchEvent.getAlarmType().getLocalizedDescription() + NEWLINE
				+ NEWLINE
				+ AFFECTED + SPACE + PATH_ELEMENT + COLON_TAB + getExtendedName(lineMismatchEvent.getAffectedPathElementId()) + NEWLINE
				+ (lineMismatchEvent.isAffectedPathElementSpacious()
						? PHYSICAL_DISTANCE_TO + SPACE + START_OF + SPACE + PATH_ELEMENT_GENITIVE + COLON_TAB + getLocalizedDistance((int) lineMismatchEvent.getPhysicalDistanceToStart()) + NEWLINE
						+ PHYSICAL_DISTANCE_TO + SPACE + END_OF + SPACE + PATH_ELEMENT_GENITIVE + COLON_TAB + getLocalizedDistance((int) lineMismatchEvent.getPhysicalDistanceToEnd()) + NEWLINE
						: "")
				+ NEWLINE
				+ (lineMismatchEvent.hasMismatch()
						? MISMATCH_LEVEL + COLON_TAB + lineMismatchEvent.getMinMismatch() + RANGE + lineMismatchEvent.getMaxMismatch() + NEWLINE
						: "");
	}

	private static String getLocalizedDistance(final int distance) {
		assert distance >= 0 : distance;

		final String stringDistance = Integer.toString(distance);
		if (METER_SINGULAR_NOMINATIVE_REGEXP.matcher(stringDistance).matches()) {
			return stringDistance + SPACE + METER_SINGULAR_NOMINATIVE;
		} else if (METER_SINGULAR_GENITIVE_REGEXP.matcher(stringDistance).matches()) {
			return stringDistance + SPACE + METER_SINGULAR_GENITIVE;
		} else if (METER_PLURAL_GENITIVE_REGEXP.matcher(stringDistance).matches()) {
			return stringDistance + SPACE + METER_PLURAL_GENITIVE;
		} else {
			/*
			 * Never.
			 */
			assert false : stringDistance;
			return null;
		}
	}

	/**
	 * @param pathElementId
	 * @return the name of the PathElement in the following form:
	 *         &quot;&lt;name of the path element&gt; (&lt;name of the scheme
	 *         that contains it&gt;)&quot;
	 */
	private static String getExtendedName(final Identifier pathElementId) {
		try {
			final PathElement pathElement = StorableObjectPool
					.getStorableObject(pathElementId, true);
			if (pathElement == null) {
				/*
				 * We don't check all database data for integrity;
				 * however incoming data (i.e. pathElementId) may be
				 * invalid. 
				 */
				Log.debugMessage("PathElement identified by "
						+ pathElementId + " is null",
						SEVERE);
				return "";
			}

			return pathElement.getName() + SPACE + '('
					+ pathElement.getAbstractSchemeElement()
					.getNearestParentScheme().getName() + ')';
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
			return "";
		}
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
