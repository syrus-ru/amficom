/*
 * $Id: TestLoadTiming.java,v 1.4 2006/06/06 15:52:38 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.profiling;

import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Parameter;
import com.syrus.AMFICOM.measurement.Result;

/**
 * @version $Revision: 1.4 $, $Date: 2006/06/06 15:52:38 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestLoadTiming extends TestCase {

	static String[] analysisIdNames = {
		"Analysis_62",
		"Analysis_63",
		"Analysis_64",
		"Analysis_65",
		"Analysis_66"
	};

	private Identifier[] analysisIds;

	public TestLoadTiming(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(TestLoadTiming.class);
		return commonTest.createTestSetup();
	}

	private static interface Profiler {
		/**
		 * @param id analysis id to use
		 * @return ms
		 */
		long profile(Identifier id) throws ApplicationException;
	}

	private static class ProfilerResultByLicFromAnalysis
	implements Profiler {
		public long profile(Identifier id) throws ApplicationException {
			long t0 = System.nanoTime();
			LinkedIdsCondition lic =
				new LinkedIdsCondition(id, ObjectEntities.RESULT_CODE);
			long t1 = System.nanoTime();
			Set set = StorableObjectPool.getStorableObjectsByCondition(lic, true);
			long t2 = System.nanoTime();
			Result result = (Result) set.iterator().next();
			long t3 = System.nanoTime();
			Parameter[] parameters = result.getParameters();
			System.out.println("nParams: " + parameters.length + "; "
					+ "lic: " + (t1 - t0) / 1e6 + " ms; "
					+ "getSOByC: " + (t2 - t1) / 1e6 + " ms; "
					+ "iterator: " + (t3 - t2) / 1e6 + " ms");
			return t3 - t0;
		}
	}

	private static class ProfilerMeasurementByLicFromAnalysis
	implements Profiler {
		public long profile(Identifier id) throws ApplicationException {
			long t0 = System.nanoTime();
			StorableObject ret = StorableObjectPool.getStorableObject(id, true);
			long t1 = System.nanoTime();
			Measurement m = ((Analysis)ret).getMeasurement();
			long t2 = System.nanoTime();
			System.out.println("getSO:   " + (t1 - t0) / 1e6 + " ms; "
					+ "getMeas: " + (t2 - t1) / 1e6 + " ms; ");
			return t2 - t0;
		}
	}

	private static class ProfilerMeasurementResultByLicFromAnalysis
	implements Profiler {
		public long profile(Identifier id) throws ApplicationException {
			long t0 = System.nanoTime();
			StorableObject ret = StorableObjectPool.getStorableObject(id, true);
			long t1 = System.nanoTime();
			Measurement m = ((Analysis)ret).getMeasurement();
			long t2 = System.nanoTime();
			final Set<Result> results = m.getResults();
			long t3 = System.nanoTime();
			System.out.println("getSO:   " + (t1 - t0) / 1e6 + " ms; "
					+ "getMeas: " + (t2 - t1) / 1e6 + " ms; "
					+ "getRes:  " + (t3 - t2) / 1e6 + " ms; "
					+ "# results = " + results.size()
					);
			return t3 - t0;
		}
	}

	private static class ProfilerAnalysis
	implements Profiler {
		public long profile(Identifier id) throws ApplicationException {
			long t0 = System.nanoTime();
			StorableObjectPool.getStorableObject(id, true);
			long t1 = System.nanoTime();
			System.out.println("dt: " + (t1 - t0) / 1e6);
			return t1 - t0;
		}
	}

	/**
	 * holds averave and minimal time in ms
	 */
	private static class TimingStat {
		private double average;
		private double min;
		protected TimingStat(double average, double min) {
			this.average = average;
			this.min = min;
		}
		public double getAverage() {
			return this.average;
		}
		public double getMin() {
			return this.min;
		}
	}

	private TimingStat doProfile(Profiler profiler)
	throws ApplicationException {
		System.out.println("profiling " + profiler.getClass());
//		System.gc();
		boolean first = true;
		double m1 = 0;
		double m0 = 0;
		double tMin = 0;
		for (Identifier analysisId: this.analysisIds) {
			double dt = profiler.profile(analysisId);
			if (first) {
				first = false;
				tMin = dt;
			} else {
				m0++;
				m1 += dt;
				if (tMin > dt)
					tMin = dt;
			}
		}

		final double average = m1 / m0 / 1e6;
		tMin /= 1e6;

		System.out.println("average time (excluding first): "
				+ average + " ms; "
				+ "minimal time: " + tMin + " ms; ");
		return new TimingStat(average, tMin);
	}

	private class ExpectationRecord {
		public Profiler profiler;
		public TimingStat expectation;
		protected ExpectationRecord(Profiler profiler,
				double expectationAvg, double expectationMin) {
			this.profiler = profiler;
			this.expectation = new TimingStat(expectationAvg, expectationMin);
		}
	}

	@SuppressWarnings("boxing")
	public void testLoadResult() throws ApplicationException {
		ExpectationRecord[] expectations = new ExpectationRecord[] {
			// NB: the order does matter
			new ExpectationRecord(new ProfilerAnalysis(), 10.0, 8.0), // load both Analysis and its Measurement
			new ExpectationRecord(new ProfilerAnalysis(), 0.01, 0.01), // reading from cache
			new ExpectationRecord(new ProfilerResultByLicFromAnalysis(), 19.0, 18.0), // load results of Analysis
			new ExpectationRecord(new ProfilerMeasurementByLicFromAnalysis(), 0.022, 0.015), // actually just reads Measurement from cache
			new ExpectationRecord(new ProfilerMeasurementResultByLicFromAnalysis(), 130, 66)
		};
		TimingStat[] measured = new TimingStat[expectations.length];
		System.out.println(" --- preparing ids ---");
		this.analysisIds = new Identifier[analysisIdNames.length];
		for (int i = 0; i < analysisIdNames.length; i++) {
			this.analysisIds[i] = Identifier.valueOf(analysisIdNames[i]);
		}
		System.out.println(" --- starting measurments ---");
		System.gc();
		for (int i = 0; i < expectations.length; i++) {
			measured[i] = doProfile(expectations[i].profiler);
		}
		System.out.println(" --- printing results ---");
		System.out.println("   averageTime/ms                  minimalTime/ms                action");
		for (int i = 0; i < expectations.length; i++) {
			System.out.printf("%7.3f ( %5.1f%% of %7.3f ) %7.3f ( %5.1f%% of %7.3f )   %s\n",
					Double.valueOf(measured[i].getAverage()),
					Double.valueOf(measured[i].getAverage() / expectations[i].expectation.getAverage() * 100.0),
					Double.valueOf(expectations[i].expectation.getAverage()),
					Double.valueOf(measured[i].getMin()),
					Double.valueOf(measured[i].getMin() / expectations[i].expectation.getMin() * 100.0),
					Double.valueOf(expectations[i].expectation.getMin()),
					expectations[i].profiler.getClass().getSimpleName()
					);
		}
	}
}
