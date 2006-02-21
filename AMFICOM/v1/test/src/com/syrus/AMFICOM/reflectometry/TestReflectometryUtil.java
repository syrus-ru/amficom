/*-
 * $Id: TestReflectometryUtil.java,v 1.1 2006/02/21 09:32:56 saa Exp $
 * 
 * Copyright ¿ 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.reflectometry;

import com.syrus.AMFICOM.reflectometry.ReflectometryMeasurementParameters;
import com.syrus.AMFICOM.reflectometry.ReflectometryUtil;

import junit.framework.TestCase;


/**
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2006/02/21 09:32:56 $
 * @module
 */
public class TestReflectometryUtil extends TestCase {

	public void _testTimeEstimation() {
		double[] lengths = new double[] {
				5, 20, 50, 75, 125, 250, 300
		};
		for (final double traceLength: lengths) {
			ReflectometryMeasurementParameters mp =
				new ReflectometryMeasurementParameters() {
					public boolean hasGainSplice() {
						return false;
					}
	
					public boolean hasHighResolution() {
						return false;
					}
	
					public boolean hasLiveFiberDetection() {
						return false;
					}
	
					public int getNumberOfAverages() {
						return 32768;
					}
	
					public int getPulseWidth() {
						return 0;
					}
	
					public double getRefractionIndex() {
						return 0;
					}
	
					public double getResolution() {
						return 4.0;
					}
	
					public double getTraceLength() {
						return traceLength;
					}
	
					public int getWavelength() {
						return 0;
					}
			};
			double time = (int)
				ReflectometryUtil.getUpperEstimatedAgentTestTime(mp);
			System.out.println("length " + traceLength + " km: "
					+ "time " + time + " sec");
		}
	}
	public void _test1() {
		double[] params = new double[] {1000, 250, 4, 4096};
		ReflectometryMeasurementParameters mp = sample2mp(params);
		double expect = (int) ( 10.0 *
				ReflectometryUtil.getEstimatedQP1640ATestTime(mp, true))
				/ 10;
		System.out.println("expect = " + expect);
	}

	/**
	 * based on 1643A measurements dated 2006-01-27
	 */
	private static double[][] get1643ASamples() {
		return new double[][] {
				// pulse/ns, distance/km, resolution/m, nAv, t/sec
				{5, 5, 4, 4096, 7},
				{5, 5, 2, 4096, 7},
				{5, 5, 1, 4096, 9},

				{5, 5, 4, 45312, 24.5},
				{5, 5, 2, 45312, 33},
				{5, 5, 1, 45312, 53},

				{30000, 5, 4, 45312, 24},

				{5, 20, 4, 45312, 35},
				{5, 50, 4, 45312, 60},
				{5, 75, 4, 45312, 79},
				{5, 125, 4, 45312, 119},
				{5, 250, 4, 45312, 216},

				{5, 300, 4, 4096, 28},
				{5, 250, 4, 4096, 24},
				{5, 125, 4, 4096, 15},

				{5, 250, 8, 4096, 24},
				{5, 250, 2, 4096, 42},

				{5, 5, 4, 262144, 116},
				{2000, 75, 4, 45312, 79},
				{5, 5, 4, 4096, 6.5},
				{2000, 5, 4, 4096, 6.5}
		};
	}

