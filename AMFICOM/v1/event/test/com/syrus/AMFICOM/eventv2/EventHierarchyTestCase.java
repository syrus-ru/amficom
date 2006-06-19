/*-
 * $Id: EventHierarchyTestCase.java,v 1.3 2006/06/19 15:54:01 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PATHELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SYSTEMUSER_CODE;
import static com.syrus.AMFICOM.general.ObjectGroupEntities.EVENT_GROUP_CODE;
import static java.util.logging.Level.SEVERE;

import java.io.File;
import java.lang.reflect.Method;
import java.util.EnumSet;

import junit.awtui.TestRunner;
import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.syrus.AMFICOM.eventv2.LineMismatchEvent.AlarmStatus;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.IGSConnectionManager;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch;
import com.syrus.AMFICOM.reflectometry.SOAnchor;
import com.syrus.util.DefaultLogger;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2006/06/19 15:54:01 $
 * @module event
 */
public final class EventHierarchyTestCase extends TestCase {
	private static Identifier systemUserId;

	private static Identifier pathElementId;

	private static Identifier measurementId;

	private static final ReflectogramMismatch REFLECTOGRAM_MISMATCH = new ReflectogramMismatch() {
		public AlarmType getAlarmType() {
			return AlarmType.TYPE_UNDEFINED;
		}

		public int getAnchor1Coord() {
			throw new IllegalStateException();
		}

		public SOAnchor getAnchor1Id() {
			throw new IllegalStateException();
		}

		public int getAnchor2Coord() {
			throw new IllegalStateException();
		}

		public SOAnchor getAnchor2Id() {
			throw new IllegalStateException();
		}

		public int getCoord() {
			return 0;
		}

		public double getDeltaX() {
			return 0;
		}

		public double getDistance() {
			return 0;
		}

		public int getEndCoord() {
			return 0;
		}

		public double getMaxMismatch() {
			throw new IllegalStateException();
		}

		public double getMinMismatch() {
			throw new IllegalStateException();
		}

		public Severity getSeverity() {
			return Severity.SEVERITY_NONE;
		}

		public boolean hasAnchors() {
			return false;
		}

		public boolean hasMismatch() {
			return false;
		}
	};

	public EventHierarchyTestCase(final String method) {
		super(method);
	}

	public static void main(final String args[]) {
		TestRunner.main(new String[] {EventHierarchyTestCase.class.getName()});
	}

