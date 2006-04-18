/*-
 * $Id: AveragingModeTestCase.java,v 1.1.2.1 2006/04/18 10:15:00 arseniy Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

/**
 * Тестирует {@link AveragingMode} и демонстрирует его использование.
 * @author saa
 * @author $Author: arseniy $
 * @version $Revision: 1.1.2.1 $, $Date: 2006/04/18 10:15:00 $
 * @module measurement
 */
public class AveragingModeTestCase extends TestCase {
	ExpectationChecker checker; // used in testAveragingState()

	/**
	 * Tests basic functionality of {@link AveragingMode}
	 */
	public void testAveraging() {
		System.out.println("testAveraging()");
		// TestCase should be completely repeatable, so we do not use current date
		Date date		= new GregorianCalendar(2005,11, 4,18,11,59).getTime();
		Date hourStart	= new GregorianCalendar(2005,11, 4,18,00,00).getTime();
		Date dayStart	= new GregorianCalendar(2005,11, 4,00,00,00).getTime();
		// Dec,4 is Sunday; Nov,28 is Monday of the same week in Russia
		Date weekStart	= new GregorianCalendar(2005,10,28,00,00,00).getTime();
		Date monthStart	= new GregorianCalendar(2005,11, 1,00,00,00).getTime();
		Date yearStart	= new GregorianCalendar(2005,00, 1,00,00,00).getTime();
		System.out.println("Current: " + date);
		for(AveragingMode mode: AveragingMode.values()) {
			final Date rangeStart = mode.getRangeStart(date);
			System.out.printf("%7s: %s\n", mode.toString(), rangeStart);
			if (mode == AveragingMode.HOURLY) {
				assertEquals(rangeStart, hourStart);
			} else if (mode == AveragingMode.DAYLY) {
				assertEquals(rangeStart, dayStart);
			} else if (mode == AveragingMode.WEEKLY) {
				assertEquals(rangeStart, weekStart);
			} else if (mode == AveragingMode.MONTHLY) {
				assertEquals(rangeStart, monthStart);
			} else if (mode == AveragingMode.YEARLY) {
				assertEquals(rangeStart, yearStart);
			} else {
				assertFalse(true);
			}
		}
	}

	/**
	 * Demo usage of {@link AveragingMode} and {@link AveragingState}
	 */
	public void testAveragingState() {
		System.out.println("testAveragingState()");

		AveragingState state = new AveragingState();

		AveragingState.AverageProcessor processor =
			new AveragingState.AverageProcessor() {
				public void processRange(Date start, AveragingMode mode) {
					final String string = "Processor invoked with mode " + mode
							+ " for range started on " + start;
					System.out.println(string);
					AveragingModeTestCase.this.checker.assertNext(string);
				}
		};

		// these are sample dates of measurements
		Date date1 = new GregorianCalendar(2005,11, 4,18,11,59).getTime();
		Date date2 = new GregorianCalendar(2005,11, 4,18,21,00).getTime(); // same hour
		Date date3 = new GregorianCalendar(2005,11, 4,19,01,12).getTime(); // next hour
		Date date4 = new GregorianCalendar(2005,11,14,19,01,12).getTime(); // 10 days ago
		Date[] dates = new Date[] { date1, date2, date3, date4 };

		// sample date for auto-closing
		Date dateA = new GregorianCalendar(2005,11,14,20,00,00).getTime();

		// test the actions taken under these conditions: auto-closing and opening new ranges
		this.checker = new ExpectationChecker(Arrays.asList(
				// opening and closing ranges when measurements ate obtaned
				"Sun Dec 04 18:11:59 MSK 2005 - some range(s) opened",
				"Sun Dec 04 19:01:12 MSK 2005 - state needs to process",
				"Processor invoked with mode HOURLY for range started on Sun Dec 04 18:00:00 MSK 2005",
				"Sun Dec 04 19:01:12 MSK 2005 - some range(s) closed",
				"Sun Dec 04 19:01:12 MSK 2005 - some range(s) opened",
				"Wed Dec 14 19:01:12 MSK 2005 - state needs to process",
				"Processor invoked with mode HOURLY for range started on Sun Dec 04 19:00:00 MSK 2005",
				"Processor invoked with mode DAYLY for range started on Sun Dec 04 00:00:00 MSK 2005",
				"Processor invoked with mode WEEKLY for range started on Mon Nov 28 00:00:00 MSK 2005",
				"Wed Dec 14 19:01:12 MSK 2005 - some range(s) closed",
				"Wed Dec 14 19:01:12 MSK 2005 - some range(s) opened"));
		for (Date date: dates) {
			ensureState(state, processor, date);
			if (state.notifyResultObtained(date)) {
				final String string = date.toString() + " - some range(s) opened";
				System.out.println(string);
				this.checker.assertNext(string);
			}
		}
		this.checker.assertNoMore();

		// test auto-closing (without auto-opening)
		this.checker = new ExpectationChecker(Arrays.asList(
				"Wed Dec 14 20:00:00 MSK 2005 - state needs to process",
				"Processor invoked with mode HOURLY for range started on Wed Dec 14 19:00:00 MSK 2005",
				"Wed Dec 14 20:00:00 MSK 2005 - some range(s) closed"));
		ensureState(state, processor, dateA);
		this.checker.assertNoMore();

		// test forces closing
		this.checker = new ExpectationChecker(Arrays.asList(
				"<null>",
				"Processor invoked with mode DAYLY for range started on Wed Dec 14 00:00:00 MSK 2005",
				"Processor invoked with mode WEEKLY for range started on Mon Dec 12 00:00:00 MSK 2005",
				"Processor invoked with mode MONTHLY for range started on Thu Dec 01 00:00:00 MSK 2005",
				"Processor invoked with mode YEARLY for range started on Sat Jan 01 00:00:00 MSK 2005",
				"<null>"));
		ensureState(state, processor, null);
		this.checker.assertNoMore();
	}

