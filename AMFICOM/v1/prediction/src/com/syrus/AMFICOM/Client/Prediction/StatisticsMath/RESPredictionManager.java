/*-
 * $Id: RESPredictionManager.java,v 1.1 2005/12/19 15:36:15 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Prediction.StatisticsMath;

import com.syrus.AMFICOM.measurement.MonitoredElement;

/**
 * ���������� {@link PredictionManager} ����� ������ ������ RE/SRE/REC/RES/RP
 * @author saa
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2005/12/19 15:36:15 $
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
	 * @return ����/����� ������ ��������������
	 * ���� Long.MAX_VALUE, ���� ������������� ���.
	 * ���������, ��� ��� ����� � ��������� �� LowerTime �� UpperTime
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
	 * @return ����/����� ��������� ��������������
	 * ���� 0, ���� ������������� ���.
	 * ���������, ��� ��� ����� � ��������� �� LowerTime �� UpperTime
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
	 * @return LowerTime ��������� "��� �� ��� ������ �������".
	 * ��-��������, ��� getDate() ����� �� ����� ���� ��������.
	 */
	public long getLowerTime() {
		return this.res.getLowerTime();
	}

	/**
	 * @return UpperTime ��������� "��� �� ��� ������ �������".
	 * ��-��������, ��� getDate() ����� �� ����� ���� ��������.
	 */
	public long getUpperTime() {
		return this.res.getUpperTime();
	}

	/**
	 * @return MonitoredElement ��������������� ������ �/�
	 */
	public MonitoredElement getMonitoredElement() {
		return this.res.getMonitoredElement();
	}

	/**
	 * ���������� AttenuationInformation
	 * @param nEvent ����� �������
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
	 * ���������� ������������� �/� �� �������� ����
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
}