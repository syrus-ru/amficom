/*-
 * $Id: Math2.java,v 1.1 2006/07/05 02:01:25 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

import static java.lang.Math.abs;
import static java.lang.Math.log10;
import static java.lang.Math.pow;
import static java.lang.Math.rint;
import static java.lang.Math.round;
import static java.util.logging.Level.WARNING;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author Old Wise Saa
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2006/07/05 02:01:25 $
 * @module util
 */
public final class Math2 {
	private static final double LG5 = log10(5);

	private Math2() {
		assert false;
	}

	/**
	 * {@code epsilon = 0} means full precision.
	 */
	public static double roundEpsilon(final double d, final double epsilon) {
		if (epsilon < 0 || Double.isInfinite(epsilon) || Double.isNaN(epsilon)) {
			throw new IllegalArgumentException(String.valueOf(epsilon));
		}

		if (epsilon == 0 || Double.isInfinite(d) || Double.isNaN(d)) {
			return d;
		}

		if (d < 0) {
			return -roundEpsilon(-d, epsilon);
		}

		int n = -getIntegerDigitCount(d) - (startsWith56789(d) ? 1 : 0);
		while (true) {
			assert n < Integer.MAX_VALUE;

			final double d2 = roundN(d, n++, true);
			if (abs(d2 - d) <= epsilon) {
				return d2;
			}
		}
	}

	/**
	 * <p>Performs rounding (not truncating!) of a {@code double} value.</p>
	 *
	 * <p>If {@code n} is positive, returns a {@code double} that contains
	 * no more than [@code n} fractional digits. Negative values of {@code
	 * n} mean truncation of {@code Math.abs(n)} integer digits as well as
	 * the whole fractional part.</p>
	 */
	public static double roundN(final double d, final int n) {
		if (Double.isInfinite(d) || Double.isNaN(d)) {
			return d;
		}

		return roundN(d, n, false);
	}

	private static double roundN(final double d, final int n, final boolean quiet) {
		if (log10(abs(d)) < -n && !quiet) {
			Log.debugMessage("|" + d + "| < 1.0E" + -n + ".",
					WARNING);
		}

		if (d < 0) {
			return -roundN(-d, n, true);
		}

		if (n == 0) {
			return rint(d);
		}

		final double d2 = pow(10, n);
		final double d3 = d * d2;
		if (d3 > Long.MAX_VALUE) {
			Log.debugMessage("Integer overflow (d = " + d + "; n = " + n + "); returning unrounded value.",
					WARNING);
			return d;
		}
		return round(d3) / d2;
	}

	public static int getIntegerDigitCount(final double d) {
		if (Double.isInfinite(d) || Double.isNaN(d)) {
			throw new IllegalArgumentException(String.valueOf(d));
		}

		if (d < 0) {
			return getIntegerDigitCount(-d);
		}

		return d >= 1.0
				? (int) log10(d) + 1
				: 0;
	}

	/**
	 * Not very accurate; not always returns {@code true} for, say,
	 * {@code 5.0E+XX}. Still helpful.
	 */
	public static boolean startsWith56789(final double d) {
		if (Double.isInfinite(d) || Double.isNaN(d)) {
			throw new IllegalArgumentException(String.valueOf(d));
		}

		if (d < 0) {
			return startsWith56789(-d);
		}

		/*-
		 * Always return false for doubles within (-1.0; 1.0)
		 */
		if (d < 1.0) {
			return false;
		}

		final double lgd = log10(d);
		return (lgd - (short) lgd) - LG5 >= 0;
	}
}
