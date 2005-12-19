/*-
 * $Id: PredictionManager.java,v 1.5 2005/12/19 15:36:15 saa Exp $
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
 * @version $Revision: 1.5 $, $Date: 2005/12/19 15:36:15 $
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
	 * Определяет AttenuationInformation
	 * @param nEvent номер события
	 * @return AttenuationInformation
	 */
	Statistics getAttenuationInfo(int nEvent);

	Statistics getSplashAmplitudeInfo(int nEvent);

	Statistics getAmplitudeInfo(int nEvent);

	Statistics getEnergyLossInfo(int nEvent);

	Statistics getReflectanceInfo(int nEvent);

	/**
	 * Рассчитать предсказанную р/г на заданную дату
	 */
	double[] getPredictedReflectogram(long date);

}