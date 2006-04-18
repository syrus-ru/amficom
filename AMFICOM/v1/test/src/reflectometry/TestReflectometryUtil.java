/*-
 * $Id: TestReflectometryUtil.java,v 1.2.2.2 2006/04/18 16:25:34 saa Exp $
 * 
 * Copyright ¿ 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package reflectometry;

import junit.framework.TestCase;

import com.syrus.AMFICOM.reflectometry.MeasurementTimeEstimator;
import com.syrus.AMFICOM.reflectometry.ReflectometryMeasurementParameters;
import com.syrus.AMFICOM.reflectometry.ReflectometryUtil;

/**
 * @author $Author: saa $
 * @version $Revision: 1.2.2.2 $, $Date: 2006/04/18 16:25:34 $
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
	
					public int getPulseWidthNs() {
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
			double time = Math.round(
					ReflectometryUtil.getUpperEstimatedAgentTestTime(mp,
						ReflectometryUtil.getQP1643AEstimator()));
			System.out.println("length " + traceLength + " km: "
					+ "time " + time + " sec");
		}
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
	/**
	 * based on 1640MR measurements dated ~2006-02-14, 2006-04-18
	 */
	private static double[][] get1640MRSamples() {
		return new double[][] {
				// pulse/unspec, distance/km, resolution/m, nAv, t/sec

				// dated 2006-02-xx; without 320km/1m mode
				{0, 131.072, 8, 4,	2},
				{0, 131.072, 8, 32,	13},
				{0, 131.072, 8, 100,42},
				{0, 131.072, 8, 100,41},
				{0, 131.072, 8, 1,	0},

				{0, 131.072,16, 100,42},
				{0, 131.072, 4, 100,82},

				{0, 131.072, 8, 25,	10},
				{0, 131.072, 4, 25,	20},
				{0, 131.072, 4, 25,	21},
				{0, 131.072, 2, 25,	40},
				{0, 131.072, 1, 25,	80},
				{0, 131.072,16, 25,	10},

				{0, 131.072,16, 25,	10},

				{0, 65.536, 8, 100,	22},
				{0, 262.144, 8, 25,	21},
				{0, 320.000, 8, 25,	25},

				{0, 32.768, 8, 200,	25},
				{0, 16.384, 8, 400,	29},
				{0, 8.192, 8,  800,	38},
				{0, 4.096, 8, 1600,	55},

				{0, 65.536,  2, 25, 21},
				{0, 131.072, 2, 25, 41},
				{0, 262.144, 2, 12, 39},

				// dated 2006-04-18
				{0, 4.096, 4,  200, 10},
				{0, 4.096, 8,  400, 14},
				{0, 4.096, 16, 400, 13},
				{0, 4.096, 2,  100, 8},
				{0, 4.096, 2,  200, 15},
				{0, 4.096, 1,  100, 12},
				{0, 4.096, .5,  50, 11},
				{0, 4.096, .25, 25, 11},
				{0, 4.096, .25, 50, 22},
				{0, 8.192, 8,  400, 19},
				{0, 8.192, 4,  200, 14},
				{0, 8.192, 2,  100, 12},
				{0, 8.192, 2,  200, 24},
				{0, 8.192, .25, 50, 41},
				{0, 16.384, 2, 100, 22},
				{0, 16.384, 8, 400, 29},
				{0, 16.384, .25,25, 41},
				{0, 65.536, .5, 25, 80},
				{0, 4.096, .25,800,340},
				{0, 16.384,.25,200,323}
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

				public int getPulseWidthNs() {
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

	private void verifyTimeEstimation(double[][] samples,
			MeasurementTimeEstimator estimator) {
		double worstDeltaExcess = 0.0;
		double worstDeltaLack = 0.0;
		for (final double[] params: samples) {
			ReflectometryMeasurementParameters mp = sample2mp(params);
			double actual = params[4];
			double expect = (int) (
					10.0 * estimator.getEstimatedMeasurementTime(mp, true))
					/ 10.0;
			double delta = actual - expect;
			System.out.println("L " + mp.getTraceLength()
					+ " DX " + mp.getResolution()
					+ " AV " + mp.getNumberOfAverages()
					+ ": expect " + expect
					+ " actual " + actual
					+ "; delta " + delta);
			assertTrue(delta <= 0.0); // no underestimate allowed
			assertTrue(-delta <= actual * 0.06 + 2); // allow for 6% + 2 sec overestimate
			if (worstDeltaLack < delta)
				worstDeltaLack = delta;
			if (worstDeltaExcess < -delta)
				worstDeltaExcess = -delta;
		}
		System.out.println("Worst delta exp>real: " + worstDeltaExcess);
		System.out.println("Worst delta exp<real: " + worstDeltaLack);
	}

	public void testTimeEstimation1643A() {
		verifyTimeEstimation(get1643ASamples(),
				ReflectometryUtil.getQP1643AEstimator());
	}

	public void testTimeEstimation1640MR() {
		verifyTimeEstimation(get1640MRSamples(),
				ReflectometryUtil.getQP1640MREstimator());
	}
}