	private void ensureState(AveragingState state,
			AveragingState.AverageProcessor processor,
			Date date) {
		if (state.needsProcessing(date)) {
			final String string = date != null
					? date.toString() + " - state needs to process"
					: "<null>";
			System.out.println(string);
			this.checker.assertNext(string);
			if (state.processOpenedAverages(date, processor)) {
				final String string2 = date != null
						? date.toString() + " - some range(s) closed"
						: "<null>";
				System.out.println(string2);
				this.checker.assertNext(string2);
			}
		}
	}

	/**
	 * We way I see the usage of AveragingMode.
	 * Expected usage:
	 * <code>
	 * AveragingState state;
	 * private void ensureState(Date date) {
	 *     if (state.needsProcessing(date)) {
	 *     		... // prepare avrage processor
	 *     		if (state.processOpenedAverages(date, processor)) {
	 *              save(state); // some range(s) have closed 
	 *          }
	 *     }
	 * }
	 * void invokedSometimes() {
	 *     Date date = new Date();
	 *     ensureState(date);
	 * }
	 * void onResultObtained(Date date) {
	 *     ensureState(date);
	 *     if (state.notifyResultObtained(date)) {
	 *         save(state); // some new range(s) have opened
	 *     }
	 * }
	 * </code>
	 */
	private static class AveragingState {
		private Map<AveragingMode, Date> openAverage =
			new HashMap<AveragingMode, Date>();

		public static interface AverageProcessor {
			/**
			 * Would process all results since specified start date
			 * @param start
			 * @param mode
			 */
			void processRange(Date start, AveragingMode mode);
		}
		/**
		 * To to be invoked if needsProcessing() == true
		 * @param date Current date or null if need to forcely terminate all opened averages (when the test completes)
		 * @return true if averaging state has changed
		 */
		public boolean processOpenedAverages(Date date, AverageProcessor processor) {
			boolean changed = false;
			for(AveragingMode mode: AveragingMode.values()) {
				// If average if this mode has started
				//  AND (range has changed OR terminated forcely)
				// then process and close range.
				if (this.openAverage.containsKey(mode)) {
					if (date == null ||	!this.openAverage.get(mode).equals(
							mode.getRangeStart(date))) {
						changed = true;
						processor.processRange(this.openAverage.get(mode),
								mode); // process opened range
						this.openAverage.remove(mode); // close range
					}
				}
			}
			return changed;
		}

		/**
		 * Opens new ranges if required to cover the date given.
		 * Should be invoked when {@link #needsProcessing} returns false
		 * and new results have arrived.
		 * @return true if averaging state has changed
		 * @throws IllegalStateException if {@link #needsProcessing} is true
		 */
		public boolean notifyResultObtained(Date date) {
			boolean changed = false;
			for(AveragingMode mode: AveragingMode.values()) {
				final Date rangeStart = mode.getRangeStart(date);
				if (this.openAverage.containsKey(mode)) {
					// ensure the range is the same
					if (!this.openAverage.get(mode).equals(rangeStart))
						throw new IllegalStateException();
				} else {
					this.openAverage.put(mode, rangeStart);
					changed = true;
				}
			}
			return changed;
		}

		/**
		 * @param date current date or null, if need to close all opened
		 *   ranges (when test completes)
		 * @return true if one of opened ranges needs processing
		 */
		public boolean needsProcessing(Date date) {
			for(AveragingMode mode: AveragingMode.values()) {
				if (this.openAverage.containsKey(mode)) {
					if (date == null || !this.openAverage.get(mode).equals(
							mode.getRangeStart(date)))
						return true;
				}
			}
			return false;
		}
	}

	private static class ExpectationChecker {
		private List<String> expectation;
		private int position;
		public ExpectationChecker(List<String> expectation) {
			this.expectation = new ArrayList<String>(expectation);
			this.position = 0;
		}
		public void assertNext(String string) {
			assertEquals(string, this.expectation.get(this.position++));
		}
		public void assertNoMore() {
			assertEquals(this.position, this.expectation.size());
		}
	}
}