	private static double[][] get1640MRSamples() {
		return new double[][] {
				// pulse/ns, distance/km, resolution/m, nAv, t/sec
				{0, 131.072,	8,	4,		2},
				{0, 131.072,	8,	32,		13},
				{0, 131.072,	8,	100,	42},
				{0, 131.072,	8,	1,		0},
				{0, 131.072,	8,	100,	42},
				{0, 131.072,	16,	100,	42},
				{0, 131.072,	4,	100,	82},
				{0, 131.072,	8,	25,		10},
				{0, 131.072,	4,	25,		20},
				{0, 131.072,	2,	25,		40},
				{0, 131.072,	1,	25,		80},
				{0, 131.072,	16,	25,		10},
				{0, 131.072,	8,	100,	41},
				{0, 131.072,	8,	100,	42},
				{0, 131.072,	4,	25,		20},
				{0, 131.072,	4,	25,		21},
				{0, 131.072,	4,	25,		21},
				{0, 131.072,	4,	25,		21},
				{0, 65.536,		8,	100,	22},
				{0, 262.144,	8,	25,		21},
				{0, 320.000,	8,	25,		25},
				{0, 32.768,		8,	200,	25},
				{0, 16.384,		8,	400,	29},
				{0, 8.192,		8,	800,	38},
				{0, 4.096,		8,	1600,	55},
				{0, 65.536,		2,	25,		21},
				{0, 131.072,	2,	25,		41},
				{0, 262.144,	2,	12,		39},
//				{0, 320.000,	2,	12,		60},
				{0, 32.768,		2,	50,		21},
				{0, 8.192,		2,	200,	24},
				{0, 4.096,		2,	400,	29},
				{0, 131.072,	8,	800,	334},
				{0, 4.096,		8,	8000,	275},
				{0, 32.768,		8,	3200,	387},
				{0, 32.768,		1,	400,	327},
				{0, 4.096,		1,	2500,	306}
			};
	}

	private static ReflectometryMeasurementParameters sample2mp(
			final double[] params) {
		return new ReflectometryMeasurementParameters() {
				public boolean hasGainSplice() {
					return false;
				}

				public boolean hasHighResolution() {
					return false;
				}

				public boolean hasLiveFiberDetection() {
					return true;
				}

				public int getNumberOfAverages() {
					return (int)(params[3]);
				}

				public int getPulseWidth() {
					return (int)(params[0]);
				}

				public double getRefractionIndex() {
					return 0;
				}

				public double getResolution() {
					return params[2];
				}

				public double getTraceLength() {
					return params[1];
				}

				public int getWavelength() {
					return 0;
				}
		};
	}

	private void verifyTimeEstimation(MeasurementTimeEstimator estimator,
			double[][] samples,
			double allowedFactor,
			double allowedAddition) {
		double worstDeltaExcess = 0.0;
		double worstDeltaLack = 0.0;
		for (final double[] params: samples) {
			ReflectometryMeasurementParameters mp = sample2mp(params);
			double actual = params[4];
			double expect = (int) ( 10.0 *
					estimator.getEstimatedMeasurementTime(mp, true))
				/ 10;
			double delta = actual - expect;
			System.out.println("L " + mp.getTraceLength()
					+ " DX " + mp.getResolution()
					+ " AV " + mp.getNumberOfAverages()
					+ ": expect " + expect
					+ " actual " + actual
					+ "; delta " + delta);
			assertTrue(delta <= 0.0); // no underestimate allowed
			assertTrue(-delta <= actual * allowedFactor + allowedAddition);
			if (worstDeltaLack < delta)
				worstDeltaLack = delta;
			if (worstDeltaExcess < -delta)
				worstDeltaExcess = -delta;
		}
		System.out.println("Worst delta exp>real: " + worstDeltaExcess);
		System.out.println("Worst delta exp<real: " + worstDeltaLack);
	}

	public void testTimeEstimation1643A() {
		verifyTimeEstimation(new MeasurementTimeEstimator(){
			public double getEstimatedMeasurementTime(ReflectometryMeasurementParameters rmp, boolean upper) {
				return ReflectometryUtil.getEstimatedQP1640ATestTime(rmp, upper);
			}}, get1643ASamples(), 0.06, 2);
	}

	public void testTimeEstimation1640MR() {
		verifyTimeEstimation(new MeasurementTimeEstimator(){
			public double getEstimatedMeasurementTime(ReflectometryMeasurementParameters rmp, boolean upper) {
				return ReflectometryUtil.getEstimatedQP1640MRTestTime(rmp, upper);
			}}, get1640MRSamples(), 0.04, 2);
	}
}

