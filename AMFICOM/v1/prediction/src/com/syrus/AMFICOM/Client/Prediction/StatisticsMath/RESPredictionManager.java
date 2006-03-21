/*-
 * $Id: RESPredictionManager.java,v 1.2 2006/03/21 11:11:32 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Prediction.StatisticsMath;

import com.syrus.AMFICOM.measurement.MonitoredElement;

/**
 * Реализация {@link PredictionManager} через старые классы RE/SRE/REC/RES/RP
 * @author saa
 * @author $Author: saa $
 * @version $Revision: 1.2 $, $Date: 2006/03/21 11:11:32 $
 * @module prediction
 */
public class RESPredictionManager implements PredictionManager {
	private ReflectoEventStatistics res;

	public RESPredictionManager(ReflectoEventContainer []statData,
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

	private int getNTraces() {
        return this.res.statData.length;
	}
	private long getDate(int nTrace) {
        return this.res.statData[nTrace].date;
	}

	/**
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#hasAttenuationInfo(int)
	 */
	public boolean hasAttenuationInfo(int nEvent) {
		return true;
	}

	/**
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#hasSplashAmplitudeInfo(int)
	 */
	public boolean hasSplashAmplitudeInfo(int nEvent) {
		return true;
	}

	/**
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#hasAmplitudeInfo(int)
	 */
	public boolean hasAmplitudeInfo(int nEvent) {
		return true;
	}

	/**
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#hasEnergyLossInfo(int)
	 */
	public boolean hasEnergyLossInfo(int nEvent) {
		return true;
	}

	/**
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#hasReflectanceInfo(int)
	 */
	public boolean hasReflectanceInfo(int nEvent) {
		return true;
	}
}
