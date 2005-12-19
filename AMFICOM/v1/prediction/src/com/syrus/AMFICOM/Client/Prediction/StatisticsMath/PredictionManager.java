/*-
 * $Id: PredictionManager.java,v 1.5 2005/12/19 15:36:15 saa Exp $
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
 * @version $Revision: 1.5 $, $Date: 2005/12/19 15:36:15 $
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
	 * ���������� AttenuationInformation
	 * @param nEvent ����� �������
	 * @return AttenuationInformation
	 */
	Statistics getAttenuationInfo(int nEvent);

	Statistics getSplashAmplitudeInfo(int nEvent);

	Statistics getAmplitudeInfo(int nEvent);

	Statistics getEnergyLossInfo(int nEvent);

	Statistics getReflectanceInfo(int nEvent);

	/**
	 * ���������� ������������� �/� �� �������� ����
	 */
	double[] getPredictedReflectogram(long date);

}