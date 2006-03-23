/*-
 * $Id: PredictionManager.java,v 1.10 2006/03/23 09:41:03 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Prediction.StatisticsMath;

import com.syrus.AMFICOM.measurement.MonitoredElement;

/**
 * Отделяет математическую часть prediction от GUI и Pool'а.
 * Предназначен для обеспечения возможности перевода старого прогнозирования
 * одновременно на новую математику и новое GUI.
 * Не обращется к Pool'у.
 * <p>
 * Формально заменяет ReflectoEventStatistics и его интерфейс, включая
 * в себя функции ReflectogrammPredictor.
 * </p>
 * @author saa
 * @author $Author: saa $
 * @version $Revision: 1.10 $, $Date: 2006/03/23 09:41:03 $
 * @module prediction
 */
public interface PredictionManager {

	/**
	 * @return дата/время первой рефлектограммы
	 * либо Long.MAX_VALUE, если рефлектограмм нет.
	 * Ожидается, что она будет в интервале от LowerTime до UpperTime
	 */
	long getMinTime();

	/**
	 * @return дата/время последней рефлектограммы
	 * либо 0, если рефлектограмм нет.
	 * Ожидается, что она будет в интервале от LowerTime до UpperTime
	 */
	long getMaxTime();

	/**
	 * @return LowerTime интервала "как он был выбран вначале".
	 * По-видимому, все getDate() будут не менее этой величины.
	 */
	long getLowerTime();

	/**
	 * @return UpperTime интервала "как он был выбран вначале".
	 * По-видимому, все getDate() будут не более этой величины.
	 */
	long getUpperTime();

	/**
	 * @return MonitoredElement прогнозируемого набора р/г
	 */
	MonitoredElement getMonitoredElement();

	/**
	 * Определяет возможность определения
	 * статистики по затуханию для данного события (дБ/км)
	 * @param nEvent номер события
	 * @return true, если статистика по затуханию для данного события определена
	 */
	boolean hasAttenuationInfo(int nEvent);

	/**
	 * Возвращает статистику по затуханию на данном событии (дБ/км)
	 * Определен, если {@link #hasAttenuationInfo(int)} возвращает true,
	 * но и в этом случае нет гарантии, что временной ряд будет непуст.
	 * @param nEvent номер события
	 * @throws IllegalStateException {@link #hasAttenuationInfo(int)} == false
	 */
	Statistics getAttenuationInfo(int nEvent);

	/**
	 * Определяет возможность определения
	 * статистики по потерям для данного события
	 * @param nEvent номер события
	 * @return true, если статистика по потерям для данного события определена
	 */
	boolean hasLossInfo(int nEvent);

	/**
	 * Возвращает статистику по потерям на данном событии
	 * Определен, если {@link #hasLossInfo(int)} возвращает true,
	 * но и в этом случае нет гарантии, что временной ряд будет непуст.
	 * @param nEvent номер события
	 * @throws IllegalStateException {@link #hasLossInfo(int)} == false
	 */
	Statistics getLossInfo(int nEvent);

	/**
	 * Определяет возможность определения
	 * статистики по амплитуде отражательного всплеска для данного события
	 * @param nEvent номер события
	 * @return true, если статистика по амплитуде отражательного всплеска
	 *   для данного события определена
	 */
	boolean hasReflectiveAmplitudeInfo(int nEvent);

	/**
	 * Возвращает статистику по амплитуде отражательного всплеска
	 *   на данном событии
	 * Определен, если {@link #hasReflectiveAmplitudeInfo(int)} возвращает true,
	 * но и в этом случае нет гарантии, что временной ряд будет непуст.
	 * @param nEvent номер события
	 * @throws IllegalStateException {@link #hasReflectiveAmplitudeInfo(int)} == false
	 */
	Statistics getReflectiveAmplitudeInfo(int nEvent);

	/**
	 * Определяет возможность определения
	 * статистики по уровню падающего сигнала для данного события
	 * @param nEvent номер события
	 * @return true, если статистика по уровню падающего сигнала
	 *   для данного события определена
	 */
	boolean hasY0Info(int nEvent);

	/**
	 * Возвращает статистику по уровню падающего сигнала на данном событии
	 * Определен, если {@link #hasY0Info(int)} возвращает true,
	 * но и в этом случае нет гарантии, что временной ряд будет непуст.
	 * @param nEvent номер события
	 * @throws IllegalStateException {@link #hasY0Info(int)} == false
	 */
	Statistics getY0Info(int nEvent);

	/**
	 * Определяет возможность определения
	 * статистики по коэффициенту отражения для данного события
	 * @param nEvent номер события
	 * @return true, если статистика по коэффициенту отражения
	 * для данного события определена
	 */
	boolean hasReflectanceInfo(int nEvent);

	/**
	 * Возвращает статистику по коэффициенту отражения на данном событии
	 * Определен, если {@link #hasReflectanceInfo(int)} возвращает true,
	 * но и в этом случае нет гарантии, что временной ряд будет непуст.
	 * @param nEvent номер события
	 * @throws IllegalStateException {@link #hasReflectanceInfo(int)} == false
	 */
	Statistics getReflectanceInfo(int nEvent);

	/**
	 * Рассчитать предсказанную р/г на заданную дату
	 */
	double[] getPredictedReflectogram(long date);

}