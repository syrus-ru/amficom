/*-
 * $Id: ReflectometryEvaluationOverallResult.java,v 1.1 2005/10/11 13:22:40 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.reflectometry;

/**
 * ��������� ��� ������
 * ������ D � Q ��������� (��� ���� �������������) ���������� ���������.
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2005/10/11 13:22:40 $
 * @module reflectometry
 */
public interface ReflectometryEvaluationOverallResult {
	/**
	 * ���������� true, ���� D � Q ��������� ��������.
	 * ������������� ���������� �������������, ��� �� ���� ����������
	 * ����������.
	 * @return true, ���� D � Q ��������� ��������.
	 */
	boolean hasDQ();
	/**
	 * ���������� D-�������� ���������� ���������.
	 * @return D-�������� ���������� ���������.
	 * @throws IllegalStateException, ���� <tt>hasDQ() == false</tt>.
	 */
	double getD();
	/**
	 * ���������� Q-�������� ���������� ���������.
	 * @return Q-�������� ���������� ���������.
	 * @throws IllegalStateException, ���� <tt>hasDQ() == false</tt>.
	 */
	double getQ();
}
