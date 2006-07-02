/*-
 * $Id: ChangeLogRecordTestCase.java,v 1.2 2006/07/02 18:45:42 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import static com.syrus.AMFICOM.eventv2.LineMismatchEventWrapper.COLUMN_ALARM_STATUS;
import static com.syrus.AMFICOM.eventv2.LineMismatchEventWrapper.COLUMN_PARENT_LINE_MISMATCH_EVENT_ID;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;

import junit.awtui.TestRunner;
import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.syrus.AMFICOM.eventv2.LineMismatchEvent.AlarmStatus;
import com.syrus.AMFICOM.general.StringToValueConverter;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2006/07/02 18:45:42 $
 * @module event
 */
public final class ChangeLogRecordTestCase extends TestCase {
	public ChangeLogRecordTestCase(final String method) {
		super(method);
	}

	public static void main(final String args[]) {
		TestRunner.main(new String[] {ChangeLogRecordTestCase.class.getName()});
	}

	public static Test suite() {
		final TestSuite testSuite = new TestSuite();
		testSuite.addTest(new EventHierarchyTestCase("testAssertionStatus"));
		testSuite.addTest(new ChangeLogRecordTestCase("testStringConversion"));
		return new TestSetup(testSuite);
	}

	private static void testStringConversion(final String key, final Object value) {
		final StringToValueConverter stringToValueConverter = LineMismatchEventWrapper.getInstance();
		assertEquals(value, stringToValueConverter.stringToValue(
				key,
				stringToValueConverter.valueToString(
						key,
						value)));
	}

	public void testStringConversion() {
		testStringConversion(COLUMN_PARENT_LINE_MISMATCH_EVENT_ID, VOID_IDENTIFIER);
		for (final AlarmStatus alarmStatus : AlarmStatus.values()) {
			testStringConversion(COLUMN_ALARM_STATUS, alarmStatus);
		}
	}
}
