/*-
 * $Id: PredictionManager.java,v 1.6 2006/03/21 11:11:32 saa Exp $
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
 * @version $Revision: 1.6 $, $Date: 2006/03/21 11:11:32 $
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
	 * ���������� AttenuationInformation.
	 * ���������, ���� {@link #hasAttenuationInfo} ���������� true,
	 * �� � � ���� ������ ��� ��������, ��� ��������� ��� ����� ������.
	 * @param nEvent ����� �������
	 * @return AttenuationInformation
	 */
	Statistics getAttenuationInfo(int nEvent);

	Statistics getSplashAmplitudeInfo(int nEvent);

	Statistics getAmplitudeInfo(int nEvent);

	Statistics getEnergyLossInfo(int nEvent);

	Statistics getReflectanceInfo(int nEvent);

	/**
	 * ���������� ������� AttenuationInformation ��� ���������� �������.
	 * @param nEvent ����� �������
	 * @return true, ���� AttenuationInformation ����������.
	 */
	boolean hasAttenuationInfo(int nEvent);

	boolean hasSplashAmplitudeInfo(int nEvent);

	boolean hasAmplitudeInfo(int nEvent);

	boolean hasEnergyLossInfo(int nEvent);

	boolean hasReflectanceInfo(int nEvent);

	/**
	 * ���������� ������������� �/� �� �������� ����
	 */
	double[] getPredictedReflectogram(long date);

}