/*
 * $Id: TestLoadTiming2.java,v 1.3 2006/01/20 17:08:24 saa Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.profiling;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBACommonTest;
import com.syrus.AMFICOM.general.CommonTest;
import com.syrus.AMFICOM.general.CorbaTestContext;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.DatabaseTestContext;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.UniCommonTest;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Result;

/**
 * @version $Revision: 1.3 $, $Date: 2006/01/20 17:08:24 $
 * @author $Author: saa $
 * @module test
 */
public final class TestLoadTiming2 extends TestCase {

	private static final long testIdLong = 146085512912831939L;

	private final ExpectationRecord[] expectationsDb = new ExpectationRecord[] {
			// NB: the order does matter
			// Values are based on suiteDb() results on saa's computer
			new ExpectationRecord(new ProfiledGetTest(),        			1,   1,  40.0,	0, 0), // first=390.0?
			new ExpectationRecord(new ProfiledGetTest(),        			1,   1, 0.033,	0, 0),
			new ExpectationRecord(new ProfiledGetMeasurements(),			1, 118, 120.0,	0, 0),
			new ExpectationRecord(new ProfiledGetMeasurements(), 			1, 118,   6.0,	0, 0),
			new ExpectationRecord(new ProfiledGetOneResultByMeasurement(),	2,   1,	120.0,120,120),
			new ExpectationRecord(new ProfiledGetOneResultByMeasurement(),	2,   1,	 85.0, 85,85),
			new ExpectationRecord(new ProfiledGetManyResultsByMeasurement(),1, 236, 3700 ,	0, 0),
			new ExpectationRecord(new ProfiledGetManyResultsByMeasurement(),1, 236, 340.0,	0, 0),
		};

	private final ExpectationRecord[] expectationsCorba = new ExpectationRecord[] {
			// NB: the order does matter
			// Values are based on suiteDb() results on saa's computer
			new ExpectationRecord(new ProfiledGetTest(),        			1,   1,  40.0,	0, 0), // first=390.0?
			new ExpectationRecord(new ProfiledGetTest(),        			1,   1, 0.033,	0, 0),
			new ExpectationRecord(new ProfiledGetMeasurements(),			1, 118, 120.0,	0, 0),
			new ExpectationRecord(new ProfiledGetMeasurements(), 			1, 118,   6.0,	0, 0),
			new ExpectationRecord(new ProfiledGetOneResultByMeasurement(),	2,   1,	120.0,120,120),
			new ExpectationRecord(new ProfiledGetOneResultByMeasurement(),	2,   1,	 85.0, 85,85),
			new ExpectationRecord(new ProfiledGetManyResultsByMeasurement(),1, 236, 3700 ,	0, 0),
			new ExpectationRecord(new ProfiledGetManyResultsByMeasurement(),1, 236, 340.0,	0, 0),
		};
	
	Measurement[] measurements = null;

	public TestLoadTiming2(final String name) {
		super(name);
	}

	public static Test suite() {
		return suiteUni();
	}

	public static Test suiteUni() {
		final CommonTest commonTest = new UniCommonTest();
		commonTest.addTestSuite(TestLoadTiming2.class);
		return commonTest.createTestSetup();
	}

	public static Test suiteDb() {
		final CommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(TestLoadTiming2.class);
		return commonTest.createTestSetup();
	}

	public static Test suiteCorba() {
		final CommonTest commonTest = new CORBACommonTest();
		commonTest.addTestSuite(TestLoadTiming2.class);
		return commonTest.createTestSetup();
	}

	/**
	 * holds timing.
	 * count - число испытаний
	 * size - условный объем каждого испытания (число объектов)
	 * total - полное время, мс
	 * first - время первого замера, мс
	 * min - минимальное время, мс
	 */
	private static class TimingStat {
		private int count;
		private int size;
		private double total;
		private double first;
		private double min;
		private TimingStat(int count, int size,
				double total, double first, double min) {
			this.count = count;
			this.size = size;
			this.total = total;
			this.first = first;
			this.min = min;
		}
		public static TimingStat fromNs(int count, int size,
				long totalNs, long firstNS, long minNs) {
			return new TimingStat(count, size,
					totalNs / 1e6, firstNS / 1e6, minNs / 1e6);
		}

