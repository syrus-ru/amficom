/*-
 * $Id: AlarmStatusTestCase.java,v 1.1 2006/05/31 16:13:40 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import java.util.EnumSet;

import junit.framework.TestCase;
import junit.textui.TestRunner;

import com.syrus.AMFICOM.eventv2.LineMismatchEvent.AlarmStatus;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2006/05/31 16:13:40 $
 * @module event
 */
public final class AlarmStatusTestCase extends TestCase {
	public void testSequentialOrder() {
		for (final AlarmStatus alarmStatus : AlarmStatus.values()) {
			final EnumSet<AlarmStatus> allowedSuccessors = alarmStatus.getAllowedSuccessors();
			final EnumSet<AlarmStatus> disallowedSuccessors = EnumSet.complementOf(allowedSuccessors);

			assertFalse(allowedSuccessors.contains(alarmStatus));
			assertTrue(disallowedSuccessors.contains(alarmStatus));

			for (final AlarmStatus allowedSuccessor : allowedSuccessors) {
				assertTrue(alarmStatus.isAllowedPredecessorOf(allowedSuccessor));
			}
			for (final AlarmStatus disallowedSuccessor : disallowedSuccessors) {
				assertFalse(alarmStatus.isAllowedPredecessorOf(disallowedSuccessor));
			}
		}
	}

	public void testDebugInfo() {
		int i = 0;
		for (final AlarmStatus alarmStatus : AlarmStatus.values()) {
			for (int j = 0, n = i++; j < n; j++) {
				System.out.print("|\t");
			}
			System.out.println(alarmStatus);
		}
		for (final AlarmStatus successor : AlarmStatus.values()) {
			for (final AlarmStatus predecessor : AlarmStatus.values()) {
				System.out.print("" + (predecessor.isAllowedPredecessorOf(successor) ? '+' : '-') + '\t');
			}
			System.out.println(successor);
		}
	}

	/**
	 * @param args
	 */
	public static void main(final String args[]) {
		TestRunner.run(AlarmStatusTestCase.class);
	}
}
