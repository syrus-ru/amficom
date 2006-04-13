/*-
 * $Id: TimeLabelTestCase.java,v 1.1.4.1 2006/04/13 12:49:44 saa Exp $
 * 
 * Copyright © 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.validator;

import junit.framework.TestCase;

/**
 * @author $Author: saa $
 * @version $Revision: 1.1.4.1 $, $Date: 2006/04/13 12:49:44 $
 * @module
 */
public class TimeLabelTestCase extends TestCase {
	public void testRange() {
		Range range1 = new Range(2,3);
		Range range2 = new Range(3,3);
		Range range3 = new Range(0,10);
		Range range4 = new Range(4,5);

		assertTrue(range1.intersects(range1));
		assertTrue(range2.intersects(range2));

		assertTrue(range1.intersects(range2));
		assertTrue(range2.intersects(range1));

		assertTrue(range1.intersects(range3));
		assertTrue(range3.intersects(range1));

		assertFalse(range1.intersects(range4));
		assertFalse(range4.intersects(range1));

		assertTrue(range3.intersects(range4));
		assertTrue(range4.intersects(range3));
	}

	private void assertIntersect(TimeLabel p1, TimeLabel p2,
			boolean result) {
		assertEquals(p1.intersects(p2), result);
		assertEquals(p2.intersects(p1), result);
		assertEquals(TimeLabel.patternsIntersect(p1,p2), result);
	}

	public void testPeriodicalTemporalPattern1() {
		OnceTimeLabel once1 = new OnceTimeLabel(new Range(-10, 3));
		OnceTimeLabel once2 = new OnceTimeLabel(new Range(-3, -3));
		OnceTimeLabel once3 = new OnceTimeLabel(new Range(-2, -2));
		OnceTimeLabel once4 = new OnceTimeLabel(new Range(-1, -1));
		OnceTimeLabel once5 = new OnceTimeLabel(new Range(5, 5));
		OnceTimeLabel once6 = new OnceTimeLabel(new Range(6, 6));
		OnceTimeLabel once7 = new OnceTimeLabel(new Range(7, 7));
		OnceTimeLabel once8 = new OnceTimeLabel(new Range(9, 9));
		OnceTimeLabel once9 = new OnceTimeLabel(new Range(10, 10));
		OnceTimeLabel once10= new OnceTimeLabel(new Range(11, 11));
		OnceTimeLabel once11= new OnceTimeLabel(new Range(-5, -5));
		OnceTimeLabel once12= new OnceTimeLabel(new Range(-6, -6));
		OnceTimeLabel once13= new OnceTimeLabel(new Range(-7, -7));
		OnceTimeLabel once14= new OnceTimeLabel(new Range(-7, 10));
		PeriodicalTimeLabel period1 = new PeriodicalTimeLabel(
				-5, 2, 4, 4); // -5..-3, -1..1, 3..5, 7..9

		assertIntersect(once1, period1, true);
		assertIntersect(once2, period1, true);
		assertIntersect(once3, period1, false);
		assertIntersect(once4, period1, true);
		assertIntersect(once5, period1, true);
		assertIntersect(once6, period1, false);
		assertIntersect(once7, period1, true);
		assertIntersect(once8, period1, true);
		assertIntersect(once9, period1, false);
		assertIntersect(once10, period1, false);
		assertIntersect(once11, period1, true);
		assertIntersect(once12, period1, false);
		assertIntersect(once13, period1, false);
		assertIntersect(once14, period1, true);
	}

	public void testPeriodicalTemporalPattern2() {
		PeriodicalTimeLabel period1 = new PeriodicalTimeLabel(
				0, 1, 10, 4); // 0-1, 10-11, 20-21, 30-31
		PeriodicalTimeLabel period2 = new PeriodicalTimeLabel(
				1, 1, 10, 4); // 1-2, 11-12, 21-22, 31-32
		PeriodicalTimeLabel period3 = new PeriodicalTimeLabel(
				2, 1, 10, 4); // 2-3, 12-13, 22-23, 32-33

		PeriodicalTimeLabel period4 = new PeriodicalTimeLabel(
				2, 1, 8, 4); // 2-3, 10-11, 18-19, 26-27

		PeriodicalTimeLabel period5 = new PeriodicalTimeLabel(
				3, 1, 7, 4); // 3-4, 10-11, 16-17, 24-25

		PeriodicalTimeLabel period6 = new PeriodicalTimeLabel(
				2, 1, 6, 4); // 2-3, 8-9, 14-15, 20-21

		PeriodicalTimeLabel period7 = new PeriodicalTimeLabel(
				2, 1, 6, 3); // 2-3, 8-9, 14-15, but not 20-21

		PeriodicalTimeLabel period8 = new PeriodicalTimeLabel(
				3, 1, 6, 3); // 3-4, 9-10, 15-16, but not 21-22

		PeriodicalTimeLabel period9 = new PeriodicalTimeLabel(
				0, 1, 6, 0); // no at all

		assertIntersect(period1, period1, true);
		assertIntersect(period2, period2, true);
		assertIntersect(period3, period3, true);

		assertIntersect(period1, period2, true);
		assertIntersect(period2, period3, true);
		assertIntersect(period1, period3, false);

		assertIntersect(period4, period1, true);
		assertIntersect(period4, period2, true);
		assertIntersect(period4, period3, true);

		assertIntersect(period5, period1, true);
		assertIntersect(period5, period2, true);
		assertIntersect(period5, period3, true);

		assertIntersect(period6, period1, true);
		assertIntersect(period6, period2, true);
		assertIntersect(period6, period3, true);

		assertIntersect(period7, period1, false);
		assertIntersect(period7, period2, true);

		assertIntersect(period8, period1, true);
		assertIntersect(period8, period2, false);

		assertIntersect(period9, period1, false);
	}
}
