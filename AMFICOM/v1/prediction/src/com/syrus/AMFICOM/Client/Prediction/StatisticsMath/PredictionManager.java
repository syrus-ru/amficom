/*-
 * $Id: PredictionManager.java,v 1.2 2005/12/19 14:53:48 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Prediction.StatisticsMath;

import com.syrus.AMFICOM.measurement.MonitoredElement;

/**
 * �������� �������������� ����� prediction �� GUI � Pool'�.
 * ������������ ��� ����������� ����������� �������� ������� ���������������
 * ������������ �� ����� ���������� � ����� GUI.
 * �� ��������� � Pool'�.
 * <p>
 * ��������� �������� ReflectoEventStatistics � ��� ���������, �������
 * � ���� ������� ReflectogrammPredictor.
 * </p>
 * @author saa
 * @author $Author: saa $
 * @version $Revision: 1.2 $, $Date: 2005/12/19 14:53:48 $
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
	 * @return ����� �������������
	 */
	public int getNTraces() {
		return this.res.statData.length;
	}

	/**
	 * @param nTrace ����� ��������������
	 * @return ����/����� ��������������
	 */
	public long getDate(int nTrace) {
		return this.res.statData[nTrace].date;
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
}
