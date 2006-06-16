/*-
 * $Id: GroupLeaderRetrievalTestCase.java,v 1.2 2006/06/16 14:54:00 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import static com.syrus.AMFICOM.eventv2.EventHierarchyTestCase.newLineMismatchEvent;
import static com.syrus.AMFICOM.eventv2.EventHierarchyTestCase.oneTimeSetUp;
import static com.syrus.AMFICOM.eventv2.EventHierarchyTestCase.oneTimeTearDown;
import static com.syrus.AMFICOM.general.ObjectEntities.LINEMISMATCHEVENT_CODE;
import static java.util.logging.Level.SEVERE;

import java.lang.reflect.Constructor;
import java.util.EnumSet;
import java.util.Set;

import junit.awtui.TestRunner;
import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.syrus.AMFICOM.eventv2.LineMismatchEvent.AlarmStatus;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseTypicalCondition;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2006/06/16 14:54:00 $
 * @module event
 */
public final class GroupLeaderRetrievalTestCase extends TestCase {
	public GroupLeaderRetrievalTestCase(final String method) {
		super(method);
	}

	public static void main(final String args[]) {
		TestRunner.main(new String[] {GroupLeaderRetrievalTestCase.class.getName()});
	}

	public static Test suite() {
		final TestSuite testSuite = new TestSuite();
		testSuite.addTest(new EventHierarchyTestCase("testAssertionStatus"));
		testSuite.addTest(new GroupLeaderRetrievalTestCase("testTypicalCondition"));
		testSuite.addTest(new GroupLeaderRetrievalTestCase("testWholesaleRetrieval"));
		testSuite.addTest(new GroupLeaderRetrievalTestCase("testCustomRetrieval"));
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

	private static DatabaseTypicalCondition getDatabaseCondition(final TypicalCondition condition)
	throws Exception {
		final Class<? extends TypicalCondition> clazz = TypicalCondition.class;
		final Class<?> databaseClazz = Class.forName(clazz.getPackage().getName() + ".Database" + clazz.getSimpleName());
		final Constructor<?> ctor = databaseClazz.getDeclaredConstructor(TypicalCondition.class);
		ctor.setAccessible(true);
		return (DatabaseTypicalCondition) ctor.newInstance(condition);
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
}