	public static Test suite() {
		final TestSuite testSuite = new TestSuite();
		testSuite.addTest(new EventHierarchyTestCase("testAssertionStatus"));
		testSuite.addTest(new EventHierarchyTestCase("testAllowedImproperUse"));
		testSuite.addTest(new EventHierarchyTestCase("testSelfSubmissionFailure"));
		testSuite.addTest(new EventHierarchyTestCase("testCircularSubmissionFailure"));
		testSuite.addTest(new EventHierarchyTestCase("testDetachmentFailure"));
		testSuite.addTest(new EventHierarchyTestCase("testReattachmentFailure"));
		testSuite.addTest(new EventHierarchyTestCase("testTwoLevelHierarchyFailure"));
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

	public static void oneTimeSetUp() {
		final String logDir = System.getProperty("java.io.tmpdir") + File.separatorChar
				+ ".amficom-" + System.getProperty("user.name");
		System.setProperty("amficom.logging.LogDebugLevel", "1");
		System.setProperty("amficom.logging.EchoDebug", Boolean.toString(true));
		System.setProperty("amficom.logging.LogPath", logDir);
		System.setProperty("amficom.logging.AllowLevelOutput", Boolean.toString(false));
		System.setProperty("amficom.logging.AllowAnsiColor", Boolean.toString(false));
		System.setProperty("amficom.logging.FullSte", Boolean.toString(false));
		System.setProperty("amficom.logging.StackTraceDataSource", "throwable");
		Log.setLogger(new DefaultLogger());

		StorableObjectPool.init(new EmptyEventObjectLoader());
		StorableObjectPool.addObjectPoolGroup(EVENT_GROUP_CODE, Integer.MAX_VALUE, Long.MAX_VALUE);

		final IdentifierGeneratorServer identifierGeneratorServer = new IdentifierGeneratorServer() {
			private long l;
			private static final long serialVersionUID = -9123341768454319489L;

			public IdlIdentifier getGeneratedIdentifier(short entity) {
				return this.getGeneratedIdentifierRange(entity, 1)[0];
			}

			public synchronized IdlIdentifier[] getGeneratedIdentifierRange(short entity, int size) {
				final IdlIdentifier ids[] = new IdlIdentifier[size];
				for (int i = 0; i < size; i++) {
					ids[i] = Identifier.valueOf(ObjectEntities.codeToString(entity) + '_' + this.l++).getIdlTransferable();
				}
				return ids;
			}
		};

		IdentifierPool.init(new IGSConnectionManager() {
			public IdentifierGeneratorServer getIGSReference() throws CommunicationException {
				return identifierGeneratorServer;
			}
		});
		systemUserId = newIdentifier(SYSTEMUSER_CODE);
		pathElementId = newIdentifier(PATHELEMENT_CODE);
		measurementId = newIdentifier(MEASUREMENT_CODE);
	}

	public static void oneTimeTearDown() {
		// empty
	}

	private static Identifier newIdentifier(final short entityCode) {
		try {
			return IdentifierPool.getGeneratedIdentifier(entityCode);
		} catch (final IdentifierGenerationException ige) {
			return Identifier.valueOf(ObjectEntities.codeToString(entityCode) + "_0");
		}
	}

	private static ReflectogramMismatchEvent newReflectogramMismatchEvent() {
		try {
			final ReflectogramMismatchEvent reflectogramMismatchEvent = DefaultReflectogramMismatchEvent.newInstance(systemUserId, REFLECTOGRAM_MISMATCH, measurementId);
			assertTrue(reflectogramMismatchEvent instanceof StorableObject);
			assertFalse(((StorableObject) reflectogramMismatchEvent).isChanged());
			final Method method = StorableObject.class.getDeclaredMethod("markAsChanged");
			method.setAccessible(true);
			method.invoke(reflectogramMismatchEvent);
			assertTrue(((StorableObject) reflectogramMismatchEvent).isChanged());
			return reflectogramMismatchEvent;
		} catch (final RuntimeException re) {
			throw re;
		} catch (final Error e) {
			throw e;
		} catch (final Exception e) {
			throw new Error(e);
		}
	}

	static LineMismatchEvent newLineMismatchEvent() {
		try {
			final LineMismatchEvent lineMismatchEvent = DefaultLineMismatchEvent.newInstance(systemUserId, pathElementId, false, 0, 0, 0, 0, "foo", "<html>foo</html>", newReflectogramMismatchEvent().getId());
			assertTrue(lineMismatchEvent instanceof StorableObject);
			/*
			 * Any new LineMismatchEvent gets saved right after creation.
			 */
			assertFalse(((StorableObject) lineMismatchEvent).isChanged());
			final AlarmStatus alarmStatus = lineMismatchEvent.getAlarmStatus();
			assertSame(AlarmStatus.PENDING, alarmStatus);
			final EnumSet<AlarmStatus> allowedSuccessors = alarmStatus.getAllowedSuccessors();
			assertFalse(allowedSuccessors.isEmpty());
			lineMismatchEvent.setAlarmStatus(allowedSuccessors.iterator().next());
			/*
			 * Ensure the object is changed now, so, despite we're using a
			 * fake loader, it doesn't get pushed out of the pool
			 */
			assertTrue(((StorableObject) lineMismatchEvent).isChanged());
			return lineMismatchEvent;
		} catch (final ApplicationException ae) {
			throw new Error(ae);
		}
	}

	public void testAssertionStatus() {
		try {
			assert false;
			fail();
		} catch (final AssertionError ae) {
			assertTrue(true);
		}
	}

	public void testAllowedImproperUse() throws ApplicationException {
		/*
		 * 1. Headless to headless, no children.
		 */
		final LineMismatchEvent headless = newLineMismatchEvent();
		headless.setParentLineMismatchEvent(null);
		assertTrue(true);

		final LineMismatchEvent leader = newLineMismatchEvent();
		final LineMismatchEvent child = headless;
		child.setParentLineMismatchEvent(leader);

		/*
		 * 2. Headless (leader) to headless, 1 child.
		 */
		leader.setParentLineMismatchEvent(null);
		assertTrue(true);

		/*
		 * 3. Subordinate to subordinate, no change of parent.
		 */
		child.setParentLineMismatchEvent(leader);
		assertTrue(true);
	}

	public void testSelfSubmissionFailure() throws ApplicationException {
		final LineMismatchEvent lineMismatchEvent = newLineMismatchEvent();
		try {
			lineMismatchEvent.setParentLineMismatchEvent(lineMismatchEvent);
			fail();
		} catch (final IllegalArgumentException iae) {
			Log.debugMessage(iae.getLocalizedMessage(), SEVERE);
			assertTrue(true);
		}
	}

	public void testCircularSubmissionFailure() throws ApplicationException {
		final LineMismatchEvent leader = newLineMismatchEvent();
		final LineMismatchEvent child = newLineMismatchEvent();
		child.setParentLineMismatchEvent(leader);

		try {
			leader.setParentLineMismatchEvent(child);
			fail();
		} catch (final IllegalArgumentException iae) {
			Log.debugMessage(iae.getLocalizedMessage(), SEVERE);
			assertTrue(true);
		}
	}

	public void testDetachmentFailure() throws ApplicationException {
		final LineMismatchEvent leader = newLineMismatchEvent();
		final LineMismatchEvent child = newLineMismatchEvent();
		child.setParentLineMismatchEvent(leader);

		try {
			child.setParentLineMismatchEvent(null);
			fail();
		} catch (final IllegalStateException ise) {
			Log.debugMessage(ise.getLocalizedMessage(), SEVERE);
			assertTrue(true);
		}
	}

	public void testReattachmentFailure() throws ApplicationException {
		final LineMismatchEvent oldLeader = newLineMismatchEvent();
		final LineMismatchEvent newLeader = newLineMismatchEvent();
		final LineMismatchEvent child = newLineMismatchEvent();
		child.setParentLineMismatchEvent(oldLeader);

		try {
			child.setParentLineMismatchEvent(newLeader);
			fail();
		} catch (final IllegalStateException ise) {
			Log.debugMessage(ise.getLocalizedMessage(), SEVERE);
			assertTrue(true);
		}
	}

	public void testTwoLevelHierarchyFailure() throws ApplicationException {
		final LineMismatchEvent superLeader = newLineMismatchEvent();
		final LineMismatchEvent leader = newLineMismatchEvent();
		final LineMismatchEvent child = newLineMismatchEvent();
		final LineMismatchEvent subChild = newLineMismatchEvent();
		child.setParentLineMismatchEvent(leader);

		try {
			subChild.setParentLineMismatchEvent(child);
			fail();
		} catch (final IllegalArgumentException iae) {
			Log.debugMessage(iae.getLocalizedMessage(), SEVERE);
			assertTrue(true);
		}

		try {
			leader.setParentLineMismatchEvent(superLeader);
			fail();
		} catch (final IllegalStateException ise) {
			Log.debugMessage(ise.getLocalizedMessage(), SEVERE);
			assertTrue(true);
		}
	}
}
