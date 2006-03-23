/*-
 * $Id: PredictionManager.java,v 1.10 2006/03/23 09:41:03 saa Exp $
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
 * @version $Revision: 1.10 $, $Date: 2006/03/23 09:41:03 $
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
	boolean hasLossInfo(int nEvent);

	/**
	 * ���������� ���������� �� ������� �� ������ �������
	 * ���������, ���� {@link #hasLossInfo(int)} ���������� true,
	 * �� � � ���� ������ ��� ��������, ��� ��������� ��� ����� ������.
	 * @param nEvent ����� �������
	 * @throws IllegalStateException {@link #hasLossInfo(int)} == false
	 */
	Statistics getLossInfo(int nEvent);

	/**
	 * ���������� ����������� �����������
	 * ���������� �� ��������� �������������� �������� ��� ������� �������
	 * @param nEvent ����� �������
	 * @return true, ���� ���������� �� ��������� �������������� ��������
	 *   ��� ������� ������� ����������
	 */
	boolean hasReflectiveAmplitudeInfo(int nEvent);

	/**
	 * ���������� ���������� �� ��������� �������������� ��������
	 *   �� ������ �������
	 * ���������, ���� {@link #hasReflectiveAmplitudeInfo(int)} ���������� true,
	 * �� � � ���� ������ ��� ��������, ��� ��������� ��� ����� ������.
	 * @param nEvent ����� �������
	 * @throws IllegalStateException {@link #hasReflectiveAmplitudeInfo(int)} == false
	 */
	Statistics getReflectiveAmplitudeInfo(int nEvent);

	/**
	 * ���������� ����������� �����������
	 * ���������� �� ������ ��������� ������� ��� ������� �������
	 * @param nEvent ����� �������
	 * @return true, ���� ���������� �� ������ ��������� �������
	 *   ��� ������� ������� ����������
	 */
	boolean hasY0Info(int nEvent);

	/**
	 * ���������� ���������� �� ������ ��������� ������� �� ������ �������
	 * ���������, ���� {@link #hasY0Info(int)} ���������� true,
	 * �� � � ���� ������ ��� ��������, ��� ��������� ��� ����� ������.
	 * @param nEvent ����� �������
	 * @throws IllegalStateException {@link #hasY0Info(int)} == false
	 */
	Statistics getY0Info(int nEvent);

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