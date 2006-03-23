/*-
 * $Id: PredictionManager.java,v 1.9 2006/03/23 09:37:50 saa Exp $
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
 * @version $Revision: 1.9 $, $Date: 2006/03/23 09:37:50 $
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
	 * ���������, ���� {@link #hasAttenuationInfo(int)} ���������� true,
	 * �� � � ���� ������ ��� ��������, ��� ��������� ��� ����� ������.
	 * @param nEvent ����� �������
	 * @throws IllegalStateException {@link #hasAttenuationInfo(int)} == false
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
	 * ���������, ���� {@link #hasEnergyLossInfo(int)} ���������� true,
	 * �� � � ���� ������ ��� ��������, ��� ��������� ��� ����� ������.
	 * @param nEvent ����� �������
	 * @throws IllegalStateException {@link #hasEnergyLossInfo(int)} == false
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
	 * ���������, ���� {@link #hasSplashAmplitudeInfo(int)} ���������� true,
	 * �� � � ���� ������ ��� ��������, ��� ��������� ��� ����� ������.
	 * @param nEvent ����� �������
	 * @throws IllegalStateException {@link #hasSplashAmplitudeInfo(int)} == false
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
	 * ���������, ���� {@link #hasAmplitudeInfo(int)} ���������� true,
	 * �� � � ���� ������ ��� ��������, ��� ��������� ��� ����� ������.
	 * @param nEvent ����� �������
	 * @throws IllegalStateException {@link #hasAmplitudeInfo(int)} == false
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
	 * ���������, ���� {@link #hasReflectanceInfo(int)} ���������� true,
	 * �� � � ���� ������ ��� ��������, ��� ��������� ��� ����� ������.
	 * @param nEvent ����� �������
	 * @throws IllegalStateException {@link #hasReflectanceInfo(int)} == false
	 */
	Statistics getReflectanceInfo(int nEvent);

	/**
	 * ���������� ������������� �/� �� �������� ����
	 */
	double[] getPredictedReflectogram(long date);

}