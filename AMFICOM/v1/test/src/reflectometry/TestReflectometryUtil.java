/*-
 * $Id: TestReflectometryUtil.java,v 1.2 2006/01/27 16:11:25 saa Exp $
 * 
 * Copyright ¿ 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package reflectometry;

import junit.framework.TestCase;

import com.syrus.AMFICOM.reflectometry.ReflectometryMeasurementParameters;
import com.syrus.AMFICOM.reflectometry.ReflectometryUtil;

/**
 * @author $Author: saa $
 * @version $Revision: 1.2 $, $Date: 2006/01/27 16:11:25 $
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

	// actual testcase
	public void testTimeEstimation2() {
		double worstDeltaExcess = 0.0;
		double worstDeltaLack = 0.0;
		for (final double[] params: get1643ASamples()) {
			ReflectometryMeasurementParameters mp = sample2mp(params);
			double actual = params[4];
			double expect = (int) ( 10.0 *
				ReflectometryUtil.getEstimatedQP1640ATestTime(mp, true))
				/ 10;
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
}