		public int getCount() {
			return this.count;
		}
		public double getTotal() {
			return this.total;
		}
		public double getWholeAverage() {
			return this.total / this.count;
		}
		public double getButFirstAverage() {
			return (this.total - this.first) / (this.count - 1);
		}
		public double getMin() {
			return this.min;
		}
		public double getFirst() {
			return this.first;
		}
		public int getSize() {
			return this.size;
		}
	}

	private interface ProfiledMethod { // is not static
		TimingStat profile() throws ApplicationException;
	}

	private class ProfiledGetTest implements ProfiledMethod {
		public TimingStat profile() throws ApplicationException {
			long t0 = System.nanoTime();
			Identifier id = Identifier.valueOf(testIdLong);
			long t1 = System.nanoTime();
			StorableObjectPool.getStorableObject(id, true);
			long t2 = System.nanoTime();
			System.out.println("Id:    " + (t1 - t0) / 1e6);
			System.out.println("getSO: " + (t2 - t1) / 1e6);
			return TimingStat.fromNs(1, 1, t2 - t0, t2 - t0, t2 - t0);
		}
	}

	private class ProfiledGetMeasurements implements ProfiledMethod {
		public TimingStat profile() throws ApplicationException {
			long t0 = System.nanoTime();
			Identifier id = Identifier.valueOf(testIdLong);
			LinkedIdsCondition lic =
				new LinkedIdsCondition(id, ObjectEntities.MEASUREMENT_CODE);
			long t1 = System.nanoTime();
			Set<Measurement> set =
				StorableObjectPool.getStorableObjectsByCondition(lic, true);
			long t2 = System.nanoTime();
			TestLoadTiming2.this.measurements = set.toArray(new Measurement[set.size()]);
			long t3 = System.nanoTime();
			int count = set.size();
			System.out.println("lic: " + (t1 - t0) / 1e6 + " ms; "
					+ "getSOByC: " + (t2 - t1) / 1e6 + " ms; "
					+ "toArray(*): " + (t3 - t2) / 1e6 + " ms; "
					+ "size = " + count);
			return TimingStat.fromNs(1, count, t2 - t0, t2 - t0, t2 - t0);
		}
	}

	private class ProfiledGetOneResultByMeasurement implements ProfiledMethod {
		public TimingStat profile()
				throws ApplicationException {
			int m0 = 0;
			long m1 = 0;
			long minTime = 0;
			long firstTime = 0;
			int sumSetSize = 0;
			boolean first = true;
			for(Measurement m: TestLoadTiming2.this.measurements) {
				long t0 = System.nanoTime();
				LinkedIdsCondition lic =
					new LinkedIdsCondition(m.getId(), ObjectEntities.RESULT_CODE);
//				long t1 = System.nanoTime();
				Set<Result> set =
					StorableObjectPool.getStorableObjectsByCondition(lic, true);
				long t2 = System.nanoTime();
				if (m.getId().getIdentifierCode() == 145241087982705227L)
					System.out.println("set.size for m.id " + m.getId() + "(" + m.getId().getIdentifierCode() + "): " + set.size());
				sumSetSize += set.size();
				long dt = t2 - t0;
				if (first) {
					minTime = dt;
					firstTime = dt;
					first = false;
				} else {
					if (minTime > dt)
						minTime = dt;
					m0++;
					m1 += dt;
					break; // do not try 3rd object
				}
			}
			System.out.println("sum of set.size(): " + sumSetSize);
			return TimingStat.fromNs(m0 + 1, 1, firstTime + m1, firstTime, minTime);
		}
	}

