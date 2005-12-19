/*-
 * $Id: PredictionManager.java,v 1.1 2005/12/19 11:42:24 saa Exp $
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
 * <p>
 * Формально заменяет ReflectoEventStatistics и его интерфейс, включая
 * в себя функции ReflectogrammPredictor.
 * Так же, как и ReflectoEventStatistics, хранится GUI'ми в Pool'е.
 * </p>
 * @author saa
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2005/12/19 11:42:24 $
 * @module prediction
 */
public class PredictionManager {
	private ReflectoEventStatistics res;

	public PredictionManager(ReflectoEventContainer []statData,
			ReflectoEventContainer reference,
			long lowerTime, long upperTime,
			MonitoredElement me) {
		this.res = new ReflectoEventStatistics(statData,
				reference, lowerTime, upperTime, me);
	}

	/**
	 * @return число рефлектограмм
	 */
	public int getNTraces() {
		return this.res.statData.length;
	}

	/**
	 * @param nTrace номер рефлектограммы
	 * @return дата/время рефлектограммы
	 */
	public long getDate(int nTrace) {
		return this.res.statData[nTrace].date;
	}

	/**
	 * @return LowerTime интервала "как он был выбран вначале".
	 * По-видимому, все getDate() будут не менее этой величины.
	 */
	public long getLowerTime() {
		return this.res.getLowerTime();
	}

	/**
	 * @return UpperTime интервала "как он был выбран вначале".
	 * По-видимому, все getDate() будут не более этой величины.
	 */
	public long getUpperTime() {
		return this.res.getUpperTime();
	}

	/**
	 * @return MonitoredElement прогнозируемого набора р/г
	 */
	public MonitoredElement getMonitoredElement() {
		return this.res.getMonitoredElement();
	}

	/**
	 * Кладет в Pool AttenuationInformation
	 * @todo отделить вычисление от помещения в Pool
	 * @param nEvent
	 */
	public void poolizeAttenuationInformation(int nEvent) {
		this.res.getAttenuationInformation(nEvent);
	}

	public void poolizeSplashAmplitudeInformation(int nEvent) {
		this.res.getSplashAmplitudeInformation(nEvent);
	}

	public void poolizeAmplitudeInformation(int nEvent) {
		this.res.getAmplitudeInformation(nEvent);
	}

	public void poolizeEnergyLossInformation(int nEvent) {
		this.res.getEnergyLossInformation(nEvent);
	}

	public void poolizeReflectanceInformation(int nEvnts) {
		this.res.getReflectanceInformation(nEvnts);
	}

	/**
	 * Рассчитать предсказанную р/г на заданную дату
	 */
	public double[] getPredictedReflectogram(long date) {
		 return new ReflectogrammPredictor(date,
				 this.res).getPredictedReflectogramm();
	}
}
