/*-
 * $Id: PredictionManager.java,v 1.8 2006/03/23 09:27:26 saa Exp $
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
 * @version $Revision: 1.8 $, $Date: 2006/03/23 09:27:26 $
 * @module prediction
 */
public interface PredictionManager {

	/**
	 * @return ����/����� ������ ��������������
	 * ���� Long.MAX_VALUE, ���� ������������� ���.
	 * ���������, ��� ��� ����� � ��������� �� LowerTime �� UpperTime
	 */
	long getMinTime();

	/**
	 * @return ����/����� ��������� ��������������
	 * ���� 0, ���� ������������� ���.
	 * ���������, ��� ��� ����� � ��������� �� LowerTime �� UpperTime
	 */
	long getMaxTime();

	/**
	 * @return LowerTime ��������� "��� �� ��� ������ �������".
	 * ��-��������, ��� getDate() ����� �� ����� ���� ��������.
	 */
	long getLowerTime();

	/**
	 * @return UpperTime ��������� "��� �� ��� ������ �������".
	 * ��-��������, ��� getDate() ����� �� ����� ���� ��������.
	 */
	long getUpperTime();

	/**
	 * @return MonitoredElement ��������������� ������ �/�
	 */
	MonitoredElement getMonitoredElement();

	/**
	 * ���������� ����������� �����������
	 * ���������� �� ��������� ��� ������� ������� (��/��)
	 * @param nEvent ����� �������
	 * @return true, ���� ���������� �� ��������� ��� ������� ������� ����������
	 */
	boolean hasAttenuationInfo(int nEvent);

	/**
	 * ���������� ���������� �� ��������� �� ������ ������� (��/��)
	 * ���������, ���� {@link #hasAttenuationInfo} ���������� true,
	 * �� � � ���� ������ ��� ��������, ��� ��������� ��� ����� ������.
	 * @param nEvent ����� �������
	 */
	Statistics getAttenuationInfo(int nEvent);

	/**
	 * ���������� ����������� �����������
	 * ���������� �� ������� ��� ������� �������
	 * @param nEvent ����� �������
	 * @return true, ���� ���������� �� ������� ��� ������� ������� ����������
	 */
	boolean hasEnergyLossInfo(int nEvent);

	/**
	 * ���������� ���������� �� ������� �� ������ �������
	 * ���������, ���� {@link #hasEnergyLossInfo} ���������� true,
	 * �� � � ���� ������ ��� ��������, ��� ��������� ��� ����� ������.
	 * @param nEvent ����� �������
	 * @throws IllegalStateException {@link #hasEnergyLossInfo} == false
	 */
	Statistics getEnergyLossInfo(int nEvent);

	/**
	 * ���������� ����������� �����������
	 * ���������� �� ��������� �������������� �������� ��� ������� �������
	 * @param nEvent ����� �������
	 * @return true, ���� ���������� �� ��������� �������������� ��������
	 *   ��� ������� ������� ����������
	 */
	boolean hasSplashAmplitudeInfo(int nEvent);

	/**
	 * ���������� ���������� �� ��������� �������������� ��������
	 *   �� ������ �������
	 * ���������, ���� {@link #hasSplashAmplitudeInfo} ���������� true,
	 * �� � � ���� ������ ��� ��������, ��� ��������� ��� ����� ������.
	 * @param nEvent ����� �������
	 * @throws IllegalStateException {@link #hasSplashAmplitudeInfo} == false
	 */
	Statistics getSplashAmplitudeInfo(int nEvent);

	/**
	 * ���������� ����������� �����������
	 * ���������� �� ������ ��������� ������� ��� ������� �������
	 * @param nEvent ����� �������
	 * @return true, ���� ���������� �� ������ ��������� �������
	 *   ��� ������� ������� ����������
	 */
	boolean hasAmplitudeInfo(int nEvent);

	/**
	 * ���������� ���������� �� ������ ��������� ������� �� ������ �������
	 * ���������, ���� {@link #hasAmplitudeInfo} ���������� true,
	 * �� � � ���� ������ ��� ��������, ��� ��������� ��� ����� ������.
	 * @param nEvent ����� �������
	 * @throws IllegalStateException {@link #hasAmplitudeInfo} == false
	 */
	Statistics getAmplitudeInfo(int nEvent);

	/**
	 * ���������� ����������� �����������
	 * ���������� �� ������������ ��������� ��� ������� �������
	 * @param nEvent ����� �������
	 * @return true, ���� ���������� �� ������������ ���������
	 * ��� ������� ������� ����������
	 */
	boolean hasReflectanceInfo(int nEvent);

	/**
	 * ���������� ���������� �� ������������ ��������� �� ������ �������
	 * ���������, ���� {@link #hasReflectanceInfo} ���������� true,
	 * �� � � ���� ������ ��� ��������, ��� ��������� ��� ����� ������.
	 * @param nEvent ����� �������
	 * @throws IllegalStateException {@link #hasReflectanceInfo} == false
	 */
	Statistics getReflectanceInfo(int nEvent);

	/**
	 * ���������� ������������� �/� �� �������� ����
	 */
	double[] getPredictedReflectogram(long date);

}