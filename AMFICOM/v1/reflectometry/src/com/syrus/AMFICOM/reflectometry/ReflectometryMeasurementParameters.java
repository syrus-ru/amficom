/*-
 * $Id: ReflectometryMeasurementParameters.java,v 1.2.2.1 2006/04/10 13:06:34 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.reflectometry;

/**
 * ��������� ��������� � ��������������
 * @author $Author: saa $
 * @version $Revision: 1.2.2.1 $, $Date: 2006/04/10 13:06:34 $
 * @module
 */
public interface ReflectometryMeasurementParameters {

	/**
	 * ����������, ������� �� ����� gain splice
	 * @return true, ���� ������� ����� gain splice
	 */
	boolean hasGainSplice();

	/**
	 * ����������, ������� �� ����� �������� ����������
	 * @return true, ���� ������� ����� �������� ����������
	 */
	boolean hasHighResolution();

	/**
	 * ����������, ������� �� �������� ���������� �������
	 * @return true, ���� ������� �������� ���������� �������
	 */
	boolean hasLiveFiberDetection();

	/**
	 * ����������	����� ����������
	 * @return		����� ����������
	 */
	int getNumberOfAverages();

	/**
	 * ����������	������������ ��������, ��
	 * @return		������������ ��������, ��
	 */
	int getPulseWidthNs();

	/**
	 * ����������	���������� �����������
	 * @return		���������� �����������
	 */
	double getRefractionIndex();

	/**
	 * ����������	����������, �����
	 * @return		����������, �����
	 */
	double getResolution();

	/**
	 * ����������	��������� ������������, ��
	 * @return 		��������� ������������, ��
	 */
	double getTraceLength();

	/**
	 * ���������� 	����� �����, �����
	 * @return 		����� �����, �����
	 */
	int getWavelength();

}