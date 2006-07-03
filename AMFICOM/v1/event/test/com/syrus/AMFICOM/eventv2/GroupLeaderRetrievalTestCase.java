/*-
 * $Id: GroupLeaderRetrievalTestCase.java,v 1.6 2006/07/03 15:44:58 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import static com.syrus.AMFICOM.eventv2.EventHierarchyTestCase.newLineMismatchEvent;
import static com.syrus.AMFICOM.eventv2.EventHierarchyTestCase.oneTimeSetUp;
import static com.syrus.AMFICOM.eventv2.EventHierarchyTestCase.oneTimeTearDown;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.LINEMISMATCHEVENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.REFLECTOGRAMMISMATCHEVENT_CODE;
import static java.util.logging.Level.SEVERE;

import java.lang.reflect.Constructor;
import java.util.EnumSet;
import java.util.Set;

import junit.awtui.TestRunner;
import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.syrus.AMFICOM.bugs.Crutch581;
import com.syrus.AMFICOM.eventv2.LineMismatchEvent.AlarmStatus;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseStorableObjectCondition;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.AlarmType;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.Severity;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2006/07/03 15:44:58 $
 * @module event
 */
public final class GroupLeaderRetrievalTestCase extends TestCase {
	public GroupLeaderRetrievalTestCase(final String method) {
		super(method);
	}

	public static void main(final String args[]) {
		TestRunner.main(new String[] {GroupLeaderRetrievalTestCase.class.getName()});
	}

	@Crutch581(notes = "Re-enable testCustomRetrieval().")
	public static Test suite() {
		final TestSuite testSuite = new TestSuite();
		testSuite.addTest(new EventHierarchyTestCase("testAssertionStatus"));
		testSuite.addTest(new GroupLeaderRetrievalTestCase("testTypicalCondition"));
		testSuite.addTest(new GroupLeaderRetrievalTestCase("testWholesaleRetrieval"));
//		testSuite.addTest(new GroupLeaderRetrievalTestCase("testCustomRetrieval"));
		testSuite.addTest(new GroupLeaderRetrievalTestCase("testOtherConditions"));
		return new TestSetup(testSuite) {
			@Override
			protected void setUp() {
				oneTimeSetUp();
			}

			@Override
			protected void tearDown() {
				oneTimeTearDown();
			}
		};
	}

	private static DatabaseStorableObjectCondition getDatabaseCondition(final StorableObjectCondition condition)
	throws Exception {
		final Class<? extends StorableObjectCondition> clazz = condition.getClass();
		final Class<?> databaseClazz = Class.forName(clazz.getPackage().getName() + ".Database" + clazz.getSimpleName());
		final Constructor<?> ctor = databaseClazz.getDeclaredConstructor(clazz);
		ctor.setAccessible(true);
		return (DatabaseStorableObjectCondition) ctor.newInstance(condition);
	}

	public void testTypicalCondition() throws Exception {
		final LineMismatchEvent lineMismatchEvent = newLineMismatchEvent();
		final AlarmStatus alarmStatus = lineMismatchEvent.getAlarmStatus();
		final TypicalCondition condition1 = new TypicalCondition(
				alarmStatus,
				OperationSort.OPERATION_EQUALS,
				LINEMISMATCHEVENT_CODE,
				LineMismatchEventWrapper.COLUMN_ALARM_STATUS);
		final TypicalCondition condition2 = new TypicalCondition(
				alarmStatus.ordinal(),
				OperationSort.OPERATION_EQUALS,
				LINEMISMATCHEVENT_CODE,
				LineMismatchEventWrapper.COLUMN_ALARM_STATUS_INT);
		final Set<StorableObject> lineMismatchEvents
				= StorableObjectPool.getStorableObjectsByCondition(
						condition1,
						true);
		assertTrue(lineMismatchEvents.equals(
				StorableObjectPool.getStorableObjectsByCondition(
						condition2,
						true)));
		assertTrue(lineMismatchEvents.contains(lineMismatchEvent));
		try {
			Log.debugMessage(getDatabaseCondition(condition1).getSQLQuery(), SEVERE);
			assertTrue(true);
		} catch (final AssertionError ae) {
			Log.debugMessage(ae.getLocalizedMessage(), SEVERE);
			fail();
		}
		try {
			Log.debugMessage(getDatabaseCondition(condition2).getSQLQuery(), SEVERE);
			fail();
		} catch (final AssertionError ae) {
			Log.debugMessage(ae.getLocalizedMessage(), SEVERE);
			assertTrue(true);
		}
	}

