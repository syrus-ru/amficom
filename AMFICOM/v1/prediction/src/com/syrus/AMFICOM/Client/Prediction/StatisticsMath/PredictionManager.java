/*-
 * $Id: PredictionManager.java,v 1.7 2006/03/23 08:51:02 saa Exp $
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
 * @version $Revision: 1.7 $, $Date: 2006/03/23 08:51:02 $
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
	 * Возвращает статистику по затуханию.
	 * Определен, если {@link #hasAttenuationInfo} возвращает true,
	 * но и в этом случае нет гарантии, что временной ряд будет непуст.
	 * @param nEvent номер события
	 */
	Statistics getAttenuationInfo(int nEvent);

	/**
	 * "Хрен знает" (мы со Стасом не знаем)
	 * @todo понять либо удалить
	 */
	Statistics getSplashAmplitudeInfo(int nEvent);

	/**
	 * Возвращает статистику уровеня падающего сигнала (y0).
	 * Определен, если {@link #hasAmplitudeInfo} возвращает true.
	 * @todo переименовать в getIncidentLevelInfo (и метод has... тоже)
	 * @param nEvent номер события
	 */
	Statistics getAmplitudeInfo(int nEvent);

	/**
	 * Возвращает статистику по потерям на данном событии
	 * Определен, если {@link #hasEnergyLossInfo} возвращает true.
	 * @param nEvent номер события
	 */
	Statistics getEnergyLossInfo(int nEvent);

	/**
	 * Возвращает статистику по амплитуде отражательного всплеска
	 * на данном событии.
	 * Определен, если {@link #hasReflectanceInfo} возвращает true.
	 * @todo переименовать в getReflectiveAmplInfo или типа того (и метод has... тоже)
	 * @param nEvent номер события
	 */
	Statistics getReflectanceInfo(int nEvent);

	/**
	 * Определяет наличие AttenuationInformation для указанного события.
	 * @param nEvent номер события
	 * @return true, если AttenuationInformation определена.
	 */
	boolean hasAttenuationInfo(int nEvent);

	boolean hasSplashAmplitudeInfo(int nEvent);

	boolean hasAmplitudeInfo(int nEvent);

	boolean hasEnergyLossInfo(int nEvent);

	boolean hasReflectanceInfo(int nEvent);

	/**
	 * Рассчитать предсказанную р/г на заданную дату
	 */
	double[] getPredictedReflectogram(long date);

}