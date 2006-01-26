/*-
 * $Id: TestReflectometryUtil.java,v 1.1 2006/01/26 16:11:59 saa Exp $
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
 * @version $Revision: 1.1 $, $Date: 2006/01/26 16:11:59 $
 * @module
 */
public class TestReflectometryUtil extends TestCase {
	public void testTimeEstimation() {
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
}
