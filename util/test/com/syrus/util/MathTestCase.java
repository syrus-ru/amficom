/*-
 * $Id: MathTestCase.java,v 1.1 2006/07/05 02:01:25 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

import static com.syrus.util.Math2.getIntegerDigitCount;
import static com.syrus.util.Math2.roundEpsilon;
import static com.syrus.util.Math2.roundN;

import java.lang.Math;

import junit.framework.TestCase;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2006/07/05 02:01:25 $
 * @module util
 */
public class MathTestCase extends TestCase {
	/**
	 * Test method for {@link com.syrus.util.Math2#roundEpsilon(double, double)}.
	 */
	public final void testRoundEpsilon() {
		assertEquals(0.0, roundEpsilon(0.01, 0.1), 1e-99);
		assertEquals(0.0, roundEpsilon(0.04, 0.1), 1e-98);
		assertEquals(0.0, roundEpsilon(0.05, 0.1), 1e-97);
		assertEquals(0.0, roundEpsilon(0.10, 0.1), 1e-96);

		assertEquals(0.0, roundEpsilon(0.1, 1.0), 1e-95);
		assertEquals(0.0, roundEpsilon(0.4, 1.0), 1e-94);
		assertEquals(0.0, roundEpsilon(0.5, 1.0), 1e-93);
		assertEquals(0.0, roundEpsilon(1.0, 1.0), 1e-92);

		assertEquals(0.0, roundEpsilon(1, 10), 1e-91);
		assertEquals(0.0, roundEpsilon(4, 10), 1e-90);
		assertEquals(0.0, roundEpsilon(5, 10), 1e-89);
		assertEquals(0.0, roundEpsilon(10, 10), 1e-88);

		assertEquals(0.0, roundEpsilon(10, 100), 1e-87);
		assertEquals(0.0, roundEpsilon(40, 100), 1e-86);
		assertEquals(0.0, roundEpsilon(50.0000000000001, 100), 1e-85);
		assertEquals(0.0, roundEpsilon(100, 100), 1e-84);
	}

	/**
	 * Test method for {@link com.syrus.util.Math2#roundN(double, int)}.
	 */
	public final void testRoundN() {
		assertEquals(0.0, roundN(0.1, -1), 1e-99);
		assertEquals(0.0, roundN(1.1, -1), 1e-98);
		assertEquals(0.0, roundN(2.1, -1), 1e-97);
		assertEquals(0.0, roundN(3.1, -1), 1e-96);
		assertEquals(0.0, roundN(4.1, -1), 1e-95);
		assertEquals(10.0, roundN(5.1, -1), 1e-94);
		assertEquals(10.0, roundN(6.1, -1), 1e-93);
		assertEquals(10.0, roundN(7.1, -1), 1e-92);
		assertEquals(10.0, roundN(8.1, -1), 1e-91);
		assertEquals(10.0, roundN(9.1, -1), 1e-90);
		assertEquals(10.0, roundN(10.1, -1), 1e-89);

		assertEquals(0.0, roundN(0.01, 0), 1e-99);
		assertEquals(0.0, roundN(0.11, 0), 1e-98);
		assertEquals(0.0, roundN(0.21, 0), 1e-97);
		assertEquals(0.0, roundN(0.31, 0), 1e-96);
		assertEquals(0.0, roundN(0.41, 0), 1e-95);
		assertEquals(1.0, roundN(0.51, 0), 1e-94);
		assertEquals(1.0, roundN(0.61, 0), 1e-93);
		assertEquals(1.0, roundN(0.71, 0), 1e-92);
		assertEquals(1.0, roundN(0.81, 0), 1e-91);
		assertEquals(1.0, roundN(0.91, 0), 1e-90);
		assertEquals(1.0, roundN(1.01, 0), 1e-89);

		assertEquals(0.0, roundN(0.001, 1), 1e-99);
		assertEquals(0.0, roundN(0.011, 1), 1e-98);
		assertEquals(0.0, roundN(0.021, 1), 1e-97);
		assertEquals(0.0, roundN(0.031, 1), 1e-96);
		assertEquals(0.0, roundN(0.041, 1), 1e-95);
		assertEquals(0.1, roundN(0.051, 1), 1e-94);
		assertEquals(0.1, roundN(0.061, 1), 1e-93);
		assertEquals(0.1, roundN(0.071, 1), 1e-92);
		assertEquals(0.1, roundN(0.081, 1), 1e-91);
		assertEquals(0.1, roundN(0.091, 1), 1e-90);
		assertEquals(0.1, roundN(0.101, 1), 1e-89);
	}

	/**
	 * Test method for {@link com.syrus.util.Math2#getIntegerDigitCount(double)}.
	 */
	public final void testGetIntegerDigitCount() {
		assertEquals(1, getIntegerDigitCount(Math.PI));
		assertEquals(1, getIntegerDigitCount(-Math.PI));
		assertEquals(1, getIntegerDigitCount(Math.E));
		assertEquals(1, getIntegerDigitCount(-Math.E));
		assertEquals(0, getIntegerDigitCount(0));
		assertEquals(0, getIntegerDigitCount(-0));
		assertEquals(0, getIntegerDigitCount(1e-20));
		assertEquals(0, getIntegerDigitCount(-1e-20));
	}
}