	public void testWholesaleRetrieval() throws ApplicationException {
		final LineMismatchEvent leader1 = newLineMismatchEvent();
		final LineMismatchEvent leader2 = newLineMismatchEvent();
		final LineMismatchEvent child = newLineMismatchEvent();
		child.setParentLineMismatchEvent(leader1);

		final Set<? extends LineMismatchEvent> rootLineMismatchEvents
				= EventUtilities.getRootLineMismatchEvents();
		assertTrue(rootLineMismatchEvents.contains(leader1));
		assertTrue(rootLineMismatchEvents.contains(leader2));
		assertFalse(rootLineMismatchEvents.contains(child));
	}

	@Crutch581(notes = "This test currently fails.")
	public void testCustomRetrieval() throws ApplicationException {
		final LineMismatchEvent leader1 = newLineMismatchEvent();
		final LineMismatchEvent leader2 = newLineMismatchEvent();
		final LineMismatchEvent child = newLineMismatchEvent();
		child.setParentLineMismatchEvent(leader1);

		final AlarmStatus oldAlarmStatus = leader1.getAlarmStatus();
		assertTrue(leader2.getAlarmStatus() == oldAlarmStatus);
		assertTrue(child.getAlarmStatus() == oldAlarmStatus);

		final EnumSet<AlarmStatus> allowedSuccessors = oldAlarmStatus.getAllowedSuccessors();
		assertFalse(allowedSuccessors.isEmpty());

		final AlarmStatus newAlarmStatus = allowedSuccessors.iterator().next();

		leader1.setAlarmStatus(newAlarmStatus);
		assertTrue(child.getAlarmStatus() == newAlarmStatus);

		final Set<? extends LineMismatchEvent> rootLineMismatchEvents1
				= EventUtilities.getRootLineMismatchEvents(newAlarmStatus);
		assertTrue(rootLineMismatchEvents1.contains(leader1));
		assertFalse(rootLineMismatchEvents1.contains(leader2));
		assertFalse(rootLineMismatchEvents1.contains(child));

		final Set<? extends LineMismatchEvent> rootLineMismatchEvents2
				= EventUtilities.getRootLineMismatchEvents(oldAlarmStatus);
		assertFalse(rootLineMismatchEvents2.contains(leader1));
		assertTrue(rootLineMismatchEvents2.contains(leader2));
		assertFalse(rootLineMismatchEvents2.contains(child));
	}

