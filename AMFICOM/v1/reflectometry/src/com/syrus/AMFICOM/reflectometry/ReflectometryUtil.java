/*-
 * $Id: ReflectometryUtil.java,v 1.5 2006/02/17 12:55:22 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.reflectometry;

/**
 * @author $Author: saa $
 * @version $Revision: 1.5 $, $Date: 2006/02/17 12:55:22 $
 * @module
 */
public final class ReflectometryUtil {
	
	private ReflectometryUtil() {
		assert false;
	}

	/**
	 * Оценка сверху для времени обработки непосредственно на агенте
	 * и передачи между КИС и агентом.
	 * Секунды.
	 */
	private static final double UPPER_AGENT_TIME = 30.0;

	/**
	 * Время запаса.
	 * Может компенсировать непредсказуемость времени переключения OTAU.
	 */
	private static final double SAFE_TIME = 20.0;

	/**
	 * Оценивает время проведения измерения на QP1640A.
	 * @param rmp параметры измерения
	 * @param upper true, если нужна оценка сверху,
	 *   false, если нужна несмещенная оценка
	 * @return время измерения в секундах,
	 *   средняя погрешность порядка 1% + 1 сек
	 *   макс. погрешность 6% + 2 сек (см. тест)
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
	 * Оценивает время проведения измерения на QP1640MR
	 * FIXME: выдает неверные (заниженные) результаты для режима 320 км)
	 * @param rmp параметры измерения
	 * @param upper true, если нужна верхняя оценка
	 * @return время измерения в секундах
	 */
	public static double getEstimatedQP1640MRTestTime(
			final ReflectometryMeasurementParameters rmp,
			final boolean upper) {
		double len = rmp.getTraceLength(); // km
		double dX = rmp.getResolution(); // m
		double av = rmp.getNumberOfAverages();
		double estimatedTime =
			av * len / 131 * .4 * 8 / Math.min(8,dX) + av * 35 / 1600;
		estimatedTime += upper ? 2.0 : 0.5;
		return estimatedTime;
	}

	/**
	 * Estimator for 1640A and 1643A. Tested for 1643A only.
	 */ 
	private static final MeasurementTimeEstimator QP1643A_ESTIMATOR =
		new MeasurementTimeEstimator() {
			final public double getEstimatedMeasurementTime(
					final ReflectometryMeasurementParameters rmp,
					final boolean upper) {
				return getEstimatedQP1640ATestTime(rmp, upper);
			}
	};

	/**
	 * Estimator for 1640MR.
	 */ 
	private static final MeasurementTimeEstimator QP1640MR_ESTIMATOR =
		new MeasurementTimeEstimator() {
			final public double getEstimatedMeasurementTime(
					final ReflectometryMeasurementParameters rmp,
					final boolean upper) {
				return getEstimatedQP1640MRTestTime(rmp, upper);
			}
	};

	public static MeasurementTimeEstimator getQP1640AEstimator() {
		return QP1643A_ESTIMATOR; // use timing for 1643A
	}

	public static MeasurementTimeEstimator getQP1643AEstimator() {
		return QP1643A_ESTIMATOR;
	}

	public static MeasurementTimeEstimator getQP1640MREstimator() {
		return QP1640MR_ESTIMATOR;
	}

	/**
	 * Оценивает сверху время проведения измерения агентом.
	 * <p> XXX: не знает, какой рефлектометр установлен,
	 * поэтому использует данные по QP1640A/1643A.
	 * @return время проведения измерения, оцененное сверху, выраженное в секундах
	 * @see #getUpperEstimatedAgentTestTime(ReflectometryMeasurementParameters, MeasurementTimeEstimator)
	 * @deprecated use {@link #getUpperEstimatedAgentTestTime(ReflectometryMeasurementParameters, MeasurementTimeEstimator)}
	 */
	@Deprecated
	public static double getUpperEstimatedAgentTestTime(
			final ReflectometryMeasurementParameters rmp) {
		return getUpperEstimatedAgentTestTime(rmp, QP1643A_ESTIMATOR);
	}

	/**
	 * Оценивает сверху время проведения измерения агентом.
	 * Оцениваемое время включает:
	 * <ul>
	 * <li> время обработки агентом перед отправкой
	 * <li> время передачи от агента к КИС
	 * <li> время измерения на КИС
	 * <li> время передачи от КИС к агенту
	 * <li> время обработки результата на агенте
	 * </ul>
	 * <p> Оценка довольно грубая, "как бы сверху", полагаясь на быструю
	 * передачу по сети. Основана на пробных замерах.
	 * <p> Типично выдает значение, завышенное на 10-20 сек.
	 * @param rmp параметры измерения
	 * @param estimator оцениватель времени измерения данного рефлектометра
	 * @return время проведения измерения, оцененное сверху, выраженное в секундах
	 */
	public static double getUpperEstimatedAgentTestTime(
			final ReflectometryMeasurementParameters rmp,
			final MeasurementTimeEstimator estimator) {
		// складываем время собственно измерения
		// и время программной обработки и сетевой передачи
		return estimator.getEstimatedMeasurementTime(rmp, true)
				+ UPPER_AGENT_TIME
				+ SAFE_TIME;
	}
}
