/*-
 * $Id: ReflectometryUtil.java,v 1.3 2006/01/26 16:10:14 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.reflectometry;

/**
 * @author $Author: saa $
 * @version $Revision: 1.3 $, $Date: 2006/01/26 16:10:14 $
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
	 * Оценивает сверху время проведения измерения агентом.
	 * Оцениваемое время включает:
	 * <ul>
	 * <li> время обработки агентом перед отправкой
	 * <li> время передачи от агента к КИС
	 * <li> время измерения на КИС
	 * <li> время передачи от КИС к агенту
	 * <li> время обработки результата на агенте
	 * </li>
	 * <p> Оценка довольно грубая, "как бы сверху", полагаясь на быструю
	 * передачу по сети. Основана на пробных замерах.
	 * <p> Типично выдает значение, завышенное на 10-20 сек.
	 * <p> XXX: не знает, какой рефлектометр установлен,
	 * поэтому использует данные по QP1640A.
	 * @return время проведения измерения, оцененное сверху, выраженное в секундах
	 */
	public static double getUpperEstimatedAgentTestTime(
			final ReflectometryMeasurementParameters rmp) {
		// складываем время собственно измерения
		// и время программной обработки и сетевой передачи
		return getEstimatedQP1640ATestTime(rmp, true)
				+ UPPER_AGENT_TIME
				+ SAFE_TIME;
	}
}
