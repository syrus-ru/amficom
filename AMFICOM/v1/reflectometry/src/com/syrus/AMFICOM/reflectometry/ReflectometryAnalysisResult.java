/*-
 * $Id: ReflectometryAnalysisResult.java,v 1.1 2005/10/10 10:16:24 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.reflectometry;

/**
 * @author saa
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2005/10/10 10:16:24 $
 * @module reflectometry
 */
public interface ReflectometryAnalysisResult {
	/**
	 * ���������� byte[]-������������� ��� AnalysisResult �� dadara.
	 * @return byte[]-������������� ��� AnalysisResult �� dadara;
	 *   null, ���� ������� �� ����.
	 */
	byte[] getDadaraAnalysisResultBytes();
	/**
	 * ���������� byte[]-������������� ��� ReflectogramMismatchImpl[] �� dadara.
	 * @return byte[]-������������� ��� ReflectogramMismatchImpl[] �� dadara;
	 *   null, ���� ��������� �� ����.
	 */
	byte[] getDadaraReflectogramMismatchBytes();
	/**
	 * ���������� byte[]-����� ��� EvaluationResult �� dadara.
	 * <p>
	 * Note: ��� �������� EvaluationResult ����� ���� ������������
	 * ������ ����� ������, � ����� ������������ ����� D- � Q- ���������.
	 * </p>
	 * @return byte[]-����� ��� EvaluationResult �� dadara;
	 *   null, ���� ��������� �� ����.
	 */
	byte[] getDadaraEvaluationResultBytes();
	/**
	 * ���������� true, ���� D � Q ��������� ��������.
	 * ��������� ����� ���� ���������� ��� ��-�� ����, ��� ���������
	 * �� �����������, ��� � ��-�� ����, ��� ��������� D � Q ����������
	 * �� ������� ��-�� �������� ��������� ��������� �����, ���
	 * �� �����-�� ������ ��������.
	 * @return true, ���� D � Q ��������� ��������.
	 */
	boolean hasDQ();
	/**
	 * ���������� D-�������� ���������� ���������.
	 * @return D-�������� ���������� ���������.
	 * @throws IllegalStateException, ���� <tt>hasDQParameters() == false</tt>.
	 */
	double getD();
	/**
	 * ���������� Q-�������� ���������� ���������.
	 * @return Q-�������� ���������� ���������.
	 * @throws IllegalStateException, ���� <tt>hasDQParameters() == false</tt>.
	 */
	double getQ();
}
