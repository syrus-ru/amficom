/*-
 * $Id: EventUtilities.java,v 1.1 2006/06/16 14:53:33 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import static com.syrus.AMFICOM.eventv2.LineMismatchEventWrapper.COLUMN_ALARM_STATUS;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.LINEMISMATCHEVENT_CODE;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort.AND;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.eventv2.LineMismatchEvent.AlarmStatus;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2006/06/16 14:53:33 $
 * @module event
 */
public final class EventUtilities {
	private static final StorableObjectCondition ROOT_LINE_MISMATCH_EVENTS_CONDITION
			= new LinkedIdsCondition(VOID_IDENTIFIER, LINEMISMATCHEVENT_CODE);

	private static final Map<AlarmStatus, StorableObjectCondition> ROOT_LINE_MISMATCH_EVENTS_BY_STATUS_CONDITIONS;

	static {
		final Map<AlarmStatus, StorableObjectCondition> conditions
				= new EnumMap<AlarmStatus, StorableObjectCondition>(AlarmStatus.class);
		for (final AlarmStatus alarmStatus : AlarmStatus.values()) {
			conditions.put(alarmStatus, new CompoundCondition(
					ROOT_LINE_MISMATCH_EVENTS_CONDITION,
					AND,
					new TypicalCondition(
							alarmStatus,
							OPERATION_EQUALS,
							LINEMISMATCHEVENT_CODE,
							COLUMN_ALARM_STATUS)));
		}
		ROOT_LINE_MISMATCH_EVENTS_BY_STATUS_CONDITIONS = Collections.unmodifiableMap(conditions);
	}

	private EventUtilities() {
		assert false;
	}

	public static Set<? extends LineMismatchEvent> getRootLineMismatchEvents()
	throws ApplicationException {
		return StorableObjectPool.<AbstractLineMismatchEvent> getStorableObjectsByCondition(
				ROOT_LINE_MISMATCH_EVENTS_CONDITION,
				true);
	}

	public static Set<? extends LineMismatchEvent> getRootLineMismatchEvents(
			final AlarmStatus alarmStatus)
	throws ApplicationException {
		return StorableObjectPool.<AbstractLineMismatchEvent> getStorableObjectsByCondition(
				ROOT_LINE_MISMATCH_EVENTS_BY_STATUS_CONDITIONS.get(alarmStatus),
				true);
	}

	public static Set<? extends LineMismatchEvent> getRootLineMismatchEventsButIds(
			final Set<Identifier> ids)
	throws ApplicationException {
		return StorableObjectPool.<AbstractLineMismatchEvent> getStorableObjectsButIdsByCondition(
				ids,
				ROOT_LINE_MISMATCH_EVENTS_CONDITION,
				true);
	}

	public static Set<? extends LineMismatchEvent> getRootLineMismatchEventsButIds(
			final AlarmStatus alarmStatus,
			final Set<Identifier> ids)
	throws ApplicationException {
		return StorableObjectPool.<AbstractLineMismatchEvent> getStorableObjectsButIdsByCondition(
				ids,
				ROOT_LINE_MISMATCH_EVENTS_BY_STATUS_CONDITIONS.get(alarmStatus),
				true);
	}
}
