/*-
* $Id: PeriodicalTemporalPatternTestCase.java,v 1.1 2006/02/15 14:55:32 bob Exp $
*
* Copyright ¿ 2006 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.measurement;

import java.util.Calendar;
import java.util.Date;
import java.util.SortedSet;

import junit.framework.TestCase;


/**
 * @version $Revision: 1.1 $, $Date: 2006/02/15 14:55:32 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module measurement
 */
public class PeriodicalTemporalPatternTestCase extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(PeriodicalTemporalPatternTestCase.class);
	}

	public void testTimes() throws Exception {
		final PeriodicalTemporalPattern pattern = new PeriodicalTemporalPattern(null, null, null, 5 * 60 * 1000);
		final Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		final Date start = calendar.getTime();
		calendar.set(Calendar.MINUTE, 7);
		final Date startInterval = calendar.getTime();
		calendar.set(Calendar.MINUTE, 25);
		final Date end = calendar.getTime();
		calendar.set(Calendar.MINUTE, 18);
		final Date endInterval = calendar.getTime();
		final SortedSet<Date> times = pattern.getTimes(start, end, startInterval, endInterval);
		System.out.println("Start:" + start + ", end:" + end);
		System.out.println("StartInterval:" + startInterval + ", endInterval:" + endInterval);
		for (final Date date : times) {			
			System.out.println(date);
		}
		
		assertTrue(times.size() == 2);
		
		final long hour = 60L * 60L * 1000L;
		final PeriodicalTemporalPattern pattern1 = new PeriodicalTemporalPattern(null, null, null, hour);
		final Date now = new Date();
		final Date startDate = new Date(now.getTime() - 2L* hour);
		final Date endDate = new Date(now.getTime() + 2L* hour);		
		
		final Date startDate1 = new Date(now.getTime() - hour);
		final Date endDate1 = new Date(now.getTime() + hour);
		
		final SortedSet<Date> times1 = pattern1.getTimes(startDate, now, now, now);
		assertTrue(times1.size() == 1);
		
		final SortedSet<Date> times5 = pattern1.getTimes(startDate, endDate, startDate, endDate);
		assertTrue(times5.size() == 5);
		
		final SortedSet<Date> times3 = pattern1.getTimes(startDate, endDate, startDate1, endDate1);
		assertTrue(times3.size() == 3);
	}
	
}

