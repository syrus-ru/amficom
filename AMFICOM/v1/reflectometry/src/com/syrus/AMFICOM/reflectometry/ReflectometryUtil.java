/*-
 * $Id: ReflectometryUtil.java,v 1.5.2.3 2006/04/18 16:21:07 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.reflectometry;

/**
 * @author $Author: saa $
 * @version $Revision: 1.5.2.3 $, $Date: 2006/04/18 16:21:07 $
 * @module
 */
public final class ReflectometryUtil {
	private static final double SPEED_OF_LIGHT_M_NS = 0.3; // c = 0.3 m/ns

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
	 * Оценивает время проведения измерения на QP1640MR.
	 * Оценка основана на предположении,
	 * что режим 320км/2м не используется.
	 * Использование режима 320км/2м (по замерам в феврале-марте 2006 г.)
	 * приводит к увеличению времени последующих измерений в
	 * остальных режимах.
	 * <p>
	 * Мы рассчитываем на использование любых режимов с числом точек,
	 * не превышающем 128K = 131072, т.к. ни один из этих
	 * режимов в описанном поведении замечен не был.
	 * 
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
			0.5 + av * len / 131 * .4 * 8 / Math.min(8,dX) + av * 35 / 1600;
		return upper ? 0.5 + estimatedTime * 1.01 : estimatedTime;
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

	/**
	 * Преобразует длину импульса, выраженную в "метрах" pk7600,
	 * в соответствующую длину импульса, выраженную в
	 * наносекундах QP164xA.
	 * Учитывает традиционные отличия в терминологии "длина импульса"
	 * в этих двух сериях рефлектометров.
	 * 
	 * Не предназначен для обобщенного перевода метров в наносекунды.
	 * 
	 * При необходимости использования целочисленных значений,
	 * рекомендуется округлять возвращаемое значение через
	 * {@link Math#round()}.
	 * 
	 * На данный момент реализовано преобразование, основанное на следующем
	 * понимании терминов:
	 * <ul>
	 * <li> один метр pk7600 - такая длина тестового импульса, при которой
	 *   базовая ширина точечного события на рефлектограмма окажется
	 *   равной одному метру.
	 * <li> одна наносекунда QP164xA - тестовый импульс имеет длину одна
	 *   наносекунда.
	 * </ul>
	 * 
	 * Фактически, этот метод нужен в реализациях интерфейса
	 * {@link ReflectometryMeasurementParameters}.
	 * 
	 * @param pkMeters длина импульса в "метрах" в терминологии
	 *   pk7600.
	 * @param refractiveIndex показатель преломления волокна.
	 * @return длина импульса в наносекундах
	 */
	public static double pulseWidthPKMetersToNanoseconds(
			double pkMeters,
			double refractiveIndex) {
		return pkMeters / (SPEED_OF_LIGHT_M_NS / 2.0 / refractiveIndex);
	}

	/**
	 * Выполняет преобразование, обратное
	 * {@link #pulseWidthPKMetersToNanoseconds(double, double)}.
	 * 
	 * Честно говоря, не знаю, понадобится ли когда-нибудь этот метод,
	 * он здесь для логической завершенности.
	 * 
	 * @param nanoseconds длина импульса, выраженная в наносекундах
	 * @param refractiveIndex показатель преломления волокна
	 * @return длина импульса, выраженная в метрах в понимании pk7600.
	 */
	public static double pulseWidthNanosecondsToPKMeters(
			double nanoseconds,
			double refractiveIndex) {
		return nanoseconds * SPEED_OF_LIGHT_M_NS / 2.0 / refractiveIndex;
	}

	/**
	 * Преобразует длину импульса, выраженную в наносекундах,
	 * в базовую протяженность точечного события на рефлектограмме.
	 * 
	 * Фактически этот метод может пригодиться только в анализе (dadara).
	 * 
	 * @param nanoseconds длина импульса в наносекундах.
	 * @param refractiveIndex показатель преломления волокна.
	 * @return базовая протяженность точечного события на рефлектограмме
	 */
	public static double pulseWidthNanosecondsToEventLengthMeters(
			double nanoseconds,
			double refractiveIndex) {
		/* да-да, ширина события совпадает с длиной pk7600 */
		return nanoseconds * SPEED_OF_LIGHT_M_NS / 2.0 / refractiveIndex;
	}

	/**
	 * Преобразование, обратное к
	 * {@link #pulseWidthNanosecondsToEventLengthMeters(double, double)}.
	 * 
	 * Думаю, это преобразование уже точно никому не понадобится,
	 * и, чтобы подчеркнуть это, видимость этого метода ограничена.
	 * При необходимости использовать, расширьте видимость.
	 */
	static double pulseWidthEventLengthMetersToNanoseconds(
			double eventWidthMeters,
			double refractiveIndex) {
		return eventWidthMeters / (SPEED_OF_LIGHT_M_NS / 2.0 / refractiveIndex);
	}
}
