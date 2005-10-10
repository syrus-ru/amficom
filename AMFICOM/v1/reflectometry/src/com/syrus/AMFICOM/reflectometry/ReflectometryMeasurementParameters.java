/*-
 * $Id: ReflectometryMeasurementParameters.java,v 1.2 2005/10/10 09:51:22 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.reflectometry;

/**
 * ��������� ��������� � ��������������
 * @author $Author: saa $
 * @version $Revision: 1.2 $, $Date: 2005/10/10 09:51:22 $
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
	int getPulseWidth();

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