	public void testOtherConditions() throws Exception {
		final LineMismatchEvent lineMismatchEvent = newLineMismatchEvent();
		final ReflectogramMismatchEvent reflectogramMismatchEvent = lineMismatchEvent.getReflectogramMismatchEvent();

		final Severity severity = reflectogramMismatchEvent.getSeverity();
		final TypicalCondition condition1 = new TypicalCondition(
				severity,
				OperationSort.OPERATION_EQUALS,
				REFLECTOGRAMMISMATCHEVENT_CODE,
				ReflectogramMismatchEventWrapper.COLUMN_SEVERITY);
		final TypicalCondition condition2 = new TypicalCondition(
				severity.ordinal(),
				OperationSort.OPERATION_EQUALS,
				REFLECTOGRAMMISMATCHEVENT_CODE,
				ReflectogramMismatchEventWrapper.COLUMN_SEVERITY_INT);
		try {
			Log.debugMessage(getDatabaseCondition(condition1).getSQLQuery(), SEVERE);
			assertTrue(true);
		} catch (final AssertionError ae) {
			Log.debugMessage(ae.getLocalizedMessage(), SEVERE);
			fail();
		}
		try {
			Log.debugMessage(getDatabaseCondition(condition2).getSQLQuery(), SEVERE);
			fail();
		} catch (final AssertionError ae) {
			Log.debugMessage(ae.getLocalizedMessage(), SEVERE);
			assertTrue(true);
		}

		final AlarmType alarmType = reflectogramMismatchEvent.getAlarmType();
		final TypicalCondition condition3 = new TypicalCondition(
				alarmType,
				OperationSort.OPERATION_EQUALS,
				REFLECTOGRAMMISMATCHEVENT_CODE,
				ReflectogramMismatchEventWrapper.COLUMN_ALARM_TYPE);
		final TypicalCondition condition4 = new TypicalCondition(
				alarmType.ordinal(),
				OperationSort.OPERATION_EQUALS,
				REFLECTOGRAMMISMATCHEVENT_CODE,
				ReflectogramMismatchEventWrapper.COLUMN_ALARM_TYPE_INT);
		try {
			Log.debugMessage(getDatabaseCondition(condition3).getSQLQuery(), SEVERE);
			assertTrue(true);
		} catch (final AssertionError ae) {
			Log.debugMessage(ae.getLocalizedMessage(), SEVERE);
			fail();
		}
		try {
			Log.debugMessage(getDatabaseCondition(condition4).getSQLQuery(), SEVERE);
			fail();
		} catch (final AssertionError ae) {
			Log.debugMessage(ae.getLocalizedMessage(), SEVERE);
			assertTrue(true);
		}

		final TypicalCondition condition5 = new TypicalCondition(.0d, 1.0d,
				OperationSort.OPERATION_IN_RANGE,
				LINEMISMATCHEVENT_CODE,
				LineMismatchEventWrapper.COLUMN_MISMATCH_OPTICAL_DISTANCE);
		try {
			Log.debugMessage(getDatabaseCondition(condition5).getSQLQuery(), SEVERE);
			assertTrue(true);
		} catch (final AssertionError ae) {
			Log.debugMessage(ae.getLocalizedMessage(), SEVERE);
			fail();
		}

		final LinkedIdsCondition condition6 = new LinkedIdsCondition(
				lineMismatchEvent.getAffectedPathElementId(),
				LINEMISMATCHEVENT_CODE);
		assertTrue(condition6.isConditionTrue((StorableObject) lineMismatchEvent));
		try {
			Log.debugMessage(getDatabaseCondition(condition6).getSQLQuery(), SEVERE);
			assertTrue(true);
		} catch (final AssertionError ae) {
			Log.debugMessage(ae.getLocalizedMessage(), SEVERE);
			fail();
		}

		final LinkedIdsCondition condition7 = new LinkedIdsCondition(
				lineMismatchEvent.getReflectogramMismatchEventId(),
				LINEMISMATCHEVENT_CODE);
		assertTrue(condition7.isConditionTrue((StorableObject) lineMismatchEvent));
		try {
			Log.debugMessage(getDatabaseCondition(condition7).getSQLQuery(), SEVERE);
			assertTrue(true);
		} catch (final AssertionError ae) {
			Log.debugMessage(ae.getLocalizedMessage(), SEVERE);
			fail();
		}

		final LinkedIdsCondition condition8 = new LinkedIdsCondition(
				VOID_IDENTIFIER,
				LINEMISMATCHEVENT_CODE);
		try {
			Log.debugMessage(getDatabaseCondition(condition8).getSQLQuery(), SEVERE);
			assertTrue(true);
		} catch (final AssertionError ae) {
			Log.debugMessage(ae.getLocalizedMessage(), SEVERE);
			fail();
		}
	}
}
