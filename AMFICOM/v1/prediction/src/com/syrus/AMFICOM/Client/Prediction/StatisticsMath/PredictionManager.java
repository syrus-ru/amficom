/*-
 * $Id: PredictionManager.java,v 1.1 2005/12/19 11:42:24 saa Exp $
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
 * <p>
 * ��������� �������� ReflectoEventStatistics � ��� ���������, �������
 * � ���� ������� ReflectogrammPredictor.
 * ��� ��, ��� � ReflectoEventStatistics, �������� GUI'�� � Pool'�.
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
	 * ������ � Pool AttenuationInformation
	 * @todo �������� ���������� �� ��������� � Pool
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
	 * ���������� ������������� �/� �� �������� ����
	 */
	public double[] getPredictedReflectogram(long date) {
		 return new ReflectogrammPredictor(date,
				 this.res).getPredictedReflectogramm();
	}
}
