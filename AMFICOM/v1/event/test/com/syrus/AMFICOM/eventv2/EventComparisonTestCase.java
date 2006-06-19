/*-
 * $Id: EventComparisonTestCase.java,v 1.1 2006/06/19 15:54:12 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import static com.syrus.AMFICOM.eventv2.EventHierarchyTestCase.newLineMismatchEvent;
import static com.syrus.AMFICOM.eventv2.EventHierarchyTestCase.oneTimeSetUp;
import static com.syrus.AMFICOM.eventv2.EventHierarchyTestCase.oneTimeTearDown;
import static java.util.logging.Level.SEVERE;
import junit.awtui.TestRunner;
import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2006/06/19 15:54:12 $
 * @module event
 */
public final class EventComparisonTestCase extends TestCase {
	public EventComparisonTestCase(final String method) {
		super(method);
	}

	public static void main(final String args[]) {
		TestRunner.main(new String[] {EventComparisonTestCase.class.getName()});
	}

	public static Test suite() {
		final TestSuite testSuite = new TestSuite();
		testSuite.addTest(new EventHierarchyTestCase("testAssertionStatus"));
		testSuite.addTest(new EventComparisonTestCase("testSelfComparison"));
		testSuite.addTest(new EventComparisonTestCase("testLeaderNChildComparison"));
		testSuite.addTest(new EventComparisonTestCase("testLeaderNLeaderComparison"));
		testSuite.addTest(new EventComparisonTestCase("testChildNChildComparison"));
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

	public void testSelfComparison() {
		final LineMismatchEvent lineMismatchEvent = newLineMismatchEvent();
		assertEquals(0, lineMismatchEvent.compareTo(lineMismatchEvent));
	}

	public void testLeaderNChildComparison() throws ApplicationException {
		final LineMismatchEvent leader = newLineMismatchEvent();
		final LineMismatchEvent child = newLineMismatchEvent();
		child.setParentLineMismatchEvent(leader);
		assertTrue(leader.compareTo(child) < 0);
		assertTrue(child.compareTo(leader) > 0);
		assertTrue(leader.compareTo(child) + child.compareTo(leader) == 0);

		final LineMismatchEvent leader2 = newLineMismatchEvent();

		try {
			child.compareTo(leader2);
			fail();
		} catch (final IllegalArgumentException iae) {
			Log.debugMessage(iae.getLocalizedMessage(), SEVERE);
			assertTrue(true);
		}

		try {
			leader2.compareTo(child);
			fail();
		} catch (final IllegalArgumentException iae) {
			Log.debugMessage(iae.getLocalizedMessage(), SEVERE);
			assertTrue(true);
		}
	}

	public void testLeaderNLeaderComparison() {
		final LineMismatchEvent leader1 = newLineMismatchEvent();
		final LineMismatchEvent leader2 = newLineMismatchEvent();

		try {
			leader1.compareTo(leader2);
			fail();
		} catch (final IllegalArgumentException iae) {
			Log.debugMessage(iae.getLocalizedMessage(), SEVERE);
			assertTrue(true);
		}
	}

	public void testChildNChildComparison() throws ApplicationException {
		final LineMismatchEvent leader = newLineMismatchEvent();
		final LineMismatchEvent child1 = newLineMismatchEvent();
		final LineMismatchEvent child2 = newLineMismatchEvent();
		child1.setParentLineMismatchEvent(leader);
		child2.setParentLineMismatchEvent(leader);

		assertTrue(child1.compareTo(child2) != 0);
		assertTrue(child1.compareTo(child2) + child2.compareTo(child1) == 0);

		final LineMismatchEvent leader2 = newLineMismatchEvent();
		final LineMismatchEvent child3 = newLineMismatchEvent();
		child3.setParentLineMismatchEvent(leader2);

		try {
			child1.compareTo(child3);
			fail();
		} catch (final IllegalArgumentException iae) {
			Log.debugMessage(iae.getLocalizedMessage(), SEVERE);
			assertTrue(true);
		}
	}
}
