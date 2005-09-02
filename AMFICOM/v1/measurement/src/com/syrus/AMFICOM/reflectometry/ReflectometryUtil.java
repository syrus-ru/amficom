/*-
 * $Id: ReflectometryUtil.java,v 1.2 2005/09/02 14:04:28 bob Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.reflectometry;

/**
 * @author $Author: bob $
 * @version $Revision: 1.2 $, $Date: 2005/09/02 14:04:28 $
 * @module
 */
public final class ReflectometryUtil {
	
	private ReflectometryUtil() {
		assert false;
	}
	
	private static final double UPPER_AGENT_TIME = 30.0;

	/**
	 * Оценивает время проведения измерения на QP1640A
	 * @param rmp параметры измерения
	 * @param upper true, если нужна оценка сверху,
	 *   false, если нужна несмещенная оценка
	 * @return время измерения в секундах, средняя погрешность порядка 1% + 1 сек
	 */
	public static double getEstimatedQP1640ATestTime(
			final ReflectometryMeasurementParameters rmp,
			final boolean upper) {
		double len = rmp.getTraceLength(); // km
		double dX = rmp.getResolution(); // m
		double lfd = rmp.hasLiveFiberDetection() ? 1 : 0;
		double kav = rmp.getNumberOfAverages() / 1024.0;

		double estimatedTime;
		if (! upper) {
			// экспериментальная формула
			estimatedTime = 4.49 + lfd * .32 + kav * .248
				+ kav / Math.min(dX, 4) * (.495 + 0.0705 * len);
		} else {
			// с запасом для оценки сверху
			estimatedTime = 5.1 + lfd * .32 + kav * .25
				+ kav / Math.min(dX, 4) * (.495 + 0.071 * len);
		}

		return estimatedTime;
	}

	/**
	 * Оценивает сверху время проведения измерения на агенте.
	 * XXX: не знает, какой рефлектометр установлен,
	 * поэтому использует данные по QP1640A
	 * @return время проведения измерения, оцененное сверху.
	 */
	public static double getUpperEstimatedAgentTestTime(
			final ReflectometryMeasurementParameters rmp) {
		return getEstimatedQP1640ATestTime(rmp, true) + UPPER_AGENT_TIME;
	}
}
