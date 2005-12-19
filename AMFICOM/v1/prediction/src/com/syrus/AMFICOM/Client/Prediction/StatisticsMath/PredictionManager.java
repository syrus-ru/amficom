/*-
 * $Id: PredictionManager.java,v 1.3 2005/12/19 15:24:53 saa Exp $
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
 * @version $Revision: 1.3 $, $Date: 2005/12/19 15:24:53 $
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
	 * @return дата/время первой рефлектограммы
	 * либо Long.MAX_VALUE, если рефлектограмм нет.
	 * Ожидается, что она будет в интервале от LowerTime до UpperTime
	 */
	public long getMinTime() {
		long t1=Long.MAX_VALUE;		
		int nTraces = getNTraces();
		for(int i = 0; i < nTraces; i++) {
			if (t1 > getDate(i))
				t1 = getDate(i);
		}
		return t1;
	}

	/**
	 * @return дата/время последней рефлектограммы
	 * либо 0, если рефлектограмм нет.
	 * Ожидается, что она будет в интервале от LowerTime до UpperTime
	 */
	public long getMaxTime() {
		long t2=0;		
		int nTraces = getNTraces();
		for(int i = 0; i < nTraces; i++) {
			if (t2 < getDate(i))
				t2 = getDate(i);
		}
		return t2;
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
	 * Определяет AttenuationInformation
	 * @param nEvent номер события
	 * @return AttenuationInformation
	 */
	public Statistics getAttenuationInfo(int nEvent) {
		return this.res.trueGetAttenuationInformation(nEvent);
	}
	public Statistics getSplashAmplitudeInfo(int nEvent) {
		return this.res.trueGetSplashAmplitudeInformation(nEvent);
	}
	public Statistics getAmplitudeInfo(int nEvent) {
		return this.res.trueGetAmplitudeInformation(nEvent);
	}
	public Statistics getEnergyLossInfo(int nEvent) {
		return this.res.trueGetEnergyLossInformation(nEvent);
	}
	public Statistics getReflectanceInfo(int nEvent) {
		return this.res.trueGetReflectanceInformation(nEvent);
	}

	/**
	 * Рассчитать предсказанную р/г на заданную дату
	 */
	public double[] getPredictedReflectogram(long date) {
		 return new ReflectogrammPredictor(date,
				 this.res).getPredictedReflectogramm();
	}
}