	private class ProfiledGetManyResultsByMeasurement implements ProfiledMethod {
		public TimingStat profile()
				throws ApplicationException {
			System.out.println("In: " + new Date());
			long t0 = System.nanoTime();
			Set<Measurement> mset = new HashSet<Measurement>(
					Arrays.asList(TestLoadTiming2.this.measurements));
			LinkedIdsCondition lic =
				new LinkedIdsCondition(Identifier.createIdentifiers(mset),
						ObjectEntities.RESULT_CODE);
			long t1 = System.nanoTime();
			Set<Result> set =
				StorableObjectPool.getStorableObjectsByCondition(lic, true);
			long t2 = System.nanoTime();
			int setSize = set.size();
			long dt = t2 - t0;
			System.out.println("Out: " + new Date());
			System.out.println("lic: " + (t1 - t0) / 1e6 + " ms; "
					+ "getSObyC: " + (t2 - t1) / 1e6 + " ms");
			System.out.println("sum of set.size(): " + setSize);
			return TimingStat.fromNs(1, setSize, dt, dt, dt);
		}
	}

	private class ExpectationRecord {
		public ProfiledMethod profiler;
		public TimingStat expectation;
		protected ExpectationRecord(ProfiledMethod profiler,
				int expectedCount, int expectedSize,
				double expectedFirst, double expectedBfAvg, double expectedMin) {
			this.profiler = profiler;
			this.expectation = new TimingStat(expectedCount, expectedSize,
					expectedBfAvg * (expectedCount - 1) + expectedFirst,
					expectedFirst,
					expectedMin);
		}
	}

	@SuppressWarnings("boxing")
	private static void printValueComparison(double value, double vsValue) {
		if (value < 999.99)
			System.out.printf("%7.3f ", value);
		else
			System.out.printf("%6.0f  ", value);
		if (vsValue > 0) {
			System.out.printf("( %5.1f%% )  ", value / vsValue * 100.0);
		} else {
			System.out.printf("            ");
		}
	}

	private void operation1() throws ApplicationException {
		final LinkedIdsCondition condition = new LinkedIdsCondition(
				Identifier.valueOf(145241087982705227L),
				ObjectEntities.RESULT_CODE);
		Set set = StorableObjectPool.getStorableObjectsByCondition(condition, true);
		System.out.println("test1()| set.size = " + set.size());
	}

	/**
	 * Just an operability test
	 */
	public void test1() throws ApplicationException {
		UniCommonTest.use(new DatabaseTestContext());
		operation1();
		UniCommonTest.use(new CorbaTestContext());
		operation1();
	}

	@SuppressWarnings("boxing")
	private void operationLoadResult(ExpectationRecord[] expectations)
	throws ApplicationException {
		StorableObjectPool.clean();
		TimingStat[] measured = new TimingStat[expectations.length];
		System.out.println(" --- starting measurments ---");
		System.gc();
		for (int i = 0; i < expectations.length; i++) {
			System.out.println("profiling " + expectations[i].profiler.getClass());
			measured[i] = expectations[i].profiler.profile();
		}
		System.out.println(" --- printing results ---");
		System.out.println("-------count--------  ---firstTime/ms---  -bFAvgTime/req/ms-  -minimTime/req/ms-  ---totalTime/ms---  ---action---");
		for (int i = 0; i < expectations.length; i++) {
			final TimingStat expected = expectations[i].expectation;
			final int expCount = expected.getCount();
			final int expSize = expected.getSize();
			final int mCount = measured[i].getCount();
			final int mSize = measured[i].getSize();
			System.out.printf("%6s ( vs %6s )  ",
					"" + mCount + ":" + mSize,
					"" + expCount + ":" + expSize);
			printValueComparison(measured[i].getFirst(), expected.getFirst());
			if (mCount > 1 && expCount > 1) {
				printValueComparison(measured[i].getButFirstAverage() / mSize,
						expected.getButFirstAverage() / expSize);
			} else {
				// have no BF data
				System.out.printf("%18s  ", "");
			}
			printValueComparison(measured[i].getMin() / mSize, expected.getMin() / expSize);
			printValueComparison(measured[i].getTotal(), expected.getTotal());
			System.out.printf("%s\n",
					expectations[i].profiler.getClass().getSimpleName());
		}
		System.out.flush();
	}

	public void testLoadResult() throws ApplicationException {
		UniCommonTest.use(new DatabaseTestContext());
		System.out.println("==== Using DB loader ====");
		operationLoadResult(expectationsDb);
		UniCommonTest.use(new CorbaTestContext());
		System.out.println("==== Using Corba loader ====");
		operationLoadResult(expectationsCorba);
	}
}
